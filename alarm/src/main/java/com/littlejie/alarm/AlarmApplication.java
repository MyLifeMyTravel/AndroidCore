package com.littlejie.alarm;

import android.util.Log;

import com.littlejie.core.base.BaseApplication;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */

public class AlarmApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "AlarmApplication onCreate()");
        AlarmManager.getInstance().init(this);
    }

}
