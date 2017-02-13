package com.littlejie.filemanager;

import com.littlejie.core.base.BaseApplication;
import com.littlejie.core.util.ImageLoaderUtil;
import com.littlejie.filemanager.manager.StorageManager;
import com.littlejie.filemanager.util.Constant;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by littlejie on 2017/2/10.
 */

public class FileApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initLeakCanary();
        ImageLoaderUtil.init(this, Constant.IMAGE_CACHE_DIR);
        StorageManager.init(this);
        StorageManager.setShowHiddenFile(true);
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
