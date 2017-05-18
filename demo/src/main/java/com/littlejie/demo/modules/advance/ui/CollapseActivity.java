package com.littlejie.demo.modules.advance.ui;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.BindView;
import butterknife.OnClick;

@Description(description = "折叠展示内容")
public class CollapseActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.scroll)
    ScrollView mScrollView;
    @BindView(R.id.inner)
    View mView;
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;

    private Drawable mDrawable;
    private Handler mHandler = new Handler();

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_collapse;
    }

    @Override
    protected void initData() {
//        mHandler.postDelayed(mScrollRunnable, 1000);
        mDrawable = getResources().getDrawable(R.mipmap.ic_launcher);
    }

    @Override
    protected void initView() {
        mDrawable = mIvIcon.getDrawable();
    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.iv_icon)
    void onIconClick() {
        Log.d(TAG, "onIconClick");
    }

    @Override
    protected void process() {

    }

    private Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("LANGUAGE", "scroll height = " + mScrollView.getHeight());
            int off = mView.getMeasuredHeight() - mScrollView.getHeight();//判断高度
            if (off > 0) {
                mScrollView.scrollBy(0, 30);
                if (mScrollView.getScrollY() == off) {
                    Thread.currentThread().interrupt();
                } else {
                    mHandler.postDelayed(this, 1000);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
    }
}
