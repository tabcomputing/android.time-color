package tabcomputing.tcwallpaper;

import android.app.Fragment;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class ClockFragment extends Fragment {

    // clock settings
    ClockSettings settings = ClockSettings.getInstance();

    // persist clock view
    ClockView clock;

    //SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.clockarticle_view, container, false);

        // clock view
        clock = new ClockView(getActivity().getApplicationContext());

        // setup touch listener to use left-right swipe to toggle 12/24 hour clock
        clock.setOnTouchListener(new View.OnTouchListener() {
            //private float x1, x2;
            //static final int MIN_DISTANCE = 200;

            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    //    x1 = event.getX();
                        return true;
                    case MotionEvent.ACTION_UP:
                        Rect bounds = clock.toggleButtonBounds();
                        float x = event.getX();
                        float y = event.getY();
                        //Log.d("ClockFragment", "x: " + x + " y: " + y);
                        //Log.d("ClockFragment", "bounds: " + bounds.left + " " + bounds.right + " " + bounds.top + " " + bounds.bottom);
                        if (bounds.contains((int) x, (int) y)) {
                            toggle12and24();
                        }
                        return true;
                }
                return false;
            }
        });

        // return clock view
        return clock;
    }

    /**
     * Toggle clock between 12/24 hour representations.
     */
    protected void toggle12and24() {
        clock.toggle12and24();
        //settings.toggleAMPM();
        //clock.invalidate();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getActivity().getApplicationContext();

        settings.setAssets(context.getAssets());

        // register listener on changed preference
        //prefs = getSharedPrefs();
        //prefs.registerOnSharedPreferenceChangeListener(this);

        // read settings
        settings.readPreferences(context);

        //readPatternSettings();

        // clock view
        //ClockView clock = new ClockView(getApplicationContext());
        //setContentView(clock);

        //ActionBar actionBar = getSupportActionBar();
        //if (actionBar != null) {
        //    actionBar.setDisplayHomeAsUpEnabled(true);
        //}
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_clock_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }
    */

    /*
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
    */

    /*
    public void showSettings() {
        Intent intent = new Intent(ClockActivity.this, ClockSettingsActivity.class); //BroadcastSettingsActivity.class);
        startActivity(intent);
    }
    */

    //@Override
    //public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    //    Log.d("CLOCK", "onSharedPreferenceChanged: " + key);
    //    settings.changePreference(sharedPreferences, key);
    //}

    @Override
    public void onResume() {
        super.onResume();

        //getSharedPrefs().registerOnSharedPreferenceChangeListener(this);
        //prefs.registerOnSharedPreferenceChangeListener(this);

        // read settings
        settings.readPreferences(getActivity().getApplicationContext());

        //readPatternSettings();
    }

    //@Override
    //protected void onPause() {
    //    super.onPause();
    //    //getSharedPrefs().unregisterOnSharedPreferenceChangeListener(this);
    //    //prefs.unregisterOnSharedPreferenceChangeListener(this);
    //}

    //@Override
    //protected void onDestroy() {
    //    super.onDestroy();
    //    //prefs.unregisterOnSharedPreferenceChangeListener(this);
    //}

    private SharedPreferences getSharedPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    }

    /**
     *
     */
    private void readPatternSettings() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getActivity().getApplicationContext());
        WallpaperInfo info = wallpaperManager.getWallpaperInfo();

        if (info == null) { return; }

        String serviceName = info.getServiceName();

        if (! serviceName.contains("tabcomputing.tcwallpaper")) {
            return;
        }

        //if (settings.usePatternSettings()) {
            Context context = getActivity().getApplicationContext();
            settings.readPreferences(serviceName, context);
        //}
    }
}