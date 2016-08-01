package tabcomputing.library.clock;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import tabcomputing.library.color.ColorWheel;


    /*
    private final int[] SEGMENTS_BIHEXADECIMAL = {16, 16, 16, 16};
    private final int[] SEGMENTS_BIHEXIMAL     = {12, 36, 216};
    */

public abstract class AbstractTime implements TimeSystem {

    public AbstractTime() {}

    protected static final int SECONDS_IN_DAY = 24 * 60 * 60;
    protected static final int MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;

    protected static final int TIMESTAMP_NO_HOUR = 0x1;
    protected static final int TIMESTAMP_NO_SECONDS = 0x2;
    protected static final int TIMESTAMP_NO_HOUR_OR_SECONDS = 0x3;
    protected static final int TIMESTAMP_HOUR_ONLY = 0x4;

    protected static final String[] NUMBERS_ROMAN = {
            "0", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI",
            "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI", "XXII", "XXIII",
            "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX", "XXXI", "XXXII", "XXXIII",
            "XXXIV", "XXXV", "XXXVI"
    };

    protected int hourOffset = 0;

    public void setOffset(int offset) {
        hourOffset = offset;
    }

    public void setOffset(double ratio) {
        hourOffset = (int) (hoursInDay() * ratio);
    }

    // TODO: Do we really need this as state? Maybe just separate methods would be better.

    private boolean baseConvert = true;

    public boolean isBaseConverted() {
        return baseConvert;
    }

    public void setBaseConverted(boolean convert) {
        baseConvert = convert;
    }

    public int timeBase() {
        return 10;
    }

    /**
     *
     * @param sansSeconds       leave off seconds
     * @return                  array of time segments
     */
    public int[] timeSegments(boolean sansSeconds) {
        if (sansSeconds) {
            Arrays.copyOf(timeSegments(), size() - 1);
        }
        return timeSegments();
    }

    /**
     * Typically, clock segments are the same as time segments.
     */
    public int[] clockSegments(boolean sansSeconds) {
        return timeSegments(sansSeconds);
    }
    public int[] clockSegments() {
        return timeSegments();
    }

    public int hoursInDay() {
        return timeSegments()[0];
    }

    public int hoursOnClock() {
        return timeSegments()[0];
    }

    public int minutesOnClock() {
        return timeSegments()[1];
    }

    //abstract public int timeBase();

    /**
     * Time of the day represented as a ratio between 0 to 1.
     *
     * @return time as a ratio
     */
    public double ratioTime() {
        return (1.0 / MILLISECONDS_IN_DAY) * millisecondsSinceMidnight();
    }

    /**
     * Calculate the milliseconds since midnight.
     *
     * @return milliseconds since midnight
     */
    private long millisecondsSinceMidnight() {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return now - c.getTimeInMillis();
    }

    /**
     * Compute the length of one "second" of this time system in standard seconds.
     *
     * @return seconds
     */
    public double standardSecond() {
        int[] ts = timeSegments();
        int f = 1;
        for (int s : ts) {
            f = f * s;
        }
        return (double) SECONDS_IN_DAY / f;
    }

    /**
     * Duration of time, in standard milliseconds, between ticks of the clock. If by seconds,
     * this is the number of standard milliseconds in "one second" of this time system.
     * Otherwise it is the number of standard milliseconds in "one minute" of this time
     * system.
     *
     * @return  number in milliseconds
     */
    public int tickTime(boolean second) {
        if (second) {
            return (int) (standardSecond() * 1000);
        } else {
            int[] s = timeSegments();
            return (int) (standardSecond() * s[s.length - 1] * 1000);
        }
    }

    /**
     * Some time systems, like the traditional system, count the day in two parts,
     * normally referred to as AM and PM.
     *
     * @return true if day is counted in two parts
     */
    public boolean isDaySplit() {
        return false;
    }

    /**
     * @param withSeconds include seconds in stamp
     * @return time stamp example
     */
    public String timeStampSample(boolean withSeconds) {
        if (withSeconds) {
            return "44:44:44";
        } else {
            return "44:44";
        }
    }

    /**
     * Numbers to put on the clock face.
     *
     * @param type type of clock numbers
     * @return array of strings
     */
    @Override
    public String[] clockNumbers(int type, int h) {
        //int h = hoursOnClock();

        String[] n = new String[h];
        switch (type) {
            case 2:
                n = Arrays.copyOf(NUMBERS_ROMAN, h);
                n[0] = NUMBERS_ROMAN[h];
                break;
            case 1:
                for (int i = 0; i < h; i++) {
                    n[i] = "" + base(i);
                }
                break;
            default:
                n[0] = "" + base(h);
                for (int i = 1; i < h; i++) {
                    n[i] = "" + base(i);
                }
                break;
        }
        return n;
    }

    public String[] clockNumbers(int type) {
        return clockNumbers(type, hoursOnClock());
    }

    /**
     * Return the current time as an array of integers, one for each time segment
     * of the active time system.
     *
     * The `upto` number is the segment up to which to calculate. If negative it
     * will be subtracted from the segment count.
     *
     * @param upto  calculate time up to segment number
     * @return      time as array of integers
     */
    public int[] time(int upto) {
        if (upto < 0){ upto = end() + upto; }
        int size = upto + 1;

        int[] s = timeSegments();
        double r = ratioTime();

        int[] time = new int[size];

        int n; double v;

        for (int i = 0; i < size; i++) {
            v = r * s[i];
            n = (int) v;
            r = v - n;
            time[i] = n;
        }
        return time;
    }

    public int[] time(boolean withoutSeconds) {
        if (withoutSeconds) {
            return time(-1);
        }
        return time(end());
    }

    /**
     * TODO: Could make this more efficient?
     *
     * @return              segment of time
     */
    public int[] time() {
        return time(end());
    }

    public int hour() {
        return time(0)[0];
    }

    public int minute() {
        return time(1)[1];
    }

    public int second() {
        int end = end();
        return time(end)[end];
    }

    /**
     *
     * @return
     */
    public int size() {
        return timeSegments().length;
    }

    /**
     *
     * @return
     */
    protected int end() {
        return size() - 1;
    }

    /**
     * Returns time as an array or base-converted and formatted segments.
     *
     * @return      time array
     */
    public String[] timeRebased(int upto) {
        if (upto < 0){ upto = end() + upto; }
        int size = upto + 1;

        int[] t = time();

        String[] baseTime = new String[size];

        for(int i=0; i < size; i++) {
            baseTime[i] = fmtSegment(base(t[i]), i);
        }

        return baseTime;
    }

    public String[] timeRebased(boolean withoutSeconds) {
        if (withoutSeconds) {
            return timeRebased(-1);
        }
        return timeRebased(end());
    }

    public String[] timeRebased() {
        return timeRebased(end());
    }

    public String timeStamp() {
        return timeStamp(false);
    }

    public String timeStampSample() {
        return timeStampSample(false);
    }

    /**
     * Format a time segment. The segment is passed as a string because it will have already
     * been base converted.
     *
     * @param segment       time segment as a string
     * @param index         which segment of time is it
     * @return              formatted segment
     */
    protected String fmtSegment(String segment, int index) {
        return segment;
    }

    /**
     *
     * @param segments
     * @return
     */
    protected String joinTime(ArrayList<String> segments) {
        if (isBaseConverted()) {
            return TextUtils.join(" ", segments);
        } else {
            return TextUtils.join(":", segments);
        }
    }

    /**
     * Return the current time as a series of ratios, one for each time segment of the
     * active time system.
     *
     * @return series of time ratios
     */
    public double[] timeRatios(int upto) {
        if (upto < 0){ upto = end() + upto; }
        int size = upto + 1;

        int[] s = timeSegments();
        double r = ratioTime(); //(double) sinceMidnightMillis() / MILLISECONDS_IN_DAY;

        int n; double v;

        double[] time = new double[size];

        for (int i = 0; i < size; i++) {
            time[i] = r;
            v = r * s[i];
            n = (int) v;
            r = v - n;
        }
        return time;
    }

    public double[] timeRatios(boolean withoutSeconds) {
        if (withoutSeconds) {
            return timeRatios(-1);
        }
        return timeRatios(end());
    }

    public double[] timeRatios() {
        return timeRatios(end());
    }

    /**
     * Ratios for placement of clock hands. Usually this is no different that timeRatios(), but
     * in the case of MeridiemTime, for instance, the day is split into two full rotations,
     * i.e. AM and PM.
     *
     * TODO: What to do about `upto` here?
     *
     * @return  array of clock hand ratios
     */
    public double[] handRatios(int upto) {
        return timeRatios(upto);
    }
    public double[] handRatios(boolean sansSeconds) {
        if (sansSeconds) {
            return handRatios(-1);
        }
        return handRatios(end());
    }
    public double[] handRatios() {
        return handRatios(end());
    }

    /**
     * Produces an array of ratios, one for each time segment. Unlike the other methods this
     * produces each ratio based solely on it's own segment value.
     *
     * TODO: Take day split into account?
     *
     * @return array of colors
     */
    public double[] discreteRatios(int upto) {
        if (upto < 0) { upto = end() + upto; }
        int size = upto + 1;

        int[] t = time();
        int[] s = timeSegments();

        double[] r = new double[size];

        for (int i = 0; i < size; i++) {
            r[i] = (double) t[i] / s[i];
        }

        // TODO: make this optional?
        if (isDaySplit()) {
            r[0] = reduce(r[0] * 2);
        }

        return r;
    }

    public double[] discreteRatios(boolean withoutSeconds) {
        if (withoutSeconds) {
            return discreteRatios(end() - 1);
        }
        return discreteRatios(end());
    }

    public double[] discreteRatios() {
        return discreteRatios(end());
    }

    /**
     * @deprecated  Hour hand ratio.
     *
     * @return hour hand ratio
     */
    public double hourHandRatio() {
        double q;
        double offset = 1.0 / (2 * hoursOnClock());

        if (isDaySplit()) {
            double d = ratioTime() * 2;
            int n = (int) d;
            q = (d - n);
        } else {
            q = ratioTime();
        }
        return reduce(q - offset);
    }

    /**
     * @deprecated  The minute ratio is the exact time passed as a ratio of an hour.
     *
     * @return minute hand ratio
     */
    public double minuteHandRatio() {
        //double offset = 1.0 / (2 * hoursOnClock());
        double r = ratioTime();
        double p = r * hoursInDay();
        int h = (int) p;
        double q = (p - h);
        return q; //reduce(q - offset);
    }

    /**
     * @deprecated Better to call ColorWheel on timeSystem.handRatios() ?
     *
     * @param cw        instance of ColorWheel
     * @return          array of color integers
     */
    public int[] timeColors(ColorWheel cw) {
        double[] cr = handRatios();
        return cw.colors(cr);
    }

    /**
     * Array of color to put on clock face. The color wheel it simply divided up
     * into equal segments, one for each hour on the clock face.
     *
     * @param cw        instance of ColorWheel
     * @return          array of color integers
     */
    public int[] clockColors(ColorWheel cw) {
        return cw.colors(hoursOnClock());
    }

    /**
     * Simply returns an sequential array for the number of hours in the day.
     * Note, if hoursInDay() != hoursOnClock, this must be overridden and replaced
     * with the method as it is defined in MeridiemTime.
     *
     * TODO: Generalize for MeridiemTime too.
     *
     * @return      sequence array
     */
    public int[] getSequence() {
        // TODO: raise error if hoursInDay() != hoursOnClock()
        int hours = hoursInDay();
        int[] result = new int[hours];
        for(int i=0; i < hoursInDay(); i++) {
            result[i] = i;
        }
        return result;
    }

    /**
     * @return      number converted to settings base
     */
    protected String base(int number) {
        return base(number, timeBase());
    }

    /**
     * @return      number converted to given base
     */
    protected String base(int number, int base) {
        if (isBaseConverted()) {
            return Integer.toString(number, base).toUpperCase();
        } else {
            return "" + number;
        }
    }

    /**
     * @param pad   number of zeros to pad number with (max 5)
     * @return      number converted to given base
     */
    protected String base(int number, String pad) {
        if (isBaseConverted()) {
            String s = pad + Integer.toString(number, timeBase()).toUpperCase();
            return s.substring(s.length() - pad.length());
        } else {
            return "" + number;
        }
    }

    /**
     * @param pad   number of zeros to pad number with (max 5)
     * @return      number converted to given base
     */
    protected String base(int number, int base, String pad) {
        if (isBaseConverted()) {
            String s = pad + Integer.toString(number, base).toUpperCase();
            return s.substring(s.length() - pad.length());
        } else {
            return "" + number;
        }
    }

    /**
     * TODO: better name for this method
     *
     * @param ratio ratio to reduce
     * @return ratio reduced to 0 to 1
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

    /**
     * Get the GMT offset for the current system time.
     *
     * @return      timezone offset
     */
    public int gmtOffset() {
        //Calendar mCalendar = new GregorianCalendar();
        TimeZone tz = TimeZone.getDefault();
        //TimeZone mTimeZone = mCalendar.getTimeZone();
        int offset = tz.getRawOffset();
        return (int) TimeUnit.HOURS.convert(offset, TimeUnit.MILLISECONDS);
    }

    /**
     * Real modulo.
     *
     * @param n     numerator
     * @param d     denominator
     * @return      modulus
     */
    protected int mod(int n, int d) {
        if (n < 0) {
            return (d + (n % d));
        } else {
            return (n % d);
        }
    }

    /**
     * Rotate array of integers.
     *
     * @param a     array to rotate
     * @param n     degree of rotation
     * @return      new array
     */
    protected int[] rotate(int[] a, int n) {
        int[] x = new int[a.length];
        for(int i = 0; i < a.length; i++) {
            x[i] = a[(i + n) % a.length];
        }
        return x;
    }

}