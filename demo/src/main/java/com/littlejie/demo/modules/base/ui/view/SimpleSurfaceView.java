package com.littlejie.demo.modules.base.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.littlejie.core.util.DisplayUtil;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by littlejie on 2017/12/4.
 */

public class SimpleSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = SimpleSurfaceView.class.getSimpleName();

    private SurfaceHolder mSurfaceHolder;
    //工作线程
    private Thread mWorkThread;
    //是否正在绘制
    private AtomicBoolean isDrawing;
    private Paint mPaint;
    //用于随机数
    private Random mRandom;
    private int mScreenWidth;
    private int mScreenHeight;

    public SimpleSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mRandom = new Random();
        mScreenWidth = DisplayUtil.getScreenWidth((Activity) getContext());
        mScreenHeight = DisplayUtil.getScreenHeight((Activity) getContext());

        isDrawing = new AtomicBoolean();

        mSurfaceHolder = getHolder();
        //SurfaceHolder type 类型，已废弃。需要时系统自动设置
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //设置屏幕常亮
        mSurfaceHolder.setKeepScreenOn(true);
        //设置 Surface 上预期的显示的 PixelFormat
        mSurfaceHolder.setFormat(PixelFormat.RGBA_8888);
        //给 SurfaceView 设置分辨率,默认跟 View 大小一致
        //mSurfaceHolder.setFixedSize(mScreenWidth, mScreenHeight);
        //设置 SurfaceHolder.Callback, 分别在SurfaceView创建，改变，销毁时进行回调
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //SurfaceView创建时回调，一般在这开启工作线程
        mWorkThread = new Thread(this);
        mWorkThread.start();
        isDrawing.set(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged, format = " + format
                + ";width = " + width + ";height = " + height);
        //给 SurfaceView 设置分辨率
        holder.setFixedSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing.set(false);
    }

    @Override
    public void run() {
        while (isDrawing.get()) {
            Canvas canvas = mSurfaceHolder.lockCanvas();
            if (canvas == null) {
                return;
            }
            //清空画布
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);

            for (int i = 0; i < 500; i++) {
                int a = mRandom.nextInt(255);
                int r = mRandom.nextInt(255);
                int g = mRandom.nextInt(255);
                int b = mRandom.nextInt(255);
                float x = mRandom.nextInt(mScreenWidth);
                float y = mRandom.nextInt(mScreenHeight);
                float radius = mRandom.nextInt(100);
                mPaint.setColor(Color.argb(a, r, g, b));
                canvas.drawCircle(x, y, radius, mPaint);
            }

            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}