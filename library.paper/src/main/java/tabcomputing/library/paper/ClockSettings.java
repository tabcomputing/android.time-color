package tabcomputing.library.paper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import tabcomputing.library.clock.DecimalTime;
import tabcomputing.library.clock.DuodecimalTime;
import tabcomputing.library.clock.HexadecimalTime;
import tabcomputing.library.clock.HeximalTime;
import tabcomputing.library.clock.StandardTime;
import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.color.ColorWheel;

//import tabcomputing.library.clock.MeridiemTime;

/**
 * @deprecated
 */
public class ClockSettings extends AbstractSettings {

    // Singleton
    //private static ClockSettings instance;
    //public static ClockSettings getInstance() {
    //    if (instance == null) {
    //        instance = new ClockSettings();
    //    }
    //    return instance;
    //}

    public static final int TIME_STANDARD = 0;
    //public static final int TIME_MERIDIEM = 1;
    public static final int TIME_DECIMAL = 1;
    public static final int TIME_DUODECIMAL = 2;
    public static final int TIME_HEXADECIMAL = 3;
    public static final int TIME_HEXIMAL = 4;

    public static final String KEY_COLOR_GAMUT      = "colorGamut";
    public static final String KEY_COLOR_DYNAMIC    = "colorDaylight";
    public static final String KEY_COLOR_REVERSE    = "colorReversed";
    public static final String KEY_COLOR_DUPLEX     = "colorDuplex";  // presently deprecated
    public static final String KEY_COLOR_SWAP       = "colorSwap";

    public static final String KEY_TIME_SYSTEM      = "timeSystem";
    public static final String KEY_TIME_SECONDS     = "timeSeconds";
    public static final String KEY_BASE_CONVERT     = "baseConvert";

    public static final String KEY_CLOCK_NUMBERS    = "numberSystem";
    public static final String KEY_CLOCK_TYPEFACE   = "clockTypeface";
    public static final String KEY_CLOCK_TYPE       = "clockType";
    public static final String KEY_CLOCK_ROTATE     = "clockRotate";
    public static final String KEY_CLOCK_BACKGROUND = "clockBackground";
    public static final String KEY_CLOCK_AMPM       = "clockAMPM";

    //public static final String KEY_PATTERN_SETTINGS = "usePatternSettings";


    public ClockSettings() {
        //propertyBoolean(KEY_PATTERN_SETTINGS, true);

        propertyBoolean(KEY_COLOR_DYNAMIC, true);
        propertyBoolean(KEY_COLOR_REVERSE, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);
        propertyBoolean(KEY_COLOR_SWAP, false);
        propertyInteger(KEY_COLOR_GAMUT, 0);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_SECONDS, false);
        propertyBoolean(KEY_BASE_CONVERT, true);

        propertyInteger(KEY_CLOCK_TYPE, 0);
        propertyInteger(KEY_CLOCK_NUMBERS, 0);
        propertyInteger(KEY_CLOCK_TYPEFACE, 0);
        propertyBoolean(KEY_CLOCK_ROTATE, false);
        propertyInteger(KEY_CLOCK_BACKGROUND, 0);
        propertyBoolean(KEY_CLOCK_AMPM, false);
    }

    /**
     * Get shared preferences.
     *
     * @return      SharedPreferences
     */
    public SharedPreferences getPreferences(Context context) {
        SharedPreferences prefs;

        //try {
        //    Context prefContext = context.createPackageContext("tabcomputing.wallpaper", Context.MODE_PRIVATE);
        //    prefs = prefContext.getSharedPreferences(getDefaultSharedPreferencesName(prefContext), Activity.MODE_PRIVATE);
        //} catch (Exception e) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context); //AbstractWallpaper.this);
        //}

        return prefs;
    }

    //private static String getDefaultSharedPreferencesName(Context context) {
    //    return context.getPackageName() + "_preferences";
    //}

    /**
     * Read pattern preferences.
     *
     * @param patternServiceName        pattern's service name
     * @param context                   context
     */
    public void readPreferences(String patternServiceName, Context context) {
        String prefName = getPrefName(patternServiceName);
        //Log.d("readPreferences", "! pref name: " + prefName);
        SharedPreferences prefs = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        readPreferences(prefs);
    }

    /**
     * Read clock preferences given the context.
     */
    public void readPreferences(Context context) {
        SharedPreferences prefs = getPreferences(context);
        readPreferences(prefs);
    }

    /**
     * For constructor changes.
     *
     * @param prefs     shared preferences
     * @param key       preference key
     */
    @Override
    public void updatePreference(SharedPreferences prefs, String key) {
        //Log.d("log", "ClockSettings.updatePreference: " + key);
        if (isChanged(key)) {
            switch (key) {
                case KEY_TIME_SYSTEM:
                    changeTimeSystem();
                    break;
                case KEY_COLOR_DUPLEX:
                    changeTimeSystem();
                    changeColorWheel();
                    break;
                case KEY_COLOR_GAMUT:
                case KEY_COLOR_DYNAMIC:
                    changeColorWheel();
                    break;
                case KEY_CLOCK_TYPEFACE:
                    changeTypeface();
                    break;
            }
        }
        super.updatePreference(prefs, key);
    }

    /**
     * Get a unique name to use for storing preferences in the file system.
     *
     * @return      name
     */
    public String getPrefName(String serviceName) {
        String[] serviceNameParts = serviceName.split("[.]");
        String name = serviceNameParts[serviceNameParts.length - 2];
        return name + "_preferences";
    }

    // -- readers --

    //public boolean usePatternSettings() {
    //    return booleanSettings.get(KEY_PATTERN_SETTINGS);
    //}

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

    public boolean displaySeconds() {
        return booleanSettings.get(KEY_TIME_SECONDS);
    }

    public int getClockType() {
        return integerSettings.get(KEY_CLOCK_TYPE);
    }

    public int getBackground() {
        return integerSettings.get(KEY_CLOCK_BACKGROUND);
    }

    public boolean isDuplexed() {
        //return booleanSettings.get(KEY_COLOR_DUPLEX);
        return false;
    }

    public boolean isAMPM() {
        return booleanSettings.get(KEY_CLOCK_AMPM);
    }

    public void toggleAMPM() {
        booleanSettings.put(KEY_CLOCK_AMPM, !isAMPM());
    }

    public void toggleSeconds() {
        booleanSettings.put(KEY_TIME_SECONDS, !displaySeconds());
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

    public boolean isBaseConverted() {
        return booleanSettings.get(KEY_BASE_CONVERT);
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

    // -- writers --

    //public void setFlare(String val) {
    //    integerSettings.put(KEY_FLARE, Integer.parseInt(val));
    //}
    //public void setFlare(int val) {
    //    integerSettings.put(KEY_FLARE, val);
    //}

    //public void setTimeType(String val) {
    //   integerSettings.put(KEY_TIME_SYSTEM, Integer.parseInt(val));
    //    resetTimeSystem();
    //}

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

    public ColorWheel colorWheel;

    private TimeSystem timeSystem;
    //private int timeSystemId = 0;

    public TimeSystem getTimeSystem() {
        //if (getInteger(KEY_TIME_SYSTEM) != timeSystemId || timeSystem == null) {
        //    changeTimeSystem();
        //}
        if (timeSystem == null) {
            changeTimeSystem();
        }
        return timeSystem;
    }

    protected void changeTimeSystem() {
        int timeSystemId = getInteger(KEY_TIME_SYSTEM);
        timeSystem = newTimeSystem(timeSystemId);
    }

    private TimeSystem newTimeSystem(int id) {
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

        // set base conversion
        timeSystem.setBaseConverted(isBaseConverted());

        return timeSystem;
    }


    private Typeface typeface;
    //private int typefaceId = 0;

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

    /**
     * Setup new typeface.
     */
    protected void changeTypeface() {
        //typefaceId = getInteger(KEY_TYPEFACE);
        typeface = newTypeface(getInteger(KEY_CLOCK_TYPEFACE));
    }

    /**
     *
     * @return      color wheel
     */
    public ColorWheel getColorWheel() {
        if (colorWheel == null) {
            changeColorWheel();
        }
        return colorWheel;
    }

    //private void resetColorWheel() {
    //    int colorGamut = integerSettings.get(KEY_COLOR_GAMUT);
    //   colorWheel = new ColorWheel();
    //    colorWheel.setColorGamut(colorGamut);
    //}


    protected void changeColorWheel() {
        //colorGamutId = getInteger(KEY_COLOR_GAMUT);

        if (colorWheel == null) {
            colorWheel = new ColorWheel();
        }

        //double offset = (double) timeSystem.gmtOffset() / 24;
        //double offset = 0.0;
        //colorWheel.setOffset(offset);

        int     gamut    = getInteger(KEY_COLOR_GAMUT);
        boolean daylight = getBoolean(KEY_COLOR_DYNAMIC); //COLOR_DAYLIGHT);

        colorWheel.setColorGamut(gamut);

        if (daylight) {
            colorWheel.setDaylightFactor(0.7f);
        } else {
            colorWheel.setDaylightFactor(0.0f);  // turn off
        }
    }

    /**
     *
     *
    public void savePreferences(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();

        writeBoolean(editor, KEY_COLOR_DYNAMIC);
        writeInteger(editor, KEY_COLOR_GAMUT);
        //writeBoolean(editor, KEY_COLOR_DUPLEX);

        writeInteger(editor, KEY_TIME_SYSTEM);
        writeBoolean(editor, KEY_TIME_SECONDS);
        writeBoolean(editor, KEY_BASE_CONVERT);
        //writeBoolean(editor, KEY_CLOCK_ROTATE);

        editor.apply();
    }

    protected void writeString(SharedPreferences.Editor editor, String key) {
        editor.putString(key, getString(key));
    }

    protected void writeInteger(SharedPreferences.Editor editor, String key) {
        editor.putString(key, String.valueOf(getInteger(key)));

    }
    protected void writeBoolean(SharedPreferences.Editor editor, String key) {
        editor.putBoolean(key, getBoolean(key));
    }
    */

}
