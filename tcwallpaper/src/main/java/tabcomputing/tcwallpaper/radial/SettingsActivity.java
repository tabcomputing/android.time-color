package tabcomputing.tcwallpaper.radial;

import tabcomputing.library.paper.AbstractSettingsActivity;
import tabcomputing.tcwallpaper.CommonSettings;
import tabcomputing.tcwallpaper.R;

public class SettingsActivity extends AbstractSettingsActivity {

    private final String[] UPGRADES = {Settings.KEY_COLOR_REVERSE};

    @Override
    public String[] getUpgradeOptions() {
        if (CommonSettings.DEBUG) {
            return null;
        }
        return UPGRADES;
    }

    @Override
    public int getPrefResId() {
        return R.xml.radial_preferences;
    }

}
