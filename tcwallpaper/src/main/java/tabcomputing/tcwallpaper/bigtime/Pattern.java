package tabcomputing.tcwallpaper.bigtime;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.StaticLayout;
import android.text.TextPaint;

import tabcomputing.library.paper.AbstractPattern;

/**
 *
 */
public class Pattern extends AbstractPattern {

    public Pattern(Settings settings) {
        setSettings(settings);
    }

    //public PatternBigTime(Settings settings, TimeSystem timeSystem, ColorWheel colorWheel, Typeface font) {
    //    this.settings   = settings;
    //    this.timeSystem = timeSystem;
    //    this.colorWheel = colorWheel;
    //   this.font = font;
    //}


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

    //private Typeface font;

    @Override
    public void draw(Canvas canvas) {
        StaticLayout slLg;
        StaticLayout slSm;

        Rect canvasBounds = canvas.getClipBounds();

        float width  = canvasBounds.width();
        float height = canvasBounds.height();

        //float width  = canvas.getWidth();
        //float height = canvas.getHeight();

        int[] tc = timeColors();

        canvas.drawColor(tc[0]);

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
     *
     * @return      text paint
     */
    private Paint digitalPaint() {
        Paint paint = new TextPaint();

        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(120.0f);
        paint.setStrokeWidth(3.0f);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(10.0f, 0.0f, 0.0f, Color.BLACK);
        paint.setTypeface(typeface);
        //paint.setTypeface(Typeface.SANS_SERIF); // TODO: Need a skinny font
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
