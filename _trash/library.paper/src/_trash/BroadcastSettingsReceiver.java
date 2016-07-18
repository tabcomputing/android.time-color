package tabcomputing.library.paper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
//import android.widget.Toast;

public class BroadcastSettingsReceiver extends BroadcastReceiver {

    //
    private ClockSettings settings = ClockSettings.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Don't panic but your preferences are being read!!!!.",  Toast.LENGTH_LONG).show();

        String key = intent.getStringExtra("key");

        if (key != null) {
            Log.d("-receive----------->", "" + context + " " + key);

            switch (settings.getType(key)) {
                case AbstractSettings.TYPE_BOOLEAN:
                    settings.setBoolean(key, intent.getBooleanExtra("value", false));
                    break;
                case AbstractSettings.TYPE_INTEGER:
                    settings.setInteger(key, intent.getIntExtra("value", 0));
                    break;
            }
        }

        // Vibrate the mobile phone
        //Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        //vibrator.vibrate(2000);
    }

}
