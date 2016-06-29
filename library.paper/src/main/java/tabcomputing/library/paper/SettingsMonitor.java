package tabcomputing.library.paper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

public class SettingsMonitor implements SharedPreferences.OnSharedPreferenceChangeListener {

    AbstractSettings settings;

    public SettingsMonitor(Context context, AbstractSettings settings) {
        //this.context  = context;
        this.settings = settings;

        String name = settings.getPrefName() + "_preferences";

        //Log.d("-------------------->", " pref name: " + name);

        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        //prefs = settings.getPreferences(context);
        //prefs = PreferenceManager.getDefaultSharedPreferences(context);

        prefs.registerOnSharedPreferenceChangeListener(this);

        settings.readPreferences(prefs);
    }

    // Wallpaper context.
    //private Context context;

    //private AbstractSettings settings;

    private SharedPreferences prefs;

    private ArrayList<SettingsListener> receivers = new ArrayList<>();

    /**
     * @param receiver
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

        //Log.d("SettingsObserver", "Key: " + key);

        for (SettingsListener r : receivers) {
            r.preferenceChanged(key);
        }

        /*
        //if (Settings.KEY_PATTERN.equals(key)) {
        //    configurePattern();
        //}

        if (Settings.KEY_COLOR_DUPLEX.equals(key)) {
            configureDuplex();
        }

        if (Settings.KEY_TIME_TYPE.equals(key)) {
            configureTimeSystem();
        }

        if (Settings.KEY_BASE.equals(key)) {
            configureBaseConversion();
        }

        if (Settings.KEY_COLOR_DYNAMIC.equals(key)) {
            //configureColorWheel();
        }

        if (Settings.KEY_COLOR_GAMUT.equals(key)) {
            configureColorGamut();
        }

        if (Settings.KEY_TYPEFACE.equals(key)) {
            configureTypeface();
        }

        // TODO: This is probably a good idea.
        //if (Settings.KEY_ROTATE_TIME.equals(key)) {
        //    configureTimeOffset();
        //}
        */

    }

    /*
    private void configureTimeSystem() {
        timeSystem = settings.getTimeSystem();

        for (SettingsListener r : receivers) {
            r.setTimeSystem(timeSystem);
        }

        //configureColorWheel();
    }

    private void configureDuplex() {
        //
    }

    private void configureBaseConversion() {
        //timeSystem.setBaseConverted(settings.baseConvert());
    }

    private void configureColorGamut() {
        colorWheel.setColorGamut(settings.getColorGamut());
    }

    private void configureRotation() {
        //double offset = (double) timeSystem.gmtOffset() / 24;
        double offset = 0.0;
        colorWheel.setOffset(offset);
    }

    private void configureTypeface() {
        typeface = settings.getTypeface(context.getAssets());

        for(SettingsListener r : receivers) {
            r.setTypeface(typeface);
        }
    }
    */

}
