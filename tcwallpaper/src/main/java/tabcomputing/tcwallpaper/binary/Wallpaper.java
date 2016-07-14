package tabcomputing.tcwallpaper.binary;

import tabcomputing.library.paper.AbstractPattern;
import tabcomputing.library.paper.AbstractWallpaper;
import tabcomputing.library.paper.CommonSettings;

public class Wallpaper extends AbstractWallpaper {

    private Settings settings;
    private AbstractPattern pattern;

    public Wallpaper() {
        settings = new Settings();
        pattern  = new Pattern(settings);
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
