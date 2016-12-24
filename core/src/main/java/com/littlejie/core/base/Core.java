package com.littlejie.core.base;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.littlejie.core.net.HttpManager;
import com.littlejie.core.util.ClipboardUtil;

/**
 * Created by littlejie on 2016/12/1.
 */

public class Core {

    private static Context mContext;
    private static ClipboardUtil mClipboardManager;

    public static Context getApplicationContext() {
        return BaseApplication.getInstance();
    }

    public static void init() {
        mContext = getApplicationContext();
        mClipboardManager = ClipboardUtil.init(mContext);
        HttpManager.init();
    }

    // 执行异步任务
    public static void runOnUIThread(Runnable r) {
        BaseApplication.runOnUIThread(r);
    }

    // 延时执行异步任务
    public static void runOnUIThreadDelayed(long milliSec, Runnable r) {
        BaseApplication.runDelayOnUIThread(r, milliSec);
    }

    // 移除异步任务
    public static void removeRunnable(Runnable r) {
        BaseApplication.removeRunnable(r);
    }

    public static int getPixel(int resId) {
        return mContext.getResources().getDimensionPixelSize(resId);
    }

    public static String getString(int resId) {
        return mContext.getResources().getString(resId);
    }

    public static String getString(int resId, String arg1) {
        return mContext.getResources().getString(resId, arg1);
    }

    public static void showDefautToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    public static void showDefautToast(int resId) {
        Toast.makeText(mContext, getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void showCustomToast(int layout, int gravity, int duration) {
        View customView = LayoutInflater.from(mContext).inflate(layout, null);
        showCustomToast(customView, gravity, duration);
    }

    public static void showCustomToast(int layout) {
        showCustomToast(layout, Gravity.CENTER, Toast.LENGTH_SHORT);
    }

    public static void showCustomToast(View view) {
        showCustomToast(view, Gravity.CENTER, Toast.LENGTH_SHORT);
    }

    public static void showCustomToast(View view, int gravity, int duration) {
        Toast toast = new Toast(view.getContext());
        toast.setView(view);
        toast.setGravity(gravity, 0, 0);
        toast.setDuration(duration);
        toast.show();
    }

    public static ClipboardUtil getClipboardManager() {
        return mClipboardManager;
    }
}
