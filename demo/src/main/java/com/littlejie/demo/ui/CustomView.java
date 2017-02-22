package com.littlejie.demo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.littlejie.core.util.MiscUtil;

/**
 * Created by littlejie on 2017/1/18.
 */

public class CustomView extends View {

    public static final int DEFAULT_SIZE = 400;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MiscUtil.measure(widthMeasureSpec, DEFAULT_SIZE),
                MiscUtil.measure(heightMeasureSpec, DEFAULT_SIZE));
    }

    Paint mPaint = new Paint();
    int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED};
    RectF mRectF = new RectF(0, 0, 400, 400);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        SweepGradient gradient = new SweepGradient(200, 200, colors, null);
        Matrix matrix = new Matrix();
        matrix.postRotate(135);
        gradient.setLocalMatrix(matrix);
        mPaint.setShader(gradient);
        canvas.drawArc(mRectF, 135, 360, false, mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
