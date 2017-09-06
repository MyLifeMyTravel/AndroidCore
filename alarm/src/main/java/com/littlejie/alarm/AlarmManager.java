package com.littlejie.alarm;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.littlejie.alarm.models.Alarm;

import java.util.Calendar;
import java.util.List;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */

public class AlarmManager {

    private static final String TAG = AlarmManager.class.getSimpleName();
    private static final long INTERVAL_DAY = 24 * 60 * 60 * 1000;
    private static final long INTERVAL_WEEK = 7 * INTERVAL_DAY;

    private static AlarmManager instance = new AlarmManager();

    private Context context;
    private android.app.AlarmManager alarmManager;

    private AlarmManager() {
    }

    public static AlarmManager getInstance() {
        return instance;
    }

    public void init(Context context) {
        init(context, false);
    }

    public void init(Context context, boolean encrypted) {
        this.context = context.getApplicationContext();
        AlarmDaoManager.getInstance().init(context, encrypted);
        alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        enableAlarmReceiver(context);
        loadAllAlarm();
    }

    private void enableAlarmReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void loadAllAlarm() {
        List<Alarm> alarmList = AlarmDaoManager.getInstance().loadAll();
        if (alarmList == null || alarmList.size() == 0) {
            return;
        }
        for (Alarm alarm : alarmList) {
            if (!alarm.getIsOpen()) {
                return;
            }
            setAlarm(alarm);
        }
    }

    public Alarm load(long id) {
        return AlarmManager.getInstance().load(id);
    }

    public void set(Alarm alarm) {
        AlarmDaoManager.getInstance().insert(alarm);
        Log.d(TAG, "alarm id = " + alarm.getId());
        setAlarm(alarm);
    }

    public void close(Alarm alarm) {
        alarm.setIsOpen(false);
        AlarmDaoManager.getInstance().update(alarm);
        setAlarm(alarm);
    }

    public void delete(Alarm alarm) {
        AlarmDaoManager.getInstance().delete(alarm);
        cancelAlarm(alarm);
    }

    public void update(Alarm alarm) {
        AlarmDaoManager.getInstance().update(alarm);
        setAlarm(alarm);
    }

    public void updateAlarmById(long id) {
        setAlarm(AlarmDaoManager.getInstance().load(id));
    }

    public void setAlarm(Alarm alarm) {
        if (alarm == null) {
            return;
        }
        PendingIntent alarmIntent = getAlarmPendingIntent(alarm.getId());
        alarmManager.cancel(alarmIntent);
        if (alarm.getIsOpen()) {
            return;
        }

        long interval = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, alarm.getSecond());

        long alarmId = alarm.getId();
        Alarm.Type type = alarm.getType();
        Log.d(TAG, "setAlarm,alarm id = " + alarmId + ";闹钟类型 = " + type);
        switch (type) {
            case UNKNOWN:
            case ONCE:
                interval = INTERVAL_DAY;
                break;
            case EVERY_DAY:
                interval = INTERVAL_DAY;
                break;
            case EVERY_WEEK:
                int weekday = getAlarmDayInCurrentWeek(alarm);
                Log.d(TAG, "setAlarm,alarm id = " + alarmId
                        + ";getAlarmDayInCurrentWeek = " + weekday);
                if (weekday == 0) {
                    weekday = getFirstAlarmDayInWeek(alarm.getWeekdays());
                    Log.d(TAG, "setAlarm,alarm id = " + alarmId
                            + ";getFirstAlarmDayInWeek = " + weekday);
                }
                calendar.set(Calendar.DAY_OF_WEEK, weekday);
                interval = INTERVAL_WEEK;
                break;
            default:
                break;
        }

        long triggerTime = calendar.getTimeInMillis();
        //小于当前时间，设置下一个提醒
        if (triggerTime < Calendar.getInstance().getTimeInMillis()) {
            triggerTime += interval;
            Log.d(TAG, "setAlarm,alarm id = " + alarmId + ";skip to next triggerTime.");
        }
        Log.d(TAG, "setAlarm,alarm id = " + alarmId
                + ";triggerTime = " + triggerTime
                + ";currentTime = " + Calendar.getInstance().getTimeInMillis()
                + ";interval = " + interval);
        alarmManager.setRepeating(android.app.AlarmManager.RTC_WAKEUP,
                triggerTime, interval, alarmIntent);
    }

    private int getAlarmDayInCurrentWeek(Alarm alarm) {
        int weekdays = alarm.getWeekdays();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, alarm.getSecond());

        for (Alarm.Week weekDay : Alarm.Week.values()) {
            if ((weekDay.getValue() & weekdays) != weekDay.getValue()) {
                continue;
            }
            //Alarm.Weekday与Calendar之间相互转换
            int currentWeekday = (int) (Math.log(weekDay.getValue()) / Math.log(2));
            calendar.set(Calendar.DAY_OF_WEEK, currentWeekday);
            if (calendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis()) {
                return calendar.get(Calendar.DAY_OF_WEEK);
            }
        }
        return 0;
    }

    private int getFirstAlarmDayInWeek(int weekdays) {
        Alarm.Week alarmWeekDay = Alarm.Week.UNKNOWN;
        for (Alarm.Week weekDay : Alarm.Week.values()) {
            if ((weekDay.getValue() & weekdays) == weekDay.getValue()) {
                alarmWeekDay = weekDay;
                break;
            }
        }
        return (int) (Math.log(alarmWeekDay.getValue()) / Math.log(2));
    }

    private void cancelAlarm(Alarm alarm) {
        alarmManager.cancel(getAlarmPendingIntent(alarm.getId()));
    }

    private PendingIntent getAlarmPendingIntent(long id) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Constants.ACTION_ALARM);
        intent.putExtra(Constants.EXTRA_ID, id);
        return PendingIntent.getBroadcast(context, (int) id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
