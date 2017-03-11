package tabcomputing.chronochrome.wave;

import tabcomputing.library.paper.AbstractSettingsActivity;

public class SettingsActivity extends AbstractSettingsActivity {

    //private final String[] UPGRADES = {"sunGlare"};

    //@Override
    //public String[] getUpgradeOptions() {
    //    if (CommonSettings.DEBUG) {
    //        return null;
    //    }
    //    return UPGRADES;
    //}

    @Override
    public int getPrefResId() {
        return R.xml.wave_preferences;
    }

}