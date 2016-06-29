package tabcomputing.wallpaper.mondrian;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import tabcomputing.library.paper.AbstractPattern;

import java.util.Arrays;

public class Pattern extends AbstractPattern {

    public Pattern(Settings settings) {
        setSettings(settings);
    }

    @Override
    public void draw(Canvas canvas) {
        float m; // = 0;
        float x1, y1, x2, y2;
        Path p;

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        float fr = faceRadius(canvas);

        float radius; //= faceRadius(canvas) - (spotRadius(canvas) / 2);

        //int hc = hoursOnClock();

        // the hour color is on the outside in normal mode
        //if (settings.isColorReversed()) {
        //    reverseArray(colors);
        //}

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        //TODO
        //paint.setShadowLayer(20.0f, 0.0f, 0.0f, Color.BLACK);

        canvas.drawColor(Color.WHITE);

        float ratio;
        int i;
        int[] colors;

        double[] r = timeSystem.handRatios();

        colors = timeColors();

        if (!settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

        for(int j=0; j < colors.length; j++) {
            i = colors.length - j - 1;

            radius = (fr / colors.length) * (j + 0.1f);

            float x = (float) (cx + radius * sin(r[i] + rot()));
            float y = (float) (cy - radius * cos(r[i] + rot()));

            if (y - cy == 0) {
                y1 = 0.0f;
                x1 = x;

                y2 = canvas.getHeight();
                x2 = x;
            } else {
                m = -((x - cx) / (y - cy));

                x1 = 0.0f;
                y1 = y - (m * (x - x1));

                x2 = w;
                y2 = y - (m * (x - x2));
            }

            //int[] c = {0, 1};

            // this prevents the colors from switching side when m = INFINITY
            //if (y - cy < 0) {
            //    reverseArray(c);
            //}

            paint.setColor(colors[i]);

            p = new Path();
            p.moveTo(w, 0);

            // this prevents the colors from switching side when m = INFINITY
            if (y - cy < 0) {
                p.lineTo(0, 0);
            } else {
                p.lineTo(w, h);
            }
            p.lineTo(0, h);
            p.lineTo(x1, y1);
            p.lineTo(x2, y2);
            p.close();
            canvas.drawPath(p, paint);

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(25.0f);
            canvas.drawLine(x1, y1, x2, y2, paint);
        }

    }

}