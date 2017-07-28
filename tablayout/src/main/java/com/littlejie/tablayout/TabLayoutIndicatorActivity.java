package com.littlejie.tablayout;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.littlejie.core.base.BaseActivity;

import butterknife.BindView;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */

public class TabLayoutIndicatorActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_tablayout_indicator;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        CustomTab tab1 = new CustomTab(this, mTabLayout);
        tab1.setIcon(R.drawable.btn_angry_normal);
        tab1.setTab("白带");
        tab1.addToTabLayout();

        CustomTab tab2 = new CustomTab(this, mTabLayout);
        tab2.setTab("爱爱");
        tab2.addToTabLayout();

        CustomTab tab3 = new CustomTab(this, mTabLayout);
        tab3.setTab("排卵试纸");
        tab3.addToTabLayout();

        CustomTab tab4 = new CustomTab(this, mTabLayout);
        tab4.setTab("排卵日");
        tab4.addToTabLayout();

        CustomTab tab5 = new CustomTab(this, mTabLayout);
        tab5.setTab("点滴出血");
        tab5.addToTabLayout();
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }
}
