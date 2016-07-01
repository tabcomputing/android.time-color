package com.tabcomputing.chronochrome.watch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.TextPaint;
import android.text.format.Time;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import java.lang.ref.WeakReference;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.color.ColorWheel;

/**
 * Digital watch face with seconds. In ambient mode, the seconds aren't displayed. On devices with
 * low-bit ambient mode, the text is drawn without anti-aliasing in ambient mode.
 */
public class WatchFace extends CanvasWatchFaceService {
    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    private Typeface font = NORMAL_TYPEFACE;

    /**
     * Update rate in milliseconds for interactive mode. We update once a second since seconds are
     * displayed in interactive mode.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private static class EngineHandler extends Handler {
        private final WeakReference<WatchFace.Engine> mWeakReference;

        public EngineHandler(WatchFace.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            WatchFace.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }

    private class Engine extends CanvasWatchFaceService.Engine {
        final Handler mUpdateTimeHandler = new EngineHandler(this);
        boolean mRegisteredTimeZoneReceiver = false;
        Paint mBackgroundPaint;
        Paint mTextPaint;
        boolean mAmbient;
        Time mTime;
        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTime.clear(intent.getStringExtra("time-zone"));
                mTime.setToNow();
            }
        };
        int mTapCount;

        float mXOffset;
        float mYOffset;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        boolean mLowBitAmbient;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(WatchFace.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .setAcceptsTapEvents(true)
                    .build());
            Resources resources = WatchFace.this.getResources();
            mYOffset = resources.getDimension(R.dimen.digital_y_offset);

            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(resources.getColor(R.color.background));

            mTextPaint = new Paint();
            mTextPaint = createTextPaint(resources.getColor(R.color.digital_text));

            mTime = new Time();
        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }

        private Paint createTextPaint(int textColor) {
            Paint paint = new Paint();
            paint.setColor(textColor);
            paint.setTypeface(NORMAL_TYPEFACE);
            paint.setAntiAlias(true);
            return paint;
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                mTime.clear(TimeZone.getDefault().getID());
                mTime.setToNow();
            } else {
                unregisterReceiver();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            WatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            WatchFace.this.unregisterReceiver(mTimeZoneReceiver);
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);

            // Load resources that have alternate values for round watches.
            Resources resources = WatchFace.this.getResources();
            boolean isRound = insets.isRound();
            mXOffset = resources.getDimension(isRound
                    ? R.dimen.digital_x_offset_round : R.dimen.digital_x_offset);
            float textSize = resources.getDimension(isRound
                    ? R.dimen.digital_text_size_round : R.dimen.digital_text_size);

            mTextPaint.setTextSize(textSize);
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (mAmbient != inAmbientMode) {
                mAmbient = inAmbientMode;
                if (mLowBitAmbient) {
                    mTextPaint.setAntiAlias(!inAmbientMode);
                }
                invalidate();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        /**
         * Captures tap event (and tap type) and toggles the background color if the user finishes
         * a tap.
         */
        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            Resources resources = WatchFace.this.getResources();
            switch (tapType) {
                case TAP_TYPE_TOUCH:
                    // The user has started touching the screen.
                    break;
                case TAP_TYPE_TOUCH_CANCEL:
                    // The user has started a different gesture or otherwise cancelled the tap.
                    break;
                case TAP_TYPE_TAP:
                    // The user has completed the tap gesture.
                    mTapCount++;
                    mBackgroundPaint.setColor(resources.getColor(mTapCount % 2 == 0 ?
                            R.color.background : R.color.background2));
                    break;
            }
            invalidate();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            // Draw the background.
            if (isInAmbientMode()) {
                canvas.drawColor(Color.BLACK);
            } else {
                canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundPaint);
            }

            // Draw H:MM in ambient mode or H:MM:SS in interactive mode.
            mTime.setToNow();
            String text = mAmbient
                    ? String.format("%d:%02d", mTime.hour, mTime.minute)
                    : String.format("%d:%02d:%02d", mTime.hour, mTime.minute, mTime.second);
            canvas.drawText(text, mXOffset, mYOffset, mTextPaint);
        }

        /**
         * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage() {
            invalidate();
            if (shouldTimerBeRunning()) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS
                        - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }



        // All my crap ...

        private ColorWheel colorWheel;
        private TimeSystem timeSystem;
        private AbstractPattern pattern;
        private Settings settings;

        /**
         * Draw the wallpaper.
         */
        private void drawChronochrome(Canvas canvas, Rect bounds) {
            //canvas.save();

            //log(timeStamp());

            //drawPattern();
            //drawFlare();
            drawClockface(canvas, bounds);

            //canvas.restore();
        }

        private void drawClockface(Canvas canvas, Rect bounds) {
            if (settings.hasColorWheel()) {
                drawColorWheel(canvas, bounds);
            }

            if (settings.hasClockDashes()) {
                drawDashes(canvas, bounds);
            }

            if (settings.isClockNumbered()) {
                drawNumbers(canvas, bounds);
            }

            if (settings.displayTime()) {
                drawTime(canvas, bounds);
            }

            drawClockHands(canvas, bounds);
        }

        /**
         *
         *
         */
        private void drawRainbowWheel(Canvas canvas, Rect bounds) {
            float cx = bounds.width() / 2;  //centerX();
            float cy = bounds.height() / 2; //centerY();

            int h = timeSystem.hoursOnClock();

            float w = spotRadius(bounds, 1.5f * timeSystem.hoursOnClock()) + faceRadius(bounds) * 0.05f;
            //float w = faceRadius() * 0.95f;

            //float r0 = (float) (w / (1.0f + 2.0f * Math.sin(Math.PI / (2.0f * h))));
            //float r1 = w - r0;

            int segments = 16;
            int[] colors = new int[segments+1];
            //float[] positions = new float[segments+1];

            double ratio;

            for (int i = 0; i <= segments; i++) {
                ratio = ((float) i) / segments;
                colors[i] = 0; //ratioToColor(colorCorrect(ratio));  // FIXME
                //positions[i] = ratio;
            }
            SweepGradient wheel = new SweepGradient(cx, cy, colors, null); //positions);

            float rotate = 270f;
            Matrix gradientMatrix = new Matrix();
            gradientMatrix.preRotate(rotate, cx, cy);
            wheel.setLocalMatrix(gradientMatrix);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(1);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.FILL);
            paint.setShader(wheel);
            canvas.drawCircle(cx, cy, w, paint);
        }

        // Reusable paint instance.
        private Paint paint = new Paint();

        /**
         * @return  maximum radius of the clock face
         */
        private float faceRadius(Rect bounds) {
            return (bounds.width() / 2) * 0.85f;
        }

        private float spotRadius(Rect bounds) {
            return spotRadius(bounds, hoursOnClock());
        }

        private float spotRadius(Rect bounds, float h) {
            float w = faceRadius(bounds);
            return (float) (w / (1.0f + (2.0f * Math.sin(Math.PI / (2.0f * h)))));
        }

        private float centerX(Rect bounds) {
            return bounds.width() / 2;
        }

        private float centerY(Rect bounds) {
            return bounds.height() / 2;
        }

        private int hoursOnClock() {
            return timeSystem.hoursOnClock();
        }


        /**
         * The size of the wheel is calculated very carefully. We know the total width of area
         * in which the wheel must reside (w). We then must calculate the radius of the main
         * circle on which the small circles are centered. This formula derives from the chord
         * equation for a circle (c = 2R sin(a/2)).
         *
         */
        private void drawColorWheel(Canvas canvas, Rect bounds) {
            float cx = centerX(bounds);
            float cy = centerY(bounds);

            int hd = timeSystem.hoursInDay();
            int hc = timeSystem.hoursOnClock();
            //int mc = timeSystem.minutesOnClock();

            int half = hd / 2;
            int qtr  = hd / 4;

            float w  = faceRadius(bounds);

            // TODO: spare option?
            //float r0 = sparse ? spotRadius(1.5f * hc) : spotRadius();
            float r0 = spotRadius(bounds);
            float r1 = w - r0;
            //float r2 = r0 + (r1 * 1.15f);

            float x, y;
            double r;

            int i;
            int[] colors;

            colors = clockColors();
            //colors = hourColors();

            paint = new Paint();
            paint.setAntiAlias(false);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(10.0f);
            paint.setStyle(Paint.Style.FILL);
            paint.clearShadowLayer();

            // draw hours
            for (i = 0; i < hc; i++) {
                r = ((double) i) / hc;

                //c = ratioToColor(colorCorrect(r));
                paint.setColor(colors[i]);

                x = (float) (cx + r0 * sin(r + rot()));
                y = (float) (cy - r0 * cos(r + rot()));

                //circlePaint.setShadowLayer(3.0f, 0f, 0f, c);
                canvas.drawCircle(x, y, r1, paint);

                //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
                //y = (float) (cy - r0 * cos(r + rot() - 0.041666667));
            }

/*
            if (isDaySplit() && !isDuplexed()) {
                // draw hours
                for (i = 0; i < hc; i++) {
                    r = ((double) i) / hc;

                    //c = ratioToColor(colorCorrect(r));
                    circlePaint.setColor(colors[i + half]);

                    x = (float) (cx + r0 * sin(r + rot()));
                    y = (float) (cy - r0 * cos(r + rot()));

                    //circlePaint.setShadowLayer(3.0f, 0f, 0f, c);
                    canvas.drawCircle(x, y, r1 / 2, circlePaint);

                    //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
                    //y = (float) (cy - r0 * cos(r + rot() - 0.041666667)); //Math.sqrt(radius*radius + x*x);
                }
            }
*/
/*
            if (isDaySplit() && !isDuplexed()) {
                int[] hourColors = colorWheel.colors(hoursInDay());

                int h = (int) (timeRatios()[0] * hd);
                i = mod(h - (hc / 2), hc);

                int q = half + mod(h + qtr, hc);

                // TODO: need to get the color from the full specturm of hour colors
                //int[] c = {hourColors[mod(i + half, hd)], colors[i]};
                //int[] c = {colors[i], colors[mod(i - 1, hc)]};
                int[] c = {colors[i], hourColors[q]};

                if (h < hc - qtr || h >= hc + qtr) {
                    reverseArray(c);
                }

                r = (double) i / hc;

                //r = ((double) h) / hc;

                x = (float) (cx + r0 * sin(r + rot()));
                y = (float) (cy - r0 * cos(r + rot()));

                //float x1 = (float) (cx + (r0 - r0) * sin(r + rot()));
                //float y1 = (float) (cy - (r0 - r0) * cos(r + rot()));

                //float x2 = (float) (cx + (r0 + r0) * sin(r + rot()));
                //float y2 = (float) (cy - (r0 + r0) * cos(r + rot()));

                //int a = (int) (180 / Math.PI * Math.atan2(y2 - y1, x2 - x1));

                float a = (float) r * 360 - 90;

                drawSemiCircles(c, x, y, r1, a);
            }
            */
        }

        private void drawColorWheelSplit(Canvas canvas, Rect bounds) {
            float cx = centerX(bounds);
            float cy = centerY(bounds);

            int hd = timeSystem.hoursInDay();
            int hc = timeSystem.hoursOnClock();

            int half = hd / 2;
            int qtr = hd / 4;

            float w = faceRadius(bounds);

            // TODO: spare option?
            //float r0 = sparse ? spotRadius(1.5f * hc) : spotRadius();
            float r0 = spotRadius(bounds);
            float r1 = w - r0;
            //float r2 = r0 + (r1 * 1.15f);

            float x, y;
            double r;

            int i, j, s;
            int[] colors;

            colors = clockColors();

            int h = (int) (timeSystem.timeRatios()[0] * hd);

            if (h < hc - qtr || h >= hc + qtr) {
                s = half;
            } else {
                s = 0;
            }

            int q = half + mod(h + qtr, hc);

            Paint circlePaint = new Paint();
            circlePaint.setAntiAlias(false);
            circlePaint.setColor(Color.WHITE);
            circlePaint.setStrokeWidth(10.0f);
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.clearShadowLayer();

            //log("Q: " + q);

            for (i = 0; i < q; i++) {
                j = mod(i + s, hd);

                r = ((double) j) / hc;

                x = (float) (cx + r0 * sin(r + rot()));
                y = (float) (cy - r0 * cos(r + rot()));

                circlePaint.setColor(colors[j]);

                //circlePaint.setShadowLayer(3.0f, 0f, 0f, c);
                canvas.drawCircle(x, y, r1, circlePaint);
            }

        }

        /**
         *
         * @param c     colors
         * @param x     center x
         * @param y     center y
         * @param r     radius
         * @param a     start angle
         */
        private void drawSemiCircles(Canvas canvas, Rect bounds, int[] c, float x, float y, float r, float a) {
            float cx = centerX(bounds);
            float cy = centerY(bounds);

            int hd = timeSystem.hoursInDay();
            int hc = timeSystem.hoursOnClock();

            int i;

            int h = (int) (timeSystem.timeRatios()[0] * hd);

            i = mod(h - (hc / 2), hc);

            final RectF oval = new RectF();
            oval.set(x - r, y - r, x + r, y + r);

            Paint circlePaint = new Paint();
            circlePaint.setAntiAlias(false);
            circlePaint.setStrokeWidth(10.0f);
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.clearShadowLayer();

            circlePaint.setColor(c[0]);
            canvas.drawArc(oval, a, 180, true, circlePaint);

            circlePaint.setColor(c[1]);
            canvas.drawArc(oval, a, -180, true, circlePaint);
        }

        /**
         *
         * @param c     colors
         * @param x     center x
         * @param y     center y
         * @param r     radius
         * @param a     start angle
         */
        private void drawSemiGradients(Canvas canvas, Rect bounds, int[] c, float x, float y, float r, float a) {
            float cx = centerX(bounds);
            float cy = centerY(bounds);

            float r0 = spotRadius(bounds);

            // TODO: simplify this calc?
            //int h = (int) (timeRatios()[0] * hd);

            //i = mod(h - (hc / 2), hc);
            //r = (double) i / hc;

            //x = (float) (cx + r0 * sin(r + rot()));
            //y = (float) (cy - r0 * cos(r + rot()));

            float x1 = x - (r * (cy - y) / r0);
            float y1 = y - (r * (x - cx) / r0);

            float x2 = x + (r * (cy - y) / r0);
            float y2 = y + (r * (x - cx) / r0);

            //int[] c = { colors[mod(i + 12, hd)], colors[i] };

            //if (h < hc - 6 || h > hc + 6) {
            //    reverseArray(c);
            //}

            LinearGradient shader = new LinearGradient(x1, y1, x2, y2, c, null, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setShader(shader);

            //circlePaint.setColor(Color.WHITE); //colors[i]);

            canvas.drawCircle(x, y, r, paint); //circlePaint);
        }

        /**
         * Get a string representation of the time.
         *
         * @return string of time
         */
        private String timeStamp() {
            return timeSystem.timeStamp(settings.displaySeconds());
        }

        /**
         * When displaying the time we size the font to fit a specific area of the display.
         * In order to keep text from shifting noticeably as the time changes, we set the
         * font size using a sample text representative of the time to be displayed, rather
         * then the actual time.
         *
         * The letter A and number 4 are used because they tends to be the widest in print.
         *
         * @return      sample time
         */
        private String sampleTime() {
            return timeSystem.timeStampSample(settings.displaySeconds());
        }

        /**
         * Draw time in the center of the clock face area.
         */
        private void drawTime(Canvas canvas, Rect bounds) {
            Rect d;

            String stamp = timeStamp();

            float x = centerX(bounds);
            float y = centerY(bounds);

            Paint digitalPaint = new TextPaint();
            digitalPaint.setAntiAlias(true);
            digitalPaint.setColor(Color.WHITE);
            digitalPaint.setTextSize(120.0f);
            digitalPaint.setStrokeWidth(3.0f);
            digitalPaint.setStyle(Paint.Style.FILL);
            digitalPaint.setTypeface(font);
            digitalPaint.setTextAlign(Paint.Align.CENTER);
            //digitalPaint.setShadowLayer(3.0f, -3.0f, -3.0f, Color.BLACK);

            setTextSizeForWidth(digitalPaint, x * 0.8f, sampleTime());

            d = textDimensions(stamp, digitalPaint);

            canvas.drawText(stamp, x, y + (d.height() / 2f), digitalPaint);

            /*
            // every thing after the hour
            String rest = fmtRest();

            float radius1 = canvas.getWidth() / hoursOnClock();
            float radius  = (canvas.getWidth() / 2 - radius1) * 0.9f;

            setTextSizeForWidth(digitalPaint, x / 3f, "XX XX");

            d = textDimensions(hour, digitalPaint);

            if (settings.hasColorWheel()) {
                // place the minutes just below the hour circle
                canvas.drawText(rest, x, y + d.height() + (radius + radius1) * 1.05f, digitalPaint);
            } else {
                // shift the minutes just below the hour
                canvas.drawText(rest, x, y + d.height() * 2.5f, digitalPaint);
            }
            */
        }

        /**
         * Clock hand colors.
         *
         * @return      array of color integers
         */
        private int[] clockColors() {
            int[] colors = timeSystem.clockColors(colorWheel);

            if (settings.hasDynamicColor()) {
                colors = dynamic(colors);
            }

            return colors;
        }

        /**
         * Draw the numbers of the clock face.
         */
        private void drawNumbers(Canvas canvas, Rect bounds) {
            int i;
            double r;
            float x,y;

            float cx = centerX(bounds);
            float cy = centerY(bounds);

            float radius = spotRadius(bounds);
            float textWidth = faceRadius(bounds) - radius;

            int hc = hoursOnClock();

            // TODO: should we reuse a shared paint instance to reduce memory footprint?
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(10.0f);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(5.0f, 1.0f, 1.0f, Color.LTGRAY);
            paint.setTypeface(font);
            paint.setTextSize(100.0f);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(font);

            if (settings.isClockNumbered()) {
                paint.setColor(Color.WHITE);

                setTextSizeForWidth(paint, textWidth, "X4");

                float th = textHeight("X4", paint) / 2f;

                String[] nums = timeSystem.clockNumbers(settings.getNumberSystem());

                for (i = 0; i < hc; i++) {
                    r = ((double) i) / hc;
                    x = (float) (cx + radius * sin(r + rot()));
                    y = (float) (cy - radius * cos(r + rot()));
                    canvas.drawText(nums[i], x, y + th, paint);
                }
            }
        }

        /**
         * Draw the minute/second dashes of the clock face.
         */
        private void drawDashes(Canvas canvas, Rect bounds) {
            double r;
            int c;
            float x0, y0, x1, y1;

            float length = 12.0f;

            float w  = faceRadius(bounds);
            //float r0 = spotRadius(bounds);
            //float r1 = w - r0;
            float radius = w * 1.05f; //r0 + (r1 * 1.15f);

            float cx = centerX(bounds);
            float cy = centerY(bounds);

            int mc = timeSystem.minutesOnClock();

            Paint tickPaint = new Paint();
            tickPaint.setAntiAlias(true);
            tickPaint.setColor(Color.WHITE);
            tickPaint.setStrokeWidth(10.0f);
            tickPaint.setStrokeCap(Paint.Cap.ROUND);
            tickPaint.setStyle(Paint.Style.FILL);
            tickPaint.setShadowLayer(3.0f, 1.0f, 1.0f, Color.LTGRAY);

            for (int i = 0; i < mc; i++) {
                r = ((double) i) / mc;
                c = colorWheel.color(r);  // TODO: dynamic?

                tickPaint.setColor(c);

                x0 = (float) (cx + radius * sin(r + rot()));
                y0 = (float) (cy - radius * cos(r + rot()));

                x1 = (float) (cx + (radius + length) * sin(r + rot()));
                y1 = (float) (cy - (radius + length) * cos(r + rot()));

                canvas.drawLine(x0, y0, x1, y1, tickPaint);
            }
        }

        /**
         * Draw clock hands.
         */
        private void drawClockHands(Canvas canvas, Rect bounds) {
            if (!settings.hasClockHands()) {
                return;
            }

            float l;  // length of hand
            float x, y;

            float cx = centerX(bounds);
            float cy = centerY(bounds);

            float w  = faceRadius(bounds);
            float r0 = spotRadius(bounds);
            float r1 = w - r0;

            double[] r = timeSystem.handRatios(); //handRatios();

            int s = r.length;

            if (!settings.displaySeconds()) {
                s = s - 1;
            }

            l = ((r0 - r1) * 0.90f);

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStrokeCap(Paint.Cap.ROUND);

            for(int i=0; i < s; i++) {
                //0.45f + (i * 0.5f / (r.length - 1));

                x = (float) (cx + l * sin(r[i] + rot()));
                y = (float) (cy - l * cos(r[i] + rot()));

                // second hand
                if (i == r.length - 1) {
                    paint.setStrokeWidth(5.0f);
                }
                canvas.drawLine(cx, cy, x, y, paint);

                l = (r0 + r1) * 0.9f;
            }
        }

        /**
         * Apply daylight dynamic to color.
         *
         * The dynamic ratio adjusts the brightness and saturation in alignment with
         * typical sunlight patterns -- colors are brightest at midday and darkest
         * at midnight; saturation is vibrant at dawn and dusk and muted at midday
         * and midnight.
         *
         * @param color     color integer
         * @return          color integer
         */
        private int dynamic(int color) {
            double dayRatio = timeSystem.timeRatios()[0];
            if (settings.hasDynamicColor()) {
                return colorWheel.daylight(color, dayRatio);
            } else {
                return color;
            }
        }

        /**
         * Apply daylight dynamic to array of colors.
         *
         * @param colors    array of color integers
         * @return          array of color integers
         */
        private int[] dynamic(int[] colors) {
            if (settings.hasDynamicColor()) {
                double dayRatio = timeSystem.timeRatios()[0];
                int[] newColors = new int[colors.length];
                for(int i=0; i < colors.length; i++) {
                    newColors[i] = colorWheel.daylight(colors[i], dayRatio);
                }
                return newColors;
            } else {
                return colors;
            }
        }

        /**
         *
         * @return
         */
        private float maxDimension(Rect bounds) {
            float h = bounds.height();
            float w = bounds.width();
            return (h > w ? h : w);
        }

        /**
         * Given a string of text and a paint instance, determine the area, both width and height,
         * that the text will take up.
         *
         * @return      area as a Rect instance
         */
        private Rect textDimensions(String text, Paint paint) {
            Rect rect = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect);
            return rect;
        }

        /**
         * Given a string of text and a paint instance, determine the amount of screen width that
         * the text will take up.
         *
         * @param text      text to be drawn
         * @param paint     instance of Paint to be used to draw text
         * @return          text width
         */
        private float textWidth(String text, Paint paint) {
            return textDimensions(text, paint).width();
        }

        /**
         *
         * @param text      text to be drawn
         * @param paint     instance of Paint to be used to draw text
         * @return          text height
         */
        private float textHeight(String text, Paint paint) {
            return textDimensions(text, paint).height();
        }

        /**
         * Sets the text size for a Paint object so a given string of text will be a
         * given width.
         *
         * @param paint             instance of Paint to set the text size for
         * @param desiredWidth      desired width
         * @param text              the text that should be that width
         */
        private void setTextSizeForWidth(Paint paint, float desiredWidth, String text) {
            // Pick a reasonably large value for the test. Larger values produce
            // more accurate results, but may cause problems with hardware
            // acceleration. But there are workarounds for that, too; refer to
            // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
            final float testTextSize = 48f;

            // Get the bounds of the text, using our testTextSize.
            paint.setTextSize(testTextSize);
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);

            // Calculate the desired size as a proportion of our testTextSize.
            float desiredTextSize = (testTextSize * desiredWidth / bounds.width()) * 0.90f;

            // Set the paint for that size.
            paint.setTextSize(desiredTextSize);
        }

        /**
         *
         * @return      ratio to rotate
         */
        private double rot() {
            if (settings.rotateTime()) {
                return 0.5; //Math.PI;
            } else {
                return 0;
            }
        }

        private final double TAU = 2 * Math.PI;

        /**
         * Calculate the sine of a given ratio. Unlike Math.sin, this method works with ratios
         * instead of radians, i.e. it multiples the given ratio by 2π before passing it off to
         * Math.sin().
         *
         * @param ratio     value between 0 and 1
         * @return          sin result
         */
        private double sin(double ratio) {
            return Math.sin(TAU * ratio);
        }

        /**
         * Calculate the cosine of a given ratio. Unlike Math.cos, this method works with ratios
         * instead of radians, i.e. it multiples the given ratio by 2π before passing it off to
         * Math.cos().
         *
         * @param ratio     value between 0 and 1
         * @return          sin result
         */
        private double cos(double ratio) {
            return Math.cos(TAU * ratio);
        }

        /**
         * Real modulus.
         *
         * @param n     numerator
         * @param d     denominator
         * @return      modulo
         */
        private int mod(int n, int d) {
            if (n < 0) {
                return (d + (n % d));
            } else {
                return (n % d);
            }
        }

    }
}
