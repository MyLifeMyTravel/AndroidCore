package com.littlejie.demo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import com.littlejie.core.util.DisplayUtil;
import com.littlejie.demo.R;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */

public class WeightView extends View {

    public static final String TAG = WeightView.class.getSimpleName();
    private int smallRoundColor;//小圆点颜色
    private int bigRoundColor;//大圆点颜色
    private int scaleColor;//刻度颜色
    private int valueColor;//刻度值颜色
    private int scaleWidth;//大刻度线宽度
    private int scaleRadius;//小刻度线半径
    private int lineRadius;//指针圆半径

    private int scaleLength;//大刻度长度
    private int scaleSpace;//刻度间距
    private int scaleSpaceUnit;//每大格刻度间距
    private int height;//view高
    private int width;//view宽
    private int paddingBottom = 10;

    private int max = 100;//最大刻度
    private int min = 0;//最小刻度
    private int borderMax;//最大值坐标
    private int borderMin;//最小值坐标
    private float midX;//当前中心刻度y坐标
    private float originMidX;//初始中心刻度y坐标
    private float maxX;//最大刻度x坐标,从最大刻度开始画刻度
    private float originMaxX;//初始最大刻度x坐标
    private float lastX;

    private boolean isMeasured;//是否测量过 如果测量过就不再重置指针位置 重新测量后指针可保持在上次测量位置

    private float originValue = 0;//初始刻度对应的值
    private float currentValue = originValue;//当前刻度对应的值

    private Paint paint;//画笔

    private Context context;

    private VelocityTracker velocityTracker;//速度监测
    private float velocity;//当前滑动速度
    private float accel = 1000000;//加速度
    private boolean continueScroll;//是否继续滑动

    private OnValueChangeListener onValueChangeListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null != onValueChangeListener) {
                float v = (float) (Math.round(currentValue * 10)) / 10;//保留一位小数
                Log.d(TAG, "v = " + v);
                onValueChangeListener.onValueChanged(v);
            }
        }
    };

    public WeightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.WeightView);
        smallRoundColor = t.getColor(R.styleable.WeightView_small_round_color,
                getResources().getColor(R.color.main_color));
        bigRoundColor = t.getColor(R.styleable.WeightView_big_round_color,
                getResources().getColor(R.color.white));
        scaleColor = t.getColor(R.styleable.WeightView_scale_color,
                getResources().getColor(R.color.main_color));
        valueColor = t.getColor(R.styleable.WeightView_value_color,
                getResources().getColor(R.color.primary_color));

        t.recycle();
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);

        scaleRadius = DisplayUtil.dp2px(context, 3);
        scaleWidth = scaleRadius * 2;
        scaleLength = DisplayUtil.dp2px(context, 6);
        lineRadius = DisplayUtil.dp2px(context, 15);
        scaleSpace = scaleWidth;
        scaleSpaceUnit = 2 * scaleWidth;
    }

    //设置刻度范围
    public void setRange(int min, int max) {
        this.min = min;
        this.max = max;
        originValue = (max + min) / 2;
        currentValue = originValue;
        initMeasure();
    }

    //设置value变化监听
    public void setOnValueChangeListener(
            OnValueChangeListener
                    onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

    //设置值
    public void setData(final float value, final boolean needUpdate) {
        currentValue = value;
        if (value < min) {
            return;
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                float totalOffset = (value - originValue) * 2 * scaleWidth;
                maxX = originMaxX - totalOffset;
                midX = originMidX - totalOffset;
                calculateCurrentScale(needUpdate);
                confirmBorder();
                postInvalidate();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured) {
            initMeasure();
        }
        isMeasured = true;
    }

    private void initMeasure() {
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        borderMax = width / 2 + ((min + max) / 2 - min) * scaleSpaceUnit;
        borderMin = width / 2 - ((min + max) / 2 - min) * scaleSpaceUnit;
        midX = (borderMax + borderMin) / 2;
        maxX = borderMax;
        //根据originValue初始化刻度
        midX += ((min + max) / 2 - originValue) * scaleSpaceUnit;
        maxX += ((min + max) / 2 - originValue) * scaleSpaceUnit;
        originMidX = midX;
        originMaxX = maxX;
    }

    private Rect rect = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.FILL);
        //画刻度线
        for (int i = min; i <= max; i++) {
            if (i % 10 == 0) {
                //画刻度数字
                String str = String.valueOf(i);
                paint.setColor(valueColor);
                paint.setTextSize(DisplayUtil.sp2px(context, 10));
                paint.getTextBounds(str, 0, str.length(), rect);
                int w = rect.width();
                int h = rect.height();
                canvas.drawText(str, maxX - (max - i) * scaleSpaceUnit - w / 2, h, paint);
                //画大刻度线
                paint.setColor(scaleColor);
                paint.setStrokeWidth(scaleWidth);
                canvas.drawLine(maxX - (i - min) * scaleSpaceUnit, height -
                        paddingBottom - lineRadius - scaleLength / 2, maxX - (i - min) *
                        scaleSpaceUnit, height - paddingBottom - lineRadius + scaleLength /
                        2, paint);
            } else {
                if (i == min) {
                    continue;//最后一条不画小刻度线
                }
                //画小刻度线
                paint.setColor(smallRoundColor);
                canvas.drawCircle(maxX - (i - min) * scaleSpaceUnit, height - paddingBottom - lineRadius,
                        scaleRadius, paint);
            }
        }

        //画指针圆
        paint.setColor(bigRoundColor);
        canvas.drawCircle(width / 2, height - paddingBottom - lineRadius,
                lineRadius, paint);

        paint.setStrokeWidth(DisplayUtil.dp2px(context, 1));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.black_transparent_53));
        canvas.drawCircle(width / 2, height - paddingBottom - lineRadius,
                lineRadius, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                continueScroll = false;
                //初始化速度追踪
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                int offsetX = (int) (lastX - x);
                maxX -= offsetX;
                midX -= offsetX;
                calculateCurrentScale(true);
                invalidate();
                lastX = x;
                break;
            case MotionEvent.ACTION_UP:
                confirmBorder();
                //当前滑动速度
                velocityTracker.computeCurrentVelocity(1000);
                velocity = velocityTracker.getXVelocity();
                float minVelocity = ViewConfiguration.get(context)
                        .getScaledMinimumFlingVelocity();
                if (Math.abs(velocity) > minVelocity) {
                    continueScroll = true;
                    continueScroll();
                } else {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.recycle();
                velocityTracker = null;
                break;
            default:
                break;
        }
        return true;
    }

    //计算当前刻度
    private void calculateCurrentScale(boolean needUpdate) {
        Log.d(TAG, "midX = " + midX + "; originMidX = " + originMidX + "; originValue = " + originValue);
        float offsetTotal = midX - originMidX;
        float offsetBig = offsetTotal / scaleSpaceUnit;//移动的大刻度数
        float offsetS = offsetTotal % scaleSpaceUnit;
        //移动的小刻度数 四舍五入取整
        float offsetSmall = (new BigDecimal(offsetS / (scaleSpace +
                scaleRadius * 2)).setScale(0, BigDecimal.ROUND_HALF_UP)).floatValue();
        float offset = offsetBig + offsetSmall * 0.1f;
        if (originValue - offset > max) {
            currentValue = max;
        } else if (originValue - offset < min) {
            currentValue = min;
        } else {
            currentValue = originValue - offset;
        }
        if (needUpdate) {
            handler.sendEmptyMessage(0);
        }
    }

    //指针线超出范围时 重置回边界处
    private void confirmBorder() {
        if (midX < borderMin) {
            midX = borderMin;
            maxX = borderMax + (borderMin - borderMax) / 2;
            postInvalidate();
        } else if (midX > borderMax) {
            midX = borderMax;
            maxX = borderMax - (borderMin - borderMax) / 2;
            postInvalidate();
        }
    }

    //手指抬起后继续惯性滑动
    private void continueScroll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                float velocityAbs = 0;//速度绝对值
                if (velocity > 0 && continueScroll) {
                    velocity -= 50;
                    maxX += velocity * velocity / accel;
                    midX += velocity * velocity / accel;
                    velocityAbs = velocity;
                } else if (velocity < 0 && continueScroll) {
                    velocity += 50;
                    maxX -= velocity * velocity / accel;
                    midX -= velocity * velocity / accel;
                    velocityAbs = -velocity;
                }
                calculateCurrentScale(true);
                confirmBorder();
                postInvalidate();
                if (continueScroll && velocityAbs > 0) {
                    post(this);
                } else {
                    continueScroll = false;
                }
            }
        }).start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    public interface OnValueChangeListener {
        void onValueChanged(float value);
    }
}
