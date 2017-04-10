package com.littlejie.demo.modules.base.media;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.utils.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class FileActionActivity extends BaseActivity {

    public static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_file_action;
    }

    @Override
    protected void initData() {
//        File src = new File(Constant.ROOT + "/nmdsdcid");
//        boolean result = src.renameTo(new File(Constant.ROOT + "/tt/1"));
//        Log.d(TAG, "initData: result = " + result);
    }

    @Override
    protected void initView() {
//        File src = new File(Constant.ROOT + "/tt");
//        boolean result = src.renameTo(new File(Constant.ROOT + "/ttt/tt"));
//        Log.d(TAG, "initView: result = " + result);
    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.btn_create_file)
    void createFile() {
        File src = new File(Constant.ROOT + "/测试");
        if (!src.exists()) {
            boolean mkdirs = src.mkdirs();
            Log.d(TAG, "mkdirs = " + mkdirs);
        }
    }

    @Override
    protected void process() {
        String[] permissions = getNeedGrantPermission(PERMISSIONS);
        if (permissions != null && permissions.length > 0) {
            ActivityCompat.requestPermissions(this, permissions, 0);
        }
    }

    private String[] getNeedGrantPermission(String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            return null;
        }
        List<String> lstPermission = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                lstPermission.add(permission);
            }
        }
        String[] needGrantPermissions = new String[lstPermission.size()];
        return lstPermission.toArray(needGrantPermissions);
    }
}
