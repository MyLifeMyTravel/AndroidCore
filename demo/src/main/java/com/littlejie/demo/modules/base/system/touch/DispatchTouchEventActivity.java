package com.littlejie.demo.modules.base.system.touch;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.BindView;

@Description(description = "Android 事件分发机制")
public class DispatchTouchEventActivity extends BaseActivity {

    @BindView(R.id.btn_dispatch_touch_event)
    Button mBtnDispatchTouchEvent;
    @BindView(R.id.iv_dispatch_touch_event)
    ImageView mIvDispatchTouchEvent;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_dispatch_touch_event;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    private Rect rect = new Rect();

    @Override
    protected void initViewListener() {
        mBtnDispatchTouchEvent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "onTouch: Button -- ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "onTouch: Button -- ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        ToastUtil.showDefaultToast("Button Touch");
                        Log.d(TAG, "onTouch: Button -- ACTION_UP");
                        break;
                }

                v.getGlobalVisibleRect(rect);
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (event.getRawX() >= rect.left && event.getRawX() <= rect.right
                            && event.getRawY() >= rect.top && event.getRawY() <= rect.bottom) {
                        Log.d(TAG, "在Button范围内，eventX = " + event.getRawX() + ";eventY = " + event.getRawY()
                                + ";viewX range = " + rect.toString());
                        return false;
                    } else {
                        Log.d(TAG, "超出Button范围，eventX = " + event.getRawX() + ";eventY = " + event.getRawY()
                                + ";view range = " + rect.toString());
                        return true;
                    }
                }
                return false;
            }
        });
        mBtnDispatchTouchEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Button Dispatch Touch Event");
            }
        });

        mIvDispatchTouchEvent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "onTouch: ImageView -- ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "onTouch: ImageView -- ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        ToastUtil.showDefaultToast("ImageView Touch");
                        Log.d(TAG, "onTouch: ImageView -- ACTION_UP");
                        break;
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (event.getRawX() >= rect.left && event.getRawX() <= rect.right
                            && event.getRawY() >= rect.top && event.getRawY() <= rect.bottom) {
                        Log.d(TAG, "在ImageView范围内，eventX = " + event.getRawX() + ";eventY = " + event.getRawY()
                                + ";viewX range = " + rect.toString());
                        return false;
                    } else {
                        Log.d(TAG, "超出ImageView范围，eventX = " + event.getRawX() + ";eventY = " + event.getRawY()
                                + ";view range = " + rect.toString());
                        return true;
                    }
                }
                return false;
            }
        });
        mIvDispatchTouchEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ImageView Dispatch Touch Event");
            }
        });
    }

    @Override
    protected void process() {

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
        return super.onTouchEvent(event);
    }
}
