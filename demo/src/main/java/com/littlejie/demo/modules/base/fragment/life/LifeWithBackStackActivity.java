package com.littlejie.demo.modules.base.fragment.life;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 如果 Fragment 通过 addToBackStack() 加入回退栈
 * 1. 进行 replace() 时不会进行 onDestroy() 和 onDetach()，反之则会
 * 2. 进行 popBackStack() 时不会再次调用 onAttach() 和 onCreate()
 * 3. add() 和 replace() 方法对 Fragment 的生命周期没有影响，但 add() 方法会造成 Fragment 叠加显示
 */
@Description(description = "Fragment 添加进回退栈时的生命周期")
public class LifeWithBackStackActivity extends BaseActivity {

    @BindView(R.id.tv_add_back_stack_tip)
    TextView mTvAddToBackStackTip;
    @BindView(R.id.widget_switch)
    SwitchCompat mSwitch;
    private int mCount = 1;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_life_circle_with_back_stack;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initViewListener() {
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateTip(isChecked);
            }
        });
    }

    @OnClick(R.id.btn_add_to_back_stack)
    void addToBackStack(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content_frame, LifeFragment.newInstance("Tab" + mCount++));
        if (mSwitch.isChecked()) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @OnClick(R.id.btn_replace_to_back_stack)
    void replaceToBackStack(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, LifeFragment.newInstance("Tab" + mCount++));
        if (mSwitch.isChecked()) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    protected void process() {
        updateTip(mSwitch.isChecked());
    }

    private void updateTip(boolean isChecked) {
        String format = getResources().getString(R.string.txt_add_to_back_stack);
        mTvAddToBackStackTip.setText(String.format(format, String.valueOf(isChecked)));
    }
}
