package tabcomputing.tcwallpaper.tile;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_SIZE = "size";
    static final String KEY_DENSITY = "density";
    static final String KEY_SHAPE = "shape";
    static final String KEY_OUTLINE = "outline";
    static final String KEY_OPAQUE = "opaque";

    public Settings() {
        propertyBoolean(KEY_CUSTOM_SETTINGS, false);

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        //propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyInteger(KEY_SIZE, 1);
        propertyInteger(KEY_DENSITY, 1);
        propertyInteger(KEY_SHAPE, 0);
        propertyBoolean(KEY_OUTLINE, false);
        propertyBoolean(KEY_OPAQUE, false);
    }

    public int getSize() {
        return getInteger(KEY_SIZE);
    }

    public int getDensity() {
        return getInteger(KEY_DENSITY);
    }

    public int getShape() {
        return getInteger(KEY_SHAPE);
    }

    public boolean isOutlined() {
        return getBoolean(KEY_OUTLINE);
    }

    public boolean isOpaque() {
        return getBoolean(KEY_OPAQUE);
    }

}
