package tabcomputing.tcwallpaper.bigtime;

import tabcomputing.tcwallpaper.BaseSettingsActivity;
import tabcomputing.tcwallpaper.R;

public class SettingsActivity extends BaseSettingsActivity {

    //public String getPrefName() {
    //    return "bigtime";
    //}

    @Override
    public int getPrefResId() {
        return R.xml.bigtime_preferences;
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

        addPreferencesFromResource(R.xml.pieslice_preferences);
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
