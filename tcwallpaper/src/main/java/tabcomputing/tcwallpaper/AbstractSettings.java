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

    protected HashMap<String, Integer> propertyTypes = new HashMap<String, Integer>();

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
                booleanSettings.put(key, prefs.getBoolean(key, false));
                break;
            case TYPE_INTEGER:
                // TODO: Why can't we use getInteger?
                integerSettings.put(key, Integer.parseInt(prefs.getString(key, "0")));
                break;
        }
    }

    public void readPreferences(SharedPreferences prefs) {
        for (String key : keySet()) {
            readPreference(prefs, key);
        }
    }

    /**
     * Generic method to get the value of a setting.
     *
     * @param key       setting's key
     * @return          setting's value
     *
    public Object getValue(String key) {
        switch(getType(key)) {
            case TYPE_BOOLEAN:
                return booleanSettings.get(key);
            case TYPE_INTEGER:
                return integerSettings.get(key);
        }
        return null;
    }
    */

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

    public int getType(String key) {
        return propertyTypes.get(key);
    }

    protected Set<String> keySet() {
        return propertyTypes.keySet();
    }


    protected AssetManager assets;

    public void setAssets(AssetManager assets) {
        this.assets = assets;
    }

    /**
     * Get a unique name to use for storing preferences in the file system.
     *
     * @return      name
     */
    public String getPrefName() {
        String cName = getClass().getName();
        String[] cParts = cName.split("[.]");
        return cParts[cParts.length - 2];
    }

}
