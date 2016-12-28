package com.littlejie.core.reveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.littlejie.core.listener.NotifyListener;
import com.littlejie.core.util.Constant;

/**
 * USB 状态监听广播
 * Created by littlejie on 2016/12/6.
 */

public class USBStateReceiver extends BroadcastReceiver {

    private static final String TAG = USBStateReceiver.class.getSimpleName();

    private NotifyListener mListener;

    public USBStateReceiver(NotifyListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Constant.Action.USB_STATE.equals(action)) {
            boolean isConnected = intent.getExtras().getBoolean("connected");
            Log.d(TAG, "usb is connect = " + isConnected);
            mListener.onNotify(isConnected, null);
        }
    }

}
