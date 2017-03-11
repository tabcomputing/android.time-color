package tabcomputing.chronochrome.caterpillar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import tabcomputing.library.paper.BasePattern;

/**
 * Fun pattern that kids will like.
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

    protected void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    @Override
    public void drawPattern(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        int hc = hoursOnClock();

        double[] hr = handRatios();

        int[] hcolors = clockColors();
        int[] colors = timeColors();

        float l = (cy * 1.2f);
        float d = (cy * 0.6f);

        canvas.drawColor(Color.WHITE);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        Paint transPaint = new Paint();
        transPaint.setAntiAlias(true);
        transPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        float x = 0;
        float y = 0;

        int i, k = 0;
        double r = 0;

        int[] t = time();
        int h = t[0];

        //if (!settings.displaySeconds()) {
        //    t = Arrays.copyOf(t, t.length - 1);
        //}

        //Bitmap bitmap;
        //Canvas cnvs;

        double offset = 0.0; //0.176;

        for (i = 0; i < 2*hc; i++) {
            k = mod(h + i + 1, hc);
            r = (double) k / hc;

            x = (float) (cx + l * sin(r + rot() + offset));
            y = (float) (cy - l * cos(r + rot() + offset));

            l = l - ((l * 0.5f) / (2*hc - i));

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

            //paint.setStyle(Paint.Style.FILL);

            //cnvs.drawCircle(x0, y0, d, transPaint);

            //canvas.drawBitmap(bitmap, 0, 0, paint);

            //x0 = x;
            //y0 = y;
        }

        // draw grin

        paint.setAntiAlias(true);

        //x = (float) (cx + (d * 0.01) * sin(r + rot() + 0.5f));
        //y = (float) (cy - (d * 0.01) * cos(r + rot() + 0.5f));

        RectF rect = new RectF();
        rect.set(x - cx / 2, y - cx / 2, x + cx / 2, y + cx / 2);

        float grinSize;
        if (isMean()) {
            grinSize = 60.0f;
            offset = ((180.0f - grinSize) / 3) / 360.0;
        } else {
            grinSize = 140f;
            offset = ((180.0f - grinSize) / 2) / 360.0;
        }

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30f);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawArc(rect, 360f * (float) offset, grinSize, false, paint);

        // draw eyes

        cx = x;
        cy = y;

        double rm = hr[1];

        offset = 0.0; //0.176;

        int eyeColor  = (settings.withSeconds() ? colors[2] : Color.BLACK);
        int eyeWhites = colors[1];

        paint.setColor(colors[1]);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        paint.setStrokeCap(Paint.Cap.SQUARE);

        x = (float) (cx + (d * 0.4f) * sin(-0.12));
        y = (float) (cy - (d * 0.4f) * cos(-0.12));
        paint.setColor(eyeWhites);
        canvas.drawCircle(x, y, d / 5, paint);

        x = (float) (x + (d/20) * sin(rm));
        y = (float) (y - (d/20) * cos(rm));
        paint.setColor(eyeColor);
        canvas.drawCircle(x, y, d / 10, paint);

        x = (float) (cx + (d * 0.4f) * sin(0.12));
        y = (float) (cy - (d * 0.4f) * cos(0.12));
        paint.setColor(eyeWhites);
        canvas.drawCircle(x, y, d / 5, paint);

        x = (float) (x + (d/20) * sin(rm));
        y = (float) (y - (d/20) * cos(rm));
        paint.setColor(eyeColor);
        canvas.drawCircle(x, y, d / 10, paint);

        if (isMean()) {

            // draw mean eyes

            x = (float) (cx + (l + d / 1.3) * sin(r + rot()));
            y = (float) (cy - (l + d / 1.3) * cos(r + rot()));

            //cx = centerX(canvas);
            //cy = centerY(canvas);

            //x = (float) (cx + (d * 0.01) * sin(r + rot() + 0.5f));
            //y = (float) (cy - (d * 0.01) * cos(r + rot() + 0.5f));

            float px = cx;
            float py = cy - d;
            float s0 = d * 0.75f;
            float s1 = d * 0.7f;

            // blot out portion above circle
            paint.setColor(hcolors[k]);
            paint.setAntiAlias(false);
            paint.setStyle(Paint.Style.FILL);

            rect.set(px - s0, py - s0, cx + s0, py + s0);
            canvas.drawArc(rect, 360.0f * 0.125f, 90.0f, true, paint);

            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(20f);
            paint.setAntiAlias(true);

            rect.set(px - s0, py - s0, cx + s0, py + s0);
            //canvas.drawArc(rect, 360.0f * 0.125f, 90.0f, false, paint);
        }

        drawAntenna(canvas, cx, cy, d, colors[1]);
    }

    @Override
    public float centerY(Canvas canvas) {
        return canvas.getHeight() / 2;
    }

    protected boolean isMean() {
        return settings.isMean();
    }

    protected void drawAntenna(Canvas canvas, float cx, float cy, float d, int color) {
        RectF rect = new RectF();

        paint.setStrokeWidth(30.0f);
        paint.setStrokeCap(Paint.Cap.ROUND);

        float x, y;
        float s = d * 0.4f;
        float a = d * 0.15f;

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        x = (float) (cx + d * sin(-0.15));
        y = (float) (cy - d * cos(-0.15));
        rect.set(x - s, y - s, x + s, y + s);
        canvas.drawArc(rect, -65f, 45f, false, paint);

        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        x = (float) (x + s * sin(0.054f));
        y = (float) (y - s * cos(0.054f));
        canvas.drawCircle(x, y, a, paint);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        x = (float) (cx + d * sin(0.15));
        y = (float) (cy - d * cos(0.15));
        rect.set(x - s, y - s, x + s, y + s);
        canvas.drawArc(rect, 200f, 45f, false, paint);

        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        x = (float) (x + s * sin(-0.054f));
        y = (float) (y - s * cos(-0.054f));
        canvas.drawCircle(x, y, a, paint);
    }

}
