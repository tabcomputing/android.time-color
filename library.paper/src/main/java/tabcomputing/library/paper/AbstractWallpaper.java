package tabcomputing.library.paper;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

public abstract class AbstractWallpaper extends WallpaperService {

    @Override
    public WallpaperService.Engine onCreateEngine() {
        return new WallpaperEngine(getPattern(), getSettings());
    }

    // must be overridden
    protected AbstractPattern getPattern() {
        return null;
    }

    // must be overridden
    protected AbstractSettings getSettings() {
        return null;
    }

    /**
     * @deprecated
     *
     * @param key   settings key
     */
    protected void onPreferenceChange(String key) {
        //pattern.preferenceChanged(key);
    }

    public class WallpaperEngine extends WallpaperService.Engine {  // implements SharedPreferences.OnSharedPreferenceChangeListener {

        //private SharedPreferences prefs;

        private Handler handler;
        private SurfaceHolder holder;
        private boolean visible;

        private Canvas canvas = null;
        //private Rect bounds;

        private AbstractSettings settings;
        private AbstractPattern pattern;

        private SettingsMonitor monitor;

        private BillOfSale billOfSale;


        public WallpaperEngine(AbstractPattern pattern, AbstractSettings settings) {
            //prefs = PreferenceManager.getDefaultSharedPreferences(AbstractWallpaper.this);
            //prefs.registerOnSharedPreferenceChangeListener(this);

            // this appears the only place we can get assets from the service
            settings.setAssets(getAssets());

            this.pattern = pattern;
            this.settings = settings;

            this.handler = new Handler();

            // TODO: Do we need the SettingsMonitor anymore, since there is only one pattern now?
            monitor = new SettingsMonitor(getBaseContext(), settings);
            monitor.add(pattern);
        }

        // TODO: What's the difference between application and base context?

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            holder = surfaceHolder;

            //settings.readPreferences(prefs);

            //configure();
            billOfSale = new BillOfSale(getApplicationContext());
            billOfSale.readBillOfSale();
        }

        private Runnable drawRunner = new Runnable() {
            public void run() {
                draw();
            }
        };

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onDesiredSizeChanged(int desiredWidth, int desiredHeight) {
            super.onDesiredSizeChanged(desiredWidth, desiredHeight);
            Log.d("log", "desiredSizeChanged width: " + desiredWidth + " height: " + desiredHeight);
        }

        /*
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            settings.changePreference(sharedPreferences, key);
        }
        */

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    drawWallpaper();
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, frameDuration());
            }
        }

        /**
         * Returns the time to wait between screen refreshes.
         */
        private long frameDuration() {
            //return settings.getTickTime(true); //settings.displaySeconds());
            return settings.getFramerate();
        }

        private void drawWallpaper() {
            canvas.save();
            drawPattern();
            canvas.restore();
        }

        private void drawPattern() {
            if (isOwned()) {
                pattern.draw(canvas);
            } else {
                pattern.drawWithNag(canvas);
            }
        }

        private boolean isOwned() {
            return billOfSale.isOwned(BillOfSale.PRODUCT_ID);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
