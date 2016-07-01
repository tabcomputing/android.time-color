package tabcomputing.library.clock;

import tabcomputing.library.color.ColorWheel;

public interface TimeSystem {

    double ratioTime();

    /**
     * Each time system decomposes the day into a set of time segments.
     * For example, standard time divides the day into 24 hours, each
     * of which is divided into 60 minutes, which in turn are divided
     * into 60 seconds. So this method would return `{24, 60, 60}` if
     * traditional time is selected in settings.
     */
    int[] timeSegments();

    /**
     * Like timeSegments() but is the number of segments shown on a
     * clock face. So for instance, traditional time would be `{12,60,60}`.
     */
    int[] clockSegments();

    /**
     * The current time as a series of ratios, one for each time segment.
     */
    double[] timeRatios();

    /**
     * Number of hours in the day.
     */
    int hoursInDay();

    /**
     * Number of hours on the clock.
     */
    int hoursOnClock();

    /**
     * Number of minutes on the clock.
     */
    int minutesOnClock();

    boolean isDaySplit();

    String timeStamp(boolean withSeconds);
    String timeStamp(int flags);

    String timeStampSample(boolean withSeconds);

    int[] time();

    String[] timeRebased();

    int timeBase();

    // TODO: Perhaps replace with timeColors() returning an array?
    //       There would be a ratios base version and a discrete version?
    //int hourColor();
    //int minutiaColor();

    /**
     *
     * @return
     */
    double[] handRatios();

    double standardSecond();

    int tickTime(boolean second);

    double[] discreteRatios();

    String[] clockNumbers(int type);

    void setOffset(int offset);
    void setOffset(double ratio);

    void setBaseConverted(boolean convert);
    boolean isBaseConverted();

    int gmtOffset();

    /**
     * Colors for each hour on the clock.
     *
     * @param cw        ColorWheel instance
     * @return          array of color integers
     */
    int[] clockColors(ColorWheel cw);

    int[] timeColors(ColorWheel cw);

}
