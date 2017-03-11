package tabcomputing.chronochrome.ring;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;

import java.util.Arrays;

import tabcomputing.library.paper.BitmapReuse;
import tabcomputing.tcwallpaper.BasePattern;


public class PatternWeb extends BasePattern {

    public PatternWeb(Wallpaper wallpaper) {
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

        BitmapReuse background = getBitmapBackground(bounds);
        canvas.drawBitmap(background.getBitmap(), 0, 0, paint);

        BitmapReuse overlay = getBitmapOverlay(bounds);
        canvas.drawBitmap(overlay.getBitmap(), 0, 0, specialPaint);

        int[] colors = {Color.WHITE, Color.TRANSPARENT};
        float cx = centerX(canvas);
        float cy = centerY(canvas);
        RadialGradient shader = new RadialGradient(cx, cy, cx / 1.25f, colors, null, Shader.TileMode.CLAMP);
        paint.reset();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);

        colors = timeColors();
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(colors[0]);
        //paint.setShadowLayer(20f, 0f, 0f, colors[0]);
        canvas.drawCircle(cx, cy, (cx / 2) * 0.94f, paint);
    }

    /**
     *
     * @param bounds        canvas bounds
     * @return              reusable bitmap
     */
    protected BitmapReuse getBitmapOverlay(Rect bounds) {
        Canvas canvas = bitmapForBackdrop.getCleanCanvas(bounds);

        BitmapReuse background = getColorWheel(bounds);
        canvas.drawBitmap(background.getBitmap(), 0, 0, paint);

        float w = bounds.width();
        float h = bounds.height();

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        int[] colors = timeColors();

        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4f);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        float x, y;

        float r = cx / 2;

        int hc = hoursInDay();

        canvas.drawCircle(cx, cy, r, paint);

        PointF p;

        PointF origin = new PointF(0, 0);
        PointF corner = new PointF(w, h);

        for (int i = 0; i < hc; i++) {
            float a = (float) i / hc;

            x = (float) (cx + r * sin(a));
            y = (float) (cy - r * cos(a));

            float slope = (cx - x) / (cy - y);

            float tanSlope = -(1 / slope);

            p = edgePoint(new PointF(x,y), origin, tanSlope);

            //if (a < 0.125 || (a > 0.45f && a < 0.55)) {
            //    xt = 0;
            //    yt = (xt - x) / tanSlope + y;
            //} else {
            //    yt = 0;
            //    xt = tanSlope * (yt - y) + x;
            //}

            canvas.drawLine(x, y, p.x, p.y, paint);

            //if (tanSlope < 0.01) {
            //    xt = cx * 2;
            //    yt = (xt - x) / tanSlope + y;
            //} else {
            //    yt = cy * 2;
            //    xt = tanSlope * (yt - y) + x;
            //}

            p = edgePoint(new PointF(x,y), corner, tanSlope);

            canvas.drawLine(x, y, p.x, p.y, paint);
        }

        return bitmapForBackdrop;
    }


    protected PointF edgePoint(PointF p, PointF e, float slope) {
        float x, y;
        if (slope > -0.0001 && slope < 0.0001) {
            y = e.y;  // y edge
            x = slope * (y - p.y) + p.x;
        } else {
            x = e.x;  // x edge
            y = (x - p.x) / slope + p.y;
        }
        return new PointF(x, y);
    }



    protected BitmapReuse getBitmapBackground(Rect bounds) {
        Canvas canvas = bitmapForCircles.getCleanCanvas(bounds);

        canvas.drawColor(Color.WHITE);

        float h = bounds.height();
        float w = bounds.width();

        float cx = centerX(bounds);
        float cy = centerY(bounds);
        float r = cx * 0.85f;

        double[] a = timeRatios();

        int[] tc = timeColors();

        paint.reset();
        paint.setColor(tc[1]);
        paint.setStrokeWidth(100f);
        paint.setStyle(Paint.Style.FILL);
        //paint.setShadowLayer(30f, 0f, 0f, Color.BLACK);
        //canvas.drawCircle(cx, cy, r, paint);

        float x = (float) (cx + r * sin(a[1]));
        float y = (float) (cy - r * cos(a[1]));

        //canvas.drawCircle(x, y, 120f, paint);

        /*
        float x0, y0, x1, y1;

        int[] fadeColors = new int[3];

        for(int i=0; i < tc.length; i++) {
            fadeColors[0] = Color.TRANSPARENT;
            fadeColors[1] = tc[i];
            fadeColors[2] = Color.TRANSPARENT;

            x0 = (float) (cx + r * sin(reduce(a[i] - 0.04 + 0.5)));
            y0 = (float) (cy - r * cos(reduce(a[i] - 0.04 + 0.5)));

            x1 = (float) (cx + r * sin(reduce(a[i] + 0.04 + 0.5)));
            y1 = (float) (cy - r * cos(reduce(a[i] + 0.04 + 0.5)));

            LinearGradient shader = new LinearGradient(x0, y0, x1, y1, fadeColors, null, Shader.TileMode.CLAMP);

            paint.reset();
            paint.setShader(shader);

            if (a[i] < 0.125 || a[i] > 0.875) {
                canvas.drawRect(new RectF(0, 0, w, cy), paint);
            } else if (a[i] < 0.375) {
                canvas.drawRect(new RectF(cx, 0, w, h), paint);
            } else if (a[i] < 0.525) {
                canvas.drawRect(new RectF(0, cy, w, h), paint);
            } else {
                canvas.drawRect(new RectF(0, 0, cx, h), paint);
            }
        }
        */

        return bitmapForCircles;
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

        int[] colors2 = Arrays.copyOf(colors, colors.length + 1);
        colors2[colors.length] = colors2[0];

        SweepGradient shader = new SweepGradient(cx, cy, colors2, null);

        paint.reset();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(shader);

        float rotation = (settings.isRotated() ? -90 : 90);

        canvas.rotate(rotation, cx, cy);
        canvas.drawArc(rectF, 0, 360, true, paint);
        canvas.rotate(-rotation, cx, cy);

        return bitmapForColorWheel;
    }

    private BitmapReuse getColorWheelDiscrete(Rect bounds) {
        Canvas canvas = bitmapForColorWheel.getCleanCanvas(bounds);

        //bitmapForColorWheel.reset(bounds);
        //Bitmap bitmap = bitmapForColorWheel.getBitmap();
        //Canvas canvas = bitmapForColorWheel.getCanvas();

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
        float offset = settings.isRotated() ? -90f : 90f;

        float sweepAngle = 360.0f / ticks;
        float timeAngle  = 0; //sweepAngle * time()[0];
        float startAngle = timeAngle - (sweepAngle / 2.0f) + offset;

        for (int i = 0; i < ticks; i++) {
            paint.setColor(colors[i]);
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
            startAngle = startAngle + sweepAngle;
        }

        return bitmapForColorWheel;
    }








    private boolean isSplit() {
        return (timeSystem.isDaySplit() && !settings.isDuplexed());
    }

    private boolean isRotated() {
        return settings.isRotated();
    }

/*
    @Override
    public float centerY(Canvas canvas) {
        return canvas.getHeight() / 2;
    }

    @Override
    public float centerY(Rect bounds) {
        return bounds.height() / 2;
    }
*/



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
