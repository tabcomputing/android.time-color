package tabcomputing.tcwallpaper.caterpillar;

import tabcomputing.library.paper.AbstractWallpaper;

public class Wallpaper extends AbstractWallpaper {

    private Settings settings;
    private PatternCaterpillar pattern;

    public Wallpaper() {
        settings = new Settings();
        pattern  = new PatternCaterpillar(this);
    }

    @Override
    protected PatternCaterpillar getPattern() {
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
