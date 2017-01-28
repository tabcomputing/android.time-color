package tabcomputing.tcwallpaper.bigtime;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.Log;

import tabcomputing.library.paper.FontScale;
import tabcomputing.tcwallpaper.BasePattern;

/**
 *
 */
public class PatternBigtime extends BasePattern {

    public PatternBigtime(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
        this.settings = wallpaper.getSettings();
    }

    private Settings settings;

    private TextPaint textPaint = new TextPaint();

    // font
    //private Typeface font;

    /*
    public void prepSettings() {
        propertyInteger(KEY_COLOR_GAMUT, 0);
        propertyBoolean(KEY_COLOR_DAYLIGHT, true);
        propertyBoolean(KEY_COLOR_DUPLEX, false);

        propertyInteger(KEY_TIME_SYSTEM, 0);
        //propertyBoolean(KEY_CLOCK_ROTATE, false);
        propertyBoolean(KEY_TIME_SECONDS, false);

        propertyInteger(KEY_TYPEFACE, 0);
    }
    */

    @Override
    public void drawPattern(Canvas canvas) {
        Rect canvasBounds = canvas.getClipBounds();

        float width  = canvasBounds.width();
        float height = canvasBounds.height();

        float centerX  = canvasBounds.width() / 2;

        int[] colors = timeColors();

        // -- draw background
        //canvas.drawColor(colors[0]);
        int[] rColors = rotate(colors, 1);
        LinearGradient shader = new LinearGradient(0, 0, 0, canvas.getHeight(), rColors, null, Shader.TileMode.CLAMP);
        paint.reset(); // paint = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);

        // --
        textPaint = digitalPaint();

        String[] time = timeSystem.timeRebased();

        float g = (height / colors.length);

        RectF rect = new RectF(0, 0, width, g);

        // we use time[1] here but we should probably create a sample segment method
        float estSize = textSizeForRect(time[1], rect, textPaint);
        textPaint.setTextSize(estSize);
        textPaint.setColor(colors[1]);

        Bitmap bmp;

        FontScale fs = settings.typefaceScale();

        float zx = width * fs.scaleX;
        float zy = g * fs.scaleY;

        float ox = width * fs.offsetX;
        float oy = g * fs.offsetY;

        for(int i = colors.length-1; i >= 0; i--) {
            textPaint.setColor(colors[i]);
            // TODO: Offset should probably go in textAsBitmap
            //bmp = textAsBitmap(time[i], ox, oy, textPaint);
            bmp = textAsBitmap(time[i], 10f, textPaint);
            bmp = Bitmap.createScaledBitmap(bmp, (int) zx, (int) zy, true);
            canvas.drawBitmap(bmp, 0 + ox, (g * i) + oy, textPaint);

            // this is the distance from the baseline to the center
            //float base = ((paint.descent() + paint.ascent()) / 2);

            //canvas.drawRect(0, g * i, width, g * (i + 1), paint);
            //canvas.drawText(time[i], centerX, g*(i+1), paint);
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

        Rect canvasBounds = canvas.getClipBounds();

        float width  = canvasBounds.width();
        float height = canvasBounds.height();

        //float width  = canvas.getWidth();
        //float height = canvas.getHeight();

        int[] tc = timeColors();

        canvas.drawColor(tc[0]);

        String sample = sampleTime();
        String stamp  = timeStamp();

        textPaint = digitalPaint();

        // TODO: make each part of the time the color it represents?
        textPaint.setColor(tc[1]);

        if (width > height) {
            //setTextSizeForWidth(textPaint, width, sample, 1.0f);
            textPaint.setTextSize(textSizeForWidth(sample, width, textPaint));
            // this is the distance from the baseline to the center
            float base = ((textPaint.descent() + textPaint.ascent()) / 2);

            canvas.drawText(stamp, width/2, height/2 - base, textPaint);
        } else {
            //setTextSizeForWidth(textPaint, height, sample, 1.0f);
            textPaint.setTextSize(textSizeForWidth(sample, height, textPaint));

            // TODO: think we need the inverse of the base, i.e. from the center to the top line

            // this is the distance from the baseline to the center
            float base = ((textPaint.descent() + textPaint.ascent()) / 2);

            canvas.save();
            canvas.rotate(-90);
            canvas.drawText(stamp, -height/2, (width/2 - base), textPaint);
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
     * @return      text paint
     */
    private TextPaint digitalPaint() {
        textPaint.reset();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(120.0f);
        textPaint.setStrokeWidth(3.0f);
        textPaint.setStyle(Paint.Style.FILL);
        //textPaint.setShadowLayer(3.0f, 0.0f, 0.0f, Color.BLACK);
        textPaint.setTypeface(settings.getTypeface());
        textPaint.setTextAlign(Paint.Align.CENTER);
        return textPaint;
    }

    /**
     * Get a string representation of the time.
     *
     * @return string of time
     */
    private String timeStamp() {
        return timeSystem.timeStamp();
    }

    //private Typeface getFont() {
    //    if (font == null) {
    //        font = Typeface.createFromAsset(context.getAssets(), "space.ttf");
    //    }
    //    return font;
    //}

}
