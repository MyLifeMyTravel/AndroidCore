package com.littlejie.demo.modules.base.component.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;

import butterknife.OnClick;

public class BinderServiceActivity extends BaseActivity {

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder myBinder = (MyService.MyBinder) service;
            myBinder.startDownload();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_binder_service;
    }

    @Override
    protected void initData() {
        startService(new Intent(this, MyService.class));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {
    }

    @OnClick(R.id.btn_bind_service)
    void bindService() {
        bindService(new Intent(this, MyService.class), mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void process() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //调用 bindService() 启动的服务在 Activity 销毁时，必须调用 unbindService() 解绑
        //否则会出现 ServiceConnectionLeaked
        //unbindService(mServiceConnection);
    }
}
