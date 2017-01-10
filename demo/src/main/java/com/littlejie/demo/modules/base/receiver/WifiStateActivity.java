package com.littlejie.demo.modules.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;

public class WifiStateActivity extends BaseActivity {

    private WifiStateReceiver mReceiver;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_wifi_state;
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
        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mReceiver = new WifiStateReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    static class WifiStateReceiver extends BroadcastReceiver {

        public static final String TAG = WifiStateReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                if (wifiState == WifiManager.WIFI_STATE_DISABLING) {
                    Log.i(TAG, "正在关闭");
                } else if (wifiState == WifiManager.WIFI_STATE_ENABLING) {
                    Log.i(TAG, "正在打开");
                } else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                    Log.i(TAG, "已经关闭");
                } else if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                    Log.i(TAG, "已经打开");
                } else {
                    Log.i(TAG, "未知状态");
                }
            }
        }
    }
}
