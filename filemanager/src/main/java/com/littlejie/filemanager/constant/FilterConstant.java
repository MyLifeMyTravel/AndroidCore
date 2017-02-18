package com.littlejie.filemanager.constant;

import com.littlejie.core.util.FileUtil;
import com.littlejie.filemanager.manager.StorageManager;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by littlejie on 2017/2/17.
 */

public class FilterConstant {

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
