package tabcomputing.tcwallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import tabcomputing.library.paper.AbstractPattern;

/**
 * Base Pattern class used by all wallpaper patterns.
 */
public class BasePattern extends AbstractPattern {

    private CommonSettings settings;

    protected void setSettings(CommonSettings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    /**
     *
     * @param key       key of preference that changed
     */
    public void preferenceChanged(String key) {
        //Log.d("log", "AbstractPattern.preferenceChanged: " + key);
        if (key.equals(CommonSettings.KEY_TIME_SYSTEM)) {
            timeSystem = settings.getTimeSystem();
        } else if (key.equals(CommonSettings.KEY_TYPEFACE)) {
            typeface = settings.getTypeface();
        } else if (key.equals(CommonSettings.KEY_COLOR_GAMUT)) {
            //colorWheel = settings.getColorWheel();
            colorWheel.setColorGamut(settings.getColorGamut());
        } else if (key.equals(CommonSettings.KEY_COLOR_DAYLIGHT)) {
            //colorWheel = settings.getColorWheel();
            colorWheel.setDaylightFactor(settings.isDaylight() ? 0.7 : 0.0);
        }
    }

    public void resetPreferences() {
        timeSystem = settings.getTimeSystem();
        colorWheel = settings.getColorWheel();
        typeface   = settings.getTypeface();
    }

    @Override
    protected ArrayList<Integer> getNagMessageList() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.string.bug_phrase_0);
        list.add(R.string.bug_phrase_1);
        list.add(R.string.bug_phrase_2);
        list.add(R.string.bug_phrase_3);
        list.add(R.string.bug_phrase_4);
        list.add(R.string.bug_phrase_5);
        return list;
    }

    @Override
    protected boolean withSeconds() {
        return settings.withSeconds();
    }

    /**
     * Outer radius of analog clock face.
     *
     * @return  outer radius of the clock face
     */
    protected float faceRadius(Canvas canvas) {
        return centerX(canvas) * 0.9f;
    }

    /**
     * Inner radius of analog clock face.
     *
     * @return  radius of the inner face circle
     */
    protected float spotRadius(Canvas canvas) {
        return spotRadius(canvas, hoursOnClock());
    }

    /**
     * @param h number of divisions
     * @return radius of central circle
     */
    protected float spotRadius(Canvas canvas, float h) {
        float w = faceRadius(canvas);
        return (float) (w / (1.0f + (2.0f * Math.sin(Math.PI / (2.0f * h)))));
    }

    /**
     * Midnight at bottom is default position.
     *
     * @return      ratio to rotate
     */
    protected double rot() {
        if (settings.isRotated()) {
            return 0.0; //Math.PI;
        } else {
            return 0.5;
        }
    }

    /**
     * Some patterns, if they are rather plain, can offer this as an option to spice
     * things up a bit.
     *
     * @param canvas    Canvas
     */
    public void drawSunGlare(Canvas canvas) {
        float x, y;

        int m = 5;
        int s = numberOfSegments();

        int[] t = time();

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        double w = Math.sqrt(cx * cx + cy * cy);

        double a, b;
        double d0, d1;

        double[] hr = handRatios();
        double r = hr[0];

        int[] colors = timeColors();

        paint.reset();
        paint.setAntiAlias(true);
        paint.setShadowLayer(2f, 0f, 0f, Color.WHITE);

        int[] g = getGlareList();

        for (int i = 0; i < m; i++) {
            int c = colors[mod(i, s)];

            if (mod(i, s+1) == 0) {
                c = colorWheel.complementaryColor(c);
            }

            paint.setColor(c);
            paint.setAlpha(64);
            //paint.setShadowLayer(2f, 0f, 0f, Color.WHITE);

            d0 = (w / 6f) * (i + 1);

            a = g[i];
            b = g[i+1];

            d1 = w / (2 * (a + ((b - a) * sin(t[0]))));

            x = (float) (cx + d0 * sin(r + rot()));
            y = (float) (cy - d0 * cos(r + rot()));

            // condition allows the glare circle not to be draw sometimes
            if (! (c == colors[0] && (mod((int)a,3) == 0))) {
                // TODO: maybe a radial gradient sometimes?
                canvas.drawCircle(x, y, (float) d1, paint);
            }

            a = g[i+2];
            b = g[i+3];

            d1 = w / (1.9 * (a + ((b - a) * sin(t[0]))));

            paint.setColor(c);
            paint.setAlpha(64); //196);
            //paint.setShadowLayer(2f, 0f, 0f, Color.WHITE);

            x = (float) (cx + d0 * sin(r + rot() + 0.5));
            y = (float) (cy - d0 * cos(r + rot() + 0.5));

            // condition allow the glare circle not to be draw sometimes
            if (! (c == colors[0] && (mod((int)a,3) == 0))) {
                canvas.drawCircle(x, y, (float) d1, paint);
            }
        }
    }

    private int[] glareList = null;

    /**
     * Generates a random list of numbers between 2 and 8 used to create sun glares.
     * The result is cached so it only happens once when the pattern is started,
     * which allows the pattern to change gradually over time instead of abruptly.
     *
     * @return  int list
     */
    protected int[] getGlareList() {
        if (glareList == null) {
            int size = 20;
            Random rand = new Random();
            int[] list = new int[size];
            for (int i = 0; i < size; i++) {
                list[i] = rand.nextInt(7) + 2;
                Log.d("log", "rand: " + list[i]);
            }
            glareList = list;
        }
        return glareList;
    }

    public void resetGlareList() {
        glareList = null;
    }

}
