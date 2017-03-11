package tabcomputing.chronochrome.binary;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    public Settings() {
        defineProperties();
    }

    // Singleton
    private static Settings instance;
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    @Override
    protected void defineProperties() {
        super.defineProperties();

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        //propertyBoolean(KEY_CLOCK_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);
    }

    //@Override
    //public String getPrefName() {
    //    return "binarystripes";
    //}

}
