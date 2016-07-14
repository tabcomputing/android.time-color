package tabcomputing.tcwallpaper.relic;

import android.graphics.Typeface;

import tabcomputing.library.paper.CommonSettings;
import tabcomputing.library.paper.FontScale;

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
                font = Typeface.createFromAsset(assets, "abstract_flakes.ttf"); //"toolego.ttf");
                break;
            case 4:
                font = Typeface.createFromAsset(assets, "abstract_futurelic.ttf");
                break;
            case 3:
                font = Typeface.createFromAsset(assets, "abstract_maze.ttf");
                break;
            case 2:
                font = Typeface.createFromAsset(assets, "abstract_space.ttf");
                break;
            case 1:
                font = Typeface.createFromAsset(assets, "abstract_evilz.ttf");
                break;
            default:
                font = Typeface.createFromAsset(assets, "abstract_setiperu.ttf");
        }
        return font;
    }

    @Override
    public FontScale typefaceScale() {
        FontScale fs = new FontScale();
        switch (getInteger(KEY_ABSTRACTION)) {
            case 5:  // snow flakes
                fs.set(0, 0.1f, 1, 0.9f);
                break;
            case 4:  // futurelic
                fs.set(0.05f, 0, 1, 1);
                break;
            case 3:  // maze (guitarism)
                fs.set(0, 0, 1, 1);
                break;
            case 2:  // space
                fs.set(0, 0.05f, 1, 0.95f);
                break;
            case 1:  // evilz
                fs.set(0, -0.05f, 1, 1);
                break;
            case 0: // setiperu
                fs.set(0.025f, -0.07f, 1, 0.97f);
                break;
            default:
                fs.set(0, 0, 1, 1);
                break;
        }
        return fs;
    }

}
