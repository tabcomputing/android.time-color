package tabcomputing.wallpaper.stripes;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    public static final String KEY_ORIENTATION = "orientation";

    public Settings() {
        propertyInteger(KEY_ORIENTATION, 0);

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);
    }

    public int orientation = 0;

    public int getOrientation() {
        return orientation;
    }
}
