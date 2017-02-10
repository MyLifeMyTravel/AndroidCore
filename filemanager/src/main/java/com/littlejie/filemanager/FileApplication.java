package com.littlejie.filemanager;

import com.littlejie.core.base.BaseApplication;
import com.littlejie.core.util.ImageLoaderUtil;
import com.littlejie.filemanager.util.Constant;

/**
 * Created by littlejie on 2017/2/10.
 */

public class FileApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderUtil.init(this, Constant.IMAGE_CACHE_DIR);
    }
}
