package com.littlejie.demo.modules.base.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by littlejie on 2017/11/3.
 */

public class FractionLinearLayout extends LinearLayout {

    private static final String TAG = FractionLinearLayout.class.getName();

    public FractionLinearLayout(Context context) {
        super(context);
    }

    public FractionLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public float getYFraction() {
        return getTranslationY();
    }

    public void setYFraction(float yFraction) {
    }
}
