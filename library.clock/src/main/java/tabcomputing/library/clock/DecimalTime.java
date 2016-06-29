package tabcomputing.library.clock;

public class DecimalTime extends AbstractTime {

    final int[] TIME_SEGMENTS = {10, 100, 100};

    public int[] timeSegments() {
        return TIME_SEGMENTS;
    }

    @Override
    public String timeStamp(boolean withSeconds) {
        double r = ratioTime();
        if (withSeconds) {
            return String.format("%.4f", r * 10);
        } else {
            return String.format("%.2f", r * 10);
        }
    }

    @Override
    public String timeStamp(int range) {
        double r = ratioTime();
        switch (range) {
            case TIMESTAMP_NO_HOUR:
                return String.format("%.4f", ((r * 10) - (int) (r * 10)));
            case TIMESTAMP_NO_SECONDS:
                return String.format("%.2f", r * 10);
            case TIMESTAMP_NO_HOUR_OR_SECONDS:
                return String.format("%.2f", ((r * 10) - (int) (r * 10)));
            case TIMESTAMP_HOUR_ONLY:
                return String.format("%d", (int) (r * 10));
            default:
                return String.format("%.4f", r * 10);
        }
    }

    @Override
    public String timeStampSample(boolean withSeconds) {
        return (withSeconds ? "4.4444" : "4.444");
    }

}