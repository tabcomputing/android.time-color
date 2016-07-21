package tabcomputing.tcwallpaper.lotus;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import tabcomputing.tcwallpaper.AbstractPattern;

public class Pattern extends AbstractPattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    @Override
    public void draw(Canvas canvas) {
        float cx = centerX(canvas);
        //float cy = centerY(canvas);
        float cy = canvas.getHeight() / 2;

        float x,y;

        int hc = hoursOnClock();

        double[] hr = timeSystem.handRatios();

        int s = hr.length;

        if (!settings.displaySeconds()) {
            s = s - 1;
        }

        int[] hcolors = clockColors();

        int[] colors = timeColors();

        int color;

        float l = cy;
        float d = cy;

        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int i, k;
        double r = 0;

        int h = timeSystem.time()[0];

        for (i = 0; i < hc; i++) {
            k = mod(h + i + 1, hc);
            r = (double) k / hc;

            x = (float) (cx + l * sin(r + rot() + (1.0 / hc)));
            y = (float) (cy - l * cos(r + rot() + (1.0 / hc)));

            paint.setColor(hcolors[k]);
            paint.setAlpha(50);   // (i+1) / hc
            canvas.drawCircle(x, y, d, paint);
        }

        float w = cx / s;
        float radius;

        // TODO: colors seem like they are off by a slight angle
        for (i = 0; i < s; i++) {
            radius = w * (i + 1);

            r = (double) ((int) (hr[i] * hc)) / hc;
            x = (float) ((cx + radius) * sin(r + rot()));
            y = (float) ((cy - radius) * cos(r + rot()));

            paint.setColor(colors[i]);
            paint.setAlpha(255);
            canvas.drawCircle(x, y, d / 16, paint);
        }
    }

}
