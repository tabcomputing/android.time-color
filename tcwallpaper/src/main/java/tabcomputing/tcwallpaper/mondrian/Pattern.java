package tabcomputing.tcwallpaper.mondrian;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import tabcomputing.tcwallpaper.BasePattern;

public class Pattern extends BasePattern {

    private Path path = new Path();

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    /**
     * TODO: need to draw the lines slightly outside the borders.
     *
     * @param canvas    canvas instance
     */
    @Override
    public void drawPattern(Canvas canvas) {
        float m; // = 0;
        float x1, y1, x2, y2;

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

        paint.reset();  //Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        //TODO
        //paint.setShadowLayer(20.0f, 0.0f, 0.0f, Color.BLACK);

        canvas.drawColor(Color.WHITE);

        //float ratio;
        int i;

        double[] hr = handRatios();
        int[] colors = timeColors();

        for(int j=0; j < colors.length; j++) {
            i = colors.length - j - 1;

            radius = (fr / colors.length) * (j + 0.1f);

            float x = (float) (cx + radius * sin(hr[i] + rot()));
            float y = (float) (cy - radius * cos(hr[i] + rot()));

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

            path.reset();
            path.moveTo(w, 0);
            // this prevents the colors from switching side when m = INFINITY
            if (y - cy < 0) {
                path.lineTo(0, 0);
            } else {
                path.lineTo(w, h);
            }
            path.lineTo(0, h);
            path.lineTo(x1, y1);
            path.lineTo(x2, y2);
            path.close();
            canvas.drawPath(path, paint);

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(25.0f);
            canvas.drawLine(x1, y1, x2, y2, paint);
        }

    }

}