package com.littlejie.demo.modules.base.fragment.life;

import android.view.View;
import android.widget.Button;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;

/**
 * 简单测试通过 hide() 、 show() 方法时 Fragment 的回调。
 * 为排除干扰，在 initData() 时把需要的 Fragment 先创建完成。
 * 通过 hide() 、 show() 可以发现，此时 Fragment 只改变了可见性，并不涉及生命周期的改变
 */
public class LifeWithHideShowActivity extends BaseActivity implements View.OnClickListener {

    private LifeFragment mFragment1, mFragment2;
    private Button mBtnShowOneHideTwo, mBtnShowTwoHideOne;

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
        mBtnShowOneHideTwo = (Button) findViewById(R.id.btn_show_one_hide_two);
        mBtnShowTwoHideOne = (Button) findViewById(R.id.btn_show_two_hide_one);
    }

    @Override
    protected void initViewListener() {
        mBtnShowOneHideTwo.setOnClickListener(this);
        mBtnShowTwoHideOne.setOnClickListener(this);
    }

    @Override
    protected void process() {
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, mFragment1).hide(mFragment1)
                .add(R.id.content_frame, mFragment2).hide(mFragment2)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_one_hide_two:
                getSupportFragmentManager().beginTransaction().show(mFragment1).hide(mFragment2).commit();
                break;
            case R.id.btn_show_two_hide_one:
                getSupportFragmentManager().beginTransaction().show(mFragment2).hide(mFragment1).commit();
                break;
        }
    }
}
