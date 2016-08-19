package tabcomputing.tcwallpaper.maze;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;

import tabcomputing.library.paper.FontScale;
import tabcomputing.library.paper.ScaledText;
import tabcomputing.tcwallpaper.BasePattern;

/**
 *
 */
public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
        this.settings = wallpaper.getSettings();
    }

    private Settings settings;

    private TextPaint textPaint = new TextPaint();

    // font
    private Typeface font;

    @Override
    public void drawPattern(Canvas canvas) {
        // get the canvas bounds
        Rect bounds = new Rect();
        canvas.getClipBounds(bounds);

        Bitmap bitmap;

        // get bitmap with timestamp in odd font
        bitmap = getBackdropBitmap(bounds);
        canvas.drawBitmap(bitmap, 0, 0, paint);


        // get bitmap with timestamp in odd font
        bitmap = getLongwiseBitmap(bounds);
        // draw bitmap
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }



    public void drawHourGlass(Canvas canvas) {
        Rect bounds = new Rect();
        canvas.getClipBounds(bounds);

        int[] colors = timeColors();

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);

        canvas.drawColor(colors[0]);

        Path path = new Path();

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        path.moveTo(0, 0);
        path.quadTo(cx, cy, 1, h);
        path.close();

        paint.setColor(colors[1]);
        canvas.drawPath(path, paint);

        path.moveTo(w, 0);
        path.quadTo(cx, cy, w, h);
        path.close();

        paint.setColor(colors[1]);
        canvas.drawPath(path, paint);
    }

    /**
     * Generate a bitmap of symbols.
     *
     * @param bounds    boundary size of canvas/bitmap
     * @return          bitmap
     */
    private Bitmap getBackdropBitmap(Rect bounds) {
        // TODO: use BitmapReuse
        Bitmap bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawColor(Color.RED);
        canvas.drawCircle(100f, 100f, 100f, paint);

        return bitmap;
    }

    private void drawFade(Canvas canvas, boolean reverse) {
        int[] c = timeColors();

        LinearGradient shader;

        float h = canvas.getHeight();
        float w = canvas.getWidth();

        Log.d("log", "width: " + w + " rev: " + reverse);

        if (reverse) {
            int[] colors = {c[0], c[1]};
            shader = new LinearGradient(0, 0, 0, h, colors, null, Shader.TileMode.CLAMP);
        } else {
            int[] colors = {c[1], c[0]};
            shader = new LinearGradient(0, 0, w, 0, colors, null, Shader.TileMode.CLAMP);
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

    /**
     * Configure shared textPaint for cut out.
     */
    private void clearTextPaint() {
        textPaint.reset();
        textPaint.setStrokeWidth(1);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(getFont());
        textPaint.setTextSize(150.0f); //getSize());
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.BLACK);
        //textPaint.setColor(0xFFFFFF);
        //textPaint.setAlpha(0);
        textPaint.setAntiAlias(true);
        textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }




    protected void drawBlocked(Canvas canvas) {
        Rect canvasBounds = canvas.getClipBounds();

        float width  = canvasBounds.width();
        float height = canvasBounds.height();

        float centerX  = canvasBounds.width() / 2;

        int[] colors = timeColors();

        // -- draw background
        //canvas.drawColor(colors[0]);
        int[] reverseColors = reverse(colors);
        LinearGradient shader = new LinearGradient(0, 0, 0, canvas.getHeight(), reverseColors, null, Shader.TileMode.CLAMP);
        paint.reset(); // paint = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);

        // --
        //textPaint = digitalPaint();
        clearTextPaint();

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
     * TODO: Make the strips long strips containing each each clock value and
     * translate teh pattern up and down according tot he time.
     * Hmm... this might be ard to do without a fixed size font.
     */

    /**
     * Draw time from bottom to top in a single line, i.e. time stamp.
     *
     * @param bounds    canvas bounds
     * @return          bitmap
     */
    protected Bitmap getLongwiseBitmap(Rect bounds) {
        float width = bounds.width();
        float height = bounds.height();

        //String sample = sampleTime();
        //String stamp = timeStamp();

        // TODO: use a BitmapReuse
        Bitmap bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Bitmap bmp;

        textPaint.reset();
        textPaint.setTypeface(getFont());
        textPaint.setTextSize(120f);

        int textColor;
        int shadeColor;

        textColor = Color.BLACK;
        shadeColor = Color.TRANSPARENT;
        textPaint.setColor(textColor);
        //textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        switch (settings.getBackdrop()) {
            case 0:
                canvas.drawColor(Color.BLACK);
                break;
            case 1:
                canvas.drawColor(Color.WHITE);
                break;
            default:
                drawRainbow(canvas);
        }

        if (settings.isFaded()) {
            textPaint.setShadowLayer(10f, 0, 0, textColor);
        }

        int blur = (settings.isFaded() ? 10 : 1);

        // this is the distance from the baseline to the center
        //float base = ((textPaint.descent() + textPaint.ascent()) / 2);

        float rotation = 0;

        if (height > width) {
            rotation = 90f;
            float temp = width;
            width = height;
            height = temp;
        }

        //height = height / 2;

        //bkg = Bitmap.createBitmap((int) width, (int) textHeight, Bitmap.Config.ARGB_8888);
        //Canvas bkgCanvas = new Canvas(bkg);
        //if (settings.getFade()) {
        //    drawFade(bkgCanvas, false);
        //} else {
        //    drawSolid(bkgCanvas, false);
        //}

        //if (width > height) {
        //    //setTextSizeForWidth(textPaint, width, sample, 1.0f);
        //    textPaint.setTextSize(textSizeForWidth(sample, width, textPaint));
//
//            FontScale fs = new FontScale(0, 0, 1, 1);
//            RectF s = fs.apply( width, height);
//
//            bmp = textAsBitmap(stamp, 0f, textPaint);
//            bmp = Bitmap.createScaledBitmap(bmp, (int) s.width(), (int) s.height(), false);
//            canvas.drawBitmap(bmp, width + s.left, 0 + s.top, textPaint);
//
//            canvas.drawText(stamp, width/2, height/2 - base, textPaint);
//        } else {
        //drawBackground(canvas, tc);

        String stamp = "";
        String pad = ".";

        float cx = centerX(bounds);
        float cy = centerX(bounds);

        int[] tc = timeColors();

        String[] tb = timeRebased();

        float textHeight = height / tc.length;

        int i;

        // every other color is shade color
        int[] linearColors = new int[tc.length * 2 + 1];
        for (i = 0; i < linearColors.length; i++) {
            linearColors[i] = shadeColor;
        }
        for (i = 0; i < tc.length; i++) {
            linearColors[2 * i + 1] = tc[i];
        }

        Shader shader = new LinearGradient(0, cy, bounds.right, cy, linearColors, null, Shader.TileMode.CLAMP);
        paint.reset();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, bounds.right, bounds.bottom), paint);

        canvas.rotate(rotation, bounds.width(), 0);

        for (i = 0; i < tc.length; i++) {
            pad = (tb[i].length() > 2 ? "." : "..");
            // TODO: probably can reuse this object instead of creating a new one each time
            ScaledText scaledText = new ScaledText();
            scaledText.setText(pad + tb[i] + pad);
            scaledText.setScale(0, 0, 1, 1);
            scaledText.setPadding(10f);

            bmp = scaledText.toBitmap(width, textHeight, textPaint);

            for (int n = 0; n < blur; n++) {
                canvas.drawBitmap(bmp, bounds.width(), (height / tc.length) * (tc.length - i - 1), paint);
            }
        }

        canvas.rotate(-rotation, bounds.width(), 0);

        return bitmap;
    }

    protected void drawRainbow(Canvas canvas) {
        int[] colors = clockColors();

        LinearGradient shader = new LinearGradient(0, 0, 0, canvas.getHeight(), colors, null, Shader.TileMode.CLAMP);
        paint.reset();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }

    /**
     * Get shade of sky blue given the time of day.
     */
    protected int skyBlue() {
        double ratio = timeSystem.ratioTime();
        float[] hsv = {180f, 1f, (float) sin(reduce(ratio / 2))};
        return Color.HSVToColor(hsv);
    }

    @Override
    public Bitmap textAsBitmap(String text, float padding, Paint paint) {
        paint.setTextAlign(Paint.Align.LEFT);

        //Paint.FontMetrics fm = paint.getFontMetrics();
        float baseline = -paint.ascent(); // ascent() is negative

        int width  = (int) (paint.measureText(text) + (2*padding));
        int height = (int) (baseline + paint.descent() + (2*padding));

        // TODO: use BitmapReuse
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        //canvas.drawColor(tc[0]);
        if (settings.isFaded()) {
            drawFade(canvas, false);
        } else {
            drawSolid(canvas, false);
        }

        canvas.drawText(text, padding, baseline + padding, paint);
        return bmp;
    }



    /**
     * Get paint for text.
     *
     * @return      text paint
     *
    private TextPaint digitalPaint() {
        textPaint.reset();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(120.0f);
        textPaint.setStrokeWidth(3.0f);
        textPaint.setStyle(Paint.Style.FILL);
        //textPaint.setShadowLayer(8.0f, -5.0f, -5.0f, Color.BLACK);
        textPaint.setTypeface(getFont());
        textPaint.setTextAlign(Paint.Align.CENTER);
        return textPaint;
    }
    */

    /**
     * Get a string representation of the time.
     *
     * @return string of time
     */
    private String timeStamp() {
        return timeSystem.timeStamp(settings.sansSeconds());
    }

    private Typeface getFont() {
        if (font == null) {
            font = Typeface.createFromAsset(context.getAssets(), "maze.ttf");
        }
        return font;
    }

    private void drawBackground(Canvas canvas, int[] colors) {
        //if (settings.isFade()) {

        int[] reverseColors = reverse(colors);
        LinearGradient shader = new LinearGradient(0, 0, 0, canvas.getHeight(), reverseColors, null, Shader.TileMode.CLAMP);
        paint.reset(); // paint = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);

        // -- draw background
        //canvas.drawColor(colors[0]);
    }

    @Override
    protected void drawCheatClock(Canvas canvas) {
        // do nothing
    }

}
