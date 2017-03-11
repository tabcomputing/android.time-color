package tabcomputing.chronochrome.maze;

import tabcomputing.tcwallpaper.CommonSettings;

public class Settings extends CommonSettings {

    static final String KEY_FADE = "fade";
    static final String KEY_BACKDROP = "backdrop";

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

        propertyBoolean(KEY_FADE, true);
        propertyInteger(KEY_BACKDROP, 0);
    }

    public boolean isFaded() {
        return getBoolean(KEY_FADE);
    }

    public int getBackdrop() { return getInteger(KEY_BACKDROP); }




    /*
     * Get a typeface that corresponds to the abstraction.
     *
     * @return              typeface
     *
    @Override
    public Typeface newTypeface(int id) {
        //AssetManager assets = context.getAssets();
        Typeface typeface;
        switch (id) {
            case 8:
                typeface = Typeface.createFromAsset(assets, "basic.ttf");
                break;
            case 7:
                // make maze a separate wallpaper
                typeface = Typeface.createFromAsset(assets, "abstract_maze.ttf");
                break;
            case 6:
                typeface = Typeface.createFromAsset(assets, "toolego.ttf");
                break;
            case 5:
                typeface = Typeface.createFromAsset(assets, "diskopia2.ttf");
                break;
            case 4:
                typeface = Typeface.createFromAsset(assets, "disco.ttf");
                break;
            case 3:
                typeface = Typeface.createFromAsset(assets, "origap.ttf");
                break;
            case 2:
                typeface = Typeface.createFromAsset(assets, "neospacial.ttf");
                break;
            case 1:
                typeface = Typeface.createFromAsset(assets, "cubes.ttf");
                break;
            default:
                typeface = Typeface.createFromAsset(assets, "digital.ttf");
        }
        return typeface;
    }
    */

    /*
    @Override
    public FontScale typefaceScale() {
        FontScale fs = new FontScale();
        switch (getInteger(KEY_TYPEFACE)) {
            case 8:
                fs.set(0.05f, -0.2f, 1, 1.25f);
                break;
            case 7:
                fs.set(0, -0.025f, 1, 1);
                break;
            case 6:  // toolego
                fs.set(0, 0, 1, 1.55f);
                break;
            case 5:  // diskopia2
                fs.set(0.02f, 0, 1, 1.3f);
                break;
            case 4:  // disco
                fs.set(0.025f, -0.2f, 1, 1.25f);
                break;
            case 3:  // origap
                fs.set(0, 0, 1, 1);
                break;
            case 2:  // neospacial
                fs.set(0, 0, 1, 1);
                break;
            case 1:  // cubes
                fs.set(0, -0.2f, 1, 1.3f);
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
    */

}
