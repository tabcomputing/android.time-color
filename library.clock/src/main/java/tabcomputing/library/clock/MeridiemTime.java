package tabcomputing.library.clock;

import java.util.ArrayList;
import java.util.Calendar;

import tabcomputing.library.color.ColorWheel;

public class MeridiemTime extends AbstractTime {

    final int[] TIME_SEGMENTS  = {24, 60, 60};
    final int[] CLOCK_SEGMENTS = {12, 60, 60};

    public int[] timeSegments() {
        return TIME_SEGMENTS;
    }
    public int[] clockSegments() {
        return CLOCK_SEGMENTS;
    }

    @Override
    public int hoursOnClock() {
        return 12;
    }

    @Override
    public boolean isDaySplit() {
        return true;
    }

    /**
     * Calculate standard time. This is a 24-hr clock!!!
     *
     * TODO: Should this be a 12-hr clock?
     *
     * @return array of integers
     */
    @Override
    public int[] time(int upto) {
        if (upto < 0) { upto = end() + upto; }

        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        int s = c.get(Calendar.SECOND);

        int[] time = new int[upto + 1];

        switch (upto) {
            case 2:
                time[2] = s;
            case 1:
                time[1] = m;
            case 0:
                time[0] = h;
        }
        return time;
    }

    /**
     * Returns time as an array or base-converted and formatted segments.
     *
     * @return      time array
     */
    @Override
    public String[] timeRebased(int upto, boolean ampm) {
        if (upto < 0) { upto = end() + upto; }

        int[] t = time(upto);

        t[0] = mod(t[0] - 12, 12);

        String[] baseTime = new String[upto];

        for(int i=0; i <= upto; i++) {
            baseTime[i] = fmtSegment(base(t[i]), i);
        }

        return baseTime;
    }

    /**
     * We override this b/c standard time minutes and seconds are padding by zero.
     *
     * @param segment       time segment as a string
     * @param index         which segment of time is it
     *
     * @return              formatted time segment
     */
    @Override
    protected String fmtSegment(String segment, int index) {
        if (index == 0) {
            return segment; //"00".substring(segment.length()) + segment;
        } else {
            return "00".substring(segment.length()) + segment;
        }
    }

    /**
     * Ratios for placement of clock hands. Usually this is no different that timeRatios(), but
     * in the case of StandardTime for instance, the day is split into two full rotations,
     * i.e. AM and PM.
     *
     * @return  array of clock hand ratios
     */

    @Override
    public double[] handRatios(int upto) {
        double[] r = timeRatios(upto);
        r[0] = reduce(r[0] * 2);
        return r;
    }

    /**
     * Get a string representation of the time.
     *
     * @return string of time
     */
    @Override
    public String timeStamp(boolean sansSeconds) {
        String stamp;

        int[] t;
        int h;

        t = time();
        h = t[0];

        if (h > 12) { h = (h % 12); }
        if (h == 0) { h = 12; }

        stamp = String.format("%d:%02d", h, t[1]);

        if (!sansSeconds) {
            stamp = stamp + ":" + String.format("%02d", t[2]);
        }

        // TODO: if display AM/PM
        //  stamp += (t[0] > 11) ? "PM" : "AM";

        return stamp;
    }

    @Override
    public String timeStampFormatted(int range) {
        String stamp;
        int[] t = time();
        ArrayList<String> s = new ArrayList<String>();

        switch (range) {
            case TIMESTAMP_NO_HOUR:
                stamp = String.format("%02d:%02d", t[1], t[2]);
                break;
            case TIMESTAMP_NO_SECONDS:
                stamp = String.format("%d:%02d", t[0], t[1]);
                break;
            case TIMESTAMP_NO_HOUR_OR_SECONDS:
                stamp = String.format("%02d", t[1]);
                break;
            case TIMESTAMP_HOUR_ONLY:
                stamp = String.format("%d", t[0]);
                break;
            default:
                stamp = String.format("%d:%02d:%02d", t[0], t[1], t[2]);
        }

        return stamp;
    }

    /**
     * Colors for clock face.
     *
     * @param cw        color wheel instance
     * @param duplex    duplex mode
     * @return          array of colors
     *
    @Override
    public int[] hourColors(ColorWheel cw, boolean duplex) {
        if (duplex) {
            int[] c = cw.colors(hoursOnClock());
            return merge(c, c);
        } else {
            return cw.colors(hoursInDay());
        }
    }
    */

    /**
     * Get a color array where the colors are split between night and day, and we ony
     * want the relevant set for the particular time of day.
     *
     * @return      array of colors
     */
    private int[] splitColors(ColorWheel cw) {
        int hd = hoursInDay();
        int hc = hoursOnClock();

        int half = hd / 2;
        int qtr  = hd / 4;

        double r;

        int i, j, k, s;
        int[] colors24;

        colors24 = cw.colors(hd);

        int h = (int) (timeRatios()[0] * hd);

        if (h < hc - qtr || h >= hc + qtr) {
            s = half;
        } else {
            s = 0;
        }

        int q = half + mod(h + qtr, hc);

        //log("Q: " + q);

        int[] colors = new int[12];

        for(i=0; i < q; i++) {
            j = mod(i + s, hd);

            //r = ((double) j) / hc;
            k = mod(j, hc);

            colors[k] = colors24[j];
        }

        return colors;
    }

    /**
     * TODO: Use this for splitColors().
     *
     * @return      array of indexes
     */
    public int[] getSequence() {
        int i, add;

        int hour   = hour();
        int hours  = hoursInDay();
        int length = hoursOnClock();

        if (mod(hours, length) != 0) {
            throw new ArithmeticException("number of hours must be multiple of length");
        }

        int[] result = new int[length];

        add = hour + hours - (length / 2) - (mod(length,2)) + 1;

        for (i = 0; i < length; i++) {
            result[mod(add + i, length)] = mod(add + i, hours);
        }

        return result;
    }

}
