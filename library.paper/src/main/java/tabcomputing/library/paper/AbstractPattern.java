package tabcomputing.library.paper;

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
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.color.ColorWheel;

public abstract class AbstractPattern implements SettingsListener {

    public AbstractPattern() { }

    protected void setContext(Context context) {
        this.context = context;
        this.upsaleManager = new UpsaleManager(context, getNagMessageList());

    }

    protected void setSettings(AbstractSettings settings) {
        this.settings = settings;
    }

    protected Context context;

    protected UpsaleManager upsaleManager;

    protected AbstractSettings settings;

    protected TimeSystem timeSystem;

    protected ColorWheel colorWheel;

    protected Typeface typeface = Typeface.DEFAULT;

    // Reusable paint object, just call paint.reset to use.
    protected Paint paint = new Paint();

    // Implement SettingsListener interface

    //public void setSettings(AbstractSettings s) {
    //    settings = s;
    //}


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
        upsaleManager.drawMessage(canvas, nagId());
    }

    private int nagId() {
        int[] time = time();
        int hour = time[0];

        double[] tr = timeRatios();
        int[] s = timeSegments();

        double q = (5.0 * s[0]) / (24.0 * 60.0);

        //Log.d("log", "tr[1]: " + tr[1] + " q: " + q);

        // if less than 5 minutes (in traditional time) past the hour (in whatever time system)
        if (tr[1] < q) {
            return hour;
        }

        return -1;
    }

    /**
     * Override this method returning an array list of resource ids to nag messages.
     *
     * @return      list of string resource ids
     */
    protected ArrayList<Integer> getNagMessageList() {
        return new ArrayList<>();
    }

    /**
     * Center X coordinate.
     *
     * @return horizontal origin
     */
    public float centerX(Canvas canvas) {
        return canvas.getWidth() / 2;
    }
    public float centerX(Rect bounds) {
        return bounds.width() / 2;
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
    public float centerY(Rect bounds) {
        return bounds.height() / 2 * 0.85f;
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
     * Override this in subclass.
     */
    protected boolean withSeconds() {
        return true;
    }

    protected boolean sansSeconds() {
        return !withSeconds();
    }

    /**
     * @return
     */
    protected int[] time(boolean sansSeconds) {
        return timeSystem.time(sansSeconds);
        //if (!all && !settings.displaySeconds()) {
        //    time = Arrays.copyOf(time, time.length - 1);
        //}
        //return time;
    }
    protected int[] time() {
        return time(sansSeconds());
    }

    /**
     * Each time system decomposes the day into a set of time segments.
     * For example, standard time divides the day into 24 hours, each
     * of which is divided into 60 minutes, which in turn are divided
     * into 60 seconds. So this method would return `{24, 60, 60}` if
     * traditional time is selected in settings.
     *
     * @return      array of time segments
     */
    protected int[] timeSegments(boolean sansSeconds) {
        return timeSystem.timeSegments(sansSeconds);
        //if (!all && !settings.displaySeconds()) {
        //    time = Arrays.copyOf(time, time.length - 1);
        //}
        //return time;
    }
    protected int[] timeSegments() {
        return timeSegments(sansSeconds());
    }

    /**
     * Return the current time as a series of ratios, one for each time segment of the
     * active time system.
     *
     * @return      series of time ratios
     */
    protected double[] timeRatios(boolean sansSeconds) {
        return timeSystem.timeRatios(sansSeconds);
        //if (!all && !settings.displaySeconds()) {
        //    ratios = Arrays.copyOf(ratios, ratios.length - 1);
        //}
        //return ratios;
    }
    protected double[] timeRatios() {
        return timeRatios(sansSeconds());
    }

    /**
     * Just like time() but values are base converted.
     *
     * @return          array of time converted to intrinsic base
     */
    protected String[] timeRebased(boolean sansSeconds) {
        return timeSystem.timeRebased(sansSeconds);
        //if (!all && !settings.displaySeconds()) {
        //    t = Arrays.copyOf(t, t.length - 1);
        //}
        //return t;
    }
    protected String[] timeRebased() {
        return timeRebased(sansSeconds());
    }

    /**
     * Return the current time as a series of ratios, one for each hand of the clock for
     * the active time system. This is different from timeRatios() in that standard time
     * splits the hours into day and night.
     *
     * @return series of time ratios
     */
    protected double[] handRatios(boolean sansSeconds) {
        return timeSystem.handRatios(sansSeconds);
    }
    protected double[] handRatios() {
        return handRatios(sansSeconds());
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

    //protected int minutesOnClock() {
    //    return timeSystem.minutesOnClock();
    //}

    /**
     * Clock hour colors.
     *
     * @return array of color integers
     */
    protected int[] clockColors() {
        return colors(hoursInDay());
    }

    /**
     *
     * @return
     */
    protected int[] clockSegments(boolean sansSeconds) {
        return timeSystem.clockSegments(sansSeconds);
    }
    protected int[] clockSegments() {
        return timeSystem.clockSegments(sansSeconds());
    }

    /**
     *
     * @param sansSeconds
     * @return
     */
    protected int numberOfSegments(boolean sansSeconds) {
        int s = timeSystem.clockSegments().length;
        if (sansSeconds) {
            s = s - 1;
        }
        return s;
    }
    protected int numberOfSegments() {
        return numberOfSegments(sansSeconds());
    }

    /**
     * Produces an array of colors, one for each time segment. Each color is produced from the
     * ratio of a segment and all the segments that follow. Hence the colors have smooth
     * continuity, unlike those from discreteColors().
     *
     * @return array of colors
     */
    protected int[] timeColors(boolean sansSeconds) {
        double[] ratios = timeSystem.timeRatios(sansSeconds);

        //if (settings.isDuplexed()) {
        //    ratios[0] = reduce(ratios[0] * 2);
        //}

        return colorWheel.colors(ratios);
    }
    protected int[] timeColors() {
        return timeColors(sansSeconds());
    }

    /**
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
     * @param ratio     color ratio
     * @return          color integer
     */
    protected int color(double ratio) {
        return colorWheel.color(ratio); //, settings.hasDynamicColor());
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
        return colorWheel.color(ratio);
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

    //public static final double MAX_BRIGHTNESS = 1.0;
    //public static final double MAX_SATURATION = 1.0;

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
    protected String sampleTime(boolean sansSeconds) {
        return timeSystem.timeStampSample(sansSeconds);
    }
    protected String sampleTime() {
        return timeSystem.timeStampSample(sansSeconds());
    }

    /**
     * Sets the text size for a Paint object so a given string of text will be a
     * given width.
     *
     * @param paint        instance of Paint to set the text size for
     * @param desiredWidth desired width
     * @param text         the text that should be that width
     */
    @Deprecated
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

    protected float textSizeForWidth(String text, float desiredWidth, Paint paint) {
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
        return (testTextSize * desiredWidth / bounds.width());
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

    protected int[] append(int[] a, int e) {
        int[] x = Arrays.copyOf(a, a.length+1);
        x[a.length] = e;
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
     * Concatenate two int arrays.
     *
     * @param a     first int array
     * @param b     second int array
     * @return      new array
     */
    public int[] concat(int[] a, int[] b) {
        int aLen = a.length;
        int bLen = b.length;
        int[] c= new int[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
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

    /*
     * Scratch is a reusable canvas and bitmap. Simple call resetScratch() before using.
     *
     * DEPRECATED in favor of BitmapReuse
     *
    protected Rect scratchBounds;
    protected Bitmap scratchBitmap;
    protected Canvas scratchCanvas;

    protected void resetScratch(Rect bounds) {
        if (scratchBounds == null) {
            newScratch(bounds);
        }
        if (!scratchBounds.equals(bounds)) {
            Log.d("log", "BOUNDS NOT EQUAL!!!!!!");
            newScratch(bounds);
        }
        scratchCanvas.drawColor(Color.TRANSPARENT);
    }

    protected void newScratch(Rect bounds) {
        scratchBounds = bounds;
        scratchBitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        scratchCanvas = new Canvas(scratchBitmap);
    }
    */

}
