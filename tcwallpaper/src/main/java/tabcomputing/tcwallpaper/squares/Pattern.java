package tabcomputing.tcwallpaper.squares;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import tabcomputing.tcwallpaper.BasePattern;
import tabcomputing.tcwallpaper.ring.*;

/**
 * Draw series of spiraling square within other spiraling squares.
 */
public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    protected Settings settings;

    protected void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    @Override
    public void drawPattern(Canvas canvas) {
        float h = canvas.getHeight();
        float w = canvas.getWidth();

        Path p = new Path();

        int[] cc = timeColors();
        int[] c = new int[cc.length+1];

        for(int i=0; i < cc.length; i++) {
            c[i] = cc[i];
        }
        c[cc.length] = cc[0];

        double[] r = timeRatios();

        paint.reset();
        paint.setAntiAlias(true);
        //paint.setShadowLayer(20.0f, 0f, 0f, Color.BLACK);

        float x, y;

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float radius;

        int s = r.length;

        canvas.drawColor(c[s - 1]);

        radius = (2 * cy) - 20;

        int i;

        for(int j=0; j < (s*2); j++) {
            i = mod(j, s);

            p.reset();
            paint.setColor(c[i]); //colorWheel.alphaColor(cc[i+1], 0.75));

            for (float d = 0; d < 1; d = d + 0.25f) {
                x = (float) (cx + radius * sin(r[i] + rot() + d));
                y = (float) (cy - radius * cos(r[i] + rot() + d));

                // TODO: would like to clip off the corners of each square except d==0
                if (d == 0) {
                    p.moveTo(x, y);
                } else {
                    p.lineTo(x, y);
                }
            }
            p.close();
            canvas.drawPath(p, paint);

            radius = radius * 0.55f;
        }
    }

    // TODO: RENAME TO "concentric" add add option for circles!!!!!!!!!

    protected void drawCircles(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        //int[] ts = timeSegments();
        //int hd = hoursInDay();

        //float w = faceRadius(canvas);
        //float r0 = spotRadius(canvas, hd);
        //float r1 = (w - r0) * 0.975f;

        int[] colors = timeColors();

        //canvas.drawColor(colors[0]);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeWidth(r1 * 2);

        float m = min(cx, cy);

        float s = 6 - settings.getSize();

        float radius = (m / (2 * s));
        float step = (radius * 2);

        paint.setStrokeWidth(step);

        int x = colors.length * 8;

        for (int k = 0; k < x; k++) {
            paint.setColor(colors[mod(k, colors.length)]);
            //paint.setAlpha(150);

            //canvas.drawCircle(cx, cy, q, paint);

            //float q2 = q - (u / 2);
            //drawTicks(canvas, ts[k], q - (u / 2));

            canvas.drawCircle(cx, cy, radius, paint);

            radius = radius + step;
        }

        //paint.setStyle(Paint.Style.FILL);
        //paint.setStrokeWidth(1);
        //drawShape(canvas, u / (2 * d), paint);
        //canvas.drawCircle(cx, cy, u / (2 * d), paint);
    }

}
