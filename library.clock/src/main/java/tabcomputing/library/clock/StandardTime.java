package tabcomputing.library.clock;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class StandardTime extends AbstractTime {

    final int[] TIME_SEGMENTS = {24, 60, 60};

    public int[] timeSegments() {
        return TIME_SEGMENTS;
    }

    /**
     * Calculate military time.
     *
     * @return  array of integers
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
     * Military time segments are always padded by zero.
     *
     * @param segment       time segment as a string
     * @param index         which segment of time is it
     * @return              formatted time segment
     */
    @Override
    protected String fmtSegment(String segment, int index) {
        return "00".substring(segment.length()) + segment;
    }

    /**
     * Get a string representation of the time.
     *
     * @return string of time
     */
    public String timeStamp(boolean sansSeconds) {
        String stamp;
        int[] t;

        t = time();

        stamp = String.format("%02d:%02d", t[0], t[1]);

        if (!sansSeconds) {
            stamp = stamp + ":" + String.format("%02d", t[2]);
        }

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
                stamp = String.format("%02d:%02d", t[0], t[1]);
                break;
            case TIMESTAMP_NO_HOUR_OR_SECONDS:
                stamp = String.format("%02d", t[1]);
                break;
            case TIMESTAMP_HOUR_ONLY:
                stamp = String.format("%02d", t[0]);
                break;
            default:
                stamp = String.format("%02d:%02d:%02d", t[0], t[1], t[2]);
        }

        return stamp;
    }

}
