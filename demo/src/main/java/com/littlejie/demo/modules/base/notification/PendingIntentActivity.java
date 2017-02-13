package com.littlejie.demo.modules.base.notification;

import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.modules.DemoApplication;
import com.littlejie.demo.utils.Constant;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/2/12.
 */

public class PendingIntentActivity extends BaseActivity {

    @BindView(R.id.tv_content)
    TextView mTvContent;

    private String mName;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_pending_intent;
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            mName = getIntent().getStringExtra(Constant.EXTRA_NAME);
            DemoApplication.getNotificationManager().cancel(Constant.NOTIFICATION_CUSTOM);
        }
    }

    @Override
    protected void initView() {
        mTvContent.setText(mName);
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }
}
