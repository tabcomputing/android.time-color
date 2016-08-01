package tabcomputing.tcwallpaper.lotus;

import android.graphics.Canvas;
import android.graphics.Color;

import tabcomputing.tcwallpaper.BasePattern;

public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    @Override
    public void draw(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float x,y;

        int hc = hoursOnClock();

        int[] clockColors = clockColors();

        canvas.drawColor(Color.WHITE);

        paint.reset();
        paint.setAntiAlias(true);

        int[] t = time();
        int h = t[0];

        double unit = (1.0 / hc);

        float radius = cy;
        int k;
        double r;

        for (int i = 0; i < hc; i++) {
            k = mod(h + i + 1, hc);
            r = (double) k / hc;

            x = (float) (cx + radius * sin(r + rot()));
            y = (float) (cy - radius * cos(r + rot()));

            paint.setColor(clockColors[k]);
            paint.setAlpha(30);
            canvas.drawCircle(x, y, cy, paint);
        }

        for (int i = hc - 1; i > 0; i--) {
            k = mod(h + i + 1, hc);
            r = (double) k / hc;

            x = (float) (cx + radius * sin(r + rot()));
            y = (float) (cy - radius * cos(r + rot()));

            paint.setColor(clockColors[k]);
            paint.setAlpha(30);
            canvas.drawCircle(x, y, cy, paint);
        }

        drawTimeDots(canvas);
    }

    protected void drawTimeDots(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        int[] colors = timeColors();
        int s = timeColors().length;
        double[] hr = handRatios();
        int hc = hoursOnClock();

        float m = min(cx, cy);
        float g = m / 12;
        //float w = m / s;
        float radius = m / 1.2f;

        double r;
        float x, y;

        paint.reset();
        paint.setAntiAlias(true);
        paint.setShadowLayer(3f, 0f, 0f, Color.WHITE);

        for (int i = 0; i < s; i++) {
            //radius = (w * (i + 1)) - g;
            g = m / (9 + (i * 3));

            r = (double) ((int) (hr[i] * hc)) / hc;
            x = (float) (cx + radius * sin(r + rot()));
            y = (float) (cy - radius * cos(r + rot()));

            paint.setColor(colors[i]);
            paint.setAlpha(255);
            canvas.drawCircle(x, y, g, paint);
        }
    }

    @Override
    public float centerY(Canvas canvas) {
        return canvas.getHeight() / 2;
    }

}
