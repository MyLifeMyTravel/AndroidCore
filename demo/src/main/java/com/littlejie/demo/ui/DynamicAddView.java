package com.littlejie.demo.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */

public class DynamicAddView extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    public DynamicAddView(Context context) {
        super(context);
        init(context);
    }

    public DynamicAddView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        addTextView();
    }

    private void addTextView() {
        TextView textView = new TextView(getContext());
        textView.setText("1211113");
        addView(textView);
    }
}
