package com.littlejie.filemanager;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.filemanager.modules.fragment.StorageFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar_bar)
    Toolbar mToolbar;
    @BindView(R.id.design_navigation_view)
    NavigationView mNavigationView;

    ActionBarDrawerToggle mDrawerToggle;

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
        mToolbar.setNavigationIcon(R.mipmap.ic_menu_black_24dp);
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
                switch (item.getItemId()) {
                    case R.id.menu_device:

                        break;
                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    protected void process() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame,
                        StorageFragment.newInstance(Environment.getExternalStorageDirectory().getAbsolutePath()))
                .commit();
//        Intent intent = new Intent("com.littlejie.android.demo.static.broadcast");
//        sendBroadcast(intent);
    }

}
