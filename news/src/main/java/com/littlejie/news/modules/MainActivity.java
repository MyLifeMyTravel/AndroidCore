package com.littlejie.news.modules;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.news.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.tv)
    TextView mTv;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {
        mTv.setText("ebfe");
    }
}
