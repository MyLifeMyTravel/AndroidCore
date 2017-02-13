package com.littlejie.demo.modules.base.media;

import android.util.Log;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.utils.Constant;

import java.io.File;

public class FileActionActivity extends BaseActivity {

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_file_action;
    }

    @Override
    protected void initData() {
        File src = new File(Constant.ROOT + "/nmdsdcid");
        boolean result = src.renameTo(new File(Constant.ROOT + "/tt/1"));
        Log.d(TAG, "initData: result = " + result);
    }

    @Override
    protected void initView() {
        File src = new File(Constant.ROOT + "/tt");
        boolean result = src.renameTo(new File(Constant.ROOT + "/ttt/tt"));
        Log.d(TAG, "initView: result = " + result);
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }
}
