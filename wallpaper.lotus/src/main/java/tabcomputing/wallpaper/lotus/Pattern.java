package tabcomputing.wallpaper.lotus;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import tabcomputing.library.paper.AbstractPattern;
import tabcomputing.library.paper.CommonSettings;

public class Pattern extends AbstractPattern {

    public Pattern(CommonSettings settings) {
        setSettings(settings);
    }

    //public PatternLotus(Settings settings, TimeSystem timeSystem, ColorWheel colorWheel) {
    //    this.settings   = settings;
    //    this.timeSystem = timeSystem;
    //    this.colorWheel = colorWheel;
    //}

    @Override
    public void draw(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

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

        int i, j, k;
        double r = 0;

        int h = timeSystem.time()[0];

        for (i = 0; i < hc; i++) {
            k = mod(h + i + 1, hc);
            r = (double) k / hc;

            x = (float) (cx + l * sin(r + rot()));
            y = (float) (cy - l * cos(r + rot()));

            //color = colorWheel.alphaColor(hcolors[k], (float) (i+1) / hc);
            paint.setColor(hcolors[k]);
            paint.setAlpha(50);
            canvas.drawCircle(x, y, d, paint);
        }

        for (i = 0; i < s; i++) {
            r = (double) ((int) (hr[i] * hc)) / hc;
            x = (float) (cx + (l / 2) * sin(r + rot()));
            y = (float) (cy - (l / 2) * cos(r + rot()));

            paint.setColor(colors[i]);
            canvas.drawCircle(x, y, d / 16, paint);
        }
    }

}
