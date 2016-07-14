package tabcomputing.tcwallpaper.bigtime;

import android.graphics.Typeface;

import tabcomputing.tcwallpaper.CommonSettings;
import tabcomputing.tcwallpaper.FontScale;

public class Settings extends CommonSettings {

    static final String KEY_ABSTRACTION = "abstraction";

    public Settings() {
        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        //propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyInteger(KEY_ABSTRACTION, 0);
    }

    //@Override
    //public String getPrefName() {
    //    return "bigtime";
    //}

    private Typeface abstraction;

    private int abstractionId = 0;

    public Typeface getAbstraction() {
        if (getInteger(KEY_ABSTRACTION) != abstractionId || abstraction == null) {
            abstractionId = getInteger(KEY_ABSTRACTION);
            abstraction = newAbstraction();
        }
        return abstraction;
    }

    /**
     * Get the typeface that corresponds to the abstraction.
     *
     * @return              typeface
     */
    private Typeface newAbstraction() {
        //AssetManager assets = context.getAssets();

        Typeface font;

        switch (getInteger(KEY_ABSTRACTION)) {
            case 5:
                font = Typeface.createFromAsset(assets, "toolego.ttf");
                break;
            case 4:
                font = Typeface.createFromAsset(assets, "futurelic.ttf");
                break;
            case 3:
                font = Typeface.createFromAsset(assets, "d3guitarism.ttf");
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
        switch (getInteger(KEY_ABSTRACTION)) {
            case 5:  // toolego
                fs.set(0, 0, 1, 1.55f);
                //fs.set(0, 0.2f, 1, 1.3f);
                break;
            case 4:  // futurelic
                fs.set(0.05f, 0, 1, 1);
                break;
            case 3:  // guitarism
                fs.set(0, 0, 1, 1);
                break;
            case 2:  // space
                fs.set(0, 0.1f, 1, 0.9f);
                break;
            case 1:  // neospacial
                fs.set(0, 0, 1, 1);
                break;
            case 0: // setiperu
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
