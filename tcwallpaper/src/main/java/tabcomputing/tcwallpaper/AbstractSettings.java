package tabcomputing.tcwallpaper;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Set;

public abstract class AbstractSettings {

    public AbstractSettings() {}

    public static final int TYPE_BOOLEAN = 0;
    public static final int TYPE_INTEGER = 1;

    protected HashMap<String, Integer> propertyTypes = new HashMap<>();

    protected HashMap<String, Boolean> booleanDefaults = new HashMap<String, Boolean>();
    protected HashMap<String, Integer> integerDefaults = new HashMap<String, Integer>();

    protected HashMap<String, Boolean> booleanSettings = new HashMap<String, Boolean>();
    protected HashMap<String, Integer> integerSettings = new HashMap<String, Integer>();

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

    public void changePreference(SharedPreferences prefs, String key) {
        readPreference(prefs, key);
    }

    protected void readPreference(SharedPreferences prefs, String key) {
        int type = propertyTypes.get(key);

        switch (type) {
            case TYPE_BOOLEAN:
                //Log.d("AbstractSettings", "key: " + key + " value: " + prefs.getBoolean(key, false));
                booleanSettings.put(key, prefs.getBoolean(key, false));
                break;
            case TYPE_INTEGER:
                // TODO: Why can't we use getInteger?
                //Log.d("AbstractSettings", "key: " + key + " value: " + Integer.parseInt(prefs.getString(key, "0")));
                integerSettings.put(key, Integer.parseInt(prefs.getString(key, "0")));
                break;
        }
    }

    public void readPreferences(SharedPreferences prefs) {
        for (String key : keySet()) {
            readPreference(prefs, key);
        }
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

    public boolean setBoolean(String key, boolean val) {
        return booleanSettings.put(key, val);
    }

    public int setInteger(String key, int val) {
        return integerSettings.put(key, val);
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

}
