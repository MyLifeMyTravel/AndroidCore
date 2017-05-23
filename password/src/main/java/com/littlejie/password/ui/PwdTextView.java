package com.littlejie.password.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */

public class PwdTextView extends TextView {

    private Paint bottomLinePaint;

    public PwdTextView(Context context) {
        super(context);
        init();
    }

    public PwdTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        bottomLinePaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBottomLine(canvas);
    }

    private void drawBottomLine(Canvas canvas) {
        bottomLinePaint.setColor(Color.YELLOW);
        bottomLinePaint.setStrokeWidth(10);
        canvas.drawLine(getLeft(), getTop(), getRight(), getBottom(), bottomLinePaint);
    }

    private void drawPasswordCircle(Canvas canvas) {

    }
}
