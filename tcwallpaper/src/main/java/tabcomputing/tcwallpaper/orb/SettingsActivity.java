package tabcomputing.tcwallpaper.orb;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(tabcomputing.wallpaper.orb.R.xml.orb_preferences);
        }
    }

}




 /*
public class SettingsActivity extends PreferenceActivity {  //implements SharedPreferences.OnSharedPreferenceChangeListener {

    //private Settings settings = Settings.getInstance();

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        addPreferencesFromResource(R.xml.radiant_preferences);
    }

    @Override
    protected void onDestroy() {
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    //@Override
    //public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    //    settings.changePreference(sharedPreferences, key);
    //}

}
*/
