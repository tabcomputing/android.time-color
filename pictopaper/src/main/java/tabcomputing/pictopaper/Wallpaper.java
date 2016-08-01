package tabcomputing.pictopaper;

import tabcomputing.library.paper.AbstractWallpaper;

// TODO: How are we going to handle upgrading here if there is no main activity?
//       Can we add a button to the settings activity?

public class Wallpaper extends AbstractWallpaper {

    private Settings settings;
    private Pattern pattern;

    public Wallpaper() {
        settings = new Settings();
        pattern  = new Pattern(this);
    }

    @Override
    protected Pattern getPattern() {
        return pattern;
    }

    @Override
    protected Settings getSettings() {
        return settings;
    }

    //@Override
    //protected void onPreferenceChange(String key) {
    //    pattern.preferenceChanged(key);
    //}

}
