package cn.bongmi.uidemo;

import com.littlejie.core.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */

public class SearchActivity extends BaseActivity {

    @BindView(R.id.iv_search)
    SearchDeviceImageView ivSearch;

    private boolean start;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_search;
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

    @Override
    protected void process() {

    }

    @OnClick(R.id.iv_search)
    void onClick() {

    }

}
