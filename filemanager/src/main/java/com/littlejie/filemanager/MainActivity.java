package com.littlejie.filemanager;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.littlejie.core.base.BaseActivity;

import java.io.File;

import butterknife.BindView;

import static android.R.attr.path;

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
//        Collection<File> files = FileUtils.listFilesAndDirs(Environment.getExternalStorageDirectory(),
//                TrueFileFilter.TRUE, null);
        File[] files = Environment.getExternalStorageDirectory().listFiles();
        for (File file :
                files) {
            Log.d(TAG, "file = " + file.getAbsolutePath());
        }
    }

    public static final Uri EXTERNAL_CONTENT_URI = MediaStore.Files.getContentUri("external");

    private boolean ensure(File file) {
//        Log.d(TAG, "file path = " + file.getAbsolutePath());
        if (file.isFile()) {
            return false;
        }
        File[] files = file.listFiles();
        if (files == null) {
            return false;
        }
        for (File s : files) {
            ensure(s);
        }
        Cursor cursor = getContentResolver().query(EXTERNAL_CONTENT_URI,
                new String[]{BaseColumns._ID}, "_data like ?",
                new String[]{file.getAbsolutePath()}, null);
        if (cursor != null) {
            return false;
        }
        if (cursor.getCount() > 1) {
            Log.d(TAG, "file : " + path);
            return true;
        }
        return false;
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
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    protected void process() {

    }

}
