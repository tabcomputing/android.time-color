package tabcomputing.library.paper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.ArrayList;

public class UpsaleManager {

    public UpsaleManager(Context context, ArrayList<Integer> msgIds) {
        this.context = context;
        this.messageIds = msgIds;
    }

    private Context context;

    private ArrayList<Integer> messageIds;

    /**
     * Setup the messages.
     *
     * @param stringResIds      array list of string resource ids
     */
    public void setMessageIds(ArrayList<Integer> stringResIds) {
        messageIds = stringResIds;
    }

    /**
     * Draw nag message on canvas.
     *
     * @param canvas    drawing canvas
     * @param pick      message number
     */
    public void drawMessage(Canvas canvas, int pick) {
        if (pick < 0) { return; }

        String msg;

        Resources res = context.getResources();

        float cx = canvas.getWidth() / 2;
        float cy = canvas.getHeight() / 2;

        int idx = pick % messageIds.size();
        int mid = messageIds.get(idx);

        msg = res.getString(mid);

        //float gap = canvas.getWidth() * 0.05f;
        float cut = canvas.getWidth() * 0.9f;

        TextPaint paint = new TextPaint();
        paint.setAntiAlias(true);
        paint.setTextSize(80.0f);
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(10.0f, 5.0f, 5.0f, Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);

        StaticLayout txt;

        canvas.save();

        canvas.translate(cx, cy / 1.6f);
        txt = new StaticLayout(msg, paint, (int) (cut), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);
        txt.draw(canvas);

        canvas.translate(0, txt.getHeight() + 40);
        msg = "Time + Color = Wallpaper";
        paint.setTextSize(55.0f);
        txt = new StaticLayout(msg, paint, (int) (cut), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);
        txt.draw(canvas);

        canvas.restore();
    }

}
