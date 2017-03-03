package com.littlejie.demo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.littlejie.core.util.DisplayUtil;
import com.littlejie.core.util.MiscUtil;
import com.littlejie.demo.R;

/**
 * Created by littlejie on 2017/2/28.
 */

public class CustomTextView extends View {

    public static final String TAG = CustomTextView.class.getSimpleName();
    public static final String TEXT = "测试";
    private int mDefaultSize = 0;

    private TextPaint mTextPaint;
    private float mTextSize;
    private float mTextHeight;
    private float mTextWidth;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDefaultSize = MiscUtil.dipToPx(context, 150);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

        mTextSize = typedArray.getDimension(R.styleable.CustomTextView_textSize, DisplayUtil.sp2px(context, 15));

        typedArray.recycle();

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.YELLOW);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);

        Rect rect = new Rect();
        mTextPaint.getTextBounds(TEXT, 0, TEXT.length(), rect);
        Log.d(TAG, "CustomTextView: getTextBounds width = " + rect.width() + "; height = " + rect.height());

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        Log.d(TAG, "CustomTextView: fontMetrics,top = " + fontMetrics.top + "; bottom = " + fontMetrics.bottom
                + "; ascent = " + fontMetrics.ascent + "; descent = " + fontMetrics.descent);
        Log.d(TAG, "CustomTextView: measure text width = " + mTextPaint.measureText(TEXT));
        Log.d(TAG, "CustomTextView: measure Text height = " + (mTextPaint.descent() + mTextPaint.ascent()));
        Log.d(TAG, "CustomTextView: measure Text height = " + Math.ceil(mTextPaint.descent() - mTextPaint.ascent()));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MiscUtil.measure(widthMeasureSpec, mDefaultSize) + getPaddingLeft() + getPaddingRight(),
                MiscUtil.measure(heightMeasureSpec, mDefaultSize) + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
