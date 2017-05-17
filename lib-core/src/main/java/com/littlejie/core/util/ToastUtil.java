package com.littlejie.core.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.littlejie.core.base.Core;

/**
 * Toast工具类
 * Created by littlejie on 2017/1/5.
 */

public class ToastUtil {

    private static Context sContext;

    /**
     * 建议使用 ApplicationContext 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        sContext = context;
    }

    public static void showDefaultToast(CharSequence s) {
        Toast.makeText(sContext, s, Toast.LENGTH_SHORT).show();
    }

    public static void showDefaultToast(int resId) {
        Toast.makeText(sContext, Core.getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void showCustomToast(Context context, int layout, int gravity, int duration) {
        View customView = LayoutInflater.from(context).inflate(layout, null);
        showCustomToast(customView, gravity, duration);
    }

    public static void showCustomToast(Context context, int layout) {
        showCustomToast(context, layout, Gravity.CENTER, Toast.LENGTH_SHORT);
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
