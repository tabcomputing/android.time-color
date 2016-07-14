package tabcomputing.tcwallpaper.relic;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_STYLE = "relic_style";

    public Settings() {
        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyInteger(KEY_STYLE, 0);
    }

    public int getStyle() {
        return getInteger(KEY_STYLE);
    }

    //@Override
    //public String getPrefName() {
    //    return "bigtime";
    //}

    /*
    private Typeface abstraction;

    private int abstractionId = 0;

    public Typeface getAbstraction() {
        if (getInteger(KEY_ABSTRACTION) != abstractionId || abstraction == null) {
            abstractionId = getInteger(KEY_ABSTRACTION);
            abstraction = newAbstraction();
        }
        return abstraction;
    }
    */

}
