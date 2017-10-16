package com.jacobtools.libaray;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hujian on 2017/9/13.
 */

public class SignatureView extends View {

    private Paint paint;
    private Path path;
    private float prevX, prevY;
    private Canvas bitmapCanvas;
    private Bitmap bitmap;

    /**
     * 默认背景为白色
     */
    private int bgColor = Color.WHITE;

    /**
     * 默认线的颜色为黑色
     */
    private int lineColor = Color.BLACK;

    /**
     * 线条宽度
     */
    private int lineWith = 4;

    public SignatureView(Context context) {
        super(context);
        init();
    }

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(lineColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineWith);
        paint.setStyle(Paint.Style.STROKE);

        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmapCanvas = new Canvas(bitmap);
        bitmapCanvas.drawColor(bgColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                prevX = event.getX();
                prevY = event.getY();
                path.moveTo(prevX, prevY);
                break;
            case MotionEvent.ACTION_MOVE: {
                float x = event.getX();
                float y = event.getY();

                final float dx = Math.abs(x - prevX);
                final float dy = Math.abs(y - prevY);

                if (dx >= 3 || dy >= 3) {
                    float cx = (x + prevX) / 2;
                    float cy = (y + prevY) / 2;

                    path.quadTo(prevX, prevY, cx, cy);

                    prevX = x;
                    prevY = y;
                }

                break;
            }
            case MotionEvent.ACTION_UP: {
                bitmapCanvas.drawPath(path, paint);
                path.reset();
                break;
            }
            default:
                break;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
    }

    /**
     * 清楚数据
     */
    public void clear() {
        if (bitmapCanvas != null) {
            bitmapCanvas.drawColor(bgColor);
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
                bitmapCanvas = new Canvas(bitmap);
                bitmapCanvas.drawColor(bgColor);
            }
            invalidate();
        }
    }

    /**
     * 获取签名的bitmap
     * @return
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * 设置背景颜色
     * @param bgColor
     */
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        if (bitmapCanvas != null) {
            bitmapCanvas.drawColor(bgColor);
        }
    }

    /**
     * 设置线条颜色
     * @param lineColor
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        paint.setColor(lineColor);
    }

    /**
     * 设置线条宽度
     * @param lineWith
     */
    public void setLineWith(int lineWith) {
        this.lineWith = lineWith;
        paint.setStrokeWidth(lineWith);
    }
}
