package tabcomputing.tcwallpaper.bigtime;

import android.graphics.Typeface;

import tabcomputing.tcwallpaper.CommonSettings;
import tabcomputing.tcwallpaper.FontScale;

public class Settings extends CommonSettings {

    static final String KEY_TYPEFACE = "abstraction";

    public Settings() {
        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        //propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

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
            case 5:
                typeface = Typeface.createFromAsset(assets, "toolego.ttf");
                break;
            case 4:
                typeface = Typeface.createFromAsset(assets, "disco.ttf");
                break;
            case 3:
                typeface = Typeface.createFromAsset(assets, "origap.ttf");
                break;
            case 2:
                typeface = Typeface.createFromAsset(assets, "cubes.ttf");
                break;
            case 1:
                typeface = Typeface.createFromAsset(assets, "neospacial.ttf");
                break;
            default:
                typeface = Typeface.createFromAsset(assets, "digital.ttf");
        }
        return typeface;
    }

    @Override
    public FontScale typefaceScale() {
        FontScale fs = new FontScale();
        switch (getInteger(KEY_TYPEFACE)) {
            case 5:  // toolego
                fs.set(0, 0, 1, 1.55f);
                //fs.set(0, 0.2f, 1, 1.3f);
                break;
            case 4:  // disco
                fs.set(0.05f, 0, 1, 1);
                break;
            case 3:  // origap
                fs.set(0, 0, 1, 1);
                break;
            case 2:  // cubes
                fs.set(0, 0.1f, 1, 0.9f);
                break;
            case 1:  // neospacial
                fs.set(0, 0, 1, 1);
                break;
            case 0: // digital
                fs.set(0.025f, 0.025f, 1, 1.05f);
                break;
            default:
                fs.set(0, 0, 1, 1);
                break;
        }
        return fs;
    }

    // TODO: Make Big time a bonus pattern using cubes, digital, neospatial, etc.

}
