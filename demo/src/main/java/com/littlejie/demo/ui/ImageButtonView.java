package com.littlejie.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */

public class ImageButtonView extends ImageView implements View.OnTouchListener {

    public static final float ENABLE_ALPHA = 1f;
    public static final float PRESSED_ALPHA = 0.5f;

    public ImageButtonView(Context context) {
        super(context);
        initView();
    }

    public ImageButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ImageButtonView(Context context, AttributeSet attrs,
                           int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOnTouchListener(this);
    }

    public void performDown() {
        setAlpha(PRESSED_ALPHA);
    }

    public void performUp() {
        setAlpha(ENABLE_ALPHA);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setAlpha(ENABLE_ALPHA);
        } else {
            setAlpha(PRESSED_ALPHA);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                performDown();
                break;
            case MotionEvent.ACTION_UP:
                performUp();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_OUTSIDE:
                performUp();
                break;
            default:
                break;
        }
        return false;
    }
}