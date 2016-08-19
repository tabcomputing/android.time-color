package tabcomputing.library.paper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;

public class BitmapReuse {

    private Bitmap b = null;
    private Canvas c = null;
    private Rect r = new Rect();

    public BitmapReuse() { }

    public BitmapReuse(Rect bounds) {
        r = bounds;
        b = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
    }

    public BitmapReuse(RectF bounds) {
        Rect rect = new Rect((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom);
        r = rect;
        b = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
    }

    public Bitmap getBitmap() {
        return b;
    }

    public Canvas getCanvas() {
        return c;
    }

    public Canvas getCleanCanvas(Rect bounds) {
        reset(bounds);
        return c;
    }

    public void clear() {
        //c.restore();
        c.drawColor(Color.TRANSPARENT);
        //c.save();
    }

    public void reset(Rect bounds) {
        if (b == null || !bounds.equals(r)) {
            r = bounds;
            b = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
            c = new Canvas(b);
        }
        //c.restore();
        b.eraseColor(Color.TRANSPARENT);
        c.drawColor(Color.TRANSPARENT);
        //c.save();
    }

    public void recycle() { //for e.g. onDestroy() Activity or other
        if (b != null)
            b.recycle();
    }

    public void drawBitmap(BitmapReuse bitmap, float left, float top, Paint paint) {
        c.drawBitmap(bitmap.getBitmap(), left, top, paint);
    }

    public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {
        c.drawBitmap(bitmap, left, top, paint);
    }

}
