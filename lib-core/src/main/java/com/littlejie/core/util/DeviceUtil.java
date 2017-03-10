package com.littlejie.core.util;

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
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import com.littlejie.core.base.Core;
import com.littlejie.core.reveiver.USBStateReceiver;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 获取设备相关属性
 * Created by littlejie on 2016/12/1.
 */

public class DeviceUtil {

    private static final String TAG = DeviceUtil.class.getSimpleName();
    private static final String PREF_DEVICE = "device_pref";
    private static final String KEY_UUID = "key_uuid";
    private static ThreadLocal<String> sDeviceUIID = new ThreadLocal<String>();

    /**
     * 获取CPU个数
     *
     * @return CPU个数
     */
    public static int getCoreNum() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                return Pattern.matches("cpu[0-9]", pathname.getName());
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
     * @return 手机总内存(Bytes)
     */
    public static long getTotalMemory() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return statFs.getTotalBytes();
        } else {
            return statFs.getBlockSize() * statFs.getBlockCount();
        }
    }

    /**
     * 获取手机可用内存
     *
     * @return 手机总内存(Bytes)
     */
    public static long getAvailMemory() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return statFs.getTotalBytes();
        } else {
            return statFs.getBlockSize() * statFs.getAvailableBlocks();
        }
    }

    public static StatFs getStatFs(String path) {
        return new StatFs(path);
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

    /**
     * 获取设备序列号
     *
     * @return
     */
    public static String getDeviceSerial() {
        return getSystemProperty("ro.serialno");
    }

    /**
     * 获取设备 UUID
     *
     * @param context
     * @return
     */
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
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
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
     * @return 手机链接wifi的路由器的名字
     */
    public static String getWifiSSID(Context context) {
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            return wifiInfo.getSSID();
        }
        return "";
    }

    /**
     * @return 手机链接wifi的路由器的mac地址
     */
    public static String getWifiBSSID(Context context) {
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            return wifiInfo.getBSSID();
        }
        return "";
    }

    /**
     * 获取 wifi 的ip
     *
     * @param context
     * @return
     */
    public static String getWifiIP(Context context) {
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            int ip = wifiInfo.getIpAddress();
            return (ip & 0xff) + "." + (ip >> 8 & 0xff) + "."
                    + (ip >> 16 & 0xff) + "." + (ip >> 24 & 0xff);
        }
        return "";
    }

    /**
     * 获取wifi的mac地址
     *
     * @param context
     * @return
     */
    public static String getWifiMacAddress(Context context) {
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            return wifiInfo.getMacAddress();
        }
        return "";
    }

    /**
     * 获取wifi连接速度
     *
     * @param context
     * @return
     */
    public static int getWifiLinkSpeed(Context context) {
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            return wifiInfo.getLinkSpeed();
        }
        return 0;
    }

    /**
     * 获取 Wifi 信号强度
     * <p>
     * 得到的值是一个0到-100的区间值，是一个int型数据，
     * 其中0到-50表示信号最好，
     * -50到-70表示信号偏差，
     * 小于-70表示最差，
     * 有可能连接不上或者掉线，
     * 一般Wifi已断则值为-200。
     * </p>
     *
     * @param context
     * @return
     */
    public static int getWifiRssi(Context context) {
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            return wifiInfo.getRssi();
        }
        return 0;
    }

    public static WifiInfo getWifiInfo(Context context) {
        WifiManager mWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifi.isWifiEnabled()) {
            return mWifi.getConnectionInfo();
        }
        return null;
    }

    /**
     * 获取手机 IP 列表
     * 一般只有一个IPV4地址，但当手机打开热点时会有两个
     *
     * @return
     */
    public static List<String> getMobileIP() {
        List<String> lstIP = new ArrayList<>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                        lstIP.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstIP;
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
            bool = !((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists()));

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
        Log.d(TAG, "register USBStateReceiver");
        context.registerReceiver(receiver, new IntentFilter(Constant.Action.USB_STATE));
    }

    public static void unregisterUSBStateReceiver(Context context, USBStateReceiver receiver) {
        Log.d(TAG, "unregister USBStateReceiver");
        context.unregisterReceiver(receiver);
    }

    /**
     * 获取当前系统语言
     *
     * @return
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统国家
     *
     * @return
     */
    public static String getSystemCountry() {
        return Locale.getDefault().getCountry();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取存储路径
     */
    public static String[] getStoragePath() {
        StorageManager storageManager = (StorageManager) Core.getApplicationContext().getSystemService(Context
                .STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
            getVolumePathsMethod.setAccessible(true);
            Object[] params = {};
            Object invoke = getVolumePathsMethod.invoke(storageManager, params);
            return (String[]) invoke;
        } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 获取除内置存储以外的所有存储路径
     *
     * @return
     */
    public static List<String> getExtSDCardPath() {
        String[] paths = getStoragePath();
        if (paths == null || paths.length == 0) {
            return null;
        }
        List<String> lstPath = new ArrayList<>();
        for (String path : paths) {
            if (Environment.getExternalStorageDirectory().getAbsolutePath().equals(path)) {
                continue;
            }
            lstPath.add(path);
        }
        return lstPath;
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

    /**
     * 手机制造商
     *
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机型号，可能为unknown
     *
     * @return
     */
    public static String getMobileModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getMobileBrand() {
        return Build.BRAND;
    }

}
