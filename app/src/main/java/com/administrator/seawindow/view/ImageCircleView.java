package com.administrator.seawindow.view;

/**
 * Created by libm on 2017/4/21.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageCircleView extends ImageView {
    Path path;
    public PaintFlagsDrawFilter mPaintFlagsDrawFilter;
    private Paint paint;

    public ImageCircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ImageCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageCircleView(Context context) {
        super(context);
        init();
    }

    public void init() {
        this.paint = new Paint();
    }

    protected void onDraw(Canvas cns) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = circleDraw(bitmap);
            Rect rect1 = new Rect(0, 0, b.getWidth(), b.getHeight());
            Rect rect2 = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.paint.reset();
            cns.drawBitmap(b, rect1, rect2, this.paint);
        } else {
            super.onDraw(cns);
        }
    }

    private Bitmap circleDraw(Bitmap bitmap) {
        int r = 0;
        int width = getWidth();
        int height = getHeight();
        if (width > height) {
            r = height;
        } else {
            r = width;
        }

        Bitmap output = Bitmap.createBitmap(r, r, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Rect rect = new Rect(0, 0, r, r);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-1);

        canvas.drawCircle(r / 2, r / 2, r / 2, this.paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        bitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), true);
        canvas.drawBitmap(bitmap, rect, rect, this.paint);
        return output;
    }
}
