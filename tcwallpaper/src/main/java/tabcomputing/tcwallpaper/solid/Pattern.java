package tabcomputing.tcwallpaper.solid;

import android.graphics.Canvas;

import tabcomputing.tcwallpaper.BasePattern;

/**
 * Make background a solid color based on time. By default it is by hour, but if
 * colors are reversed in the settings it is instead by the minute.
 */
public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    private Settings settings;

    protected void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    @Override
    public void draw(Canvas canvas) {
        int[] colors = timeColors();

        if (settings.isSwapped()) {
            canvas.drawColor(colors[1]);
        } else {
            canvas.drawColor(colors[0]);
        }

        if (settings.useGlare()) {
            drawSunGlare(canvas);
        }
    }

}
