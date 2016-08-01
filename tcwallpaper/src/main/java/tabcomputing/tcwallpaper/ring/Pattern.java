package tabcomputing.tcwallpaper.ring;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;

import java.util.Arrays;

import tabcomputing.tcwallpaper.BasePattern;
import tabcomputing.library.paper.BitmapReuse;

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

    private Bitmap bitmapForColorWheel;

    private BitmapReuse bitmapForCircles = new BitmapReuse();

    // for some reason we can't use the normal paint.reset() for this one
    private Paint specialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds(canvas);

        // I read this would speed things up but is just screwed things up instead
        //setLayerType(LAYER_TYPE_HARDWARE, paint);

        // since the color wheel backdrop is static we only update it if settings change
        if (changeFlag || bitmapForColorWheel == null) {
            // TODO: changes twice, no big deal, but it should only do so once
            bitmapForColorWheel = getColorWheel(bounds);
            changeFlag = false;
        }
        canvas.drawBitmap(bitmapForColorWheel, 0, 0, specialPaint);

        Bitmap cw = getCircleWheel(bounds);
        canvas.drawBitmap(cw, 0, 0, specialPaint);
    }

    // if bounds or preference change this is set to true
    private boolean changeFlag = false;

    private Rect getBounds(Canvas canvas) {
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
    private Bitmap getColorWheel(Rect bounds) {
        if (isSplit()) {
            // TODO: needs descrete version of split
            return getColorWheelSplit(bounds);
        } else {
            if (isSmooth()) {
                return getColorWheelSmooth(bounds);
            } else {
                return getColorWheelDiscrete(bounds);
            }
        }
    }

    /**
     * Draw a rainbow wheel within the given bounds.
     *
     * @param bounds        boundary size
     * @return              bitmap
     */
    private Bitmap getColorWheelSmooth(Rect bounds) {
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

        return bitmap;
    }

    private Bitmap getColorWheelDiscrete(Rect bounds) {
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

        return bitmap;
    }

    protected boolean isSmooth() {
        return settings.isSmooth();
    }

    /**
     * TODO: We can get the position of the gradient switch over just right
     *       if we calculate the positions of each color and at the switch over
     *       adjust the positions of the two adjacent colors (at q?) by the fractional
     *       ratio of the time.
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
            int k = mod(i, hc);
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

        float rotation = (settings.isRotated() ? -90 : 90);

        canvas.rotate(rotation, cx, cy);
        canvas.drawArc(rectF, 0, 360, true, paint);
        canvas.rotate(-rotation, cx, cy);

        return bitmap;
    }

    /**
     * Draw hour circles on a canvas. These will be masked on top of the rainbow wheel.
     *
     * TODO: Draw background.
     */
    private Bitmap getCircleWheel(Rect bounds) {
        //Bitmap bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        //Canvas canvas = new Canvas(bitmap);

        bitmapForCircles.reset(bounds);
        Bitmap bitmap = bitmapForCircles.getBitmap();
        Canvas canvas = bitmapForCircles.getCanvas();

        //RectF rectF = new RectF(bounds.left, bounds.top, bounds.right, bounds.bottom);

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        int hc = hoursOnClock();

        //int half = hd / 2;
        //int qtr = hd / 4;

        float w = faceRadius(canvas);

        // TODO: spare option?
        //float r0 = sparse ? spotRadius(1.5f * hc) : spotRadius();
        float r0 = spotRadius(canvas);
        float r1 = w - r0;
        //float r2 = r0 + (r1 * 1.15f);

        float x, y;
        double r;

        //int[] colors;
        //colors = clockColors(settings.isDuplexed() && !timeSystem.isDaySplit());
        //colors = hourColors();

        drawAnalogBackground(canvas);

        paint.reset();
        paint.setColor(0xFFFFFF);
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        paint.setColor(Color.TRANSPARENT);
        //paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        //paint.setAntiAlias(settings.getBackground() != 2);
        //paint.setStrokeWidth(1.0f);
        //paint.clearShadowLayer();

        //int[] colors = timeColors();
        //canvas.drawColor(colors[0]);

        // draw hours
        for (int i = 0; i < hc; i++) {
            r = ((double) i) / hc;
            x = (float) (cx + r0 * sin(r + rot()));
            y = (float) (cy - r0 * cos(r + rot()));
            //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
            //y = (float) (cy - r0 * cos(r + rot() - 0.041666667));
            canvas.drawCircle(x, y, r1, paint);
        }

        return bitmap;
    }

    /**
     * Draw background color and clock face color.
     *
     * @param canvas    drawing canvas
     */
    protected void drawAnalogBackground(Canvas canvas) {
        int[] colors = timeColors();

        if (settings.isSwapped()) {
            int temp = colors[0];
            colors[0] = colors[1];
            colors[1] = temp;
        }

        float cx = centerX(canvas);
        float cy = centerY(canvas);
        float r  = spotRadius(canvas);

        paint.reset();
        paint.setColor(colors[0]);

        canvas.drawColor(colors[1]);
        canvas.drawCircle(cx, cy, r, paint);
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

}
