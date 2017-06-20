package com.littlejie.demo.modules;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.littlejie.core.base.BaseApplication;
import com.littlejie.core.crash.CrashHandler;
import com.littlejie.demo.SharePrefsManager;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by littlejie on 2017/2/6.
 */

public class DemoApplication extends BaseApplication {

    private static NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //通过静态广播、分享之类的 Intent 调起，都会调用 Application 的 onCreate() 方法
        Log.d(TAG, "onCreate: DemoApplication onCreate");
        initLeakCanary();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        SharePrefsManager.getInstance().init(this);
        CrashHandler.getInstance().init(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/FileManager");
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public static NotificationManager getNotificationManager() {
        return mNotificationManager;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate: ");
    }

}