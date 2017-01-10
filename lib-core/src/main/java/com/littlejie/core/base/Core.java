package com.littlejie.core.base;

import android.content.Context;

import com.littlejie.core.net.HttpManager;
import com.littlejie.core.util.ClipboardUtil;
import com.littlejie.core.util.ToastUtil;

/**
 * Created by littlejie on 2016/12/1.
 */

public class Core {

    private static Context mContext;
    private static ClipboardUtil mClipboardManager;

    public static Context getApplicationContext() {
        return BaseApplication.getInstance();
    }

    static void init() {
        mContext = getApplicationContext();
        mClipboardManager = ClipboardUtil.init(mContext);
        HttpManager.init();
        ToastUtil.init(mContext);
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

    public static ClipboardUtil getClipboardManager() {
        return mClipboardManager;
    }
}
