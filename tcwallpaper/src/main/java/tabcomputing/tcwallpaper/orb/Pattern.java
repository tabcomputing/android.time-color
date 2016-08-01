package tabcomputing.tcwallpaper.orb;

import android.graphics.Canvas;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

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
    public void draw(Canvas canvas) {
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
    public void drawCenter(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float[] d = ordDim();
        float m = min(cx, cy);
        float rm = m * d[0];
        float gw = m * d[1];

        int[] colors = timeColors();

        // the hour color is on the outside in normal mode
        if (settings.isSwapped()) {
            reverseArray(colors);
        }

        // draw the circular gradient
        RadialGradient shader = new RadialGradient(cx, cy, gw, colors, null, Shader.TileMode.CLAMP);
        paint.reset(); // = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);

        // draw the orb
        paint.reset(); // = new Paint();
        paint.setColor(colors[0]);
        paint.setAntiAlias(true);
        canvas.drawCircle(cx, cy, rm, paint);
    }

    /**
     * Draw orb in an orbit around an invisible clock face, as it were.
     *
     * @param canvas        drawing canvas
     */
    public void drawOrbit(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float[] d = ordDim();
        float m = min(cx, cy);
        float rm = m * d[0];
        float gw = m * d[1];

        //float w = faceRadius(canvas);
        float r0 = spotRadius(canvas);
        //float r1 = w - r0;

        double[] r = handRatios();

        float hx = (float) (cx + r0 * sin(r[0] + rot()));
        float hy = (float) (cy - r0 * cos(r[0] + rot()));

        int[] colors = timeColors();

        // the hour color is on the inside in normal mode
        if (settings.isSwapped()) {
            reverseArray(colors);
        }

        // draw the circular gradient
        RadialGradient shader = new RadialGradient(hx, hy, gw, colors, null, Shader.TileMode.CLAMP);
        paint.reset(); // = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);

        // draw the orb
        paint.reset(); // = new Paint();
        paint.setColor(colors[0]);
        paint.setAntiAlias(true);
        canvas.drawCircle(hx, hy, rm, paint);
    }

    /**
     * Size of orb option. It would be nice if this could be a sliding scale, but
     * also Android preference doesn't have such a field built-in.
     *
     * @return      factors for circle radius and gradient radius.
     */
    private float[] ordDim() {
        float[] d = new float[2];
        switch (settings.orbSize()) {
            case 0:
                d[0] = 0.01f;
                d[1] = 1.10f;
                break;
            case 1:
                d[0] = 0.25f;
                d[1] = 0.50f;
                break;
            case 2:
                d[0] = 0.5f;
                d[1] = 1.0f;
                break;
            case 3:
                d[0] = 0.75f;
                d[1] = 1.50f;
                break;
        }
        return d;
    }

}
