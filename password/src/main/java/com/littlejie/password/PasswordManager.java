package com.littlejie.password;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */

public class PasswordManager {

    public static final int DEFAULT_PASSWORD_LENGTH = 4;
    public static final int DEFAULT_RETRY_TIMES = 5;
    public static final int DEFAULT_LOCK_TIME = 30000;

    private static PasswordManager instance;
    private LockService lockService;

    private int passwordLength = DEFAULT_PASSWORD_LENGTH;
    private int passwordRetryTimes = DEFAULT_RETRY_TIMES;

    private OnDeblockListener onDeblockResultListener;

    public static PasswordManager getInstance() {
        synchronized (PasswordManager.class) {
            if (instance == null) {
                instance = new PasswordManager();
            }
        }
        return instance;
    }

    public void init(Context context) {
        init(context, DEFAULT_LOCK_TIME, DEFAULT_PASSWORD_LENGTH, DEFAULT_RETRY_TIMES);
    }

    public void init(Context context, long lockMills) {
        init(context, lockMills, DEFAULT_PASSWORD_LENGTH, DEFAULT_RETRY_TIMES);
    }

    public void init(Context context, long lockMills, int passwordLength, int retryTimes) {
        this.passwordRetryTimes = retryTimes;
        this.passwordLength = passwordLength;

        Intent lockServiceIntent = new Intent(context, LockService.class);
        lockServiceIntent.putExtra(Constants.PARAMS_LOCK_MILLS, lockMills);
        context.bindService(lockServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    public int getPasswordLength() {
        return passwordLength;
    }

    public int getPasswordRetryTimes() {
        return passwordRetryTimes;
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (!(service instanceof LockService.LockBinder)) {
                return;
            }
            lockService = ((LockService.LockBinder) service).getService();
            lockService.registerActivityLifecycleCallbacks();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            lockService = null;
        }
    };

    public OnDeblockListener getOnDeblockResultListener() {
        return onDeblockResultListener;
    }

    public void setOnDeblockListener(OnDeblockListener onDeblockResultListener) {
        this.onDeblockResultListener = onDeblockResultListener;
    }
}
