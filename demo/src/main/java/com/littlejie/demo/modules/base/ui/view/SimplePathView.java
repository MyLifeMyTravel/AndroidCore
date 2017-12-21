package com.littlejie.demo.modules.base.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 简单的闭合路径View
 * Created by littlejie on 2017/12/18.
 */

public class SimplePathView extends View {

    private Path mCirclePath;
    private Path mRectPath;
    private Paint mPathPaint;

    public SimplePathView(final Context context) {
        this(context, null);
    }

    public SimplePathView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mPathPaint = new Paint();
        mPathPaint.setColor(Color.BLACK);
        mCirclePath = new Path();
        mRectPath = new Path();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        mCirclePath.addCircle(200, 200, 200, Path.Direction.CW);
        mRectPath.addRect(0, 0, 400, 300, Path.Direction.CW);
        mCirclePath.op(mRectPath, Path.Op.INTERSECT);
        canvas.drawPath(mCirclePath, mPathPaint);
    }
}
