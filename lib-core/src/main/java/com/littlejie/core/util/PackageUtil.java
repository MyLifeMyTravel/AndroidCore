package com.littlejie.core.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取应用相关属性，如版本、包名
 * Created by littlejie on 2016/12/1.
 */

public class PackageUtil {

    private static final String TAG = PackageUtil.class.getSimpleName();

    public enum Filter {
        System, User, All
    }

    /**
     * 获取 APP 的名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        return getPackageInfo(context).applicationInfo.loadLabel(context.getPackageManager()).toString();
    }

    /**
     * 版本名
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 版本号
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        return getPackageInfo(context).packageName;
    }

    /**
     * 判断是否为系统应用
     *
     * @param context
     * @param packageName 包名
     * @return
     */
    public static boolean isSystemApp(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return isSystemApp(ai);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSystemApp(PackageInfo info) {
        return isSystemApp(info.applicationInfo);
    }

    public static boolean isSystemApp(ApplicationInfo ai) {
        if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0
                || (ai.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        }
        return false;
    }

    public static boolean isUninstall(ApplicationInfo applicationInfo) {
        return false;
    }

    /**
     * 获取APK图标
     *
     * @param context
     * @param apkPath apk文件所在路径
     * @return
     */
    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                Log.e("ApkIconLoader", e.toString());
            }
        }
        return null;
    }

    /**
     * @param context
     * @param filter
     * @return
     */
    public static List<PackageInfo> getPackageInfos(Context context, Filter filter) {
        List<PackageInfo> all = context.getPackageManager().getInstalledPackages(0);
        if (filter == null || filter == Filter.All) {
            return all;
        }
        List<PackageInfo> packageInfos = new ArrayList<>();
        int size = all.size();
        for (int i = 0; i < size; i++) {
            PackageInfo packageInfo = all.get(i);
            // 如果属于非系统程序，则添加到列表显示
            boolean isSystemApp = isSystemApp(context, packageInfo.packageName);
            Log.d(TAG, "getPackageInfos: isSystemApp = " + isSystemApp + ";package = " + packageInfo.packageName);
            if ((filter == Filter.System && isSystemApp)
                    || (filter == Filter.User && !isSystemApp)) {
                packageInfos.add(packageInfo);
            }

        }
        return packageInfos;
    }

    public static List<PackageInfo> getAllInstallApp(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.getInstalledPackages(0);
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

}
