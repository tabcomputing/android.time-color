package tabcomputing.tcwallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;

import java.sql.Time;

import tabcomputing.library.clock.DecimalTime;
import tabcomputing.library.clock.DuodecimalTime;
import tabcomputing.library.clock.HexadecimalTime;
import tabcomputing.library.clock.HeximalTime;
import tabcomputing.library.clock.MilitaryTime;
import tabcomputing.library.clock.StandardTime;
import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.color.ColorWheel;


public class ClockSettings extends AbstractSettings {

    // Singleton
    private static ClockSettings instance;
    public static ClockSettings getInstance() {
        if (instance == null) {
            instance = new ClockSettings();
        }
        return instance;
    }

    public static final int TIME_TRADITIONAL = 0;
    public static final int TIME_MILITARY = 1;
    public static final int TIME_HEXADECIMAL = 2;
    public static final int TIME_HEXIMAL = 3;
    public static final int TIME_DECIMAL = 4;
    public static final int TIME_DUODECIMAL = 5;

    //public static final String KEY_FLARE          = "flare";

    public static final String KEY_COLOR_GAMUT      = "colorGamut";
    public static final String KEY_COLOR_DYNAMIC    = "colorDaylight";
    public static final String KEY_COLOR_REVERSE    = "colorReversed";
    public static final String KEY_COLOR_DUPLEX     = "duplex";

    public static final String KEY_TIME_SYSTEM      = "timeSystem";
    public static final String KEY_TIME_ROTATE      = "timeRotate";
    public static final String KEY_TIME_SECONDS     = "timeSeconds";

    public static final String KEY_BASE             = "baseConversion";

    public static final String KEY_NUMBER_SYSTEM    = "numberSystem";
    public static final String KEY_CLOCK_TYPEFACE   = "typeface";
    public static final String KEY_CLOCK_TYPE       = "clockType";
    public static final String KEY_CLOCK_BACKGROUND = "clockBackground";

    public static final String KEY_PATTERN_SETTINGS = "usePatternSettings";


    public ClockSettings() {
        propertyBoolean(KEY_PATTERN_SETTINGS, true);

        propertyBoolean(KEY_COLOR_DYNAMIC, true);
        propertyBoolean(KEY_COLOR_REVERSE, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);
        propertyInteger(KEY_COLOR_GAMUT, 0);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_SECONDS, false);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_BASE, true);

        propertyInteger(KEY_NUMBER_SYSTEM, 0);

        propertyInteger(KEY_CLOCK_TYPEFACE, 0);
        propertyInteger(KEY_CLOCK_TYPE, 0);
        propertyInteger(KEY_CLOCK_BACKGROUND, 0);
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
     * Read clock preferences given the context.
     */
    public void readPreferences(Context context) {
        SharedPreferences prefs = getPreferences(context);
        readPreferences(prefs);
    }

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

    public boolean usePatternSettings() {
        return booleanSettings.get(KEY_PATTERN_SETTINGS);
    }

    public int getNumberSystem() {
        return integerSettings.get(KEY_NUMBER_SYSTEM);
    }

    public int timeSystem() {
        return integerSettings.get(KEY_TIME_SYSTEM);
    }

    public boolean rotateTime() {
        return booleanSettings.get(KEY_TIME_ROTATE);
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
        return booleanSettings.get(KEY_COLOR_DUPLEX);
    }

    public boolean baseConvert() {
        return booleanSettings.get(KEY_BASE);
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
        integerSettings.put(KEY_NUMBER_SYSTEM, Integer.parseInt(val));
    }

    public void setRotateTime(boolean val) {
        booleanSettings.put(KEY_TIME_ROTATE, val);
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
        booleanSettings.put(KEY_BASE, val);
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
    private int timeSystemId = 0;

    public TimeSystem getTimeSystem() {
        if (getInteger(KEY_TIME_SYSTEM) != timeSystemId || timeSystem == null) {
            setupTimeSystem();
        }
        return timeSystem;
    }

    protected void setupTimeSystem() {
        timeSystemId = getInteger(KEY_TIME_SYSTEM);
        timeSystem   = newTimeSystem(timeSystemId);
    }

    private TimeSystem newTimeSystem(int id) {
        TimeSystem timeSystem;
        switch (id) {
            case TIME_HEXADECIMAL:
                timeSystem = new HexadecimalTime();
                break;
            case TIME_HEXIMAL:
                timeSystem = new HeximalTime();
                break;
            case TIME_DECIMAL:
                timeSystem = new DecimalTime();
                break;
            case TIME_DUODECIMAL:
                timeSystem = new DuodecimalTime();
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

        // set base conversion
        timeSystem.setBaseConverted(baseConvert());

        return timeSystem;
    }


    private Typeface typeface;
    private int typefaceId = 0;

    public Typeface getTypeface() {
        if (getInteger(KEY_CLOCK_TYPEFACE) != typefaceId || typeface == null) {
            typefaceId = getInteger(KEY_CLOCK_TYPEFACE);
            typeface = newTypeface(typefaceId);
        }
        return typeface;
    }

    /**
     *
     * @return              typeface
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
     *
     * @return      color wheel
     */
    public ColorWheel getColorWheel() {
        if (colorWheel == null) {
            resetColorWheel();
        }
        return colorWheel;
    }

    private void resetColorWheel() {
        int colorGamut = integerSettings.get(KEY_COLOR_GAMUT);
        colorWheel = new ColorWheel();
        colorWheel.setColorGamut(colorGamut);
    }

}
