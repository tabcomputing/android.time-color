package tabcomputing.wallpaper.orb;

import android.graphics.Canvas;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

import tabcomputing.library.paper.AbstractPattern;

import java.util.Arrays;

/**
 * Draw a glowing orb.
 */
public class Pattern extends AbstractPattern {

    // NOTE: You would think using @Override would be enough.
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
        if (settings.isOrbital()) {
            drawOrbit(canvas);
        } else {
            drawCenter(canvas);
        }
    }

    public void drawCenter(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float rm = min(cx, cy);

        switch (settings.orbSize()) {
            case 0:
                rm = rm * 0.25f;
                break;
            case 1:
                rm = rm * 0.5f;
                break;
            case 2:
                rm = rm * 0.75f;
                break;
        }

        float gw = rm * 2.0f;

        int[] colors = timeColors();

        if (! settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

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

    public void drawOrbit(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float rm = min(cx, cy);

        switch (settings.orbSize()) {
            case 0:
                rm = rm * 0.25f;
                break;
            case 1:
                rm = rm * 0.5f;
                break;
            case 2:
                rm = rm * 0.75f;
                break;
        }

        float gw = rm * 2.0f;

        //float w = faceRadius(canvas);
        float r0 = spotRadius(canvas);
        //float r1 = w - r0;

        double[] r = timeSystem.handRatios();

        float hx = (float) (cx + r0 * sin(r[0] + rot()));
        float hy = (float) (cy - r0 * cos(r[0] + rot()));

        int[] colors = timeColors();

        if (! settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

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

        // draw the orb
        paint.reset(); // = new Paint();
        paint.setColor(colors[0]);
        paint.setAntiAlias(true);
        canvas.drawCircle(hx, hy, rm, paint);
    }

}
