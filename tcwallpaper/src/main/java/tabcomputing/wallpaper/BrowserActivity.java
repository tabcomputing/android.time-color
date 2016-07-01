package tabcomputing.tcwallpaper;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import tabcomputing.library.paper.BroadcastSettingsActivity;

/**
 * Browse wallpapers.
 */
public class BrowserActivity extends AppCompatActivity {

    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhYt78mTecdGIo2yy/38446QR9ASKufTdM9mmTmcjsL3aNUBoo2pJSd5f1A2r+HWHMKYJZmFhMMT2qh/ga6debjXWu6zGfypCpxyKv3xEvuB0gwPi7w61pYmdDqt5l8x6/j/Qm6Q8h3xY5peYmGeEqdCi6vqFVbnRPeiigTE4K//VQ/TUEvWodcDI/0ScRutsTfuvcyPhB3H2PnxCQArjI8aydUsyvTFlt2Qec7q+RJqjPVTR2sR9nINKlIk6lRk9TwOl3NxkN4zZVWB4lQSASWiIZ+B7b4e8UywqUAGlNbm2qYlLMKAycs1uTNPnMVHqZSy2nN3VFLP709KOTIhrQQIDAQAB";

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private final Handler mHideHandler = new Handler();

    private View mContentView;

    private BillingService billingService;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            //hideUI();
        }
    };

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

    private View mControlsView;

    /*
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                //actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    */

    private boolean mVisible;

    /*
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            show();
        }
    };
    */

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     *
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    */

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     *
    private final View.OnTouchListener mDelayShowTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                show(); //delayedShow(300);
            }
            return false;
        }
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browser);

        //ActionBar actionBar = getSupportActionBar();
        //if (actionBar != null) {
        //    //actionBar.setDisplayHomeAsUpEnabled(true);
        //}

        mVisible = true;

        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        mControlsView.setVisibility(View.GONE);




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

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        browserAdapter = new BrowseAdapter(this);

        // wallpaper images
        GridView gridview = (GridView) mContentView; //findViewById(R.id.fullscreen_content);
        gridview.setAdapter(browserAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                clickWapaper(position);
                //Toast.makeText(BrowserActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        // billing service
        //billingService = new BillingService(getApplicationContext());
        billingService = new BillingService(this, updateRunner);

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

    BrowseAdapter browserAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();

        billingService.unbindService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        hideUI();

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //hide(); //delayedHide(100);
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
            showClock();
        }

        return super.onOptionsItemSelected(item);
    }

    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);
    //}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode     = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData  = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    String dp  = jo.getString("developerPayload");
                    if (payloads.contains(dp)) {
                        payloads.remove(dp);
                        browserAdapter.markPurchased(sku);
                        enableWallpaperService(sku);  // TODO: sku same as name?
                        alert("Thank you for your purchase. Enjoy!");
                    } else {
                        alert("Purchase transaction returned invalid verification code.");
                    }
                }
                catch (JSONException e) {
                    alert("Failed to parse purchase data.");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Flash message.
     *
     * @param msg       message to give to user
     */
    private void alert(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Enable the wallpaper service so that it will show up in the operating system's
     * Live Wallpaper selector list.
     *
     * @param name      wallpaper service name
     */
    private void enableWallpaperService(String name) {
        String pkgName = getPackageName();
        ComponentName cmpName = new ComponentName(pkgName, pkgName + "." + name + ".Wallpaper");
        getPackageManager().setComponentEnabledSetting(cmpName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

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

    Runnable updateRunner = new Runnable() {
        public void run() {
            updateAvailableProducts(billingService.availableProducts);
            updatePurchasedProducts(billingService.purchasedProducts);
        }
    };

    public void updateAvailableProducts(List<BillingService.AvailableProduct> products) {
        Log.d("BrowserActivity", "updateAvailableProducts");
        for(BillingService.AvailableProduct product : products) {
            browserAdapter.markPrice(product.sku, product.price);
        }
    }

    public void updatePurchasedProducts(List<BillingService.PurchasedProduct> products) {
        Log.d("BrowserActivity", "updatePurchasedProducts");
        for(BillingService.PurchasedProduct product : products) {
            browserAdapter.markPurchased(product.sku);
            enableWallpaperService(product.sku);  // TODO: sku same as name?
        }
    }

    // TODO: Show setting of current wallpaper if a T+C=W Wallpaper is active
    public void showSettings() {
        Intent intent = new Intent(BrowserActivity.this, ClockSettingsActivity.class);
        startActivity(intent);
    }

    public void showClock() {
        Intent intent = new Intent(BrowserActivity.this, ClockActivity.class);
        startActivity(intent);

        //ClockView clock = new ClockView(getBaseContext());
        //setContentView(clock);
    }

    // any code will do?
    int REQUEST_SET_LIVE_WALLPAPER = 200;

    private void clickWapaper(int position) {
        String sku = browserAdapter.getSku(position);
        //Log.d("---------->", uri + " " + browserAdapter.isInstalled(uri));
        if (browserAdapter.isPurchased(sku)) {
            selectWallpaper(sku);
        } else {
            installWallpaper(sku);
        }
    }

    // FIXME
    private void selectWallpaper(String sku) {
        Intent intent;
        String uri = browserAdapter.getPackageName(sku);
        //ComponentName component = new ComponentName(getPackageName(), uri + "/.Wallpaper");
        ComponentName component = new ComponentName(uri, uri + ".Wallpaper");
        if(Build.VERSION.SDK_INT > 15) {
            intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, component);
        } else {
            intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        }
        //Log.d("----SELECT---->", " " + component.toString());
        startActivityForResult(intent, REQUEST_SET_LIVE_WALLPAPER); //startActivity(intent);
    }

    private void installWallpaper(String sku) {
        String devPayload = billingService.buyProduct(sku, this);
        if (devPayload == null) {
            alert("");
        } else {
            payloads.add(devPayload);
        }
    }

    // store current payloads
    Set<String> payloads = new HashSet<>();

}