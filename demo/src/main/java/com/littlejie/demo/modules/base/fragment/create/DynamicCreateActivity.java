package com.littlejie.demo.modules.base.fragment.create;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.OnClick;

@Description(description = "动态添加 Fragment")
public class DynamicCreateActivity extends BaseActivity {

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_dynamic_create;
    }

    @Override
    protected void initData() {}

    @Override
    protected void initView() {}

    @Override
    protected void initViewListener() {}

    /*-------------------------*/
    @Override
    protected void process() {}

    @OnClick(R.id.btn_show_fragment1)
    public void showFragment1() {
        showFragment(TestFragment.newInstance("This is Fragment1", Color.YELLOW));
    }

    @OnClick(R.id.btn_show_fragment2)
    public void showFragment2() {
        showFragment(TestFragment.newInstance("This is Fragment2", Color.CYAN));
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
