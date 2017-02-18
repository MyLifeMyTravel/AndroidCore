package com.littlejie.filemanager.modules.activity;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.ui.ConfigItem;
import com.littlejie.filemanager.R;
import com.littlejie.filemanager.manager.StorageManager;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/2/16.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.config_show_hidden_file)
    ConfigItem mConfigShowHiddenFile;

    @Override
    protected int getPageLayoutID() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mConfigShowHiddenFile.setChecked(StorageManager.isShowHiddenFile());
    }

    @Override
    protected void initViewListener() {
        mConfigShowHiddenFile.setOnCheckedListener(new ConfigItem.OnCheckedListener() {
            @Override
            public void onChecked(boolean isChecked) {
                StorageManager.setShowHiddenFile(isChecked);
            }
        });
    }

    @Override
    protected void process() {

    }
}
