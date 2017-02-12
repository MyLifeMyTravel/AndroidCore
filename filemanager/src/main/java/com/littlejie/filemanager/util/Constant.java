package com.littlejie.filemanager.util;

import android.os.Environment;

import com.littlejie.core.util.FileUtil;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by littlejie on 2017/1/10.
 */

public class Constant {

    //App 根目录
    public static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/FileManager";
    public static final String CACHE_DIR = ROOT + "/cache";
    public static final String IMAGE_CACHE_DIR = CACHE_DIR + "/image";

    /*参数*/
    public static final String EXTRA_PATH = "path";
    public static final String EXTRA_FILES = "files";

    public static final FileFilter HIDDEN_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return !pathname.isHidden();
        }
    };

    public static final FileFilter IMAGE_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String mimeType = FileUtil.getMimeType(pathname.getAbsolutePath());
            return !pathname.isHidden() && mimeType != null && mimeType.startsWith("image");
        }
    };
}
