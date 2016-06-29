package tabcomputing.wallpaper.gradient;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import tabcomputing.library.paper.AbstractPattern;

import java.util.Arrays;

/**
 * Draw a vertical color gradient such that the top color represents the hour,
 * and the bottom the minute or seconds. If seconds are included in settings then
 * they will be at the bottom, otherwise minutes will be. Some time systems
 * have an extra time segment; in those cases there will be an additional middle
 * color in the gradations.
 *
 * The orbiting pattern is similar, but instead of running vertically it
 * runs at an angle perpendicular to the current hour on the clock face (even if
 * the clock face is not actually displayed). The hour color can be distinguished from
 * the minute color as it is the larger of the two and takes up most of the center of
 * the screen.
 *
 * TODO: Add seconds.
 *   The orbital gradient only accounts for the first two time segments
 *   (e.g. hours and minutes); the rest are omitted.
 */
public class Pattern extends AbstractPattern {

    protected Settings settings;

    public Pattern(Settings settings) {
        setSettings(settings);
    }

    // This might well be the dumbest damn thing I have had to program.
    protected void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    @Override
    public void draw(Canvas canvas) {
        if(settings.isOrbital()) {
            drawOrbiting(canvas);
        } else {
            drawVertical(canvas);
        }
    }

    public void drawVertical(Canvas canvas) {
        int[] colors = timeColors();

        if (! settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

        if (settings.isSwapped()) {
            reverseArray(colors);
        }

        LinearGradient shader = new LinearGradient(0, 0, 0, canvas.getHeight(), colors, null, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }

    public void drawOrbiting(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float w = faceRadius(canvas);
        float r0 = spotRadius(canvas);
        //float r1 = w - r0;

        double[] r = timeSystem.handRatios();

        int[] colors = timeColors();

        colors = Arrays.copyOf(colors, 2);  // just the first two colors

        if (settings.isSwapped()) {
            reverseArray(colors);
        }

        //if (!settings.displaySeconds()) {
        //    colors = Arrays.copyOf(colors, colors.length - 1);
        //}

        // -- hour --
        //int hc = ratioToColor(r[0]);
        int hc = colors[0];

        //float hx = (float) (cx + r0 * sin(hr + rot()));
        //float hy = (float) (cy - r0 * cos(hr + rot()));

        float rx = (float) (cx + (w - cy) * sin(r[0] + rot() + 1));
        float ry = (float) (cy - (w - cy) * cos(r[0] + rot() + 1));

        float fx = (float) (cx + cy * sin(r[0] + rot() + 1));
        float fy = (float) (cy - cy * cos(r[0] + rot() + 1));

        // -- minutiae --
        //int mc = ratioToColor(r[1]);
        int mc = colors[1];

        //float mx = (float) (cx + r0 * sin(mr + rot()));
        //float my = (float) (cy - r0 * cos(mr + rot()));

        LinearGradient shader = new LinearGradient(rx, ry, fx, fy, hc, mc, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }

}