package com.littlejie.filemanager;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.base.Core;
import com.littlejie.core.manager.TintManager;
import com.littlejie.core.util.MediaUtil;
import com.littlejie.filemanager.manager.AppCommand;
import com.littlejie.filemanager.modules.fragment.StorageFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar_bar)
    Toolbar mToolbar;
    @BindView(R.id.design_navigation_view)
    NavigationView mNavigationView;

    ActionBarDrawerToggle mDrawerToggle;

    private Fragment mCurrentFragment;
    private Map<Integer, Fragment> mFragmentMap = new HashMap<>();

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setContentInsetStartWithNavigation(0);
        mToolbar.setNavigationIcon(TintManager.tintDrawable(this, R.mipmap.ic_menu_black_24dp, Color.WHITE));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void initViewListener() {
        //导航菜单点击事件
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switchItem(item.getItemId(), item.getTitle());
                item.setChecked(true);
                return true;
            }
        });
    }

    private boolean switchItem(int menuID, int title) {
        return switchItem(menuID, Core.getString(title));
    }

    private boolean switchItem(int menuID, CharSequence title) {
        invalidateOptionsMenu();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = mFragmentMap.get(menuID);

        if (fragment == null) {
            switch (menuID) {
                case R.id.menu_device:
                    fragment = StorageFragment.newInstance();
                    break;
                case R.id.menu_image:
                    fragment = StorageFragment.newInstance(MediaUtil.getImageFolder(this));
                    break;
                case R.id.menu_music:
                    fragment = StorageFragment.newInstance(MediaUtil.getAudioFiles(this));
                    break;
                case R.id.menu_video:
                    fragment = StorageFragment.newInstance(MediaUtil.getVideoFiles(this));
                    break;
                case R.id.menu_setting:

                    break;
                case R.id.menu_feedback:

                    break;
                case R.id.menu_about:

                    break;
            }
            mFragmentMap.put(menuID, fragment);
        }
        if (fragment == null) {
            return false;
        }
        if (mCurrentFragment == null) {
            ft.add(R.id.content_frame, fragment).commit();
        } else {
            if (fragment.isAdded()) {
                ft.hide(mCurrentFragment).show(fragment).commit();
            } else {
                ft.add(R.id.content_frame, fragment).hide(mCurrentFragment).commit();
            }
        }
        mCurrentFragment = fragment;
        mToolbar.setTitle(title);
        return true;
    }

    @Override
    protected void process() {
        switchItem(R.id.menu_device, R.string.nav_menu_device);
    }

    @Override
    public void onBackPressed() {
        if (!AppCommand.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
