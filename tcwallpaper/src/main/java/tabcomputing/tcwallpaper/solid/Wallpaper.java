package tabcomputing.tcwallpaper.solid;

import tabcomputing.tcwallpaper.AbstractPattern;
import tabcomputing.tcwallpaper.AbstractWallpaper;
import tabcomputing.tcwallpaper.CommonSettings;

public class Wallpaper extends AbstractWallpaper {

    private Settings settings;
    private Pattern  pattern;

    public Wallpaper() {
        settings = new Settings();
        pattern  = new Pattern(this);
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
