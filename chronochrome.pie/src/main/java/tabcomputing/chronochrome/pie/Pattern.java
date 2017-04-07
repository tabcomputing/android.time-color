package tabcomputing.chronochrome.pie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;

import tabcomputing.library.paper.BasePattern;

/**
 * This pattern simply cuts out a slice of the screen aligned with hands on a clock.
 *
 * TODO: Perhaps in a future version we can add an option to make it look like real pie ;-)
 */
public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    public Pattern(Context context, Settings settings) {
        setContext(context);
        setSettings(settings);
        resetPreferences();
    }

    private Settings settings;

    public void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    public void drawPattern(Canvas canvas) {
        if (settings.isSmooth()) {
            drawGradientHands(canvas);
        } else {
            drawSolidSlices(canvas);
        }
    }

    /**
     * TODO: Improve seconds support.
     *
     * @param canvas    canvas instance
     */
    public void drawSolidSlicesX(Canvas canvas) {
        float h = canvas.getHeight();

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        int[] colors = timeColors();
        double[] hr = handRatios();

        int s = hr.length;
        int l = s - 1;

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1.0f);

        canvas.drawColor(colors[0]);

        float d  = cy - cx;

        // This was a pain in the ass to figure out.
        // It has to be square and centered as (cx,cy).
        RectF rect = new RectF(-(d+h), -h, cx+cx+d+h, cy+cy+h);

        double startAngle; // = hr[l];
        double sweepAngle; // = 0.0;

        // TODO: Offer shadow option?
        //if (settings.isShadow()) {
        //    paint.setShadowLayer(20.0f, 0.0f, 0.0f, Color.BLACK);
        //}

        // TODO: Not sure I want to support rotation at all. Instead we might support an AM/PM Mode which will put midnight on top with noon.
        double offset;
        if (settings.isRotated()) {
            offset = -0.25;
        } else {
            offset = 0.25;
        }

        for(int i=0; i < l; i++) {
            paint.setColor(colors[i+1]);

            startAngle = hr[i];
            //sweepAngle = 360 - startAngle + (float) (hr[i] * 360);
            sweepAngle = hr[i+1] - hr[i]; // - startAngle;

            if (sweepAngle < 0) {
                sweepAngle = sweepAngle + 1.0;
            }

            canvas.drawArc(rect, (float) (startAngle + offset) * 360.0f, (float) sweepAngle * 360.0f, true, paint);
        }
    }

    protected void drawSolidSlices(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        // really only needs to be `Math.sqrt(cy * cy + cx * cx)`, but why bother?
        float radius = cy + cx;

        //canvas.drawCircle(cx, cy, radius, paint);

        int[] tc = timeColors();

        //int[] colors = Arrays.copyOf(tc, tc.length + 1) ;
        //colors[tc.length] = colors[0];

        double[] tr = timeRatios();

        float[] pos = new float[tr.length];

        for(int i=0; i < tr.length; i++) {
            pos[i] = (float) tr[i];
        }

        // adjustment so 0 is on bottom
        float offset = 0.25f; //reduce(pos[0] - 0.25f);

        sortBoth(pos, tc);

        //
        //angle = angle - pos[0];

        // make all positions relative to 0
        //for(int i=0; i < pos.length; i++) {
        //    pos[i] = pos[i] - pos[0];
        //}

        //for(int i=0; i < pos.length; i++) {
        //    Log.d("log", "pos " + i + ": " + pos[i] + " c:" + tc[i]);
        //}
        //Log.d("log", " ");

        RectF rectF = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);

        paint.reset();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);

        SweepGradient shader;

        float deg, dif;
        float[] p = new float[2];

        float p0 = 0;
        float p1 = pos[0];

        for(int i=0; i < tc.length; i++) {
            int j = i + 1; if (j == tc.length) { j = 0; }

            int[] cg = { tc[i], tc[j] };

            if (j < i) {
                dif = (1 - pos[i]) + pos[j];
                deg = pos[i]; //reduce(pos[j0] + angle) * 360f;
            } else {
                dif = pos[j] - pos[i];
                deg = pos[i]; //reduce(pos[j0] + angle) * 360f;
            }

            p[0] = 0;
            p[1] = dif;

            //shader = new SweepGradient(cx, cy, cg, p);
            //paint.setShader(shader);

            paint.setColor(tc[i]);

            deg = (deg + offset) * 360f;

            canvas.rotate(deg, cx, cy);
            canvas.drawArc(rectF, p[0], p[1] * 360f, true, paint);
            canvas.rotate(-deg, cx, cy);
        }

        //canvas.drawCircle(cx, cy, radius, paint);
    }



    protected void drawGradientHands(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        // really only needs to be `Math.sqrt(cy * cy + cx * cx)`, but why bother?
        float radius = cy + cx;

        //canvas.drawCircle(cx, cy, radius, paint);

        int[] tc = timeColors();

        //int[] colors = Arrays.copyOf(tc, tc.length + 1) ;
        //colors[tc.length] = colors[0];

        double[] tr = timeRatios();

        float[] pos = new float[tr.length];

        for(int i=0; i < tr.length; i++) {
            pos[i] = (float) tr[i];
        }

        // adjustment so 0 is on bottom
        float offset = 0.25f; //reduce(pos[0] - 0.25f);

        sortBoth(pos, tc);

        //
        //angle = angle - pos[0];

        // make all positions relative to 0
        //for(int i=0; i < pos.length; i++) {
        //    pos[i] = pos[i] - pos[0];
        //}

        //for(int i=0; i < pos.length; i++) {
        //    Log.d("log", "pos " + i + ": " + pos[i] + " c:" + tc[i]);
        //}
        //Log.d("log", " ");

        RectF rectF = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);

        paint.reset();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);

        SweepGradient shader;

        float deg, dif;
        float[] p = new float[2];

        float p0 = 0;
        float p1 = pos[0];

        for(int i=0; i < tc.length; i++) {
            int j = i + 1; if (j == tc.length) { j = 0; }

            int[] cg = { tc[i], tc[j] };

            if (j < i) {
                dif = (1 - pos[i]) + pos[j];
                deg = pos[i]; //reduce(pos[j0] + angle) * 360f;
            } else {
                dif = pos[j] - pos[i];
                deg = pos[i]; //reduce(pos[j0] + angle) * 360f;
            }

            p[0] = 0;
            p[1] = dif;

            shader = new SweepGradient(cx, cy, cg, p);

            paint.setShader(shader);

            deg = (deg + offset) * 360f;

            canvas.rotate(deg, cx, cy);
            canvas.drawArc(rectF, p[0], p[1] * 360f, true, paint);
            canvas.rotate(-deg, cx, cy);
        }

        //canvas.drawCircle(cx, cy, radius, paint);
    }

}
