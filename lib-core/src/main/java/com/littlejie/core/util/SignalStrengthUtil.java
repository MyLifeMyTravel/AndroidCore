package com.littlejie.core.util;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by littlejie on 2017/1/11.
 */

public class SignalStrengthUtil {

    private static final String TAG = SignalStrengthUtil.class.getSimpleName();
    private static int mSignalStrength;

    /**
     * 获取手机信号强度，第一次获取的时候可能为0
     * 单位：dBm
     *
     * @return
     */
    public static void init(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
                String[] signals = signalStrength.toString().split(" ");
                mSignalStrength = Integer.valueOf(signals[9]);
                Log.d(TAG, "获取信号强度(dBm) = " + mSignalStrength);
            }
        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public static int getRawSignalStrength() {
        return mSignalStrength;
    }

    /**
     * gsm信号强度分级详见
     * https://m10.home.xs4all.nl/mac/downloads/3GPP-27007-630.pdf
     *
     * @return
     */
    public static SignalStrength getSignalStrength() {
        int dBm = mSignalStrength;
        if (dBm <= -113) {
            return SignalStrength.BAD;
        } else if (dBm <= -111) {
            return SignalStrength.NORMAL;
        } else if (dBm >= -109 && dBm <= -53) {
            return SignalStrength.GOOD;
        } else if (dBm == -51) {
            return SignalStrength.VERY_GOOD;
        } else {
            return SignalStrength.UNKNOWN;
        }
    }

    enum SignalStrength {
        VERY_GOOD, GOOD, NORMAL, BAD, UNKNOWN
    }
}
