package com.littlejie.demo.modules;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.manager.TintManager;
import com.littlejie.core.util.RegexUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.modules.advance.AndroidAdvanceFragment;
import com.littlejie.demo.modules.base.AndroidBaseFragment;

import butterknife.BindView;

//todo 测试各种情况下的ip
//1. wifi
//2. 手机网络
public class MainActivity extends BaseActivity {

    @BindView(R.id.activity_main)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar_bar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    private DrawerLayout.DrawerListener mDrawerToggle;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        Log.d(TAG, "initData: "+ RegexUtil.isValidName("abc"));
        Log.d(TAG, "initData: "+RegexUtil.isValidName("a:c"));
        Log.d(TAG, "initData: "+RegexUtil.isValidName("厉圣杰"));
        Log.d(TAG, "initData: "+RegexUtil.isValidName("厉圣杰.txt"));
        Log.d(TAG, "initData: "+RegexUtil.isValidName("厉圣杰。txt"));
    }

    @Override
    protected void initView() {
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
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return switchItem(item.getItemId());
            }
        });
    }

    private boolean switchItem(int id) {
        switch (id) {
            case R.id.menu_base:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, AndroidBaseFragment.newInstance())
                        .commit();
                return true;
            case R.id.menu_advanced:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, AndroidAdvanceFragment.newInstance())
                        .commit();
                return true;
            case R.id.menu_give_up:

                return true;
            case R.id.menu_setting:

                return true;
            case R.id.menu_feedback:

                return true;
            case R.id.menu_about:

                return true;
            default:
                return false;
        }
    }

    @Override
    protected void process() {
        switchItem(R.id.menu_base);
    }

}
