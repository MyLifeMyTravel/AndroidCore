package com.littlejie.demo.modules;

import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.DeviceUtil;
import com.littlejie.core.util.FileUtil;
import com.littlejie.demo.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar_bar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {
        String[] files = DeviceUtil.getStoragePath();
//        List<String> sets = DeviceUtil.getExternalMounts();
        for (String path : files) {
            File file = new File(path);
            Log.d(TAG, "device files  = " + file.list());
            Log.d(TAG, "device storage = " + file.getTotalSpace());
        }
        Log.d(TAG, "mime type = " + FileUtil.getMimeType("/storage/emulated/0/DCIM/100MEDIA/IMAG0922.jpg"));
    }

    private void initToolbar() {

    }

}
