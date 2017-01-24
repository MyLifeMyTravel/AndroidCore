package com.littlejie.demo.modules.base.system;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.FileUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import java.util.ArrayList;

import butterknife.BindView;

@Description(description = "处理其它 APP 的分享 Intent")
public class ShareIntentActivity extends BaseActivity {

    @BindView(R.id.tv_content)
    TextView mTvContent;

    private StringBuilder mStringBuilder = new StringBuilder();

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_receive_share_intent;
    }

    @Override
    protected void initData() {
        //由于将Activity的launchMode设置成singleTask，故此处代码最好放在onResume()中处理
        if (getIntent() == null) {
            return;
        }
        String action = getIntent().getAction();
        String type = getIntent().getType();
        mStringBuilder.append("action = ").append(action)
                .append(";type = ").append(type).append("\n");
        if (Intent.ACTION_SEND.equals(action)) {
            //纯文本的mimeType规定为 text/plain ，所以先处理掉
            if ("text/plain".equals(type)) {
                mStringBuilder.append("text = ").append(getIntent().getStringExtra(Intent.EXTRA_TEXT));
            } else {
                Uri stream = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
                String path = FileUtil.getPathFromUri(this, stream);
                mStringBuilder.append("stream = ").append(stream.toString())
                        .append("; path = ").append(path).append("\n");
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {
            ArrayList<Uri> streams = getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            for (Uri stream : streams) {
                String path = FileUtil.getPathFromUri(this, stream);
                mStringBuilder.append("stream = ").append(stream.toString())
                        .append("; path = ").append(path).append("\n");
            }
        }
        Log.d(TAG, "initData: " + mStringBuilder.toString());
    }

    @Override
    protected void initView() {
        mTvContent.setText(mStringBuilder.toString());
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }

}
