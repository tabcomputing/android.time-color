package tabcomputing.tcwallpaper.gradient;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

import tabcomputing.tcwallpaper.BasePattern;

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
public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    private Settings settings;
    protected void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    @Override
    public void draw(Canvas canvas) {
        switch (settings.getOrientation()) {
            case 2:
                drawOrbiting(canvas);
                break;
            case 1:
                drawVertical(canvas);
                break;
            default:
                drawHorizontal(canvas);
        }

        if (settings.useGlare()) {
            drawSunGlare(canvas);
        }
    }

    public void drawHorizontal(Canvas canvas) {
        int[] colors = timeColors();

        if (settings.isSwapped()) {
            reverseArray(colors);
        }

        LinearGradient shader = new LinearGradient(0, 0, 0, canvas.getHeight(), colors, null, Shader.TileMode.CLAMP);
        paint.reset(); // paint = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }

    public void drawVertical(Canvas canvas) {
        int[] colors = timeColors();

        //if (! settings.displaySeconds()) {
        //    colors = Arrays.copyOf(colors, colors.length - 1);
        //}

        if (settings.isSwapped()) {
            reverseArray(colors);
        }

        LinearGradient shader = new LinearGradient(0, 0, canvas.getWidth(), 0, colors, null, Shader.TileMode.CLAMP);
        paint.reset();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }

    public void drawOrbiting(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float w = faceRadius(canvas);
        float r0 = spotRadius(canvas);
        //float r1 = w - r0;

        double[] r = handRatios();
        int[] colors = timeColors();

        colors = Arrays.copyOf(colors, 2);  // just the first two colors

        if (settings.isSwapped()) {
            reverseArray(colors);
        }

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
        paint.reset();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }



    /**
     * @deprecated      use Orb pattern instead
     */
    public void drawCircularCenter(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float gw = min(cx, cy) * 2.0f;

        int[] colors = timeColors();

        //if (! settings.displaySeconds()) {
        //    colors = Arrays.copyOf(colors, colors.length - 1);
        //}

        // the hour color is on the outside in normal mode
        if (settings.isSwapped()) {
            reverseArray(colors);
        }

        // draw the circular gradient
        RadialGradient shader = new RadialGradient(cx, cy, gw, colors, null, Shader.TileMode.CLAMP);
        paint.reset(); // = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }

    /**
     * @deprecated      use Orb pattern instead
     */
    public void drawCircularOrbiting(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float gw = min(cx, cy);

        //float w = faceRadius(canvas);
        float r0 = spotRadius(canvas);
        //float r1 = w - r0;

        double[] r = handRatios();

        float hx = (float) (cx + r0 * sin(r[0] + rot()));
        float hy = (float) (cy - r0 * cos(r[0] + rot()));

        int[] colors = timeColors();

        //if (! settings.displaySeconds()) {
        //    colors = Arrays.copyOf(colors, colors.length - 1);
        //}

        // the hour color is on the inside in normal mode
        if (settings.isSwapped()) {
            reverseArray(colors);
        }

        //Paint paint;

        // draw the circular gradient
        RadialGradient shader = new RadialGradient(hx, hy, gw, colors, null, Shader.TileMode.CLAMP);
        paint.reset(); // = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }

}
