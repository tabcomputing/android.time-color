package tabcomputing.library.clock;

import java.util.ArrayList;
import java.util.Calendar;

import tabcomputing.library.color.ColorWheel;

public class StandardTime extends AbstractTime {

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
     * @return array of integers
     */
    @Override
    public int[] time() {
        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        int s = c.get(Calendar.SECOND);

        int[] time = new int[3];
        time[0] = h;
        time[1] = m;
        time[2] = s;

        return time;
    }

    /**
     * Ratios for placement of clock hands. Usually this is no different that timeRatios(), but
     * in the case of StandardTime for instance, the day is split into two full rotations,
     * i.e. AM and PM.
     *
     * @return  array of clock hand ratios
     */
    @Override
    public double[] handRatios(boolean offset) {
        double[] r = timeRatios();

        double adj;
        if (offset) {
            adj = 1.0 / (2 * hoursOnClock());
        } else {
            adj = 0;
        }

        r[0] = reduce((r[0] * 2) - adj);

        return r;
    }

    /**
     * Get a string representation of the time.
     *
     * @return string of time
     */
    @Override
    public String timeStamp(boolean withSeconds) {
        String stamp;

        int[] t;
        int h;

        t = time();
        h = t[0];

        if (h > 12) { h = (h % 12); }
        if (h == 0) { h = 12; }

        stamp = String.format("%d:%02d", h, t[1]);

        if (withSeconds) {
            stamp = stamp + ":" + String.format("%02d", t[2]);
        }

        // TODO: if display AM/PM
        //  stamp += (t[0] > 11) ? "PM" : "AM";

        return stamp;
    }

    @Override
    public String timeStamp(int range) {
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
     * TODO: % should be mod()?
     *
     * TODO: Use this for splitColors().
     */
    int[] getColorIndexes(int hour, int hours, int length) {
        int i, add;

        int[] result = new int[length];

        //if (hours % length) throw "number of hours must be multiple of length";

        add = hour + hours - (length / 2) - (length % 2) + 1;
        for (i = 0; i < length; i++) {
            result[(add + i) % length] = (add + i) % hours;
        }
        return result;
    }

}
