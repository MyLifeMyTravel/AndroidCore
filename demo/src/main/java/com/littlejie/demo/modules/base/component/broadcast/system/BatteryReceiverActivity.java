package com.littlejie.demo.modules.base.component.broadcast.system;

import android.content.Intent;
import android.content.IntentFilter;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.reveiver.BatteryReceiver;
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

}
