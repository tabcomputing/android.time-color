package tabcomputing.tcwallpaper;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class AcknowledgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /*
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
    */

}
