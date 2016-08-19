package tabcomputing.library.paper;

import android.graphics.RectF;

/**
 *
 */
public class FontScale {

    public FontScale() {}

    public FontScale(float offsetX, float offsetY, float scaleX, float scaleY) {
        set(offsetX, offsetY, scaleX, scaleY);
    }

    // TODO: should we apply an offset from the scale automatically?

    public void set(float offsetX, float offsetY, float scaleX, float scaleY) {
        this.scaleX  = scaleX;
        this.scaleY  = scaleY;

        this.offsetX = offsetX;
        this.offsetY = offsetY;

        //this.offsetX = offsetX + (1 - scaleX);
        //this.offsetY = offsetY + (1 - scaleY);
    }

    public float scaleX = 1.0f;
    public float scaleY = 1.0f;

    public float offsetX = 0.0f;
    public float offsetY = 0.0f;

    public RectF apply(float width, float height) {
        float zx = width  * scaleX;
        float zy = height * scaleY;

        float ox = width  * offsetX;
        float oy = height * offsetY;

        return new RectF(ox, oy, zx + ox, zy + oy);
    }

}
