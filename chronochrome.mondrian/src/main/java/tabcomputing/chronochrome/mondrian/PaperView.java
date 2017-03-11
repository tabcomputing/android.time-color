package tabcomputing.chronochrome.mondrian;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import tabcomputing.library.paper.AbstractPaperView;
import tabcomputing.library.paper.Widget;
import tabcomputing.library.paper.WidgetBar;

public class PaperView extends AbstractPaperView {

    // Store bounds of toggle button
    //protected Rect toggleBounds = new Rect();
    //protected Drawable toggleImageOn;
    //protected Drawable toggleImageOff;

    protected Widget secWidget = new Widget(1, 1);
    protected WidgetBar controls = new WidgetBar();

    public PaperView(Context context) {
        super(context);

        Settings settings = getSettings();

        pattern  = new Pattern(context, settings);

        //Widget royalWidget = new Widget(0, 1);
        //royalWidget.setImageOn(context.getResources().getDrawable(R.drawable.crown_on));
        //royalWidget.setImageOff(context.getResources().getDrawable(R.drawable.crown_off));
        //royalWidget.setState(settings.isRoyal());
        //royalWidget.setText("Royal");
        //controls.add(royalWidget);

        secWidget.setImageOn(context.getResources().getDrawable(R.drawable.clock_fast));
        secWidget.setImageOff(context.getResources().getDrawable(R.drawable.clock_slow));
        secWidget.setText("Sec");
        secWidget.setState(settings.withSeconds());
        controls.add(secWidget);
    }

    @Override
    public Settings getSettings() {
        return Settings.getInstance();
    }

    //public Settings settings;
    //private static Settings settings = Settings.getInstance();

    /**
     *
     * @param canvas    drawing canvas
     */
    @Override
    protected void drawWidgets(Canvas canvas) {
        Settings settings = getSettings();

        secWidget.setState(settings.withSeconds());

        //drawControlBox(canvas);
        controls.draw(canvas);

        //if (settings.isRoyal()) {
        //    royalWidget.drawOn(canvas);
        //} else {
        //    royalWidget.drawOff(canvas);
        //}
    }

    private void drawControlBox(Canvas canvas) {
        Rect canvasBounds = canvas.getClipBounds();
        Rect rect = new Rect(canvasBounds.left, (int) (canvasBounds.bottom * 0.9f), canvasBounds.right, canvasBounds.bottom);
        Paint paint = new Paint();
        paint.setARGB(150, 0, 0, 0);
        canvas.drawRect(rect, paint);
    }

    /**
     * setup touch listener to use left-right swipe to toggle 12/24 hour clock
     */
    @Override
    public void setupTouchListener() {
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //    x1 = event.getX();
                        return true;
                    case MotionEvent.ACTION_UP:
                        Widget pressed = controls.depressedButton(event);
                        if (pressed != null) {
                          toggle(pressed);
                        }
                        //if (royalWidget.withinBounds(event)) {
                        //    toggleRoyal();
                        //}
                        //if (selectWidget.withinBounds(event)) {
                        //    selectPaper();
                        //}
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Toggle royal colour setting and redraw.
     */
    protected void toggle(Widget widget) {
        Settings settings = getSettings();

        switch (widget.getID()) {
            case 0:
                //settings.toggleRoyal();
                settings.save(getContext());
                break;
            case 1:
                settings.toggleSeconds();
                settings.save(getContext());
                break;
        }
        invalidate();
        draw();
    }

    /**
     *
     */
    //protected void selectPaper() {
    //    // TODO
    //}



    public Rect getToggleButtonBounds(Canvas canvas) {
        Rect canvasBounds = canvas.getClipBounds();  // Adjust this for where you want it
        float r = faceRadius(canvasBounds);
        int w = (int) r / 3;
        int h = (int) (w / 1.645);
        Rect imageBounds = new Rect(0, 0, w, h); //toggleImage.getBounds();

        int offset = 40;
        int right  = imageBounds.width() + offset; //canvasBounds.width() - offset;
        int left   = offset; //right - imageBounds.width();
        int bottom = canvasBounds.bottom - (offset + 15);
        int top    = bottom - imageBounds.height();

        return new Rect(left, top, right, bottom);
    }

    public Rect getSelectButtonBounds(Canvas canvas) {
        Rect canvasBounds = canvas.getClipBounds();  // Adjust this for where you want it
        float r = faceRadius(canvasBounds);
        int w = (int) r / 3;
        int h = (int) (w / 1.645);
        Rect imageBounds = new Rect(0, 0, w, h); //toggleImage.getBounds();

        int offset = 40;
        int right  = canvasBounds.width() - offset;
        int left   = right - imageBounds.width();
        int bottom = canvasBounds.bottom - (offset + 15);
        int top    = bottom - imageBounds.height();

        return new Rect(left, top, right, bottom);
    }

}
