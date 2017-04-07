package tabcomputing.chronochrome.stripes;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_SIZE = "size";

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

        propertyInteger(KEY_ORIENTATION, 0);
        propertyInteger(KEY_SIZE, 1);
    }

    public int getSize() {
        return getInteger(KEY_SIZE);
    }

}
