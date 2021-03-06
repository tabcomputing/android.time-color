package tabcomputing.library.paper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Set;

public abstract class AbstractSettings {

    public AbstractSettings() {}

    // The only global setting defined here. It us used to determine if shared settings
    // will be ignored in favor of custom ones.
    public static final String KEY_CUSTOM_SETTINGS = "customSettings";

    public static final int TYPE_BOOLEAN = 0;
    public static final int TYPE_INTEGER = 1;
    public static final int TYPE_STRING  = 2;

    protected HashMap<String, Integer> propertyTypes = new HashMap<>();

    protected HashMap<String, Boolean> booleanDefaults = new HashMap<String, Boolean>();
    protected HashMap<String, Integer> integerDefaults = new HashMap<String, Integer>();
    protected HashMap<String, String>  stringDefaults  = new HashMap<String, String>();

    protected HashMap<String, Boolean> booleanSettings = new HashMap<String, Boolean>();
    protected HashMap<String, Integer> integerSettings = new HashMap<String, Integer>();
    protected HashMap<String, String>  stringSettings  = new HashMap<String, String>();

    protected HashMap<String, Boolean> booleanCache = new HashMap<String, Boolean>();
    protected HashMap<String, Integer> integerCache = new HashMap<String, Integer>();
    protected HashMap<String, String>  stringCache  = new HashMap<String, String>();

    protected void propertyBoolean(String key, boolean defaultValue) {
        propertyTypes.put(key, TYPE_BOOLEAN);
        booleanDefaults.put(key, defaultValue);
        booleanSettings.put(key, defaultValue);
    }

    protected void propertyInteger(String key, int defaultValue) {
        propertyTypes.put(key, TYPE_INTEGER);
        integerDefaults.put(key, defaultValue);
        integerSettings.put(key, defaultValue);
    }

    protected void propertyString(String key, String defaultValue) {
        propertyTypes.put(key, TYPE_STRING);
        stringDefaults.put(key, defaultValue);
        stringSettings.put(key, defaultValue);
    }

    public void changePreference(SharedPreferences prefs, String key) {
        readPreference(prefs, key);
    }

    // override this to affect and constructors, be sure to call super.
    public void updatePreference(SharedPreferences prefs, String key) {
        int type = propertyTypes.get(key);
        switch (type) {
            case TYPE_BOOLEAN:
                booleanCache.put(key, booleanSettings.get(key));
                break;
            case TYPE_INTEGER:
                integerCache.put(key, integerSettings.get(key));
                break;
            case TYPE_STRING:
                stringCache.put(key, stringSettings.get(key));
                break;
        }
    }

    protected void readPreference(SharedPreferences prefs, String key) {
        if (! propertyTypes.containsKey(key)) {
            Log.d("log", "ERROR: propertyTypes does not contain key: " + key);
            return;
        }
        int type = propertyTypes.get(key);
        switch (type) {
            case TYPE_BOOLEAN:
                //Log.d("AbstractSettings", "key: " + key + " value: " + prefs.getBoolean(key, false));
                booleanSettings.put(key, prefs.getBoolean(key, getBoolean(key)));
                break;
            case TYPE_INTEGER:
                // TODO: Why can't we use getInteger?
                //Log.d("AbstractSettings", "key: " + key + " value: " + Integer.parseInt(prefs.getString(key, "0")));
                integerSettings.put(key, Integer.parseInt(prefs.getString(key, "" + getInteger(key))));
                break;
            case TYPE_STRING:
                stringSettings.put(key, prefs.getString(key, getString(key)));
                break;
        }
        updatePreference(prefs, key);
    }

    public void readPreferences(SharedPreferences prefs) {
        for (String key : keySet()) {
            readPreference(prefs, key);
        }
    }
    /**
     * Read clock preferences given the context.
     */
    public void readPreferences(Context context) {
        SharedPreferences prefs = getPreferences(context);
        readPreferences(prefs);
    }

    /**
     * Read pattern preferences.
     *
     * @param patternServiceName        pattern's service name
     * @param context                   context
     */
    public void readPreferences(String patternServiceName, Context context) {
        String prefName = getPrefName(patternServiceName);
        //Log.d("readPreferences", "! pref name: " + prefName);
        SharedPreferences prefs = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        readPreferences(prefs);
    }

    /**
     * Get shared preferences.
     *
     * @return      SharedPreferences
     */
    public SharedPreferences getPreferences(Context context) {
        SharedPreferences prefs;
        //try {
        //    Context prefContext = context.createPackageContext("tabcomputing.wallpaper", Context.MODE_PRIVATE);
        //    prefs = prefContext.getSharedPreferences(getDefaultSharedPreferencesName(prefContext), Activity.MODE_PRIVATE);
        //} catch (Exception e) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context); //AbstractWallpaper.this);
        //}
        return prefs;
    }

    public boolean getBoolean(String key) {
        if (booleanSettings.containsKey(key)) {
            return booleanSettings.get(key);
        }
        if (booleanDefaults.containsKey(key)) {
            return booleanDefaults.get(key);
        }
        return false;
    }

    public int getInteger(String key) {
        if (integerSettings.containsKey(key)) {
            return integerSettings.get(key);
        }
        if (integerDefaults.containsKey(key)) {
            return integerDefaults.get(key);
        }
        return 0;
    }

    public String getString(String key) {
        if (stringSettings.containsKey(key)) {
            return stringSettings.get(key);
        }
        if (stringDefaults.containsKey(key)) {
            return stringDefaults.get(key);
        }
        return "";
    }

    public boolean setBoolean(String key, boolean val) {
        return booleanSettings.put(key, val);
    }

    public int setInteger(String key, int val) {
        return integerSettings.put(key, val);
    }

    public String setString(String key, String val) {
        return stringSettings.put(key, val);
    }

    /**
     * Determine if a setting has changed by comparing current setting to cache.
     *
     * @param key   preference key
     * @return      true or false
     */
    public boolean isChanged(String key) {
        int type = propertyTypes.get(key);
        switch (type) {
            case TYPE_BOOLEAN:
                return (! booleanSettings.get(key).equals(booleanCache.get(key)));
            case TYPE_INTEGER:
                return (! integerSettings.get(key).equals(integerCache.get(key)));
            case TYPE_STRING:
                return (! stringSettings.get(key).equals(stringCache.get(key)));
            default:
                return false;
        }
    }

    /**
     * Return the type of a given key.
     *
     * @param key       key name
     * @return          key type
     */
    public int getType(String key) {
        return propertyTypes.get(key);
    }

    /**
     * Return the set of property keys.
     *
     * @return      set of keys
     */
    public Set<String> keySet() {
        return propertyTypes.keySet();
    }

    /**
     * Store the AssetManager instance.
     */
    protected AssetManager assets;

    /**
     * Unfortunately to have a typeface setting, this class needs a reference to an AssetManager,
     * which it can only get from the current activity.
     *
     * TODO: Maybe the settings should just return the typeface id and let the pattern class worry about the actual Typeface instance.
     *
     * @param assets        instance of AssetManager
     */
    public void setAssets(AssetManager assets) {
        this.assets = assets;
    }

    /**
     * Get a unique name to use for storing preferences in the file system.
     *
     * This is a bit of a hack, but it works because each pattern is in a sub-package.
     * So each subclass of AbstractSettings has a name like: "tabcomputing.tcwallpaper.binary.Settings".
     * We take the second to last point section, e.g. "binary", and use that to return a unique name.
     *
     * @return      name
     */
    public String getPrefName() {
        String[] parts = getClass().getName().split("[.]");
        String name = parts[parts.length - 2];
        return name + "_preferences";
    }

    /**
     * Get a unique name to use for storing preferences in the file system.
     *
     * @return      name
     */
    public String getPrefName(String serviceName) {
        String[] serviceNameParts = serviceName.split("[.]");
        String name = serviceNameParts[serviceNameParts.length - 2];
        return name + "_preferences";
    }

    /**
     * Rate of wallpaper refresh, by default one every second. Override if it needs
     * to be different.
     *
     * TODO: Technically this should be adjusted to fit the time system.
     *
     * @return      milliseconds
     */
    public int getFramerate() {
        return 1000;
    }

    /**
     *
     * @return
     */
    public boolean sharedSettings() {
        return !getBoolean("customSettings");
    }

    /**
     *
     * @param context       application context
     */
    public void save(Context context) {
        for (String key : keySet()) {
            if (isChanged(key)) {
                saveSetting(context, key);
            }
        }
    }

    /**
     * TODO: update cache
     *
     * @param context       application context
     * @param key           key to save
     */
    public void saveSetting(Context context, String key) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        int type = propertyTypes.get(key);
        switch (type) {
            case TYPE_BOOLEAN:
                //return (! booleanSettings.get(key).equals(booleanCache.get(key)));
                editor.putBoolean(key, booleanSettings.get(key));
                booleanCache.remove(key);
                break;
            case TYPE_INTEGER:
                //return (! integerSettings.get(key).equals(integerCache.get(key)));
                Log.d("saveSetting", "KEY: " + key);
                editor.putInt(key, integerSettings.get(key));
                integerCache.remove(key);
                break;
            case TYPE_STRING:
                //return (! stringSettings.get(key).equals(stringCache.get(key)));
                editor.putString(key, stringSettings.get(key));
                stringCache.remove(key);
                break;
        }
        editor.apply();
    }

}
