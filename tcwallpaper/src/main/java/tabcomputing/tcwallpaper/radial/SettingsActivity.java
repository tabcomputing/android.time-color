package tabcomputing.tcwallpaper.radial;

import tabcomputing.library.paper.AbstractSettingsActivity;
import tabcomputing.tcwallpaper.R;

public class SettingsActivity extends AbstractSettingsActivity {

    @Override
    public int getPrefResId() {
        return R.xml.radial_preferences;
    }

    // TODO: maybe this should have it's own name
    private final String[] UPGRADES = {Settings.KEY_COLOR_SWAP};

    //@Override
    //public String[] getUpgradeOptions() {
    //    return UPGRADES;
    //}

}
