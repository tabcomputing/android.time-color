package tabcomputing.tcwallpaper.echo;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    //public static final String KEY_HIDE_ORB = "hideOrb";

    public Settings() {
        propertyBoolean(KEY_CUSTOM_SETTINGS, false);

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        //propertyBoolean(KEY_HIDE_ORB, false);
        propertyBoolean(KEY_COLOR_SWAP, false);
    }

    //public boolean isOrbHidden() {
    //    return getBoolean(KEY_HIDE_ORB);
    //}

}
