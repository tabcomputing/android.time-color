package tabcomputing.library.color;

import android.graphics.Color;

public class Colour {

    private int color;

    public Colour(int red, int green, int blue, int alpha) {
        this.color = Color.argb(alpha, red, green, blue);
    }

    public Colour(double red, double green, double blue, double alpha) {
        int r = (int) (256 * red);
        int g = (int) (256 * green);
        int b = (int) (256 * blue);
        int a = (int) (256 * alpha);

        this.color = Color.argb(a, r, g, b);
    }

    public void setAlpha(double alpha) {
        int a = (int) (256 * alpha);
        int c = Color.argb(a, Color.red(color), Color.green(color), Color.blue(color));
        this.color = c;
    }

    public void setRed(double red) {
        int r = (int) (256 * red);
        int c = Color.argb(Color.alpha(color), r, Color.green(color), Color.blue(color));
        this.color = c;
    }

    public void setGreen(double green) {
        int g = (int) (256 * green);
        int c = Color.argb(Color.alpha(color), Color.red(color), g, Color.blue(color));
        this.color = c;
    }

    public void setBlue(double blue) {
        int b = (int) (256 * blue);
        int c = Color.argb(Color.alpha(color), Color.red(color), Color.green(color), b);
        this.color = c;
    }

    /**
     * TODO: better name for this method
     *
     * @param ratio     ratio to reduce
     * @return          ratio reduced to 0 to 1
     */
    private double reduce(double ratio) {
        if (ratio < 0) {
            int n = (int) ratio;
            return (1.0 - (ratio - n));
        } else {
            int n = (int) ratio;
            return (ratio - n);
        }
    }

    /**
     * Get hex code for the color.
     *
     * @return hex code for color
     */
    public String hexString() {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format("#%02x%02x%02x", r, g, b);
    }

}
