package tabcomputing.tcwallpaper.bigtime;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    public Settings() {
        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        //propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyInteger(KEY_TYPEFACE, 0);
    }

    //@Override
    //public String getPrefName() {
    //    return "bigtime";
    //}

}
