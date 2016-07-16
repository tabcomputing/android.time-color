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

    private Typeface typeface;

    private int typefaceId = 0;

    public Typeface getTypeface() {
        if (getInteger(KEY_TYPEFACE) != typefaceId || typeface == null) {
            typefaceId = getInteger(KEY_TYPEFACE);
            typeface = newTypeface();
        }
        return typeface;
    }

    /**
     * Get the typeface that corresponds to the abstraction.
     *
     * @return              typeface
     */
    @Override
    public Typeface newTypeface() {
        //AssetManager assets = context.getAssets();

        Typeface font;

        switch (getInteger(KEY_TYPEFACE)) {
            case 5:
                font = Typeface.createFromAsset(assets, "toolego.ttf");
                break;
            case 4:
                font = Typeface.createFromAsset(assets, "disco.ttf");
                break;
            case 3:
                font = Typeface.createFromAsset(assets, "origap.ttf");
                break;
            case 2:
                font = Typeface.createFromAsset(assets, "cubes.ttf");
                break;
            case 1:
                font = Typeface.createFromAsset(assets, "neospacial.ttf");
                break;
            default:
                font = Typeface.createFromAsset(assets, "digital.ttf");
        }
        return font;
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
