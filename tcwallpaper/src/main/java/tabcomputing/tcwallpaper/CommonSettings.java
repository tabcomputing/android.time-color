package tabcomputing.tcwallpaper;

import android.graphics.Typeface;

import tabcomputing.library.clock.DecimalTime;
import tabcomputing.library.clock.DuodecimalTime;
import tabcomputing.library.clock.HexadecimalTime;
import tabcomputing.library.clock.HeximalTime;
import tabcomputing.library.clock.MilitaryTime;
import tabcomputing.library.clock.StandardTime;
import tabcomputing.library.clock.TimeSystem;
import tabcomputing.library.color.ColorWheel;

/**
 * Common Pattern Settings
 */
public class CommonSettings extends AbstractSettings {

    public static final String KEY_OWNED = "owned";

    public static final String KEY_COLOR_GAMUT    = "colorGamut";
    public static final String KEY_COLOR_DAYLIGHT = "colorDaylight";
    public static final String KEY_COLOR_DUPLEX   = "colorDuplex";
    public static final String KEY_COLOR_SWAP     = "colorSwap";

    public static final String KEY_TIME_SYSTEM    = "timeSystem";
    public static final String KEY_TIME_ROTATE    = "timeRotate";
    public static final String KEY_TIME_SECONDS   = "timeSeconds";

    // not all patterns use these but they aren't atypical
    public static final String KEY_ORIENTATION = "orientation";
    public static final String KEY_TYPEFACE       = "typeface";

    public static final int TIME_TRADITIONAL = 0;
    public static final int TIME_MILITARY = 1;
    public static final int TIME_HEXADECIMAL = 2;
    public static final int TIME_HEXIMAL = 3;
    public static final int TIME_DECIMAL = 4;
    public static final int TIME_DUODECIMAL = 5;

    private ColorWheel colorWheel;
    private TimeSystem timeSystem;
    private Typeface   typeface;

    /**
     * Get instance of currently selected time system.
     */
    public TimeSystem getTimeSystem() {
        if (timeSystem == null) {
            newTimeSystem();
        }
        return timeSystem;
    }

    public void resetTimeSystem() {
        timeSystem = newTimeSystem();
    }

    public TimeSystem newTimeSystem() {
        int timeType = integerSettings.get(KEY_TIME_SYSTEM);
        switch (timeType) {
            case TIME_HEXADECIMAL:
                timeSystem = new HexadecimalTime();
                break;
            case TIME_HEXIMAL:
                timeSystem = new HeximalTime();
                break;
            case TIME_DECIMAL:
                timeSystem = new DecimalTime();
                break;
            case TIME_DUODECIMAL:
                timeSystem = new DuodecimalTime();
                break;
            default:
                // TODO: can just be one class?
                if (isDuplexed()) {
                    timeSystem = new StandardTime();
                } else {
                    timeSystem = new MilitaryTime();
                }
                break;
        }

        // TODO: set base conversion
        //timeSystem.setBaseConverted(baseConvert());

        return timeSystem;
    }

    /**
     *
     * @return      color wheel
     */
    public ColorWheel getColorWheel() {
        if (colorWheel == null) {
            colorWheel = newColorWheel();
        }
        return colorWheel;
    }

    private ColorWheel newColorWheel() {
        ColorWheel colorWheel;

        int     gamut    = getInteger(KEY_COLOR_GAMUT);
        boolean daylight = getBoolean(KEY_COLOR_DAYLIGHT);

        colorWheel = new ColorWheel();

        colorWheel.setColorGamut(gamut);

        if (daylight) {
            colorWheel.setDaylightFactor(0.7f);
        }

        return colorWheel;
    }

    /**
     *
     * @return      integer for type of color wheel
     */
    public int getColorGamut() {
        return getInteger(KEY_COLOR_GAMUT);
    }

    /**
     *
     * @return      true if the color wheel should repeat twice a day
     */
    public boolean isDuplexed() {
        return getBoolean(KEY_COLOR_DUPLEX);
    }

    /**
     *
     * @return      true is colors should be adjusted for time of day
     */
    public boolean isDaylight() {
        return getBoolean(KEY_COLOR_DAYLIGHT);
    }

    /**
     *
     * @return      true if seconds should be displayed
     */
    public boolean displaySeconds() {
        return getBoolean(KEY_TIME_SECONDS);
    }

    /**
     *
     * @return      true if clock colors are rotated
     */
    public boolean isRotated() {
        return getBoolean(KEY_TIME_ROTATE);
    }

    /**
     *
     * @return      true if colors should be swapped
     */
    public boolean isSwapped() {
        return getBoolean(KEY_COLOR_SWAP);
    }

    /**
     * Get orientation, which typically will have the values 0, 1 or 2 for horizontal,
     * vertical or rotating, respectively.
     *
     * @return      integer value
     */
    public int getOrientation() {
        return getInteger(KEY_ORIENTATION);
    }

    /**
     * Get current typeface.
     *
     * @return      typeface instance
     */
    public Typeface getTypeface() {
        if (typeface == null) {
            typeface = newTypeface();
        }
        return typeface;
    }

    /**
     * Get a new typeface.
     *
     * @return              typeface
     */
    public Typeface newTypeface() {
        //AssetManager assets = context.getAssets();
        Typeface font;

        int typeface = getInteger(KEY_TYPEFACE);

        switch (typeface) {
            case 7:
                font = Typeface.createFromAsset(assets, "arcade.ttf");
                break;
            case 6:
                font = Typeface.createFromAsset(assets, "basic.ttf");
                break;
            case 5:
                font = Typeface.createFromAsset(assets, "cubes.ttf");
                break;
            case 4:
                font = Typeface.createFromAsset(assets, "digital.ttf");
                break;
            case 3:
                font = Typeface.MONOSPACE;
                break;
            case 2:
                font = Typeface.SANS_SERIF;
                break;
            case 1:
                font = Typeface.SERIF;
                break;
            default:
                font = Typeface.DEFAULT;
        }
        return font;
    }

    /**
     *
     * @return
     */
    public FontScale typefaceScale() {
        return new FontScale();
    }

    /**
     * Time between ticks on the clock.
     *
     * @param wSeconds      include seconds in calculation?
     * @return              time in milliseconds
     */
    public long getTickTime(boolean wSeconds) {
        return timeSystem.tickTime(wSeconds);
    }

    // Setting Change Monitor ?

    /**
     *
     *
    public void setOwned(boolean owned) {
        setBoolean(KEY_OWNED, owned);
    }
    */

    public boolean isOwned() {
        return getBoolean(KEY_OWNED);
    }

}
