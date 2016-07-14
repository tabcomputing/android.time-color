package tabcomputing.tcwallpaper.mondrian;

import tabcomputing.tcwallpaper.AbstractPattern;
import tabcomputing.tcwallpaper.AbstractWallpaper;

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
    protected Settings getSettings() {
        return settings;
    }

    @Override
    protected void onPreferenceChange(String key) {
        pattern.preferenceChanged(key);
    }

}
