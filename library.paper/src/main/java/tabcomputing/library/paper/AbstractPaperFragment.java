package tabcomputing.library.paper;

import android.app.Fragment;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractPaperFragment extends Fragment {

    // clock settings
    //ClockSettings settings = ClockSettings.getInstance();

    // persist clock view
    AbstractPaperView view;

    //SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.clockarticle_view, container, false);

        view = paperView(); //new ClockView(getActivity().getApplicationContext());
        view.setupTouchListener();

        /*
        // setup touch listener to use left-right swipe to toggle 12/24 hour clock
        view.setOnTouchListener(new View.OnTouchListener() {
            //private float x1, x2;
            //static final int MIN_DISTANCE = 200;

            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    //    x1 = event.getX();
                        return true;
                    case MotionEvent.ACTION_UP:
                        Rect bounds = view.toggleButtonBounds();
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
        */
        return view;
    }

    /**
     * Override this in subclass.
     *
     * @return      paper view
     */
    protected AbstractPaperView paperView() {
        return null;
    }

    /**
     *
     * @return      settings
     */
    protected CommonSettings getSettings() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getActivity().getApplicationContext();

        CommonSettings settings = getSettings();

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
        getSettings().readPreferences(getActivity().getApplicationContext());

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
     * @deprecated
     *
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
            getSettings().readPreferences(serviceName, context);
        //}
    }
     */

}