package tabcomputing.tcwallpaper.radial;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    public static final String KEY_COLOR_SMOOTH = "smooth";
    public static final String KEY_COLOR_REVERSE = "reverse";
    public static final String KEY_COLOR_CENTER = "center";

    public Settings() {
        defineProperties();
    }

    @Override
    protected void defineProperties() {
        super.defineProperties();

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyBoolean(KEY_COLOR_CENTER, false);
        propertyBoolean(KEY_COLOR_REVERSE, false);
        propertyBoolean(KEY_COLOR_SMOOTH, false);
    }

    public boolean isReversed() {
        return getBoolean(KEY_COLOR_REVERSE);
    }

    public boolean isSmooth() {
        return getBoolean(KEY_COLOR_SMOOTH);
    }

    public boolean isCentered() {
        return getBoolean(KEY_COLOR_CENTER);
    }

}
