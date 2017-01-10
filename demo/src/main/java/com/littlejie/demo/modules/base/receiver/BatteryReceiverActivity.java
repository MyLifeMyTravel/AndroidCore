package com.littlejie.demo.modules.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;

/**
 * 电池状态广播只能动态注册，如要全局获取，则在 Application 中注册
 */
public class BatteryReceiverActivity extends BaseActivity {

    private BatteryReceiver mBatteryReceiver;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_battery_receiver;
    }

    @Override
    protected void initData() {
        //注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        //从电池电量过低转变为正常时发送该广播
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        //创建广播接受者对象
        mBatteryReceiver = new BatteryReceiver();
        //注册receiver
        registerReceiver(mBatteryReceiver, intentFilter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBatteryReceiver);
    }

    static class BatteryReceiver extends BroadcastReceiver {

        private static final String TAG = BatteryReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                //当前电池电量
                int level = intent.getIntExtra("level", 0);
                //电池的总量
                int scale = intent.getIntExtra("scale", 100);
                //电池状态，如：充电
                int status = intent.getIntExtra("status", 0);
                //电池健康状态
                int health = intent.getIntExtra("health", 0);
                //充电类型
                int plugged = intent.getIntExtra("plugged", 0);
                //电池电压
                int voltage = intent.getIntExtra("voltage", 0);
                //电池温度，温度需要 * 0.1
                int temperature = intent.getIntExtra("temperature", 0);
                //电池使用的技术。比如，对于锂电池是Li-ion
                String technology = intent.
                        getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
                Log.d(TAG, "当前电池电量 = " + level + ";电池总量 = " + scale);
                Log.d(TAG, "当前电池电压 = " + voltage + ";电池温度 = " + temperature * 0.1 + "℃");
                Log.d(TAG, "电池类型 = " + technology);
                printPluggedType(plugged);
                printBatteryStatus(status);
                printBatteryHealthStatus(health);
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
}
