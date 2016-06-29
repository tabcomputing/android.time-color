package tabcomputing.wallpaper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextPaint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import tabcomputing.library.clock.ColorWheel;
import tabcomputing.library.clock.TimeSystem;

/**
 *
 */
public class ClockView extends SurfaceView {

    public ClockView(Context context) {
        super(context, null);

        setupClock();
    }

    private boolean mAttached;

    private final Handler handler = new Handler();

    private TimeSystem timeSystem;

    //private Settings settings;
    private static ClockSettings settings = ClockSettings.getInstance();

    // shared paint instance
    private Paint paint = new Paint();

    //
    private Typeface font = Typeface.DEFAULT;

    //
    private ColorWheel colorWheel;

    //
    private Runnable drawRunner = new Runnable() {
        public void run() {
            draw();
        }
    };

    //@Override
    //public void onDestroy() {
    //    super.onDestroy();
    //   handler.removeCallbacks(drawRunner);
    //}

    //@Override
    //protected void onVisibilityChanged(boolean visible) {
    //    this.visible = visible;
    //    if (visible) {
    //        handler.post(drawRunner);
    //    } else {
    //       handler.removeCallbacks(drawRunner);
    //    }
    //}

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
        }
        this.visible = true;
        if (visible) {
            handler.post(drawRunner);
        //    } else {
        //       handler.removeCallbacks(drawRunner);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            handler.removeCallbacks(drawRunner);
            mAttached = false;
        }
    }

    private void onTimeChanged() {
        //Calendar calendar = Calendar.getInstance();
        //calendar.setToNow();

        //int hour = mCalendar.hour;
        //int minute = mCalendar.minute;
        //int second = mCalendar.second;

        //mMinutes = minute + second / 60.0f;
        //mHour = hour + mMinutes / 60.0f;
        mChanged = true;
    }

    private boolean mChanged = false;
    private boolean visible = true;

    /**
     * Return the time to wait between screen refreshes.
     *
     * @return      duration in milliseconds
     */

    private int frameDuration() {
        return timeSystem.tickTime(true); //settings.displaySeconds());
    }

    private Canvas canvas;

    /**
     * Draw routine.
     */
    private void draw() {
        SurfaceHolder holder = getHolder(); //getSurfaceHolder();
        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                drawClock(canvas);
            }
        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }

        handler.removeCallbacks(drawRunner);

        if (visible) {
            handler.postDelayed(drawRunner, frameDuration());
        }
    }

    private Rect bounds = new Rect();

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawClock(canvas);
    }

    /**
     *
     * @param canvas
     */
    private void drawClock(Canvas canvas) {
        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }

        canvas.getClipBounds(bounds);

        canvas.drawColor(Color.WHITE);

        switch(settings.getClockType()) {
            case 1:
                drawTime(canvas, bounds);
                break;
            default:
                drawColorWheel(canvas, bounds);
                drawNumbers(canvas, bounds);
                drawTicks(canvas, bounds);
                drawClockHands(canvas, bounds);
                break;
        }
    }

    /**
     *
     */
    public void setupClock() {
        //font = settings.getTypeface();
        timeSystem = settings.getTimeSystem();
        colorWheel = settings.getColorWheel();
    }

    /**
     * Draw time in the center of the clock face area.
     */
    private void drawTime(Canvas canvas, Rect bounds) {
        Rect d;

        //String stamp = timeStamp();
        String[] time = timeSystem.timeRebased();

        int[] colors = timeSystem.timeColors(colorWheel);

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        float off = bounds.width() / (time.length + 2);

        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);

        Paint digitalPaint = new TextPaint();
        digitalPaint.setAntiAlias(true);
        digitalPaint.setColor(Color.WHITE);
        digitalPaint.setTextSize(120.0f);
        digitalPaint.setStrokeWidth(3.0f);
        digitalPaint.setStyle(Paint.Style.FILL);
        digitalPaint.setTypeface(font);
        digitalPaint.setTextAlign(Paint.Align.CENTER);
        //digitalPaint.setShadowLayer(3.0f, -3.0f, -3.0f, Color.BLACK);

        //setTextSizeForWidth(digitalPaint, x * 0.8f, sampleTime());
        setTextSizeForWidth(digitalPaint, off * 0.75f, "44");

        d = textDimensions("44", digitalPaint);

        //canvas.drawText(stamp, x, y + (d.height() / 2f), digitalPaint);

        float y = cy + (d.height() / 2f);
        float x = off * 1.5f;

        for(int i=0; i < time.length; i++) {
            circlePaint.setColor(colors[i]);
            canvas.drawCircle(x, cy, off/2.0f, circlePaint);
            canvas.drawText(time[i], x, y, digitalPaint);
            x = x + off;
        }

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

    /**
     * Draw the numbers of the clock face.
     */
    private void drawNumbers(Canvas canvas, Rect bounds) {
        int i;
        double r;
        float x,y;

        float radius = spotRadius(bounds);
        float textWidth = faceRadius(bounds) - radius;

        int hc = timeSystem.hoursOnClock();

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        // TODO: should we reuse a shared paint instance to reduce memory footprint?
        paint = new Paint();
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

        //if (settings.isClockNumbered()) {
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
        //}
    }

    /**
     * Draw the minute/second dashes of the clock face.
     */
    private void drawTicks(Canvas canvas, Rect bounds) {
        double r;
        int c;
        float x0, y0, x1, y1;

        float length = 12.0f;

        float w  = faceRadius(bounds);
        //float r0 = spotRadius();
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
        //if (!settings.hasClockHands()) {
        //    return;
        //}

        float l;  // length of hand
        float x, y;

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        float w  = faceRadius(bounds);
        float r0 = spotRadius(bounds);
        float r1 = w - r0;

        double[] r = timeSystem.handRatios();

        int s = r.length;

        if (!settings.displaySeconds()) {
            s = s - 1;
        }

        l = ((r0 - r1) * 0.90f);

        paint = new Paint();
        paint.setStrokeWidth(20.0f);
        paint.setColor(Color.LTGRAY);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setShadowLayer(15.0f, 5.0f, 5.0f, Color.BLACK);

        for(int i=0; i < s; i++) {
            //0.45f + (i * 0.5f / (r.length - 1));

            x = (float) (cx + l * sin(r[i] + rot()));
            y = (float) (cy - l * cos(r[i] + rot()));

            // second hand
            if (i == r.length - 1) {
                paint.setStrokeWidth(10.0f);
            }
            canvas.drawLine(cx, cy, x, y, paint);

            l = (r0 + r1) * 0.9f;
        }
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
     * Clock hand colors.
     *
     * @return      array of color integers
     */
    private int[] clockColors() {
        int[] colors = timeSystem.clockColors(colorWheel);

        //if (isDynamic()) {
        //    colors = dynamic(colors);
        //}

        return colors;
    }

    /**
     * Center X coordinate.
     *
     * @return      horizontal origin
     */
    private float centerX(Rect bounds) {
        return bounds.width() / 2;
    }

    /**
     * The center Y coordinate is raised up a bit b/c android phones always have
     * a set of buttons at the bottom of the screen.
     *
     * @return      vertical origin
     */
    private float centerY(Rect bounds) {
        return bounds.height() / 2;
    }

    /**
     * @return  maximum radius of the clock face
     */
    private float faceRadius(Rect bounds) {
        return centerX(bounds) * 0.85f;
    }

    /**
     *
     * @return
     */
    private float spotRadius(Rect bounds) {
        return spotRadius(bounds, timeSystem.hoursOnClock());
    }

    /**
     *
     * @param h     number of divisions
     * @return      radius of central circle
     */
    private float spotRadius(Rect bounds, float h) {
        float w = faceRadius(bounds);
        return (float) (w / (1.0f + (2.0f * Math.sin(Math.PI / (2.0f * h)))));
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

}
