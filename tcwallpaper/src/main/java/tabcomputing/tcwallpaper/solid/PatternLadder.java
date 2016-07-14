package tabcomputing.tcwallpaper.solid;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import tabcomputing.library.paper.AbstractPattern;

/**
 * DEPRECATED: Not sure this is worth having.
 *
 * This draws a line between the left and right sides of the screen,
 * where the left is the hour from bottom to top and the right is the minute
 * in the same fashion.
 */
public class PatternLadder extends AbstractPattern {

    //public PatternLadder(Settings settings, TimeSystem timeSystem, ColorWheel colorWheel) {
    //    this.settings   = settings;
    //    this.timeSystem = timeSystem;
    //    this.colorWheel = colorWheel;
    //}

    @Override
    public void draw(Canvas canvas) {
        Path p;

        int j, k;

        float cx = canvas.getWidth();
        float cy = canvas.getHeight();

        //int[] s = timeSystem.timeSegments();

        double[] r = timeSystem.handRatios();
        //int h = timeSystem.hoursOnClock();

        float y0 = (float) (cy * (1.0 - r[0]));
        float y1 = (float) (cy * (1.0 - r[1]));

        int[] colors = timeColors();

        if (settings.isSwapped()) {
            j = 1; k = 0;
        } else {
            j = 0;
            k = 1;
        }

        Paint paint = new Paint();

        paint.setColor(colors[j]);
        p = new Path();
        p.moveTo(0, 0);
        p.lineTo(0, y0);
        p.lineTo(cx, y1);
        p.lineTo(cx, 0);
        p.close();
        canvas.drawPath(p, paint);

        paint.setColor(colors[k]);
        p = new Path();
        p.moveTo(0, cy);
        p.lineTo(0, y0);
        p.lineTo(cx, y1);
        p.lineTo(cx, cy);
        p.close();
        canvas.drawPath(p, paint);

        canvas.drawLine(0, y0, cx, y1, paint);
    }

}
