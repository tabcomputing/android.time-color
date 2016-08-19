package tabcomputing.library.paper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class ScaledText {

    private String text = "";

    private float scaleX = 1.0f;
    private float scaleY = 1.0f;

    private float offsetX = 0.0f;
    private float offsetY = 0.0f;

    private float padding = 0;

    private int backgroundColor = Color.TRANSPARENT;
    private Bitmap backgroundBitmap = null;

    private Paint bkgPaint = new Paint();

    /**
     *
     * @param text      string
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Set the scaling parameters.
     *
     * @param offsetX   horizontal offset
     * @param offsetY   vertical offset
     * @param scaleX    horizontal scaling factor
     * @param scaleY    vertical scaling factor
     */
    public void setScale(float offsetX, float offsetY, float scaleX, float scaleY) {
        this.scaleX  = scaleX;
        this.scaleY  = scaleY;

        this.offsetX = offsetX;
        this.offsetY = offsetY;

        //this.offsetX = offsetX + (1 - scaleX);
        //this.offsetY = offsetY + (1 - scaleY);
    }

    public void setBackground(int color) {
        this.backgroundColor = color;
    }

    public void setBackground(Bitmap bitmap) {
        this.backgroundBitmap = bitmap;
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    /**
     * TODO: Allow for setting a background.
     *
     * @param paint     instance of Paint for drawing text
     * @return          bitmap
     */
    public Bitmap toBitmap(float width, float height, Paint paint) {
        //canvas.drawColor(tc[0]);
        //if (settings.getFade()) {
        //    drawFade(canvas, false);
        //} else {
        //    drawSolid(canvas, false);
        //}

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(textSizeForWidth(text, width, paint));

        Bitmap bmp = textAsBitmap(padding, paint);

        RectF rectF = getScale(width, height);

        return Bitmap.createScaledBitmap(bmp, (int) rectF.width(), (int) rectF.height(), false);
    }

    protected Bitmap textAsBitmap(float padding, Paint paint) {
        //Paint.FontMetrics fm = paint.getFontMetrics();
        float baseline = -paint.ascent(); // ascent() is negative

        int width  = (int) (paint.measureText(text) + (2 * padding));
        int height = (int) (baseline + paint.descent() + (2 * padding));

        // TODO: use BitmapReuse
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        canvas.drawColor(backgroundColor);

        if (backgroundBitmap != null) {
            canvas.drawBitmap(backgroundBitmap, 0, 0, getBkgPaint());
        }

        canvas.drawText(text, padding, baseline + padding, paint);

        return bmp;
    }

    protected RectF getScale(float width, float height) {
        float zx = width  * scaleX;
        float zy = height * scaleY;

        float ox = width  * offsetX;
        float oy = height * offsetY;

        return new RectF(ox, oy, zx + ox, zy + oy);
    }

    protected float textSizeForWidth(String text, float desiredWidth, Paint paint) {
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
        return (testTextSize * desiredWidth / bounds.width());
    }

    protected Paint getBkgPaint() {
        bkgPaint.reset();
        bkgPaint.setColor(Color.BLACK);
        return bkgPaint;
    }

}
