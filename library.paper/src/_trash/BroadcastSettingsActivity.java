package tabcomputing.library.paper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Settings activity
 */
public class BroadcastSettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    //private CheckBoxPreference displayColorWheel;
    //private CheckBoxPreference displayClockFace;

    private ClockSettings settings = ClockSettings.getInstance();

    private Context prefContext;

    Intent broadcastIntent = new Intent();

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        try {
            prefContext = createPackageContext("tabcomputing.wallpaper", Context.MODE_PRIVATE);
        } catch(Exception e) {
            prefContext = getBaseContext();
        }

        PreferenceManager prefMgr = getPreferenceManager();
        prefMgr.setSharedPreferencesName(getDefaultSharedPreferencesName(prefContext));
        //prefMgr.setSharedPreferencesMode(MODE_WORLD_READABLE);
        prefMgr.setSharedPreferencesMode(MODE_PRIVATE);
        */

        prefContext = getBaseContext();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //prefs = settings.getPreferences(getBaseContext());

        prefs.registerOnSharedPreferenceChangeListener(this);

        //addPreferencesFromResource(R.xml.preferences); // FIXME: HERE

        //displayColorWheel = (CheckBoxPreference) findPreference(Settings.KEY_COLOR_WHEEL);
        //displayClockFace  = (CheckBoxPreference) findPreference(Settings.KEY_CLOCK_FACE);

        //displayColorWheel.setOnPreferenceChangeListener(prefChangeListener);
        //displayClockFace.setOnPreferenceChangeListener(prefChangeListener);
    }

    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    @Override
    protected void onDestroy() {
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    private void broadcastPreferenceChanged(String key) {
        Log.d("-send------------>", "" + prefContext + " key: " + key);

        broadcastIntent.setAction("tabcomputing.wallpaper.broadcast.settings");

        broadcastIntent.putExtra("key", key);

        switch(settings.getType(key)) {
            case ClockSettings.TYPE_BOOLEAN:
                broadcastIntent.putExtra("value", settings.getBoolean(key));
                break;
            case ClockSettings.TYPE_INTEGER:
                broadcastIntent.putExtra("value", settings.getInteger(key));
                break;
            default:
                Log.d("SettingsActivity", "Broadcasting " + key + " could find no type!");
        }

        prefContext.sendBroadcast(broadcastIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        settings.changePreference(sharedPreferences, key);
        //broadcastPreferenceChanged(key);
    }

    /*
    private OnPreferenceChangeListener prefChangeListener = new OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String key = preference.getKey();

            /*
            if (Settings.KEY_CLOCK_FACE.equals(key)) {
                boolean value = (Boolean) newValue;
                if (value) {
                    displayColorWheel.setChecked(true);
                }
                return true;
            }

            if (Settings.KEY_COLOR_WHEEL.equals(key)) {
                boolean value = (Boolean) newValue;
                if (!value) {
                    displayClockFace.setChecked(false);
                }
                return true;
            }
            *

            // TODO: Apply default settings for 180 orientation and clock numbers when time system is changed.

            /*
            if (DISPLAY_SECONDS_KEY.equals(key)) {
                boolean value = (Boolean) newValue;
                //Toast.makeText(getApplicationContext(),
                //        R.string.displayseconds_title + " " + (value ? R.string.enabled : R.string.disabled),
                //        Toast.LENGTH_SHORT).show();
                return true;
            }
            *

            return false;
        }
    };
    */
}