package tabcomputing.wallpaper.plaid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import tabcomputing.library.paper.AbstractPattern;

public class Pattern extends AbstractPattern {

    Settings settings;

    public Pattern(Settings settings) {
        setSettings(settings);
    }

    protected void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    @Override
    public void draw(Canvas canvas) {
        if (settings.isInterlaced()) {
            drawInterlaced(canvas);
        } else {
            drawRegular(canvas);
        }
    }

    // TODO: Third pattern that draws hours and minutes perpendicular to other?

    /**
     * Draw plaid design. This is based on binary stripes pattern. To get the plaid effect it
     * simply overlays another copy of the stripes orthogonal to the first with using alpha
     * transparency.
     */
    protected void drawRegular(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(minuteColor());
        paint.setAntiAlias(true);
        //paint.setStrokeWidth(10.0f);
        //paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        //paint.setShadowLayer(5.0f, 1.0f, 1.0f, Color.LTGRAY);

        float wx = canvas.getWidth();
        float wy = canvas.getHeight();

        int[] t = timeSystem.time();
        int[] s = timeSystem.timeSegments();

        int e = t.length;
        if (!settings.displaySeconds()) {
            e = e - 1;
        }

        float x = 0;

        int[] colors = timeColors();

        float w = wx / e;

        canvas.drawColor(Color.WHITE);

        int b, f, i;
        float d, g;

        for(i=0; i < e; i++) {
            // number of bits required to represent the time segment
            b = log2(s[i]) + 1;

            d = w / b;
            g = d / 2;

            x = (w * i) + g;

            // draw background
            paint.setColor(colorWheel.alphaColor(colors[i], 0.6));
            canvas.drawRect(w * i, 0, (w * i) + w, wy, paint);

            paint.setStrokeWidth(d);
            //paint.setColor(colors[i]);

            for (int j=0; j < b; j++) {
                f = (int) Math.pow(2, b - j - 1);
                if ((t[i] & f) == f) {
                    canvas.drawLine(x + (d * j), 0, x + (d * j), wy, paint);
                } else {
                    //canvas.drawLine(x + (d * j), 0, x + (d * j), wy, paint);
                }
            }
        }

        float h = wy / e;
        float y;

        for(i=0; i < e; i++) {
            //i = mod(k, e);

            b = log2(s[i]) + 1;

            d = h / b;
            g = d / 2;

            y = (h * i) + g;

            // TODO: paint.setAlpha()
            paint.setColor(colorWheel.alphaColor(colors[i], 0.6));

            canvas.drawRect(0, h * i, wx, (h * i), paint);

            paint.setStrokeWidth(d);
            //paint.setColor(colors[i]);

            for (int j=0; j < b; j++) {
                f = (int) Math.pow(2, b - j - 1);
                if ((t[i] & f) == f) {
                    canvas.drawLine(0, y + (d * j), wx, y + (d * j), paint);
                }
            }
        }
    }

    /**
     * A slightly better design that weaves the strips together.
     */
    public void drawInterlaced(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(minuteColor());
        paint.setAntiAlias(true);
        //paint.setStrokeWidth(10.0f);
        //paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        //paint.setShadowLayer(5.0f, 1.0f, 1.0f, Color.LTGRAY);

        float wx = canvas.getWidth();
        float wy = canvas.getHeight();

        int[] t = timeSystem.time();
        int[] s = timeSystem.timeSegments();

        int e = t.length;
        if (!settings.displaySeconds()) {
            e = e - 1;
        }

        int[] colors = timeColors();

        float w = wx / e;
        float h = wy / e;

        canvas.drawColor(Color.WHITE);

        int i, b, f;
        float d, g;

        float x = 0;
        float y = 0;

        int[] remains = new int[e];

        // horizontal

        for(i=0; i < e; i++) {
            remains[i] = log2(s[i]) + 1;
        }

        while(sum(remains) > 0) {
            i = maxIndex(remains);

            b = log2(s[i]) + 1;

            d = w / b;
            g = d / 2;

            f = (int) Math.pow(2, remains[i] - 1); //b - j - 1);

            paint.setStrokeWidth(d);
            paint.setColor(colors[i]);

            if ((t[i] & f) == f) {
                paint.setAlpha(150);
                canvas.drawLine(x + g, 0, x + g, wy, paint);
            } else {
                paint.setAlpha(64);
                canvas.drawLine(x + g, 0, x + g, wy, paint);
            }

            remains[i]--;

            x = x + d;
        }

        // vertical

        for(i=0; i < e; i++) {
            remains[i] = log2(s[i]) + 1;
        }

        while(sum(remains) > 0) {
            i = maxIndex(remains);

            b = log2(s[i]) + 1;

            d = h / b;
            g = d / 2;

            f = (int) Math.pow(2, remains[i] - 1); //b - j - 1);

            paint.setStrokeWidth(d);
            paint.setColor(colors[i]);

            if ((t[i] & f) == f) {
                paint.setAlpha(150);
                canvas.drawLine(0, y + g, wx, y + g, paint);
            } else {
                paint.setAlpha(64);
                canvas.drawLine(0, y + g, wx, y + g, paint);
            }

            remains[i]--;

            y = y + d;
        }

    }

}
