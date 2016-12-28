package com.littlejie.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

/**
 * 常见 Intent 工具类
 * Created by littlejie on 2016/12/1.
 */

public class IntentUtil {

    /**
     * 异步扫描指定文件
     *
     * @param context
     * @param path    文件路径
     */
    public static void scanFileAsync(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(path)));
        context.sendBroadcast(intent);
    }

    /**
     * 异步扫描指定文件夹
     *
     * @param context
     * @param dir     文件目录
     */
    public void scanDirAsync(Context context, String dir) {
        Intent scanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR");
        scanIntent.setData(Uri.fromFile(new File(dir)));
        context.sendBroadcast(scanIntent);
    }

    /**
     * 选择文件
     *
     * @param activity
     * @param requestCode
     */
    public static void pickupFile(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 根据应用包名，跳转到应用市场
     *
     * @param activity    承载跳转的Activity
     * @param packageName 所需下载（评论）的应用包名
     */
    public static void goToMarket(Activity activity, String packageName) {
        try {
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "您没有安装应用市场", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 调用系统浏览器打开指定网址
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * 打开设置界面
     *
     * @param context
     * @param action  查看{@link android.provider.Settings}
     */
    public static void openSetting(Context context, String action) {
        Intent intent = new Intent(action);
        context.startActivity(intent);
    }
}
