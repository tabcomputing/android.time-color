package tabcomputing.tcwallpaper.ring;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_SMOOTH  = "smooth";
    static final String KEY_ASPECT  = "aspect";
    static final String KEY_DENSITY = "density";
    static final String KEY_SWAP    = "colorSwap";

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

        propertyBoolean(KEY_SMOOTH, false);
        propertyInteger(KEY_ASPECT, 0);
        propertyInteger(KEY_DENSITY, 1);
    }

    public boolean isSmooth() {
        return getBoolean(KEY_SMOOTH);
    }

    public int getAspect() {
        return getInteger(KEY_ASPECT);
    }

    public int getDensity() {
        return getInteger(KEY_DENSITY);
    }

    public boolean isSwapped() {
        return getBoolean(KEY_SWAP);
    }

}
