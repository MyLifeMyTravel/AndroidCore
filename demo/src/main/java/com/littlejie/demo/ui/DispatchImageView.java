package com.littlejie.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 测试 Android 事件分发自定义 ImageView
 * Created by littlejie on 2017/2/5.
 */

public class DispatchImageView extends ImageView {

    public static final String TAG = DispatchImageView.class.getSimpleName();

    public DispatchImageView(Context context) {
        super(context);
    }

    public DispatchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "dispatchTouchEvent: ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "dispatchTouchEvent: ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "dispatchTouchEvent: ACTION_UP");
                break;
        }
        boolean consume = super.dispatchTouchEvent(ev);
        Log.d(TAG, "dispatchTouchEvent: event is consume = " + consume);
        return consume;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: ACTION_UP");
                break;
        }
        boolean consume = super.onTouchEvent(event);
        Log.d(TAG, "onTouchEvent: event is consume = " + consume);
        return consume;
    }
}
