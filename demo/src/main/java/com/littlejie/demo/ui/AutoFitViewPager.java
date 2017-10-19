package com.littlejie.demo.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 自适应高度ViewPager
 * Created by littlejie on 2017/10/19.
 */

public class AutoFitViewPager extends ViewPager {

    private static final String TAG = AutoFitViewPager.class.getSimpleName();

    public AutoFitViewPager(Context context) {
        super(context);
    }

    public AutoFitViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        Log.d(TAG, "current item = " + getCurrentItem());
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = child.getMeasuredHeight();
            Log.d(TAG, "index = " + i + ";height = " + height);
        }

        //通过TAG查找当前View
        View current = findViewWithTag(getCurrentItem());
        if (current != null) {
            height = current.getMeasuredHeight();
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
