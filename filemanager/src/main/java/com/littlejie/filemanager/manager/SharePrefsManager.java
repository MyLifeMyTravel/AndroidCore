package com.littlejie.filemanager.manager;

import android.content.Context;

import com.littlejie.core.util.SharePrefsUtil;

/**
 * 缓存管理，只对StorageManager可见
 * Created by littlejie on 2017/2/13.
 */

final class SharePrefsManager {

    static final String DEFAULT_PREFS = "file_manager";
    static final String KEY_SHOW_HIDDEN_FILE = "show_hidden_file";

    private static SharePrefsManager sInstance;
    private static SharePrefsUtil sSharePrefs;

    private boolean showHiddenFile = false;

    private SharePrefsManager() {
    }

    static synchronized SharePrefsManager getInstance() {
        if (sInstance == null) {
            sInstance = new SharePrefsManager();
        }
        return sInstance;
    }

    void init(Context context) {
        init(context, DEFAULT_PREFS);
    }

    private void init(Context context, String prefs) {
        sSharePrefs = SharePrefsUtil.getInstance();
        sSharePrefs.init(context, prefs);

        showHiddenFile = sSharePrefs.getBoolean(KEY_SHOW_HIDDEN_FILE, false);
    }

    boolean isShowHiddenFile() {
        return showHiddenFile;
    }

    void setShowHiddenFile(boolean showHiddenFile) {
        this.showHiddenFile = showHiddenFile;
        sSharePrefs.setBoolean(KEY_SHOW_HIDDEN_FILE, showHiddenFile);
    }
}
