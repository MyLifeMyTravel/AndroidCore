package com.littlejie.demo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.littlejie.core.base.BaseFragment;
import com.littlejie.demo.annotation.AnnotationUtil;
import com.littlejie.demo.ui.adapter.TabAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/2/21.
 */

public class BaseTabLayoutFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    protected TabAdapter mAdapter;
    protected List<Fragment> mFragmentList;

    @Override
    protected int getPageLayoutID() {
        return R.layout.fragment_base_tablayout;
    }

    @Override
    protected void initData() {
        mFragmentList = new ArrayList<>();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mAdapter = new TabAdapter(getFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process(Bundle savedInstanceState) {
    }

    /**
     * 获取注解的标题
     *
     * @return
     */
    protected List<String> getTabTitles() {
        List<String> lstTitle = new ArrayList<>();
        for (Fragment fragment : mFragmentList) {
            lstTitle.add(AnnotationUtil.getTitle(fragment.getClass()));
        }
        return lstTitle;
    }

    protected void notifyDataChanged() {
        mAdapter.setData(mFragmentList, getTabTitles());
    }
}
