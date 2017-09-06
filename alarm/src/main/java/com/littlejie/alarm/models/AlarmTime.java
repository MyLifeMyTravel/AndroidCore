package com.littlejie.alarm.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */
@Entity
public class AlarmTime {
    @Id(autoincrement = true)
    private Long id;

    private long alarmId;
    private int week;
    private int hour;
    private int minute;
    private int second;

    @Generated(hash = 1127932849)
    public AlarmTime(Long id, long alarmId, int week, int hour, int minute,
                     int second) {
        this.id = id;
        this.alarmId = alarmId;
        this.week = week;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    @Generated(hash = 1845244293)
    public AlarmTime() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAlarmId() {
        return this.alarmId;
    }

    public void setAlarmId(long alarmId) {
        this.alarmId = alarmId;
    }

    public int getWeek() {
        return this.week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getHour() {
        return this.hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return this.second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

}
