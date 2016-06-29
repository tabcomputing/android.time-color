package tabcomputing.wallpaper.horizons;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    //public static final String KEY_FLARE          = "flare";

    public Settings() {
        //propertyInteger(KEY_FLARE, 0);

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);
    }

}
