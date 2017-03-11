package tabcomputing.chronochrom.eecho;

import android.graphics.Canvas;
import android.graphics.Paint;

import tabcomputing.tcwallpaper.BasePattern;

/**
 * Echo pattern
 */
public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    private Settings settings;
    public void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    @Override
    public void drawPattern(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        int[] tc = timeColors();
        int[] t  = time();

        float y, d;

        //canvas.drawColor(tc[0]);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        int i, k;
        double r;

        //int[] ts = timeSystem.timeSegments();
        int[] cs = timeSystem.clockSegments();

        d = cx;

        int idx = (settings.isSwapped() ? 1 : 0);
        int segs = cs[idx];  // number of segments, e.g. 24 hours

        for (i = 0; i < segs; i++) {
            int j = i + 1;
            k = mod(t[idx] - j, segs);
            r = (double) k / segs;
            y = cy * ((float) j / segs);

            paint.setColor(color(r));
            canvas.drawCircle(cx, y, d, paint);
            canvas.drawCircle(cx, cy*2 - y, d, paint);
        }

        //paint.setColor(color(t[idx] / segs));
        paint.setColor(tc[idx]);
        canvas.drawCircle(cx, cy, d, paint);

        //drawGradientHands(canvas, d);
    }

    /**
     * Exact center.
     *
     * @return      vertical origin
     */
    @Override
    public float centerY(Canvas canvas) {
        return canvas.getHeight() / 2;
    }

}
