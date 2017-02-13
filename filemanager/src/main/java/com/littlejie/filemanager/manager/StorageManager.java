package com.littlejie.filemanager.manager;

import android.content.Context;

/**
 * Created by littlejie on 2017/2/13.
 */

public class StorageManager {

    public static void init(Context context) {
        SharePrefsManager.getInstance().init(context);
    }

    public static boolean isShowHiddenFile() {
        return SharePrefsManager.getInstance().isShowHiddenFile();
    }

    public static void setShowHiddenFile(boolean showHiddenFile) {
        SharePrefsManager.getInstance().setShowHiddenFile(showHiddenFile);
    }

}
