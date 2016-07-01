package tabcomputing.tcwallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;

import tabcomputing.library.clock.DecimalTime;
import tabcomputing.library.clock.DuodecimalTime;
import tabcomputing.library.clock.HexadecimalTime;
import tabcomputing.library.clock.HeximalTime;
import tabcomputing.library.clock.MilitaryTime;
import tabcomputing.library.clock.StandardTime;
import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.paper.AbstractSettings;
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

    public static final String KEY_FLARE          = "flare";

    public static final String KEY_COLOR_GAMUT    = "colorGamut";
    public static final String KEY_COLOR_DYNAMIC  = "dynamicColor";
    public static final String KEY_COLOR_REVERSE  = "colorReversed";
    public static final String KEY_COLOR_DUPLEX   = "duplex";

    public static final String KEY_SECONDS        = "displaySeconds";

    public static final String KEY_TIME_TYPE      = "timeType";
    public static final String KEY_TIME_ROTATE    = "timeRotate";
    public static final String KEY_BASE           = "baseConversion";

    public static final String KEY_NUMBER_SYSTEM  = "numberSystem";
    public static final String KEY_TYPEFACE       = "typeface";
    public static final String KEY_CLOCK_TYPE     = "clockType";

    public ClockSettings() {
        propertyBoolean(KEY_COLOR_DYNAMIC, true);
        propertyBoolean(KEY_COLOR_REVERSE, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_BASE, true);
        propertyBoolean(KEY_SECONDS, false);

        propertyInteger(KEY_FLARE, 0);
        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyInteger(KEY_TIME_TYPE, 0);
        propertyInteger(KEY_NUMBER_SYSTEM, 0);
        propertyInteger(KEY_TYPEFACE, 0);
        propertyInteger(KEY_CLOCK_TYPE, 0);
    }

    /**
     * Get shared preferences.
     *
     * @return      SharedPreferences
     */
    public SharedPreferences getPreferences(Context context) {
        SharedPreferences prefs;

        try {
            Context prefContext = context.createPackageContext("tabcomputing.wallpaper", Context.MODE_PRIVATE);
            prefs = prefContext.getSharedPreferences(getDefaultSharedPreferencesName(prefContext), Activity.MODE_PRIVATE);
        } catch (Exception e) {
            Log.d("SettingsObserver", "Could not access shared preferences.");
            prefs = PreferenceManager.getDefaultSharedPreferences(context); //AbstractWallpaper.this);
        }

        return prefs;
    }

    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    /**
     * Read preferences given the context.
     */
    public void readPreferences(Context context) {
        SharedPreferences prefs = getPreferences(context);
        readPreferences(prefs);
    }

    // -- readers --

    public int getFlare() {
        return integerSettings.get(KEY_FLARE);
    }

    public int getFont() {
        return integerSettings.get(KEY_TYPEFACE);
    }

    public int getNumberSystem() {
        return integerSettings.get(KEY_NUMBER_SYSTEM);
    }

    public int timeType() {
        return integerSettings.get(KEY_TIME_TYPE);
    }

    public boolean rotateTime() {
        return booleanSettings.get(KEY_TIME_ROTATE);
    }

    public boolean displaySeconds() {
        return booleanSettings.get(KEY_SECONDS);
    }

    public int getClockType() {
        return integerSettings.get(KEY_CLOCK_TYPE);
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

    public void setFlare(String val) {
        integerSettings.put(KEY_FLARE, Integer.parseInt(val));
    }

    public void setFlare(int val) {
        integerSettings.put(KEY_FLARE, val);
    }

    public void setTimeType(String val) {
        integerSettings.put(KEY_TIME_TYPE, Integer.parseInt(val));
        resetTimeSystem();
    }

    public void setNumberSystem(String val) {
        integerSettings.put(KEY_NUMBER_SYSTEM, Integer.parseInt(val));
    }

    public void setRotateTime(boolean val) {
        booleanSettings.put(KEY_TIME_ROTATE, val);
    }

    public void setClockType(String val) {
        integerSettings.put(KEY_CLOCK_TYPE, Integer.parseInt(val));
    }

    public void setFont(String val) {
        integerSettings.put(KEY_TYPEFACE, Integer.parseInt(val));
    }

    public void hasSeconds(boolean val) {
        booleanSettings.put(KEY_SECONDS, val);
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
    public TimeSystem timeSystem;

    /**
     *
     */
    private TimeSystem resetTimeSystem() {
        int timeType = integerSettings.get(KEY_TIME_TYPE);
        switch (timeType) {
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

    public TimeSystem getTimeSystem() {
        if (timeSystem == null) {
            resetTimeSystem();
        }
        return timeSystem;
    }

    /**
     *
     * @param assets        instance of AssetManager
     * @return              typeface
     */
    public Typeface getTypeface(AssetManager assets) {
        Typeface font;

        int typeface = integerSettings.get(KEY_TYPEFACE);

        switch (typeface) {
            case 7:
                font = Typeface.createFromAsset(assets, "arcade.ttf");
                break;
            case 6:
                font = Typeface.createFromAsset(assets, "basic.ttf");
                break;
            case 5:
                font = Typeface.createFromAsset(assets, "cubes.ttf");
                break;
            case 4:
                font = Typeface.createFromAsset(assets, "digital.ttf");
                break;
            case 3:
                font = Typeface.MONOSPACE;
                break;
            case 2:
                font = Typeface.SANS_SERIF;
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
