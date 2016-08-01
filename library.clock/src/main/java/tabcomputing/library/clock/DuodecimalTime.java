package tabcomputing.library.clock;

import java.util.ArrayList;

public class DuodecimalTime extends AbstractTime {

    final int[] TIME_SEGMENTS = {12, 12, 12, 12};

    public int[] timeSegments() {
        return TIME_SEGMENTS;
    }

    @Override
    public int timeBase() {
        return 12;
    }

    public String timeStamp(boolean sansSeconds) {
        int[] t;
        ArrayList<String> s = new ArrayList<String>();

        t = time();

        s.add(base(t[0]));
        s.add(base(t[1]));
        s.add(base(t[2]));
        if (!sansSeconds) {
            s.add(base(t[3]));
        }

        return joinTime(s).toUpperCase();
    }

    @Override
    public String timeStampFormatted(int range) {
        int[] t = time();
        ArrayList<String> s = new ArrayList<String>();

        switch (range) {
            case TIMESTAMP_NO_HOUR:
                s.add(base(t[1]));
                s.add(base(t[2]));
                s.add(base(t[3]));
                break;
            case TIMESTAMP_NO_SECONDS:
                s.add(base(t[0]));
                s.add(base(t[1]));
                s.add(base(t[2]));
                break;
            case TIMESTAMP_NO_HOUR_OR_SECONDS:
                s.add(base(t[1]));
                s.add(base(t[2]));
                break;
            case TIMESTAMP_HOUR_ONLY:
                s.add(base(t[0]));
                break;
            default:
                s.add(base(t[0]));
                s.add(base(t[1]));
                s.add(base(t[2]));
                s.add(base(t[3]));
        }

        return joinTime(s).toUpperCase();
    }

    @Override
    public String timeStampSample(boolean sansSeconds) {
        if (isBaseConverted()) {
            return (sansSeconds ? "A 4 A" : "A 4 A 4");
        } else {
            return (sansSeconds ? "44 44 44" : "44 44 44 44");
        }
    }

}
