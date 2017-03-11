package tabcomputing.chronochrome.checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import tabcomputing.library.paper.BasePattern;

/**
 *
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

    public static final float[] SIZES = {9.5f, 5.5f, 1.5f, 1.0f};


    /**
     * @param canvas    canvas instance
     */
    @Override
    public void drawPattern(Canvas canvas) {
        int[] colors = timeColors();

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        float size = w / SIZES[settings.getSize()];

        canvas.drawColor(Color.BLACK);

        //if (settings.isSwapped()) { reverseArray(colors); }

        if (settings.isFade()) {
            LinearGradient shader = new LinearGradient(0, 0, 0, canvas.getHeight(), colors, null, Shader.TileMode.CLAMP);
            paint.reset();
            paint.setShader(shader);
            canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
        } else {
            canvas.drawColor(Color.WHITE);
        }

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(6f);

        for (float y = 0; y < h; y = y + size) {
            for (float x = 0; x < w; x = x + size) {
                if (settings.isFade()) {
                    drawHollowSquare(canvas, x, y, size, colors);
                } else {
                    drawSquare(canvas, x, y, size, colors);
                }
            }
        }
    }

    private void drawSquare(Canvas canvas, float x, float y, float size, int[] colors) {
        float s = size / 2;

        float x0 = x;     float y0 = y;
        float x1 = x + s; float y1 = y;
        float x2 = x + s; float y2 = y + s;
        float x3 = x;     float y3 = y + s;

        paint.setColor(colors[0]);

        canvas.drawRect(x, y, x + s, y + s, paint);
        canvas.drawRect(x + s, y + s, x + s + s, y + s + s, paint);

        paint.setColor(colors[1]);

        canvas.drawRect(x + s, y, x + s + s, y + s, paint);
        canvas.drawRect(x, y + s, x + s, y + s + s, paint);

        //if (settings.displaySeconds()) {
        //    paint.setColor(colors[2]);
        //} else {
        //    // todo: which or option?
        //    paint.setColor(Color.BLACK);
        //    paint.setColor(Color.LTGRAY);
        //}
    }

    private void drawHollowSquare(Canvas canvas, float x, float y, float size, int[] colors) {
        float s = size / 2;

        float x0 = x;     float y0 = y;
        float x1 = x + s; float y1 = y;
        float x2 = x + s; float y2 = y + s;
        float x3 = x;     float y3 = y + s;

        paint.setColor(colors[0]);

        canvas.drawRect(x, y, x + s, y + s, paint);

        paint.setColor(colors[1]);

        canvas.drawRect(x + s, y + s, x + s + s, y + s + s, paint);

        //if (settings.displaySeconds()) {
        //    paint.setColor(colors[2]);
        //} else {
        //    // todo: which or option?
        //    paint.setColor(Color.BLACK);
        //    paint.setColor(Color.LTGRAY);
        //}
    }

}
