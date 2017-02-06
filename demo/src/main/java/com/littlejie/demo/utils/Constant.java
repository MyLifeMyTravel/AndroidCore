package com.littlejie.demo.utils;

import android.os.Environment;

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
}
