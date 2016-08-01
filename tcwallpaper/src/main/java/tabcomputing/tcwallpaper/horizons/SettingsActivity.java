package tabcomputing.tcwallpaper.horizons;

import tabcomputing.library.paper.AbstractSettingsActivity;
import tabcomputing.tcwallpaper.R;

public class SettingsActivity extends AbstractSettingsActivity {

    @Override
    public int getPrefResId() {
        return R.xml.horizons_preferences;
    }

    private final String[] UPGRADES = {Settings.KEY_SUN};

    //@Override
    //public String[] getUpgradeOptions() {
    //    return UPGRADES;
    //}

}
