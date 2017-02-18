package com.littlejie.filemanager.constant;

import android.os.Environment;

/**
 * Created by littlejie on 2017/2/17.
 */

public class PathConstant {

    //App 根目录
    public static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/FileManager";
    public static final String CACHE_DIR = ROOT + "/cache";
    public static final String IMAGE_CACHE_DIR = CACHE_DIR + "/image";
}
