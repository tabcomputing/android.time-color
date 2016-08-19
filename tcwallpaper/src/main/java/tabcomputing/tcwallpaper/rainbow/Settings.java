package tabcomputing.tcwallpaper.rainbow;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_TICKLESS = "tickless";

    public Settings() {
        propertyBoolean(KEY_CUSTOM_SETTINGS, false);

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyBoolean(KEY_TICKLESS, false);
    }

    public boolean isTickless() {
        return getBoolean(KEY_TICKLESS);
    }

}
