package tabcomputing.tcwallpaper.stripes;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Arrays;

import tabcomputing.tcwallpaper.AbstractPattern;

/**
 * TODO: Draw series of stripes.
 *
 * Split the screen along a line the runs tangent to an (invisible) clock face.
 */
public class Pattern extends AbstractPattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    @Override
    public void draw(Canvas canvas) {
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

    public void drawHorizontal(Canvas canvas) {
        //float cx = centerX(canvas);
        //float cy = centerY(canvas);

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        int[] colors = timeColors();

        if (! settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

        float g = (h / colors.length);

        paint.reset();
        paint.setStyle(Paint.Style.FILL);

        for(int i=0; i < colors.length; i++) {
            paint.setColor(colors[i]);
            canvas.drawRect(0, g * i, w, g * (i + 1), paint);;
        }
    }

    public void drawVertical(Canvas canvas) {
        //float cx = centerX(canvas);
        //float cy = centerY(canvas);

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        int[] colors = timeColors();

        if (! settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

        float g = (w / colors.length);

        paint.reset();
        paint.setStyle(Paint.Style.FILL);

        for(int i=0; i < colors.length; i++) {
            paint.setColor(colors[i]);
            canvas.drawRect(g * i, 0, g * (i + 1), h, paint);
        }

    }

    /**
     * Draw strips, rotating at angle of the hour.
     */
    public void drawRotating(Canvas canvas) {

        float cx = canvas.getWidth() / 2;
        float cy = canvas.getHeight() / 2;

        float w = canvas.getWidth();
        float h = canvas.getHeight();
        float d = (float) Math.sqrt(w*w + h*h);

        int[] colors = timeColors();

        if (!settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

        // FIXME: hand ratios is off by 30 minutes
        //        shouldn't we be using timeRatio -- but then 12hr vs 24hr clock?
        double[] r = timeSystem.handRatios();

        canvas.save();
        canvas.rotate((float) -(r[0] * 360), cx, cy);

        float g = (d / colors.length);

        paint.reset();
        paint.setStyle(Paint.Style.FILL);

        for(int i=0; i < colors.length; i++) {
            paint.setColor(colors[i]);
            canvas.drawRect((g * i) - (w/2), -h, (g * (i + 1)) - (w/2), h*2, paint);
        }

        //canvas.rotate((float) -(r[0] * 360));
        canvas.restore();
    }

}