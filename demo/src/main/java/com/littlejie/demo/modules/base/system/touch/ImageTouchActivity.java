package com.littlejie.demo.modules.base.system.touch;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.OnClick;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */

@Description(description = "ImageView触摸事件")
public class ImageTouchActivity extends BaseActivity {

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_image_touch;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.ll_container)
    void touchContainer() {
        ToastUtil.showDefaultToast("Container.");
    }

    @OnClick(R.id.iv_touch)
    void touchImage() {
        ToastUtil.showDefaultToast("Image Touch.");
    }

    @Override
    protected void process() {

    }
}
