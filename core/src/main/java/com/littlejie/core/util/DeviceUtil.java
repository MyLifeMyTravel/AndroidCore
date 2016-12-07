package com.littlejie.core.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import com.littlejie.core.reveiver.USBStateReceiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 获取设备相关属性
 * Created by littlejie on 2016/12/1.
 */

public class DeviceUtil {

    private static final String TAG = DeviceUtil.class.getSimpleName();
    private static final String PREF_DEVICE = "com.kuaidi.daijia.driver.device_pref";
    private static final String KEY_UUID = "key_uuid";
    private static ThreadLocal<String> sDeviceUIID = new ThreadLocal<String>();

    /**
     * 获取CPU个数
     */
    public static int getCoreNum() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Default to return 1 core
            return 1;
        }
    }

    /**
     * 获取手机总内存
     *
     * @return 手机总内存(兆)
     */
    public static long getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            if (!TextUtils.isEmpty(str2)) {
                arrayOfString = str2.split("\\s+");
                initial_memory = Integer.valueOf(arrayOfString[1]).intValue() / 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            }
            localBufferedReader.close();
            localFileReader.close();
        } catch (IOException e) {
        }
        return initial_memory;// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * @param context
     * @return 手机当前可用内存(兆)
     */
    public static long getAvailMemory(Context context) {// 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem / 1024 / 1024;
    }

    /**
     * 获取系统属性
     *
     * @param propertyName 系统属性名
     * @return 属性值
     */
    public static String getSystemProperty(String propertyName) {
        String propertyValue = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            java.lang.reflect.Method get = c.getMethod("get", String.class);
            propertyValue = (String) get.invoke(c, propertyName);
        } catch (Exception ignored) {
            Log.e(TAG, "Failed to get the property-name: " + propertyName, ignored);
        }

        return propertyValue;
    }

    public static String getDeviceSerial() {
        return getSystemProperty("ro.serialno");
    }

    public synchronized static String getDeviceUUID(Context context) {
        String uuid = sDeviceUIID.get();
        if (TextUtils.isEmpty(uuid)) {
            SharedPreferences sp = context.getSharedPreferences(
                    PREF_DEVICE, Context.MODE_PRIVATE);
            uuid = sp.getString(KEY_UUID, null);
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
                sp.edit().putString(KEY_UUID, uuid).commit();
            }
            sDeviceUIID.set(uuid);
        }
        return uuid;
    }

    /**
     * @return imei
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        return imei;
    }

    /**
     * @return imsi
     */
    public static String getIMSI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId();
        return imsi;
    }

    /**
     * @return 当前sim卡手机号(很有可能是空)
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String mob = tm.getLine1Number();
        if (mob == null) {
            mob = "";
        }
        return mob;
    }

    /**
     * @return 手机链接wifi的路由器的名字
     */
    public static String getWifiSSID(Context context) {
        WifiManager mWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifi.isWifiEnabled()) {
            WifiInfo wifiInfo = mWifi.getConnectionInfo();
            return wifiInfo.getSSID();
        }
        return "";
    }

    /**
     * @return 手机链接wifi的路由器的mac地址
     */
    public static String getWifiBSSID(Context context) {
        WifiManager mWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifi.isWifiEnabled()) {
            WifiInfo wifiInfo = mWifi.getConnectionInfo();
            return wifiInfo.getBSSID();
        }
        return "";
    }

    /**
     * @return 基站ID
     */
    public static String getCellID(Context context) {
        if (PermissionUtil.checkPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            CellLocation location = (tm == null) ? null : tm.getCellLocation();
            if (location != null) {
                if (location instanceof GsmCellLocation) {
                    GsmCellLocation gsm = (GsmCellLocation) tm.getCellLocation();
                    if (gsm != null) {
                        return String.valueOf(gsm.getCid());
                    }
                } else if (location instanceof CdmaCellLocation) {
                    CdmaCellLocation cdma = (CdmaCellLocation) tm.getCellLocation();
                    if (cdma != null) {
                        return String.valueOf(cdma.getBaseStationId());
                    }
                }
            }
        }
        return "";
    }

    /**
     * @return 基站lac
     */
    public static String getLac(Context context) {
        if (PermissionUtil.checkPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            CellLocation location = tm.getCellLocation();
            if (location != null) {
                if (location instanceof GsmCellLocation) {
                    GsmCellLocation gsm = (GsmCellLocation) tm.getCellLocation();
                    if (gsm != null) {
                        return String.valueOf(gsm.getLac());
                    }
                } else if (location instanceof CdmaCellLocation) {
                    CdmaCellLocation cdma = (CdmaCellLocation) tm.getCellLocation();
                    if (cdma != null) {
                        return String.valueOf(cdma.getNetworkId());
                    }
                }
            }
        }
        return "";
    }

    /**
     * @return 手机mac地址
     */
    public static String getDeviceMac(Context context) {
        WifiManager mWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifi.isWifiEnabled()) {
            WifiInfo wifiInfo = mWifi.getConnectionInfo();
            return wifiInfo.getMacAddress();
        }
        return "";
    }

    /**
     * 判断wifi是否开启
     *
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return (wm != null) && (wm.getWifiState() == WifiManager.WIFI_STATE_ENABLED || wm.getWifiState() == WifiManager.WIFI_STATE_ENABLING);
    }

    /**
     * Is wifi connected
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnected()
                && (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * If wifi connected, disconnect
     *
     * @param context
     */
    public static void disconnectWifi(Context context) {
        if (isWifiConnected(context)) {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wm != null) {
                List<WifiConfiguration> configs = wm.getConfiguredNetworks();
                if (wm.getConnectionInfo() != null && configs != null) {
                    Iterator<WifiConfiguration> iter = configs.iterator();
                    while (iter.hasNext()) {
                        WifiConfiguration config = iter.next();
                        if (config.networkId == wm.getConnectionInfo().getNetworkId()) {
                            wm.disableNetwork(wm.getConnectionInfo().getNetworkId());
                            return;
                        }
                    }

                }
                wm.disconnect();
            }
        }
    }

    /**
     * 判断是否为某品牌生产的手机
     */
    public static boolean isProduceByBrand(String brand) {
        boolean result = false;
        //判断厂家
        if (Build.MANUFACTURER != null) {
            if (Build.MANUFACTURER.toLowerCase().contains(brand)) {
                result = true;
            }
        }
        //判断品牌
        if (!result && Build.BRAND != null) {
            if (Build.BRAND.toLowerCase().contains(brand)) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isProductInBrands(String... brands) {
        for (String brand : brands) {
            if (isProduceByBrand(brand)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSimReady(Context context) {
        try {
            TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return TelephonyManager.SIM_STATE_READY == mgr.getSimState() || TelephonyManager.SIM_STATE_NETWORK_LOCKED == mgr.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断当前手机是否有ROOT权限
     *
     * @return
     */
    public static boolean isRoot() {
        boolean bool = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }

        } catch (Exception e) {

        }
        return bool;
    }

    /**
     * 判断当前设备是否连接USB
     *
     * @param context
     * @return
     */
    public static boolean isUSBConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Constant.Action.USB_STATE));
        return intent.getExtras().getBoolean("connected");
    }

    /**
     * 注册USB链接广播
     *
     * @param context
     * @param receiver
     */
    public static void registerUSBStateReceiver(Context context, USBStateReceiver receiver) {
        Log.d(TAG,"register USBStateReceiver");
        context.registerReceiver(receiver, new IntentFilter(Constant.Action.USB_STATE));
    }

    public static void unregisterUSBStateReceiver(Context context, USBStateReceiver receiver) {
        Log.d(TAG,"unregister USBStateReceiver");
        context.unregisterReceiver(receiver);
    }

    /**
     * 简单判断设备是否是模拟器
     *
     * @return
     */
    public static boolean isEmulator() {
        return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk")
                || Build.MODEL.equals("Android SDK built for x86"));
    }

    public static String getSysInfo() {
        StringBuffer sysInfo = new StringBuffer();
        sysInfo.append("ID " + Build.ID + ";");
        sysInfo.append("DISPLAY " + Build.DISPLAY + ";");
        sysInfo.append("PRODUCT " + Build.PRODUCT + ";");
        sysInfo.append("DEVICE " + Build.DEVICE + ";");
        sysInfo.append("BOARD " + Build.BOARD + ";");
        sysInfo.append("MODEL " + Build.MODEL + ";");
        sysInfo.append("BRAND " + Build.BRAND + ";");
        sysInfo.append("MANUFACTURER " + Build.MANUFACTURER + ";");

        sysInfo.append("HARDWARE " + Build.HARDWARE + ";");
        sysInfo.append("SERIAL " + Build.SERIAL + ";");

        sysInfo.append("VERSION.INCREMENTAL " + Build.VERSION.INCREMENTAL + ";");
        sysInfo.append("VERSION.RELEASE " + Build.VERSION.RELEASE + ";");
        sysInfo.append("VERSION.SDK " + Build.VERSION.SDK_INT + ";");
        sysInfo.append("VERSION.CODENAME " + Build.VERSION.CODENAME + ";");

        sysInfo.append("TYPE " + Build.TYPE + ";");
        sysInfo.append("TAGS " + Build.TAGS + ";");
        sysInfo.append("FINGERPRINT " + Build.FINGERPRINT + ";");

        //用于内部开发
        sysInfo.append("USER " + Build.USER + ";");
        sysInfo.append("HOST " + Build.HOST);
        return sysInfo.toString();
    }

}
