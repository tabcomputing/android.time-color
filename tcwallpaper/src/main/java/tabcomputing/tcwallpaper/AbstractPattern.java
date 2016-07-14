package tabcomputing.tcwallpaper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.color.ColorWheel;

public abstract class AbstractPattern implements SettingsListener {

    public AbstractPattern() {
    }

    protected void setContext(Context context) {
        this.context = context;
    }

    protected void setSettings(CommonSettings settings) {
        this.settings = settings;
    }

    protected Context context;

    protected CommonSettings settings;

    protected TimeSystem timeSystem;

    protected ColorWheel colorWheel;

    protected Typeface typeface = Typeface.DEFAULT;

    // Reusable paint object, just call paint.reset to use.
    protected Paint paint = new Paint();

    // Implement SettingsListener interface

    //public void setSettings(AbstractSettings s) {
    //    settings = s;
    //}

    public void preferenceChanged(String key) {
        if (key.equals(CommonSettings.KEY_TIME_SYSTEM)) {
            timeSystem = settings.newTimeSystem();
        } else if (key.equals(CommonSettings.KEY_TYPEFACE)) {
            typeface = settings.newTypeface();
        } else if (key.equals(CommonSettings.KEY_COLOR_GAMUT)) {
            colorWheel.setColorGamut(settings.getColorGamut());
        } else if (key.equals(CommonSettings.KEY_COLOR_DAYLIGHT)) {
            colorWheel.setDaylightFactor(settings.isDaylight() ? 0.7 : 0.0);
        }
    }

    public void resetPreferences() {
        timeSystem = settings.getTimeSystem();
        colorWheel = settings.getColorWheel();
        typeface = settings.getTypeface();
    }

    /**
     * @param canvas canvas instance
     */
    public void draw(Canvas canvas) {
        // OVERRIDE ME
    }

    /**
     * Draw the pattern but add a nag message on the hour. This is draw method used
     * when the user hasn't bought the product.
     */
    public void drawWithNag(Canvas canvas) {
        draw(canvas);
        drawNag(canvas);
    }

    private void drawNag(Canvas canvas) {
        String msg;

        Resources res = context.getResources();

        float cx = canvas.getWidth() / 2;
        float cy = canvas.getHeight() / 2;

        int[] time = timeSystem.time();
        int hour = time[0];
        int minute = time[1];

        if (minute >= 0 && minute < 5) {
            int pick = hour % 4;
            switch (pick) {
                case 4:
                    msg = res.getString(tabcomputing.library.paper.R.string.bug_phrase_4);
                    break;
                case 3:
                    msg = res.getString(tabcomputing.library.paper.R.string.bug_phrase_3);
                    break;
                case 2:
                    msg = res.getString(tabcomputing.library.paper.R.string.bug_phrase_2);
                    break;
                case 1:
                    msg = res.getString(tabcomputing.library.paper.R.string.bug_phrase_1);
                    break;
                default:
                    msg = res.getString(tabcomputing.library.paper.R.string.bug_phrase_0);
            }

            float gap = canvas.getWidth() * 0.05f;
            float cut = canvas.getWidth() * 0.9f;

            TextPaint paint = new TextPaint();
            paint.setTextSize(80.0f);
            paint.setColor(Color.WHITE);
            paint.setShadowLayer(10.0f, 10.0f, 10.0f, Color.BLACK);
            paint.setTextAlign(Paint.Align.CENTER);

            canvas.save();
            canvas.translate(cx, cy / 2.0f);
            StaticLayout txt = new StaticLayout(msg, paint, (int) (cut), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);
            txt.draw(canvas);
            canvas.restore();
        }
    }

    /**
     * Center X coordinate.
     *
     * @return horizontal origin
     */
    public float centerX(Canvas canvas) {
        return canvas.getWidth() / 2;
    }

    /**
     * The center Y coordinate is raised up a bit b/c android phones always have
     * a set of buttons at the bottom of the screen.
     *
     * @return vertical origin
     */
    public float centerY(Canvas canvas) {
        return canvas.getHeight() / 2 * 0.85f;
    }

    /**
     * @return maximum radius of the clock face
     */
    protected float faceRadius(Canvas canvas) {
        return centerX(canvas) * 0.85f;
    }

    /**
     * @return
     */
    protected float spotRadius(Canvas canvas) {
        return spotRadius(canvas, hoursOnClock());
    }

    /**
     * @param h number of divisions
     * @return radius of central circle
     */
    protected float spotRadius(Canvas canvas, float h) {
        float w = faceRadius(canvas);
        return (float) (w / (1.0f + (2.0f * Math.sin(Math.PI / (2.0f * h)))));
    }

    /**
     * @return
     */
    protected float maxDimension(Canvas canvas) {
        float h = canvas.getHeight();
        float w = canvas.getWidth();
        return (h > w ? h : w);
    }

    /**
     * @return
     */
    protected int[] time() {
        return timeSystem.time();
    }

    /**
     * Return the current time as a series of ratios, one for each time segment of the
     * active time system.
     *
     * @return series of time ratios
     */
    protected double[] timeRatios() {
        return timeSystem.timeRatios();
    }

    /**
     * Return the current time as a series of ratios, one for each hand of the clock for
     * the active time system. This is different from timeRatios() in that standard time
     * splits the hours into day and night.
     *
     * @return series of time ratios
     */
    protected double[] handRatios() {
        return timeSystem.handRatios();
    }

    /**
     * The number of hours in a day based on the active time system.
     *
     * @return number of hours
     */
    protected int hoursInDay() {
        return timeSystem.hoursInDay();
    }

    /**
     * The number of hours on a clock based on the active time system.
     *
     * @return number of hours
     */
    protected int hoursOnClock() {
        return timeSystem.hoursOnClock();
    }

    protected int minutesOnClock() {
        return timeSystem.minutesOnClock();
    }

    /**
     * Clock hand colors.
     *
     * @return array of color integers
     */
    protected int[] clockColors() {
        int[] colors = timeSystem.clockColors(colorWheel);

        //if (settings.hasDynamicColor()) {
        //    return dynamic(colors);
        //}

        return colors;
    }

    /**
     * Produces an array of colors, one for each time segment. Each color is produced from the
     * ratio of a segment and all the segments that follow. Hence the colors have smooth
     * continuity, unlike those from discreteColors().
     *
     * @return array of colors
     */
    protected int[] timeColors() {
        double[] ratios = timeSystem.timeRatios();

        if (settings.isDuplexed()) {
            ratios[0] = reduce(ratios[0] * 2);
        }

        return colorWheel.colors(ratios);
    }

    /**
     * DEPRECATED
     * <p/>
     * Color for minutes and seconds of the day. The particular color depends on the
     * type of clock. The brightness and saturation are functions of the hour, whereas
     * the hue is a function of the minutes and seconds.
     *
     * @return color integer
     */
    @Deprecated
    protected int minuteColor() {
        return timeColors()[1];
    }

    /**
     * @param ratios array of color ratios
     * @return array of color integers
     */
    protected int[] colors(double[] ratios) {
        int[] cs = new int[ratios.length];
        for (int i = 0; i < ratios.length; i++) {
            cs[i] = color(ratios[i]);
        }
        return cs;
    }

    /**
     * @param divs number of divisions
     * @return array of color integers
     */
    protected int[] colors(int divs) {
        int[] cs = new int[divs];
        double ratio;
        for (int i = 0; i < divs; i++) {
            ratio = (double) i / divs;
            cs[i] = color(ratio);
        }
        return cs;
    }

    /**
     * @param ratio color ratio
     * @return color integer
     */
    protected int color(double ratio) {
        return color(ratio); //, settings.hasDynamicColor());
    }

    //For traditional clocks, the time of day is mapped
    //* to the color wheel such that it rotates twice daily.

    /**
     * Color for given time ratio. When the dynamic flag is used, the colors will be adjust
     * according to the time of day. Brightness is highest as noon and lowest at midnight.
     * Saturation is highest at dawn and dusk and lowest at midnight and midday.
     *
     * @param ratio   color ratio
     * @param dynamic is daylight dynamic color?
     * @return color integer
     */
    protected int color(double ratio, boolean dynamic) {
        int color = colorWheel.color(ratio);
        //if (dynamic) {
        //    return dynamic(color);
        //}
        return color;
    }

    /**
     * TODO: add dynamic ?
     *
     * @param ratio color ratio
     * @return color integer
     */
    protected int color(double ratio, double alpha) {
        return colorWheel.alphaColor(ratio, alpha);
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
     * @return color integer
     *
    protected int dynamic(int color) {
    double t = timeSystem.ratioTime();

    double s = saturation(t);
    double v = brightness(t);

    //log("color: " + colorWheel.hexString(color) + " ratio: " + ratio + " sat: " + s + " brt: " + v);

    return adjustColor(color, s, v);
    }

    protected int dynamic(int color, double ratio) {
    double s = saturation(ratio);
    double v = brightness(ratio);
    return adjustColor(color, s, v);
    }
     */

    /**
     * Apply daylight dynamic to array of colors.
     *
     * @param colors    array of color integers
     * @return array of color integers
     * <p/>
     * protected int[] dynamic(int[] colors) {
     * //double t = timeSystem.timeRatios()[0];
     * double t = timeSystem.ratioTime();
     * <p/>
     * int hc;
     * //if (timeSystem.isDaySplit()) {
     * //    hc = hoursOnClock();
     * //} else {
     * hc = hoursOnClock();
     * //}
     * <p/>
     * double s = saturation(t);
     * double v = brightness(t);
     * <p/>
     * int[] newColors = new int[colors.length];
     * <p/>
     * for(int i=0; i < colors.length; i++) {
     * //double r = (double) i / hc;
     * <p/>
     * //s = saturation(r);
     * //v = brightness(r);
     * <p/>
     * newColors[i] = adjustColor(colors[i], s, v);
     * }
     * <p/>
     * return newColors;
     * }
     */

    public static final double MAX_BRIGHTNESS = 1.0;
    public static final double MAX_SATURATION = 1.0;

    /**
     * Given a color, adjust its brightness and saturation.
     *
     * TODO: prevent values from exceeding 1 or going below 0.
     *
     * @param color     color integer
     * @param s         saturation factor
     * @param b         brightness factor
     * @return adjusted colors
     *
    protected int adjustColor(int color, double s, double b) {
    float[] hsv = new float[3];
    Color.colorToHSV(color, hsv);
    hsv[1] = (float) (s * hsv[1]);
    hsv[2] = (float) (b * hsv[2]);
    return Color.HSVToColor(hsv);
    }
     */

    /**
     *
     * @param ratio     color ratio
     * @return saturation level
     *
    protected double saturation(double ratio) {
    if (settings.hasDynamicColor()) {
    double a = absSin(ratio);
    return a * 0.2 + 0.8;
    } else {
    return MAX_SATURATION;
    }
    }
     */

    /**
     *
     * @param ratio     color ratio
     * @return brightness level
     *
    protected double brightness(double ratio) {
    if (settings.hasDynamicColor()) {
    double a = absSin2(ratio);
    return a * 0.3 + 0.7;
    } else {
    return MAX_BRIGHTNESS;
    }
    }
     */

    /**
     * When displaying the time we size the font to fit a specific area of the display.
     * In order to keep text from shifting noticeably as the time changes, we set the
     * font size using a sample text representative of the time to be displayed, rather
     * then the actual time.
     * <p/>
     * The letter A and number 4 are used because they tends to be the widest in print.
     *
     * @return sample time
     */
    protected String sampleTime() {
        return timeSystem.timeStampSample(settings.displaySeconds());
    }

    /**
     * @param paint        instance of Paint to set the text size for
     * @param desiredWidth desired width
     * @param text         the text that should be that width
     * @deprecated Sets the text size for a Paint object so a given string of text will be a
     * given width.
     */
    protected void setTextSizeForWidth(Paint paint, float desiredWidth, String text, float fudge) {
        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float testTextSize = 72f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = (testTextSize * desiredWidth / bounds.width()) * fudge;

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }

    /**
     * Return text size for a given String and Paint object so a given string of text will be a
     * given bounds.
     *
     * @param text  text to adjust font size for
     * @param rect  bounds to fit the text in
     * @param paint instance of Paint to set the text size for
     */
    protected float textSizeForRect(String text, RectF rect, Paint paint) {
        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float testTextSize = 72f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float hSize = (testTextSize * rect.height() / bounds.height());
        float wSize = (testTextSize * rect.width() / bounds.width());

        // take the smaller of the two
        return (hSize < wSize ? hSize : wSize);
    }

    public Bitmap textAsBitmap(String text, float offsetX, float offsetY, Paint paint) {
        paint.setTextAlign(Paint.Align.LEFT);

        //Paint.FontMetrics fm = paint.getFontMetrics();

        float baseline = -paint.ascent(); // ascent() is negative

        int width  = (int) (paint.measureText(text) + offsetX);
        int height = (int) (baseline + paint.descent() + offsetY);

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(bmp);
        canvas.drawText(text, offsetX, baseline + offsetY, paint);
        return bmp;
    }

    /**
     *
     * @return      ratio to rotate
     */
    protected double rot() {
        if (settings.isRotated()) {
            return 0.5; //Math.PI;
        } else {
            return 0;
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
    protected double absSin2(double ratio) {
        return(Math.abs(Math.sin(Math.PI * ratio)));
    }

    /**
     * Takes the absolute value of the sine of the given ratio. This produces a sinusoidal value
     * from 0 to 1 to 0 to 1 to 0, for a given value between 0 and 1.
     *
     * @param ratio     ratio between 0 and 1
     * @return          sinusoidal value
     */
    protected double absSin(double ratio) {
        return(Math.abs(Math.sin(2 * Math.PI * ratio)));
    }

    /**
     * TODO: better name for this method
     *
     * @param ratio     ratio to reduce
     * @return          ratio reduced to 0 to 1
     */
    protected double reduce(double ratio) {
        if (ratio < 0) {
            int n = (int) ratio;
            return (1.0 - (ratio - n));
        } else {
            int n = (int) ratio;
            return (ratio - n);
        }
    }

    protected final double TAU = 2 * Math.PI;

    /**
     * Calculate the sine of a given ratio. Unlike Math.sin, this method works with ratios
     * instead of radians, i.e. it multiples the given ratio by 2π before passing it off to
     * Math.sin().
     *
     * @param ratio     value between 0 and 1
     * @return          sin result
     */
    protected double sin(double ratio) {
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
    protected double cos(double ratio) {
        return Math.cos(TAU * ratio);
    }

    /**
     * Returns Log2 of an integer, truncated to an integer itself.
     * This is an imprecise implementation, but it is good enough for our needs.
     *
     * @param n     integer
     * @return      log2 of integer, truncated to an integer
     */
    protected int log2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }

    /**
     * Real modulus.
     *
     * @param n     numerator
     * @param d     denominator
     * @return      modulo
     */
    protected int mod(int n, int d) {
        if (n < 0) {
            return (d + (n % d));
        } else {
            return (n % d);
        }
    }

    /**
     * Rotate an array by n positions.
     *
     * @param a     array of integers
     * @param n     number of rotations
     * @return      rotated array of integers
     */
    protected int[] rotate(int[] a, int n) {
        int[] x = new int[a.length];
        for(int i = 0; i < a.length; i++) {
            x[i] = a[(i + n) % a.length];
        }
        return x;
    }

    /**
     * Reverse an int array.
     *
     * @param a     array
     */
    protected void reverseArray(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int temp = a[i];
            a[i] = a[a.length - i - 1];
            a[a.length - 1] = temp;
        }
    }

    /**
     * Sum values in int array.
     *
     * @param list      array of integers
     * @return          integer sum
     */
    protected int sum(int[] list) {
        int s = 0;
        for(int x : list) {
            s = s + x;
        }
        return s;
    }

    /**
     * Index of element with highest value.
     *
     * @param list      array of integers
     * @return          index
     */
    protected int maxIndex(int[] list) {
        int x = 0;
        for(int i=0; i < list.length; i++) {
            if (list[i] > list[x]) {
                x = i;
            }
        }
        return x;
    }

    protected float min(float a, float b) {
        return ((a < b) ? a : b);
    }

    protected float max(float a, float b) {
        return ((a > b) ? a : b);
    }

    protected double min(double a, double b) {
        return ((a < b) ? a : b);
    }

    protected double max(double a, double b) {
        return ((a > b) ? a : b);
    }

    protected int min(int a, int b) {
        return ((a < b) ? a : b);
    }

    protected int max(int a, int b) {
        return ((a > b) ? a : b);
    }

}
