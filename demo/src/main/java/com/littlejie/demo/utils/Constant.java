package com.littlejie.demo.utils;

import android.net.Uri;
import android.os.Environment;

import com.littlejie.demo.R;

/**
 * Created by littlejie on 2017/1/23.
 */

public class Constant {

    public static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DEMO_FOLDER = ROOT + "/AndroidDemo";
    public static final String CACHE_FOLDER = DEMO_FOLDER + "/cache";

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_COLOR = "color";
    public static final String EXTRA_MENU = "hasOptionsMenu";
    public static final String EXTRA_NAME = "name";

    /*Notification*/
    public static final long[] VIBRATE = {0, 500, 1000, 1500};
    public static final Uri NOTIFICATION_SOUND =
            Uri.parse("android.resource://com.littlejie.notification/" + R.raw.beep);
}
