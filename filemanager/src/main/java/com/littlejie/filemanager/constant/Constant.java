package com.littlejie.filemanager.constant;

import android.os.Environment;

import com.littlejie.core.util.FileUtil;
import com.littlejie.filemanager.manager.StorageManager;

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

    /*SharePrefs的key*/
    public static final String KEY_SHOW_HIDDEN_FILE = "show_hidden_file";

    public static final FileFilter HIDDEN_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return StorageManager.isShowHiddenFile() || !pathname.isHidden();
        }
    };

    public static final FileFilter IMAGE_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String mimeType = FileUtil.getMimeType(pathname.getAbsolutePath());
            return (StorageManager.isShowHiddenFile() || !pathname.isHidden())
                    && mimeType != null && mimeType.startsWith("image");
        }
    };
}
