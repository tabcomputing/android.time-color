package tabcomputing.chronochrome.space;

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
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import tabcomputing.tcwallpaper.BasePattern;

/**
 * Relic is based on a very odd font.
 */
public class Pattern extends BasePattern {

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


    @Override
    public void drawPattern(Canvas canvas) {
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
        // TODO: use BitmapReuse
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

        StaticLayout txt = new StaticLayout(msg, textPaint, cut, Layout.Alignment.ALIGN_NORMAL, lsm, lsa, true);

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
            font = Typeface.createFromAsset(context.getAssets(), "space.ttf");
        }
        return font;
    }

}
