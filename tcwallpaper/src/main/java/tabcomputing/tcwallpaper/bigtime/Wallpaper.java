package tabcomputing.tcwallpaper.bigtime;

import tabcomputing.library.paper.AbstractWallpaper;

public class Wallpaper extends AbstractWallpaper {

    private Settings settings;
    private PatternBigtime pattern;

    public Wallpaper() {
        settings = new Settings();
        pattern  = new PatternBigtime(this);
    }

    @Override
    protected PatternBigtime getPattern() {
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
