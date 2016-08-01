package tabcomputing.library.paper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class BitmapReuse {

    private Bitmap b = null;
    private Canvas c = null;
    private Rect r = new Rect();

    public BitmapReuse() {
        // kind of a waste but it should avoid null exceptions
        b = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
    }

    public BitmapReuse(Rect bounds) {
        r = bounds;
        b = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
    }

    public BitmapReuse(RectF bounds) {
        Rect rect = new Rect((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom);
        r = rect;
        b = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
    }

    public Bitmap getBitmap() {
        return b;
    }

    public Canvas getCanvas() {
        return c;
    }

    public void reset() {
        //c.restore();
        c.drawColor(Color.TRANSPARENT);
        //c.save();
    }

    public void reset(Rect bounds) {
        if (! bounds.equals(r)) {
            Log.d("log", "BOUNDS NOT EQUAL!!!!");
            r = bounds;
            b = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
            c = new Canvas(b);
            //c.save();
        }
        //c.restore();
        c.drawColor(Color.TRANSPARENT);
        //c.save();
    }

    public void recycle() { //for e.g. onDestroy() Activity or other
        if (b != null)
            b.recycle();
    }

}
