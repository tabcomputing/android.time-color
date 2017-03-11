package tabcomputing.chronochrome.qbert;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import tabcomputing.tcwallpaper.BasePattern;

/**
 *
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

    public static final float[] SIZES = {60f, 120f, 180f, 240f};

    private Path path = new Path();


    /**
     * @param canvas    canvas instance
     */
    @Override
    public void drawPattern(Canvas canvas) {
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(6f);

        int[] colors = timeColors();

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        float size = SIZES[settings.getSize()];
        float space = 2 * size;

        float offset = size * 1.5f;

        boolean even = false;

        for (float y = 0; y < (h + space); y = y + offset) {
            for (float x = 0; x < (w + space); x = x + space) {
                if (even) {
                    drawCube(canvas, x - size, y, size, colors);
                } else {
                    drawCube(canvas, x, y, size, colors);
                }
            }
            even = !even;
        }
    }

    private void drawCube(Canvas canvas, float x, float y, float size, int[] colors) {

        float l = size;
        float h = l / 2;

        float x0 = x;     float y0 = y - l;
        float x1 = x + l; float y1 = y - h;
        float x2 = x + l; float y2 = y + h;
        float x3 = x;     float y3 = y + l;
        float x4 = x - l; float y4 = y + h;
        float x5 = x - l; float y5 = y - h;

        paint.setColor(colors[0]);

        path.reset();
        path.moveTo(x, y);
        path.lineTo(x5, y5);
        path.lineTo(x0, y0);
        path.lineTo(x1, y1);
        path.lineTo(x, y);
        path.close();
        canvas.drawPath(path, paint);

        paint.setColor(colors[1]);

        path.reset();
        path.moveTo(x, y);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        path.lineTo(x5, y5);
        path.lineTo(x, y);
        path.close();
        canvas.drawPath(path, paint);

        if (settings.withSeconds()) {
            paint.setColor(colors[2]);
        } else {
            // TODO: should this be a separate option?
            if (settings.isDaylight()) {
                // shade goes from white to black from noon to midnight?
                double tr = timeSystem.ratioTime();
                int c = Color.rgb((int)(255 * tr), (int)(255 * tr), (int)(255 * tr));
                paint.setColor(c); //Color.LTGRAY);
            } else {
                // TODO: prefer off-white tan
                paint.setColor(Color.LTGRAY);
            }
        }

        path.reset();
        path.moveTo(x, y);
        path.lineTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x, y);
        path.close();
        canvas.drawPath(path, paint);

        if (settings.isOutlined()) {
            paint.setColor(Color.BLACK);

            canvas.drawLine(x, y, x5, y5, paint);
            canvas.drawLine(x, y, x1, y1, paint);
            canvas.drawLine(x, y, x3, y3, paint);

            canvas.drawLine(x0, y0, x1, y1, paint);
            canvas.drawLine(x1, y1, x2, y2, paint);
            canvas.drawLine(x2, y2, x3, y3, paint);
            canvas.drawLine(x3, y3, x4, y4, paint);
            canvas.drawLine(x4, y4, x5, y5, paint);
            canvas.drawLine(x5, y5, x0, y0, paint);
        }
    }

}
