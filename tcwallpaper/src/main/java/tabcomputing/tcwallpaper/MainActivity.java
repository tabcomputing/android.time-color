package tabcomputing.tcwallpaper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
//import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // TODO: Okay what are we going to do with this?
    private static final String BASE64_PUBLIC_KEY = "" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhYt78mTecdGIo2yy" +
            "/38446QR9ASKufTdM9mmTmcjsL3aNUBoo2pJSd5f1A2r+HWHMKYJZmFhMMT2" +
            "qh/ga6debjXWu6zGfypCpxyKv3xEvuB0gwPi7w61pYmdDqt5l8x6/j/Qm6Q8" +
            "h3xY5peYmGeEqdCi6vqFVbnRPeiigTE4K//VQ/TUEvWodcDI/0ScRutsTfuv" +
            "cyPhB3H2PnxCQArjI8aydUsyvTFlt2Qec7q+RJqjPVTR2sR9nINKlIk6lRk9" +
            "TwOl3NxkN4zZVWB4lQSASWiIZ+B7b4e8UywqUAGlNbm2qYlLMKAycs1uTNPn" +
            "MVHqZSy2nN3VFLP709KOTIhrQQIDAQAB";

    private static final String HOMEPAGE = "http://tabcomputing.com/tcwallpaper/";

    private DrawerLayout mDrawer;
    //private Toolbar toolbar;
    private NavigationView nvDrawer;
    //private ActionBarDrawerToggle drawerToggle;

    private BillingService billingService;

    private Handler nagHandler = new Handler();
    private Boolean nagDismiss = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // Set a Toolbar to replace the ActionBar.
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer  = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            selectDrawerItem(nvDrawer.getMenu().findItem(R.id.nav_wallpaper));
        }

        // billing service
        billingService = new BillingService(this, updateRunner);

        if (! billingService.isOwned(PRODUCT_ID)) {
            nagHandler.postDelayed(nagRunner, 15000);
        }

        // TODO: should we do this here? Do we need to run this async and use a callback?
        //if (! billingService.isSupported()) {
        //    cancelNagDialog();
        //}
    }

    @Override
    protected void onPause() {
        super.onPause();
        nagDismiss = true;
        nagHandler.removeCallbacks(nagRunner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nagDismiss) {
            nagDismiss = false;
            if (! billingService.isOwned(PRODUCT_ID)) {
                nagHandler.postDelayed(nagRunner, 15000);
            }
        }
    }

    // Show a nag dialog.
    private Runnable nagRunner = new Runnable() {
        @Override
        public void run() {
            showNagDialog();
            //nagHandler.postDelayed(this, 120000);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:  // this ID represents the Home or Up button.
                //NavUtils.navigateUpFromSameTask(this);
                if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                    mDrawer.closeDrawer(GravityCompat.START);
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }
                return true;
            case R.id.nav_clock:
                showClock();
                return true;
            case R.id.nav_about:
                showAbout();
                return true;
            case R.id.nav_website:
                showWebsite();
                return true;
            //case R.id.nav_settings:
            //    showSettings();
            //    return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // This is called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        billingService.unbindService();
        super.onDestroy();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                }
        );
    }

    private final static String PRODUCT_ID = "freedom";

    /**
     * Create a new fragment and specify the fragment to show based on nav item clicked.
     */
    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;

        switch(menuItem.getItemId()) {
            case R.id.nav_upgrade:
                buyProduct(PRODUCT_ID);
                return;
            case R.id.nav_website:
                showWebsite();
                return;
            case R.id.nav_about:
                fragmentClass = AboutFragment.class;
                break;
            case R.id.nav_settings:
                fragmentClass = ClockSettingsFragment.class;
                break;
            case R.id.nav_clock:
                fragmentClass = ClockFragment.class;
                break;
            case R.id.nav_wallpaper:
                fragmentClass = BrowserFragment.class;
                break;
            default:
                fragmentClass = BrowserFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setFragment(fragment);

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle("T+C=W " + menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    /**
     * Insert the fragment by replacing any existing fragment.
     *
     * TODO: work on back stack, should only ever be one deep?
     */
    private void setFragment(Fragment fragment) {
        //String fragName = fragment.getClass().getName();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(fragName).commit();
    }


    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    MenuInflater inflater = getMenuInflater();
    //    inflater.inflate(R.menu.activity_main_actions, menu);
    //    return super.onCreateOptionsMenu(menu);
    //}


    // TODO: Show setting of current wallpaper if a T+C=W Wallpaper is active
    public void showSettings() {
        //Intent intent = new Intent(BrowserFragment.this, ClockSettingsActivity.class);
        //startActivity(intent);
    }

    public void showClock() {
        //Intent intent = new Intent(BrowserFragment.this, ClockActivity.class);
        //startActivity(intent);

        //ClockView clock = new ClockView(getBaseContext());
        //setContentView(clock);
    }

    public void showAbout() {
        //Intent intent = new Intent(BrowserFragment.this, AcknowledgeActivity.class);
        //startActivity(intent);
    }

    public void showWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(HOMEPAGE));
        startActivity(intent);
    }

    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);
    //}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            billingService.buyResult(requestCode, resultCode, data);
        }
    }

    /**
     * Buy product.
     */
    private void buyProduct(String sku) {
        String devPayload = billingService.buyProduct(sku, this);
    }

    private String getPrice(String sku) {
        return billingService.getPrice(sku);
    }

    // Update bill of sale.
    private Runnable updateRunner = new Runnable() {
        public void run() {
            if (billingService.isOwned(PRODUCT_ID)) {
                cancelNagDialog();
            }
            //refreshUI();
        }
    };

    private void showNagDialog() {
        //DialogFragment dialog = new NagDialogFragment();
        nagDialogFragment.show(getFragmentManager(), "What's this for?");
    }

    private void cancelNagDialog() {
        nagHandler.removeCallbacks(nagRunner);
    }

    private void showBuyButton() {
        //BrowserFragment fragment = (BrowserFragment) getFragmentManager().findFragmentById(R.id.nav_wallpaper);
        //fragment.showBuyButton(getPrice("all"));
        //buyButtonBox.setVisibility(View.VISIBLE);
    }

    private void hideBuyButton() {
        //BrowserFragment fragment = (BrowserFragment) getFragmentManager().findFragmentById(R.id.nav_wallpaper);
        //fragment.hideBuyButton();
    }

    public DialogFragment nagDialogFragment = new DialogFragment() {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_nag_message)
                    .setPositiveButton(R.string.dialog_nag_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            buyProduct(PRODUCT_ID);
                        }
                    })
                    .setNegativeButton(R.string.dialog_nag_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    };


    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //drawClock(findViewById(R.id.main_layout_id));
    }

    public void setWallpaper(View v) {
        Intent intent;
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        //    intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        //    intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, Wallpaper.class));
        //} else {
            intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
            //intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, Wallpaper.class));
        //}
        startActivity(intent);
    }

    //public void showSettings(View v) {
    //    Intent intent = new Intent(MainActivity.this, BroadcastSettingsActivity.class);
    //    startActivity(intent);
    //}

    public void showClock(View v) {
        Intent intent = new Intent(MainActivity.this, ClockActivity.class);
        startActivity(intent);

        //ClockView clock = new ClockView(getBaseContext());
        //setContentView(clock);
    }

    public void browseWallpaper(View v) {
        Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
        startActivity(intent);
    }
    */

    /**
     * Flash message.
     *
     * @param msg       message to give to user
     */
    public void alert(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

}
