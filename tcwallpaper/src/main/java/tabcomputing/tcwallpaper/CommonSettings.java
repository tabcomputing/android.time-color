package tabcomputing.tcwallpaper;

import android.graphics.Typeface;
import android.util.Log;

import tabcomputing.library.clock.DecimalTime;
import tabcomputing.library.clock.DuodecimalTime;
import tabcomputing.library.clock.HexadecimalTime;
import tabcomputing.library.clock.HeximalTime;
import tabcomputing.library.clock.MilitaryTime;
import tabcomputing.library.clock.StandardTime;
import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.color.ColorWheel;

/**
 * Common Pattern Settings
 */
public class CommonSettings extends AbstractSettings {

    public static final String KEY_COLOR_GAMUT    = "colorGamut";
    public static final String KEY_COLOR_DAYLIGHT = "colorDaylight";
    public static final String KEY_COLOR_DUPLEX   = "colorDuplex";
    public static final String KEY_COLOR_SWAP     = "colorSwap";

    public static final String KEY_TIME_SYSTEM    = "timeSystem";
    public static final String KEY_TIME_ROTATE    = "timeRotate";
    public static final String KEY_TIME_SECONDS   = "timeSeconds";

    // not all patterns use these but they aren't atypical
    public static final String KEY_ORIENTATION    = "orientation";
    public static final String KEY_TYPEFACE       = "typeface";

    public static final int TIME_TRADITIONAL = 0;
    public static final int TIME_MILITARY = 0;
    public static final int TIME_DECIMAL = 1;
    public static final int TIME_DUODECIMAL = 2;
    public static final int TIME_HEXADECIMAL = 3;
    public static final int TIME_HEXIMAL = 4;

    private ColorWheel colorWheel;
    private int colorGamutId = 0;

    private TimeSystem timeSystem;
    private int timeSystemId = 0;

    private Typeface   typeface;
    private int typefaceId = 0;

    /**
     * Get instance of currently selected time system.
     */
    public TimeSystem getTimeSystem() {
        int id = integerSettings.get(KEY_TIME_SYSTEM);
        if (timeSystem == null) {
            setupTimeSystem();
        }
        if (timeSystemId != id) {
            setupTimeSystem();
        }
        return timeSystem;
    }

    protected void setupTimeSystem() {
        timeSystemId = integerSettings.get(KEY_TIME_SYSTEM);
        timeSystem   = newTimeSystem(timeSystemId);
    }

    // FIXME: standard vs military ???
    protected TimeSystem newTimeSystem(int id) {
        TimeSystem timeSystem;
        switch (id) {
            case TIME_HEXIMAL:
                timeSystem = new HeximalTime();
                break;
            case TIME_HEXADECIMAL:
                timeSystem = new HexadecimalTime();
                break;
            case TIME_DUODECIMAL:
                timeSystem = new DuodecimalTime();
                break;
            case TIME_DECIMAL:
                timeSystem = new DecimalTime();
                break;
            default:
                // TODO: can just be one class?
                if (isDuplexed()) {
                    timeSystem = new StandardTime();
                } else {
                    timeSystem = new MilitaryTime();
                }
                break;
        }

        // TODO: handle rotation in time system?
        //if (Settings.KEY_ROTATE_TIME.equals(key)) {
        //    configureTimeOffset();
        //}

        // TODO: set base conversion
        //timeSystem.setBaseConverted(baseConvert());
        return timeSystem;
    }

    /**
     * Get current color wheel instance.
     *
     * @return      color wheel
     */
    public ColorWheel getColorWheel() {
        int id = getInteger(KEY_COLOR_GAMUT);
        if (colorWheel == null) {
            setupColorWheel();
        }
        if (colorGamutId != id) {
            setupColorWheel();
        }
        return colorWheel;
    }

    protected void setupColorWheel() {
        colorGamutId = getInteger(KEY_COLOR_GAMUT);

        if (colorWheel == null) {
            colorWheel = new ColorWheel();
        }

        //double offset = (double) timeSystem.gmtOffset() / 24;
        //double offset = 0.0;
        //colorWheel.setOffset(offset);

        int     gamut    = getInteger(KEY_COLOR_GAMUT);
        boolean daylight = getBoolean(KEY_COLOR_DAYLIGHT);

        colorWheel.setColorGamut(gamut);

        if (daylight) {
            colorWheel.setDaylightFactor(0.7f);
        }
    }

    /**
     *
     * @return      integer for type of color wheel
     */
    public int getColorGamut() {
        return getInteger(KEY_COLOR_GAMUT);
    }

    /**
     *
     * @return      true if the color wheel should repeat twice a day
     */
    public boolean isDuplexed() {
        return getBoolean(KEY_COLOR_DUPLEX);
    }

    /**
     *
     * @return      true is colors should be adjusted for time of day
     */
    public boolean isDaylight() {
        return getBoolean(KEY_COLOR_DAYLIGHT);
    }

    /**
     *
     * @return      true if seconds should be displayed
     */
    public boolean displaySeconds() {
        return getBoolean(KEY_TIME_SECONDS);
    }

    /**
     *
     * @return      true if clock colors are rotated
     */
    public boolean isRotated() {
        return getBoolean(KEY_TIME_ROTATE);
    }

    /**
     *
     * @return      true if colors should be swapped
     */
    public boolean isSwapped() {
        return getBoolean(KEY_COLOR_SWAP);
    }

    /**
     * Get orientation, which typically will have the values 0, 1 or 2 for horizontal,
     * vertical or rotating, respectively.
     *
     * @return      integer value
     */
    public int getOrientation() {
        return getInteger(KEY_ORIENTATION);
    }

    /**
     * Get current typeface.
     *
     * @return      typeface instance
     */
    public Typeface getTypeface() {
        if (typeface == null) {
            setupTypeface();
        }
        if (typefaceId != getInteger(KEY_TYPEFACE)) {
            setupTypeface();
        }
        return typeface;
    }

    /**
     * Setup new typeface.
     */
    protected void setupTypeface() {
        typefaceId = getInteger(KEY_TYPEFACE);
        typeface   = newTypeface(typefaceId);
    }

    protected Typeface newTypeface(int typefaceId) {
        //AssetManager assets = context.getAssets();
        Typeface typeface;
        switch (typefaceId) {
            case 7:
                typeface = Typeface.createFromAsset(assets, "arcade.ttf");
                break;
            case 6:
                typeface = Typeface.createFromAsset(assets, "basic.ttf");
                break;
            case 5:
                typeface = Typeface.createFromAsset(assets, "cubes.ttf");
                break;
            case 4:
                typeface = Typeface.createFromAsset(assets, "digital.ttf");
                break;
            case 3:
                typeface = Typeface.MONOSPACE;
                break;
            case 2:
                typeface = Typeface.SANS_SERIF;
                break;
            case 1:
                typeface = Typeface.SERIF;
                break;
            default:
                typeface = Typeface.DEFAULT;
        }
        return typeface;
    }

    /**
     *
     * @return
     */
    public FontScale typefaceScale() {
        return new FontScale();
    }

    /**
     * Time between ticks on the clock.
     *
     * @param wSeconds      include seconds in calculation?
     * @return              time in milliseconds
     */
    public long getTickTime(boolean wSeconds) {
        return timeSystem.tickTime(wSeconds);
    }

    // Setting Change Monitor ?

}
