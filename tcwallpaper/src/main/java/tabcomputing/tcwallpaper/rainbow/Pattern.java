package tabcomputing.tcwallpaper.rainbow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.Log;

import java.util.Arrays;

import tabcomputing.library.paper.BitmapReuse;
import tabcomputing.tcwallpaper.BasePattern;

/**
 * Draw a color ring.
 */
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
    //private BitmapReuse bitmapForCircles    = new BitmapReuse();
    //private BitmapReuse bitmapForBackdrop   = new BitmapReuse();
    //private BitmapReuse bitmapForPetals     = new BitmapReuse();

    // for some reason we can't use the normal paint.reset() for this one
    private Paint specialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    public void drawPattern(Canvas canvas) {
        Rect bounds = getBounds(canvas);

        float m = min(bounds.width(), bounds.height()) / 2;

        double[] tr = timeRatios();
        int[] ts = timeSegments();

        BitmapReuse cw = getColorWheel(bounds, tr[0]);

        canvas.drawBitmap(cw.getBitmap(), 0, 0, specialPaint);

        if (! settings.isTickless()) {
            for(int j=1; j < tr.length; j++) {
                drawTicks(canvas, ts[j], m * (1 - (j * 0.15f)), tr[j]);
            }
        }
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
     * Draw a rainbow wheel within the given bounds.
     *
     * @param bounds        boundary size
     * @return              bitmap
     */
    private BitmapReuse getColorWheel(Rect bounds, double angle) {
        float cx = centerX(bounds);
        float cy = centerY(bounds);

        float w = bounds.width();
        float h = bounds.height();
        float m = (float) Math.sqrt(w * w + h * h);

        RectF rectF = new RectF(cx - m, cy - m, cx + m, cy + m);
        Rect  rectI = new Rect((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);

        Canvas canvas = bitmapForColorWheel.getCleanCanvas(rectI);

        //bitmapForColorWheel.reset(bounds);
        //Bitmap bitmap = bitmapForColorWheel.getBitmap();
        //Canvas canvas = bitmapForColorWheel.getCanvas();

        int[] colors = clockColors(); //(settings.isDuplexed());

        int[] colors2 = Arrays.copyOf(colors, colors.length + 1);
        colors2[colors.length] = colors2[0];

        SweepGradient shader = new SweepGradient(cx, cy, colors2, null);

        paint.reset();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(shader);

        //float rotation = (settings.isRotated() ? -90 : 90);
        float rotation = (float) reduce(angle + 0.25) * 360f;

        canvas.rotate(-rotation, cx, cy);
        canvas.drawArc(rectF, 0, 360, true, paint);
        canvas.rotate(rotation, cx, cy);

        return bitmapForColorWheel;
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

    /**
     * Draw the minute/second dashes of the clock face.
     */
    private void drawTicks(Canvas canvas, int div, float radius, double angle) {
        double r;
        int c;
        float x0, y0, x1, y1;

        float length = 12.0f;

        //float w  = faceRadius(canvas);
        //float radius = w * 1.05f; //r0 + (r1 * 1.15f);

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        //int mc = timeSystem.minutesOnClock();

        double rot = rot() - reduce(angle + 0.50);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10.0f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(3.0f, 1.0f, 1.0f, Color.LTGRAY);

        for (int i = 0; i < div; i++) {
            r = ((double) i) / div;
            c = colorWheel.color(r);  // TODO: dynamic?

            paint.setColor(c);

            x0 = (float) (cx + radius * sin(r + rot));
            y0 = (float) (cy - radius * cos(r + rot));

            x1 = (float) (cx + (radius + length) * sin(r + rot));
            y1 = (float) (cy - (radius + length) * cos(r + rot));

            canvas.drawLine(x0, y0, x1, y1, paint);
        }
    }

    private boolean isSplit() {
        return (timeSystem.isDaySplit() && !settings.isDuplexed());
    }

    private boolean isRotated() {
        return settings.isRotated();
    }

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

    /*
    @Override
    public float centerY(Rect bounds) {
        return bounds.height() / 2;
    }

    @Override
    public float centerY(Canvas canvas) {
        return canvas.getHeight() / 2;
    }
    */

}
