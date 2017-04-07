package tabcomputing.library.paper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class WidgetBar {

    private float barHeight = 100f;
    protected List<Widget> widgets = new ArrayList<>();


    public WidgetBar() {

    }

    public void add(Widget widget) {
        widgets.add(widget);
    }

    public void get(int index) {
        widgets.get(index);
    }

    public void draw(Canvas canvas) {
        drawControlBox(canvas);

        int right = (int) (barHeight * 0.1f);
        for (Widget widget: widgets) {
            widget.draw(canvas, right, barHeight);
            //right = right + (int) widget.width(canvas);
            right = right + (int) widget.width(barHeight);
        }
    }

    private void drawControlBox(Canvas canvas) {
        Rect canvasBounds = canvas.getClipBounds();
        //Rect rect = new Rect(canvasBounds.left, (int) (canvasBounds.bottom * 0.9f), canvasBounds.right, canvasBounds.bottom);
        Rect rect = new Rect(canvasBounds.left, (int) (canvasBounds.bottom - barHeight), canvasBounds.right, canvasBounds.bottom);
        Paint paint = new Paint();
        paint.setARGB(150, 0, 0, 0);
        canvas.drawRect(rect, paint);
    }

    public Widget depressedButton(MotionEvent event) {
        for (Widget widget: widgets) {
            if (widget.withinBounds(event)) {
                toggle(widget);
                return widget;
            }
        }
        return null;
    }

    protected void toggle(Widget widget) {
        widget.toggle();
    }

    /**
     * Set height of action bar.
     *
     * @param height    height
     */
    public void setHeight(float height) {
        barHeight = height;
    }

}
