package com.tabcomputing.chronochrome.wallpaper;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
//import android.preference.Preference;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.text.TextPaint;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Calendar;

import tabcomputing.library.clock.ColorWheel;
import tabcomputing.library.clock.MeridiemTime;
import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.wallpaper.AbstractPattern;
import tabcomputing.wallpaper.ClockSettings;

public class Wallpaper extends WallpaperService {
    private static final boolean DEBUG = true;
    private static final String TAG = "Wallpaper";
    private static void log(String message) {
        if (DEBUG) {
            Log.i(TAG, message);
        }
    }

    public static final int TIME_TRADITIONAL = 0;
    public static final int TIME_MILITARY = 1;
    public static final int TIME_HEXADECIMAL = 2;
    public static final int TIME_HEXIMAL = 3;
    public static final int TIME_DECIMAL = 4;
    public static final int TIME_DUODECIMAL = 5;

    @Override
    public WallpaperService.Engine onCreateEngine() {
        return new ChromaTimeWallpaperEngine(); //(movie);
    }

    public class ChromaTimeWallpaperEngine extends WallpaperService.Engine implements SharedPreferences.OnSharedPreferenceChangeListener {
        private final int millisecondsInDay = 24 * 60 * 60 * 1000;

        private SharedPreferences prefs;
        private Handler handler;
        private SurfaceHolder holder;
        private boolean visible;

        //private Rect bounds;

        private Paint paint = new Paint();

        Typeface font = Typeface.SERIF;

        private Canvas canvas = null;

        private TimeSystem timeSystem = new MeridiemTime();

        private ColorWheel colorWheel = new ColorWheel();

        private ClockSettings settings = ClockSettings.getInstance();

        public ChromaTimeWallpaperEngine() {
            prefs = PreferenceManager.getDefaultSharedPreferences(Wallpaper.this);
            prefs.registerOnSharedPreferenceChangeListener(this);

            handler = new Handler();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;

            settings.readPreferences(prefs);

            configure();
        }

        /**
         *
         */
        private void configure() {
            configureTypeface();
            configureTimeSystem();
            //configureColorWheel();
            //configurePattern();
        }

        /**
         *
         */
        private void configureTimeSystem() {
            timeSystem = settings.getTimeSystem();

            configurePattern();
            configureColorWheel();
            configureBaseConversion();
        }

        /**
         *
         */
        private void configurePattern() {
            /*
            switch (settings.getPattern()) {
                case 16:
                    Typeface font = Typeface.createFromAsset(getAssets(), "disco.ttf");
                    pattern = new PatternBigTime(settings, timeSystem, colorWheel, font);
                    break;
                case 15:
                    pattern = new PatternPlaidier(settings, timeSystem, colorWheel);
                    break;
                case 14:
                    pattern = new PatternPlaid(settings, timeSystem, colorWheel);
                    break;
                case 13:
                    pattern = new PatternBinaryStrips(settings, timeSystem, colorWheel);
                    break;
                case 12:
                    pattern = new PatternSquares(settings, timeSystem, colorWheel);
                    break;
                case 11:
                    pattern = new PatternEcho(settings, timeSystem, colorWheel);
                    break;
                case 10:
                    pattern = new PatternCaterpillar(settings, timeSystem, colorWheel);
                    //pattern = new PatternSpiral(settings, timeSystem, colorWheel);
                    break;
                case 9:
                    pattern = new PatternHorizon(settings, timeSystem, colorWheel);
                    break;
                case 8:
                    pattern = new PatternLotus(settings, timeSystem, colorWheel);
                    break;
                case 7:
                    pattern = new PatternRadial(settings, timeSystem, colorWheel);
                    break;
                case 6:
                    pattern = new PatternPieSlice(settings, timeSystem, colorWheel);
                    break;
                case 5:
                    pattern = new PatternRadiantOrbit(settings, timeSystem, colorWheel);
                    break;
                case 4:
                    pattern = new PatternRadiant(settings, timeSystem, colorWheel);
                    break;
                case 3:
                    pattern = new PatternGradientRotating(settings, timeSystem, colorWheel);
                    break;
                case 2:
                    pattern = new PatternGradient(settings, timeSystem, colorWheel);
                    break;
                case 1:
                    pattern = new PatternMondrian(settings, timeSystem, colorWheel);
                    break;
                default:
                    pattern = new PatternSolid(settings, timeSystem, colorWheel);
                    break;
            }
            */
        }

        private void configureBaseConversion() {
            timeSystem.setBaseConverted(settings.baseConvert());
        }

        private void configureColorWheel() {
            //colorWheel.setSplitDay(timeSystem.isDaySplit());
            //colorWheel.setDuplex(isDuplexed());
            colorWheel.setColorGamut(settings.colorGamut());

            //double offset = (double) timeSystem.gmtOffset() / 24;
            double offset = 0.0;
            colorWheel.setOffset(offset);
        }

        /**
         * Set font based on setting.
         */
        private void configureTypeface() {
            font = settings.getTypeface(getAssets());
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            settings.changePreference(sharedPreferences, key);

            //if (Settings.KEY_PATTERN.equals(key)) {
            //    configurePattern();
            //}

            if (ClockSettings.KEY_COLOR_DUPLEX.equals(key)) {
                //configureColorWheel();
                configureTimeSystem();
            }

            if (ClockSettings.KEY_TIME_TYPE.equals(key)) {
                configureTimeSystem();
            }

            if (ClockSettings.KEY_BASE.equals(key)) {
                configureBaseConversion();
            }

            if (ClockSettings.KEY_COLOR_DYNAMIC.equals(key)) {
                configureColorWheel();
            }

            if (ClockSettings.KEY_COLOR_GAMUT.equals(key)) {
                configureColorWheel();
            }

            if (ClockSettings.KEY_TYPEFACE.equals(key)) {
                configureTypeface();
            }

            // TODO: This is probably a good idea.
            //if (Settings.KEY_ROTATE_TIME.equals(key)) {
            //    configureTimeOffset();
            //}
        }

        private Runnable drawRunner = new Runnable() {
            public void run() {
                draw();
            }
        };

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        /**
         * Return the time to wait between screen refreshes.
         *
         * @return      duration in milliseconds
         */
        private int frameDuration() {
            return timeSystem.tickTime(true); //settings.displaySeconds());
        }

        /**
         * Draw routine.
         */
        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    drawChronochrome();
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

        private String timeStamp = "";

        /**
         * Returns true if the current time stamp differs from the
         * previous time this method was called.
         *
         * @return      true or false
         */
        private boolean timeChanged() {
            if (!timeStamp.equals(timeStamp())) {
                timeStamp = timeStamp();
                return true;
            }
            return false;
        }

        /**
         * Draw the wallpaper.
         */
        private void drawChronochrome() {
            canvas.save();

            //log(timeStamp());

            drawPattern();
            drawFlare();
            drawClockface();

            canvas.restore();
        }

        private void drawClockface() {
            if (false) { //settings.hasColorWheel()) {
                drawColorWheel();
            }

            //if (settings.hasClockDashes()) {
                drawDashes(12);
            //}

            //if (settings.isClockNumbered()) {
                drawNumbers();
            //}

            //if (settings.displayTime()) {
                drawTime();
            //}

            drawClockHands();
        }

        AbstractPattern pattern;

        /**
         * Draw the wallpaper pattern.
         */
        private void drawPattern() {
            pattern.draw(canvas);
        }

        private void drawFlare() {
            switch (settings.getFlare()) {
                case 1:
                    drawGlare();
                    break;
                case 2:
                    drawSunspot();
                    break;
                case 3:
                    //drawMoonspot();
                    break;
            }
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

        private void drawColorWheel() {
            //if (isDaySplit() && !isDuplexed()) {
            //    drawColorWheelSplit();
            //} else {
                drawColorWheelWhole();
            //}
        }

        /**
         * The size of the wheel is calculated very carefully. We know the total width of area
         * in which the wheel must reside (w). We then must calculate the radius of the main
         * circle on which the small circles are centered. This formula derives from the chord
         * equation for a circle (c = 2R sin(a/2)).
         *
         */
        private void drawColorWheelWhole() {
            float cx = centerX();
            float cy = centerY();

            int hd = hoursInDay();
            int hc = hoursOnClock();
            //int mc = minutesOnClock();

            int half = hd / 2;
            int qtr  = hd / 4;

            float w  = faceRadius();

            // TODO: spare option?
            //float r0 = sparse ? spotRadius(1.5f * hc) : spotRadius();
            float r0 = spotRadius();
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

        private void drawColorWheelSplit() {
            float cx = centerX();
            float cy = centerY();

            int hd = hoursInDay();
            int hc = hoursOnClock();

            int half = hd / 2;
            int qtr = hd / 4;

            float w = faceRadius();

            // TODO: spare option?
            //float r0 = sparse ? spotRadius(1.5f * hc) : spotRadius();
            float r0 = spotRadius();
            float r1 = w - r0;
            //float r2 = r0 + (r1 * 1.15f);

            float x, y;
            double r;

            int i, j, s;
            int[] colors;

            colors = clockColors();

            int h = (int) (timeRatios()[0] * hd);

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
        private void drawSemiCircles(int[] c, float x, float y, float r, float a) {
            float cx = centerX();
            float cy = centerY();

            int hd = hoursInDay();
            int hc = hoursOnClock();

            int i;

            int h = (int) (timeRatios()[0] * hd);

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
        private void drawSemiGradients(int[] c, float x, float y, float r, float a) {
            float cx = centerX();
            float cy = centerY();

            float r0 = spotRadius();

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
         * Clock hand colors.
         *
         * @return      array of color integers
         */
        private int[] clockColors() {
            int[] colors = timeSystem.clockColors(colorWheel);

            if (isDynamic()) {
                colors = dynamic(colors);
            }

            return colors;
        }

        /**
         * Draw the numbers of the clock face.
         */
        private void drawNumbers() {
            int i;
            double r;
            float x,y;

            float radius = spotRadius();
            float textWidth = faceRadius() - radius;

            int hc = hoursOnClock();

            float cx = centerX();
            float cy = centerY();

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

            if (false) {  //settings.isClockNumbered()) {
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
         *
         * @param length    length of ticks
         */
        private void drawDashes(float length) {
            double r;
            int c;
            float x0, y0, x1, y1;

            float w  = faceRadius();
            //float r0 = spotRadius();
            //float r1 = w - r0;
            float radius = w * 1.05f; //r0 + (r1 * 1.15f);

            float cx = centerX();
            float cy = centerY();

            int mc = minutesOnClock();

            Paint tickPaint = new Paint();
            tickPaint.setAntiAlias(true);
            tickPaint.setColor(Color.WHITE);
            tickPaint.setStrokeWidth(10.0f);
            tickPaint.setStrokeCap(Paint.Cap.ROUND);
            tickPaint.setStyle(Paint.Style.FILL);
            tickPaint.setShadowLayer(3.0f, 1.0f, 1.0f, Color.LTGRAY);

            for (int i = 0; i < mc; i++) {
                r = ((double) i) / mc;
                c = color(r);  // TODO: dynamic?

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
        private void drawClockHands() {
            //if (!settings.hasClockHands()) {
            //    return;
            //}

            float l;  // length of hand
            float x, y;

            float cx = centerX();
            float cy = centerY();

            float w  = faceRadius();
            float r0 = spotRadius();
            float r1 = w - r0;

            double[] r = handRatios();

            int s = r.length;

            if (!settings.displaySeconds()) {
                s = s - 1;
            }

            l = ((r0 - r1) * 0.90f);

            paint = new Paint();
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
         * Draw solid circle. By default the circle is based on the hour ratio, but if colors
         * are reversed in settings, it is instead by the minute ratio.
         */
        private void drawSunspot() {
            int[] colors = timeColors();

            float x = centerX();
            float y = centerY();
            float w = faceRadius();

            float r;

            if (false) { //settings.hasColorWheel()) {
                //int h = hoursOnClock();
                float r0 = spotRadius(); //float) (w / (1.0f + (2.0f * Math.sin(Math.PI / (2.0f * h)))));
                float r1 = w - r0;
                //r = (settings.clockFace() == 3) ? r0 - r1 : r0;
                r = r0; // - r1;
            } else {
                r = w;
            }

            Paint paint = new Paint();
            paint.setAntiAlias(true);

            if (settings.isColorReversed()) {
                paint.setColor(colors[0]);
                paint.setAlpha(216);
            } else {
                paint.setColor(colors[1]);
                paint.setAlpha(216);
            }

            canvas.drawCircle(x, y, r, paint);
        }

        /**
         *
         */
        private void drawGlare() {
            float x, y;

            int m = 5; // change to day number or week + 2?

            float cx = centerX();
            float cy = centerY();

            int hc = hoursOnClock();

            float w = faceRadius(); // * 2f;
            float d = w;

            double[] t = timeSystem.handRatios();

            double r = t[0];

            Paint paint = new Paint();
            paint.setShadowLayer(3.0f, 0f, 0f, Color.WHITE);

            for (int i = 1; i < m; i++) {
                //r = ((double) i) / hc;
                int c = dynaColor(r / i, 0.5);

                paint.setColor(c); //colors[i]);
                paint.setAlpha(196);

                d = (1.5f*w) / i;

                x = (float) (cx + d * sin(r + rot()));
                y = (float) (cy - d * cos(r + rot()));

                canvas.drawCircle(x, y, w/16, paint);

                x = (float) (cx + d * sin(r + rot() + 0.5));
                y = (float) (cy - d * cos(r + rot() + 0.5));

                canvas.drawCircle(x, y, d/3.5f, paint);
            }
        }

        /**
         * Draw time in the center of the clock face area.
         */
        private void drawTime() {
            Rect d;

            String stamp = timeStamp();

            float x = centerX();
            float y = centerY();

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
         * @return  maximum radius of the clock face
         */
        private float faceRadius() {
            return centerX() * 0.85f;
        }

        /**
         *
         * @return
         */
        private float spotRadius() {
            return spotRadius(hoursOnClock());
        }

        /**
         *
         * @param h     number of divisions
         * @return      radius of central circle
         */
        private float spotRadius(float h) {
            float w = faceRadius();
            return (float) (w / (1.0f + (2.0f * Math.sin(Math.PI / (2.0f * h)))));
        }

        /**
         *
         * @return
         */
        private float maxDimension() {
            float h = canvas.getHeight();
            float w = canvas.getWidth();
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
         *
         * @return
         */
        private boolean isDuplexed() {
            return settings.isDuplexed(); // && !timeSystem.isDaySplit());
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
         * @return      number converted to settings base
         */
        private String base(int number) {
            return base(number, timeBase());
        }

        /**
         * @return      number converted to given base
         */
        private String base(int number, int base) {
            if (settings.timeType() == TIME_TRADITIONAL || settings.timeType() == TIME_MILITARY) {
                return "" + number;
            }

            if (settings.baseConvert()) {
                return Integer.toString(number, base);
            }

            return "" + number;
        }

        /**
         * @param pad   number of zeros to pad number with (max 5)
         * @return number converted to given base
         *
        private String base(int number, int base, int pad) {
            if (settings.timeType() == TIME_TRADITIONAL || settings.timeType() == TIME_MILITARY) {
                return "" + number;
            }

            if (settings.baseConvert()) {
                String s = "0000" + Integer.toString(number, base);
                return s.substring(s.length() - pad);
            }

            return "" + number;
        }
        */

        /**
         * If base conversion is activate in settings then certain time systems can be converted
         * to their intrinsic base, e.g. Heximal times can be elegantly represented in senary
         * (base 6), just as Hexadecimal time can be elegantly represented in hexadecimal (base 16).
         *
         * @return      base
         */
        private int timeBase() {
            return timeSystem.timeBase();
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
         * Return the current time as a series of ratios, one for each time segment of the
         * active time system.
         *
         * @return      series of time ratios
         */
        private double[] timeRatios() {
            return timeSystem.timeRatios();
        }

        /**
         * Return the current time as a series of ratios, one for each clock hand of the
         * active time system.
         *
         * @return      series of time ratios
         */
        private double[] handRatios() {
            return timeSystem.handRatios();
        }

        /**
         * Calculate the milliseconds since midnight.
         *
         * @return      milliseconds since midnight
         */
        private long sinceMidnightMillis() {
            Calendar c = Calendar.getInstance();
            long now = c.getTimeInMillis();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return now - c.getTimeInMillis();
        }

        /**
         * Time of the day represented as a ratio between 0 to 1.
         *
         * @return      time as a ratio
         */
        //private double ratioTime() {
        //    return (1.0d / millisecondsInDay) * sinceMidnightMillis();
        //}

        /**
         * The number of hours in a day based on the active time system.
         *
         * @return      number of hours
         */
        private int hoursInDay() {
            return timeSystem.hoursInDay();
        }

        /**
         * The number of hours on a clock based on the active time system.
         *
         * @return      number of hours
         */
        private int hoursOnClock() {
            return timeSystem.hoursOnClock();
        }

        private int minutesOnClock() {
            return timeSystem.minutesOnClock();
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
         * Returns Log2 of an integer, truncated to an integer itself.
         * This is an imprecise implementation, but it is good enough for our needs.
         *
         * @param n     integer
         * @return      log2 of integer, truncated to an integer
         */
        private int log2(int n) {
            return (int) (Math.log(n) / Math.log(2));
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


        /**
         * Center X coordinate.
         *
         * @return      horizontal origin
         */
        private float centerX() {
            return pattern.centerX(canvas);
        }

        /**
         * The center Y coordinate is raised up a bit b/c android phones always have
         * a set of buttons at the bottom of the screen.
         *
         * @return      vertical origin
         */
        private float centerY() {
            return pattern.centerY(canvas);
        }

        /**
         * Produces an array of colors, one for each time segment. Each color is produced from the
         * ratio of a segment and all the segments that follow. Hence the colors have smooth
         * continuity, unlike those from discreteColors().
         *
         * @return      array of colors
         */
        public int[] timeColors() {
            double[] ratios = timeSystem.handRatios();

            //if (isDuplexed()) {
            //    ratios[0] = reduce(ratios[0] * 0.5);
            //}

            int[] c = colors(ratios);

            return dynamic(c);

            // TODO: Just hours or all?
            //if (isDynamic()) {
            //    c[0] = dynamic(c[0]); //, ratios[0]);
            //}
            //return c; //colors(ratios);
        }

        /**
         *
         * @return
         */
        private boolean isDynamic() {
            return settings.hasDynamicColor();
        }

        /**
         * Produces an array of colors, one for each time segment. Unlike timeColors() this method
         * produces each color based solely on it's segment value.
         *
         * @return      array of colors
         */
        public int[] discreteTimeColors() {
            double[] ratios = timeSystem.discreteRatios();

            if (isDuplexed()) {
                ratios[0] = reduce(ratios[0] * 2);
            }

            return colors(ratios);
        }

        /**
         * Convert a double array to a float array.
         *
         * @param arr       array of doubles
         * @return          array of floats
         */
        private float[] toFloatArray(double[] arr) {
            if (arr == null) return null;
            int n = arr.length;
            float[] ret = new float[n];
            for (int i = 0; i < n; i++) {
                ret[i] = (float)arr[i];
            }
            return ret;
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
         * Reverse an int array.
         *
         * @param a     array
         */
        public void reverseArray(int[] a) {
            for (int i = 0; i < a.length; i++) {
                int temp = a[i];
                a[i] = a[a.length - i - 1];
                a[a.length - 1] = temp;
            }
        }

        private int[] rotate(int[] a, int n) {
            int[] x = new int[a.length];
            for(int i = 0; i < a.length; i++) {
                x[i] = a[(i + n) % a.length];
            }
            return x;
        }

        /**
         *
         * @param ratios    array of color ratios
         * @return          array of color integers
         */
        private int[] colors(double[] ratios) {
            int[] cs = new int[ratios.length];
            for(int i=0; i < ratios.length; i++) {
                cs[i] = color(ratios[i]);
            }
            return cs;
        }

        /**
         *
         * @param divs      number of divisions
         * @return          array of color integers
         */
        private int[] colors(int divs) {
            int[] cs = new int[divs];
            double ratio;
            for (int i = 0; i < divs; i++) {
                ratio = (double) i / divs;
                cs[i] = color(ratio);
            }
            return cs;
        }

        /**
         *
         * @param ratio     color ratio
         * @return          color integer
         */
        private int color(double ratio) {
            return colorWheel.color(ratio);
        }

        /**
         *
         * @param ratio     color ratio
         * @return          color integer
         */
        private int color(double ratio, double alpha) {
            return colorWheel.alphaColor(ratio, alpha);
        }

        /**
         * Same as color but applies duplex and dynamic adjustments.
         *
         * @param ratio     color ratio
         * @return          color integer
         */
        private int dynaColor(double ratio) {
            int c = colorWheel.color(ratio * (isDuplexed() ? 2.0 : 1.0));
            if (isDynamic()) {
                c = dynamic(c); //, ratio);
            }
            return c;
        }

        /**
         *
         * Same as alphaColor but applies duplex and dynamic adjustments.
         *
         * @param ratio     color ratio
         * @param alpha     alpha ratio
         * @return          color integer
         */
        private int dynaColor(double ratio, double alpha) {
            int c = colorWheel.alphaColor(ratio * (isDuplexed() ? 2.0 : 1.0), alpha);
            if (isDynamic()) {
                c = dynamic(c);  //, ratio);
            }
            return c;
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
            double t = timeSystem.timeRatios()[0];

            int hc;
            //if (timeSystem.isDaySplit()) {
            //    hc = hoursOnClock();
            //} else {
                hc = hoursOnClock();
            //}

            double s = saturation(t);
            double v = brightness(t);

            int[] newColors = new int[colors.length];

            for(int i=0; i < colors.length; i++) {
                //double r = (double) i / hc;

                //s = saturation(r);
                //v = brightness(r);

                newColors[i] = adjustColor(colors[i], s, v);
            }

            return newColors;
        }

        public static final double MAX_BRIGHTNESS  = 1.0;
        public static final double MAX_SATURATION  = 1.0;

        /**
         * Given a color, adjust its brightness and saturation.
         *
         * TODO: prevent values from exceeding 1 or going below 0.
         *
         * @param color     color integer
         * @param s         saturation factor
         * @param b         brightness factor
         * @return          adjusted colors
         */
        private int adjustColor(int color, double s, double b) {
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[1] = (float) (s * hsv[1]);
            hsv[2] = (float) (b * hsv[2]);
            return Color.HSVToColor(hsv);
        }

        /**
         *
         * @param ratio     color ratio
         * @return          saturation level
         */
        private double saturation(double ratio) {
            if (settings.hasDynamicColor()) {
                double a = absSin(ratio);
                return a * 0.2 + 0.8;
            } else {
                return MAX_SATURATION;
            }
        }

        /**
         *
         * @param ratio     color ratio
         * @return          brightness level
         */
        private double brightness(double ratio) {
            if (settings.hasDynamicColor()) {
                double a = absSin2(ratio);
                return a * 0.3 + 0.7;
            } else {
                return MAX_BRIGHTNESS;
            }
        }

        /**
         * Takes the absolute value of the sin of the given ratio. This produces a sinusoidal value
         * from 0 to 1 to 0, for a given value between 0 and 1. This is useful for making noon
         * bright and midnight dark.
         *
         * @param ratio     ratio between 0 and 1
         * @return          sinusoidal value
         */
        private double absSin2(double ratio) {
            return(Math.abs(Math.sin(Math.PI * ratio)));
        }

        /**
         * Takes the absolute value of the sine of the given ratio. This produces a sinusoidal value
         * from 0 to 1 to 0 to 1 to 0, for a given value between 0 and 1.
         *
         * @param ratio     ratio between 0 and 1
         * @return          sinusoidal value
         */
        private double absSin(double ratio) {
            return(Math.abs(Math.sin(2 * Math.PI * ratio)));
        }

        /**
         * TODO: better name for this method
         *
         * @param ratio     ratio to reduce
         * @return          ratio reduced to 0 to 1
         */
        private double reduce(double ratio) {
            if (ratio < 0) {
                int n = (int) ratio;
                return (1.0 - (ratio - n));
            } else {
                int n = (int) ratio;
                return (ratio - n);
            }
        }

    }

}