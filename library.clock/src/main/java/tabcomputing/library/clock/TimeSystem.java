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
    int[] timeSegments(boolean sansSeconds);

    /**
     * Like timeSegments() but is the number of segments shown on a
     * clock face. So for instance, Meridiem time would be `{12,60,60}`
     * instead of `{24,60,60}`.
     */
    int[] clockSegments();
    int[] clockSegments(boolean sansSeconds);

    /**
     * The current time as a series of ratios, one for each time segment.
     */
    double[] timeRatios();
    double[] timeRatios(int upto);
    double[] timeRatios(boolean sansSeconds);

    /**
     *
     */
    double[] handRatios();
    double[] handRatios(int upto);
    double[] handRatios(boolean sanSeconds);

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

    String timeStamp();
    String timeStamp(boolean sansSeconds);

    String timeStampSample();
    String timeStampSample(boolean sansSeconds);

    String timeStampFormatted(int flags);

    /**
     * Get the current time as an array of integers, one for each segment.
     */
    int[] time();
    int[] time(int upto);
    int[] time(boolean withSeconds);

    /**
     * Get the current time as an array of base converted integers, one for each segment.
     */
    String[] timeRebased();
    String[] timeRebased(boolean ampm);
    String[] timeRebased(int upto, boolean ampm);

    /**
     * Intrinsic number base.
     */
    int timeBase();

    // TODO: Perhaps replace with timeColors() returning an array?
    //       There would be a ratios base version and a discrete version?
    //int hourColor();
    //int minutiaColor();

    double standardSecond();

    int tickTime(boolean second);

    double[] discreteRatios();
    double[] discreteRatios(int upto);
    double[] discreteRatios(boolean sansSeconds);

    String[] clockNumbers(int type);
    String[] clockNumbers(int type, int hours);

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

    int[] getSequence();

}
