package com.littlejie.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.littlejie.core.base.Core;
import com.littlejie.core.listener.NotifyListener;
import com.littlejie.core.reveiver.USBStateReceiver;
import com.littlejie.core.util.DeviceUtil;
import com.littlejie.core.util.PackageUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private USBStateReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "isSystemApp = " + PackageUtil.isSystemApp(this, "com.google.android.apps.maps"));
        Log.d(TAG, "isSystemApp = " + PackageUtil.isSystemApp(this, "com.nero.htcsenselink"));

        mReceiver = new USBStateReceiver(new NotifyListener() {
            @Override
            public void onNotify(Object status, Object result) {
                Core.showDefautToast("usb is connect = " + status);
            }
        });
        DeviceUtil.registerUSBStateReceiver(this, mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DeviceUtil.unregisterUSBStateReceiver(this, mReceiver);
    }
}
