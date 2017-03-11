package tabcomputing.chronochrome.solid;

import android.content.Context;
import android.graphics.Canvas;

import tabcomputing.library.paper.BasePattern;

/**
 * Make background a solid color based on time. By default it is by hour, but if
 * colors are reversed in the settings it is instead by the minute.
 */
public class Pattern extends BasePattern {

    public Pattern(Wallpaper wallpaper) {
        setContext(wallpaper);
        setSettings(wallpaper.getSettings());
    }

    public Pattern(Context context, Settings settings) {
        setContext(context);
        setSettings(settings);

        resetPreferences();
    }

    private Settings settings;

    protected void setSettings(Settings settings) {
        super.setSettings(settings);
        this.settings = settings;
    }

    @Override
    public void drawPattern(Canvas canvas) {
        int[] colors = timeColors(false);  // include seconds

        int part = settings.timePart();

        canvas.drawColor(colors[part]);

        if (settings.useGlare()) {
            drawSunGlare(canvas);
        }
    }

}
