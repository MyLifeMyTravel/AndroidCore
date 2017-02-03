package com.littlejie.demo.modules.base.fragment.menu;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.adapter.TabAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

@Description(description = "Fragment 创建菜单")
public class FragmentMenuActivity extends BaseActivity {

    private static final String[] TITLES = new String[]{"menu1", "menu2", "menu3", "menu4", "menu5"};
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private TabAdapter mAdapter;

    private List<Fragment> mLstMenuFragment;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_fragment_menu;
    }

    @Override
    protected void initData() {
        mLstMenuFragment = new ArrayList<>();
        mLstMenuFragment.add(MenuFragment.newInstance(R.menu.fragment_menu1, TITLES[0]));
        mLstMenuFragment.add(SubMenuFragment.newInstance());
        mLstMenuFragment.add(MenuFragment.newInstance(R.menu.fragment_menu3, TITLES[2]));
        mLstMenuFragment.add(MenuFragment.newInstance(R.menu.fragment_menu4, TITLES[3]));
        mLstMenuFragment.add(MenuFragment.newInstance(R.menu.fragment_menu5, TITLES[4]));
    }

    @Override
    protected void initView() {
        mAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mAdapter.setData(mLstMenuFragment, Arrays.asList(TITLES));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: FragmentMenuActivity called.");
        return super.onCreateOptionsMenu(menu);
    }
}
