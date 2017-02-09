package com.littlejie.demo.modules.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.littlejie.core.base.BaseFragment;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.AnnotationUtil;
import com.littlejie.demo.modules.adapter.TabAdapter;
import com.littlejie.demo.modules.base.component.ComponentDemoFragment;
import com.littlejie.demo.modules.base.fragment.DemoFragment;
import com.littlejie.demo.modules.base.media.MediaDemoFragment;
import com.littlejie.demo.modules.base.notification.NotificationListFragment;
import com.littlejie.demo.modules.base.system.SystemDemoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/1/7.
 */

public class AndroidBaseFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private TabAdapter mAdapter;
    private List<Fragment> mFragmentList;

    public static AndroidBaseFragment newInstance() {
        Bundle args = new Bundle();

        AndroidBaseFragment fragment = new AndroidBaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getPageLayoutID() {
        return R.layout.fragment_android_base;
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
        mFragmentList.add(DemoFragment.newInstance());
        mFragmentList.add(ComponentDemoFragment.newInstance());
        mFragmentList.add(NotificationListFragment.newInstance());
        mFragmentList.add(MediaDemoFragment.newInstance());
        mFragmentList.add(SystemDemoFragment.newInstance());
        mAdapter.setData(mFragmentList, getTabTitles());
    }

    /**
     * 获取注解的标题
     *
     * @return
     */
    private List<String> getTabTitles() {
        List<String> lstTitle = new ArrayList<>();
        for (Fragment fragment : mFragmentList) {
            lstTitle.add(AnnotationUtil.getTitle(fragment.getClass()));
        }
        return lstTitle;
    }
}
