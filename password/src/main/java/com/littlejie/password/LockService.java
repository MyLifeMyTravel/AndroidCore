package com.littlejie.password;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.littlejie.password.storage.PasswrodStorage;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */

public class LockService extends Service {

    public static final String TAG = LockService.class.getSimpleName();

    private Handler mainThreadHandler = new Handler();
    private Runnable lockRunnable = new LockRunnable();

    private Binder binder = new LockBinder();
    private long lockMills;
    private boolean showLock;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        lockMills = intent.getLongExtra(Constants.PARAMS_LOCK_MILLS, 0);
        return binder;
    }

    public class LockBinder extends Binder {

        public LockService getService() {
            return LockService.this;
        }

    }

    public void registerActivityLifecycleCallbacks() {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    private Application.ActivityLifecycleCallbacks callbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (showLock
                    && !TextUtils.isEmpty(PasswrodStorage.get(activity))) {
                Log.d(TAG, "show lock activity");
                Intent intent = new Intent(getApplicationContext(), InputPwdActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                showLock = false;
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (Utils.isBackground(activity)) {
                Log.d(TAG, "Application is to background，" + lockMills + "seconds later,the app will be locked.");
                mainThreadHandler.postDelayed(lockRunnable, lockMills);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    private class LockRunnable implements Runnable {

        @Override
        public void run() {
            if (!Utils.isBackground(getApplicationContext())) {
                Log.d(TAG, "Application is back to foreground.");
                return;
            }
            showLock = true;
        }
    }

}
