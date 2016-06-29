package tabcomputing.wallpaper.pieslice;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import tabcomputing.library.paper.AbstractPattern;

/**
 * This pattern simply cuts out a slice of the screen aligned with hands on a clock.
 *
 * TODO: Perhaps in a future version we can add an option to make it look like real pie ;-)
 */
public class Pattern extends AbstractPattern {

    public Pattern(Settings settings) {
        setSettings(settings);
    }

    /**
     * TODO: Improve seconds support.
     *
     * @param canvas    canvas instance
     */
    @Override
    public void draw(Canvas canvas) {
        float h = canvas.getHeight();

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        double[] hr = timeSystem.handRatios();

        int s = hr.length;

        if (!settings.displaySeconds()) {
            s = s - 1;
        }

        int l = s - 1;

        int[] colors = timeColors();

        Paint paint = new Paint();
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

        double offset;
        if (settings.isRotated()) {
            offset = 0.25;
        } else {
            offset = -0.25;
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

}
