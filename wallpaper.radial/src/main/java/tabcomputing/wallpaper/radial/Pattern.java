package tabcomputing.wallpaper.radial;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import tabcomputing.library.paper.AbstractPattern;

public class Pattern extends AbstractPattern {

    public Pattern(Settings settings) {
        setSettings(settings);
    }

    @Override
    public void draw(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        int hd = hoursInDay();
        int hc = hoursOnClock();
        //int hc = minutesOnClock();

        double[] hr = handRatios();

        int s = settings.displaySeconds() ? hr.length : hr.length - 1;

        int[] tc = timeColors();
        int[] ts = timeSystem.clockSegments();

        int i;
        int[] colors;

        canvas.drawColor(Color.WHITE);

        //colors = handColors();
        colors = colorWheel.colors(hc);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        //float d = canvas.getWidth();
        float d;
        RectF rect;

        //d = canvas.getHeight();
        //RectF rect = new RectF(cx - d, cy - d, cx + d, cy + d);

        d = maxDimension(canvas); //canvas.getHeight();
        rect = new RectF(cx - d, cy - d, cx + d, cy + d);

        float sweepAngle = 360.0f / hc;
        float startAngle = 0.0f - (sweepAngle / 2.0f);

        // TODO: move clock rotation into the time system itself
        int offset;
        if (settings.isRotated()) {
            offset = 270;
        } else {
            offset = 90;
        }

        // draw wheel
        for (i = 0; i < hc; i++) {
            //r = ((double) i) / hc;
            paint.setColor(colors[i]);
            canvas.drawArc(rect, startAngle - offset, sweepAngle, true, paint);
            startAngle = startAngle + sweepAngle;
        }

        i = 1;

        sweepAngle = 360.0f / ts[i];
        //startAngle = sweepAngle * (float) t - (sweepAngle / 2.0f);
        startAngle = (360.0f * (float) hr[i]) - (sweepAngle / 2.0f);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(tc[i]);
        //if (hr[i] == (int) hr[i]) {
        //paint.setShadowLayer(2.0f * (i + 1) + 10.0f, 0.0f, 0.0f, Color.BLACK);
        //}
        canvas.drawArc(rect, startAngle - offset, sweepAngle, true, paint);

        canvas.drawCircle(cx, cy, d * 0.25f, paint);

        //offset = offset - 10;

        d = canvas.getHeight() * 0.2f;
        rect = new RectF(cx - d, cy - d, cx + d, cy + d);

        sweepAngle = 360.0f / hc;
        startAngle = 0.0f - (sweepAngle / 2.0f);

        // draw wheel
        for (i = 0; i < hc; i++) {
            //r = ((double) i) / hc;
            paint.setColor(colors[i]);
            canvas.drawArc(rect, startAngle - offset, sweepAngle, true, paint);
            startAngle = startAngle + sweepAngle;
        }

        i = 0;

        sweepAngle = 360.0f / ts[i];
        //startAngle = sweepAngle * (float) t - (sweepAngle / 2.0f);
        startAngle = (360.0f * (float) hr[i]) - (sweepAngle / 2.0f);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(tc[i]);
        //if (hr[i] == (int) hr[i]) {
        //paint.setShadowLayer(2.0f * (i + 1) + 10.0f, 0.0f, 0.0f, Color.BLACK);
        //}
        canvas.drawArc(rect, startAngle - offset, sweepAngle, true, paint);

        canvas.drawCircle(cx, cy, d/2.25f, paint);
    }

}
