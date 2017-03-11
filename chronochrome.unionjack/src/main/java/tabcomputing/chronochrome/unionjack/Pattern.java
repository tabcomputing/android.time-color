package tabcomputing.chronochrome.unionjack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import tabcomputing.library.paper.BasePattern;

public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    public Pattern(Context context, Settings settings) {
        setContext(context);
        setSettings(settings);

        resetPreferences();
    }

    private Settings settings;

    public void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    private RectF rect = new RectF();

    @Override
    public void drawPattern(Canvas canvas) {
        int[] colors = timeColors();

        int bkgColor;
        int sqrColor;
        int digColor;

        if (settings.isRoyal()) {
            bkgColor = Color.rgb(0, 0, 102);
            sqrColor = Color.rgb(204, 0, 0);
            digColor = Color.rgb(204, 0, 0);
        } else {
            bkgColor = colors[1];
            sqrColor = colors[0];
            digColor = colors[0];
            // this does nothing for systems with more than three time fragments
            if (settings.withSeconds()) {
                digColor = colors[2];
            }
        }

        // background
        canvas.drawColor(bkgColor);

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        float cx = w / 2;
        float cy = h / 2;

        float m = min(w, h);

        float gd = m / 12.0f;  // diag
        float gr = m / 7.0f;   // rect lg
        float gq = m / 12.0f;  // rect sm

        paint.reset();
        paint.setStrokeWidth(1);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(null);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        // draw large diagonals
        paint.setStrokeWidth(gd * 2);
        canvas.drawLine(0, 0, w, h, paint);;
        canvas.drawLine(w, 0, 0, h, paint);;

        // draw small diagonals
        paint.setStrokeWidth(gd / 2);
        paint.setColor(digColor);

        canvas.save();
        canvas.translate(-w * 0.0275f, 0);
        canvas.drawLine(0, 0, cx, cy, paint);
        canvas.drawLine(w, 0, cx, cy, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(w * 0.0275f, 0);
        canvas.drawLine(0, h, cx, cy, paint);
        canvas.drawLine(w, h, cx, cy, paint);
        canvas.restore();

        // draw white blocks
        paint.setStrokeWidth(1);
        paint.setColor(Color.WHITE);
        rect.set(cx - gr, 0, cx + gr, h);
        canvas.drawRect(rect, paint);
        rect.set(0, cy - gr, w, cy + gr);
        canvas.drawRect(rect, paint);

        // draw red blocks
        paint.setStrokeWidth(1);
        paint.setColor(sqrColor);
        rect.set(cx - gq, 0, cx + gq, h);
        canvas.drawRect(rect, paint);
        rect.set(0, cy - gq, w, cy + gq);
        canvas.drawRect(rect, paint);
    }

}
