package com.littlejie.demo.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.LinearInterpolator;

import com.littlejie.demo.R;

/**
 * Created by littlejie on 2017/11/6.
 */

public class SearchDeviceImageView2 extends SurfaceView implements SurfaceHolder.Callback {

    private static final int MAX_SEARCH_CIRCLE = 3;

    private SurfaceHolder surfaceHolder;
    private Paint paint;
    //动画的时长
    private long duration;
    //白色区域宽度，动画效果可达最大宽度
    private int minWidth;
    private int minWidthThreshold;
    private int maxWidthThreshold;
    private int widthDiff;
    private Bitmap bitmap;
    private ValueAnimator valueAnimator;

    private int circleInterval;
    //坐标圆心
    private int cx, cy;

    public SearchDeviceImageView2(Context context) {
        super(context);
        init();
    }

    public SearchDeviceImageView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        BitmapDrawable bitmapDrawable =
                (BitmapDrawable) getResources().getDrawable(R.drawable.binding_device_search);
        bitmap = bitmapDrawable.getBitmap();
        // 多增加一些距离，使动画更加流畅
        minWidth = bitmap.getWidth() / 2;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        duration = 1500;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置长宽相等
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());

        int maxWidth = getMeasuredWidth() / 2;
        minWidthThreshold = minWidth;
        maxWidthThreshold = maxWidth;
        widthDiff = maxWidth - minWidthThreshold;
        circleInterval = (maxWidthThreshold - minWidthThreshold) / MAX_SEARCH_CIRCLE;
        cx = getMeasuredWidth() / 2;
        cy = getMeasuredHeight() / 2;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startAnimator();
    }

    private void startAnimator() {
        valueAnimator = ValueAnimator.ofFloat(minWidthThreshold, maxWidthThreshold);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                drawCircle(value);
            }
        });
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        valueAnimator.cancel();
    }

    private void drawCircle(float value) {
        if (surfaceHolder == null) {
            return;
        }

        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        paint.setColor(getResources().getColor(R.color.colorAccent));

        for (int i = 0; i < MAX_SEARCH_CIRCLE; i++) {
            float radius = value;
            //循环，如果超出边界，则当做内环计算
            if (value > maxWidthThreshold) {
                radius = (minWidthThreshold + value - maxWidthThreshold);
            }
            float alpha = (1 - (radius - minWidthThreshold) / (widthDiff)) * 255;
            paint.setAlpha((int) alpha);
            canvas.drawCircle(cx, cy, radius, paint);
            value += circleInterval;
        }

        paint.setAlpha(255);
        int left = cx - bitmap.getWidth() / 2;
        int top = cy - bitmap.getHeight() / 2;
        canvas.drawBitmap(bitmap, left, top, paint);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

}
