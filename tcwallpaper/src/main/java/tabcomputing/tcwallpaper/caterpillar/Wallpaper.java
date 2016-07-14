package tabcomputing.tcwallpaper.caterpillar;

import tabcomputing.tcwallpaper.AbstractPattern;
import tabcomputing.tcwallpaper.AbstractWallpaper;
import tabcomputing.tcwallpaper.CommonSettings;

public class Wallpaper extends AbstractWallpaper {

    private Settings settings;
    private PatternCaterpillar pattern;

    public Wallpaper() {
        settings = new Settings();
        pattern  = new PatternCaterpillar(this);
    }

    @Override
    protected AbstractPattern getPattern() {
        return pattern;
    }

    @Override
    protected CommonSettings getSettings() {
        return settings;
    }

    @Override
    protected void onPreferenceChange(String key) {
        pattern.preferenceChanged(key);
    }

}
