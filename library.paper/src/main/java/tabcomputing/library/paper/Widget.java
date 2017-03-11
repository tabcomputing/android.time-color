package tabcomputing.library.paper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;

public class Widget {
    static String TAG = "Widget";

    public static int BOTTOM_LEFT = 0;
    public static int BOTTOM_RIGHT = 1;

    protected float size = 0.1f;
    protected float ratio = 1.0f; //0.60784f;
    protected int location;
    protected String text = "AM/PM";

    protected Drawable imageOn;
    protected Drawable imageOff;

    Rect cacheBounds;

    int OFFSET_VERTICAL = 0;
    int OFFSET_HORIZONTAL = 100;

    protected int IMAGE_PADDING = 20;

    private Paint paint = new Paint();

    public Widget(int idNum, int states) {
        id = idNum;
        maxState = states;

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(48f);
    }

    private int id;

    private int maxState = 1;

    private int state = 0;

    public int getID() {
        return id;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setRatio(float widthToHeight) {
        this.ratio = widthToHeight;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public void setImageOn(Drawable image) {
        imageOn = image;
    }

    public void setImageOff(Drawable image) {
        imageOff = image;
    }

    public void setText(String string) {
        text = string;
    }

    public Rect getBounds() {
        return cacheBounds;
    }

    public float width(Canvas canvas) {
        Rect canvasBounds = canvas.getClipBounds();
        if (imageOn != null) {
            return canvasBounds.height() * size;
        } else {
            return paint.measureText(text) + OFFSET_HORIZONTAL;
        }
    }

    private Rect getBottomRightBounds(Canvas canvas, int rightOffset) {
        Rect canvasBounds = canvas.getClipBounds();
        //int w = (int) (canvasBounds.width() * size);
        int h = (int) (canvasBounds.height() * size);
        int w = (int) width(canvas); //(int) (ratio * h);

        int right  = canvasBounds.right - rightOffset;
        int left   = right - w;
        int bottom = canvasBounds.bottom;
        int top    = bottom - h;

        return new Rect(left, top, right, bottom);
    }

    private Rect getBottomLeftBounds(Rect canvasBounds) {
        int h = (int) (canvasBounds.height() * size);
        int w = (int) (ratio * h);

        int left   = OFFSET_HORIZONTAL;
        int right  = left + w;
        int bottom = canvasBounds.bottom - OFFSET_VERTICAL;
        int top    = bottom - h;

        return new Rect(left, top, right, bottom);
    }

    public void draw(Canvas canvas, int right) {
        drawOn(canvas, right);
    }

    public void drawOn(Canvas canvas, int right) {
        Rect bounds = getBottomRightBounds(canvas, right);

        //Log.d(TAG, "drawOn: " + bounds);
        //canvas.drawRect(bounds, paint);

        cacheBounds = bounds;

        //canvas.drawRect(bounds, paint);

        if (imageOn != null) {
            Drawable image = (state == 0 ? imageOff : imageOn);
            Rect imgBounds = adjustBounds(bounds, 0.15f);
            image.setBounds(imgBounds);
            image.draw(canvas);
        } else {
            paint.setColor(Color.WHITE);
            canvas.drawText(text, bounds.centerX(), bounds.centerY(), paint);
        }
    }

    public void drawOff(Canvas canvas, int right) {
        Rect bounds = getBottomRightBounds(canvas, right);

        cacheBounds = bounds;

        //Drawable image = imageOff;
        //image.setBounds(bounds);
        //image.draw(canvas);

        paint.setColor(Color.GRAY);

        //canvas.drawRect(bounds, paint);

        canvas.drawText(text, bounds.centerX(), bounds.centerY(), paint);
    }

    public boolean withinBounds(MotionEvent event) {
        Rect bounds = getBounds();
        float x = event.getX();
        float y = event.getY();
        return bounds.contains((int) x, (int) y);
    }

    public void setState(boolean boolState) {
        if (boolState) {
            state = 1;
        } else {
            state = 0;
        }
    }

    public void toggle() {
        state = state + 1;
        if (state > maxState) {
            state = 0;
        }
    }

    protected Rect adjustBounds(Rect bounds, float precent) {
        int padW = (int) (bounds.width() * precent);
        int padH = (int) (bounds.height() * precent);
        return new Rect(bounds.left + padW, bounds.top + padH, bounds.right - padW, bounds.bottom - padH);
    }

}
