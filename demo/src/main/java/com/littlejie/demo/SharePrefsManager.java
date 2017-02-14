package com.littlejie.demo;

import android.content.Context;
import android.text.TextUtils;

import com.littlejie.core.util.JsonUtil;
import com.littlejie.core.util.SharePrefsUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 缓存管理
 * Created by littlejie on 2017/2/13.
 */

public class SharePrefsManager {

    private static final String DEFAULT_PREFS = "file_manager";

    //key
    private static final String KEY_PACKAGE_SELECT = "package_select";

    private static SharePrefsManager sInstance;
    private static SharePrefsUtil sSharePrefs;

    private Map<String, Boolean> mPackageSelectMap = new HashMap<>();

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

    private void init(Context context, String prefs) {
        sSharePrefs = SharePrefsUtil.getInstance();
        sSharePrefs.init(context, prefs);
        initPackageSelectMap();
    }

    private void initPackageSelectMap() {
        String cache = sSharePrefs.getString(KEY_PACKAGE_SELECT, null);
        Map<String, Object> map = null;
        if (TextUtils.isEmpty(cache) || (map = JsonUtil.json2Map(cache)) == null) {
            return;
        }
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            mPackageSelectMap.put(key, (Boolean) map.get(key));
        }
    }

    public void setPackageSelect(Map<String, Boolean> packageSelectMap) {
        mPackageSelectMap = packageSelectMap;
        Map<String, Object> map = new HashMap<>();
        Set<String> keySet = mPackageSelectMap.keySet();
        for (String key : keySet) {
            map.put(key, packageSelectMap.get(key));
        }
        sSharePrefs.setString(KEY_PACKAGE_SELECT, JsonUtil.map2Json(map));
    }

    public Map<String, Boolean> getPackageSelect() {
        return mPackageSelectMap;
    }

}
