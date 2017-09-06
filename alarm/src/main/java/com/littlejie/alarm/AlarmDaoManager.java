package com.littlejie.alarm;

import android.content.Context;

import com.littlejie.alarm.models.Alarm;
import com.littlejie.alarm.models.AlarmDao;
import com.littlejie.alarm.models.DaoMaster;
import com.littlejie.alarm.models.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.util.List;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */

class AlarmDaoManager {

    private static final String ENCRYPTED_ALARM_DB = "encrypted_alarm_db";
    private static final String ALARM_DB = "alarm_db";

    private static AlarmDaoManager instance = new AlarmDaoManager();

    private AlarmDao alarmDao;

    private AlarmDaoManager() {
    }

    public static AlarmDaoManager getInstance() {
        return instance;
    }

    public void init(Context context, boolean encrypted) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,
                encrypted ? ENCRYPTED_ALARM_DB : ALARM_DB);
        Database db = encrypted
                ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();
        alarmDao = daoSession.getAlarmDao();
    }

    List<Alarm> loadAll() {
        return alarmDao.loadAll();
    }

    Alarm load(long id) {
        return alarmDao.load(id);
    }

    long insert(Alarm alarm) {
        return alarmDao.insert(alarm);
    }

    void update(Alarm alarm) {
        alarmDao.update(alarm);
    }

    void delete(Alarm alarm) {
        alarmDao.delete(alarm);
    }
}
