package tabcomputing.library.paper;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import tabcomputing.library.paper.BillOfSale;

public abstract class AbstractSettingsActivity extends PreferenceActivity {

    //static final String[] EMPTY = {};

    static final String KEY_PREF_NAME = "prefName";
    static final String KEY_PREF_ID   = "resId";
    static final String KEY_UPGRADES  = "upFields";
    static final String KEY_UPGRADED  = "isUpgraded";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment settings = new SettingsFragment();

        BillOfSale bos = BillOfSale.getInstance(getApplicationContext());

        Bundle args = new Bundle();
        args.putString(KEY_PREF_NAME, getPrefName());
        args.putInt(KEY_PREF_ID, getPrefResId());
        args.putStringArray(KEY_UPGRADES, getUpgradeOptions());  // Yeah! This can be null.
        args.putBoolean(KEY_UPGRADED, bos.isOwned());
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

            String name = args.getString(KEY_PREF_NAME);
            int resId   = args.getInt(KEY_PREF_ID);
            String[] upgradeFields = args.getStringArray(KEY_UPGRADES);
            boolean isUpgraded = args.getBoolean(KEY_UPGRADED);

            PreferenceManager manager = getPreferenceManager();
            //manager.setSharedPreferencesMode(MODE_PRIVATE);

            // apparently this is the only way to tell the settings activity to use a specific file
            manager.setSharedPreferencesName(name);

            //Log.d("settings", "pref name: " + manager.getSharedPreferencesName());

            addPreferencesFromResource(resId);

            // TODO: We could just get the billOfSale here instead of above, but since we still needs the upgradeFields it doesn't much matter.

            // enable/disable upgradable options
            if (upgradeFields != null) {
                for (String key : upgradeFields) {
                    Preference pref = findPreference(key);
                    pref.setEnabled(isUpgraded);
                }
            }
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

    /**
     * Override this is a pattern has settings that are only active if the user
     * has upgraded.
     */
    public String[] getUpgradeOptions() {
        return null;
    }

}
