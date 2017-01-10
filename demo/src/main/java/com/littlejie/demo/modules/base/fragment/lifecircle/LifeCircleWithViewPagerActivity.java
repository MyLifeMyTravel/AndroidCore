package com.littlejie.demo.modules.base.fragment.lifecircle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.modules.base.fragment.LifeCircleFragment;
import com.littlejie.demo.modules.adapter.TabAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测试 Fragment 与 ViewPager 结合使用时的生命周期。
 * ViewPager 的加载机制会导致 Fragment 的销毁(onDestroy)与重新创建(onCreateView)
 */
public class LifeCircleWithViewPagerActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TabAdapter mAdapter;

    private List<Fragment> mFragments;
    private String[] mTitles = new String[]{"Tab1", "Tab2", "Tab3", "Tab4", "Tab5"};

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_life_circle_with_viewpager;
    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        int size = mTitles.length;
        for (int i = 0; i < size; i++) {
            mFragments.add(LifeCircleFragment.newInstance(mTitles[i]));
        }
    }

    @Override
    protected void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mAdapter.setData(mFragments, Arrays.asList(mTitles));
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }
}
