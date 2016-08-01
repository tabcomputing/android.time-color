package tabcomputing.tcwallpaper.caterpillar;

import tabcomputing.library.paper.AbstractSettingsActivity;
import tabcomputing.tcwallpaper.R;

public class SettingsActivity extends AbstractSettingsActivity {

    //@Override
    //public String getPrefName() {
    //    return "caterpillar";
    //}

    @Override
    public int getPrefResId() {
        return R.xml.caterpillar_preferences;
    }

    private final String[] UPGRADES = {Settings.KEY_MEAN};

    //@Override
    //public String[] getUpgradeOptions() {
    //    return UPGRADES;
    //}

}
