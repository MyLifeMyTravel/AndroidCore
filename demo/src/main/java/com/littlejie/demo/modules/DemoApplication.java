package com.littlejie.demo.modules;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.littlejie.core.base.BaseApplication;
import com.littlejie.core.crash.CrashHandler;
import com.littlejie.demo.SharePrefsManager;
import com.littlejie.password.DeblockType;
import com.littlejie.password.OnDeblockListener;
import com.littlejie.password.PasswordManager;
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
        PasswordManager.getInstance().init(this, 0, 4, 5);
        PasswordManager.getInstance().setOnDeblockListener(new OnDeblockListener() {
            @Override
            public void onDeblock(DeblockType type, boolean success) {
                if (DeblockType.DEBLOCK == type) {
                    if (success) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "密码多次输入错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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