package cn.bongmi.uidemo.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bongmi.uidemo.BindStep;
import cn.bongmi.uidemo.R;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */

public class BindActionFragment extends BaseBindFragment {

    private static final String TAG = BindActionFragment.class.getSimpleName();
    private static final String ALPHA = "alpha";
    private static final String TRANSLATION_X = "translationX";
    private static final String TRANSLATION_Y = "translationY";

    @BindView(R.id.iv_device_body)
    ImageView ivDeviceBody;
    @BindView(R.id.iv_device_cover)
    ImageView ivDeviceCover;
    @BindView(R.id.iv_hand1)
    ImageView ivHand1;
    @BindView(R.id.iv_hand2)
    ImageView ivHand2;
    @BindView(R.id.iv_bind_bluetooth)
    ImageView ivBlueTooth;
    @BindView(R.id.iv_phone)
    ImageView ivPhone;

    //体温计水平位移
    private float offsetX;
    //体温计盖及手的垂直位移，单位px
    private float offsetY = 200;
    private float offsetPhoneX;
    private int screenWidth;

    private AnimatorSet step1 = new AnimatorSet();
    private AnimatorSet step1Alpha = new AnimatorSet();
    private AnimatorSet step2 = new AnimatorSet();
    private AnimatorSet step3 = new AnimatorSet();
    private ObjectAnimator step4;
    private AnimatorSet step5 = new AnimatorSet();

    public static BindActionFragment newInstance() {

        Bundle args = new Bundle();

        BindActionFragment fragment = new BindActionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bind_action,
                container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().getDecorView().getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                //体温计水平位移距离
                                screenWidth = 1080;
                                offsetX = screenWidth / 2
                                        - ivDeviceCover.getWidth() / 2 - ivDeviceCover.getX();
                                offsetPhoneX = screenWidth - ivPhone.getX();
                                getActivity().getWindow().getDecorView()
                                        .getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                afterMeasure();
                                animStep1();
                            }
                        });
    }

    private void afterMeasure() {
        ivDeviceBody.setTranslationX(offsetX);
        ivDeviceCover.setTranslationX(offsetX);
        ivPhone.setTranslationX(offsetPhoneX);
    }


    /**
     * 体温计居中向右平移并且显示手.
     */
    private void animStep1() {
        ObjectAnimator body =
                ObjectAnimator.ofFloat(ivDeviceBody, TRANSLATION_X, offsetX, 0);
        ObjectAnimator cover =
                ObjectAnimator.ofFloat(ivDeviceCover, TRANSLATION_X, offsetX, 0);
        step1.play(body).with(cover);
        step1.setStartDelay(500);
        step1.setDuration(100);
        step1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animStep1Alpha();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        step1.start();
    }

    private void animStep1Alpha() {
        ivHand1.setVisibility(View.VISIBLE);
        ivHand2.setVisibility(View.VISIBLE);
        ObjectAnimator hand1 = ObjectAnimator.ofFloat(ivHand1, ALPHA, 0, 1);
        ObjectAnimator hand2 = ObjectAnimator.ofFloat(ivHand2, ALPHA, 0, 1);
        step1Alpha.play(hand1).with(hand2);
        step1Alpha.setDuration(300);
        step1Alpha.start();
        step1Alpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animStep2();
                animStep2Alpha();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 体温计盖子及手向上平移，并且逐渐隐藏.
     */
    private void animStep2() {
        ObjectAnimator hand1 =
                ObjectAnimator.ofFloat(ivHand1, TRANSLATION_Y, 0, -offsetY);
        ObjectAnimator hand2 =
                ObjectAnimator.ofFloat(ivHand2, TRANSLATION_Y, 0, -offsetY);
        ObjectAnimator cover =
                ObjectAnimator.ofFloat(ivDeviceCover, TRANSLATION_Y, 0, -offsetY);
        step2.play(hand1).with(hand2).with(cover);
        step2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animStep3();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        step2.setDuration(600);
        step2.start();
    }

    private void animStep2Alpha() {
        ObjectAnimator hand1 = ObjectAnimator.ofFloat(ivHand1, ALPHA, 1, 0);
        ObjectAnimator hand2 = ObjectAnimator.ofFloat(ivHand2, ALPHA, 1, 0);
        ObjectAnimator cover = ObjectAnimator.ofFloat(ivDeviceCover, ALPHA, 1, 0);
        AnimatorSet set = new AnimatorSet();
        set.play(hand1).with(hand2).with(cover);
        set.setDuration(300);
        //延迟300ms播放
        set.setStartDelay(300);
        set.start();
    }

    /**
     * 将体温计主题移至左侧.
     */
    private void animStep3() {
        float translationX = (ivDeviceBody.getX() + ivDeviceBody.getWidth() / 2)
                - (ivBlueTooth.getX() + ivBlueTooth.getWidth() / 2);
        float translationY = ivDeviceBody.getY() -
                (ivBlueTooth.getY() + ivBlueTooth.getHeight() / 2);
        ObjectAnimator translateBodyX =
                ObjectAnimator.ofFloat(ivDeviceBody, TRANSLATION_X, 0, -translationX);
        ObjectAnimator translateBodyY =
                ObjectAnimator.ofFloat(ivDeviceBody, TRANSLATION_Y, 0, -translationY);
        ObjectAnimator scaleBodyX =
                ObjectAnimator.ofFloat(ivDeviceBody, "scaleX", 1.0f, 0.8f);
        ObjectAnimator scaleBodyY =
                ObjectAnimator.ofFloat(ivDeviceBody, "scaleY", 1.0f, 0.8f);
        step3.play(translateBodyX)
                .with(translateBodyY)
                .with(scaleBodyX)
                .with(scaleBodyY);
        step3.setDuration(400);
        step3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animStep4();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        step3.setInterpolator(new AnticipateInterpolator());
        step3.start();
    }

    /**
     * 从右至左展示持手机的手.
     */
    private void animStep4() {
        ivPhone.setVisibility(View.VISIBLE);
        step4 = ObjectAnimator.ofFloat(ivPhone, TRANSLATION_X, offsetPhoneX, 0);
        step4.setDuration(600);
        step4.setStartDelay(300);
        step4.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animStep5();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        step4.start();
    }

    /**
     * 蓝牙图标渐隐渐现.
     */
    private void animStep5() {
        ivBlueTooth.setVisibility(View.VISIBLE);
        // 不要重用动画，否则会报如下错误
        // java.lang.IllegalStateException:
        // Circular dependencies cannot exist in AnimatorSet
        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(ivBlueTooth, ALPHA, 0, 1);
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(ivBlueTooth, ALPHA, 1, 0);
        ObjectAnimator alpha3 = ObjectAnimator.ofFloat(ivBlueTooth, ALPHA, 0, 1);
        alpha1.setDuration(1000);
        alpha2.setDuration(1000);
        alpha3.setDuration(1000);
        step5.playSequentially(alpha1, alpha2, alpha3);
        step5.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onStepFinishListener != null) {
                    onStepFinishListener.onStepFinish(BindStep.BIND_ACTION, true);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        step5.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
        onStepFinishListener = null;
        step1.cancel();
        step1Alpha.cancel();
        step2.cancel();
        step3.cancel();
        step4.cancel();
        step5.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
