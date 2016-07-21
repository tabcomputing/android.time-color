package tabcomputing.tcwallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class SettingsMonitor implements SharedPreferences.OnSharedPreferenceChangeListener {

    AbstractSettings settings;

    public SettingsMonitor(Context context, AbstractSettings settings) {
        //this.context  = context;
        this.settings = settings;

        Context appContext = context.getApplicationContext();
        String prefName = settings.getPrefName();

        //Log.d("SettingsMonitor", " pref name: " + prefName);
        //Log.d("SettingsMonitor", " context: " + appContext.toString());

        SharedPreferences prefs = appContext.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        //prefs = settings.getPreferences(context);
        //prefs = PreferenceManager.getDefaultSharedPreferences(context);

        prefs.registerOnSharedPreferenceChangeListener(this);

        settings.readPreferences(prefs);
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

        for (SettingsListener r : receivers) {
            r.preferenceChanged(key);
        }
    }

}
