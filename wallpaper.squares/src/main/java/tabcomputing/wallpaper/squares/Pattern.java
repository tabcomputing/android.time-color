package tabcomputing.wallpaper.squares;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import tabcomputing.library.paper.AbstractPattern;
import tabcomputing.library.paper.CommonSettings;

/**
 * Draw series of spiraling square within other spiraling squares.
 */
public class Pattern extends AbstractPattern {

    public Pattern(CommonSettings settings) {
        this.settings = settings;
    }

    @Override
    public void draw(Canvas canvas) {
        float h = canvas.getHeight();
        float w = canvas.getWidth();

        Path p = new Path();

        int[] cc = timeColors();
        int[] c = new int[cc.length+1];

        for(int i=0; i < cc.length; i++) {
            c[i] = cc[i];
        }
        c[cc.length] = cc[0];

        double[] r = timeRatios();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //paint.setShadowLayer(20.0f, 0f, 0f, Color.BLACK);

        float x, y;

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float radius;

        int s = r.length;

        if (!settings.displaySeconds()) {
            s = s - 1;
        }

        canvas.drawColor(c[s - 1]);

        radius = (2 * cy) - 20;

        int i;

        for(int j=0; j < (s*2); j++) {
            i = mod(j, s);

            p.reset();
            paint.setColor(c[i]); //colorWheel.alphaColor(cc[i+1], 0.75));

            for (float d = 0; d < 1; d = d + 0.25f) {
                x = (float) (cx + radius * sin(r[i] + rot() + d));
                y = (float) (cy - radius * cos(r[i] + rot() + d));

                // TODO: would like to clip off the corners of each square except d==0
                if (d == 0) {
                    p.moveTo(x, y);
                } else {
                    p.lineTo(x, y);
                }
            }
            p.close();
            canvas.drawPath(p, paint);

            radius = radius * 0.55f;
        }
    }

}
