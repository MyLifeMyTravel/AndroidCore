package com.littlejie.demo.modules.base.component.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.MainActivity;

/**
 * 注册静态广播，即使进程被杀，app 也能收到广播
 * Created by littlejie on 2017/2/6.
 */
@Description(description = "静态广播")
public class StaticBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = StaticBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
