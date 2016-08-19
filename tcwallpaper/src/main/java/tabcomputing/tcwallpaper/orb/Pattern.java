package tabcomputing.tcwallpaper.orb;

import android.graphics.Canvas;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

import tabcomputing.tcwallpaper.BasePattern;

/**
 * Draw a glowing orb.
 */
public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    // NOTE: You would think using @Override would be enough.
    protected Settings settings;

    // This might well be the dumbest damn thing I have had to program.
    protected void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    @Override
    public void drawPattern(Canvas canvas) {
        int[] colors = timeColors();

        canvas.drawColor(settings.isSwapped() ? colors[0] : colors[1]);

        if (settings.isOrbital()) {
            drawOrbit(canvas);
        } else {
            drawCenter(canvas);
        }

        if (settings.useGlare()) {
            drawSunGlare(canvas);
        }
    }

    /**
     * Draw orb in the center of the screen.
     *
     * @param canvas        drawing canvas
     */
    protected void drawCenter(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float m = min(cx, cy);

        int[] colors = timeColors();

        double tr = timeSystem.ratioTime();

        float y = (float)(canvas.getHeight() * Math.abs(sin(tr)));

        int size = settings.orbSize();

        // the hour color is on the outside in normal mode
        if (size == 0) {
            // draw the circular gradient
            colors = (settings.isSwapped() ? reverse(colors) : colors);
            float r = m * 1.5f;

            RadialGradient shader = new RadialGradient(cx, y, r, colors, null, Shader.TileMode.CLAMP);

            paint.reset(); // = new Paint();
            paint.setShader(shader);
            canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
        } else {
            // draw the orb
            int color = settings.isSwapped() ? colors[1] : colors[0];
            float r = m * 0.3f * size;

            paint.reset();
            paint.setColor(color);
            paint.setAntiAlias(true);
            paint.setShadowLayer(50f, 0, 0, color);
            canvas.drawCircle(cx, y, r, paint);
        }
    }

    /**
     * Draw orb in an orbit around an invisible clock face, as it were.
     *
     * @param canvas        drawing canvas
     */
    protected void drawOrbit(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float m = min(cx, cy);

        float r0 = spotRadius(canvas);

        double[] hr = handRatios();

        float hx = (float) (cx + r0 * sin(hr[0] + rot()));
        float hy = (float) (cy - r0 * cos(hr[0] + rot()));

        int[] colors = timeColors();

        int size = settings.orbSize();

        if (size == 0) {
            // draw the circular gradient
            colors = (settings.isSwapped() ? reverse(colors) : colors);
            float r = m * 1.5f;

            RadialGradient shader = new RadialGradient(hx, hy, r, colors, null, Shader.TileMode.CLAMP);
            paint.reset(); // = new Paint();
            paint.setShader(shader);
            canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
        } else {
            // draw the orb
            int color = settings.isSwapped() ? colors[1] : colors[0];
            float r = m * 0.3f * size;

            paint.reset();
            paint.setColor(color);
            paint.setAntiAlias(true);
            paint.setShadowLayer(50f, 0, 0, color);
            canvas.drawCircle(hx, hy, r, paint);
        }
    }

}
