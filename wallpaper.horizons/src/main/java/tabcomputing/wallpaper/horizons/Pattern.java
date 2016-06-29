package tabcomputing.wallpaper.horizons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;

import tabcomputing.library.paper.AbstractPattern;
import tabcomputing.library.paper.CommonSettings;

import java.util.Arrays;

public class Pattern extends AbstractPattern {

    public Pattern(CommonSettings settings) {
        setSettings(settings);
    }

    @Override
    public void draw(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float x,y;

        int hc = hoursOnClock();

        double[] hr = timeSystem.handRatios();

        int s = hr.length;

        if (!settings.displaySeconds()) {
            s = s - 1;
        }

        int[] hcolors = clockColors();

        //int[] colors = timeColors();
        //int color;

        float l = (cy * 0.76f);
        float d = (cy * 0.76f);

        //canvas.drawColor(Color.WHITE);

        int[] colors = timeColors();

        if (! settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

        // TODO: reverse colors to swap minute for hour on hillside
        // FIXME
        if (false) { //(! settings.isColorReversed()) {
            reverseArray(colors);
        }

        LinearGradient shader = new LinearGradient(0, 0, 0, canvas.getHeight(), colors, null, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);


        paint = new Paint();
        paint.setAntiAlias(false);

        Paint transPaint = new Paint();
        transPaint.setAntiAlias(false);
        transPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        float x0 = 0, y0 = 0;
        int i, k;
        double r;

        int h = timeSystem.time()[0];
        int[] t = timeSystem.time();

        h = mod(h, hc);

        hcolors = rotate(hcolors, h + (hc/4));

        Bitmap bitmap;
        Canvas cnvs;

        int start = (int) (hc * 0.25);

        k = mod(start, hc);
        r = (double) k / hc;

        x0 = (float) (cx + l * sin(r + rot() - 0.176));
        y0 = (float) (cy - l * cos(r + rot() - 0.176));

        int j;

        for (i = 0; i <= (hc / 2); i++) {
            j = mod(i + start, hc);
            //k = mod(h + i + 1, hc);
            r = (double) j / hc;

            x = (float) (cx + l * sin(r + rot() - 0.176));
            y = (float) (cy - l * cos(r + rot() - 0.176));

            //color = colorWheel.alphaColor(hcolors[k], (float) (i+1) / hc);
            paint.setColor(hcolors[j]);

            bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            cnvs = new Canvas(bitmap);

            cnvs.drawCircle(x, y, d, paint);

            //for(int q=0; q < s; q++) {
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

            x0 = x; y0 = y;
        }
    }

}
