package com.littlejie.filemanager.util;

import android.os.Environment;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by littlejie on 2017/1/10.
 */

public class Constant {

    //根目录
    public static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/FileExplorer";
    public static final String CACHE_DIR = ROOT + "/cache";
    public static final String IMAGE_CACHE_DIR = CACHE_DIR + "/image";
    public static final String EXTRA_PATH = "path";

    public static final FileFilter HIDDEN_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return !pathname.isHidden();
        }
    };
}
