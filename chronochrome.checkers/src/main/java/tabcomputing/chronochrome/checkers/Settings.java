package tabcomputing.chronochrome.checkers;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_FADE = "fade";
    static final String KEY_SIZE = "size";
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

        propertyBoolean(KEY_SWAP, false);
        propertyInteger(KEY_SIZE, 1);
        propertyBoolean(KEY_FADE, false);
    }

    public int getSize() {
        return getInteger(KEY_SIZE);
    }

    public boolean isFade() {
        return getBoolean(KEY_FADE);
    }

    public boolean isSwapped() {
        return getBoolean(KEY_SWAP);
    }

    public void toggleFade() {
        setBoolean(KEY_FADE, !isFade());
    }

    @Override
    protected String toastMessage(String settingKey) {
        switch (settingKey) {
            case KEY_FADE:
                if (isFade()) {
                    return "Fade is On.";
                } else {
                    return "Fade is Off.";
                }
            default:
                return super.toastMessage(settingKey);
        }
    }

}
