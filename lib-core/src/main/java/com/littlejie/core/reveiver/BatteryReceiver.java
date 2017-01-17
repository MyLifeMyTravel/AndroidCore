package com.littlejie.core.reveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

/**
 * Created by littlejie on 2017/1/11.
 */

public class BatteryReceiver extends BroadcastReceiver {

    private static final String TAG = BatteryReceiver.class.getSimpleName();
    private static int mLevel = 0;
    private static int mScale = 100;
    private static int mStatus = 0;
    private static int mHealth = 0;
    private static int mPlugged = 0;
    private static int mVoltage = 0;
    private static int mTemperature = 0;
    private static String mTechnology;

    private OnBatteryChangeListener mOnBatteryChangeListener;

    public interface OnBatteryChangeListener {
        void onBatteryChange(BatteryReceiver battery);
    }

    public int getLevel() {
        return mLevel;
    }

    public int getScale() {
        return mScale;
    }

    public int getStatus() {
        return mStatus;
    }

    public int getHealth() {
        return mHealth;
    }

    public int getPlugged() {
        return mPlugged;
    }

    public int getVoltage() {
        return mVoltage;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public String getTechnology() {
        return mTechnology;
    }

    public void setOnBatteryChangeListener(OnBatteryChangeListener onBatteryChangeListener) {
        this.mOnBatteryChangeListener = onBatteryChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            //当前电池电量
            mLevel = intent.getIntExtra("level", 0);
            //电池的总量
            mScale = intent.getIntExtra("scale", 100);
            //电池状态，如：充电
            mStatus = intent.getIntExtra("status", 0);
            //电池健康状态
            mHealth = intent.getIntExtra("health", 0);
            //充电类型
            mPlugged = intent.getIntExtra("plugged", 0);
            //电池电压
            mVoltage = intent.getIntExtra("voltage", 0);
            //电池温度，温度需要 * 0.1
            mTemperature = intent.getIntExtra("temperature", 0);
            //电池使用的技术。比如，对于锂电池是Li-ion
            mTechnology = intent.
                    getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            if (mOnBatteryChangeListener != null) {
                mOnBatteryChangeListener.onBatteryChange(BatteryReceiver.this);
            }
            Log.d(TAG, "当前电池电量 = " + mLevel + ";电池总量 = " + mScale);
            Log.d(TAG, "当前电池电压 = " + mVoltage + ";电池温度 = " + mTemperature * 0.1 + "℃");
            Log.d(TAG, "电池类型 = " + mTechnology);
            printPluggedType(mPlugged);
            printBatteryStatus(mStatus);
            printBatteryHealthStatus(mHealth);
        } else if (Intent.ACTION_BATTERY_LOW.equals(action)) {
            Log.d(TAG, "电池电量过低");
        } else if (Intent.ACTION_BATTERY_OKAY.equals(action)) {
            Log.d(TAG, "正常");
        }
    }

    private void printPluggedType(int plugged) {
        String type = "unknown";
        if (BatteryManager.BATTERY_PLUGGED_AC == plugged) {
            type = "ac";
        } else if (BatteryManager.BATTERY_PLUGGED_USB == plugged) {
            type = "usb";
        } else if (BatteryManager.BATTERY_PLUGGED_WIRELESS == plugged) {
            type = "wireless";
        }
        Log.d(TAG, "充电类型 = " + type);
    }

    private void printBatteryStatus(int status) {
        String batteryStatus = "";
        if (BatteryManager.BATTERY_STATUS_CHARGING == status) {
            //充电
            batteryStatus = "charging";
        } else if (BatteryManager.BATTERY_STATUS_DISCHARGING == status) {
            //放电
            batteryStatus = "discharging";
        } else if (BatteryManager.BATTERY_STATUS_FULL == status) {
            //充满电
            batteryStatus = "full";
        } else if (BatteryManager.BATTERY_STATUS_NOT_CHARGING == status) {
            //未充电
            batteryStatus = "not charging";
        }
        Log.d(TAG, "电池状态 = " + batteryStatus);
    }

    private void printBatteryHealthStatus(int health) {
        String status = "unknown";
        if (BatteryManager.BATTERY_HEALTH_COLD == health) {
            //电池过冷
            status = "cold";
        } else if (BatteryManager.BATTERY_HEALTH_DEAD == health) {
            //没电了
            status = "dead";
        } else if (BatteryManager.BATTERY_HEALTH_GOOD == health) {
            //电池状态良好
            status = "good";
        } else if (BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE == health) {
            //电池电压过高
            status = "over voltage";
        } else if (BatteryManager.BATTERY_HEALTH_OVERHEAT == health) {
            //电池过热
            status = "overheat";
        }
        Log.d(TAG, "电池健康状态 = " + status);
    }
}
