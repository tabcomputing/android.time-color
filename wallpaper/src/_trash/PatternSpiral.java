package com.tabcomputing.chronochrome.wallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;

import tabcomputing.library.paper.AbstractPattern;

import java.util.Arrays;

/**
 * Make background a solid color based on time. By default it is by hour, but if
 * colors are reversed in the settings it is instead by the minute.
 */
public class PatternSpiral extends AbstractPattern {

    //public PatternSpiral(Settings settings, TimeSystem timeSystem, ColorWheel colorWheel) {
    //    this.settings   = settings;
    //    this.timeSystem = timeSystem;
    //    this.colorWheel = colorWheel;
    //}

    @Override
    public void draw(Canvas canvas) {
        float cx = canvas.getWidth() / 2;
        float cy = canvas.getHeight() / 2;

        float x=0, y=0;

        int hc = hoursOnClock();

        double[] hr = timeSystem.handRatios();

        int[] tc = timeColors();

        int[] t = timeSystem.time();

        int s = hr.length;

        if (!settings.displaySeconds()) {
            s = s - 1;
        }

        int[] hcolors = clockColors();

        int[] colors = timeColors();

        int color;

        float l;
        float d;

        canvas.drawColor(tc[0]);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        //Paint transPaint = new Paint();
        //transPaint.setAntiAlias(true);
        //transPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        float x0 = 0, y0 = 0;
        int i, k = 0;
        double r = 0;

        int h = t[0];

        if (!settings.displaySeconds()) {
            t = Arrays.copyOf(t, t.length - 1);
        }

        //Bitmap bitmap;
        //Canvas cnvs;

        double offset = 0.0; //0.176;

        int[] ts = timeSystem.timeSegments();

        l = (cx * 1.0f);
        d = (cx * 1.0f);

        for (i = 0; i < ts[1]; i++) {
            k = mod(t[1] + i + 1, ts[1]);
            r = (double) k / ts[1];

            x = (float) (cx + l * sin(r + rot() + offset));
            y = (float) (cy - l * cos(r + rot() + offset));

            l = l - 5;

            paint.setColor(color(r));
            canvas.drawCircle(x, y, d, paint);
        }

        l = (cx * 0.46f);
        d = (cx * 0.46f);

        for (i = 0; i < hc; i++) {
            k = mod(h + i + 1, hc);
            r = (double) k / hc;

            x = (float) (cx + l * sin(r + rot() + offset));
            y = (float) (cy - l * cos(r + rot() + offset));

            l = l - 7;

            //color = colorWheel.alphaColor(hcolors[k], (float) (i+1) / hc);
            paint.setColor(hcolors[k]);

            //bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            //cnvs = new Canvas(bitmap);

            canvas.drawCircle(x, y, 0, paint);

            //for (int q = 0; q < s; q++) {
            //    if ((int) (r * hc) == (int) (hr[q] * hc)) {
            //       paint.setStyle(Paint.Style.STROKE);
            //        paint.setStrokeWidth(20.0f);
            //        paint.setColor(Color.WHITE);
            //        cnvs.drawCircle(x, y, d, paint);
            //    }
            //}


            //cnvs.drawCircle(x0, y0, d, transPaint);

            //canvas.drawBitmap(bitmap, 0, 0, paint);

            x0 = x;
            y0 = y;
        }

    }

    /**
     * Exact center.
     *
     * @return      vertical origin
     */
    @Override
    public float centerY(Canvas canvas) {
        return canvas.getHeight() / 2;
    }

}
