package tabcomputing.tcwallpaper.wave;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;

import java.util.Arrays;

import tabcomputing.library.paper.BitmapReuse;
import tabcomputing.tcwallpaper.BasePattern;


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

    // @deprecated
    //private Bitmap bitmapForColorWheel;

    private BitmapReuse bitmapForColorWheel = new BitmapReuse();
    private BitmapReuse bitmapForCircles    = new BitmapReuse();
    private BitmapReuse bitmapForBackdrop   = new BitmapReuse();

    // for some reason we can't use the normal paint.reset() for this one
    private Paint specialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    public void drawPattern(Canvas canvas) {
        Rect bounds = getBounds(canvas);

        // TODO: allow option for black background?
        BitmapReuse background = getBitmapBackground(bounds);
        canvas.drawBitmap(background.getBitmap(), 0, 0, paint);

        BitmapReuse overlay = getBitmapOverlay(bounds);
        canvas.drawBitmap(overlay.getBitmap(), 0, 0, paint);
    }

    /**
     *
     * @param bounds        canvas bounds
     * @return              reusable bitmap
     */
    protected BitmapReuse getBitmapOverlay(Rect bounds) {
        Canvas canvas = bitmapForBackdrop.getCleanCanvas(bounds);

        float w = bounds.width();
        float h = bounds.height();

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        int[] colors = timeColors();


        double[] tr = timeRatios();

        //drawWave(canvas, tr[0], colors[0], 0);
        //drawWave(canvas, tr[1], colors[1], h);

        drawWaveLine(canvas, tr[0], colors[0]);
        drawWaveLine(canvas, tr[1], colors[1]);

        if (settings.withSeconds()) {
            drawWaveLine(canvas, tr[2], colors[2]);
        }

        return bitmapForBackdrop;
    }

    private void drawWave(Canvas canvas, double ratio, int color, float edge) {
        Path path = new Path();

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float xb = w;
        float yb = cy;
        float ys = cy / 2;

        path.moveTo(0, cy);

        for(double x = 0.0; x <= 1.05; x = x + 0.02) {
            double y = cos(reduce(x + ratio + 0.5));
            path.lineTo((float) x * xb, (float) y * ys + yb);
        }

        path.lineTo(w, edge);
        path.lineTo(0, edge);
        path.lineTo(0, cy);

        path.close();

        paint.reset();
        paint.setStrokeWidth(30f);
        paint.setColor(color);

        canvas.drawPath(path, paint);

    }

    private void drawWaveLine(Canvas canvas, double ratio, int color) {
        paint.reset();
        paint.setStrokeWidth(30f * (1.5f * (settings.getSize() + 1)));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(color);
        paint.setAlpha(225);

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float xb = w;
        float yb = cy;
        float ys = cy / 2;

        double x0 = 0;
        double y0 = cos(reduce(x0 + ratio + 0.5));
        double delta = 0.02;

        for(double x = -0.06; x <= 1.06; x = x + delta) {
            double y = cos(reduce(x - ratio - 0.5));
            canvas.drawLine((float) x0 * xb, (float) y0 * ys + yb, (float) x * xb, (float) y * ys + yb, paint);
            x0 = x; y0 = y;
        }
    }

    protected BitmapReuse getBitmapBackground(Rect bounds) {
        return getColorWheel(bounds);
    }

    /**
     * Draw a rainbow wheel within the given bounds.
     *
     * @param bounds        boundary size
     * @return              bitmap
     */
    private BitmapReuse getColorWheel(Rect bounds) {
        if (settings.isSmooth()) {
            return getColorWheelSmooth(bounds);
        } else {
            return getColorWheelDiscrete(bounds);
        }
    }

    /**
     * Draw a rainbow wheel within the given bounds.
     *
     * @param bounds        boundary size
     * @return              bitmap
     */
    private BitmapReuse getColorWheelSmooth(Rect bounds) {
        Canvas canvas = bitmapForColorWheel.getCleanCanvas(bounds);

        //bitmapForColorWheel.reset(bounds);
        //Bitmap bitmap = bitmapForColorWheel.getBitmap();
        //Canvas canvas = bitmapForColorWheel.getCanvas();

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        float w = bounds.width();
        float h = bounds.height();
        float m = max(w, h);

        RectF rectF = new RectF(cx - m, cy - m, cx + m, cy + m);

        int[] colors = clockColors(); //(settings.isDuplexed());

        int[] colors2 = new int[colors.length + 2];

        for(int i=0; i < colors.length; i++) {
            colors2[i+1] = colors[i];
        }
        colors2[0] = colors[colors.length-1];
        colors2[colors.length] = colors[0];


        //SweepGradient shader = new SweepGradient(cx, cy, colors2, null);
        LinearGradient shader = new LinearGradient(0, cy, w, cy, colors, null, Shader.TileMode.CLAMP);

        paint.reset();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(shader);

        canvas.drawRect(0, 0, w, h, paint);

        /*
        //int[] colors2 = Arrays.copyOf(colors, colors.length + 1);
        //colors2[colors.length] = colors2[0];

        float rotation = (settings.isRotated() ? -90 : 90);

        canvas.rotate(rotation, cx, cy);
        canvas.drawArc(rectF, 0, 360, true, paint);
        canvas.rotate(-rotation, cx, cy);
        */

        return bitmapForColorWheel;
    }

    private BitmapReuse getColorWheelDiscrete(Rect bounds) {
        Canvas canvas = bitmapForColorWheel.getCleanCanvas(bounds);

        //bitmapForColorWheel.reset(bounds);
        //Bitmap bitmap = bitmapForColorWheel.getBitmap();
        //Canvas canvas = bitmapForColorWheel.getCanvas();

        float w = bounds.width();
        float h = bounds.height();

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        float m = cx + cy;
        RectF rectF = new RectF(cx - m, cy - m, cx + m, cy + m);

        //RectF rectF;
        //if (cx < cy) {
        //    rectF = new RectF(0, cy - cx, cx * 2, cy + cx);
        //} else {
        //    rectF = new RectF(cx - cy, 0, cx + cy, cy * 2);
        //}

        int[] colors = clockColors(); //(settings.isDuplexed());

        paint.reset();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);

        int ticks = hoursOnClock();

        float s = w / ticks;

        for (int i = 0; i < ticks; i++) {
            paint.setColor(colors[i]);
            canvas.drawRect(s * i, 0, s * (i + 1), h, paint);
        }

        /*
        float offset = settings.isRotated() ? -90f : 90f;

        float sweepAngle = 360.0f / ticks;
        float timeAngle  = 0; //sweepAngle * time()[0];
        float startAngle = timeAngle - (sweepAngle / 2.0f) + offset;

        for (int i = 0; i < ticks; i++) {
            paint.setColor(colors[i]);
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
            startAngle = startAngle + sweepAngle;
        }
        */

        return bitmapForColorWheel;
    }








    private boolean isSplit() {
        return (timeSystem.isDaySplit() && !settings.isDuplexed());
    }

    private boolean isRotated() {
        return settings.isRotated();
    }

    //@Override
    //public float centerY(Canvas canvas) {
    //    return canvas.getHeight() / 2;
    //}

    //@Override
    //public float centerY(Rect bounds) {
    //    return bounds.height() / 2;
    //}


    /*
     * Because the background color wheel won't change unless certain settings change,
     * we cache those settings and only redraw the color wheel bitmap as needed.
     */

    private Rect boundsCache = new Rect(0,0,0,0);

    private boolean boundsChanged(Rect bounds) {
        if (! boundsCache.equals(bounds)) {
            boundsCache = bounds;
            return true;
        }
        return false;
    }

    // if bounds or preference change this is set to true
    private boolean changeFlag = false;

    @Override
    protected Rect getBounds(Canvas canvas) {
        Rect bounds = new Rect();
        canvas.getClipBounds(bounds);
        if (boundsChanged(bounds)) {
            changeFlag = true;
        }
        return bounds;
    }

    @Override
    public void preferenceChanged(String key) {
        super.preferenceChanged(key);
        changeFlag = true;
    }






    /**
     * TODO: I hate this shit an I think we are just going to say "oh well" and not support it.
     *
     * TODO: Adjust this to draw the arc from/to the switch over point. I think I'd rather have a distinct line rather than the gray gradient.
     */
    private Bitmap getColorWheelSplit(Rect bounds) {
        int hd = hoursInDay();
        int hc = hoursOnClock();
        int half = hd / 2;
        int qtr  = hd / 4;
        //int h = (int) ((timeSystem.timeRatios()[0] * hd));
        int h = (int) ((timeSystem.timeRatios()[0] * hd) + 0.5);
        int q = half + mod(h + qtr, hc);
        //int i = mod(hx - (hc / 2), hc);  // TODO: replace this color index with black or white?
        int s;
        if (h < hc - qtr || h >= hc + qtr) {
            s = half;
        } else {
            s = 0;
        }

        // TODO: Use a BitmapReuse
        Bitmap bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        //bitmapForColorWheel.reset(bounds);
        //Bitmap bitmap = bitmapForColorWheel.getBitmap();
        //Canvas canvas = bitmapForColorWheel.getCanvas();

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        RectF rectF;
        if (cx < cy) {
            rectF = new RectF(0, cy - cx, cx * 2, cy + cx);
        } else {
            rectF = new RectF(cx - cy, 0, cx + cy, cy * 2);
        }

        int[] colors = colorWheel.colors(hd); //settings.isDuplexed() && !timeSystem.isDaySplit());

        int[] duplexColors = new int[hc];

        for (int i = 0; i < q; i++) {
            int j = mod(i + s, hd);
            int k = mod(i, hc);   // z + i ?
            duplexColors[k] = colors[j];
        }

        int z = mod(h - qtr, hc);
        duplexColors[z] = Color.GRAY;

        duplexColors = Arrays.copyOf(duplexColors, duplexColors.length + 1);
        duplexColors[duplexColors.length - 1] = duplexColors[0];

        SweepGradient shader = new SweepGradient(cx, cy, duplexColors, null);

        paint.reset();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(shader);

        float rotation = (settings.isRotated() ? 90 : -90);

        canvas.rotate(rotation, cx, cy);
        canvas.drawArc(rectF, 0, 360, true, paint);
        canvas.rotate(-rotation, cx, cy);

        return bitmap;
    }

}
