package com.littlejie.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.littlejie.alarm.models.Alarm;
import com.littlejie.core.util.ToastUtil;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "收到开机广播..." + System.currentTimeMillis());
            AlarmManager.getInstance().init(context);
        } else if (intent.getAction().equals(Constants.ACTION_ALARM)) {
            ToastUtil.showDefaultToast("闹钟响了...");
            Log.d(TAG, "闹钟响了..." + System.currentTimeMillis());
            long id = intent.getLongExtra(Constants.EXTRA_ID, 0);
            updateAlarm(id);
        }
    }

    private void updateAlarm(long id) {
        Alarm alarm = AlarmManager.getInstance().load(id);
        if (alarm == null) {
            return;
        }
        switch (alarm.getType()) {
            case UNKNOWN:
            case ONCE:
                alarm.setIsOpen(false);
                break;
        }
        AlarmManager.getInstance().update(alarm);
    }
}
