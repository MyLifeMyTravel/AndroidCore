package com.littlejie.alarm.models;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */
@Entity
public class Alarm {

    //Type类型互斥
    public enum Type {
        UNKNOWN(0),
        ONCE(1),
        EVERY_DAY(2),
        EVERY_WEEK(3);

        private final int value;

        private Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    //Week可叠加选择
    //对应的幂与Calendar中的星期几常量相对应
    public enum Week {
        UNKNOWN(1),
        SUNDAY(2),
        MONDAY(4),
        TUESDAY(8),
        WEDNESDAY(16),
        THURSDAY(32),
        FRIDAY(64),
        SATURDAY(128);

        private final int value;

        private Week(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static class AlarmTypeConverter implements PropertyConverter<Type, String> {

        @Override
        public Type convertToEntityProperty(String databaseValue) {
            return Type.valueOf(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(Type entityProperty) {
            return entityProperty.name();
        }
    }

    @Id(autoincrement = true)
    private Long id;
    private boolean isOpen = true;

    @Convert(converter = AlarmTypeConverter.class, columnType = String.class)
    private Type type;

    private int weekdays = Week.UNKNOWN.getValue();
    private long triggerTime;
    private int hour;
    private int minute;
    private int second;
    private String title;
    private String message;
    private String uri;
    @Generated(hash = 1943260627)
    public Alarm(Long id, boolean isOpen, Type type, int weekdays, long triggerTime, int hour,
            int minute, int second, String title, String message, String uri) {
        this.id = id;
        this.isOpen = isOpen;
        this.type = type;
        this.weekdays = weekdays;
        this.triggerTime = triggerTime;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.title = title;
        this.message = message;
        this.uri = uri;
    }
    @Generated(hash = 1972324134)
    public Alarm() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean getIsOpen() {
        return this.isOpen;
    }
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
    public Type getType() {
        return this.type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public int getWeekdays() {
        return this.weekdays;
    }
    public void setWeekdays(int weekdays) {
        this.weekdays = weekdays;
    }
    public long getTriggerTime() {
        return this.triggerTime;
    }
    public void setTriggerTime(long triggerTime) {
        this.triggerTime = triggerTime;
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
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getUri() {
        return this.uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }


}
