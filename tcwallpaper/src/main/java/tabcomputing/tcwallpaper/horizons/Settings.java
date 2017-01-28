package tabcomputing.tcwallpaper.horizons;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    public static final String KEY_SUN  = "sun";

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

        propertyBoolean(KEY_SUN, false);
    }

    public boolean isSun() {
        return getBoolean(KEY_SUN);
    }

}
