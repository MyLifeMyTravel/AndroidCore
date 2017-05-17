package cn.bongmi.uidemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;

import com.littlejie.core.base.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

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

    @Override
    protected int getPageLayoutID() {
        return R.layout.bind_action;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //体温计水平位移距离
                offsetX = getScreenWidth() / 2 - ivDeviceCover.getWidth() / 2 - ivDeviceCover.getX();
                offsetPhoneX = getScreenWidth() - ivPhone.getX();
                getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
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

    @Override
    protected void initViewListener() {
    }

    @Override
    protected void process() {

    }

    /**
     * 体温计居中向右平移并且显示手.
     */
    private void animStep1() {
        Log.d(TAG, "bodyX = " + ivDeviceBody.getX() + ";coverX = " + ivDeviceCover.getX());
        ObjectAnimator body = ObjectAnimator.ofFloat(ivDeviceBody, TRANSLATION_X, offsetX, 0);
        ObjectAnimator cover = ObjectAnimator.ofFloat(ivDeviceCover, TRANSLATION_X, offsetX, 0);
        AnimatorSet set = new AnimatorSet();
        set.play(body).with(cover);
        set.setDuration(100);
        set.setStartDelay(500);
        set.addListener(new Animator.AnimatorListener() {
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
        set.start();
    }

    private void animStep1Alpha() {
        ivHand1.setVisibility(View.VISIBLE);
        ivHand2.setVisibility(View.VISIBLE);
        ObjectAnimator hand1 = ObjectAnimator.ofFloat(ivHand1, ALPHA, 0, 1);
        ObjectAnimator hand2 = ObjectAnimator.ofFloat(ivHand2, ALPHA, 0, 1);
        AnimatorSet set = new AnimatorSet();
        set.play(hand1).with(hand2);
        set.setDuration(300);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
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
        ObjectAnimator hand1 = ObjectAnimator.ofFloat(ivHand1, TRANSLATION_Y, 0, -offsetY);
        ObjectAnimator hand2 = ObjectAnimator.ofFloat(ivHand2, TRANSLATION_Y, 0, -offsetY);
        ObjectAnimator cover = ObjectAnimator.ofFloat(ivDeviceCover, TRANSLATION_Y, 0, -offsetY);
        AnimatorSet set = new AnimatorSet();
        set.play(hand1).with(hand2).with(cover);
        set.addListener(new Animator.AnimatorListener() {
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
        set.setDuration(600);
        set.start();
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
     * 将体温计主题移至左侧
     */
    private void animStep3() {
        float translationX = (ivDeviceBody.getX() + ivDeviceBody.getWidth() / 2)
                - (ivBlueTooth.getX() + ivBlueTooth.getWidth() / 2);
        float translationY = ivDeviceBody.getY() -
                (ivBlueTooth.getY() + ivBlueTooth.getHeight() / 2);
        ObjectAnimator translateBodyX = ObjectAnimator.ofFloat(ivDeviceBody, TRANSLATION_X, 0, -translationX);
        ObjectAnimator translateBodyY = ObjectAnimator.ofFloat(ivDeviceBody, TRANSLATION_Y, 0, -translationY);
        ObjectAnimator scaleBodyX = ObjectAnimator.ofFloat(ivDeviceBody, "scaleX", 1.0f, 0.8f);
        ObjectAnimator scaleBodyY = ObjectAnimator.ofFloat(ivDeviceBody, "scaleY", 1.0f, 0.8f);
        AnimatorSet set = new AnimatorSet();
        set.play(translateBodyX).with(translateBodyY).with(scaleBodyX).with(scaleBodyY);
        set.setDuration(400);
        set.addListener(new Animator.AnimatorListener() {
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
        set.setInterpolator(new AnticipateInterpolator());
        set.start();
    }

    /**
     * 从右至左展示持手机的手
     */
    private void animStep4() {
        ivPhone.setVisibility(View.VISIBLE);
        ObjectAnimator translationPhone = ObjectAnimator.ofFloat(ivPhone, TRANSLATION_X, offsetPhoneX, 0);
        translationPhone.setDuration(600);
        translationPhone.setStartDelay(300);
        translationPhone.addListener(new Animator.AnimatorListener() {
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
        translationPhone.start();
    }

    /**
     * 蓝牙图标渐隐渐现
     */
    private void animStep5() {
        ivBlueTooth.setVisibility(View.VISIBLE);
        //不要重用动画，否则会报如下错误
        //java.lang.IllegalStateException: Circular dependencies cannot exist in AnimatorSet
        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(ivBlueTooth, ALPHA, 0, 1);
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(ivBlueTooth, ALPHA, 1, 0);
        ObjectAnimator alpha3 = ObjectAnimator.ofFloat(ivBlueTooth, ALPHA, 0, 1);
        alpha1.setDuration(1000);
        alpha2.setDuration(1000);
        alpha3.setDuration(1000);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(alpha1, alpha2, alpha3);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    private int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
