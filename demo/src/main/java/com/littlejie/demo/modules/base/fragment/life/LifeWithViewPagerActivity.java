package com.littlejie.demo.modules.base.fragment.life;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.adapter.TabAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 测试 Fragment 与 ViewPager 结合使用时的生命周期。
 * ViewPager 的加载机制会导致 Fragment 的销毁(onDestroy)与重新创建(onCreateView)
 */
@Description(description = "与 ViewPager 使用时的生命周期")
public class LifeWithViewPagerActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    TabAdapter mAdapter;

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
            mFragments.add(LifeFragment.newInstance(mTitles[i]));
        }
    }

    @Override
    protected void initView() {
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
