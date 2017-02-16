package com.littlejie.demo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by littlejie on 2017/2/16.
 */

public class MoveView extends View {

    private int mDefaultSize = 200;
    private Paint mPaint;

    public MoveView(Context context) {
        super(context);
        init(context);
    }

    public MoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = measureSize(widthMeasureSpec, mDefaultSize);
        setMeasuredDimension(size, size);
    }

    private int measureSize(int measureSpec, int defaultSize) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        int size = getMeasuredWidth() / 2;
        canvas.drawCircle(size, size, size, mPaint);
    }

    private int mLastX, mLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - mLastX;
                int offsetY = y - mLastY;
//                layout(getLeft() + offsetX, getTop() + offsetY,
//                        getRight() + offsetX, getBottom() + offsetY);
                offsetLeftAndRight(offsetX);
                offsetTopAndBottom(offsetY);
                break;
        }
        return true;
    }
}
