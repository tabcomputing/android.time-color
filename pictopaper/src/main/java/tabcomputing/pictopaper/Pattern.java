package tabcomputing.pictopaper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import tabcomputing.library.paper.AbstractPattern;
import tabcomputing.library.paper.FontScale;

/**
 * Relic is based on a very odd font.
 */
public class Pattern extends AbstractPattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
        //this.settings = wallpaper.getSettings();
    }

    protected void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    // font sizes
    private static int[] SIZES = {50, 200, 300, 500};

    // private reference to settings
    private Settings settings;

    // shared text paint
    private TextPaint textPaint = new TextPaint();

    // font
    private Typeface font;


    // TODO
    public void preferenceChanged(String key) { };

    // TODO
    public void resetPreferences() { };


    @Override
    public void draw(Canvas canvas) {
        Rect bounds = new Rect();
        canvas.getClipBounds(bounds);

        if (settings.getFade()) {
            drawFade(canvas, true);
        } else {
            drawSolid(canvas, true);
        }

        Bitmap bmp = getBitmap(bounds);

        paint.reset();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bmp, 0, 0, paint);
    }

    /**
     * Generate a bitmap of bloop symbols.
     *
     * @param bounds    boundary size of canvas/bitmap
     * @return          bitmap
     */
    private Bitmap getBitmap(Rect bounds) {
        Bitmap bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        String msg = patternText();

        int cut = (int) (canvas.getWidth() * 1.25f);

        float lsm = 1.0f;  // line space multiple
        float lsa = 25.0f; // line space addition

        if (settings.getFade()) {
            drawFade(canvas, false);
        } else {
            drawSolid(canvas, false);
        }

        clearTextPaint();

        // TODO: DynamicLayout or StaticLayout? Also, can we cache this between draws?
        DynamicLayout txt = new DynamicLayout(msg, textPaint, cut, Layout.Alignment.ALIGN_NORMAL, lsm, lsa, true);

        canvas.translate(centerX(canvas), -300.0f);

        txt.draw(canvas);

        return bitmap;
    }

    /**
     * Create a pattern from patten text.
     *
     * @return      pattern string
     */
    private String patternText() {
        String pattern = settings.getPattern();
        String txt;

        if (pattern.isEmpty()) {
            txt = timeText();
        } else {
            txt = customText(pattern);
        }

        // TODO: do this smarter taking font size into account?
        int x = 100000 / (getSize() * (txt.length() + 1));

        StringBuilder msg = new StringBuilder();
        for(int i = 0; i < x; i++) {
            msg.append(txt);
        }
        return msg.toString();
    }

    /**
     * Custom pattern.
     *
     * @param pattern   pattern as given in settings
     * @return          pattern
     */
    private String customText(String pattern) {
        String[] time = timeRebased();
        for(int i=0; i < 6; i++) {
            pattern = pattern.replaceAll("#"+(i+1), (time.length > i ? time[i] : ""));
        }
        return pattern;
    }

    /**
     * Create a pattern from time.
     *
     * TODO: Maybe this should just include the hour, so it doesn't change so much.
     *
     * @return      pattern string
     */
    private String timeText() {
        String[] time = timeRebased();
        StringBuilder msg = new StringBuilder();
        for(String t : time) {
            msg.append(" ");
            msg.append(t);
        }
        return msg.toString();
    }

    /**
     * Configure shared textPaint for cut out.
     */
    private void clearTextPaint() {
        textPaint.reset();
        textPaint.setTypeface(getFont());
        textPaint.setTextSize(getSize());
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setColor(0xFFFFFF);
        textPaint.setAlpha(0);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.TRANSPARENT);
        textPaint.setStrokeWidth(1);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    private int getSize() {
        int size = settings.getSize();
        if (size < 0 || size > SIZES.length - 1) {
            return 0;
        }
        return SIZES[size];
    }

    private void drawFade(Canvas canvas, boolean reverse) {
        int[] c = timeColors();

        LinearGradient shader;

        if (reverse) {
            int[] colors = {c[0], c[1], c[0]};
            shader = new LinearGradient(0, 0, 0, canvas.getHeight(), colors, null, Shader.TileMode.CLAMP);
        } else {
            int[] colors = {c[1], c[0], c[1]};
            shader = new LinearGradient(0, 0, canvas.getWidth(), 0, colors, null, Shader.TileMode.CLAMP);
        }

        paint.reset();
        paint.setShader(shader);

        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }

    private void drawSolid(Canvas canvas, boolean reverse) {
        int[] colors = timeColors();

        if (reverse) {
            canvas.drawColor(colors[0]);
        } else {
            canvas.drawColor(colors[1]);
        }
    }

    private Typeface getFont() {
        if (font == null) {
            font = Typeface.createFromAsset(context.getAssets(), "abstract_evilz.ttf");
            //font = Typeface.createFromAsset(context.getAssets(), "futurelic.ttf");
        }
        return font;
    }













    public void drawRelicSmall(Canvas canvas) {
        float cx = canvas.getWidth() / 2;
        float cy = canvas.getHeight() / 2;

        String msg = "";

        int[] colors = timeColors();

        String[] time = timeSystem.timeRebased();

        for(int i = 0; i < 300; i++) {
            for(int j=0; j < colors.length; j++) {
                msg = msg + " " + time[j];
            }
            msg += " ";
        }

        canvas.drawColor(colors[0]);

        float cut = canvas.getWidth() * 1.25f;

        textPaint = textPaint();
        textPaint.setTextSize(fontSize());
        textPaint.setColor(colors[1]);
        //paint.setShadowLayer(10.0f, 10.0f, 10.0f, Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);

        canvas.save();
        canvas.translate(cx, -200.0f);
        StaticLayout txt = new StaticLayout(msg, textPaint, (int) (cut), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);
        txt.draw(canvas);
        canvas.restore();
    }

    public void drawRelicLarge(Canvas canvas) {
        Rect canvasBounds = canvas.getClipBounds();

        float width  = canvasBounds.width();
        float height = canvasBounds.height();

        float centerX  = canvasBounds.width() / 2;

        int[] colors = timeColors();

        //if (! settings.displaySeconds()) {
        //    colors = Arrays.copyOf(colors, colors.length - 1);
        //}

        // background as blend of hour and minute color
        canvas.drawColor(Color.WHITE);

        String[] time = timeSystem.timeRebased();

        float g = (height / colors.length);

        RectF rect = new RectF(0, 0, width, g);

        textPaint = textPaint();

        // we use time[1] here but we should probably create a sample segment method
        float estSize = textSizeForRect(time[1], rect, textPaint);
        textPaint.setTextSize(estSize);

        FontScale fs = new FontScale(0.1f, 0, 1, 1);

        Bitmap bmp;

        float zx = width * fs.scaleX;
        float zy = g * fs.scaleY;

        float ox = width * fs.offsetX;
        float oy = g * fs.offsetY;

        paint.reset();

        for(int i = colors.length-1; i >= 0; i--) {
            // this is the distance from the baseline to the center
            //float base = ((paint.descent() + paint.ascent()) / 2);
            paint.setColor(colors[mod(i + 1, colors.length)]);
            canvas.drawRect(0, g * i, width, g * (i + 1), paint);
            //canvas.drawText(time[i], centerX, g*(i+1), paint);

            textPaint.setColor(colors[i]);
            bmp = textAsBitmap(time[i], ox, oy, textPaint);
            bmp = Bitmap.createScaledBitmap(bmp, (int) zx, (int) zy, true);
            canvas.drawBitmap(bmp, 0, (g * i), textPaint);
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
     * @param canvas    drawing canvas
     */
    public void drawLongwise(Canvas canvas) {
        //StaticLayout slLg;
        //StaticLayout slSm;

        textPaint = textPaint();

        Rect canvasBounds = canvas.getClipBounds();

        float width  = canvasBounds.width();
        float height = canvasBounds.height();

        //float width  = canvas.getWidth();
        //float height = canvas.getHeight();

        int[] tc = timeColors();

        canvas.drawColor(tc[0]);

        String sample = sampleTime(!settings.displaySeconds());
        String stamp  = timeStamp(); // FIXME: sansSeconds too

        // TODO: make each part of the time the color it represents?
        textPaint.setColor(tc[1]);

        if (width > height) {
            setTextSizeForWidth(textPaint, width, sample, 1.0f);

            // this is the distance from the baseline to the center
            float base = ((textPaint.descent() + textPaint.ascent()) / 2);

            canvas.drawText(stamp, width/2, height/2 - base, textPaint);
        } else {
            setTextSizeForWidth(textPaint, height, sample, 1.0f);

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
     *
     * @return      font size
     */
    private float fontSize() {
        int index = settings.getSize();
        float size;
        if (index < SIZES.length) {
            size = SIZES[index];
        } else {
            size = SIZES[0];
        }
        return size;
    }

    /**
     * Get a string representation of the time.
     *
     * @return string of time
     */
    private String timeStamp() {
        return timeSystem.timeStamp(!settings.displaySeconds());
    }


    /**
     * Get paint for text.
     *
     * @return          text paint
     */
    private TextPaint textPaint() {
        textPaint.reset();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(120.0f);
        textPaint.setStrokeWidth(1.0f);
        textPaint.setStyle(Paint.Style.FILL);
        //textPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Color.BLACK);
        textPaint.setTypeface(getFont());
        textPaint.setTextAlign(Paint.Align.CENTER);
        return textPaint;
    }

    // font sizes
    private static final int[] RELIC_SIZES = {200, 300, 400};

}
