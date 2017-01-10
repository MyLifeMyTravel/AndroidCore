package com.littlejie.demo.modules.base.fragment.lifecircle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.modules.base.fragment.LifeCircleFragment;

/**
 * 如果 Fragment 通过 addToBackStack() 加入回退栈
 * 1. 进行 replace() 时不会进行 onDestroy() 和 onDetach()，反之则会
 * 2. 进行 popBackStack() 时不会再次调用 onAttach() 和 onCreate()
 * 3. add() 和 replace() 方法对 Fragment 的生命周期没有影响，但 add() 方法会造成 Fragment 叠加显示
 */
public class LifeCircleWithBackStackActivity extends BaseActivity {

    private TextView mTvAddToBackStackTip;
    private SwitchCompat mSwitch;
    private Button mBtnAddToBackStack,mBtnReplcaeToBackStack;
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
        mTvAddToBackStackTip = (TextView) findViewById(R.id.tv_add_back_stack_tip);
        mSwitch = (SwitchCompat) findViewById(R.id.widget_switch);
        mBtnAddToBackStack = (Button) findViewById(R.id.btn_add_to_back_stack);
        mBtnReplcaeToBackStack = (Button) findViewById(R.id.btn_replace_to_back_stack);
    }

    @Override
    protected void initViewListener() {
        mBtnAddToBackStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, LifeCircleFragment.newInstance("Tab" + mCount++));
                if (mSwitch.isChecked()) {
                    transaction.addToBackStack(null);
                }
                transaction.commit();
            }
        });
        mBtnReplcaeToBackStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, LifeCircleFragment.newInstance("Tab" + mCount++));
                if (mSwitch.isChecked()) {
                    transaction.addToBackStack(null);
                }
                transaction.commit();
            }
        });
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateTip(isChecked);
            }
        });
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
