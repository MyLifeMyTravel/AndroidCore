package com.littlejie.demo.modules.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.littlejie.core.base.BaseFragment;
import com.littlejie.demo.R;
import com.littlejie.demo.modules.adapter.TabAdapter;
import com.littlejie.demo.modules.base.fragment.DemoFragment;
import com.littlejie.demo.modules.base.notification.NotificationListFragment;
import com.littlejie.demo.modules.base.system.SystemDemoFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by littlejie on 2017/1/7.
 */

public class AndroidBaseFragment extends BaseFragment {

    private static final String[] TITLES = new String[]{"Fragment", "通知", "系统"};
    private TabLayout mTabLayout;
    private TabAdapter mAdapter;
    private ViewPager mViewPager;
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
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
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
        mFragmentList.add(NotificationListFragment.newInstance());
        mFragmentList.add(SystemDemoFragment.newInstance());
        mAdapter.setData(mFragmentList, Arrays.asList(TITLES));
    }
}
