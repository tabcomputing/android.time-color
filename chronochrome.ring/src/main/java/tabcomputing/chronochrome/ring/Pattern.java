package tabcomputing.chronochrome.ring;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;

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
    private BitmapReuse bitmapForCircles    = new BitmapReuse();
    private BitmapReuse bitmapForBackdrop   = new BitmapReuse();
    private BitmapReuse bitmapForPetals     = new BitmapReuse();

    // if bounds or preference change this is set to true
    private boolean changeFlag = false;

    // for some reason we can't use the normal paint.reset() for this one
    private Paint specialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private boolean isSplit() {
        return (timeSystem.isDaySplit() && !settings.isDuplexed());
    }

    private boolean isRotated() {
        return settings.isRotated();
    }

    protected boolean isSmooth() {
        return settings.isSmooth();
    }

    @Override
    public void drawPattern(Canvas canvas) {
        Rect bounds = getBounds(canvas);

        BitmapReuse background = getBitmapBackground(bounds);
        BitmapReuse cutout     = getBitmapCutout(bounds);

        // draw background and overlay onto main canvas
        paint.reset();
        paint.setAntiAlias(true);
        //paint.setColor(Color.BLACK);

        canvas.drawBitmap(background.getBitmap(), 0, 0, paint);
        canvas.drawBitmap(cutout.getBitmap(), 0, 0, specialPaint);

        //drawTicks(canvas, bounds);
    }

    public void drawPatternX(Canvas canvas) {
        Rect bounds = getBounds(canvas);

        // I read this would speed things up but is just screwed things up instead
        //setLayerType(LAYER_TYPE_HARDWARE, paint);

        // since the color wheel backdrop is static we only update it if settings change
        //if (changeFlag || bitmapForColorWheel == null) {
        //    // TODO: changes twice, no big deal, but it should only do so once
        //    bitmapForColorWheel = getColorWheel(bounds);
        //    changeFlag = false;
        //}
        //canvas.drawBitmap(bitmapForColorWheel, 0, 0, specialPaint);

        //Bitmap cw = getCircleWheel(bounds);
        //canvas.drawBitmap(cw, 0, 0, specialPaint);

    }

    protected BitmapReuse getBitmapBackground(Rect bounds) {
        return getColorWheel(bounds);
    }

    protected BitmapReuse getBitmapCutout(Rect bounds) {
        return getPetalBitmap(bounds);
    }

    @Override
    protected Rect getBounds(Canvas canvas) {
        Rect bounds = new Rect();
        canvas.getClipBounds(bounds);
        // flag id bounds changed from last time
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
    private BitmapReuse getColorWheel(Rect bounds) {
        //if (isSplit()) {
        //    // TODO: needs descrete version of split
        //    return getColorWheelSplit(bounds);
        //} else {
            if (isSmooth()) {
                return getColorWheelSmooth(bounds);
            } else {
                return getColorWheelDiscrete(bounds);
            }
        //}
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

        return bitmapForColorWheel;
    }

    private BitmapReuse getColorWheelDiscrete(Rect bounds) {
        Canvas canvas = bitmapForColorWheel.getCleanCanvas(bounds);

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
     * Draw hour circles on a canvas. These will be masked on top of the rainbow wheel.
     */
    private BitmapReuse getCircleWheel(Rect bounds) {
        Canvas canvas = bitmapForCircles.getCleanCanvas(bounds);

        //RectF rectF = new RectF(bounds.left, bounds.top, bounds.right, bounds.bottom);

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        int hd = hoursInDay() / 2;
        //int hc = hoursOnClock();

        //int half = hd / 2;
        //int qtr = hd / 4;

        float w = faceRadius(canvas);

        // TODO: spare option?
        //float r0 = sparse ? spotRadius(1.5f * hc) : spotRadius();
        float r0 = spotRadius(canvas, hd);
        float r1 = (w - r0) * 0.95f;
        //float r2 = r0 + (r1 * 1.15f);

        float x, y;
        double r;

        //int[] colors;
        //colors = clockColors(settings.isDuplexed() && !timeSystem.isDaySplit());
        //colors = hourColors();

        drawAnalogBackground(canvas, spotRadius(canvas));

        //paint.reset();
        //paint.setColor(0xFFFFFF);
        //paint.setAlpha(0);
        //paint.setAntiAlias(true);
        ////paint.setAntiAlias(settings.getBackground() != 2);

        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        double off = (0.25 / hd);

        // draw hours
        for (int i = 0; i < hd; i++) {
            r = ((double) i) / hd + off;
            x = (float) (cx + r0 * sin(r + rot()));
            y = (float) (cy - r0 * cos(r + rot()));
            //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
            //y = (float) (cy - r0 * cos(r + rot() - 0.041666667));
            canvas.drawCircle(x, y, r1 * 2, paint);
        }

        return bitmapForCircles;
    }

    /*
    protected BitmapReuse getAnalogBackground(Rect bounds) {
        Canvas canvas = bitmapForBackdrop.getCleanCanvas(bounds);

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

        return bitmapForBackdrop;
    }
    */

    /**
     * Draw background color and clock face color.
     *
     * @param canvas    drawing canvas
     * @param radius    radius of inner circle
     */
    protected void drawAnalogBackground(Canvas canvas, float radius) {
        int[] colors = timeColors();

        if (settings.isSwapped()) {
            int temp = colors[0];
            colors[0] = colors[1];
            colors[1] = temp;
        }

        float cx = centerX(canvas);
        float cy = centerY(canvas);
        float r  = radius; //spotRadius(canvas);

        paint.reset();
        paint.setColor(colors[0]);

        canvas.drawColor(colors[1]);
        canvas.drawCircle(cx, cy, r, paint);
    }

    /**
     * Draw the minute/second dashes of the clock face.
     */
    private void drawTicks(Canvas canvas, Rect bounds) {
        double r;
        int c;
        float x0, y0, x1, y1;

        float length = 12.0f;

        float w  = faceRadius(canvas);
        //float r0 = spotRadius();
        //float r1 = w - r0;
        float radius = w * 1.05f; //r0 + (r1 * 1.15f);

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        int mc = timeSystem.minutesOnClock();

        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10.0f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(3.0f, 1.0f, 1.0f, Color.LTGRAY);

        for (int i = 0; i < mc; i++) {
            r = ((double) i) / mc;
            c = colorWheel.color(r);  // TODO: dynamic?

            paint.setColor(c);

            x0 = (float) (cx + radius * sin(r + rot()));
            y0 = (float) (cy - radius * cos(r + rot()));

            x1 = (float) (cx + (radius + length) * sin(r + rot()));
            y1 = (float) (cy - (radius + length) * cos(r + rot()));

            canvas.drawLine(x0, y0, x1, y1, paint);
        }
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
    protected BitmapReuse getPetalBitmapX(Rect bounds) {
        Canvas canvas = bitmapForPetals.getCleanCanvas(bounds);

        //paint.reset();
        //paint.setColor(Color.BLACK);
        ////paint.setAlpha(0);
        //paint.setAntiAlias(true);
        ////paint.setColor(Color.TRANSPARENT);
        //paint.setStrokeWidth(1);
        //paint.setStyle(Paint.Style.FILL);
        ////paint.setAntiAlias(settings.getBackground() != 2);
        ////paint.clearShadowLayer();

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        int hd = hoursInDay() / 2;
        //int hc = hoursOnClock();

        //int half = hd / 2;
        //int qtr = hd / 4;

        float w = faceRadius(bounds);

        // TODO: spare option?
        //float r0 = sparse ? spotRadius(1.5f * hc) : spotRadius();
        float r0 = spotRadius(bounds, hd);
        float r1 = (w - r0) * 0.95f;
        //float r2 = r0 + (r1 * 1.15f);

        float x, y;
        double r;

        //float r2 = r0;
        //int[] tc = timeColors();

        paint.reset();
        paint.setColor(Color.BLACK);

        double off = (0.25 / hd);
        int i;

        // draw hours
        for (i = 0; i < hd; i = i + 2) {
            //paint.setColor(cc[i]);

            r = ((double) i) / hd + off;

            x = (float) (cx + r0 * sin(r + rot()));
            y = (float) (cy - r0 * cos(r + rot()));
            //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
            //y = (float) (cy - r0 * cos(r + rot() - 0.041666667));

            canvas.drawCircle(x, y, r1*2, paint);
        }

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));

        // draw hours
        for (i = 1; i < hd; i = i + 2) {
            //paint.setColor(cc[i]);

            r = ((double) i) / hd + off;

            x = (float) (cx + r0 * sin(r + rot()));
            y = (float) (cy - r0 * cos(r + rot()));
            //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
            //y = (float) (cy - r0 * cos(r + rot() - 0.041666667));

            canvas.drawCircle(x, y, r1 * 2, paint);
        }

        return bitmapForPetals;
    }
    */

    protected BitmapReuse getPetalBitmap(Rect bounds) {
        Canvas canvas = bitmapForPetals.getCleanCanvas(bounds);

        //canvas.drawColor(Color.RED);

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        int hd = hoursInDay();
        //int hc = hoursOnClock();
        int half = hd / 2;

        int a = settings.getAspect() + 1;

        // TODO: sparse option?
        // hd = sparse ? hd * 2 : hd;

        float[] wa = wheelArch(bounds, hd, a);
        float w = wa[0];    // max screen radius
        float r0 = wa[1];   // major radius
        float r1 = wa[2];   // minor radius

        drawAnalogBackground(canvas, r0);

        float x, y;
        double r;

        //float r2 = r0;
        //int[] tc = timeColors();

        //double off = (0.25 / hd);
        int i;

        PointF p = new PointF();

        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(false);
        paint.setStrokeWidth(2f);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        // draw hours
        for (i = 0; i < hd; i = i + 1) {
            //paint.setColor(cc[i]);

            r = ((double) i) / hd; //+ off;

            x = (float) (cx + r0 * sin(r + rot()));
            y = (float) (cy - r0 * cos(r + rot()));
            //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
            //y = (float) (cy - r0 * cos(r + rot() - 0.041666667));

            p.set(x, y);

            //canvas.drawCircle(x, y, r1, paint);

            // this takes the diameters, hence the 2x
            drawPetal(canvas, p, (float) r, 2 * r1 * a, 2 * r1, paint);
        }

        return bitmapForPetals;
    }

    /**
     * Draw a leaf/petal.
     *
     * @param canvas
     * @param p
     * @param angle
     * @param length
     * @param width
     * @param paint
     */
    protected void drawPetal(Canvas canvas, PointF p, double angle, float length, float width, Paint paint) {
        Path path = new Path();

        float radius = ((length * length) + (width * width)) / (4 * width);
        float theta = (float) (2 * Math.asin(length / (2 * radius)));

        float y0 = (float) (Math.cos(theta / 2) * (length / 2));
        float x0 = (float) (Math.cos(theta / 2) * (radius - (width / 2)));

        RectF rect = new RectF(p.x - radius, p.y - radius, p.x + radius, p.y + radius); //width, length);

        //Paint pa = new Paint();
        //pa.setColor(Color.WHITE);
        //pa.setStrokeWidth(2f);
        //pa.setStyle(Paint.Style.STROKE);
        //canvas.drawRect(rect, pa);

        double thetaf = (theta / (2 * Math.PI));

        // snap, can't reduce(angle...) b/c reduce is too stupid for negatives still, oh well still works as is
        float degrees = (float) ((angle - (thetaf / 2)) * 360.0);

        canvas.rotate(degrees, p.x, p.y);

        canvas.translate(-x0, -y0);
        path.reset();
        path.arcTo(rect, 0, (float) ((theta * 180) / Math.PI), false);
        path.close();
        canvas.drawPath(path, paint);
        canvas.translate(x0, y0);

        canvas.translate(x0, y0);
        path.reset();
        path.arcTo(rect, 180, (float) ((theta * 180) / Math.PI), false);
        path.close();
        canvas.drawPath(path, paint);
        canvas.translate(-x0, -y0);

        canvas.rotate(-degrees, p.x, p.y);
    }




    private void drawDiscrete(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        int hd = hoursInDay();
        //int hc = hoursOnClock();

        //int half = hd / 2;
        //int qtr = hd / 4;

        float w = faceRadius(canvas);

        // TODO: spare option?
        //float r0 = sparse ? spotRadius(1.5f * hc) : spotRadius();
        float r0 = spotRadius(canvas, hd);
        float r1 = w - r0;
        //float r2 = r0 + (r1 * 1.15f);

        float x, y;
        double r;

        int[] cc = clockColors();

        //int[] colors;
        //colors = clockColors(settings.isDuplexed() && !timeSystem.isDaySplit());
        //colors = hourColors();

        drawAnalogBackground(canvas, spotRadius(canvas));

        paint.reset();
        paint.setColor(0xFFFFFF);
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        //paint.setColor(Color.TRANSPARENT);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        //paint.setAntiAlias(settings.getBackground() != 2);
        //paint.setStrokeWidth(1.0f);
        //paint.clearShadowLayer();

        //int[] colors = timeColors();
        //canvas.drawColor(colors[0]);

        //int a = doubleUp ? hd * 2 : hd;

        float r2 = r0;

        // draw hours
        for (int i = 0; i < hd; i = i + 2) {
            paint.setColor(cc[i]);

            r = ((double) i) / hd;

            x = (float) (cx + r0 * sin(r + rot()));
            y = (float) (cy - r0 * cos(r + rot()));
            //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
            //y = (float) (cy - r0 * cos(r + rot() - 0.041666667));

            canvas.drawCircle(x, y, r1*2, paint);
        }

        // draw hours
        for (int i = 1; i < hd; i = i + 2) {
            paint.setColor(cc[i]);

            r = ((double) i) / hd;

            x = (float) (cx + r2 * sin(r + rot()));
            y = (float) (cy - r2 * cos(r + rot()));
            //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
            //y = (float) (cy - r0 * cos(r + rot() - 0.041666667));

            canvas.drawCircle(x, y, r1, paint);
        }

    }

    /** This was a test for drawing pedal using overlays **/
    public void drawTest(Canvas canvas) {
        float w = canvas.getWidth();
        float h = canvas.getHeight();

        // just used to set some proportions
        float off = 300f;

        Paint paint = new Paint();

        // make a background bitmap with a yellow to green gradient
        Bitmap bitmapBkg = Bitmap.createBitmap((int) w, (int) h, Bitmap.Config.ARGB_8888);
        Canvas canvasBkg = new Canvas(bitmapBkg);
        paint.reset();
        paint.setShader(new LinearGradient(0, h/2 - off, 0, h/2 + off, Color.YELLOW, Color.GREEN, Shader.TileMode.CLAMP));
        canvasBkg.drawRect(new RectF(0, 0, w, h), paint);

        // make a secondary overlay that cuts out the whole circles
        Bitmap bitmapOver2 = Bitmap.createBitmap((int) w, (int) h, Bitmap.Config.ARGB_8888);
        Canvas canvasOver2 = new Canvas(bitmapOver2);
        paint.reset();
        paint.setShader(new LinearGradient(0, h / 2 - off, 0, h / 2 + off, Color.RED, Color.MAGENTA, Shader.TileMode.CLAMP));
        canvasOver2.drawRect(new RectF(0, 0, w, h), paint);
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvasOver2.drawCircle(w / 2 - (off / 2), h / 2, off, paint);
        canvasOver2.drawCircle(w / 2 + (off / 2), h / 2, off, paint);

        // make an overlay bitmap with a red to magenta gradient which will have the cutouts
        Bitmap bitmapOver = Bitmap.createBitmap((int) w, (int) h, Bitmap.Config.ARGB_8888);
        Canvas canvasOver = new Canvas(bitmapOver);
        paint.reset();
        paint.setShader(new LinearGradient(0, h/2 - off, 0, h/2 + off, Color.RED, Color.MAGENTA, Shader.TileMode.CLAMP));
        canvasOver.drawRect(new RectF(0, 0, w, h), paint);

        // make a bitmap of intersecting circles to be used as the cutout shape
        Bitmap bitmapCut = Bitmap.createBitmap((int) w, (int) h, Bitmap.Config.ARGB_8888);
        Canvas canvasCut = new Canvas(bitmapCut);

        paint.reset();
        paint.setColor(Color.BLACK);
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvasCut.drawCircle(w / 2 - (off / 2), h / 2, off, paint);

        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvasCut.drawCircle(w / 2 + (off / 2), h / 2, off, paint);

        // apply cutout to overlay
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        canvasOver.drawBitmap(bitmapCut, 0, 0, paint);

        // draw background and overlay onto main canvas
        paint.reset();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmapBkg, 0, 0, paint);
        canvas.drawBitmap(bitmapOver2, 0, 0, paint);
        canvas.drawBitmap(bitmapOver, 0, 0, paint);
    }

}
