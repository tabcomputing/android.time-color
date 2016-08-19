package tabcomputing.tcwallpaper.tile;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import tabcomputing.library.paper.FontScale;
import tabcomputing.library.paper.ScaledText;
import tabcomputing.tcwallpaper.BasePattern;

/**
 *
 */
public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
        this.settings = wallpaper.getSettings();
    }

    private Settings settings;

    private Calendar calendar = Calendar.getInstance();

    @Override
    public void drawPattern(Canvas canvas) {
        Rect bounds = new Rect();
        canvas.getClipBounds(bounds);

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        int[] colors = timeColors();

        float size = settings.getSize() + 1; // max 4
        float radius = w / (12 - (1.8f * size));

        // TODO: jumps too much between sparse and light
        float density = (8.0f * radius) / (settings.getDensity() + 2.5f);

        int rowGap = (int) density;
        int colGap = (int) density;

        int rowCnt = (int) (w / rowGap) + 8;
        int colCnt = (int) (h / colGap) + 8;

        int x = -(2*rowGap);
        int y = -(2*colGap);

        ArrayList<Point> cords = new ArrayList<>();

        for(int j=0; j < rowCnt; j++) {
            for(int i=0; i < colCnt; i++) {
                cords.add(new Point(x, y));
                x = x + colGap;
            }
            x = 0;
            y = y + rowGap;
        }

        x = rowGap / 2;
        y = colGap / 2;

        for(int j=0; j < rowCnt; j++) {
            for(int i=0; i < colCnt; i++) {
                cords.add(new Point(x, y));
                x = x + colGap;
            }
            x = rowGap / 2;
            y = y + rowGap;
        }

        paint.reset();
        paint.setStrokeWidth(3f);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        canvas.drawColor(Color.LTGRAY);

        // pattern is based on day or week and hour
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

        int hr = time()[0];

        Collections.shuffle(cords, new Random(weekDay + hr));

        List<Integer> pSet = Arrays.asList(PATTERNSET);
        //Collections.shuffle(pSet, new Random(hr));

        int i = 0;

        // draw shapes
        for(Point p : cords) {
            int cInt = pSet.get(mod(i++,pSet.size()));

            paint.setColor(colors[mod(cInt, colors.length)]);
            paint.setAlpha(settings.isOpaque() ? 255 : 150);
            if (settings.getShape() == 0) {
                canvas.drawCircle(p.x, p.y, radius, paint);
            } else {
                canvas.drawRect(p.x - radius, p.y - radius, p.x + radius, p.y + radius, paint);
            }
        }

        // draw outlines
        if (settings.isOutlined()) {
            // TODO: color ?
            double tr = timeSystem.ratioTime();
            int c = Color.rgb((int) (255 * tr), (int) (255 * tr), (int) (255 * tr));

            paint.reset();
            paint.setColor(c); //colors[0]);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(4f);

            for (Point p : cords) {
                if (settings.getShape() == 0) {
                    canvas.drawCircle(p.x, p.y, radius, paint);
                } else {
                    canvas.drawRect(p.x - radius, p.y - radius, p.x + radius, p.y + radius, paint);
                }
            }
        }
    }

    private final Integer[] PATTERNSET = {
            3,2,6,1,7, 8,0,9,1,6, 0,3,5,4,3, 5,1,4,0,2, 4,3,4,5,4, 2,5,6,9,4, 3,7,9,2,1,3
    };


    // ANOTHER POSSIBLE PATTERN IN THE FUTURE?
    public void drawHourGlass(Canvas canvas) {
        Rect bounds = new Rect();
        canvas.getClipBounds(bounds);

        int[] colors = timeColors();

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);

        canvas.drawColor(colors[0]);

        Path path = new Path();

        float cx = centerX(canvas);
        float cy = centerY(canvas);

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        path.moveTo(0, 0);
        path.quadTo(cx, cy, 1, h);
        path.close();

        paint.setColor(colors[1]);
        canvas.drawPath(path, paint);

        path.moveTo(w, 0);
        path.quadTo(cx, cy, w, h);
        path.close();

        paint.setColor(colors[1]);
        canvas.drawPath(path, paint);
    }


    private void drawFade(Canvas canvas, boolean reverse) {
        int[] c = timeColors();

        LinearGradient shader;

        float h = canvas.getHeight();
        float w = canvas.getWidth();

        Log.d("log", "width: " + w + " rev: " + reverse);

        if (reverse) {
            int[] colors = {c[0], c[1]};
            shader = new LinearGradient(0, 0, 0, h, colors, null, Shader.TileMode.CLAMP);
        } else {
            int[] colors = {c[1], c[0]};
            shader = new LinearGradient(0, 0, w, 0, colors, null, Shader.TileMode.CLAMP);
        }

        paint.reset();
        paint.setShader(shader);

        canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
    }

    private void drawSolid(Canvas canvas, boolean reverse) {
        int[] colors = timeColors();

        if (reverse) {
            canvas.drawColor(colors[0]);
        } else {
            canvas.drawColor(colors[1]);
        }
    }

    /**
     * Configure shared textPaint for cut out.
     */
    private void clearTextPaint() {
        paint.reset();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        //textPaint.setTypeface(getFont());
        paint.setTextSize(150.0f); //getSize());
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        //textPaint.setColor(0xFFFFFF);
        //textPaint.setAlpha(0);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    /**
     * Get a string representation of the time.
     *
     * @return string of time
     */
    private String timeStamp() {
        return timeSystem.timeStamp(settings.sansSeconds());
    }

    @Override
    protected void drawCheatClock(Canvas canvas) {
        // do nothing
    }

    protected void drawStar(Canvas canvas, float radius, Paint paint) {
        float cx = centerX(canvas);
        float cy = centerY(canvas);

        int x, y;

        ArrayList<Point> points = new ArrayList<>();

        for(float r=0; r < 1; r = r + 0.2f) {
            x = (int) (cx + radius * sin(r));
            y = (int) (cy - radius * cos(r));
            points.add(new Point(x,y));
        }

        Point p0, p1;

        for (int i=0; i < 5; i++) {
            p0 = points.get(i);

            if (i + 2 < 5) {
                p1 = points.get(i + 2);
                canvas.drawLine(p0.x, p0.y, p1.x, p1.y, paint);
            }

            if (i + 3 < 5) {
                p1 = points.get(i + 3);
                canvas.drawLine(p0.x, p0.y, p1.x, p1.y, paint);
            }
        }
    }

}
