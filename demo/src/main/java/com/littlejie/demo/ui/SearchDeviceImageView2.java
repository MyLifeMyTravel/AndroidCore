package com.littlejie.demo.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.LinearInterpolator;

import com.littlejie.demo.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by littlejie on 2017/11/6.
 */

public class SearchDeviceImageView2 extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private Handler handler;
    private AnimatorRunnable runnable = new AnimatorRunnable();
    private Map<ValueAnimator, Float> animatorMap = new HashMap<>();
    //重复动画的间隔
    private long interval;
    //动画的时长
    private long duration;
    //白色区域宽度，动画效果可达最大宽度
    private int minWidth;
    private int maxWidth;
    private int minWidthThreshold;
    private int maxWidthThreshold;
    private int widthDiff;
    private Bitmap bitmap;

    public SearchDeviceImageView2(Context context) {
        super(context);
        init();
    }

    public SearchDeviceImageView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        HandlerThread handlerThread = new HandlerThread("Search");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

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
        interval = 400;
        duration = 1500;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());

        maxWidth = getMeasuredWidth() / 2;
        minWidthThreshold = minWidth;
        maxWidthThreshold = maxWidth;
        widthDiff = maxWidth - minWidthThreshold;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    private void drawCircle() {
        if (animatorMap.isEmpty()) {
            return;
        }
        Set<ValueAnimator> iterator = animatorMap.keySet();
        paint.setColor(getResources().getColor(R.color.colorAccent));
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        int cx = getMeasuredWidth() / 2;
        int cy = getMeasuredHeight() / 2;
        for (ValueAnimator animator : iterator) {
            float value = animatorMap.get(animator);
            float alpha = (1 - (value - minWidthThreshold) / (widthDiff)) * 255;
            paint.setAlpha((int) alpha);
            canvas.drawCircle(cx, cy, value, paint);
        }
        paint.setAlpha(255);
        int left = getMeasuredWidth() / 2 - bitmap.getWidth() / 2;
        int top = getMeasuredHeight() / 2 - bitmap.getHeight() / 2;
        canvas.drawBitmap(bitmap, left, top, paint);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    private void start() {
        if (!animatorMap.isEmpty()) {
            return;
        }
        handler.post(runnable);
    }

    private void startAnimator() {
        ValueAnimator animator =
                ValueAnimator.ofFloat(minWidthThreshold, maxWidthThreshold);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                animatorMap.put(animation, value);
                if (value == maxWidthThreshold) {
                    animatorMap.remove(animation);
                }
                drawCircle();
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(duration);
        animator.start();
    }

    private void stop() {
        handler.removeCallbacks(runnable);
        if (animatorMap.isEmpty()) {
            return;
        }
        Set<ValueAnimator> animatorSet = animatorMap.keySet();
        for (ValueAnimator animator : animatorSet) {
            animator.cancel();
        }
    }

    private class AnimatorRunnable implements Runnable {

        @Override
        public void run() {
            startAnimator();
            handler.postDelayed(this, interval);
        }
    }
}
