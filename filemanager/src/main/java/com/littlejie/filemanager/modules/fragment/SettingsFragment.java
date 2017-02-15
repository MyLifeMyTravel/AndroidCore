package com.littlejie.filemanager.modules.fragment;

import android.os.Bundle;
import android.view.View;

import com.littlejie.core.base.BaseFragment;
import com.littlejie.filemanager.R;
import com.littlejie.filemanager.manager.StorageManager;
import com.littlejie.filemanager.ui.ConfigItem;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/2/14.
 */

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.config_show_hidden_file)
    ConfigItem mConfigShowHiddenFile;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getPageLayoutID() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
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
    protected void process(Bundle savedInstanceState) {

    }
}
