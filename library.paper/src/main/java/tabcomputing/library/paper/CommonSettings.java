package tabcomputing.library.paper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import tabcomputing.library.clock.DecimalTime;
import tabcomputing.library.clock.DuodecimalTime;
import tabcomputing.library.clock.HexadecimalTime;
import tabcomputing.library.clock.HeximalTime;
import tabcomputing.library.clock.StandardTime;
import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.color.ColorWheel;

//import tabcomputing.library.clock.MeridiemTime;

/**
 * Common Pattern Settings
 */
public class CommonSettings extends AbstractSettings {
    // TODO: BE SURE TO MAKE FALSE BEFORE RELEASE!
    public static final Boolean DEBUG = false;

    public static final String KEY_COLOR_GAMUT      = "colorGamut";
    public static final String KEY_COLOR_DYNAMIC    = "colorDaylight"; // TODO: deprecate
    public static final String KEY_COLOR_DAYLIGHT   = "colorDaylight";
    public static final String KEY_COLOR_REVERSE    = "colorReversed";
    public static final String KEY_COLOR_DUPLEX     = "colorDuplex";  // presently deprecated
    public static final String KEY_COLOR_SWAP       = "colorSwap";

    public static final String KEY_CLOCK_NUMBERS    = "numberSystem";
    public static final String KEY_CLOCK_TYPEFACE   = "clockTypeface";
    public static final String KEY_CLOCK_TYPE       = "clockType";
    public static final String KEY_CLOCK_ROTATE     = "clockRotate";
    public static final String KEY_CLOCK_BACKGROUND = "clockBackground";
    public static final String KEY_CLOCK_AMPM       = "clockAMPM";

    public static final String KEY_TIME_SYSTEM      = "timeSystem";
    public static final String KEY_TIME_SECONDS     = "timeSeconds";
    public static final String KEY_BASE_CONVERT     = "baseConvert";
    public static final String KEY_TIME_ROTATE      = "clockRotate";

    public static final String KEY_CHEAT_CLOCK      = "cheatClock";

    // not all patterns use these but they aren't atypical
    public static final String KEY_ORIENTATION    = "orientation";

    public static final int TIME_STANDARD = 0;
    //public static final int TIME_MERIDIEM = 1;  // DEPRECATED
    public static final int TIME_DECIMAL = 1;
    public static final int TIME_DUODECIMAL = 2;
    public static final int TIME_HEXADECIMAL = 3;
    public static final int TIME_HEXIMAL = 4;


    private ColorWheel colorWheel = new ColorWheel();
    private TimeSystem timeSystem = new StandardTime();
    private Typeface typeface = Typeface.DEFAULT;

    // Singleton Instance
    private static CommonSettings instance;
    public static CommonSettings getInstance() {
        if (instance == null) {
            instance = new CommonSettings();
        }
        return instance;
    }

    public CommonSettings() {
        defineProperties();
        defineColorProperties();
        defineTimeProperties();
        defineClockProperties();
    }

    /**
     *
     */
    protected void defineProperties() {
        propertyBoolean(KEY_CUSTOM_SETTINGS, false);
        propertyInteger(KEY_CHEAT_CLOCK, 1);
    }

    protected void defineColorProperties() {
        propertyBoolean(KEY_COLOR_DYNAMIC, true);
        propertyBoolean(KEY_COLOR_REVERSE, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);
        propertyBoolean(KEY_COLOR_SWAP, false);
        propertyInteger(KEY_COLOR_GAMUT, 0);
    }

    protected void defineTimeProperties() {
        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_SECONDS, false);
        propertyBoolean(KEY_BASE_CONVERT, true);
    }

    protected void defineClockProperties() {
        propertyInteger(KEY_CLOCK_TYPE, 0);
        propertyInteger(KEY_CLOCK_NUMBERS, 0);
        propertyInteger(KEY_CLOCK_TYPEFACE, 0);
        propertyBoolean(KEY_CLOCK_ROTATE, false);
        propertyInteger(KEY_CLOCK_BACKGROUND, 0);
        propertyBoolean(KEY_CLOCK_AMPM, false);
    }

    /**
     *
     */
    public int getCheatClock() {
        return getInteger(KEY_CHEAT_CLOCK);
    }

    /**
     * Get instance of currently selected time system.
     */
    public TimeSystem getTimeSystem() {
        if (timeSystem == null || isChanged(KEY_TIME_SYSTEM)) {
            changeTimeSystem();
        }
        return timeSystem;
    }

    protected void changeTimeSystem() {
        //timeSystem = newTimeSystem(integerSettings.get(KEY_TIME_SYSTEM));
        int timeSystemId = getInteger(KEY_TIME_SYSTEM);
        timeSystem = newTimeSystem(timeSystemId);
    }

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
            //case TIME_MERIDIEM:
            //    timeSystem = new MeridiemTime();
            //    break;
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
        if (colorWheel == null || isChanged(KEY_COLOR_GAMUT)) {
            changeColorWheel();
        }
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
            colorWheel.setDaylightFactor(true);   // (0.7f)
        } else {
            colorWheel.setDaylightFactor(false);  // (0.0f)
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

    public boolean displaySeconds() {
        return booleanSettings.get(KEY_TIME_SECONDS);
    }

    public void toggleSeconds() {
        booleanSettings.put(KEY_TIME_SECONDS, !displaySeconds());
    }

    /**
     * @return      true if clock colors are rotated
     */
    public boolean isRotated() {
        return getBoolean(KEY_TIME_ROTATE);
    }

    /**
     * @return      true if clock set for am/pm
     */
    public boolean isAMPM() {
        return getBoolean(KEY_CLOCK_AMPM);
    }

    public void toggleAMPM() {
        booleanSettings.put(KEY_CLOCK_AMPM, !isAMPM());
    }

    /**
     * Toggle and save AM/PM setting.
     */
    public void toggleAMPM(Context context) {
        String key = KEY_CLOCK_AMPM;
        Boolean tog = !isAMPM();
        booleanSettings.put(key, tog);
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(key, tog);
        editor.apply();
    }



    /**
     *
     * @return      true if colors should be swapped
     */
    //public boolean isSwapped() {
    //    return getBoolean(KEY_COLOR_SWAP);
    //}

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

    public int getNumberSystem() {
        return integerSettings.get(KEY_CLOCK_NUMBERS);
    }

    public int timeSystem() {
        return integerSettings.get(KEY_TIME_SYSTEM);
    }

    public boolean rotateTime() {
        //return booleanSettings.get(KEY_CLOCK_ROTATE);
        return false;
    }

    public int getClockType() {
        return integerSettings.get(KEY_CLOCK_TYPE);
    }

    public int getBackground() {
        return integerSettings.get(KEY_CLOCK_BACKGROUND);
    }


    public boolean hasDynamicColor() {
        return booleanSettings.get(KEY_COLOR_DYNAMIC);
    }

    public boolean isColorReversed() {
        return booleanSettings.get(KEY_COLOR_REVERSE);
    }

    public int colorGamut() {
        return integerSettings.get(KEY_COLOR_GAMUT);
    }

    public boolean isSwapped() { return booleanSettings.get(KEY_COLOR_SWAP); }

    public void setNumberSystem(String val) {
        integerSettings.put(KEY_CLOCK_NUMBERS, Integer.parseInt(val));
    }

    public void setRotateTime(boolean val) {
        booleanSettings.put(KEY_CLOCK_ROTATE, val);
    }

    public void setClockType(String val) {
        integerSettings.put(KEY_CLOCK_TYPE, Integer.parseInt(val));
    }

    public void setTypeface(String val) {
        integerSettings.put(KEY_CLOCK_TYPEFACE, Integer.parseInt(val));
    }

    public void hasSeconds(boolean val) {
        booleanSettings.put(KEY_TIME_SECONDS, val);
    }

    public void setBaseConversion(boolean val) {
        booleanSettings.put(KEY_BASE_CONVERT, val);
    }

    public void setDynamicColor(boolean val) {
        booleanSettings.put(KEY_COLOR_DYNAMIC, val);
    }

    public void setColorReversed(boolean val) {
        booleanSettings.put(KEY_COLOR_REVERSE, val);
    }

    public void setColorGamut(String val) {
        integerSettings.put(KEY_COLOR_GAMUT,  Integer.parseInt(val));

        int colorGamut = Integer.parseInt(val);
        ColorWheel cw = getColorWheel();
        cw.setColorGamut(colorGamut);
    }

    public void setDuplex(boolean val) {
        booleanSettings.put(KEY_COLOR_DUPLEX, val);
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
        switch (key) {
            //case KEY_CUSTOM_SETTINGS:
            //    // do it all
            //    changeColorWheel();
            //    changeTimeSystem();
            //    break;
            case KEY_TIME_SYSTEM:
                changeTimeSystem();
                break;
            case KEY_COLOR_DUPLEX:
                changeTimeSystem();
                changeColorWheel();
                break;
            case KEY_COLOR_GAMUT:
            case KEY_COLOR_DAYLIGHT:
                changeColorWheel();
                break;
            case KEY_CLOCK_TYPEFACE:
                changeTypeface();
                break;
        }

        // TODO: should super call come first?
        super.updatePreference(prefs, key);
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
        //if (getInteger(KEY_CLOCK_TYPEFACE) != typefaceId || typeface == null) {
        //    typefaceId = getInteger(KEY_CLOCK_TYPEFACE);
        //    typeface = newTypeface(typefaceId);
        //}
        return typeface;
    }

    /**
     * Setup new typeface.
     */
    protected void changeTypeface() {
        //typefaceId = getInteger(KEY_TYPEFACE);
        typeface = newTypeface(getInteger(KEY_CLOCK_TYPEFACE));
    }

    /**
     *
     * @return      typeface
     */
    private Typeface newTypeface(int id) {
        Typeface font;

        switch (id) {
            case 4:
                font = Typeface.createFromAsset(assets, "cubes.ttf");
                break;
            case 3:
                font = Typeface.createFromAsset(assets, "digital.ttf");
                break;
            case 2:
                font = Typeface.MONOSPACE;
                break;
            case 1:
                font = Typeface.SERIF;
                break;
            default:
                font = Typeface.DEFAULT;
        }
        return font;
    }

}
