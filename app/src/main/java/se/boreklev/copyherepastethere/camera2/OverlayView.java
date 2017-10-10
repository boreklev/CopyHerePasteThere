package se.boreklev.copyherepastethere.camera2;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import se.boreklev.copyherepastethere.CopyHerePasteThere;
import se.boreklev.copyherepastethere.R;

/**
 * Created by matti on 2017-09-12.
 */

public class OverlayView extends View {
    // static
    private static final int sBorderThickness = 5;
    private static final int sGuideThickness = 1;
    private static final int sCornerThickness = 10;

    private enum Motion {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        NONE
    }
    // member
    private Motion mDragged;
    private final Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mGuidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mCornerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mViewFinderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect mRect;
    private float mWidthSizer;
    private float mHeightSizer;

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWidthSizer = 0.5f;
        mHeightSizer = 0f;

        int white = Color.WHITE;
        mBorderPaint.setColor(Color.argb(Math.round(Color.alpha(white) * 0.7f), Color.red(white), Color.green(white), Color.blue(white)));
        mBorderPaint.setStrokeWidth(sBorderThickness);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mGuidePaint.setColor(white);
        mGuidePaint.setStrokeWidth(sGuideThickness);
        mGuidePaint.setStyle(Paint.Style.STROKE);
        mCornerPaint.setColor(white);
        mCornerPaint.setStrokeWidth(sCornerThickness);
        mCornerPaint.setStyle(Paint.Style.STROKE);
        int black = Color.BLACK;
        mBackgroundPaint.setColor(Color.argb(Math.round(Color.alpha(black) * 0.8f), Color.red(black), Color.green(black), Color.blue(black)));
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mViewFinderPaint.setColor(black);
        mViewFinderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        mDragged = Motion.NONE;
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

    /**
     * Crop image to overaly mask. Since image and mask might not have the same size we translate
     * poisition to crop at right bounds.
     *
     * @param bm - image to crop.
     * @return - cropped bitmap.
     */
    public Bitmap cropImage(Bitmap bm) {
        double w = (double)bm.getWidth() / (double)getWidth();
        double h = (double)bm.getHeight() / (double)getHeight();
        int left = (int)Math.round(mRect.left * w);
        int top = (int)Math.round(mRect.top * h);
        Bitmap croppedBitmap = Bitmap.createBitmap(bm, left, top, (int)Math.round(mRect.right * w) - left, (int)Math.round(mRect.bottom * h) - top);
        return croppedBitmap;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed) {
            setRectangle();
        }
    }

    public static float[] getRelativeCoords(MotionEvent e)
    {
        // MapView
        View contentView = CopyHerePasteThere.getMe().getWindow().
                findViewById(Window.ID_ANDROID_CONTENT);
        return new float[] {
                e.getRawX() - contentView.getLeft(),
                e.getRawY() - contentView.getTop()};
    }

    /**
     * Override to add possibility to change size and/or position of the crop area.
     *
     * @param e - the event.
     * @return - returns true if all ok.
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);
        switch(e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDragged = Motion.NONE;
                float m = mCornerPaint.getStrokeWidth() * 4f;
                int[] so = new int[2];
                getLocationInWindow(so);
                float xs = e.getRawX() - m - so[0];
                float xe = e.getRawX() + m - so[0];
                float ys = e.getRawY() - m - so[1];
                float ye = e.getRawY() + m - so[1];
                int[] ii = new int[2];
                this.getLocationOnScreen(ii);
                if(mRect.left > xs && mRect.left < xe) {
                    if(ys > mRect.top && ye < mRect.bottom)
                        mDragged = Motion.LEFT;
                    else if (mRect.top > ys && mRect.top < ye)
                        mDragged = Motion.TOP_LEFT;
                    else if(mRect.bottom > ys && mRect.bottom < ye)
                        mDragged = Motion.BOTTOM_LEFT;
                } else if(mRect.right > xs && mRect.right < xe) {
                    if(ys > mRect.top && ye < mRect.bottom)
                        mDragged = Motion.RIGHT;
                    else if (mRect.top > ys && mRect.top < ye)
                        mDragged = Motion.TOP_RIGHT;
                    else if(mRect.bottom > ys && mRect.bottom < ye)
                        mDragged = Motion.BOTTOM_RIGHT;
                } else if(mRect.top > ys && mRect.top < ye) {
                    if(xs > mRect.left && xe < mRect.right)
                        mDragged = Motion.TOP;
                } else if(mRect.bottom > ys && mRect.bottom < ye) {
                    if(xs > mRect.left && xe < mRect.right)
                        mDragged = Motion.BOTTOM;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float x = e.getX();
                float y = e.getY();
                float s = mCornerPaint.getStrokeWidth() * 10;
                switch (mDragged) {
                    case TOP_LEFT:
                        mRect.left = x < (mRect.right - s) ? (int)x : (int)(mRect.right - s);
                        mRect.top = y < (mRect.bottom - s) ? (int)y : (int)(mRect.bottom - s);
                        postInvalidate();
                        break;
                    case TOP:
                        mRect.top = y < (mRect.bottom - s) ? (int)y : (int)(mRect.bottom - s);
                        postInvalidate();
                        break;
                    case TOP_RIGHT:
                        mRect.right = x > (mRect.left + s) ? (int)x : (int)(mRect.left + s);
                        mRect.top = y < (mRect.bottom - s) ? (int)y : (int)(mRect.bottom - s);
                        postInvalidate();
                        break;
                    case RIGHT:
                        mRect.right = x > (mRect.left + s) ? (int)x : (int)(mRect.left + s);
                        postInvalidate();
                        break;
                    case BOTTOM_RIGHT:
                        mRect.right = x > (mRect.left + s) ? (int)x : (int)(mRect.left + s);
                        mRect.bottom = y > (mRect.top + s) ? (int)y : (int)(mRect.top + s);
                        postInvalidate();
                        break;
                    case BOTTOM:
                        mRect.bottom = y > (mRect.top + s) ? (int)y : (int)(mRect.top + s);
                        postInvalidate();
                        break;
                    case BOTTOM_LEFT:
                        mRect.left = x < (mRect.right - s) ? (int)x : (int)(mRect.right - s);
                        mRect.bottom = y > (mRect.top + s) ? (int)y : (int)(mRect.top + s);
                        postInvalidate();
                        break;
                    case LEFT:
                        mRect.left = x < (mRect.right - s) ? (int)x : (int)(mRect.right - s);
                        postInvalidate();
                        break;
                    default:
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //Draw Overlay background
        canvas.drawPaint(mBackgroundPaint);

        //Draw transparent shape
        canvas.drawRect(mRect.left, mRect.top, mRect.right, mRect.bottom, mViewFinderPaint);

        // Draw border lines
        canvas.drawRect(mRect.left, mRect.top, mRect.right, mRect.bottom, mBorderPaint);

        // Draw corners
        float offset = sCornerThickness / 2;
        float length = mCornerPaint.getStrokeWidth() * 2 + offset;
        // upper left corner
        canvas.drawLine(mRect.left-sCornerThickness, mRect.top-offset, mRect.left+length, mRect.top-offset, mCornerPaint);
        canvas.drawLine(mRect.left-offset, mRect.top-sCornerThickness, mRect.left-offset, mRect.top+length, mCornerPaint);
        // upper right corner
        canvas.drawLine(mRect.right-length, mRect.top-offset, mRect.right+sCornerThickness, mRect.top-offset, mCornerPaint);
        canvas.drawLine(mRect.right+offset, mRect.top-sCornerThickness, mRect.right+offset, mRect.top+length, mCornerPaint);
        // lower right corner
        canvas.drawLine(mRect.right-length, mRect.bottom+offset, mRect.right+sCornerThickness, mRect.bottom+offset, mCornerPaint);
        canvas.drawLine(mRect.right+offset, mRect.bottom-length, mRect.right+offset, mRect.bottom+sCornerThickness, mCornerPaint);
        // lower left corner
        canvas.drawLine(mRect.left-sCornerThickness, mRect.bottom+offset, mRect.left+length, mRect.bottom+offset, mCornerPaint);
        canvas.drawLine(mRect.left-offset, mRect.bottom-length, mRect.left-offset, mRect.bottom+sCornerThickness, mCornerPaint);

        // Draw guide lines
        float w = mRect.width() / 3;
        float h = mRect.height() / 3;
        // Vertical
        canvas.drawLine(mRect.left+w, mRect.top, mRect.left+w, mRect.bottom, mGuidePaint);
        canvas.drawLine(mRect.right-w, mRect.top, mRect.right-w, mRect.bottom, mGuidePaint);
        // Horizontal
        canvas.drawLine(mRect.left, mRect.top+h, mRect.right, mRect.top+h, mGuidePaint);
        canvas.drawLine(mRect.left, mRect.bottom-h, mRect.right, mRect.bottom-h, mGuidePaint);
    }
}
