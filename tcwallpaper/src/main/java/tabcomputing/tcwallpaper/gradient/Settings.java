package tabcomputing.tcwallpaper.gradient;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    public static final String KEY_ORIENTATION = "orientation";

    public Settings() {
        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);
        propertyBoolean(KEY_COLOR_SWAP, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyInteger(KEY_ORIENTATION, 0);
    }

    public int getOrientation() {
        return getInteger(KEY_ORIENTATION);
    }

}
