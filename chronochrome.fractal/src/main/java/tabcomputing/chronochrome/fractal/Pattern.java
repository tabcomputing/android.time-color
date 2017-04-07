package tabcomputing.chronochrome.fractal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;

import java.util.ArrayList;
import java.util.Arrays;

import tabcomputing.library.paper.BitmapReuse;
import tabcomputing.library.paper.BasePattern;

/**
 * @deprecated
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

    protected Settings settings;

    protected void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    private BitmapReuse bitmapForColorWheel = new BitmapReuse();
    //private BitmapReuse bitmapForCircles    = new BitmapReuse();
    private BitmapReuse bitmapForBackdrop   = new BitmapReuse();

    // for some reason we can't use the normal paint.reset() for this one
    private Paint specialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Path path = new Path();

    @Override
    public void drawPattern(Canvas canvas) {
        Rect bounds = getBounds(canvas);

        BitmapReuse background = getBitmapBackground(bounds);
        canvas.drawBitmap(background.getBitmap(), 0, 0, paint);

        BitmapReuse overlay = getBitmapFractal(bounds);
        canvas.drawBitmap(overlay.getBitmap(), 0, 0, specialPaint);
    }

    /**
     *
     * @param bounds        canvas bounds
     * @return              reusable bitmap
     */
    protected BitmapReuse getBitmapFractal(Rect bounds) {
        Canvas canvas = bitmapForBackdrop.getCleanCanvas(bounds);

        int[] colors = timeColors();

        float cx = (int) centerX(canvas);
        float cy = (int) centerY(canvas);

        //float r  = spotRadius(canvas);

        canvas.drawColor(Color.TRANSPARENT);

        paint.reset();
        //paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //paint.setShadowLayer(10f, 0f, 0f, Color.BLACK);

        float x = cx;
        float y = cy;
        float r = cx / 1.8f;

        ArrayList<ArrayList<PointFR>> factions = new ArrayList<>();

        ArrayList<PointFR> centers = new ArrayList<>();
        ArrayList<PointFR> temp;

        centers.add(new PointFR(cx, cy, r));

        for(int i=0; i < 6; i++) {
            factions.add(centers);

            temp = new ArrayList<>();

            x = x / 2f;
            y = y / 2f;
            r = r / 1.8f;

            for(PointFR p : centers) {
                //canvas.drawCircle(p.x, p.y, r, paint);
                //canvas.drawRect(p.x - r, p.y - r, p.x + r, p.y + r, paint);
                temp.add(new PointFR(p.x - x, p.y - y, r));
                temp.add(new PointFR(p.x + x, p.y - y, r));
                temp.add(new PointFR(p.x - x, p.y + y, r));
                temp.add(new PointFR(p.x + x, p.y + y, r));
            }

            centers = temp;
        }

        factions = reverse(factions);

        int i = factions.size() - 1;

        for(ArrayList<PointFR> f : factions) {
            paint.setColor(colors[mod(i--, colors.length)]);

            for(PointFR p : f) {
                path.reset();
                path.moveTo(p.x, p.y - p.r);
                path.lineTo(p.x - p.r, p.y + (p.r / 2));
                path.lineTo(p.x + p.r, p.y + (p.r / 2));
                path.moveTo(p.x, p.y - p.r);
                path.close();
                canvas.drawPath(path, paint);
                //canvas.drawCircle(p.x, p.y, p.r, paint);
            }
        }

        return bitmapForBackdrop;
    }

    private class PointFR {
        public PointFR(float x, float y, float r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }
        public float x, y, r;
    }

    private BitmapReuse getBitmapBackground(Rect bounds) {
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

    /**
     * Draw the minute/second dashes of the clock face.
     */
    private void drawTicks(Canvas canvas, int div, float radius) {
        double r;
        int c;
        float x0, y0, x1, y1;

        float length = 12.0f;

        //float w  = faceRadius(canvas);
        //float radius = w * 1.05f; //r0 + (r1 * 1.15f);

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        //int mc = timeSystem.minutesOnClock();

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

            x0 = (float) (cx + radius * sin(r + rot()));
            y0 = (float) (cy - radius * cos(r + rot()));

            x1 = (float) (cx + (radius + length) * sin(r + rot()));
            y1 = (float) (cy - (radius + length) * cos(r + rot()));

            canvas.drawLine(x0, y0, x1, y1, paint);
        }
    }

    private boolean isSplit() {
        return (timeSystem.isDaySplit() && !settings.isDuplexed());
    }

    private boolean isRotated() {
        return settings.isRotated();
    }

    @Override
    public float centerY(Canvas canvas) {
        return canvas.getHeight() / 2;
    }

    @Override
    public float centerY(Rect bounds) {
        return bounds.height() / 2;
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

}
