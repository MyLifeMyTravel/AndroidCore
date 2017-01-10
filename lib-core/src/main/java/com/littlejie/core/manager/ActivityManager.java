package com.littlejie.core.manager;

import android.content.Context;
import android.content.Intent;

/**
 * Created by littlejie on 2017/1/6.
 */

public class ActivityManager {

    public static void startActivity(Context context, Class clz) {
        context.startActivity(new Intent(context, clz));
    }
}
