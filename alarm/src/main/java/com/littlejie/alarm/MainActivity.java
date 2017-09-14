package com.littlejie.alarm;

import android.content.Intent;

import com.littlejie.alarm.models.Alarm;
import com.littlejie.core.base.BaseActivity;

import java.util.Calendar;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }

    @OnClick(R.id.btn_set_alarm)
    void setAlarm() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @OnClick(R.id.btn_set_alarm_thrity_seconds_later)
    void setAlarmThritySecondsLater() {
        Alarm alarm = new Alarm();
        alarm.setType(Alarm.Type.EVERY_WEEK);
        alarm.setWeekdays(Alarm.Week.THURSDAY.getValue() + Alarm.Week.FRIDAY.getValue());
        Calendar calendar = Calendar.getInstance();
        alarm.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        alarm.setMinute(calendar.get(Calendar.MINUTE) + 3);
        AlarmManager.getInstance().set(alarm);
    }

}
