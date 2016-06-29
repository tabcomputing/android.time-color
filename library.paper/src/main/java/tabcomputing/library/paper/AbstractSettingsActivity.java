package tabcomputing.library.paper;

import android.content.Context;
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

    public static class SettingsFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            Bundle args = getArguments();

            String name = args.getString("name") + "_preferences";
            int resId = args.getInt("resId");

            PreferenceManager manager = getPreferenceManager();
            //manager.setSharedPreferencesMode(MODE_PRIVATE);
            manager.setSharedPreferencesName(name);

            //Log.d("------>", "pref name: " + manager.getSharedPreferencesName());

            addPreferencesFromResource(resId);
        }
    }

    /**
     *  Use the library package name for preference settings name.
     *
     *  NOTE: Originally this was an abstract method like getRefResId() that
     *        had to be overridden. Maybe that's safer, but this seems to work.
     */
    public String getPrefName() {
        String cName = getClass().getName();
        String[] cParts = cName.split("[.]");
        return cParts[cParts.length - 2];
    }
    //public abstract String getPrefName();

    public abstract int getPrefResId();

}
