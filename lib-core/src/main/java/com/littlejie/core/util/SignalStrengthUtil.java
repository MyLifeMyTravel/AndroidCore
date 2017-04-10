package com.littlejie.core.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by littlejie on 2017/1/11.
 */

public class SignalStrengthUtil {

    private static final String TAG = SignalStrengthUtil.class.getSimpleName();

    private static final int NETWORKTYPE_WIFI = 0;
    private static final int NETWORKTYPE_4G = 1;
    private static final int NETWORKTYPE_2G = 2;
    private static final int NETWORKTYPE_NONE = 3;
    private static int mSignalStrength;

    private static Context sContext;

    private static PhoneStateListener sPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
//            String[] signals = signalStrength.toString().split(" ");
//            mSignalStrength = Integer.valueOf(signals[9]);
//            Log.d(TAG, "信号强度 signals = " + Arrays.toString(signals));
//            Log.d(TAG, "获取信号强度(dBm) = " + mSignalStrength);
            //获取网络信号强度
            //获取0-4的5种信号级别，越大信号越好,但是api23开始才能用
            // int level = signalStrength.getLevel();
            int gsmSignalStrength = signalStrength.getGsmSignalStrength();
            mSignalStrength = gsmSignalStrength;
            //获取网络类型
            int netWorkType = getNetWorkType(sContext);
            switch (netWorkType) {
                case NETWORKTYPE_WIFI:
                    Log.d(TAG, "当前网络为wifi,信号强度为：" + gsmSignalStrength);
                    break;
                case NETWORKTYPE_2G:
                    Log.d(TAG, "当前网络为2G移动网络,信号强度为：" + gsmSignalStrength);
                    break;
                case NETWORKTYPE_4G:
                    Log.d(TAG, "当前网络为4G移动网络,信号强度为：" + gsmSignalStrength);
                    break;
                case NETWORKTYPE_NONE:
                    Log.d(TAG, "当前没有网络,信号强度为：" + gsmSignalStrength);
                    break;
                case -1:
                    Log.d(TAG, "当前网络错误,信号强度为：" + gsmSignalStrength);
                    break;
            }
        }
    };

    public static int getNetWorkType(Context context) {
        int mNetWorkType = -1;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                return isFastMobileNetwork(context) ? NETWORKTYPE_4G : NETWORKTYPE_2G;
            }
        } else {
            mNetWorkType = NETWORKTYPE_NONE;//没有网络
        }
        return mNetWorkType;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //这里只简单区分两种类型网络，认为4G网络为快速，但最终还需要参考信号值
        return telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 获取手机信号强度，第一次获取的时候可能为0
     * 单位：dBm
     *
     * @return
     */
    public static void init(Context context) {
        sContext = context;
        TelephonyManager telephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(sPhoneStateListener, PhoneStateListener.LISTEN_SERVICE_STATE
                | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                | PhoneStateListener.LISTEN_CALL_STATE
                | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
                | PhoneStateListener.LISTEN_DATA_ACTIVITY);
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
