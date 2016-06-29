package tabcomputing.library.clock;

import java.util.ArrayList;
import java.util.Arrays;

public class HeximalTime extends AbstractTime {

    final int[] TIME_SEGMENTS = {36, 36, 36};

    //final String[] COLORS_HEX = {
    //    "FFF200", "F7CE15", "FBB416", "FB9404", "F47521", "EF5524",
    //    "ED2024", "EB2030", "EB1B3C", "EC1F4C", "ED195C", "ED0F71",
    //    "EA098B", "B91E8C", "92298D", "792D8D", "63308E", "483190",
    //    "2B3F81", "214297", "05509F", "005FAB", "0072B9", "0093D7",
    //    "00ABEB", "00ABCA", "00A79C", "14AB89", "00A76D", "00A55D",
    //    "00A551", "08AD4C", "55B948", "81C342", "A4CE39", "C5D92F"
    //};

    public int[] timeSegments() {
        return TIME_SEGMENTS;
    }

    @Override
    public int minutesOnClock() {
        return 36 * 3;
    }

    public int timeBase() {
        return 6;
    }

    @Override
    public String timeStamp(boolean withSeconds) {
        int[] t;
        ArrayList<String> s = new ArrayList<String>();

        t = time();

        s.add(base(t[0], "00"));
        s.add(base(t[1], "00"));
        if (withSeconds) {
            s.add(base(t[2], "00"));
        }

        return joinTime(s);
    }

    @Override
    public String timeStamp(int range) {
        int[] t = time();
        ArrayList<String> s = new ArrayList<String>();

        switch (range) {
            case TIMESTAMP_NO_HOUR:
                s.add(base(t[1], "00"));
                s.add(base(t[2], "00"));
                break;
            case TIMESTAMP_NO_SECONDS:
                s.add(base(t[0], "00"));
                s.add(base(t[1], "00"));
                break;
            case TIMESTAMP_NO_HOUR_OR_SECONDS:
                s.add(base(t[1], "00"));
                break;
            case TIMESTAMP_HOUR_ONLY:
                s.add(base(t[0], "00"));
                break;
            default:
                s.add(base(t[0], "00"));
                s.add(base(t[1], "00"));
                s.add(base(t[2], "00"));
        }
        return joinTime(s);
    }

    @Override
    public String timeStampSample(boolean withSeconds) {
        if (isBaseConverted()) {
            return (withSeconds ? "44 44 44" : "44 44");
        } else {
            return (withSeconds ? "44 44 44" : "44 44");
        }
    }

}
