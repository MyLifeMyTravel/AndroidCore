package com.littlejie.demo.modules.base.fragment.life;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.OnClick;

/**
 * 简单测试通过 hide() 、 show() 方法时 Fragment 的回调。
 * 为排除干扰，在 initData() 时把需要的 Fragment 先创建完成。
 * 通过 hide() 、 show() 可以发现，此时 Fragment 只改变了可见性，并不涉及生命周期的改变
 */
@Description(description = "Fragment hide()、show()时的生命周期")
public class LifeWithHideShowActivity extends BaseActivity {

    private LifeFragment mFragment1, mFragment2;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_life_circle_with_hide_show;
    }

    @Override
    protected void initData() {
        mFragment1 = LifeFragment.newInstance("Tab1");
        mFragment2 = LifeFragment.newInstance("Tab2");
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initViewListener() {
    }

    @OnClick(R.id.btn_show_one_hide_two)
    void showOneHideTwo() {
        getSupportFragmentManager().beginTransaction().show(mFragment1).hide(mFragment2).commit();
    }

    @OnClick(R.id.btn_show_two_hide_one)
    void showTwoHideOne() {
        getSupportFragmentManager().beginTransaction().show(mFragment2).hide(mFragment1).commit();
    }

    @Override
    protected void process() {
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, mFragment1).hide(mFragment1)
                .add(R.id.content_frame, mFragment2).hide(mFragment2)
                .commit();
    }

}
