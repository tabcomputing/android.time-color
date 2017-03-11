package tabcomputing.library.paper;

//import tabcomputing.library.billing.BillingService;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

//import android.support.v7.app.ActionBarDrawerToggle;

public abstract class AbstractMainActivity extends AppCompatActivity {

    // TODO: Okay what are we going to do with this?
    private static final String BASE64_PUBLIC_KEY = "" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhYt78mTecdGIo2yy" +
            "/38446QR9ASKufTdM9mmTmcjsL3aNUBoo2pJSd5f1A2r+HWHMKYJZmFhMMT2" +
            "qh/ga6debjXWu6zGfypCpxyKv3xEvuB0gwPi7w61pYmdDqt5l8x6/j/Qm6Q8" +
            "h3xY5peYmGeEqdCi6vqFVbnRPeiigTE4K//VQ/TUEvWodcDI/0ScRutsTfuv" +
            "cyPhB3H2PnxCQArjI8aydUsyvTFlt2Qec7q+RJqjPVTR2sR9nINKlIk6lRk9" +
            "TwOl3NxkN4zZVWB4lQSASWiIZ+B7b4e8UywqUAGlNbm2qYlLMKAycs1uTNPn" +
            "MVHqZSy2nN3VFLP709KOTIhrQQIDAQAB";

    private static final String HOMEPAGE = "http://tabcomputing.com/products/time+color/wallpaper/";

    protected DrawerLayout mDrawer;
    //private Toolbar toolbar;
    private NavigationView nvDrawer;
    //private ActionBarDrawerToggle drawerToggle;

    //private BillingService billingService;

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
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        Menu menu = nvDrawer.getMenu();

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            selectDrawerItem(menu.findItem(R.id.nav_wallpaper));
        }

        //setupBillingStuff(menu);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //cancelNag(nagDismiss);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startNagMessage();
    }

    //private void startNagMessage() {
    //    if (! nagDismiss) {
    //        nagHandler.postDelayed(nagRunner, 30000);
    //    }
    //}

    // Show a nag dialog.
    //private Runnable nagRunner = new Runnable() {
    //    @Override
    //    public void run() {
    //        showNagDialog();
    //    }
    //};

    // This is called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        //if (billingService != null) {
        //    billingService.unbindService();
        //}
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

    // TODO: get this from resources
    private final static String PRODUCT_ID = "upgrade";

    /**
     * Create a new fragment and specify the fragment to show based on nav item clicked.
     */
    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        CharSequence title;

        int navId = menuItem.getItemId();

        if(navId == R.id.nav_review) {
            btnRateAppOnClick();
            return;
        //} else if (navId == R.id.nav_upgrade) {
        //    buyProduct(PRODUCT_ID);
        //    return;
        } else if (navId == R.id.nav_website) {
            showWebsite();
            return;
        } else if (navId == R.id.nav_about) {
            fragmentClass = AboutFragment.class;
            title = menuItem.getTitle();
        } else if (navId == R.id.nav_settings) {
            fragmentClass = settingsFragmentClass();
            //fragmentClass = AbstractSettingsFragment.class;
            title = menuItem.getTitle();
        } else if (navId == R.id.nav_clock) {
            fragmentClass = ClockFragment.class;
            title = menuItem.getTitle();
        } else if (navId == R.id.nav_wallpaper) {
            // TODO: this class may need to come from a function
            fragmentClass = paperFragmentClass(); //PaperFragment.class;
            title = menuItem.getTitle();
        } else {
            fragmentClass = paperFragmentClass(); //PaperFragment.class;
            title = menuItem.getTitle();
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setFragment(fragment, navId);

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);

        // Set action bar title
        setTitle(title);

        // Close the navigation drawer
        mDrawer.closeDrawers();

        invalidateOptionsMenu();
    }

    /**
     * Override this is in subclass.
     *
     * @return      fragment class
     */
    protected Class paperFragmentClass() {
        return AbstractPaperFragment.class;
    }

    /**
     * Override this is in subclass.
     *
     * @return      fragment class
     */
    protected Class settingsFragmentClass() {
        return AbstractSettingsFragment.class;
    }

    private void setNavTitle(int navId) {
        MenuItem menuItem = (MenuItem) findViewById(navId);
        if (menuItem != null) {
            setTitle(menuItem.getTitle());
        }
    }

    ArrayList<Integer> navStack = new ArrayList<>();
    private int currentNavId = R.id.nav_wallpaper;

    //private SparseArray<Integer> fragIds = new SparseArray<>();

    /**
     * Insert the fragment by replacing any existing fragment.
     */
    protected void setFragment(Fragment fragment, int resId) {
        navStack.add(currentNavId);
        currentNavId = resId;
        navStack.remove((Integer) resId);

        FragmentManager fragmentManager = getFragmentManager();
        //String fragName = fragment.getClass().getName();
        //fragmentManager.popBackStackImmediate(fragName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        int fragId;
        if (resId == R.id.nav_wallpaper) {
            navStack.clear();
            // is it safe to assume that the frag id is the stack index?
            //fragmentManager.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //while(fragmentManager.getBackStackEntryCount() > 0) { fragmentManager.popBackStackImmediate(); }
            fragId = fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        } else {
            fragId = fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit(); //.addToBackStack(fragName).commit();
        }
        // looks like the fragId is just the stack index, so there is no worry about a mem leak
        //fragIds.put(fragId, resId);
    }

    @Override
    public void onBackPressed() {
        if (navStack.isEmpty()) {
            super.onBackPressed();
            //finish();  // TODO: Do we need this? I think it is handle automatically.
            return;
        }

        currentNavId = navStack.remove(navStack.size() - 1);

        MenuItem menuItem = nvDrawer.getMenu().findItem(currentNavId);
        selectDrawerItem(menuItem);
    }

    /**
     * Get the resource id for the current fragment.
     *
     * NOTE: Google is insane.
     *
     * @return      resource id
     */
    private int getCurrentFragmentId() {
        int id;
        if (currentNavId < 0) {
            id = R.id.nav_wallpaper;
        } else {
            id = currentNavId;
        }
        return id;
    }

    /*
    private int getPreviousFragmentId() {
        FragmentManager fragmentManager = getFragmentManager();
        //Log.d("log", "count: " + fragmentManager.getBackStackEntryCount());
        if (fragmentManager.getBackStackEntryCount() > 0) {
            int top = fragmentManager.getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(top);
            //Log.d("log", "id: " + entry.getId());
            return fragIds.get(entry.getId());
        } else {
            return R.id.nav_wallpaper;
        }
    }
    */

    /**
     * Create options menu.
     *
     * @param menu      Menu instance
     * @return          success ?
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        int id = getCurrentFragmentId();
        //Log.d("log", "fragId: " + id);

        if (id == R.id.nav_clock) {
            inflater.inflate(R.menu.options_clock, menu);
        } else if (id == R.id.nav_settings) {
            inflater.inflate(R.menu.options_settings, menu);
        } else if (id == R.id.nav_about) {
            inflater.inflate(R.menu.options_about, menu);
        } else if (id == R.id.nav_wallpaper) {
            inflater.inflate(R.menu.options_browser, menu);
        } else {
            inflater.inflate(R.menu.options_browser, menu);
        }

        //if (nagDismiss) {
        //    // TODO: Instead of this, just have the icon open a dialog to write a review instead.
        //    inflater.inflate(R.menu.options_clock, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        boolean result = super.onPrepareOptionsMenu(menu); //true;
        //if(isOwned()) {
        //    MenuItem item = menu.findItem(R.id.action_upgrade);
        //    if (item != null) {
        //        item.setTitle(R.string.action_review);
        //    }
        //}
        return result;
    }

    //private Settings settings;
    private static CommonSettings settings = CommonSettings.getInstance();

    /**
     * Actions to perform for action menu options.
     *
     * @param item      instance of MenuItem
     * @return          success ?
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // The action bar home/up action should open or close the drawer.
        if(id == android.R.id.home) {
            // This ID represents the Home or Up button
            //NavUtils.navigateUpFromSameTask(this);
            if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.START);
            } else {
                mDrawer.openDrawer(GravityCompat.START);
            }
            return true;
        } else if (id == R.id.action_review) {
            //if (isOwned()) {
                btnRateAppOnClick();
            //} else {
            //    showNagDialog();
            //}
            return true;
        } else if (id == R.id.action_select) {
            openWallpaper();
            return true;
        } else if (id == R.id.action_clock) {
            showClock();
            return true;
        } else if (id == R.id.action_wallpaper) {
            showWallpapers();
            return true;
        } else if (id == R.id.action_about) {
            showAbout();
            return true;
        } else if (id == R.id.action_settings) {
            showSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showWallpapers() {
        selectDrawerItem(nvDrawer.getMenu().findItem(R.id.nav_wallpaper));
    }

    // TODO: Show setting of current wallpaper if a T+C=W Wallpaper is active
    public void showSettings() {
        selectDrawerItem(nvDrawer.getMenu().findItem(R.id.nav_settings));
        //Intent intent = new Intent(BrowserFragment.this, ClockSettingsActivity.class);
        //startActivity(intent);
    }

    public void showClock() {
        selectDrawerItem(nvDrawer.getMenu().findItem(R.id.nav_clock));
        //Intent intent = new Intent(BrowserFragment.this, ClockActivity.class);
        //startActivity(intent);
        //ClockView clock = new ClockView(getBaseContext());
        //setContentView(clock);
    }

    public void showAbout() {
        selectDrawerItem(nvDrawer.getMenu().findItem(R.id.nav_about));
        //Intent intent = new Intent(BrowserFragment.this, ClockSettingsActivity.class);
        //startActivity(intent);
    }

    public void showWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(HOMEPAGE));
        startActivity(intent);
    }

    // NOTE: Keep in mind that the "packageName" might change.
    private void openWallpaper() {
        Intent intent;
        String pkg = getPackageName();
        String uri = pkg + ".Wallpaper";
        //ComponentName component = new ComponentName(getPackageName(), uri + "/.Wallpaper");
        ComponentName component = new ComponentName(pkg, uri);
        if(Build.VERSION.SDK_INT > 15) {
            intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, component);
        } else {
            intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        }
        //log("selected: " + component.toString());
        ////startActivityForResult(intent, REQUEST_SET_LIVE_WALLPAPER);
        startActivity(intent);
    }

    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);
    //}

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

    /**
     * On click event for rate this app button.
     */
    public void btnRateAppOnClick() {
        String uri = reviewURI();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // try Google play
        intent.setData(Uri.parse("market://details?id=" + uri));
        if (! tryActivity(intent)) {
            // google play app seems not installed, let's try to open a web browser
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + uri));
            if (! tryActivity(intent)) {
                // if this also fails, we have run out of options, inform the user.
                Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected String reviewURI() {
        return "tabcomputing.chronochrome.solid";
    }

    private boolean tryActivity(Intent aIntent) {
        try
        {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }

    /*
    private boolean isOwned() {
        return billingService.isOwned(PRODUCT_ID);
    }
    */

    /*
    static public class NagDialogFragment extends DialogFragment {

        public NagDialogFragment() {}

        private AbstractMainActivity mActivity;

        @Override
        public void onAttach(Activity activity)
        {
            if (activity instanceof AbstractMainActivity)
            {
                mActivity = (AbstractMainActivity) activity;
            }
            super.onAttach(activity);
        }

        @Override
        public void onAttach(Context activity)
        {
            if (activity instanceof AbstractMainActivity)
            {
                mActivity = (AbstractMainActivity) activity;
            }
            super.onAttach(activity);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_nag_message)
                    .setPositiveButton(R.string.dialog_nag_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mActivity.buyProduct(PRODUCT_ID);
                        }
                    })
                    .setNegativeButton(R.string.dialog_nag_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mActivity.cancelNag(true);
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    };
    */

}

