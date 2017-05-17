package cn.bongmi.uidemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com 6
 */

public class SearchDeviceImageView extends ImageView {
    private static final String TAG = SearchDeviceImageView.class.getSimpleName();

    private Handler handler = new Handler();
    private AnimatorRunnable runnable = new AnimatorRunnable();
    private Paint paint;
    private Map<ValueAnimator, Float> animatorMap = new HashMap<>();
    //重复动画的间隔
    private long interval;
    //动画的时长
    private long duration;
    //白色区域宽度，动画效果可达最大宽度
    private int minWidth, maxWidth;
    private int minWidthThreshold, maxWidthThreshold;
    private int widthDiff;

    public SearchDeviceImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.SearchDeviceImageView);
        interval = t.getInt(R.styleable.SearchDeviceImageView_interval, 700);
        duration = t.getInt(R.styleable.SearchDeviceImageView_duration, 2000);
        t.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        // 多增加一些距离，使动画更加流畅
        if (getDrawable() != null) {
            minWidth = getDrawable().getBounds().width() / 2;
        }
        maxWidth = getMeasuredWidth() / 2;
        minWidthThreshold = minWidth;
        maxWidthThreshold = maxWidth;
        widthDiff = maxWidth - minWidthThreshold;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircle(canvas);
        super.onDraw(canvas);
    }

    private void drawCircle(Canvas canvas) {
        if (animatorMap.isEmpty() || animatorMap.size() == 0) {
            return;
        }
        Set<ValueAnimator> iterator = animatorMap.keySet();
        for (ValueAnimator animator : iterator) {
            float value = animatorMap.get(animator);
            float alpha = (1 - (value - minWidthThreshold) / (widthDiff)) * 255;
            paint.setAlpha((int) alpha);
            canvas.drawCircle(maxWidth, maxWidth, animatorMap.get(animator), paint);
        }
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void start() {
        if (!animatorMap.isEmpty()) {
            return;
        }
        handler.post(runnable);
    }

    private void addAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(minWidthThreshold, maxWidthThreshold);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                animatorMap.put(animation, value);
                if (value == maxWidthThreshold) {
                    animatorMap.remove(animation);
                }
                invalidate();
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(duration);
        animator.start();
    }

    public void stop() {
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
            addAnimator();
            handler.postDelayed(this, interval);
        }
    }
}
