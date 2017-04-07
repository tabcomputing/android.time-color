package tabcomputing.chronochrome.fractal;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_SMOOTH = "smooth";
    static final String KEY_SIZE = "size";
    static final String KEY_DENSITY = "density";

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

        propertyBoolean(KEY_SMOOTH, false);
        propertyInteger(KEY_SIZE, 1);
        propertyInteger(KEY_DENSITY, 1);
    }

    public boolean isSmooth() {
        return getBoolean(KEY_SMOOTH);
    }

    public int getSize() {
        return getInteger(KEY_SIZE);
    }

    public int getDensity() {
        return getInteger(KEY_DENSITY);
    }

}
