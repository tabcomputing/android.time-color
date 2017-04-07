package tabcomputing.chronochrome.solid;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_GLARE = "sunGlare";
    static final String KEY_PART  = "timePart";

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

        propertyInteger(KEY_PART, 0);
        propertyBoolean(KEY_GLARE, false);
    }

    public boolean useGlare() {
        return getBoolean(KEY_GLARE);
    }

    public int timePart() {
        return getInteger(KEY_PART);
    }

    public void toggleGlare() {
        setBoolean(KEY_GLARE, !useGlare());
    }

    @Override
    protected String toastMessage(String settingKey) {
        switch (settingKey) {
            case KEY_GLARE:
                if (useGlare()) {
                    return "Sun glare is On.";
                } else {
                    return "Sun glare is Off.";
                }
            default:
                return super.toastMessage(settingKey);
        }
    }

}
