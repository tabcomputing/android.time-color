package tabcomputing.tcwallpaper.gradient;

import tabcomputing.library.paper.AbstractSettingsActivity;
import tabcomputing.tcwallpaper.R;

public class SettingsActivity extends AbstractSettingsActivity {

    @Override
    public int getPrefResId() {
        return R.xml.gradient_preferences;
    }

    private final String[] UPGRADES = {Settings.KEY_GLARE};

    //@Override
    //public String[] getUpgradeOptions() {
    //    return UPGRADES;
    //}

}
