package com.littlejie.demo.modules.base.ui;

import android.graphics.PorterDuff;
import android.os.Handler;
import android.widget.ProgressBar;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */
@Description(description = "进度条")
public class ProgressBarActivity extends BaseActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private Handler handler = new Handler();

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_progressbar;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.primary_color),
                PorterDuff.Mode.MULTIPLY);
    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.btn_show_progress)
    void showProgressBar() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("哈哈哈");
        dialog.show();
    }

    @Override
    protected void process() {

    }
}
