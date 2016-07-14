package tabcomputing.tcwallpaper.solid;

import android.graphics.Canvas;

import tabcomputing.library.paper.AbstractPattern;

/**
 * Make background a solid color based on time. By default it is by hour, but if
 * colors are reversed in the settings it is instead by the minute.
 */
public class Pattern extends AbstractPattern {

    public Pattern(Settings settings) {
        setSettings(settings);
    }

    //public PatternSolid(Settings settings, TimeSystem timeSystem, ColorWheel colorWheel) {
    //    this.settings   = settings;
    //    this.timeSystem = timeSystem;
    //    this.colorWheel = colorWheel;
    //}

    @Override
    public void draw(Canvas canvas) {
        int[] colors = timeColors();

        if (settings.isSwapped()) {
            canvas.drawColor(colors[1]);
        } else {
            canvas.drawColor(colors[0]);
        }
    }

}
