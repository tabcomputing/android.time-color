package tabcomputing.library.paper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Arrays;

import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.color.ColorWheel;

/**
 *
 */
public class ClockView extends SurfaceView {
    private static final String DEBUG_TAG = "ClockView";

    protected WidgetBar controls = new WidgetBar();

    public ClockView(Context context) {
        super(context, null);
        //setupClock();

        Widget ampmWidget = new Widget(0, 1);
        ampmWidget.setImageOn(context.getResources().getDrawable(R.drawable.clock_ampm));
        ampmWidget.setImageOff(context.getResources().getDrawable(R.drawable.clock_total));
        ampmWidget.setState(settings.isAMPM());
        ampmWidget.setText("AM/PM");
        controls.add(ampmWidget);

        Widget secWidget = new Widget(1, 1);
        secWidget.setImageOn(context.getResources().getDrawable(R.drawable.clock_fast));
        secWidget.setImageOff(context.getResources().getDrawable(R.drawable.clock_slow));
        secWidget.setText("Sec");
        secWidget.setState(settings.displaySeconds());
        controls.add(secWidget);
    }

    /**
     * Setup touch listener.
     */
    public void setupTouchListener() {
        setOnTouchListener(new View.OnTouchListener() {
            //private float x1, x2;
            //static final int MIN_DISTANCE = 200;

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
                        //Rect bounds = clock.toggleButtonBounds();
                        //float x = event.getX();
                        //float y = event.getY();
                        //Log.d("ClockFragment", "x: " + x + " y: " + y);
                        //Log.d("ClockFragment", "bounds: " + bounds.left + " " + bounds.right + " " + bounds.top + " " + bounds.bottom);
                        //if (bounds.contains((int) x, (int) y)) {
                        //    toggle12and24();
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
        CommonSettings settings = getSettings();

        switch (widget.getID()) {
            case 0:
                settings.toggleAMPM();
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
     * Get settings.
     *
     * @return      return instance of settings
     */
    protected CommonSettings getSettings() {
        return settings;
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = MotionEventCompat.getActionMasked(event);

        Log.d(DEBUG_TAG, "HERE!!!!!!!!");
        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(DEBUG_TAG,"Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d(DEBUG_TAG,"Action was MOVE");
                toggle12and24();
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.d(DEBUG_TAG,"Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d(DEBUG_TAG,"Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }
    */

    private boolean mAttached;

    private final Handler handler = new Handler();

    private TimeSystem timeSystem;

    //private Settings settings;
    private static CommonSettings settings = CommonSettings.getInstance();

    // shared paint instances
    private Paint paint = new Paint();
    private TextPaint textPaint = new TextPaint();

    //
    //private Typeface font = Typeface.DEFAULT;

    //
    private ColorWheel colorWheel;

    //
    private Runnable drawRunner = new Runnable() {
        public void run() {
            draw();
        }
    };

    //@Override
    //public void onDestroy() {
    //    super.onDestroy();
    //   handler.removeCallbacks(drawRunner);
    //}

    //@Override
    //protected void onVisibilityChanged(boolean visible) {
    //    this.visible = visible;
    //    if (visible) {
    //        handler.post(drawRunner);
    //    } else {
    //       handler.removeCallbacks(drawRunner);
    //    }
    //}

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
        }
        this.visible = true;
        if (visible) {
            handler.post(drawRunner);
        //    } else {
        //       handler.removeCallbacks(drawRunner);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            handler.removeCallbacks(drawRunner);
            mAttached = false;
        }
    }

    private void onTimeChanged() {
        //Calendar calendar = Calendar.getInstance();
        //calendar.setToNow();

        //int hour = mCalendar.hour;
        //int minute = mCalendar.minute;
        //int second = mCalendar.second;

        //mMinutes = minute + second / 60.0f;
        //mHour = hour + mMinutes / 60.0f;
        mChanged = true;
    }

    private boolean mChanged = false;
    private boolean visible = true;

    /**
     * Return the time to wait between screen refreshes.
     *
     * @return      duration in milliseconds
     */
    private int frameDuration() {
        return timeSystem.tickTime(true); //settings.displaySeconds());
    }

    private Canvas canvas;

    private Rect bounds = new Rect();

    // Store bounds of toggle button
    private Rect toggleBounds = new Rect();

    /**
     *
     */
    protected void toggle12and24() {
        settings.toggleAMPM();
        invalidate();
        draw();
    }

    /**
     * Draw routine.
     */
    private void draw() {
        SurfaceHolder holder = getHolder(); //getSurfaceHolder();
        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                drawClock(canvas);
                drawInstructions(canvas);
            }
        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }

        handler.removeCallbacks(drawRunner);

        if (visible) {
            handler.postDelayed(drawRunner, frameDuration());
        }
    }

    //@Override
    //public void onDraw(Canvas canvas) {
    //    super.onDraw(canvas);
    //    drawClock(canvas);
    //}

    /**
     *
     * @param canvas
     */
    protected void drawInstructions(Canvas canvas) {
        //drawControlBox(canvas);

        controls.draw(canvas);

        //toggleBounds = getToggleButtonBounds(canvas);
        //if (settings.isAMPM()) {
        //    toggleImageOn.setBounds(toggleBounds);
        //    toggleImageOn.draw(canvas);
        //} else {
        //    toggleImageOff.setBounds(toggleBounds);
        //    toggleImageOff.draw(canvas);
        //}
    }

    public Rect toggleButtonBounds() {
        return toggleBounds;
    }

    public Rect getToggleButtonBounds(Canvas canvas) {
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

    private void drawControlBox(Canvas canvas) {
        Rect canvasBounds = canvas.getClipBounds();
        Rect rect = new Rect(canvasBounds.left, (int) (canvasBounds.bottom * 0.9f), canvasBounds.right, canvasBounds.bottom);
        Paint paint = new Paint();
        paint.setARGB(150, 0, 0, 0);
        canvas.drawRect(rect, paint);
    }

    /**
     *
     * @param canvas
     */
    private void drawClock(Canvas canvas) {
        timeSystem = settings.getTimeSystem();
        colorWheel = settings.getColorWheel();

        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }

        canvas.getClipBounds(bounds);

        canvas.drawColor(Color.WHITE);

        switch(settings.getClockType()) {
            case 1:
                drawDigitalClock(canvas, bounds);
                break;
            default:
                drawAnalogClock(bounds);
                break;
        }
    }

    private void drawAnalogClock(Rect bounds) {
        //drawBackgroundForAnalog();
        drawColorWheel(canvas, bounds);
        drawNumbers(canvas, bounds);
        drawTicks(canvas, bounds);
        drawClockHands(canvas, bounds);
    }

    /**
     *
     */
    public void setupClock() {
        //settings.setAssets(getContext().getAssets());

        //font = settings.getTypeface();
        timeSystem = settings.getTimeSystem();
        colorWheel = settings.getColorWheel();
    }


    private void drawBackgroundForDigital(Rect bounds) {
        float cy = centerY(bounds);
        float cx = centerX(bounds);

        switch (settings.getBackground()) {
            case 0:
                drawBackgroundWhite();
                return;
            case 1:
                drawBackgroundBlack();
                return;
        }

        int[] colors = timeSystem.timeColors(colorWheel);

        if (! settings.displaySeconds()) {
            colors = Arrays.copyOf(colors, colors.length - 1);
        }

        int[] linearColors = new int[colors.length + 2];
        for (int i = 0; i < colors.length; i++) {
            linearColors[i + 1] = colors[i];
        }
        linearColors[0] = Color.BLACK;
        linearColors[linearColors.length - 1] = Color.BLACK;

        Shader shader = new LinearGradient(0, cy, bounds.right, cy, linearColors, null, Shader.TileMode.REPEAT);

        paint.reset();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, bounds.right, bounds.bottom), paint);
    }

    private void drawBackgroundWhite() {
        canvas.drawColor(Color.WHITE);
    }

    private void drawBackgroundBlack() {
        canvas.drawColor(Color.BLACK);
    }

    /**
     * Draw time in the center of the clock face area.
     */
    private void drawDigitalClock(Canvas canvas, Rect bounds) {
        drawBackgroundForDigital(bounds);

        Rect d;

        Typeface font = settings.getTypeface();

        //String stamp = timeStamp();
        String[] time = timeSystem.timeRebased(settings.isAMPM());

        if (! settings.displaySeconds()) {
            time = Arrays.copyOf(time, time.length - 1);
        }

        int[] colors = timeSystem.timeColors(colorWheel);

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        float circleWidth = bounds.width() / (time.length + 1);

        textPaint.reset();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(120.0f);
        textPaint.setStrokeWidth(3.0f);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(font);
        textPaint.setTextAlign(Paint.Align.CENTER);
        //digitalPaint.setShadowLayer(3.0f, -3.0f, -3.0f, Color.BLACK);

        //setTextSizeForWidth(digitalPaint, x * 0.8f, sampleTime());
        setTextSizeForWidth(textPaint, circleWidth * 0.75f, "X4", 0.9f);

        d = textDimensions("44", textPaint);

        //canvas.drawText(stamp, x, y + (d.height() / 2f), textPaint);

        paint.reset();
        paint.setAntiAlias(true);

        float y = cy + (d.height() / 2f);
        float x = circleWidth;

        for(int i=0; i < time.length; i++) {
            paint.setColor(colors[i]);
            canvas.drawCircle(x, cy, circleWidth/2.0f, paint);
            canvas.drawText(time[i], x, y, textPaint);
            x = x + circleWidth;
        }
    }

    private void drawColorWheel(Canvas canvas, Rect bounds) {
        drawColorClockFace(bounds);
        //if (timeSystem.isDaySplit() && !settings.isDuplexed()) {
        //    drawColorWheelSplit(canvas, bounds);
        //} else {
        //    drawColorWheelFull(canvas, bounds);
        //}
    }

    /**
     * The size of the wheel is calculated very carefully. We know the total width of area
     * in which the wheel must reside (w). We then must calculate the radius of the main
     * circle on which the small circles are centered. This formula derives from the chord
     * equation for a circle (c = 2R sin(a/2)).
     *
     */
    private void drawColorWheelFull(Canvas canvas, Rect bounds) {
        float cx = centerX(bounds);
        float cy = centerY(bounds);

        int hd = timeSystem.hoursInDay();
        int hc = timeSystem.hoursOnClock();
        //int mc = timeSystem.minutesOnClock();

        int half = hd / 2;
        int qtr = hd / 4;

        float w = faceRadius(bounds);

        // TODO: spare option?
        //float r0 = sparse ? spotRadius(1.5f * hc) : spotRadius();
        float r0 = spotRadius(bounds);
        float r1 = w - r0;
        //float r2 = r0 + (r1 * 1.15f);

        float x, y;
        double r;

        int i;
        int[] colors;

        colors = clockColors(settings.isDuplexed() && !timeSystem.isDaySplit());
        //colors = hourColors();

        paint.reset();
        paint.setAntiAlias(settings.getBackground() != 2);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10.0f);
        paint.setStyle(Paint.Style.FILL);
        paint.clearShadowLayer();

        // draw hours
        for (i = 0; i < hc; i++) {
            r = ((double) i) / hc;

            //c = ratioToColor(colorCorrect(r));
            paint.setColor(colors[i]);

            x = (float) (cx + r0 * sin(r + rot()));
            y = (float) (cy - r0 * cos(r + rot()));

            //circlePaint.setShadowLayer(3.0f, 0f, 0f, c);
            canvas.drawCircle(x, y, r1, paint);

            //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
            //y = (float) (cy - r0 * cos(r + rot() - 0.041666667));
        }
    }

/*
            if (isDaySplit() && !isDuplexed()) {
                // draw hours
                for (i = 0; i < hc; i++) {
                    r = ((double) i) / hc;

                    //c = ratioToColor(colorCorrect(r));
                    circlePaint.setColor(colors[i + half]);

                    x = (float) (cx + r0 * sin(r + rot()));
                    y = (float) (cy - r0 * cos(r + rot()));

                    //circlePaint.setShadowLayer(3.0f, 0f, 0f, c);
                    canvas.drawCircle(x, y, r1 / 2, circlePaint);

                    //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
                    //y = (float) (cy - r0 * cos(r + rot() - 0.041666667)); //Math.sqrt(radius*radius + x*x);
                }
            }
*/
/*
            if (isDaySplit() && !isDuplexed()) {
                int[] hourColors = colorWheel.colors(hoursInDay());

                int h = (int) (timeRatios()[0] * hd);
                i = mod(h - (hc / 2), hc);

                int q = half + mod(h + qtr, hc);

                // TODO: need to get the color from the full specturm of hour colors
                //int[] c = {hourColors[mod(i + half, hd)], colors[i]};
                //int[] c = {colors[i], colors[mod(i - 1, hc)]};
                int[] c = {colors[i], hourColors[q]};

                if (h < hc - qtr || h >= hc + qtr) {
                    reverseArray(c);
                }

                r = (double) i / hc;

                //r = ((double) h) / hc;

                x = (float) (cx + r0 * sin(r + rot()));
                y = (float) (cy - r0 * cos(r + rot()));

                //float x1 = (float) (cx + (r0 - r0) * sin(r + rot()));
                //float y1 = (float) (cy - (r0 - r0) * cos(r + rot()));

                //float x2 = (float) (cx + (r0 + r0) * sin(r + rot()));
                //float y2 = (float) (cy - (r0 + r0) * cos(r + rot()));

                //int a = (int) (180 / Math.PI * Math.atan2(y2 - y1, x2 - x1));

                float a = (float) r * 360 - 90;

                drawSemiCircles(c, x, y, r1, a);
            }
            */



    private boolean isColorDuplexed() {
        return (timeSystem.isDaySplit() && !settings.isDuplexed());
    }

    /**
     *
     * @param bounds
     */
    private void drawColorClockFace(Rect bounds) {
        //Bitmap bkg = getAnalogBackground(bounds);
        //Bitmap cw0 = (isColorDuplexed() ? getColorWheelDuplex(bounds) : getColorWheel(bounds));
        Bitmap cw0 = (settings.isAMPM() ? getColorWheelDuplex(bounds) : getColorWheel(bounds));
        Bitmap cw1 = getCircleWheel(bounds);

        Paint q = new Paint(Paint.ANTI_ALIAS_FLAG);
        //setLayerType(LAYER_TYPE_HARDWARE, q);
        canvas.drawBitmap(cw0, 0, 0, q);
        //q.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(cw1, 0, 0, q);
        //q.setXfermode(null);
    }

    /**
     * Draw a rainbow wheel within the given bounds.
     *
     * @param bounds        boundary size
     * @return              bitmap
     */
    private Bitmap getColorWheel(Rect bounds) {
        Bitmap bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        RectF rectF;
        if (cx < cy) {
            rectF = new RectF(0, cy - cx, cx * 2, cy + cx);
        } else {
            rectF = new RectF(cx - cy, 0, cx + cy, cy * 2);
        }

        int[] colors = clockColors(settings.isDuplexed()); // && !timeSystem.isDaySplit());

        int[] colors2 = Arrays.copyOf(colors, colors.length + 1);
        colors2[colors.length] = colors2[0];

        SweepGradient shader = new SweepGradient(cx, cy, colors2, null);

        paint.reset();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(shader);

        if (settings.rotateTime() || settings.isAMPM()) {
            canvas.rotate(-90, cx, cy);
        } else {
            canvas.rotate(90, cx, cy);
        }

        canvas.drawArc(rectF, 0, 360, true, paint);

        return bitmap;
    }

    /**
     * TODO: We can get the position of the gradient switch over just right
     *       if we calculate the positions of each color and at the switch over
     *       adjust the positions of the two adjacent colors (at q?) by the fractional
     *       ratio of the time.
     */
    private Bitmap getColorWheelDuplex(Rect bounds) {
        int hd = hoursInDay();
        int hc = hoursOnClock();
        int half = hd / 2;
        int qtr  = hd / 4;
        //int h = (int) ((timeSystem.timeRatios()[0] * hd));
        int h = (int) ((timeSystem.timeRatios()[0] * hd) + 0.5);
        int q = half + mod(h + qtr, hc);
        //int i = mod(hx - (hc / 2), hc);  // TODO: replace this color index with black or white?
        int s;
        if (h < hc - qtr || h >= hc + qtr) {
            s = half;
        } else {
            s = 0;
        }

        Bitmap bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        RectF rectF;
        if (cx < cy) {
            rectF = new RectF(0, cy - cx, cx * 2, cy + cx);
        } else {
            rectF = new RectF(cx - cy, 0, cx + cy, cy * 2);
        }

        int[] colors = colorWheel.colors(hd); //settings.isDuplexed() && !timeSystem.isDaySplit());

        int[] duplexColors = new int[hc];

        for (int i = 0; i < q; i++) {
            int j = mod(i + s, hd);
            int k = mod(i, hc);
            duplexColors[k] = colors[j];
        }

        int z = mod(h - qtr, hc);
        duplexColors[z] = Color.GRAY;

        duplexColors = Arrays.copyOf(duplexColors, duplexColors.length + 1);
        duplexColors[duplexColors.length - 1] = duplexColors[0];

        SweepGradient shader = new SweepGradient(cx, cy, duplexColors, null);

        // TODO: paint.reset();
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(shader);

        //canvas.rotate(-90, cx, cy);
        if (settings.rotateTime() || settings.isAMPM()) {
            canvas.rotate(-90, cx, cy);
        } else {
            canvas.rotate(90, cx, cy);
        }

        canvas.drawArc(rectF, 0, 360, true, paint);

        return bitmap;
    }

    public int[] insert(int[] a, int i, int c) {
        int[] newArr = new int[a.length + 1];
        for (int j = 0; j < i; j++) {
            newArr[j] = a[j];
        }
        newArr[i] = c;
        for (int j = i; j < a.length; j++) {
            newArr[j + 1] = a[j];
        }
        return newArr;
    }

    protected int hoursOnClock() {
        if (settings.isAMPM()) {
            return timeSystem.hoursInDay() / 2;
        } else {
            return timeSystem.hoursInDay();
        }
    }

    /**
     * Draw hour circles on a canvas. These will be masked on top of the rainbow wheel.
     *
     * TODO: Draw background.
     */
    private Bitmap getCircleWheel(Rect bounds) {
        Bitmap bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        //RectF rectF = new RectF(bounds.left, bounds.top, bounds.right, bounds.bottom);

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        int hc = hoursOnClock();

        //int half = hd / 2;
        //int qtr = hd / 4;

        float w = faceRadius(bounds);

        // TODO: spare option?
        //float r0 = sparse ? spotRadius(1.5f * hc) : spotRadius();
        float r0 = spotRadius(bounds);
        float r1 = w - r0;
        //float r2 = r0 + (r1 * 1.15f);

        float x, y;
        double r;

        //int[] colors;
        //colors = clockColors(settings.isDuplexed() && !timeSystem.isDaySplit());
        //colors = hourColors();

        drawAnalogBackground(canvas, bounds);

        paint.reset();
        paint.setColor(0xFFFFFF);
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        paint.setColor(Color.TRANSPARENT);
        //paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        //paint.setAntiAlias(settings.getBackground() != 2);
        //paint.setStrokeWidth(1.0f);
        //paint.clearShadowLayer();

        //int[] colors = timeColors();
        //canvas.drawColor(colors[0]);

        // draw hours
        for (int i = 0; i < hc; i++) {
            r = ((double) i) / hc;
            x = (float) (cx + r0 * sin(r + rot()));
            y = (float) (cy - r0 * cos(r + rot()));
            //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
            //y = (float) (cy - r0 * cos(r + rot() - 0.041666667));
            canvas.drawCircle(x, y, r1, paint);
        }

        return bitmap;
    }

    protected Bitmap getAnalogBackground(Rect bounds) {
        Bitmap bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        int[] colors = timeColors();

        switch(settings.getBackground()) {
            case 2:
                // TODO: draw minute circle if analog
                canvas.drawColor(colors[0]);
                break;
            case 1:
                canvas.drawColor(Color.BLACK);
                break;
            default:
                canvas.drawColor(Color.WHITE);
                break;
        }

        return bitmap;
    }

    /**
     * Draw background color and clock face color.
     *
     * @param canvas    drawing canvas
     * @param bounds    drawing boundaries
     */
    protected void drawAnalogBackground(Canvas canvas, Rect bounds) {
        int[] colors = timeColors();

        if (settings.isSwapped()) {
            int temp = colors[0];
            colors[0] = colors[1];
            colors[1] = temp;
        }

        switch(settings.getBackground()) {
            case 2:
                canvas.drawColor(Color.WHITE);
                break;
            case 1:
                canvas.drawColor(Color.BLACK);
                break;
            default:
                float cx = centerX(bounds);
                float cy = centerY(bounds);
                float r  = spotRadius(bounds);

                paint.reset();
                paint.setColor(colors[0]);

                canvas.drawColor(colors[1]);
                canvas.drawCircle(cx, cy, r, paint);
        }
    }

    /**
     * Produces an array of colors, one for each time segment. Each color is produced from the
     * ratio of a segment and all the segments that follow. Hence the colors have smooth
     * continuity, unlike those from discreteColors().
     *
     * @return array of colors
     */
    protected int[] timeColors() {
        double[] ratios = timeSystem.timeRatios();

        if (settings.isDuplexed()) {
            ratios[0] = reduce(ratios[0] * 2);
        }

        return colorWheel.colors(ratios);
    }

    /**
     * Clock hour colors.
     *
     * @return array of color integers
     */
    protected int[] clockColors() {
        int[] colors;

        //int[] colors = timeSystem.clockColors(colorWheel);
        int hc = timeSystem.hoursOnClock();

        if (settings.isDuplexed()) {
            if (timeSystem.isDaySplit()) {
                colors = colorWheel.colors(hc);
            } else {
                colors = colorWheel.colors(hc / 2);
                colors = concat(colors, colors);  // TODO: rotate 90 degrees?
            }
        } else {
            if (settings.isAMPM()) {
            //if (timeSystem.isDaySplit()) {
                colors = clockColorsPartial();
            } else {
                colors = colorWheel.colors(hc);
            }
        }

        return colors;
    }

    protected int hoursInDay() {
        return timeSystem.hoursInDay();
    }

    protected int[] clockColorsPartial() {
        int hour = timeSystem.time()[0];

        int hd = hoursInDay();
        int hc = hoursOnClock();

        int[] colors = colorWheel.colors(hd);

        int[] ci = getColorIndexes(hour, hd, hc);

        int[] nc = new int[ci.length];
        for(int i=0; i < ci.length; i++) {
            nc[i] = colors[ci[i]];
        }
        return nc;
    }

    protected int[] getColorIndexes(int hour, int hours, int length) {
        int i, add;

        if (mod(hours, length) != 0) {
            throw new ArithmeticException("number of hours must be multiple of length");
        }

        int[] result = new int[length];

        add = hour + hours - (length / 2) - (mod(length,2)) + 1;

        for (i = 0; i < length; i++) {
            result[mod(add + i, length)] = mod(add + i, hours);
        }

        return result;
    }

    /**
     * TODO: better name for this method
     *
     * @param ratio     ratio to reduce
     * @return          ratio reduced to 0 to 1
     */
    protected double reduce(double ratio) {
        if (ratio < 0) {
            int n = (int) ratio;
            return (1.0 - (ratio - n));
        } else {
            int n = (int) ratio;
            return (ratio - n);
        }
    }

    /**
     * Draw the numbers of the clock face.
     */
    private void drawNumbers(Canvas canvas, Rect bounds) {
        int i;
        double r;
        float x,y;

        Typeface font = settings.getTypeface();

        float radius = spotRadius(bounds);
        float textWidth = faceRadius(bounds) - radius;

        int hc = hoursOnClock();

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        // TODO: should we reuse a shared paint instance to reduce memory footprint?
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10.0f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(5.0f, 1.0f, 1.0f, Color.LTGRAY);
        paint.setTypeface(font);
        paint.setTextSize(120.0f);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(font);

        //if (settings.isClockNumbered()) {
            paint.setColor(Color.WHITE);

            setTextSizeForWidth(paint, textWidth, "X4", 1.1f);

            float th = textHeight("X4", paint) / 2f;

            String[] nums = clockNumbers();

            for (i = 0; i < hc; i++) {
                r = ((double) i) / hc;
                x = (float) (cx + radius * sin(r + rot()));
                y = (float) (cy - radius * cos(r + rot()));
                canvas.drawText(nums[i], x, y + th, paint);
            }
        //}
    }

    private String[] clockNumbers() {
        String[] nums = timeSystem.clockNumbers(settings.getNumberSystem(), hoursOnClock());

        //nums = Arrays.copyOf(nums, hoursOnClock());

        return nums;
    }

    /**
     * Draw the minute/second dashes of the clock face.
     */
    private void drawTicks(Canvas canvas, Rect bounds) {
        double r;
        int c;
        float x0, y0, x1, y1;

        float length = 12.0f;

        float w  = faceRadius(bounds);
        //float r0 = spotRadius();
        //float r1 = w - r0;
        float radius = w * 1.05f; //r0 + (r1 * 1.15f);

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        int mc = timeSystem.minutesOnClock();

        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10.0f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(3.0f, 1.0f, 1.0f, Color.LTGRAY);

        for (int i = 0; i < mc; i++) {
            r = ((double) i) / mc;
            c = colorWheel.color(r);  // TODO: dynamic?

            paint.setColor(c);

            x0 = (float) (cx + radius * sin(r + rot()));
            y0 = (float) (cy - radius * cos(r + rot()));

            x1 = (float) (cx + (radius + length) * sin(r + rot()));
            y1 = (float) (cy - (radius + length) * cos(r + rot()));

            canvas.drawLine(x0, y0, x1, y1, paint);
        }
    }

    /**
     * Draw clock hands.
     */
    private void drawClockHands(Canvas canvas, Rect bounds) {
        //if (!settings.hasClockHands()) {
        //    return;
        //}

        float l;  // length of hand
        float x, y;

        float cx = centerX(bounds);
        float cy = centerY(bounds);

        float w  = faceRadius(bounds);
        float r0 = spotRadius(bounds);
        float r1 = w - r0;

        double[] r = handRatios();
        int s = r.length;

        l = ((r0 - r1) * 0.90f);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20.0f);
        paint.setColor(Color.LTGRAY);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setShadowLayer(15.0f, 5.0f, 5.0f, Color.BLACK);

        for(int i=0; i < s; i++) {
            //0.45f + (i * 0.5f / (r.length - 1));

            x = (float) (cx + l * sin(r[i] + rot()));
            y = (float) (cy - l * cos(r[i] + rot()));

            // second hand
            if (i == r.length - 1) {
                paint.setStrokeWidth(10.0f);
            }
            canvas.drawLine(cx, cy, x, y, paint);

            l = (r0 + r1) * 0.9f;
        }
    }

    /**
     * Return the current time as a series of ratios, one for each hand of the clock for
     * the active time system. This is different from timeRatios() in that standard time
     * splits the hours into day and night.
     *
     * @return series of time ratios
     */
    protected double[] handRatios(boolean all) {
        double[] ratios = timeSystem.handRatios();
        if (!all && !settings.displaySeconds()) {
            ratios = Arrays.copyOf(ratios, ratios.length - 1);
        }

        //if (timeSystem.isDaySplit()) {
        if (settings.isAMPM()) {
            ratios[0] = reduce(ratios[0] * 2);
        }

        return ratios;
    }
    protected double[] handRatios() {
        return handRatios(false);
    }

    /**
     * Get a string representation of the time.
     *
     * @return string of time
     */
    private String timeStamp() {
        return timeSystem.timeStamp(!settings.displaySeconds());
    }

    /**
     * When displaying the time we size the font to fit a specific area of the display.
     * In order to keep text from shifting noticeably as the time changes, we set the
     * font size using a sample text representative of the time to be displayed, rather
     * then the actual time.
     *
     * The letter A and number 4 are used because they tends to be the widest in print.
     *
     * @return      sample time
     */
    private String sampleTime() {
        return timeSystem.timeStampSample(settings.displaySeconds());
    }

    /**
     * Clock hand colors.
     *
     * @return      array of color integers
     */
    private int[] clockColors(boolean duplex) {
        int[] colors;

        if (duplex) {
            colors = colorWheel.colors(timeSystem.hoursOnClock() / 2);
            colors = concat(colors, colors);
        } else {
            colors = colorWheel.colors(timeSystem.hoursOnClock());
        }

        return colors;
    }

    private int[] concat(int[] a, int[] b) {
        int aLen = a.length;
        int bLen = b.length;
        int[] c = new int[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    /**
     * Center X coordinate.
     *
     * @return      horizontal origin
     */
    private float centerX(Rect bounds) {
        return bounds.width() / 2;
    }
    private float centerX(RectF bounds) {
        return bounds.width() / 2;
    }

    /**
     * The center Y coordinate is raised up a bit when orientation is vertical as it places
     * the center in a more ergonomic position.
     *
     * @return      vertical origin
     */
    private float centerY(Rect bounds) {
        if (bounds.height() > bounds.width()) {
            return bounds.height() / 2 * 0.9f;
        } else{
            return bounds.height() / 2;
        }
    }
    private float centerY(RectF bounds) {
        if (bounds.height() > bounds.width()) {
            return bounds.height() / 2 * 0.9f;
        } else{
            return bounds.height() / 2;
        }
    }

    /**
     * @return  maximum radius of the clock face
     */
    private float faceRadius(Rect bounds) {
        //return centerX(bounds) * 0.85f;
        float x = centerX(bounds);
        float y = centerY(bounds);
        if (x < y) {
            return x * 0.85f;
        } else {
            return y * 0.85f;
        }
    }
    private float faceRadius(RectF bounds) {
        //return centerX(bounds) * 0.85f;
        float x = centerX(bounds);
        float y = centerY(bounds);
        if (x < y) {
            return x * 0.85f;
        } else {
            return y * 0.85f;
        }
    }

    /**
     *
     * @return
     */
    private float spotRadius(Rect bounds) {
        return spotRadius(bounds, hoursOnClock());
    }
    private float spotRadius(RectF bounds) {
        return spotRadius(bounds, hoursOnClock());
    }

    /**
     *
     * @param h     number of divisions
     * @return      radius of central circle
     */
    private float spotRadius(Rect bounds, float h) {
        float w = faceRadius(bounds);
        return (float) (w / (1.0f + (2.0f * Math.sin(Math.PI / (2.0f * h)))));
    }
    private float spotRadius(RectF bounds, float h) {
        float w = faceRadius(bounds);
        return (float) (w / (1.0f + (2.0f * Math.sin(Math.PI / (2.0f * h)))));
    }

    /**
     * Sets the text size for a Paint object so a given string of text will be a
     * given width.
     *
     * @param paint             instance of Paint to set the text size for
     * @param desiredWidth      desired width
     * @param text              the text that should be that width
     */
    private void setTextSizeForWidth(Paint paint, float desiredWidth, String text, float fudge) {
        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float testTextSize = 72f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = (testTextSize * desiredWidth / bounds.width()) * fudge;

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }

    /**
     * Given a string of text and a paint instance, determine the amount of screen width that
     * the text will take up.
     *
     * @param text      text to be drawn
     * @param paint     instance of Paint to be used to draw text
     * @return          text width
     */
    private float textWidth(String text, Paint paint) {
        return textDimensions(text, paint).width();
    }

    /**
     *
     * @param text      text to be drawn
     * @param paint     instance of Paint to be used to draw text
     * @return          text height
     */
    private float textHeight(String text, Paint paint) {
        return textDimensions(text, paint).height();
    }

    /**
     * Given a string of text and a paint instance, determine the area, both width and height,
     * that the text will take up.
     *
     * @return      area as a Rect instance
     */
    private Rect textDimensions(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    private final double TAU = 2 * Math.PI;

    /**
     * Calculate the sine of a given ratio. Unlike Math.sin, this method works with ratios
     * instead of radians, i.e. it multiples the given ratio by 2π before passing it off to
     * Math.sin().
     *
     * @param ratio     value between 0 and 1
     * @return          sin result
     */
    private double sin(double ratio) {
        return Math.sin(TAU * ratio);
    }

    /**
     * Calculate the cosine of a given ratio. Unlike Math.cos, this method works with ratios
     * instead of radians, i.e. it multiples the given ratio by 2π before passing it off to
     * Math.cos().
     *
     * @param ratio     value between 0 and 1
     * @return          sin result
     */
    private double cos(double ratio) {
        return Math.cos(TAU * ratio);
    }

    /**
     *
     * @return      ratio to rotate
     */
    private double rot() {
        if (settings.rotateTime() || settings.isAMPM()) {
            return 0; //Math.PI;
        } else {
            return 0.5;
        }
    }

    /**
     * Reverse an int array.
     *
     * @param a     array
     */
    protected void reverseArray(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int temp = a[i];
            a[i] = a[a.length - i - 1];
            a[a.length - 1] = temp;
        }
    }

    /**
     * Real modulus.
     *
     * @param n     numerator
     * @param d     denominator
     * @return      modulo
     */
    protected int mod(int n, int d) {
        if (n < 0) {
            return (d + (n % d));
        } else {
            return (n % d);
        }
    }




    /**
     * @deprecated
     *
     * @param canvas
     * @param bounds
     */
    private void drawColorWheelSplit2(Canvas canvas, Rect bounds) {
        float cx = centerX(bounds);
        float cy = centerY(bounds);

        int hd = timeSystem.hoursInDay();
        int hc = timeSystem.hoursOnClock();
        //int mc = timeSystem.minutesOnClock();

        int half = hd / 2;
        int qtr  = hd / 4;

        float w  = faceRadius(bounds);

        // TODO: spare option?
        //float r0 = sparse ? spotRadius(1.5f * hc) : spotRadius();
        float r0 = spotRadius(bounds);
        float r1 = w - r0;
        //float r2 = r0 + (r1 * 1.15f);

        float x, y;
        double r;

        int i;
        int[] colors;

        //colors = clockColors();
        //colors = hourColors();
        colors = colorWheel.colors(timeSystem.hoursInDay());

        paint = new Paint();
        paint.setAntiAlias(settings.getBackground() != 2);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10.0f);
        paint.setStyle(Paint.Style.FILL);
        paint.clearShadowLayer();

        /*
        for (i = 0; i < hc; i++) {
            r = ((double) i) / hc;

            //c = ratioToColor(colorCorrect(r));
            paint.setColor(colors[i]);

            x = (float) (cx + r0 * sin(r + rot()));
            y = (float) (cy - r0 * cos(r + rot()));

            //circlePaint.setShadowLayer(3.0f, 0f, 0f, c);
            canvas.drawCircle(x, y, r1, paint);

            //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
            //y = (float) (cy - r0 * cos(r + rot() - 0.041666667));
        }
        */


        // draw hours
        for (i = 0; i < hc; i++) {
            r = ((double) i) / hc;

            //c = ratioToColor(colorCorrect(r));
            paint.setColor(colors[i + half]);

            x = (float) (cx + r0 * sin(r + rot()));
            y = (float) (cy - r0 * cos(r + rot()));

            //circlePaint.setShadowLayer(3.0f, 0f, 0f, c);
            canvas.drawCircle(x, y, r1 / 2, paint);

            //x = (float) (cx + r0 * sin(r + rot() - 0.041666667));
            //y = (float) (cy - r0 * cos(r + rot() - 0.041666667)); //Math.sqrt(radius*radius + x*x);
        }

        int[] hourColors = colorWheel.colors(timeSystem.hoursInDay());

        int h = (int) (timeSystem.timeRatios()[0] * hd);
        i = mod(h - (hc / 2), hc);

        int q = half + mod(h + qtr, hc);

        // TODO: need to get the color from the full specturm of hour colors
        //int[] c = {hourColors[mod(i + half, hd)], colors[i]};
        //int[] c = {colors[i], colors[mod(i - 1, hc)]};
        int[] c = {colors[i], hourColors[q]};

        if (h < hc - qtr || h >= hc + qtr) {
            reverseArray(c);
        }

        r = (double) i / hc;

        //r = ((double) h) / hc;

        x = (float) (cx + r0 * sin(r + rot()));
        y = (float) (cy - r0 * cos(r + rot()));

        //float x1 = (float) (cx + (r0 - r0) * sin(r + rot()));
        //float y1 = (float) (cy - (r0 - r0) * cos(r + rot()));

        //float x2 = (float) (cx + (r0 + r0) * sin(r + rot()));
        //float y2 = (float) (cy - (r0 + r0) * cos(r + rot()));

        //int a = (int) (180 / Math.PI * Math.atan2(y2 - y1, x2 - x1));

        float a = (float) r * 360 - 90;

        drawSemiCircles(c, x, y, r1, a);
    }

    /**
     * @deprecated
     *
     * @param canvas
     * @param bounds
     */
    private void drawColorWheelSplit(Canvas canvas, Rect bounds) {
        float cx = centerX(bounds);
        float cy = centerY(bounds);

        int hd = timeSystem.hoursInDay();
        int hc = timeSystem.hoursOnClock();

        int half = hd / 2;
        int qtr = hd / 4;

        float w = faceRadius(bounds);

        // TODO: spare option?
        //float r0 = sparse ? spotRadius(1.5f * hc) : spotRadius();
        float r0 = spotRadius(bounds);
        float r1 = w - r0;
        //float r2 = r0 + (r1 * 1.15f);

        float x, y;
        double r;

        int i, j, s;
        int[] colors;

        colors = colorWheel.colors(hd); //clockColors();

        int h = (int) (timeSystem.timeRatios()[0] * hd);

        if (h < hc - qtr || h >= hc + qtr) {
            s = half;
        } else {
            s = 0;
        }

        int q = half + mod(h + qtr, hc);

        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(false);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStrokeWidth(10.0f);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.clearShadowLayer();

        //log("Q: " + q);

        for (i = 0; i < q; i++) {
            j = mod(i + s, hd);

            r = ((double) j) / hc;

            x = (float) (cx + r0 * sin(r + rot()));
            y = (float) (cy - r0 * cos(r + rot()));

            circlePaint.setColor(colors[j]);

            //circlePaint.setShadowLayer(3.0f, 0f, 0f, c);
            canvas.drawCircle(x, y, r1, circlePaint);
        }

        // draw semi-circles

        int hx = (int) ((timeSystem.timeRatios()[0] * hd) + 0.5);

        i = mod(hx - (hc / 2), hc);
        //int q = half + mod(h + qtr, hc);
        int[] c = {colors[i], colors[q]};

        //if (h < hc - qtr || h >= hc + qtr) {
        //    reverseArray(c);
        //}

        r = (double) i / hc;
        //r = ((double) h) / hc;

        x = (float) (cx + r0 * sin(r + rot()));
        y = (float) (cy - r0 * cos(r + rot()));

        float a = (float) r * 360 - 90;

        drawSemiCircles(c, x, y, r1, a);
    }

    /**
     * @deprecated
     *
     * @param c     colors
     * @param x     center x
     * @param y     center y
     * @param r     radius
     * @param a     start angle
     */
    private void drawSemiCircles(int[] c, float x, float y, float r, float a) {
        float cx = centerX(bounds);
        float cy = centerY(bounds);

        int hd = timeSystem.hoursInDay();
        int hc = timeSystem.hoursOnClock();

        int i;

        int h = (int) (timeSystem.timeRatios()[0] * hd);

        i = mod(h - (hc / 2), hc);

        final RectF oval = new RectF();
        oval.set(x - r, y - r, x + r, y + r);

        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(false);
        circlePaint.setStrokeWidth(10.0f);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.clearShadowLayer();

        circlePaint.setColor(c[0]);
        canvas.drawArc(oval, a, 180, true, circlePaint);

        circlePaint.setColor(c[1]);
        canvas.drawArc(oval, a, -180, true, circlePaint);
    }

}
