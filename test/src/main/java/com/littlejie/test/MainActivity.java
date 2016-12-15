package com.littlejie.test;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.littlejie.core.base.Core;
import com.littlejie.core.util.ClipboardUtil;

public class MainActivity extends AppCompatActivity implements ClipboardUtil.OnPrimaryClipChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Core.getClipboardManager().addOnPrimaryClipChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Core.getClipboardManager().removeOnPrimaryClipChangedListener(this);
    }

    @Override
    public void onPrimaryClipChanged(ClipboardManager clipboardManager) {
        ClipData data = clipboardManager.getPrimaryClip();
        if (data != null) {
            ClipData.Item item = data.getItemAt(0);
            String mimeType = data.getDescription().getMimeType(0);
            Log.d(TAG, mimeType);
//            String htmlText = item.getHtmlText();
            CharSequence text = item.getText();
//            if (!TextUtils.isEmpty(htmlText)) {
//                Log.d(TAG, htmlText);
//            }
            if (!TextUtils.isEmpty(text)) {
                Log.d(TAG, text.toString());
            }
        }
    }
}
