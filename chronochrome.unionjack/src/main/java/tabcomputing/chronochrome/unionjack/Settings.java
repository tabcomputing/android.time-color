package tabcomputing.chronochrome.unionjack;

import tabcomputing.library.paper.CommonSettings;

public class Settings extends CommonSettings {

    private static final String KEY_SWAP  = "colorSwap";
    private static final String KEY_ROYAL = "colorRoyal";

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
        //propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyBoolean(KEY_SWAP, false);
        propertyBoolean(KEY_ROYAL, false);
    }

    public boolean isSwapped() {
        return getBoolean(KEY_SWAP);
    }

    public boolean isRoyal() {
        return getBoolean(KEY_ROYAL);
    }

    public void toggleRoyal() {
        setBoolean(KEY_ROYAL, !isRoyal());
    }

    public void toggleSeconds() {
        setBoolean(KEY_TIME_SECONDS, !withSeconds());
    }

    // TODO: message needs to be in resource file for future translations

    @Override
    protected String toastMessage(String settingKey) {
        switch (settingKey) {
            case KEY_ROYAL:
                if (isRoyal()) {
                    return "Royal colours only!";
                } else {
                    return "Colour by time.";
                }
            default:
                return super.toastMessage(settingKey);
        }
    }

}
