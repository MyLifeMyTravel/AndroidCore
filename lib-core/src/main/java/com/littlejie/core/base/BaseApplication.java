package com.littlejie.core.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.littlejie.core.manager.ActivityManager;

/**
 * Created by littlejie on 2016/12/1.
 */

public class BaseApplication extends Application {

    /**
     * TAG
     */
    public static final String TAG = BaseApplication.class.getSimpleName();

    /**
     * the application instance
     */
    private static Context mInstance;

    /**
     * uiThreadHandler
     */
    private static Handler uiThreadHandler = null;

    /**
     * uiThread
     */
    private static Thread uiThread = null;

    public static Context getInstance() {
        return mInstance;
    }

    public static void runOnUIThread(Runnable work) {
        if (Thread.currentThread() != uiThread) {
            uiThreadHandler.post(work);
        } else {
            work.run();
        }
    }

    public static void runDelayOnUIThread(Runnable work, long time) {
        uiThreadHandler.postDelayed(work, time);
    }

    public static void removeRunnable(Runnable work) {
        uiThreadHandler.removeCallbacks(work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        uiThread = Thread.currentThread();
        uiThreadHandler = new Handler();
        Core.init();
    }

    @Override
    public void onTerminate() {
        ActivityManager.removeAllActivities();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
