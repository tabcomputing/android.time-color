package tabcomputing.chronochrome.orb;

import android.os.Bundle;

import tabcomputing.library.paper.AbstractSettingsFragment;

public class SettingsFragment extends AbstractSettingsFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
