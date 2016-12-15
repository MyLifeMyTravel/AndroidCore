package com.littlejie.core.util;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 剪贴板工具类
 * http://developer.android.com/guide/topics/text/copy-paste.html
 * Created by littlejie on 2016/4/15.
 */
public class ClipboardUtil {

    public static final String TAG = ClipboardUtil.class.getSimpleName();
    /**
     * 由于系统剪贴板在某些情况下会多次调用，但调用间隔基本不会超过5ms
     * 考虑到用户操作，将阈值设为100ms，过滤掉前几次无效回调
     */
    private static final int THRESHOLD = 100;

    private static ClipboardUtil mInstance;
    private ClipboardManager mClipboardManager;

    private Handler mHandler = new Handler();

    private ClipboardManager.OnPrimaryClipChangedListener onPrimaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable, THRESHOLD);
        }
    };

    private ClipboardChangedRunnable mRunnable = new ClipboardChangedRunnable();

    private List<OnPrimaryClipChangedListener> mOnPrimaryClipChangedListeners = new ArrayList<>();

    /**
     * 自定义 OnPrimaryClipChangedListener
     * 用于处理某些情况下系统多次调用 onPrimaryClipChanged()
     */
    public interface OnPrimaryClipChangedListener {
        void onPrimaryClipChanged(ClipboardManager clipboardManager);
    }

    private class ClipboardChangedRunnable implements Runnable {

        @Override
        public void run() {
            for (OnPrimaryClipChangedListener listener : mOnPrimaryClipChangedListeners) {
                listener.onPrimaryClipChanged(mClipboardManager);
            }
//            ClipData data = mClipboardManager.getPrimaryClip();
//            if (data != null) {
//                ClipData.Item item = data.getItemAt(0);
//                String mimeType = data.getDescription().getMimeType(0);
//                Log.d(TAG, mimeType);
//                String htmlText = item.getHtmlText();
//                CharSequence text = item.getText();
//                if (!TextUtils.isEmpty(htmlText)) {
//                    Log.d(TAG, htmlText);
//                }
//                if (!TextUtils.isEmpty(text)) {
//                    Log.d(TAG, text.toString());
//                }
//            }
        }
    }

    private ClipboardUtil(Context context) {
        mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.addPrimaryClipChangedListener(onPrimaryClipChangedListener);
    }

    /**
     * 单例。暂时不清楚此处传 Activity 的 Context 是否会造成内存泄漏
     * 建议在 Application 的 onCreate 方法中实现
     *
     * @param context
     * @return
     */
    public static ClipboardUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ClipboardUtil(context);
        }
        return mInstance;
    }

    public void addOnPrimaryClipChangedListener(OnPrimaryClipChangedListener listener) {
        if (!mOnPrimaryClipChangedListeners.contains(listener)) {
            mOnPrimaryClipChangedListeners.add(listener);
        }
    }

    public void removeOnPrimaryClipChangedListener(OnPrimaryClipChangedListener listener) {
        mOnPrimaryClipChangedListeners.remove(listener);
    }

    /**
     * 获取剪贴板中第一条String
     *
     * @return
     */
    public String getClipText() {
        String text = null;
        ClipData data = mClipboardManager.getPrimaryClip();
        if (data != null
                && mClipboardManager.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            text = data.getItemAt(0).getText().toString();
            Log.d(TAG, text);
        }
        return text;
    }

    /**
     * 将文本拷贝至剪贴板
     *
     * @param text
     */
    public void copyText(String text) {
        ClipData myClip = ClipData.newPlainText("text", text);
        clipboard.setPrimaryClip(myClip);
    }

    /**
     * 清空剪贴板
     */
    public void clearTextClip() {
        ClipData data = ClipData.newPlainText("", "");
        mClipboardManager.setPrimaryClip(data);
    }

    public void destory() {
        if (mInstance != null) {
            mInstance = null;
        }
        if (mClipboardManager != null) {
            mClipboardManager = null;
        }
    }
}