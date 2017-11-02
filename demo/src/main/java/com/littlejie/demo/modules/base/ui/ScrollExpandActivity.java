package com.littlejie.demo.modules.base.ui;

import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.ui.ExpandLinearLayout;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/10/26.
 */
@Description(description = "在Scroll中可展开折叠的控件")
public class ScrollExpandActivity extends BaseActivity {

    @BindView(R.id.scrollView)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.group)
    ViewGroup mViewGroup;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_scroll_expand;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        for (int i = 0; i < 100; i++) {
            if (i % 3 == 0) {
                TextView textView = new TextView(this);
                textView.setText("哈哈哈 " + i);
                textView.setOnClickListener(mOnClickListener);
                mViewGroup.addView(textView);
            } else {
                ExpandLinearLayout layout = new ExpandLinearLayout(this, "title " + i);
                layout.setOnClickListener(mOnClickListener);
                mViewGroup.addView(layout);
            }
        }
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int result = (int) v.getY() - mNestedScrollView.getScrollY();
            Log.d(TAG, "item y = " + v.getY() + ";NestedScrollView getScrollY = " + mNestedScrollView.getScrollY() + ";result = " + result);
            mNestedScrollView.smoothScrollTo(0, result);
        }
    };

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }
}
