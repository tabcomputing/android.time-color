package tabcomputing.tcwallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class BrowserFragment extends Fragment {

    //private final Handler mHideHandler = new Handler();

    BrowseAdapter browserAdapter;

    private View mContentView;
    private GridView gridView;

    /*
    @SuppressLint("InlinedApi")
    private void hideUI() {
        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        //mContentView.setSystemUiVisibility(
        //          View.SYSTEM_UI_FLAG_LOW_PROFILE
        //        | View.SYSTEM_UI_FLAG_FULLSCREEN
        //        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        //        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        mContentView.setSystemUiVisibility(
                  View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                //| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @SuppressLint("InlinedApi")
    private void showUI() {
        //mContentView.setSystemUiVisibility(
        //          View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mContentView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
    */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.browser, container, false);

        //mContentView = view.findViewById(R.id.fullscreen_content);
        gridView = (GridView) view.findViewById(R.id.fullscreen_content);

        //buyButtonBox = view.findViewById(R.id.buy_controls);
        //buyButton    = (TextView) view.findViewById(R.id.buy_button);
        //buyButton.setOnClickListener(buyClickListener);

        browserAdapter = new BrowseAdapter(getActivity().getApplication());  // (this)

        gridView.setAdapter(browserAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                clickWallpaper(position);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_browser);

        //ActionBar actionBar = getSupportActionBar();
        //if (actionBar != null) {
        //    //actionBar.setDisplayHomeAsUpEnabled(true);
        //}


        // Set up the user interaction to manually show or hide the system UI.
        //mContentView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        toggle();
        //    }
        //});

        //mContentView.setOnTouchListener(new View.OnTouchListener() {
        //    @Override
        //    public boolean onTouch(View view, MotionEvent e) {
        //        delayedHide(300); //AUTO_HIDE_DELAY_MILLIS); //toggle();
        //        return false;
        //    }
        //});

        //delayedShow(AUTO_SHOW_DELAY_MILLIS);

        //Log.d("------------>", "BILLING READY: " + billingService.isReady());
    }


    /*
    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };
    */




    /**
     * When a purchase transaction is completed, we need to consume the credits and activate
     * the selected wallpapers. This is called by the BillingService.
     *
    public void onConsumption(String token) {
        //writeBillOfSale();

        Transaction trans = transactions.get(token);

        for(BrowseAdapter.Item item : trans.items) {
            item.markPurchased();
            enableWallpaperService(item.name);
        }

        refreshUI();

        alert("Thank you for your purchase. Enjoy!");
    }
    */


    /**
     * Redraw the context view.
     *
    private void refreshUI() {
        gridView.invalidate();
    }
    */

    /**
     * Flash message.
     *
     * @param msg       message to give to user
     */
    private void alert(String msg) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Enable the wallpaper service so that it will show up in the operating system's
     * Live Wallpaper selector list.
     *
     * @param name      wallpaper service name
     *
    private void enableWallpaperService(String name) {
        String pkgName = getPackageName();
        ComponentName cmpName = new ComponentName(pkgName, pkgName + "." + name + ".Wallpaper");
        getPackageManager().setComponentEnabledSetting(cmpName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
     */

    /*
    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }
    */

    /*
    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        hideUI();
        // Schedule a runnable to remove the status and navigation bar after a delay
        //mHideHandler.removeCallbacks(mShowPart2Runnable);
        //mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        showUI();
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }
    */

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     *
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);

        delayedShow(AUTO_SHOW_DELAY_MILLIS);
    }
    */

    /**
     * Schedules a call to show() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     *
    private void delayedShow(int delayMillis) {
        mHideHandler.removeCallbacks(mShowRunnable);
        mHideHandler.postDelayed(mShowRunnable, delayMillis);
    }
    */


    /*
    // TODO: Do we need to read the preferences and make sure to only write them if their is a change?
    private void writeOwnershipToSettings(String name) {
        String prefName = name + "_preferences";
        SharedPreferences sharedPref = getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("owned", true);
        editor.apply();  //editor.commit();  // Use commit() if it doesn't need to save immediately.
    }
     */


    private void clickWallpaper(int position) {
        BrowseAdapter.Item item = browserAdapter.getItem(position);
        openWallpaper(item);
    }

    // any code will do?
    int REQUEST_SET_LIVE_WALLPAPER = 200;

    // NOTE: Keep in mind that the "packageName" might change.
    private void openWallpaper(BrowseAdapter.Item item) {
        Intent intent;
        String pkg = getActivity().getPackageName();
        String uri = browserAdapter.getPackageName(item.name);
        //ComponentName component = new ComponentName(getPackageName(), uri + "/.Wallpaper");
        ComponentName component = new ComponentName(pkg, uri + ".Wallpaper");
        if(Build.VERSION.SDK_INT > 15) {
            intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, component);
        } else {
            intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        }
        //Log.d("----SELECT---->", " " + component.toString());
        //startActivityForResult(intent, REQUEST_SET_LIVE_WALLPAPER);
        startActivity(intent);
    }



    // track selected patterns
    //private HashSet<BrowseAdapter.Item> selection = new HashSet<>();

    /**
     * When user click on a wallpaper they do not own.
     *
    private void selectWallpaper(BrowseAdapter.Item item) {
        if (item.isSelected()) {
            item.markSelected(false);
            selection.remove(item);
        } else {
            item.markSelected(true);
            selection.add(item);
        }

        int size = selection.size();

        if (size > 0) {
            showBuyButton();
        } else {
            // hide buy button
            hideBuyButton();
        }

        refreshUI();
    }
    */

    /*
    private void showSelectMore() {
        int itemCount = selection.size();

        Button buyButton = (Button)findViewById(R.id.buy_button);
        String price = getPrice(itemCount);

        Resources resources = getApplicationContext().getResources();

        if (price != null && buyButton != null) {
            buyButtonBox.setVisibility(View.VISIBLE);

            String buyPhrase = String.format(resources.getString(R.string.buy_more_phrase), 3 - itemCount, price);
            buyButton.setText(buyPhrase);
        }
    }
    */

}