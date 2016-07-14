package tabcomputing.tcwallpaper.relic;

import tabcomputing.library.paper.AbstractPattern;
import tabcomputing.library.paper.AbstractWallpaper;

public class Wallpaper extends AbstractWallpaper {

    private Settings settings;
    private AbstractPattern pattern;

    public Wallpaper() {
        settings = new Settings();
        pattern  = new Pattern(this);
    }

    @Override
    protected AbstractPattern getPattern() {
        return pattern;
    }

    @Override
    protected Settings getSettings() {
        return settings;
    }

    @Override
    protected void onPreferenceChange(String key) {
        pattern.preferenceChanged(key);
    }

}
