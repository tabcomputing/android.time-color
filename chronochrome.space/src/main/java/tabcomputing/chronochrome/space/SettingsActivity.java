package tabcomputing.chronochrome.space;

import tabcomputing.library.paper.AbstractSettingsActivity;
import tabcomputing.library.paper.CommonSettings;

public class SettingsActivity extends AbstractSettingsActivity {

    private final String[] UPGRADES = {Settings.KEY_PATTERN};

    @Override
    public String[] getUpgradeOptions() {
        if (CommonSettings.DEBUG) {
            return null;
        }
        return UPGRADES;
    }

    @Override
    public int getPrefResId() {
        return R.xml.space_preferences;
    }

}
