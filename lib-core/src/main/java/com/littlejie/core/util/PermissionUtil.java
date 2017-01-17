package com.littlejie.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Android 权限相关工具类，如：Android 6.0 之后的运行时权限
 * 完善中...
 * Created by littlejie on 2016/12/1.
 */

public class PermissionUtil {

    /**
     * 运行时权限申请结果回调接口
     */
    public interface OnRequestPermissionResult {
        /**
         * 用户授权的权限
         *
         * @param permissions
         */
        void onGranted(String[] permissions);

        /**
         * 用户拒绝授权的权限
         *
         * @param permissions
         */
        void onDenied(String[] permissions);
    }

    /**
     * 申请授权
     *
     * @param activity
     * @param permissions               需要授权的权限
     * @param explain                   解释申请该权限的原因
     * @param onRequestPermissionResult 回调
     */
    public static void requestPermissions(Activity activity, String[] permissions, String[] explain,
                                          OnRequestPermissionResult onRequestPermissionResult) {
        List<String> lstRequestPermission = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                lstRequestPermission.add(permission);
            }
        }
        String[] requestPermissions = new String[lstRequestPermission.size()];
        ActivityCompat.requestPermissions(activity, requestPermissions, 1);
    }

    /**
     * @param permission
     * @return App是否拥有permission权限
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean permit = false;
        // 如果在判断的时候报错（有些机型），则默认不满足此权限
        try {
            permit = PackageManager.PERMISSION_GRANTED == context
                    .checkCallingOrSelfPermission(permission);
        } catch (Exception e) {
            return permit;
        }
        return permit;
    }
}
