package tabcomputing.tcwallpaper.bigtime;

import android.graphics.Typeface;

import tabcomputing.tcwallpaper.CommonSettings;
import tabcomputing.library.paper.FontScale;

public class Settings extends CommonSettings {

    static final String KEY_TYPEFACE = "typeface";

    public Settings() {
        propertyBoolean(KEY_CUSTOM_SETTINGS, false);

        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, false);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        //propertyBoolean(KEY_CLOCK_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);
        propertyBoolean(KEY_BASE_CONVERT, true);

        propertyInteger(KEY_TYPEFACE, 0);
    }

    //@Override
    //public String getPrefName() {
    //    return "bigtime";
    //}

    //private Typeface typeface;

    //private int typefaceId = 0;

    //public Typeface getTypeface() {
    //    if (getInteger(KEY_TYPEFACE) != typefaceId || typeface == null) {
    //        setupTypeface();
    //    }
    //    return typeface;
    //}

    /**
     * Get a typeface that corresponds to the abstraction.
     *
     * @return              typeface
     */
    @Override
    public Typeface newTypeface(int id) {
        //AssetManager assets = context.getAssets();
        Typeface typeface;
        switch (id) {
            case 7:
                typeface = Typeface.createFromAsset(assets, "basic.ttf");
                break;
            case 6:
                typeface = Typeface.createFromAsset(assets, "1012.ttf");
                break;
            case 5:
                typeface = Typeface.createFromAsset(assets, "toolego.ttf");
                break;
            case 4:
                typeface = Typeface.createFromAsset(assets, "diskopia2.ttf");
                break;
            case 3:
                typeface = Typeface.createFromAsset(assets, "disco.ttf");
                break;
            case 2:
                typeface = Typeface.createFromAsset(assets, "origap.ttf");
                break;
            case 1:
                typeface = Typeface.createFromAsset(assets, "neospacial.ttf");
                break;
            default:
                typeface = Typeface.createFromAsset(assets, "cubes.ttf");
                break;
        }
        return typeface;
    }

    @Override
    public FontScale typefaceScale() {
        FontScale fs = new FontScale();
        switch (getInteger(KEY_TYPEFACE)) {
            case 7:
                fs.set(0.05f, -0.2f, 1, 1.25f);
                break;
            case 6:
                fs.set(0.025f, -0.1f, 1, 1.4f);
                break;
            case 5:  // toolego
                fs.set(0, 0, 1, 1.55f);
                break;
            case 4:  // diskopia2
                fs.set(0.03f, 0, 1, 1.3f);
                break;
            case 3:  // disco
                fs.set(0.025f, -0.1f, 1, 1.25f);
                break;
            case 2:  // origap
                fs.set(0, -0.03f, 1, 1.2f);
                break;
            case 1:  // neospacial
                fs.set(0, -0.03f, 1, 1);
                break;
            case 0:  // cubes
                fs.set(0, -0.2f, 1, 1.3f);
                break;
            default:
                fs.set(0, 0, 1, 1);
                break;
        }
        return fs;
    }

    // TODO: Make Big time a bonus pattern using cubes, digital, neospatial, etc.

}
