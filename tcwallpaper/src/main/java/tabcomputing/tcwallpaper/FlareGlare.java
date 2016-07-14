package tabcomputing.tcwallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 */
public class FlareGlare extends AbstractFlare {

    public FlareGlare(CommonSettings settings) {
        setSettings(settings);
    }

    @Override
    public void draw(Canvas canvas) {
        float x, y;

        int m = 5; // change to day number or week + 2?

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        int hc = hoursOnClock();

        float w = faceRadius(canvas); // * 2f;
        float d = w;

        double[] t = timeSystem.handRatios();

        double r = t[0];

        Paint paint = new Paint();
        paint.setShadowLayer(3.0f, 0f, 0f, Color.WHITE);

        for (int i = 1; i < m; i++) {
            //r = ((double) i) / hc;
            int c = color(r / i, 0.5);  // TODO: daylight?

            paint.setColor(c); //colors[i]);
            paint.setAlpha(196);

            d = (1.5f*w) / i;

            x = (float) (cx + d * sin(r + rot()));
            y = (float) (cy - d * cos(r + rot()));

            canvas.drawCircle(x, y, w/16, paint);

            x = (float) (cx + d * sin(r + rot() + 0.5));
            y = (float) (cy - d * cos(r + rot() + 0.5));

            canvas.drawCircle(x, y, d/3.5f, paint);
        }
    }

    //@Override
    //protected float centerY(Canvas canvas) {
    //    return canvas.getHeight() / 2;
    //}

}
