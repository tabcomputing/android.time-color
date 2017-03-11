package tabcomputing.chronochrome.lotus;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_BINDI = "bindi";
    static final String KEY_SWAP  = "colorSwap";

    public Settings() {
        defineProperties();
    }

    @Override
    protected void defineProperties() {
        super.defineProperties();

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyBoolean(KEY_SWAP, false);
        propertyBoolean(KEY_BINDI, false);
    }

    public boolean isBindi() {
        return getBoolean(KEY_BINDI);
    }

    public boolean isSwapped() {
        return getBoolean(KEY_SWAP);
    }

}
