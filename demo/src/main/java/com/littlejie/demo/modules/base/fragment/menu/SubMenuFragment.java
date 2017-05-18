package com.littlejie.demo.modules.base.fragment.menu;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.littlejie.core.base.BaseFragment;
import com.littlejie.demo.R;
import com.littlejie.demo.ui.adapter.TabAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/1/23.
 */

public class SubMenuFragment extends BaseFragment {

    private static final String[] TITLES = new String[]{"submenu1", "submenu2"};
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private TabAdapter mAdapter;

    private List<Fragment> mLstMenuFragment;

    public static SubMenuFragment newInstance() {

        Bundle args = new Bundle();

        SubMenuFragment fragment = new SubMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: SubMenuFragment; isVisibleToUser = " + isVisibleToUser);
    }

    @Override
    protected int getPageLayoutID() {
        return R.layout.fragment_sub_menu;
    }

    @Override
    protected void initData() {
        mLstMenuFragment = new ArrayList<>();
        mLstMenuFragment.add(MenuFragment.newInstance(R.menu.fragment_sub_menu1, TITLES[0]));
        mLstMenuFragment.add(MenuFragment.newInstance(R.menu.fragment_sub_menu2, TITLES[1]));
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mAdapter = new TabAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mAdapter.setData(mLstMenuFragment, Arrays.asList(TITLES));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initViewListener() {
//        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.d(LANGUAGE, "onPageSelected: position = " + position);
//                ((Activity) getContext()).invalidateOptionsMenu();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                Log.d(LANGUAGE, "onTabSelected: position = " + tab.getPosition());
//                ((Activity) getContext()).invalidateOptionsMenu();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    @Override
    protected void process(Bundle savedInstanceState) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(TAG, "onCreateOptionsMenu: SubMenuFragment,current item = " + mViewPager.getCurrentItem());
//        if (mViewPager.getCurrentItem() == 0) {
//            inflater.inflate(R.menu.fragment_sub_menu1, menu);
//        } else {
//            inflater.inflate(R.menu.fragment_sub_menu2, menu);
//        }
    }

}
