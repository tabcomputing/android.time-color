package tabcomputing.wallpaper.stripes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.Arrays;

import tabcomputing.library.paper.AbstractPattern;

/**
 * TODO: Draw series of stripes.
 *
 * Split the screen along a line the runs tangent to an (invisible) clock face.
 */
public class Pattern extends AbstractPattern {

    private Settings settings;

    public Pattern(Settings settings) {
        this.settings = settings;
        setSettings(settings);
    }

    @Override
    public void draw(Canvas canvas) {
        if (settings.getOrientation() == 0) {
            drawHorizontal(canvas);
        } else {
            drawVertical(canvas);
        }
    }

    public void drawVertical(Canvas canvas) {
        //float cx = centerX(canvas);
        //float cy = centerY(canvas);

        float w = canvas.getWidth();
        //float h = canvas.getHeight();

        int[] colors = timeColors();

        if (! settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

        float g = (w / colors.length);

        for(int i=0; i < colors.length; i++) {
            paint.setColor(colors[0]);
            canvas.drawRect(0, g * i, w, g * (i + 1), paint);;
        }

    }

    public void drawHorizontal(Canvas canvas) {

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        int[] colors = timeColors();

        if (! settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

        float g = (h / colors.length);

        for(int i=0; i < colors.length; i++) {
            paint.setColor(colors[0]);
            canvas.drawRect(g * i, 0, g * (i + 1), h, paint);;
        }

    }

    // FIXME
    public void drawRotation(Canvas canvas) {
        float m; // = 0;
        float x1, y1, x2, y2;
        Path p;

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        float radius; // = faceRadius(canvas) - (spotRadius(canvas) / 2);

        // FIXME: hand ratios is off by 30 minutes
        //        shouldn't we be using timeRatio -- but then 12hr vs 24hr clock?
        double[] r = timeSystem.handRatios();

        int[] colors = timeColors();

        if (!settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

        // the hour color is on the outside in normal mode
        //if (settings.isColorReversed()) {
        //    reverseArray(colors);
        //}

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        //TODO
        //paint.setShadowLayer(20.0f, 0.0f, 0.0f, Color.BLACK);

        canvas.drawColor(colors[0]);

        int i;

        float fr = faceRadius(canvas);

        for (int j = 0; j < colors.length; j++) {
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

                /*
                circlePaint.setColor(colors[c[0]]);
                p = new Path();
                p.moveTo(w, 0);
                p.lineTo(0, 0);
                p.lineTo(0, h);
                p.lineTo(x1, y1);
                p.lineTo(x2, y2);
                p.close();
                canvas.drawPath(p, circlePaint);
                */

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

            canvas.drawLine(x1, y1, x2, y2, paint);
        }
    }

}