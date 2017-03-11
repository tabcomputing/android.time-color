package tabcomputing.chronochrome.circles;

import tabcomputing.library.paper.AbstractWallpaper;

public class Wallpaper extends AbstractWallpaper {

    private Settings settings;
    private PatternCircles pattern;

    public Wallpaper() {
        settings = new Settings();
        pattern  = new PatternCircles(this);
    }

    @Override
    protected PatternCircles getPattern() {
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
