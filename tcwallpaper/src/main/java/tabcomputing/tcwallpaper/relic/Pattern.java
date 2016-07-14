package tabcomputing.tcwallpaper.relic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.Arrays;

import tabcomputing.tcwallpaper.AbstractPattern;
import tabcomputing.tcwallpaper.FontScale;

/**
 *
 */
public class Pattern extends AbstractPattern {

    private Settings settings;

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
        this.settings = wallpaper.getSettings();
    }

    private Typeface font;

    private Typeface getFont() {
        if (font == null) {
            font = Typeface.createFromAsset(context.getAssets(), "abstract_futurelic.ttf");
        }
        return font;
    }

    /*
    public void prepSettings() {
        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        //propertyBoolean(KEY_TIME_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyInteger(KEY_TYPEFACE, 0);
    }
    */

    @Override
    public void draw(Canvas canvas) {
        if (settings.getStyle() == 0) {
            drawSmall(canvas);
        } else {
            drawLarge(canvas);
        }
    }

    public void drawSmall(Canvas canvas) {
        float cx = canvas.getWidth() / 2;
        float cy = canvas.getHeight() / 2;

        String msg = "";

        int[] colors = timeColors();
        if (! settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

        String[] time = timeSystem.timeRebased();

        for(int i = 0; i < 300; i++) {
            for(int j=0; j < colors.length; j++) {
                msg = msg + " " + time[j];
            }
            msg += " ";
        }

        canvas.drawColor(colors[0]);

        float cut = canvas.getWidth() * 1.25f;

        TextPaint paint = textPaint();

        paint.setTextSize(120.0f);
        paint.setColor(colors[1]);
        //paint.setShadowLayer(10.0f, 10.0f, 10.0f, Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.save();
        canvas.translate(cx, -100.0f);
        StaticLayout txt = new StaticLayout(msg, paint, (int) (cut), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);
        txt.draw(canvas);
        canvas.restore();

    }

    public void drawLarge(Canvas canvas) {
        Rect canvasBounds = canvas.getClipBounds();

        float width  = canvasBounds.width();
        float height = canvasBounds.height();

        float centerX  = canvasBounds.width() / 2;

        int[] colors = timeColors();

        if (! settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

        // background as blend of hour and minute color
        canvas.drawColor(Color.WHITE);

        String[] time = timeSystem.timeRebased();

        float g = (height / colors.length);

        RectF rect = new RectF(0, 0, width, g);

        TextPaint paint = textPaint();

        // we use time[1] here but we should probably create a sample segment method
        float estSize = textSizeForRect(time[1], rect, paint);
        paint.setTextSize(estSize);

        FontScale fs = new FontScale(0.1f, 0, 1, 1);

        Bitmap bmp;

        float zx = width * fs.scaleX;
        float zy = g * fs.scaleY;

        float ox = width * fs.offsetX;
        float oy = g * fs.offsetY;

        Paint offpaint = new Paint();

        for(int i = colors.length-1; i >= 0; i--) {
            // this is the distance from the baseline to the center
            //float base = ((paint.descent() + paint.ascent()) / 2);
            offpaint.setColor(colors[mod(i + 1, colors.length)]);
            canvas.drawRect(0, g * i, width, g * (i + 1), offpaint);
            //canvas.drawText(time[i], centerX, g*(i+1), paint);

            paint.setColor(colors[i]);
            bmp = textAsBitmap(time[i], ox, oy, paint);
            bmp = Bitmap.createScaledBitmap(bmp, (int) zx, (int) zy, true);
            canvas.drawBitmap(bmp, 0, (g * i), paint);
        }

        /*
        String sample = sampleTime();
        String stamp  = timeStamp();

        Paint paint = digitalPaint();

        // TODO: make each part of the time the color it represents?
        paint.setColor(tc[1]);

        if (width > height) {
            setTextSizeForWidth(paint, width, sample, 1.0f);

            // this is the distance from the baseline to the center
            float base = ((paint.descent() + paint.ascent()) / 2);

            canvas.drawText(stamp, width/2, height/2 - base, paint);
        } else {
            setTextSizeForWidth(paint, height, sample, 1.0f);

            // TODO: think we need the inverse of the base, i.e. from the center to the top line

            // this is the distance from the baseline to the center
            float base = ((paint.descent() + paint.ascent()) / 2);

            canvas.save();
            canvas.rotate(-90);

            canvas.drawText(stamp, -height/2, (width/2 - base), paint);

            canvas.restore();
        }
        */

        //canvas.drawText(h, x, y, textPaintStroke);

        //slLg = new StaticLayout(hour, digitalPaint, bounds.width(), Layout.Alignment.ALIGN_NORMAL, 1, 1, true);
        //slStroke = new StaticLayout(h, textPaintStroke, bounds.width(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);

        //canvas.translate(x, y);
        //slLg.draw(canvas);
        //slStroke.draw(canvas);
        //canvas.translate(-x, -y);

        //x = bounds.left + textWidth(hour, digitalPaint) + 2*px;
        //y = y + py;

        //String r = TextUtils.join(":", t.subList(1, t.size()));

        //slSm = new StaticLayout(r, textPaintSm, bounds.width(), Layout.Alignment.ALIGN_NORMAL, 1, 1, true);
        //slStroke = new StaticLayout(r, textPaintStroke, bounds.width(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);

        //canvas.translate(x, y);
        //slSm.draw(canvas);
        //slStroke.draw(canvas);
        //canvas.translate(-x, y);

    }

    /**
     * Draw time from bottom to top in a single line, i.e. time stamp.
     *
     * @param canvas
     */
    public void drawLongwise(Canvas canvas) {
        //StaticLayout slLg;
        //StaticLayout slSm;

        TextPaint paint = textPaint();

        Rect canvasBounds = canvas.getClipBounds();

        float width  = canvasBounds.width();
        float height = canvasBounds.height();

        //float width  = canvas.getWidth();
        //float height = canvas.getHeight();

        int[] tc = timeColors();

        canvas.drawColor(tc[0]);

        String sample = sampleTime();
        String stamp  = timeStamp();

        // TODO: make each part of the time the color it represents?
        paint.setColor(tc[1]);

        if (width > height) {
            setTextSizeForWidth(paint, width, sample, 1.0f);

            // this is the distance from the baseline to the center
            float base = ((paint.descent() + paint.ascent()) / 2);

            canvas.drawText(stamp, width/2, height/2 - base, paint);
        } else {
            setTextSizeForWidth(paint, height, sample, 1.0f);

            // TODO: think we need the inverse of the base, i.e. from the center to the top line

            // this is the distance from the baseline to the center
            float base = ((paint.descent() + paint.ascent()) / 2);

            canvas.save();
            canvas.rotate(-90);

            canvas.drawText(stamp, -height/2, (width/2 - base), paint);

            canvas.restore();
        }

        //canvas.drawText(h, x, y, textPaintStroke);

        //slLg = new StaticLayout(hour, digitalPaint, bounds.width(), Layout.Alignment.ALIGN_NORMAL, 1, 1, true);
        //slStroke = new StaticLayout(h, textPaintStroke, bounds.width(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);

        //canvas.translate(x, y);
        //slLg.draw(canvas);
        //slStroke.draw(canvas);
        //canvas.translate(-x, -y);

        //x = bounds.left + textWidth(hour, digitalPaint) + 2*px;
        //y = y + py;

        //String r = TextUtils.join(":", t.subList(1, t.size()));

        //slSm = new StaticLayout(r, textPaintSm, bounds.width(), Layout.Alignment.ALIGN_NORMAL, 1, 1, true);
        //slStroke = new StaticLayout(r, textPaintStroke, bounds.width(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);

        //canvas.translate(x, y);
        //slSm.draw(canvas);
        //slStroke.draw(canvas);
        //canvas.translate(-x, y);

    }

    /**
     * Get paint for text.
     *
     * @return          text paint
     */
    private TextPaint textPaint() {
        TextPaint paint = new TextPaint();

        Typeface font = getFont();

        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(120.0f);
        paint.setStrokeWidth(1.0f);
        paint.setStyle(Paint.Style.FILL);
        //paint.setShadowLayer(1.0f, 0.0f, 0.0f, Color.BLACK);
        paint.setTypeface(font);
        paint.setTextAlign(Paint.Align.CENTER);

        return paint;
    }

    /**
     * Get a string representation of the time.
     *
     * @return string of time
     */
    private String timeStamp() {
        return timeSystem.timeStamp(settings.displaySeconds());
    }

}
