package tabcomputing.chronochrome.checkers;

import android.content.Context;
import android.graphics.Canvas;
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

    protected Widget fadeWidget = new Widget(0, 1);
    protected Widget secWidget = new Widget(1, 1);

    protected WidgetBar controls = new WidgetBar();


    public PaperView(Context context) {
        super(context);

        Settings settings = getSettings();

        pattern = new Pattern(context, settings);

        fadeWidget.setImageOn(context.getResources().getDrawable(R.drawable.fade_on));
        fadeWidget.setImageOff(context.getResources().getDrawable(R.drawable.fade_off));
        fadeWidget.setState(settings.isFade());
        fadeWidget.setText("Fade");
        controls.add(fadeWidget);

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
        Context context = getContext();

        switch (widget.getID()) {
            case 0:
                settings.toggleFade();
                settings.save(getContext());
                settings.toast(Settings.KEY_FADE, context);
                break;
            case 1:
                settings.toggleSeconds();
                settings.save(getContext());
                settings.toast(Settings.KEY_TIME_SECONDS, context);
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
