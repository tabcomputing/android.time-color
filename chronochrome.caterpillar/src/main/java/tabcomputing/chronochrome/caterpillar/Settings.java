package tabcomputing.chronochrome.caterpillar;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    public static final String KEY_MEAN = "meanFace";

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

        propertyBoolean(KEY_MEAN, false);
    }

    public boolean isMean() {
        return getBoolean(KEY_MEAN);
    }

    public void toggleMean() {
        setBoolean(KEY_MEAN, !isMean());
    }

    @Override
    protected String toastMessage(String settingKey) {
        switch (settingKey) {
            case KEY_MEAN:
                if (isMean()) {
                    return "Mean face is On.";
                } else {
                    return "Mean face is Off.";
                }
            default:
                return super.toastMessage(settingKey);
        }
    }

}
