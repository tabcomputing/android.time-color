package tabcomputing.tcwallpaper.plaid;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    public static final String KEY_INTERLACE = "interlace";

    public Settings() {
        defineProperties();
    }

    @Override
    protected void defineProperties() {
        super.defineProperties();

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyBoolean(KEY_INTERLACE, false);

        // It seems time is always written left-to-right all around the world
        // so why should we confuse things with an option to do otherwise?
        //propertyBoolean(KEY_COLOR_SWAP, false);
    }

    public boolean isInterlaced() {
        return getBoolean(KEY_INTERLACE);
    }

}
