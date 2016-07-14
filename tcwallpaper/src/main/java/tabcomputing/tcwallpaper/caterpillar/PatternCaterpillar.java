package tabcomputing.tcwallpaper.caterpillar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import tabcomputing.library.paper.AbstractPattern;

import java.util.Arrays;

/**
 * Fun pattern that kids will like.
 */
public class PatternCaterpillar extends AbstractPattern {

    public PatternCaterpillar(Settings settings) {
        setSettings(settings);
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
        int i, k = 0;
        double r = 0;

        int h = timeSystem.time()[0];
        int[] t = timeSystem.time();

        if (!settings.displaySeconds()) {
            t = Arrays.copyOf(t, t.length - 1);
        }

        //Bitmap bitmap;
        //Canvas cnvs;

        double offset = 0.0; //0.176;

        for (i = 0; i < hc; i++) {
            k = mod(h + i + 1, hc);
            r = (double) k / hc;

            x = (float) (cx + l * sin(r + rot() + offset));
            y = (float) (cy - l * cos(r + rot() + offset));

            //color = colorWheel.alphaColor(hcolors[k], (float) (i+1) / hc);
            paint.setColor(hcolors[k]);

            //bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            //cnvs = new Canvas(bitmap);

            canvas.drawCircle(x, y, d, paint);

            //for (int q = 0; q < s; q++) {
            //    if ((int) (r * hc) == (int) (hr[q] * hc)) {
            //       paint.setStyle(Paint.Style.STROKE);
            //        paint.setStrokeWidth(20.0f);
            //        paint.setColor(Color.WHITE);
            //        cnvs.drawCircle(x, y, d, paint);
            //    }
            //}

            paint.setStyle(Paint.Style.FILL);

            //cnvs.drawCircle(x0, y0, d, transPaint);

            //canvas.drawBitmap(bitmap, 0, 0, paint);

            x0 = x;
            y0 = y;
        }

        // TODO: rotate the eyes to the minutes

        double rm = hr[1];

        x = (float) (cx + (l * 0.6f) * sin(r + rot() + offset - 0.08));
        y = (float) (cy - (l * 0.6f) * cos(r + rot() + offset - 0.08));

        paint.setColor(colors[1]);
        canvas.drawCircle(x, y, d / 6, paint);

        x = (float) (x + (d/20) * sin(rm));
        y = (float) (y - (d/20) * cos(rm));

        paint.setColor(Color.BLACK);
        canvas.drawCircle(x, y, d / 10, paint);

        x = (float) (cx + (l * 0.6f) * sin(r + rot() + offset + 0.08));
        y = (float) (cy - (l * 0.6f) * cos(r + rot() + offset + 0.08));

        paint.setColor(colors[1]);
        canvas.drawCircle(x, y, d / 6, paint);

        x = (float) (x + (d/20) * sin(rm));
        y = (float) (y - (d/20) * cos(rm));

        if (settings.displaySeconds()) {
            paint.setColor(hcolors[2]);
        } else {
            paint.setColor(Color.BLACK);
        }
        canvas.drawCircle(x, y, d / 10, paint);

        // draw smile

        x = (float) (cx + (l*0.85) * sin(r + rot() + offset));
        y = (float) (cy - (l*0.85) * cos(r + rot() + offset));

        RectF rect = new RectF();
        rect.set(x - cx / 2, y - cx / 2, x + cx / 2, y + cx / 2);

        paint.setColor(Color.BLACK);
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawArc(rect, 360.0f * ((float) reduce(r + 0.55f)), 140.0f, true, paint);

        paint.setColor(hcolors[k]);

        rect.set(x - cx/2.2f, y - cx/2.2f, x + cx/2.2f, y + cx/2.2f);
        canvas.drawArc(rect, 360.0f * ((float) reduce(r + 0.55f)), 140.0f, true, paint);

    }

}
