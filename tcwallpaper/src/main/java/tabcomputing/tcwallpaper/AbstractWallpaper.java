package tabcomputing.tcwallpaper;

import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
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
    protected CommonSettings getSettings() {
        return null;
    }

    //
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

        //private Paint paint = new Paint();
        //private Typeface font = Typeface.SERIF;

        //private TimeSystem timeSystem = new StandardTime();
        //private ColorWheel colorWheel = new ColorWheel();

        private CommonSettings settings;
        private AbstractPattern pattern;

        //private AbstractFlare flare;

        private SettingsMonitor monitor;

        public WallpaperEngine(AbstractPattern pattern, CommonSettings settings) {
            //prefs = PreferenceManager.getDefaultSharedPreferences(AbstractWallpaper.this);
            //prefs.registerOnSharedPreferenceChangeListener(this);

            // this appears the only place we can get assets from the service
            settings.setAssets(getAssets());

            this.pattern = pattern;
            this.settings = settings;

            this.handler = new Handler();

            monitor = new SettingsMonitor(getBaseContext(), settings);
            monitor.add(pattern);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            holder = surfaceHolder;

            //settings.readPreferences(prefs);

            //configure();
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

        /*
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            settings.changePreference(sharedPreferences, key);

            //if (Settings.KEY_PATTERN.equals(key)) {
            //    configurePattern();
            //}

            if (Settings.KEY_DUPLEX.equals(key)) {
                pattern.resetTimeSystem();
            }

            if (Settings.KEY_TIME_TYPE.equals(key)) {
                pattern.resetTimeSystem();
            }

            if (Settings.KEY_BASE.equals(key)) {
                pattern.resetTimeSystem();
                //pattern.reconfigureBaseConversion();
            }

            if (Settings.KEY_DYNAMIC.equals(key)) {
                configureColorWheel();
            }

            if (Settings.KEY_COLOR_GAMUT.equals(key)) {
                configureColorWheel();
            }

            if (Settings.KEY_TYPE_FACE.equals(key)) {
                configureTypeface();
            }

            // TODO: This is probably a good idea.
            //if (Settings.KEY_ROTATE_TIME.equals(key)) {
            //    configureTimeOffset();
            //}
        }
        */

        /*
        private void configurePattern() {
            pattern.setSettings(settings);
            pattern.setTimeSystem(timeSystem);
            pattern.setColorWheel(colorWheel);
        }

        private void configureTimeSystem() {
            timeSystem = settings.getTimeSystem();

            configureColorWheel();

            pattern.setTimeSystem(timeSystem);
        }

        private void configureBaseConversion() {
            timeSystem.setBaseConverted(settings.baseConvert());
        }

        private void configureColorWheel() {
            colorWheel.setColorGamut(settings.colorGamut());

            //double offset = (double) timeSystem.gmtOffset() / 24;
            double offset = 0.0;
            colorWheel.setOffset(offset);

            pattern.setColorWheel(colorWheel);
        }
        */

        //private void configureTypeface() {
        //    font = settings.getTypeface(getAssets());
        //}


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
            return settings.getTickTime(true); //settings.displaySeconds());
        }

        private void drawWallpaper() {
            canvas.save();
            drawPattern();
            //drawFlare();
            canvas.restore();
        }

        private void drawPattern() {
            if (settings.isOwned()) {
                pattern.draw(canvas);
            } else {
                // TODO: Just in case it takes a minute to verify ownership, there should be a delay on using this. How to do?
                pattern.drawWithNag(canvas);
            }
        }

        private void drawFlare() {
            /*
            switch (settings.getFlare()) {
                case 1:
                    flare = new FlareGlare();
                    break;
                case 2:
                    flare = new FlareGlare(); // TODO: Sunspot
                    break;
                case 3:
                    flare = new FlareGlare(); // TODO: Moonspot?
                    break;
                default:
                    flare = null;
            }

            if (flare != null) {
                flare.draw(canvas);
            }
            */
        }

    }

}
