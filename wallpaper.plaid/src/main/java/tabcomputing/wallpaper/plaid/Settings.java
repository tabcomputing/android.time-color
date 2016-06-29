package tabcomputing.wallpaper.plaid;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    public static final String KEY_INTERLACE = "interlace";

    public Settings() {
        //propertyInteger(KEY_FLARE, 0);

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);
        propertyBoolean(KEY_COLOR_SWAP, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyBoolean(KEY_INTERLACE, false);
    }

    public boolean isInterlaced() {
        return getBoolean(KEY_INTERLACE);
    }

}
