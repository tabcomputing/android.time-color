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
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Arrays;

import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.color.ColorWheel;

/**
 *
 */
public abstract class AbstractPaperView extends SurfaceView {
    private static final String DEBUG_TAG = "ClockView";

    protected AbstractPattern pattern;

    protected WidgetBar controls = new WidgetBar();


    public AbstractPaperView(Context context) {
        super(context, null);

        settings = getSettings();

        //setupClock();
    }

    //protected void setSettings(CommonSettings settings) {
    //    this.settings = settings;
    //}

    /**
     *
     */
    public void setupTouchListener() { }

    // TODO: Is this the right event for when action bar size might change?
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        controls.setHeight(actionBarHeight());
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
    //private static ClockSettings settings = ClockSettings.getInstance();
    private CommonSettings settings; //= ClockSettings.getInstance();

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

    /**
     * Override this.
     *
     * @return
     */
    public CommonSettings getSettings() {
        return null;
    }

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
        return timeSystem.tickTime(true); //settings.withSeconds());
    }

    private Canvas canvas;

    private Rect bounds = new Rect();

    /**
     * Draw routine.
     */
    protected void draw() {
        SurfaceHolder holder = getHolder(); //getSurfaceHolder();
        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                drawPattern(canvas);
                drawWidgets(canvas);
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
     * Override this in subclass if view needs any setting widgets.
     *
     * @param canvas    drawing canvas
     */
    protected void drawWidgets(Canvas canvas) { }

    /**
     *
     * @param canvas
     */
    private void drawPattern(Canvas canvas) {
        timeSystem = settings.getTimeSystem();
        colorWheel = settings.getColorWheel();

        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }

        canvas.getClipBounds(bounds);

        canvas.drawColor(Color.WHITE);

        pattern.draw(canvas);
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



    private void drawBackgroundWhite() {
        canvas.drawColor(Color.WHITE);
    }

    private void drawBackgroundBlack() {
        canvas.drawColor(Color.BLACK);
    }





    private boolean isColorDuplexed() {
        return (timeSystem.isDaySplit() && !settings.isDuplexed());
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

    /**
     * TODO: isAMPM() is always false -- it probably isn't actually needed for patterns, only the clock.
     *
     * @return      number of hours on clock face
     */
    protected int hoursOnClock() {
        if (settings.isAMPM()) {
            return timeSystem.hoursInDay() / 2;
        } else {
            return timeSystem.hoursInDay();
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
     *
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
     */

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

    /*
    private String[] clockNumbers() {
        String[] nums = timeSystem.clockNumbers(settings.getNumberSystem(), hoursOnClock());

        //nums = Arrays.copyOf(nums, hoursOnClock());

        return nums;
    }
    */

    /**
     * Return the current time as a series of ratios, one for each hand of the clock for
     * the active time system. This is different from timeRatios() in that standard time
     * splits the hours into day and night.
     *
     * @return series of time ratios
     */
    protected double[] handRatios(boolean all) {
        double[] ratios = timeSystem.handRatios();
        if (!all && !settings.withSeconds()) {
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
        return timeSystem.timeStamp(!settings.withSeconds());
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
        return timeSystem.timeStampSample(settings.withSeconds());
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
    protected float faceRadius(Rect bounds) {
        //return centerX(bounds) * 0.85f;
        float x = centerX(bounds);
        float y = centerY(bounds);
        if (x < y) {
            return x * 0.85f;
        } else {
            return y * 0.85f;
        }
    }
    protected float faceRadius(RectF bounds) {
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
        //if (settings.rotateTime() || settings.isAMPM()) {
        if (settings.isRotated() || settings.isAMPM()) {
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
     *
     * @param context       application context
     * @param message       message to toast
     */
    //protected void toast(Context context, String message) {
    //    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    //}

    /**
     * Action bar height. We use this to ensure the quick settings bar is the same height.
     *
     * @return              action bar height
     */
    protected float actionBarHeight() {
        Context context = getContext();
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        //int actionBarHeight = context.getResources().getDimensionPixelSize(tv.resourceId);
        return context.getResources().getDimension(tv.resourceId);
    }

}
