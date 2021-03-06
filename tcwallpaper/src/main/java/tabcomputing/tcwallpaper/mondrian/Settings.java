package tabcomputing.tcwallpaper.mondrian;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

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

        //propertyBoolean(KEY_COLOR_SWAP, false);
    }

}
