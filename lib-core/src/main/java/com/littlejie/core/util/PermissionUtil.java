package com.littlejie.core.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Android 权限相关工具类，如：Android 6.0 之后的运行时权限
 * Created by littlejie on 2016/12/1.
 */

public class PermissionUtil {

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
