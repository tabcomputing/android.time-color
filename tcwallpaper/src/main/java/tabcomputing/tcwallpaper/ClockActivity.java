package tabcomputing.tcwallpaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

//import tabcomputing.library.paper.BroadcastSettingsActivity;

/**
 * Clock Activity for reference.
 */
public class ClockActivity extends AppCompatActivity { //implements SharedPreferences.OnSharedPreferenceChangeListener {

    ClockSettings settings = ClockSettings.getInstance();

    //SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // register listener on changed preference
        //prefs = getSharedPrefs();
        //prefs.registerOnSharedPreferenceChangeListener(this);

        // read settings
        settings.readPreferences(this);

        // clock view
        ClockView clock = new ClockView(getApplicationContext());
        setContentView(clock);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_clock_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else if (id == R.id.action_settings) {
            showSettings();
        } else if (id == R.id.action_clock) {
            //showClock();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showSettings() {
        Intent intent = new Intent(ClockActivity.this, ClockSettingsActivity.class); //BroadcastSettingsActivity.class);
        startActivity(intent);
    }

    //@Override
    //public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    //    Log.d("CLOCK", "onSharedPreferenceChanged: " + key);
    //    settings.changePreference(sharedPreferences, key);
    //}

    @Override
    protected void onResume() {
        super.onResume();
        //getSharedPrefs().registerOnSharedPreferenceChangeListener(this);
        //prefs.registerOnSharedPreferenceChangeListener(this);

        // read settings
        settings.readPreferences(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //getSharedPrefs().unregisterOnSharedPreferenceChangeListener(this);
        //prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    private SharedPreferences getSharedPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

}