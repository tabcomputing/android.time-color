package tabcomputing.tcwallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//import tabcomputing.library.paper.BroadcastSettingsActivity;

public class AcknowledgeActivity extends Activity {

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
        Intent intent = new Intent(AcknowledgeActivity.this, ClockActivity.class);
        startActivity(intent);

        //ClockView clock = new ClockView(getBaseContext());
        //setContentView(clock);
    }

    public void browseWallpaper(View v) {
        Intent intent = new Intent(AcknowledgeActivity.this, BrowserActivity.class);
        startActivity(intent);
    }

}
