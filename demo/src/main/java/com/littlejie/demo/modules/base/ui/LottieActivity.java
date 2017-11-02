package com.littlejie.demo.modules.base.ui;

import com.airbnb.lottie.LottieAnimationView;
import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/11/2.
 */
@Description(description = "Lottie 动画")
public class LottieActivity extends BaseActivity {

    @BindView(R.id.iv_search_ble)
    LottieAnimationView lottieAnimationView;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_lottie;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        lottieAnimationView.playAnimation();
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }
}
