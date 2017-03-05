package com.littlejie.filemanager;

import com.littlejie.core.base.BaseApplication;
import com.littlejie.core.util.ImageLoaderUtil;
import com.littlejie.filemanager.constant.PathConstant;
import com.littlejie.filemanager.manager.StorageManager;
import com.littlejie.filemanager.manager.TintDrawableManager;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by littlejie on 2017/2/10.
 */

public class FileApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initUmengSetting();
        initLeakCanary();
        ImageLoaderUtil.init(this, PathConstant.IMAGE_CACHE_DIR);
        StorageManager.init(this);
        TintDrawableManager.init(this);
    }

    private void initUmengSetting() {
        //禁用友盟默认页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        //设置是否对日志信息进行加密, 默认false(不加密).
        //AnalyticsConfig.enableEncrypt( boolean enable);//6.0.0版本以前
        MobclickAgent.enableEncrypt(BuildConfig.DEBUG);//6.0.0版本及以后
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
