package tabcomputing.chronochrome.echo;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    //static final String KEY_HIDE_ORB = "hideOrb";
    static final String KEY_SWAP = "colorSwap";

    // Singleton
    private static Settings instance;
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

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

        //propertyBoolean(KEY_HIDE_ORB, false);
        propertyBoolean(KEY_SWAP, false);
    }

    //public boolean isOrbHidden() {
    //    return getBoolean(KEY_HIDE_ORB);
    //}

    public boolean isSwapped() {
        return getBoolean(KEY_SWAP);
    }

}
