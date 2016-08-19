package tabcomputing.tcwallpaper.wave;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_SMOOTH = "smooth";
    static final String KEY_SIZE = "size";
    static final String KEY_DENSITY = "density";

    public Settings() {
        propertyBoolean(KEY_CUSTOM_SETTINGS, false);

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyBoolean(KEY_SMOOTH, false);
        propertyInteger(KEY_SIZE, 1);
        propertyInteger(KEY_DENSITY, 1);
    }

    public boolean isSmooth() {
        return getBoolean(KEY_SMOOTH);
    }

    public int getSize() {
        return getInteger(KEY_SIZE);
    }

    public int getDensity() {
        return getInteger(KEY_DENSITY);
    }

}
