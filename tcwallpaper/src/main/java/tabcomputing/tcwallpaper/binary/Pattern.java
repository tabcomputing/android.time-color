package tabcomputing.tcwallpaper.binary;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import tabcomputing.library.paper.AbstractPattern;

public class Pattern extends AbstractPattern {

    public Pattern(Settings settings) {
        setSettings(settings);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(minuteColor());
        paint.setAntiAlias(true);
        //paint.setStrokeWidth(10.0f);
        //paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        //paint.setShadowLayer(5.0f, 1.0f, 1.0f, Color.LTGRAY);

        float wx = canvas.getWidth();
        float wy = canvas.getHeight();

        int[] t = timeSystem.time();
        int[] s = timeSystem.timeSegments();

        int e = t.length;
        if (! settings.displaySeconds()) {
            e = e - 1;
        }

        float x;

        int[] colors = timeColors();

        float w = wx / e;

        canvas.drawColor(Color.WHITE);

        int b, f;
        float d, g;

        for(int i=0; i < e; i++) {
            b = log2(s[i]) + 1;

            d = w / b;
            g = d / 2;

            x = (w * i) + g;

            paint.setColor(colorWheel.alphaColor(colors[i], 0.6));

            canvas.drawRect(w * i, 0, (w * i) + w, wy, paint);

            paint.setStrokeWidth(d);
            paint.setColor(colors[i]);

            for (int j=0; j < b; j++) {
                f = (int) Math.pow(2, b - j - 1);
                if ((t[i] & f) == f) {
                    canvas.drawLine(x + (d * j), 0, x + (d * j), wy, paint);
                }
            }
        }

    }

}
