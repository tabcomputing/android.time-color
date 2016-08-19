package tabcomputing.tcwallpaper.fractal;

import tabcomputing.library.paper.AbstractSettingsActivity;
import tabcomputing.tcwallpaper.CommonSettings;
import tabcomputing.tcwallpaper.R;

public class SettingsActivity extends AbstractSettingsActivity {

    /*
    private final String[] UPGRADES = {"sunGlare"};

    @Override
    public String[] getUpgradeOptions() {
        if (CommonSettings.DEBUG) {
            return null;
        }
        return UPGRADES;
    }
    */

    // TODO: need preferences if we ever use this
    @Override
    public int getPrefResId() {
        return R.xml.ring_preferences;
    }

}