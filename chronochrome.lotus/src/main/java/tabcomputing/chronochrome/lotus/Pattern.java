package tabcomputing.chronochrome.lotus;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RadialGradient;
import android.graphics.Shader;

import tabcomputing.tcwallpaper.BasePattern;

/**
 * Lotus pattern is a ode to Hindi culture.
 */
public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    protected Settings settings;

    // This might well be the dumbest damn thing I have had to program.
    protected void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    @Override
    public void drawPattern(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float x, y;

        int hc = hoursOnClock();

        // rainbow of colors
        //int[] clockColors = clockColors();

        paint.reset();
        paint.setAntiAlias(true);

        int[] t = time();
        int h = t[0];

        int[] tc = timeColors();
        int[] clockColors = colorShades(tc[0], hc);

        double unit = (1.0 / hc);
        double half = unit / 2.0;

        float radius = cy;
        int k;
        double r;

        // TODO: want to rotation to be in whole units of `unit` or maybe `half`, who?
        //float rot = -(float)(handRatios()[0] - unit);
        float rot = (float) 0.5;

        RadialGradient gradient;

        //int trans = Color.alpha(Color.WHITE);

        canvas.drawColor(tc[1]); //Color.WHITE);

        int[] colorSpec = {Color.WHITE, Color.TRANSPARENT, Color.TRANSPARENT, tc[0]};

        for (int i = 0; i < hc; i++) {
            k = mod(h + i + 1, hc);
            r = (double) k / hc;

            paint.setColor(tc[1]); //clockColors[k]);
            paint.setAlpha(100);

            x = (float) (cx + radius * sin(r + rot));
            y = (float) (cy - radius * cos(r + rot));

            gradient = new RadialGradient(x, y, radius, colorSpec, null, Shader.TileMode.CLAMP);
            paint.setShader(gradient);

            canvas.drawCircle(x, y, cy, paint);

            x = (float) (cx + radius * sin(-r + rot));
            y = (float) (cy - radius * cos(-r + rot));

            gradient = new RadialGradient(x, y, radius, colorSpec, null, Shader.TileMode.CLAMP);
            paint.setShader(gradient);

            canvas.drawCircle(x, y, cy, paint);
        }

        if (settings.isBindi()) {
            drawTimeDots(canvas);
        }
    }

    protected void drawTimeDots(Canvas canvas) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        int[] colors = timeColors();
        int s = timeColors().length;
        double[] hr = handRatios();
        int hc = hoursOnClock();

        float m = min(cx, cy);
        float g = m / 12;
        //float w = m / s;
        float radius = m / 1.05f;

        double r;
        float x, y;

        paint.reset();
        paint.setAntiAlias(true);
        //paint.setShadowLayer(3f, 0f, 0f, Color.WHITE);

        for (int i = 0; i < s; i++) {
            //radius = (w * (i + 1)) - g;
            g = m / (8 + (i * 4));

            //r = (double) ((int) (hr[i] * hc)) / hc;
            //x = (float) (cx + radius * sin(r + rot()));
            //y = (float) (cy - radius * cos(r + rot()));

            paint.setColor(colors[i]);
            paint.setAlpha(255);
            //canvas.drawCircle(x, y, g, paint);
            canvas.drawCircle(cx, cy - radius, g, paint);
        }
    }

    @Override
    public float centerY(Canvas canvas) {
        return canvas.getHeight() / 2;
    }

    /**
     * x
     *
     *
     * @param color
     * @param bands
     * @return
     */
    public int[] colorShades(int color, int bands) {
        int[] colorBands = new int[bands];
        int half = (int) (((float) bands) / 2.0);
        //color = lighten(color, 0.25);
        for (int index = 0; index < bands; index++) {
            colorBands[index] = darken(color, (double) index / (double) bands / 2);
            //colorBands[index + half] = lighten(color, (double) index / (double) bands);
        }
        //colorBands = rotate(colorBands, half);
        return colorBands;
    }

    protected int darken(int color, double fraction) {
        int red   = (int) Math.round(Math.max(0, Color.red(color)   - 255 * fraction));
        int green = (int) Math.round(Math.max(0, Color.green(color) - 255 * fraction));
        int blue  = (int) Math.round(Math.max(0, Color.blue(color)  - 255 * fraction));

        int alpha = Color.alpha(color);

        return Color.argb(alpha, red, green, blue);
    }

    protected int lighten(int color, double fraction) {
        int red   = (int) Math.round(Math.min(255, Color.red(color)   + 255 * fraction));
        int green = (int) Math.round(Math.min(255, Color.green(color) + 255 * fraction));
        int blue  = (int) Math.round(Math.min(255, Color.blue(color)  + 255 * fraction));

        int alpha = Color.alpha(color);

        return Color.argb(alpha, red, green, blue);
    }

}
