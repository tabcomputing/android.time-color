package tabcomputing.tcwallpaper;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

@Deprecated
public class ClockSettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.clock_preferences);
        }
    }

}

/*
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsFragment sf = new SettingsFragment();
        //int resId = getPrefResId();
        //sf.setArguments();
        getFragmentManager().beginTransaction().replace(android.R.id.content, sf).commit();
    }

    public static class SettingsFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            int prefResId = 0;

            //addPreferencesFromResource(R.xml.bigtime_preferences);
            addPreferencesFromResource(prefResId);
        }
    }

    protected int getPrefResId() {
        String uri = "tabcomputing.tcwallpaper.bigtime.Wallpaper";

        Resources res = getResources();

        return res.getIdentifier(uri, "String", getPackageName());

        //try {
        //    ComponentName name = new ComponentName(getPackageName(), uri);
        //    ServiceInfo si = getPackageManager().getServiceInfo(name, PackageManager.GET_META_DATA);
        //    Bundle bundle = si.metaData;
        //    String resource
        //} catch(Exception e) {
        //    Log.d("---->", "Failed!");
        //}

    }

}
*/