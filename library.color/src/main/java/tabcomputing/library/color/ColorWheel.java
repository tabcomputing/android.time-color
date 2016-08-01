package tabcomputing.library.color;

import android.graphics.Color;
import android.util.Log;

import java.util.Calendar;

public class ColorWheel {

    public ColorWheel() {
    }

    //public static final int FLAG_BRIGHTNESS  = 0x01;
    //public static final int FLAG_SATURATION  = 0x02;
    //public static final int FLAG_SPLIT_DAY   = 0x04;
    //public static final int FLAG_COLOR_GAMUT = 0x08;

    //public static final double MAX_BRIGHTNESS  = 1.0;
    //public static final double MAX_SATURATION  = 1.0;

    private double offset = 0;

    private double daylightFactor = 0;

    private int colorGamut = 0;


    public void setOffset(double ratio) {
        offset = ratio;
    }

    public void setDaylightFactor(double factor) {
        daylightFactor = factor;
    }

    public void setDaylightFactor(boolean factor) {
        if (factor) {
            daylightFactor = 0.7;  // TODO: make a constant
        } else {
            daylightFactor = 0;     // off
        }
    }

    public double getDaylightFactor() {
        return daylightFactor;
    }


    public void setColorGamut(int gamut) {
        colorGamut = gamut;
        //interColor( "#ED2024", "#EF5524", 0.5);
    }

    /**
     * Given the number of segments, return an array of colors of equal divisions of the
     * color wheel.
     *
     * @param segments number of color segments
     * @return array of colors
     */
    public int[] colors(int segments) {
        int[] c = new int[segments];
        for (int i = 0; i < segments; i++) {
            c[i] = color((double) i / segments);
        }
        return c;
    }

    /**
     * Given an array of ratios, return an array of corresponding colors.
     *
     * @param ratios array of ratios
     * @return array of colors
     */
    public int[] colors(double[] ratios) {
        int[] c = new int[ratios.length];
        for (int i = 0; i < ratios.length; i++) {
            c[i] = color(ratios[i]);
        }
        return c;
    }

    public int[] bicolors(int segments) {
        int s = segments / 2;
        int[] c = new int[segments];
        for (int i = 0; i < s; i++) {
            c[i] = color((double) i / s);
            c[i + s] = color((double) i / s);
        }
        return c;
    }

    // FIXME
    public int[] bicolors(double[] ratios) {
        int[] c = new int[ratios.length];
        for (int i = 0; i < ratios.length; i++) {
            c[i] = color(ratios[i] * 2);
        }
        return c;
    }

    // Real Color Wheel
    final String[] REAL_COLOR_WHEEL = {
            "#2B3F81", "#483190", "#63308E", "#792D8D", "#92298D", "#B91E8C",
            "#EA098B", "#ED0F71", "#ED195C", "#EC1F4C", "#EB1B3C", "#EB2030",
            "#ED2024", "#EF5524", "#F47521", "#FB9404", "#FBB416", "#F7CE15",
            "#F7EC2E", "#C5D92F", "#A4CE39", "#81C342", "#55B948", "#08AD4C",
            "#00A551", "#00A55D", "#00A76D", "#14AB89", "#00A79C", "#00ABCA",
            "#00ABEB", "#0093D7", "#0072B9", "#005FAB", "#05509F", "#214297"
    };
    //"#EC1D44", "#00ABEB",
    // Adjusted Real Color Wheel
    final String[] PALETTE = {
            "#2B3F81", "#483190", "#63308E", "#792D8D", "#92298D", "#B91E8C",
            "#D2148C", "#EA098B", "#ED0F71", "#EE192C", "#ED2024", "#EE3B24",
            "#EF5524", "#F47521", "#FB9404", "#FBB416", "#F7CE15", "#F7DD22",
            "#F7EC2E", "#DEE32F", "#C5D92F", "#A4CE39", "#81C342", "#55B948",
            "#08AD4C", "#00A551", "#00A665", "#14AB89", "#00A79C", "#00ABCA",
            "#009FE1", "#0093D7", "#0072B9", "#005FAB", "#05509F",
            "#214297",
    };

    /**
     * @param s0 hex string of first color
     * @param s1 hex string of second color
     * @param p  ratio in between
     */
    public int interColor(String s0, String s1, double p) {
        int c0 = Color.parseColor(s0); //"#00A55D");
        int c1 = Color.parseColor(s1); //"#00A76D");

        int r = Color.red(c0) + (int) Math.round(p * (Color.red(c1) - Color.red(c0)));
        int g = Color.green(c0) + (int) Math.round(p * (Color.green(c1) - Color.green(c0)));
        int b = Color.blue(c0) + (int) Math.round(p * (Color.blue(c1) - Color.blue(c0)));

        int color = Color.rgb(r, g, b);

        //Log.d("---------->", hexString(color));

        return color;
    }

    public int interColor(int c0, int c1, double p) {
        int r = Color.red(c0) + (int) Math.round(p * (Color.red(c1) - Color.red(c0)));
        int g = Color.green(c0) + (int) Math.round(p * (Color.green(c1) - Color.green(c0)));
        int b = Color.blue(c0) + (int) Math.round(p * (Color.blue(c1) - Color.blue(c0)));

        return Color.rgb(r, g, b);
    }

    /**
     * Return color dependent on color gamut settings.
     *
     * @param ratio ratio around wheel
     * @return color integer
     */
    public int color(double ratio) {
        int color;

        ratio = rotate(ratio, offset);

        switch (colorGamut) {
            case 1:
                color = colorRGB(ratio);
                break;
            case 2:
                color = colorBW(ratio);
                break;
            default:
                color = colorRCW(ratio);
        }

        if (daylightFactor > 0) {
            color = daylight(color);
        }

        return color;
    }

    /**
     * Average color.
     *
     * @param colors array of color integers
     * @return color integer
     */
    public int average(int[] colors) {
        int a = 0, r = 0, g = 0, b = 0;

        for (int c : colors) {
            a = a + Color.alpha(c);
            r = r + Color.red(c);
            g = g + Color.green(c);
            b = b + Color.green(c);
        }

        int l = colors.length;

        return (Color.argb(a / l, r / l, g / l, b / l));
    }

    /**
     * Add alpha to a color.
     *
     * @param color color integer
     * @param alpha alpha level
     * @return color integer
     */
    public int alphaColor(int color, double alpha) {
        return Color.argb((int) (256 * alpha), Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * Return color dependent on color gamut and duplex settings.
     *
     * @param ratio ratio around wheel
     * @param alpha alpha level
     * @return color integer
     */
    public int alphaColor(double ratio, double alpha) {
        int c;

        //if (duplexed) {
        //    ratio = reduce(ratio * 2);
        //}

        switch (colorGamut) {
            case 1:
                c = colorRGB(ratio);
                break;
            case 2:
                c = colorBW(ratio);
                break;
            default:
                c = colorRCW(ratio);
        }

        return Color.argb((int) (256 * alpha), Color.red(c), Color.green(c), Color.blue(c));
    }

    protected int colorRCW(double ratio) {
        String[] palette;

        //if (splitDay) {
        //    palette = rotate(PALETTE, 9);
        //} else {
        palette = PALETTE;
        //}

        int base = 36;
        int i = (int) (ratio * base);
        if (i >= base) {
            i = 0;
        }
        int j = i + 1;
        if (j >= base) {
            j = 0;
        }

        double a = ((double) i / base);
        //double b = ((double) j / base);

        double p = (ratio - a) * base;

        int c0 = Color.parseColor(palette[i]);
        int c1 = Color.parseColor(palette[j]);

        //c0 = adjustColor(c0, saturation(dynamic), brightness(dynamic));
        //c1 = adjustColor(c1, saturation(dynamic), brightness(dynamic));

        int r = Color.red(c0) + (int) Math.round(p * (Color.red(c1) - Color.red(c0)));
        int g = Color.green(c0) + (int) Math.round(p * (Color.green(c1) - Color.green(c0)));
        int b = Color.blue(c0) + (int) Math.round(p * (Color.blue(c1) - Color.blue(c0)));

        int color = Color.rgb(r, g, b);

        return color;
    }

    /**
     * Given a color, adjust its brightness and saturation.
     * <p/>
     * TODO: prevent values from exceeding 1 or going below 0.
     *
     * @param color color integer
     * @param s     saturation factor
     * @param b     brightness factor
     * @return adjusted colors
     */
    private int adjustColor(int color, double s, double b) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[1] = (float) (s * hsv[1]);
        hsv[2] = (float) (b * hsv[2]);
        return Color.HSVToColor(hsv);
    }

    /**
     * Given a color ratio and a time ratio, produce the corresponding color.
     *
     * @param ratio value 0 to 1
     * @return color integer
     */
    protected int colorRGB(double ratio) {
        //if (splitDay) {
        //    ratio = rotate(ratio, 0.25);
        //}

        ratio = rotate(ratio, 0.66666);

        double[] hsv = new double[3];
        hsv[0] = 360 * ratio; //colorCorrect(ratio);
        hsv[1] = 0.93; //* saturation(dynamic);
        hsv[2] = 0.93; //* brightness(dynamic);
        return colorFromHSV(hsv);
    }

    /**
     * Given a color ratio and a time ratio, produce the corresponding color.
     *
     * @param ratio value 0 to 1
     * @return color integer
     */
    protected int colorBW(double ratio) {
        int c = colorRGB(ratio);

        // 0.21 R + 0.72 G + 0.07 B
        int v = (int) ((Color.red(c) * 0.21) + (Color.green(c) * 0.72) + (Color.blue(c) * 0.07));

        return Color.rgb(v, v, v);
    }

    /**
     * Convert color ratio to Java integer color.
     *
     * @param ratio value 0 to 1
     * @param s     saturation (0 to 1)
     * @param v     brightness (0 to 1)
     * @return color integer
     */
    private int color(double ratio, double s, double v) {
        double[] hsv = new double[3];
        hsv[0] = 360 * colorCorrect(ratio);
        hsv[1] = s;
        hsv[2] = v;
        return colorFromHSV(hsv);
    }

    public int daylight(int color) {
        return daylight(color, dayRatio());
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
     * @param dayRatio  time of day as ratio
     * @return          color integer
     */
    public int daylight(int color, double dayRatio) {
        double s = saturation(dayRatio);
        double v = brightness(dayRatio);
        //log("color: " + colorWheel.hexString(color) + " ratio: " + ratio + " sat: " + s + " brt: " + v);
        return adjustColor(color, s, v);
    }

    /**
     * @param ratio color ratio
     * @return saturation level
     */
    private double saturation(double ratio) {
        double a = absSin(ratio);
        return a * 0.3 + 0.7;
        //return a * (1.0 - daylightFactor) + daylightFactor;
    }

    /**
     * @param ratio color ratio
     * @return brightness level
     */
    private double brightness(double ratio) {
        double a = absSin2(ratio);
        return a * 0.4 + 0.6;
        //return a * (1.0 - daylightFactor) + daylightFactor;
    }

    public int complementaryColor(int color) {
        return Color.rgb(255 - Color.red(color), 255 - Color.green(color), 255 - Color.blue(color));
    }

    /**
     * Convert a color given in Hue/Saturation/Value form to Java integer color.
     *
     * @param hsv   array of hue, saturation and brightness
     * @return      color integer
     */
    private int colorFromHSV(double hsv[]) {
        return Color.HSVToColor(toFloatArray(hsv));
    }

    private int colorFromHSV(float hsv[]) {
        return Color.HSVToColor(hsv);
    }

    /**
     * Color spectrum (IMHO) is a bit wonky. This function works to skew the spectrum
     * to make it more suited to human perception. Basically it expands the orange
     * and yellow area of the spectrum a bit, while shrinking the green area.
     *
     * @param ratio     color ratio
     * @return          skewed ratio
     */
    private double colorCorrect(double ratio) {
        //ratio = rotate(ratio, (splitDay ? 0.33 : 0.34));
        ratio = degreen(ratio);
        return ratio;
    }

    /**
     * TODO: Figure out a way to make green not take up quite as much of the color wheel.
     *
     * @param ratio
     * @return
     */
    //private double degreenX(double ratio) {
    //    double g = gauss(ratio * 5, 2, 1);
    //    double c = ratio * (1d - g);
    //    log("RATIO: " + ratio + " GAUSS: " + g + " COLOR: " + c);
    //    return c;
    //}

    private static final double DEGREEN_FACTOR1 = 2.75;
    private static final double GREEN = 0.65;

    /**
     * Compress the green portion of the color wheel.
     *
     * @param r     color as ratio on color wheel
     * @return      color form de-greened color wheel
     */
    private double degreen(double r) {
        double gf0 = DEGREEN_FACTOR1;
        //double gf1 = DEGREEN_FACTOR2;
        double q;

        q = (asinh((r - GREEN) * gf0) + asinh(GREEN * gf0)) / (asinh((1 - GREEN) * gf0) + asinh(GREEN * gf0));
        ////double q = (asinh(r * 2 * d - d) + asinh(d)) / (2 * asinh(d));
        //log("RATIO: " + r + " DEGREEN RATIO: " + q);
        return q;
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
     * Cool! We are using Hyperbolic Arcsine!!!
     *
     * @param x     value
     * @return      asinh(value)
     */
    private double asinh(double x) {
        return Math.log(x + Math.sqrt(x*x + 1.0));
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
     * Takes the absolute value of the sin of the given ratio. This produces a sinusoidal value
     * from 0 to 1 to 0 to 1 to 0, for a given value between 0 and 1.
     *
     * @param ratio     ratio between 0 and 1
     * @return          sinusoidal value
     */
    private double absSin(double ratio) {
        return(Math.abs(Math.sin(2 * Math.PI * ratio)));
    }

    /**
     * Narrow a range by a give ratio. For example if we have values between 0 and 100,
     * we can narrow the range such that they evenly translate to a range between 10 and 90,
     * using `narrow(val, 0.1)`.
     *
     * @param val   value to translate
     * @param per   degree of narrowing
     * @return      narrowed value
     */
    private double narrow(double val, double per) {
        return((val * (1 - per)) + (per / 2));
    }

    /**
     * Shift a ratio right or left (positive or negative) by given amount.
     *
     * FIXME: Currently ratio and shift have to be between 0 to 1.
     *
     * @param ratio     ratio to shift
     * @param shift     amount to shift (0 to 1)
     * @return          shifted ratio
     */
    private double rotate(double ratio, double shift) {
        double r = ratio + shift;
        if (r > 1) {
            return (r - 1); //(r - ((int) r));
        } else if (r < 0) {
            return (r + 1);
        } else {
            return r;
        }
    }

    private String[] rotate(String[] a, int n) {
        String[] x = new String[a.length];
        for(int i = 0; i < a.length; i++) {
            x[(i + n) % a.length] = a[i];
        }
        return x;
    }

    /**
     * Normalize color in 0 to 255 range.
     *
     * @param color     integer color
     * @param m
     * @return
     */
    private int normalize(int color, double m) {
        color = (int) Math.floor((m + color) * 255);
        if (color < 0) {
            color = 0;
        }
        return color;
    }

    /**
     * Gaussian distribution.
     *
     * @param x     value along x-axis
     * @param m     offset
     * @param s     standard deviation
     * @return      value along y-axis
     */
    private double gauss(double x, double m, double s) {
        double bas = 1 / (s * SQRT2PI);
        double exp = -(sqr(x - m) / (2 * sqr(s)));
        return bas * Math.pow(E, exp);
    }

    private final double E = Math.E;
    private final double SQRT2PI = Math.sqrt(2 * Math.PI);

    /**
     * Shorthand for taking the square, i.e. `Math.pow(d, 2)`.
     *
     * @param d     number to square
     * @return      square of given number
     */
    private double sqr(double d) {
        return Math.pow(d, 2);
    }

    /**
     * Create hex code for a color.
     *
     * @param color color in int representation
     * @return hex code for color
     */
    public String hexString(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format("#%02x%02x%02x", r, g, b);
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

    /**
     * Time of the day represented as a ratio between 0 to 1.
     *
     * @return  time of day as a ratio
     */
    protected double dayRatio() {
        return (1.0 / MILLISECONDS_IN_DAY) * millisecondsSinceMidnight();
    }

    /**
     * Calculate the milliseconds since midnight.
     *
     * @return milliseconds since midnight
     */
    protected long millisecondsSinceMidnight() {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return now - c.getTimeInMillis();
    }

    protected static final int MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;

}
