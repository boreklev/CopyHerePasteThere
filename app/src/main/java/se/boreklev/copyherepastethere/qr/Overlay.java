package se.boreklev.copyherepastethere.qr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

//import android.support.v7.widget.AppCompatImageView;

/**
 * Created by matti on 2017-09-12.
 */

public class Overlay extends View {//AppCompatImageView {
    private Rect mRect;
    private int mColor;
    private float mWidthSizer;
    private float mHeightSizer;

    public Overlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        //In versions > 3.0 need to define layer Type
        if (android.os.Build.VERSION.SDK_INT >= 11)
        {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        int color = Color.BLACK;
        mColor = Color.argb(Math.round(Color.alpha(color) * 0.9f), Color.red(color), Color.green(color), Color.blue(color));
        mWidthSizer = 0.5f;
        mHeightSizer = 0f;
        mRect = new Rect();
    }

    public void setRectangle() {
        int sw = getWidth();
        int sh = getHeight();
        int w = sh > sw ? Math.round(sw * mWidthSizer) : Math.round(sh * mWidthSizer);
        int h = w;
        if(mHeightSizer != 0f)
            h = sh > sw ? Math.round(sh * mHeightSizer) : Math.round(sw * mHeightSizer);
        mRect.left = (sw / 2) - (w / 2);
        mRect.top = (int)(Math.round(sh * 0.8) / 2) - (h / 2);
        mRect.right = mRect.left + w;
        mRect.bottom = mRect.top + h;
        Log.i("OverLay", "mHeightSizer: " + mHeightSizer + " - " + h);
        //Redraw after defining rectangle
        postInvalidate();
    }

    public Rect getRect(Bitmap bm) {
        double w = (double)bm.getWidth() / (double)getWidth();
        double h = (double)bm.getHeight() / (double)getHeight();
        return new Rect((int)Math.round(mRect.left * w), (int)Math.round(mRect.top * h), (int)Math.round(mRect.right * w), (int)Math.round(mRect.bottom * h));
    }

    public void setWidthSizer(float f) {
        mWidthSizer = f;
    }

    /*
     * Setting mHeightSizer to 0 will make a square.
     */
    public void setHeightSizer(float f) {
        mHeightSizer = f;
    }

    public boolean isSquare() {
        return mHeightSizer == 0 ? true : false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed) {
            setRectangle();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mColor);
        //Draw Overlay
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        float t = getWidth() > getHeight() ? getWidth() * 0.008f :getHeight() * 0.008f;
        float l = mRect.width() < mRect.height() ? mRect.width() * 0.15f : mRect.height() * 0.15f;
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(t);
        t = t / 2;
        // upper left corner
        canvas.drawLine(mRect.left-t, mRect.top, mRect.left+l+t, mRect.top, paint);
        canvas.drawLine(mRect.left, mRect.top-t, mRect.left, mRect.top+l+t, paint);
        // upper right corner
        canvas.drawLine(mRect.right-l-t, mRect.top, mRect.right+t, mRect.top, paint);
        canvas.drawLine(mRect.right, mRect.top-t, mRect.right, mRect.top+l+t, paint);
        // lower right corner
        canvas.drawLine(mRect.right-l-t, mRect.bottom, mRect.right+t, mRect.bottom, paint);
        canvas.drawLine(mRect.right, mRect.bottom-l-t, mRect.right, mRect.bottom+t, paint);
        // lower left corner
        canvas.drawLine(mRect.left-t, mRect.bottom, mRect.left+l+t, mRect.bottom, paint);
        canvas.drawLine(mRect.left, mRect.bottom-l-t, mRect.left, mRect.bottom+t, paint);

        //Draw ('cut out') transparent shape
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRect(mRect.left, mRect.top, mRect.right, mRect.bottom, paint);
    }
}
