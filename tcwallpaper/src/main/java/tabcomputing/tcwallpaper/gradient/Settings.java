package tabcomputing.tcwallpaper.gradient;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_GLARE = "sunGlare";
    static final String KEY_SWAP  = "colorSwap";

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

        propertyBoolean(KEY_SWAP, false);
        propertyInteger(KEY_ORIENTATION, 0);
        propertyBoolean(KEY_GLARE, false);
    }

    public boolean useGlare() {
        return getBoolean(KEY_GLARE);
    }

    public boolean isSwapped() {
        return getBoolean(KEY_SWAP);
    }

}
