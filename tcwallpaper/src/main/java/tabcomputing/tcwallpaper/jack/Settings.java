package tabcomputing.tcwallpaper.jack;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    public Settings() {
        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);
        propertyBoolean(KEY_COLOR_SWAP, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        //propertyBoolean(KEY_CLOCK_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);
    }

}
