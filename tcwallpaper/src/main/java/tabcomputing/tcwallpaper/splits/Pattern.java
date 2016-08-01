package tabcomputing.tcwallpaper.splits;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import tabcomputing.tcwallpaper.BasePattern;

/**
 * Split the screen vertical, horizontal or along a line the runs tangent to the current
 * hour on an invisible clock face.
 */
public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    private Settings settings;

    public void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        switch(settings.getOrientation()) {
            case 2:
                drawRotating(canvas);
                break;
            case 1:
                drawVertical(canvas);
                break;
            default:
                drawHorizontal(canvas);
                break;
        }
    }

    private static final int[] SIZES = { 4, 3, 2, 1 };

    public void drawHorizontal(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        int[] colors = timeColors();

        int n = SIZES[settings.getSize()];
        float g = h / (n * colors.length);

        float top = 0;
        for(int j=0; j < n; j++) {
            for (int c : colors) {
                paint.setColor(c);
                canvas.drawRect(0, top, w, top + g, paint);
                top = top + g;
            }
        }
    }

    public void drawVertical(Canvas canvas) {
        float w = canvas.getWidth();
        float h = canvas.getHeight();

        int[] colors = timeColors();

        int n = SIZES[settings.getSize()];
        float g = w / (n * colors.length);

        paint.reset();
        paint.setStyle(Paint.Style.FILL);

        float left = 0;
        for(int j=0; j < n; j++) {
            for (int c : colors) {
                paint.setColor(c);
                canvas.drawRect(left, 0, left + g, h, paint);
                left = left + g;
            }
        }
    }

    /**
     * Draw strips, rotating at angle of the hour.
     */
    public void drawRotating(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        float cx = canvas.getWidth() / 2;
        float cy = canvas.getHeight() / 2;

        float w = canvas.getWidth();
        float h = canvas.getHeight();
        //float d = (float) Math.sqrt(w*w + h*h);
        float d = (w + h) / 2;  // average

        int[] colors = timeColors();

        // FIXME: shouldn't we be using timeRatio -- but then 12hr vs 24hr clock?
        double[] hr = handRatios();

        int n = SIZES[settings.getSize()];
        float g = d / (n * colors.length);

        float left = -g * colors.length;

        float rotation = (float) (hr[0] * 360);
        float shift = -(d - w) / 2;

        canvas.rotate(rotation, cx, cy);
        canvas.translate(shift, 0);

        for(int j = -1; j <= n; j++) {
            for (int i = 0; i < colors.length; i++) {
                paint.setColor(colors[i]);
                canvas.drawRect(left, -h, left + g, h*2.0f, paint);
                left = left + g;
            }
        }

        canvas.translate(-shift, 0);
        canvas.rotate(-rotation);
    }

}