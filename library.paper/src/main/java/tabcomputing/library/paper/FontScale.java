package tabcomputing.library.paper;

/**
 *
 */
public class FontScale {

    public FontScale() {}

    public FontScale(float offsetX, float offsetY, float scaleX, float scaleY) {
        set(offsetX, offsetY, scaleX, scaleY);
    }

    public void set(float offsetX, float offsetY, float scaleX, float scaleY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.scaleX  = scaleX;
        this.scaleY  = scaleY;
    }

    public float offsetX = 0.0f;
    public float offsetY = 0.0f;

    public float scaleX = 1.0f;
    public float scaleY = 1.0f;

}
