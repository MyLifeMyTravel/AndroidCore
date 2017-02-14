package com.littlejie.filemanager.manager;

import android.content.Context;

import com.littlejie.core.util.SharePrefsUtil;
import com.littlejie.filemanager.util.Constant;

/**
 * 缓存管理
 * Created by littlejie on 2017/2/13.
 */

public class SharePrefsManager {

    private static final String DEFAULT_PREFS = "file_manager";

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

        showHiddenFile = sSharePrefs.getBoolean(Constant.KEY_SHOW_HIDDEN_FILE, false);
    }

    boolean isShowHiddenFile() {
        return showHiddenFile;
    }

    void setShowHiddenFile(boolean showHiddenFile) {
        this.showHiddenFile = showHiddenFile;
    }
}
