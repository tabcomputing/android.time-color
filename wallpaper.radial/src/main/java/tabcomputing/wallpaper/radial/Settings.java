package tabcomputing.wallpaper.radial;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    //public static final String KEY_FLARE = "flare";
    //public static final String KEY_ORBIT    = "orbital";
    //public static final String KEY_HIDE_ORB = "hideOrb";

    public Settings() {
        //propertyInteger(KEY_FLARE, 0);

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);
        propertyBoolean(KEY_COLOR_SWAP, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        //propertyBoolean(KEY_ORBIT, false);
        //propertyBoolean(KEY_HIDE_ORB, false);

    }

    //public boolean isOrbital() {
    //    return getBoolean(KEY_ORBIT);
    //}

    //public boolean isOrbHidden() {
    //    return getBoolean(KEY_HIDE_ORB);
    //}

}