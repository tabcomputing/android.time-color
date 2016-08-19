package tabcomputing.tcwallpaper.checkers;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_FADE = "fade";
    static final String KEY_SIZE = "size";

    public Settings() {
        propertyBoolean(KEY_CUSTOM_SETTINGS, false);

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyBoolean(KEY_COLOR_SWAP, false);
        propertyInteger(KEY_SIZE, 1);
        propertyBoolean(KEY_FADE, false);
    }

    public int getSize() {
        return getInteger(KEY_SIZE);
    }

    public boolean isFade() {
        return getBoolean(KEY_FADE);
    }

}
