package com.tabcomputing.chronochrome.watch;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.color.ColorWheel;

import java.util.Arrays;

/**
 * Make background a solid color based on time. By default it is by hour, but if
 * colors are reversed in the settings it is instead by the minute.
 */
public class PatternSpiral extends AbstractPattern {

    public PatternSpiral(Settings settings, TimeSystem timeSystem, ColorWheel colorWheel) {
        this.settings   = settings;
        this.timeSystem = timeSystem;
        this.colorWheel = colorWheel;
    }

    @Override
    public void draw(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float x, y;

        int hc = hoursOnClock();

        double[] hr = timeSystem.handRatios();

        int s = hr.length;

        if (!settings.displaySeconds()) {
            s = s - 1;
        }

        int[] hcolors = clockColors();

        int[] colors = timeColors();

        int color;

        float l = (cy * 0.75f);
        float d = (cy * 0.75f);

        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Paint transPaint = new Paint();
        transPaint.setAntiAlias(true);
        transPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        float x0 = 0, y0 = 0;
        int i, j, k;
        double r = 0;

        int h = timeSystem.time()[0];
        int[] t = timeSystem.time();

        if (!settings.displaySeconds()) {
            t = Arrays.copyOf(t, t.length - 1);
        }

        Bitmap bitmap;
        Canvas cnvs;

        for (i = 0; i < hc; i++) {
            k = mod(h + i + 1, hc);
            r = (double) k / hc;

            x = (float) (cx + l * sin(r + rot() - 0.176));
            y = (float) (cy - l * cos(r + rot() - 0.176));

            //color = colorWheel.alphaColor(hcolors[k], (float) (i+1) / hc);
            paint.setColor(hcolors[k]);

            bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            cnvs = new Canvas(bitmap);

            cnvs.drawCircle(x, y, d, paint);

            //for (int q = 0; q < s; q++) {
            //    if ((int) (r * hc) == (int) (hr[q] * hc)) {
            //        paint.setStyle(Paint.Style.STROKE);
            //        paint.setStrokeWidth(20.0f);
            //        paint.setColor(Color.WHITE);
            //        cnvs.drawCircle(x, y, d, paint);
            //    }
            //}

            paint.setStyle(Paint.Style.FILL);

            cnvs.drawCircle(x0, y0, d, transPaint);

            canvas.drawBitmap(bitmap, 0, 0, paint);

            x0 = x;
            y0 = y;
        }

        /*
        // TODO: rotate the eyes to the minutes

        x = (float) (cx + (l/1.8f) * sin(r + rot() - 0.1));
        y = (float) (cy - (l/1.8f) * cos(r + rot() - 0.1));

        paint.setColor(Color.WHITE);
        canvas.drawCircle(x+10, y, d / 6, paint);
        paint.setColor(colors[1]);
        canvas.drawCircle(x, y, d / 10, paint);

        x = (float) (cx + (l/1.8f) * sin(r + rot() + 0.1));
        y = (float) (cy - (l/1.8f) * cos(r + rot() + 0.1));

        paint.setColor(Color.WHITE);
        canvas.drawCircle(x+10, y, d / 6, paint);
        paint.setColor(colors[1]);
        canvas.drawCircle(x, y, d / 10, paint);
        */


        // time part below

        int[] tc = timeColors();
        int[] ts = timeSystem.timeSegments();

        i = 1;
        float sweepAngle, startAngle;

        RectF rect;

        //d = canvas.getHeight();
        //RectF rect = new RectF(cx - d, cy - d, cx + d, cy + d);

        d = maxDimension(canvas); //canvas.getHeight();
        rect = new RectF(cx - d, cy - d, cx + d, cy + d);

        sweepAngle = 360.0f / ts[i];
        //startAngle = sweepAngle * (float) t - (sweepAngle / 2.0f);
        startAngle = (360.0f * (float) hr[i]) - (sweepAngle / 2.0f);

        // TODO: move clock rotation into the time system itself
        int offset;
        if (settings.rotateTime()) {
            offset = 270;
        } else {
            offset = 90;
        }

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
        //for (i = 0; i < hc; i++) {
        //    //r = ((double) i) / hc;
        //   paint.setColor(hcolors[i]);
        //    canvas.drawArc(rect, startAngle - offset, sweepAngle, true, paint);
        //    startAngle = startAngle + sweepAngle;
        //}

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
