package tabcomputing.tcwallpaper.rainbow;

import tabcomputing.library.paper.AbstractSettingsActivity;
import tabcomputing.tcwallpaper.CommonSettings;
import tabcomputing.tcwallpaper.R;

public class SettingsActivity extends AbstractSettingsActivity {

    private final String[] UPGRADES = {"tickless"};

    @Override
    public String[] getUpgradeOptions() {
        if (CommonSettings.DEBUG) {
            return null;
        }
        return UPGRADES;
    }

    @Override
    public int getPrefResId() {
        return R.xml.rainbow_preferences;
    }

}