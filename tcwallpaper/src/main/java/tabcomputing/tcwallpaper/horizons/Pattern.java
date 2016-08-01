package tabcomputing.tcwallpaper.horizons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;

import tabcomputing.tcwallpaper.BasePattern;

public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
        settings = wallpaper.getSettings();

        paint.reset();
        paint.setAntiAlias(true);

        // TODO: If we use anti-alias it looks good at the color seam, but the transparent circle leaves an artifact! How to fix?

        circlePaint.reset();
        circlePaint.setAntiAlias(false);
        circlePaint.setStyle(Paint.Style.FILL);

        transPaint.reset();
        transPaint.setAntiAlias(false);
        transPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    private Settings settings;

    private Paint circlePaint = new Paint();
    private Paint transPaint = new Paint();
    private Paint shaderPaint = new Paint();

    @Override
    public void draw(Canvas canvas) {
        drawSky(canvas);
        if (settings.isSun()) {
            drawSun(canvas);
        }
        drawGround(canvas);
    }

    private void drawSky(Canvas canvas) {
        int[] colors = timeColors();

        // TODO: reverse colors to swap minute for hour on hillside ?
        if (false) { //(! settings.isColorReversed()) {
            reverseArray(colors);
        }

        LinearGradient shader = new LinearGradient(0, 0, 0, canvas.getHeight(), colors, null, Shader.TileMode.CLAMP);

        shaderPaint.reset();
        shaderPaint.setShader(shader);

        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), shaderPaint);
    }

    private void drawGround(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float l = (cy * 0.76f);
        float d = (cy * 0.76f);

        float off = 0.176f;
        float rot = 0f;

        int hc = hoursOnClock();
        int[] hcolors = clockColors();

        int start = (int) (hc * 0.25);
        int k = mod(start, hc);
        double r = (double) k / hc;

        float x0 = (float) (cx + l * sin(r + rot - off));
        float y0 = (float) (cy - l * cos(r + rot - off));

        int j;
        int s = hcolors.length;

        int[] t = time();
        int h = t[0];

        h = mod(h, hc);

        // FIXME: rotation looks wrong, might have to be different on split time
        hcolors = rotate(hcolors, h + (hc / 4));

        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cnvs = new Canvas(bitmap);
        cnvs.drawColor(Color.TRANSPARENT);

        float x, y;

        for (int i = 0; i <= (hc / 2); i++) {
            //for (i = 0; i < s; i++) {
            j = mod(i + start, s);
            //k = mod(h + i + 1, hc);
            r = (double) j / hc;

            x = (float) (cx + l * sin(r + rot - off));
            y = (float) (cy - l * cos(r + rot - off));

            bitmap.eraseColor(Color.TRANSPARENT);

            circlePaint.setColor(hcolors[j]);

            cnvs.drawCircle(x, y, d, circlePaint);
            cnvs.drawCircle(x0, y0, d, transPaint);

            canvas.drawBitmap(bitmap, 0, 0, paint);

            x0 = x;
            y0 = y;
        }
    }

    private void drawGroundCompact(Canvas canvas) {
        paint.reset();
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL);

        transPaint.setAntiAlias(false);
        transPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float x, y;

        int hc = hoursOnClock();

        float m = max(cx, cy);
        float l = (m * 0.9f);
        float d = (m * 0.9f);

        int[] colors = clockColors();

        int[] t = time();
        int h = t[0];

        h = mod(h, hc);

        // FIXME: rotation looks wrong, might have to be different on split time
        //colors = rotate(colors, h + (hc / 4));
        colors = rotate(colors, h);
        colors = append(colors, colors[0]);

        int s = colors.length;

        double r = 1.5 / s;

        float off = 0f; //.176f;
        float rot = 0f;

        float x0 = (float) (cx + l * sin(r + rot - off));
        float y0 = (float) (cy - l * cos(r + rot - off));

        double a = 0.5 / s;

        // TODO: may be able to reuse these instead of recreating them every draw
        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cnvs = new Canvas(bitmap);
        cnvs.drawColor(Color.TRANSPARENT);

        for (int i = 0; i < s; i++) {
            r = r + a;

            x = (float) (cx + l * sin(r + rot - off));
            y = (float) (cy - l * cos(r + rot - off));

            bitmap.eraseColor(Color.TRANSPARENT);

            circlePaint.setColor(colors[i]);

            cnvs.drawCircle(x, y, d, circlePaint);
            cnvs.drawCircle(x0, y0, d, transPaint);

            canvas.drawBitmap(bitmap, 0, 0, paint);

            x0 = x;
            y0 = y;
        }
    }

    private void drawSun(Canvas canvas) {
        //int[] s = timeSegments();
        double[] tr = timeRatios();
        int[] c = timeColors();

        float h = canvas.getHeight() * 0.90f;
        float r = 100.0f;

        float y;
        if (tr[0] > 0.5) {
            y = (float) (h * (tr[0] - 0.5) * 2) + (r * 0.51f);
        } else {
            y = (float) (h * (1.0 - (tr[0] * 2))) + (r * 0.51f);
        }

        paint.reset();
        paint.setColor(c[0]);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX(canvas), y, r, paint);
    }

}
