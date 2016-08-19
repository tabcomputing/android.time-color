package tabcomputing.tcwallpaper.lotus;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    public static final String KEY_BINDI = "bindi";

    public Settings() {
        propertyBoolean(KEY_CUSTOM_SETTINGS, false);

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyBoolean(KEY_COLOR_SWAP, false);
        propertyBoolean(KEY_BINDI, false);
    }

    public boolean isBindi() {
        return getBoolean(KEY_BINDI);
    }

}
