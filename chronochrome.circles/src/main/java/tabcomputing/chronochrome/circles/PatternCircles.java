package tabcomputing.chronochrome.circles;

import android.graphics.Canvas;
import android.graphics.Paint;

import tabcomputing.tcwallpaper.BasePattern;

/**
 * Draw series of spiraling square within other spiraling squares.
 */
public class PatternCircles extends BasePattern {

    public PatternCircles(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    @Override
    public void drawPattern(Canvas canvas) {
        float h = canvas.getHeight();
        float w = canvas.getWidth();
        float m = min(w, h);

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        double[] ratios = timeRatios();
        int[] colors    = timeColors();

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        int s = ratios.length;

        canvas.drawColor(colors[0]);

        paint.setColor(colors[1]);
        canvas.drawCircle(cx, cy, m / 2, paint);

        float radius = m / 4;
        float x = cx;
        float y = cy;

        for (int i = 0; i < s; i++) {
            paint.setColor(colors[i]);

            x = (float) (x + radius * sin(ratios[i] + rot()));
            y = (float) (y - radius * cos(ratios[i] + rot()));

            canvas.drawCircle(x, y, radius, paint);

            radius = radius / 2;
        }
    }

    //@Override
    //public float centerY(Canvas canvas) {
    //    return canvas.getHeight() / 2;
    //}

}
