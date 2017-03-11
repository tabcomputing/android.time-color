package tabcomputing.library.paper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

public class SettingsMonitor implements SharedPreferences.OnSharedPreferenceChangeListener {

    Context context;

    AbstractSettings settings;

    public SettingsMonitor(Context context, AbstractSettings settings) {
        this.context  = context.getApplicationContext();
        this.settings = settings;

        //Log.d("SettingsMonitor", " pref name: " + prefName);
        //Log.d("SettingsMonitor", " context: " + appContext.toString());

        loadPreferences();
    }

    protected void loadPreferences() {
        //String prefName = settings.getPrefName();
        //SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        //prefs = settings.getPreferences(context);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        prefs.registerOnSharedPreferenceChangeListener(this);

        settings.readPreferences(prefs);

        if (settings.sharedSettings()) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
            settings.readPreferences(prefs);
        }
    }

    // Wallpaper context.
    //private Context context;
    //private AbstractSettings settings;
    //private SharedPreferences prefs;

    private ArrayList<SettingsListener> receivers = new ArrayList<>();

    /**
     * @param receiver      object that need to listen for setting changes
     */
    public void add(SettingsListener receiver) {
        receivers.add(receiver);
        receiver.resetPreferences();
    }

    /**
     *
     */
    public void reset() {
        for (SettingsListener r : receivers) {
            r.resetPreferences();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        settings.changePreference(sharedPreferences, key);

        //Log.d("log", "onSharedPreferenceChanged: " + key);

        if (key.equals(AbstractSettings.KEY_CUSTOM_SETTINGS)) {
            loadPreferences();
            //reset(); // TODO: do we need to reset?
            return;
        }

        for (SettingsListener r : receivers) {
            r.preferenceChanged(key);
        }
    }

}
