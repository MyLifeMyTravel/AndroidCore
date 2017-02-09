package com.littlejie.demo.modules.base.component.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    public static final String TAG = MyService.class.getSimpleName();

    private Handler mHandler = new Handler();
    private LogRunnable mLogRunnable = new LogRunnable();

    private MyBinder mMyBinder = new MyBinder();

    public MyService() {
    }

    /**
     * onCreate() 在 Service 的生命周期中只执行一次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: " + this.toString());
    }

    /**
     * 每次调用 startService() 都会调用 onStartCommand()
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    // TODO: 2017/2/9 通过 startService() 调起，则杀进程后 Service 还会被唤起，求原因
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: " + this.toString());
        mHandler.post(mLogRunnable);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 在 onDestroy() 中释放必要资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + this.toString());
        //如果不在onDestroy中移除该Runnable，则会一直执行
        //如果在 Recent Apps 中杀掉该 App，还会继续打印
        //通过别的手段，如：隐式 Intent 调起 App，会发现 Application 的 onCreate() 方法不会被调用
        //但如果调用 removeCallbacks() ，则会调用 Application 的 onCreate() 方法
        mHandler.removeCallbacks(mLogRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMyBinder;
    }

    public class MyBinder extends Binder {

        public void startDownload() {
            mHandler.post(mLogRunnable);
        }
    }

    private class LogRunnable implements Runnable {

        @Override
        public void run() {
            Log.d(TAG, "run: log runnable," + MyService.this.toString());
            mHandler.postDelayed(this, 1000);
        }
    }
}
