package com.tabcomputing.chronochrome.watch;

import android.content.SharedPreferences;


public class Settings {
    public static final String KEY_PATTERN        = "background";
    public static final String KEY_FLARE          = "flare";

    public static final String KEY_COLOR_GAMUT    = "colorGamut";
    public static final String KEY_DYNAMIC        = "dynamicColor";
    public static final String KEY_COLOR_REVERSED = "colorReversed";

    public static final String KEY_DUPLEX         = "duplex";
    public static final String KEY_SECONDS        = "displaySeconds";

    public static final String KEY_TIME_TYPE      = "timeType";
    public static final String KEY_ROTATE_TIME    = "rotateTime";
    public static final String KEY_BASE           = "baseConversion";

    public static final String KEY_COLOR_WHEEL    = "colorWheel";
    public static final String KEY_CLOCK_NUMBERS  = "clockNumbers";
    public static final String KEY_NUMBER_SYSTEM  = "numberSystem";
    public static final String KEY_CLOCK_DASHES   = "clockDashes";
    public static final String KEY_DIGITAL_TIME   = "displayTime";
    public static final String KEY_CLOCK_HANDS    = "displayClockHands";
    public static final String KEY_TYPE_FACE      = "typeface";

    private int     pattern        = 0;
    private int     flare          = 0;
    private boolean colorWheel     = false;
    private boolean dynamicColor   = true;
    private boolean colorReversed  = false;
    private int     colorGamut     = 0;

    private boolean duplex         = false;

    private int     timeType       = 0;
    private boolean rotateTime     = false;
    private boolean includeSeconds = false;
    private boolean baseConversion = false;

    private int     typeface       = 0;
    private boolean clockNumbers   = false;
    private int     numberSystem   = 0;
    private boolean clockDashes    = true;
    private boolean digitalClock   = false;
    private boolean clockHands     = false;

    private static Settings instance;

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    private Settings() {}

    /**
     *
     * @param prefs     shared preferences
     */
    public void readPreferences(SharedPreferences prefs) {
        setPattern(prefs.getString(KEY_PATTERN, "0"));
        setFlare(prefs.getString(KEY_FLARE, "0"));

        setColorGamut(prefs.getString(KEY_COLOR_GAMUT, "0"));
        setDynamicColor(prefs.getBoolean(KEY_DYNAMIC, true));
        setColorReversed(prefs.getBoolean(KEY_COLOR_REVERSED, false));

        setDuplex(prefs.getBoolean(KEY_DUPLEX, false));
        hasSeconds(prefs.getBoolean(KEY_SECONDS, false));

        setTimeType(prefs.getString(KEY_TIME_TYPE, "0"));
        setRotateTime(prefs.getBoolean(KEY_ROTATE_TIME, false));
        setBaseConversion(prefs.getBoolean(KEY_BASE, true));

        setColorWheel(prefs.getBoolean(KEY_COLOR_WHEEL, false));
        setClockHands(prefs.getBoolean(KEY_CLOCK_HANDS, false));
        setClockNumbers(prefs.getBoolean(KEY_CLOCK_NUMBERS, false));
        setNumberSystem(prefs.getString(KEY_NUMBER_SYSTEM, "0"));
        setClockDashes(prefs.getBoolean(KEY_CLOCK_DASHES, false));
        setTypeface(prefs.getString(KEY_TYPE_FACE, "0"));
        setDigitalClock(prefs.getBoolean(KEY_DIGITAL_TIME, false));
    }

    /**
     *
     * @param prefs     shared preferences
     * @param key       preference key
     */
    public void changePreference(SharedPreferences prefs, String key) {
        if (KEY_PATTERN.equals(key))  {
            setPattern(prefs.getString(KEY_PATTERN, "0"));

        } else if (KEY_FLARE.equals(key)) {
            setFlare(prefs.getString(KEY_FLARE, "0"));

        } else if (KEY_COLOR_WHEEL.equals(key)) {
            setColorWheel(prefs.getBoolean(KEY_COLOR_WHEEL, false));

        } else if (KEY_DYNAMIC.equals(key)) {
            setDynamicColor(prefs.getBoolean(KEY_DYNAMIC, false));

        } else if (KEY_COLOR_REVERSED.equals(key)) {
            setColorReversed(prefs.getBoolean(KEY_COLOR_REVERSED, false));

        } else if (KEY_COLOR_GAMUT.equals(key)) {
            setColorGamut(prefs.getString(KEY_COLOR_GAMUT, "0"));

        } else if (KEY_DUPLEX.equals(key)) {
            setDuplex(prefs.getBoolean(KEY_DUPLEX, false));

        } else if (KEY_TIME_TYPE.equals(key)) {
            // TODO: Why can't we use getInteger?
            setTimeType(prefs.getString(KEY_TIME_TYPE, "0"));

        } else if (KEY_ROTATE_TIME.equals(key)) {
            setRotateTime(prefs.getBoolean(KEY_ROTATE_TIME, false));

        } else if (KEY_SECONDS.equals(key)) {
            hasSeconds(prefs.getBoolean(KEY_SECONDS, false));

        } else if (KEY_BASE.equals(key)) {
            setBaseConversion(prefs.getBoolean(KEY_BASE, false));

        } else if (KEY_CLOCK_NUMBERS.equals(key)) {
            setClockNumbers(prefs.getBoolean(KEY_CLOCK_NUMBERS, false));

        } else if (KEY_NUMBER_SYSTEM.equals(key)) {
            setNumberSystem(prefs.getString(KEY_NUMBER_SYSTEM, "0"));

        } else if (KEY_CLOCK_DASHES.equals(key)) {
            setClockDashes(prefs.getBoolean(KEY_CLOCK_DASHES, false));

        } else if (KEY_TYPE_FACE.equals(key)) {
            setTypeface(prefs.getString(KEY_TYPE_FACE, "0"));

        } else if (KEY_DIGITAL_TIME.equals(key)) {
            setDigitalClock(prefs.getBoolean(KEY_DIGITAL_TIME, false));

        } else if (KEY_CLOCK_HANDS.equals(key)) {
            setClockHands(prefs.getBoolean(KEY_CLOCK_HANDS, false));

        }
    }

    // readers

    public int getPattern() {
        return pattern;
    }

    public int getFlare() {
        return flare;
    }

    public int getTypeface() {
        return typeface;
    }

    public boolean isClockNumbered() {
        return clockNumbers;
    }

    public int getNumberSystem() {
        return numberSystem;
    }

    public boolean hasClockDashes() {
        return clockDashes;
    }

    public int timeType() {
        return timeType;
    }

    public boolean rotateTime() {
        return rotateTime;
    }

    public boolean displayTime() {
        return digitalClock;
    }

    public boolean displaySeconds() {
        return includeSeconds;
    }

    public boolean hasClockHands() {
        return clockHands;
    }

    public boolean hasColorWheel() {
        return colorWheel;
    }

    public boolean isDuplexed() {
        return duplex;
    }

    public boolean baseConvert() {
        return baseConversion;
    }

    public boolean hasDynamicColor() {
        return dynamicColor;
    }

    public boolean isColorReversed() {
        return colorReversed;
    }

    public int colorGamut() {
        return colorGamut;
    }

    // -- writers --

    public void setPattern(String val) {
        pattern = Integer.parseInt(val);
    }

    public void setFlare(String val) {
        flare = Integer.parseInt(val);
    }

    public void setTimeType(String val) {
        timeType = Integer.parseInt(val);
    }

    public void setClockNumbers(boolean val) {
        clockNumbers = val;
    }

    public void setNumberSystem(String val) {
        numberSystem = Integer.parseInt(val);
    }

    public void setClockDashes(boolean val) {
        clockDashes = val;
    }

    public void setRotateTime(boolean val) {
        rotateTime = val;
    }

    public void setClockHands(boolean val) {
        clockHands = val;
    }

    public void setDigitalClock(boolean val) {
        digitalClock = val;
    }

    public void setTypeface(String val) {
        typeface = Integer.parseInt(val);
    }

    public void setColorWheel(boolean val) {
        colorWheel = val;
    }

    public void hasSeconds(boolean val) {
        includeSeconds = val;
    }

    public void setBaseConversion(boolean val) {
        baseConversion = val;
    }

    public void setDynamicColor(boolean val) {
        dynamicColor = val;
    }

    public void setColorReversed(boolean val) {
        colorReversed = val;
    }

    public void setColorGamut(String val) { colorGamut = Integer.parseInt(val); }

    public void setDuplex(boolean val) { duplex = val; }

}
