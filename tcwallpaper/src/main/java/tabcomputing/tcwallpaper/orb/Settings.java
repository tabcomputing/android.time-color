package tabcomputing.tcwallpaper.orb;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    public static final String KEY_ORB_SIZE = "orb_size";
    public static final String KEY_ORBIT    = "orbital";
    public static final String KEY_GLARE    = "sunGlare";

    public Settings() {
        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);
        propertyBoolean(KEY_COLOR_SWAP, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyBoolean(KEY_ORBIT, false);
        propertyInteger(KEY_ORB_SIZE, 1);
        propertyBoolean(KEY_GLARE, false);
    }

    public boolean isOrbital() {
        return getBoolean(KEY_ORBIT);
    }

    public int orbSize() {
        return getInteger(KEY_ORB_SIZE);
    }

    public boolean useGlare() {
        return getBoolean(KEY_GLARE);
    }

}
