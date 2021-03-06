package tabcomputing.tcwallpaper.radial;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;

import java.util.Arrays;

import tabcomputing.library.paper.BitmapReuse;
import tabcomputing.tcwallpaper.BasePattern;

/**
 *
 */
public class Pattern extends BasePattern {

    // TODO: Generalize to handle all time segments, not just hour and minute.

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());

        paint.reset();
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL);

        circlePaint.reset();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
    }

    private Settings settings;

    public void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    Paint circlePaint = new Paint();

    private BitmapReuse scratchBitmap = new BitmapReuse();


    @Override
    public void drawPattern(Canvas canvas) {
        Bitmap bmp;

        Rect bounds = new Rect();
        canvas.getClipBounds(bounds);

        int s = numberOfSegments();

        canvas.drawColor(Color.WHITE);

        if (settings.isReversed()) {
            for (int i = s - 1; i >= 0; i--) {
                bmp = getRadialBitmap(bounds, i, s - i - 1);
                canvas.drawBitmap(bmp, 0, 0, paint);
            }
        } else {
            for (int i = 0; i < s; i++) {
                bmp = getRadialBitmap(bounds, i, i);
                canvas.drawBitmap(bmp, 0, 0, paint);
            }
        }
    }

    /**
     *
     * @param bounds        canvas bounds
     * @param segment       time segment
     * @param band          time band
     * @return              color wheel bitmap
     */
    protected Bitmap getRadialBitmap(Rect bounds, int segment, int band) {
        if (settings.isSmooth()) {
            return getRadialSmoothBitmap(bounds, segment, band);
        } else {
            return getRadialDiscreteBitmap(bounds, segment, band);
        }
    }

    /**
     *
     * @param bounds        canvas bounds
     * @param segment       time segment
     * @param band          time band
     * @return              color wheel bitmap
     */
    protected Bitmap getRadialDiscreteBitmap(Rect bounds, int segment, int band) {
        Canvas canvas = scratchBitmap.getCleanCanvas(bounds);

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        // TODO: Get this to work for 12-hour clock too
        //int[] segments = clockSegments();
        //int[] colors = clockColors();

        int[] segments = timeSegments();
        int ticks = segments[segment];
        int[] colors = colorWheel.colors(ticks);

        // TODO: move clock rotation into the time system itself?
        int offset = (settings.isRotated() ? 90 : 270);

        float d;
        if (band > 0) {
            // radius fits within the screen
            float max = min(cx, cy) * 0.9f;
            // then radius is a power of 2, e.g. 1, 1/2, 1/4
            d = (float) (max / (Math.pow(2, band - 1)));
        } else {
            // radius is the center to the corner, actually a little further b/c of Y offset
            d = (float) cy + cy; //Math.sqrt(cx * cx + cy * cy);
        }

        RectF rect = new RectF(cx - d, cy - d, cx + d, cy + d);

        // adjusted time ratio
        float atr = reduce(1.0f - ((float) time()[segment] / ticks));

        float sweepAngle = 360.0f / ticks;
        float timeAngle  = 360.0f * atr;
        float startAngle = timeAngle - (sweepAngle / 2.0f) + offset;

        circlePaint.reset();

        for (int i = 0; i < ticks; i++) {
            circlePaint.setColor(colors[i]);
            canvas.drawArc(rect, startAngle, sweepAngle, true, circlePaint);
            startAngle = startAngle + sweepAngle;
        }

        return scratchBitmap.getBitmap();
    }

    /**
     *
     * @param bounds        canvas bounds
     * @param segment       time segment
     * @param band          time band
     * @return              color wheel bitmap
     */
    protected Bitmap getRadialSmoothBitmap(Rect bounds, int segment, int band) {
        Canvas canvas = scratchBitmap.getCleanCanvas(bounds);

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        // TODO: Get this to work for 12-hour clock too
        //int[] segments = clockSegments();
        //int[] colors = clockColors();

        int[] segments = timeSegments();
        int ticks = segments[segment];

        //int[] colors = colorWheel.colors(ticks);
        int[] colors = clockColors();

        colors = Arrays.copyOf(colors, colors.length + 1);
        colors[colors.length - 1] = colors[0];

        // TODO: move clock rotation into the time system itself?
        int offset = (settings.isRotated() ? 90 : 270);

        float d;
        if (band > 0) {
            // radius fits within the screen
            float max = min(cx, cy) * 0.9f;
            // then radius is a power of 2, e.g. 1, 1/2, 1/4
            d = (float) (max / (Math.pow(2, band - 1)));
        } else {
            // radius is the center to the corner, actually a little further b/c of Y offset
            d = (float) cy + cy; //Math.sqrt(cx * cx + cy * cy);
        }

        RectF rect = new RectF(cx - d, cy - d, cx + d, cy + d);

        //float sweepAngle = 360.0f / ticks;
        //float timeAngle  = sweepAngle * time()[segment];
        //float startAngle = timeAngle - (sweepAngle / 2.0f) + offset;

        double[] tr = timeRatios();
        double angle = tr[band];

        SweepGradient shader = new SweepGradient(cx, cy, colors, null);

        circlePaint.reset();
        circlePaint.setShader(shader);

        float rotation = (float) reduce(angle + 0.25) * 360f;

        canvas.rotate(-rotation, cx, cy);

        //for (int i = 0; i < ticks; i++) {
            //circlePaint.setColor(colors[i]);
            canvas.drawArc(rect, 0, 360, true, circlePaint);
            //startAngle = startAngle + sweepAngle;
        //}

        canvas.rotate(rotation, cx, cy);

        return scratchBitmap.getBitmap();
    }

    /**
     * Real center.
     *
     * @param canvas    drawing canvas
     * @return          center height
     */
    @Override
    public float centerY(Canvas canvas) {
        if (settings.isCentered()) {
            return canvas.getHeight() / 2;
        } else {
            return super.centerY(canvas);
        }
    }





    /**
     * TODO: to get better anti-aliasing, we should draw the circles as bitmaps and then draw them onto the background.
     *
     * @param canvas    drawing canvas
     */
    protected void drawRadial(Canvas canvas, int segment, int band) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        // TODO: Get this to work of 12-hour clock too
        //int[] segments = clockSegments();
        //int[] colors = clockColors();

        int[] segments = timeSegments();
        int ticks = segments[segment];
        int[] colors = colorWheel.colors(ticks);

        // TODO: move clock rotation into the time system itself?
        int offset = (settings.isRotated() ? 90 : 270);

        //int s = numberOfSegments();

        float d;

        if (band > 0) {
            // radius fits within the screen
            float max = min(cx, cy) * 0.9f;
            // then radius is a power of 2, e.g. 1, 1/2, 1/4
            d = (float) (max / (Math.pow(2, band - 1)));
        } else {
            // radius is the center to the corner
            d = (float) Math.sqrt(cx * cx + cy * cy);
        }

        RectF rect = new RectF(cx - d, cy - d, cx + d, cy + d);

        float sweepAngle = 360.0f / ticks;
        float timeAngle  = sweepAngle * time()[segment];
        float startAngle = timeAngle - (sweepAngle / 2.0f) + offset;

        for (int i = 0; i < ticks; i++) {
            circlePaint.setColor(colors[i]);
            canvas.drawArc(rect, startAngle, sweepAngle, true, circlePaint);
            startAngle = startAngle + sweepAngle;
        }
    }





    // DELETE  ----------->

    public void drawX(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        // TODO: Presently, this only does hours-in-day, fix to do hours-on-clock.
        int hc = hoursInDay();
        double[] hr = timeRatios();

        //int hc = hoursOnClock();
        //double[] hr = handRatios();

        int[] tc = timeColors();
        int[] ts = clockSegments();

        int i;

        //colors = handColors();
        int[] colors = clockColors(); //colorWheel.colors(hc);

        //paint.reset();  // new Paint();
        //paint.setAntiAlias(false);
        //paint.setStyle(Paint.Style.FILL);

        int offset;

        // TODO: move clock rotation into the time system itself?
        if (settings.isRotated()) {
            offset = 90;
        } else {
            offset = 270;
        }

        // --- OUTER CIRCLE ---

        float d = maxDimension(canvas);
        RectF rect = new RectF(cx - d, cy - d, cx + d, cy + d);

        float sweepAngle = 360.0f / hc;
        float timeAngle  = sweepAngle * time()[0];
        float startAngle = timeAngle - (sweepAngle / 2.0f) + offset;

        // draw wheel
        for (i = 0; i < hc; i++) {
            //r = ((double) i) / hc;
            paint.setColor(colors[i]);
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint);
            startAngle = startAngle + sweepAngle;
        }

        /*
        i = (settings.isReversed() ? 0 : 1);

        sweepAngle = 360.0f / ts[i];
        //startAngle = sweepAngle * (float) t - (sweepAngle / 2.0f);
        startAngle = (360.0f * (float) hr[i]) - (sweepAngle / 2.0f);

        paint.setColor(tc[i]);
        paint.setAlpha(255);

        circlePaint.setColor(tc[i]);
        circlePaint.setAlpha(255);

        //if (hr[i] == (int) hr[i]) {
        //paint.setShadowLayer(2.0f * (i + 1) + 10.0f, 0.0f, 0.0f, Color.BLACK);
        //}

        //canvas.drawArc(rect, startAngle - offset, sweepAngle, true, paint);
        //canvas.drawCircle(cx, cy, d * 0.25f, circlePaint);
        */

        // --- INNER CIRCLE ---
        drawInnerCircle(canvas);
    }

    protected void drawInnerCircle(Canvas canvas) {
        int[] tc = timeColors();
        int[] ts = clockSegments();

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        double[] hr = handRatios();

        int hc = ts[1];
        int[] colors = colorWheel.colors(hc);

        float d = canvas.getHeight() * 0.2f;
        RectF rect = new RectF(cx - d, cy - d, cx + d, cy + d);

        // TODO: move clock rotation into the time system itself
        int offset;
        if (settings.isRotated()) {
            offset = 90;
        } else {
            offset = 270;
        }

        offset = offset + (int) (360 * hr[1]);

        float sweepAngle = 360.0f / hc;
        float startAngle = 0.0f - (sweepAngle / 2.0f);

        int i;

        circlePaint.setColor(Color.WHITE);
        canvas.drawCircle(cx, cy, d, circlePaint);

        // draw wheel
        for (i = 0; i < hc; i++) {
            //r = ((double) i) / hc;
            paint.setColor(colors[i]);
            canvas.drawArc(rect, startAngle - offset, sweepAngle, true, paint);
            startAngle = startAngle + sweepAngle;
        }

        /*
        i = (settings.isReversed() ? 1 : 0);

        sweepAngle = 360.0f / ts[i];
        //startAngle = sweepAngle * (float) t - (sweepAngle / 2.0f);
        startAngle = (360.0f * (float) hr[i]) - (sweepAngle / 2.0f);

        //paint.setStyle(Paint.Style.FILL);
        paint.setColor(tc[i]);
        paint.setAlpha(255);
        //if (hr[i] == (int) hr[i]) {
        //paint.setShadowLayer(2.0f * (i + 1) + 10.0f, 0.0f, 0.0f, Color.BLACK);
        //}
        //canvas.drawArc(rect, startAngle - offset, sweepAngle, true, paint);

        circlePaint.setColor(tc[i]);
        circlePaint.setAlpha(255);
        //canvas.drawCircle(cx, cy, d/2.25f, circlePaint);
         */
    }

}
