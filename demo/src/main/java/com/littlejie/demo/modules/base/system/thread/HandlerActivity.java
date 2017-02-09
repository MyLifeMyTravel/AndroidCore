package com.littlejie.demo.modules.base.system.thread;

import android.os.Handler;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.OnClick;

@Description(description = "Handler Demo")
public class HandlerActivity extends BaseActivity {

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_handler;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.btn_new_handler_in_sub_thread)
    void newHandlerInSubThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Handler handler = new Handler();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void process() {

    }
}
