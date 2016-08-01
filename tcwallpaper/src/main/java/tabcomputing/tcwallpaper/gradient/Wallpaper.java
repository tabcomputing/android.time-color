package tabcomputing.tcwallpaper.gradient;

import tabcomputing.library.paper.AbstractWallpaper;

public class Wallpaper extends AbstractWallpaper {

    private Settings settings;
    private Pattern  pattern;

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
    //   pattern.preferenceChanged(key);
    //}

}
