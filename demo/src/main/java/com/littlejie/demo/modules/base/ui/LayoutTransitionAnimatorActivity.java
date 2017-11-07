package com.littlejie.demo.modules.base.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.DisplayUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by littlejie on 2017/11/3.
 */
@Description(description = "LayoutTransitionAnimator")
public class LayoutTransitionAnimatorActivity extends BaseActivity {

    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_remove)
    Button btnRemove;
    @BindView(R.id.ll_container)
    LinearLayout groupContainer;

    private LayoutTransition layoutTransition;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_layout_transition_animator;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
//        layoutTransition = new LayoutTransition();
//        groupContainer.setLayoutTransition(layoutTransition);
//        setTransition();
//        LayoutTransition transition = new LayoutTransition();
//        transition.enableTransitionType(LayoutTransition.CHANGING);
//        groupContainer.setLayoutTransition(transition);

        AnimationSet set = new AnimationSet(true);

        //创建透明度渐变动画
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(200);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        animation.setDuration(200);
        set.addAnimation(animation);

        //创建LayoutAnimationController对象并设置子视图动画效果持续时间
        LayoutAnimationController controller = new LayoutAnimationController(set, 1000);

        groupContainer.setLayoutAnimation(controller);
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }

    @OnClick(R.id.btn_add)
    void add() {
        LinearLayout linearLayout = new FractionLinearLayout(this);
        linearLayout.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        DisplayUtil.dp2px(this, 100)));
        TextView textView = new TextView(this);
        ViewGroup.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        DisplayUtil.dp2px(this, 100));
        textView.setLayoutParams(params);
        textView.setText("LayoutTransitionAnimator");
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(Color.CYAN);
        linearLayout.addView(textView);
        groupContainer.addView(linearLayout);
    }

    @OnClick(R.id.btn_remove)
    void remove() {
        groupContainer.removeViewAt(groupContainer.getChildCount() - 1);
    }

    private void setTransition() {
        /**
         * view出现时 view自身的动画效果
         */
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(null, "alpha", 0f, 1f).
                setDuration(layoutTransition.getDuration(LayoutTransition.APPEARING));
        layoutTransition.setAnimator(LayoutTransition.APPEARING, animator1);

        /**
         * view 消失时，view自身的动画效果
         */
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(null, "rotation", 0f, 90f, 0f).
                setDuration(layoutTransition.getDuration(LayoutTransition.DISAPPEARING));
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, animator2);

        /**
         * view 动画改变时，布局中的每个子view动画的时间间隔
         */
        layoutTransition.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
        layoutTransition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);
    }
}
