package tabcomputing.tcwallpaper;

import android.content.SharedPreferences;
import android.graphics.Typeface;

import tabcomputing.library.clock.DecimalTime;
import tabcomputing.library.clock.DuodecimalTime;
import tabcomputing.library.clock.HexadecimalTime;
import tabcomputing.library.clock.HeximalTime;
import tabcomputing.library.clock.StandardTime;
import tabcomputing.library.clock.MeridiemTime;
import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.color.ColorWheel;
import tabcomputing.library.paper.AbstractSettings;
import tabcomputing.library.paper.FontScale;

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
    public static final String KEY_BASE_CONVERT   = "baseConvert";

    // not all patterns use these but they aren't atypical
    public static final String KEY_ORIENTATION    = "orientation";
    public static final String KEY_TYPEFACE       = "typeface";

    public static final int TIME_STANDARD = 0;
    public static final int TIME_MERIDIEM = 1;
    public static final int TIME_DECIMAL = 2;
    public static final int TIME_DUODECIMAL = 3;
    public static final int TIME_HEXADECIMAL = 4;
    public static final int TIME_HEXIMAL = 5;

    private ColorWheel colorWheel = new ColorWheel();
    private TimeSystem timeSystem = new MeridiemTime();
    private Typeface typeface = Typeface.DEFAULT;

    /**
     * Get instance of currently selected time system.
     */
    public TimeSystem getTimeSystem() {
        //int id = integerSettings.get(KEY_TIME_SYSTEM);
        if (timeSystem == null) {
            changeTimeSystem();
        }
        //if (timeSystemId != id) {
        //    changeTimeSystem();
        //}
        return timeSystem;
    }

    protected void changeTimeSystem() {
        //timeSystemId = integerSettings.get(KEY_TIME_SYSTEM);
        timeSystem = newTimeSystem(integerSettings.get(KEY_TIME_SYSTEM));
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
            case TIME_MERIDIEM:
                timeSystem = new MeridiemTime();
                break;
            case TIME_STANDARD:
            default:
                timeSystem = new StandardTime();
                break;
        }

        // TODO: handle rotation in time system?
        //if (Settings.KEY_ROTATE_TIME.equals(key)) {
        //    configureTimeOffset();
        //}

        // set base conversion
        timeSystem.setBaseConverted(isBaseConverted());

        return timeSystem;
    }

    /**
     * Get current color wheel instance.
     *
     * @return      color wheel
     */
    public ColorWheel getColorWheel() {
        //int id = getInteger(KEY_COLOR_GAMUT);
        if (colorWheel == null) {
            changeColorWheel();
        }
        //if (colorGamutId != id) {
        //    changeColorWheel();
        //}
        return colorWheel;
    }

    protected void changeColorWheel() {
        //colorGamutId = getInteger(KEY_COLOR_GAMUT);

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
            colorWheel.setDaylightFactor(true);
        } else {
            colorWheel.setDaylightFactor(false);
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
    public boolean withSeconds() {
        return  getBoolean(KEY_TIME_SECONDS);
    }
    public boolean sansSeconds() {
        return !getBoolean(KEY_TIME_SECONDS);
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

    public boolean isBaseConverted() {
        return getBoolean(KEY_BASE_CONVERT);
    }

    /**
     * Get current typeface.
     *
     * @return      typeface instance
     */
    public Typeface getTypeface() {
        if (typeface == null) {
            changeTypeface();
        }
        return typeface;
    }

    /**
     * Setup new typeface.
     */
    protected void changeTypeface() {
        //typefaceId = getInteger(KEY_TYPEFACE);
        typeface = newTypeface(getInteger(KEY_TYPEFACE));
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
     * TODO: Maybe minimum should be 1000. Currently Decimal is the lowest at 864.
     *       The lower it gets the harder it is hard on the CPU.
     *
     * @param wSeconds      include seconds in calculation?
     * @return              time in milliseconds
     */
    public long getTickTime(boolean wSeconds) {
        return timeSystem.tickTime(wSeconds);
    }

    /**
     * Override this if pattern settings class needs to create new instance of some
     * type when a setting changes, but be sure to call super.
     *
     * @param prefs     shared preferences
     * @param key       preference key
     */
    @Override
    public void updatePreference(SharedPreferences prefs, String key) {
        //Log.d("log", "updatePreference: " + key);
        switch (key) {
            case KEY_TIME_SYSTEM:
                changeTimeSystem();
                break;
            case KEY_COLOR_DUPLEX:
                changeTimeSystem();
                break;
            case KEY_COLOR_GAMUT:
            case KEY_COLOR_DAYLIGHT:
                changeColorWheel();
                break;
            case KEY_TYPEFACE:
                changeTypeface();
                break;
        }
        super.updatePreference(prefs, key);
    }

}
