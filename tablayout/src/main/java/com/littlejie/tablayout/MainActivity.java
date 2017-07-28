package com.littlejie.tablayout;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.manager.ActivityManager;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_main;
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

    @OnClick(R.id.btn_tablayout)
    void clickTabLayout() {
        ActivityManager.startActivity(this, TabLayoutIndicatorActivity.class);
    }

    @OnClick(R.id.btn_viewpager)
    void clickViewPager() {
        ActivityManager.startActivity(this, ViewPagerIndicatorActivity.class);
    }

    @Override
    protected void process() {

    }

}
