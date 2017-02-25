package com.littlejie.demo.modules.advance.ui;

import android.support.v4.content.ContextCompat;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.ui.CircleProgressBar;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class CustomUIActivity extends BaseActivity {

    @BindView(R.id.circle_progress_bar)
    CircleProgressBar mCircleProgressBar;

    private Random mRandom;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_custom_ui;
    }

    @Override
    protected void initData() {
        mRandom = new Random();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.circle_progress_bar)
    void click() {

        mCircleProgressBar.setValue(mRandom.nextFloat() * 10000);
    }

    @Override
    protected void process() {

    }
}
