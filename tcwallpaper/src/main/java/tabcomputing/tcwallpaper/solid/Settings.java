package tabcomputing.tcwallpaper.solid;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_GLARE = "sunGlare";

    public Settings() {
        propertyBoolean(KEY_CUSTOM_SETTINGS, false);

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyBoolean(KEY_COLOR_SWAP, false);
        propertyBoolean(KEY_GLARE, false);
    }

    public boolean useGlare() {
        return getBoolean(KEY_GLARE);
    }

}
