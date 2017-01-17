package com.littlejie.core.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import static com.littlejie.core.base.Core.getString;

/**
 * Toast工具类
 * Created by littlejie on 2017/1/5.
 */

public class ToastUtil {

    private static Context mContext;

    /**
     * 建议使用 ApplicationContext 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        mContext = context;
    }

    public static void showDefaultToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    public static void showDefaultToast(int resId) {
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
}
