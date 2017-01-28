package tabcomputing.tcwallpaper.space;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_SIZE = "size";
    static final String KEY_FADE = "fade";
    static final String KEY_PATTERN = "pattern";

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
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyInteger(KEY_SIZE, 1);
        propertyBoolean(KEY_FADE, true);
        propertyString(KEY_PATTERN, "#1#2");
    }

    public int getSize() {
        return getInteger(KEY_SIZE);
    }

    public boolean getFade() {
        return getBoolean(KEY_FADE);
    }

    public String getPattern() {
        return getString(KEY_PATTERN);
    }

}
