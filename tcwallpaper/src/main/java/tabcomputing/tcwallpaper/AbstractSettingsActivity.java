package tabcomputing.tcwallpaper;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public abstract class AbstractSettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment settings = new SettingsFragment();

        Bundle args = new Bundle();
        args.putString("name", getPrefName());
        args.putInt("resId", getPrefResId());
        settings.setArguments(args);

        getFragmentManager().beginTransaction().replace(android.R.id.content, settings).commit();
    }

    /**
     * Settings fragment, which I really don't get, but what Google say, I Google do.
     */
    public static class SettingsFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            Bundle args = getArguments();

            String name = args.getString("name");
            int resId = args.getInt("resId");

            PreferenceManager manager = getPreferenceManager();
            //manager.setSharedPreferencesMode(MODE_PRIVATE);

            // Apparently this is the only way to tell the settings activity to use a specific file.
            manager.setSharedPreferencesName(name);

            //Log.d("settings", "pref name: " + manager.getSharedPreferencesName());

            addPreferencesFromResource(resId);
        }
    }

    /**
     *  Use the library package name for preference settings name.
     *
     *  NOTE: Originally this was an abstract method like getRefResId() that
     *        had to be overridden. Maybe that's safer, but this works too.
     */
    public String getPrefName() {
        String[] parts = getClass().getName().split("[.]");
        String name = parts[parts.length - 2];
        return name + "_preferences";
    }

    //public abstract String getPrefName();

    /**
     * Resource Id to preferences XML. This must be overridden in subclasses.
     *
     * TODO: Is there a way to generalize this so subclasses aren't needed?
     *       It would be difficult b/c the settings activity has to be
     *       referenced in the AndroidManifest.
     */
    public abstract int getPrefResId();

}
