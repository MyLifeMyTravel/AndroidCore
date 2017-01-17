package com.littlejie.demo.modules;

import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.SignalStrengthUtil;
import com.littlejie.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar_bar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        SignalStrengthUtil.init(this);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {
        //99 0 -120 -160 -120 -1 -1 19 -105 -10 74 2147483647 2147483647
        SignalStrengthUtil.getRawSignalStrength();
    }

    private void initToolbar() {

    }

}
