package com.littlejie.demo.modules.base.ui;

import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.ui.SearchDeviceImageView;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/11/2.
 */
@Description(description = "Lottie 动画")
public class LottieActivity extends BaseActivity {

//    @BindView(R.id.iv_search_ble)
//    LottieAnimationView lottieAnimationView;

//    @BindView(R.id.iv_search_ble)
//    SearchDeviceImageView ivSearch;

    private Handler handler = new Handler();

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_lottie;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
//        lottieAnimationView.playAnimation();
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //延迟动画，不然会出现重叠，原因未知
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ivSearch.start();
//            }
//        }, 100);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        ivSearch.stop();
    }
}
