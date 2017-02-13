package com.littlejie.filemanager.manager;

import android.content.Context;

import com.littlejie.core.util.SharePrefsUtil;
import com.littlejie.filemanager.util.Constant;

/**
 * Created by littlejie on 2017/2/13.
 */

public class SharePrefsManager {

    public static final String DEFAULT_PREFS = "file_manager";

    public static SharePrefsManager sInstance;
    private static SharePrefsUtil sSharePrefs;

    private boolean showHiddenFile = false;

    private SharePrefsManager() {
    }

    public static synchronized SharePrefsManager getInstance() {
        if (sInstance == null) {
            sInstance = new SharePrefsManager();
        }
        return sInstance;
    }

    public void init(Context context) {
        init(context, DEFAULT_PREFS);
    }

    public void init(Context context, String prefs) {
        sSharePrefs = SharePrefsUtil.getInstance();
        sSharePrefs.init(context, prefs);

        showHiddenFile = sSharePrefs.getBoolean(Constant.KEY_SHOW_HIDDEN_FILE, false);
    }

    public boolean isShowHiddenFile() {
        return showHiddenFile;
    }

    public void setShowHiddenFile(boolean showHiddenFile) {
        this.showHiddenFile = showHiddenFile;
    }
}
