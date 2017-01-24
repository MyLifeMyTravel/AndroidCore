package com.littlejie.demo.modules.base.fragment.create;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.littlejie.core.base.BaseFragment;
import com.littlejie.demo.R;
import com.littlejie.demo.utils.Constant;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/1/23.
 */

public class TestFragment extends BaseFragment {

    @BindView(R.id.tv_content)
    TextView mTvContent;

    private String mTitle;
    private int mBgColor;

    public static TestFragment newInstance(String title, int bgColor) {

        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        args.putString(Constant.EXTRA_TITLE, title);
        args.putInt(Constant.EXTRA_COLOR, bgColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getPageLayoutID() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initData() {
        if (getArguments() == null) {
            return;
        }
        mTitle = getArguments().getString(Constant.EXTRA_TITLE);
        mBgColor = getArguments().getInt(Constant.EXTRA_COLOR);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(mTitle)) {
            mTvContent.setText(mTitle);
        }
        mTvContent.setBackgroundColor(mBgColor);
    }

    @Override
    protected void initViewListener() {}

    @Override
    protected void process(Bundle savedInstanceState) {}
}
