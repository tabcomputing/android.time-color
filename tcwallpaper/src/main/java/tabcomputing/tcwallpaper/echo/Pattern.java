package tabcomputing.tcwallpaper.echo;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Arrays;

import tabcomputing.library.paper.AbstractPattern;

/**
 */
public class Pattern extends AbstractPattern {

    public Pattern(Settings settings) {
        setSettings(settings);
    }

    @Override
    public void draw(Canvas canvas) {
    }

    public void drawShit(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        int[] tc = timeColors();

        int[] t = timeSystem.time();

        float y, d;

        canvas.drawColor(tc[0]);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        int i, k = 0;
        double r = 0;

        if (!settings.displaySeconds()) {
            t = Arrays.copyOf(t, t.length - 1);
        }

        //int[] ts = timeSystem.timeSegments();
        int[] cs = timeSystem.clockSegments();

        d = cx;

        int idx;

        if (settings.isSwapped()) {
            idx = 1;
        } else {
            idx = 0;
        }

        for (i = 0; i < cs[idx]; i++) {
            k = mod(t[idx] + i + 1, cs[idx]);
            r = (double) k / cs[idx];
            y = ((float) (i+1) / cs[idx]) * cy;

            paint.setColor(color(r));
            canvas.drawCircle(cx, y, d, paint);
            canvas.drawCircle(cx, cy*2 - y, d, paint);
        }
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
