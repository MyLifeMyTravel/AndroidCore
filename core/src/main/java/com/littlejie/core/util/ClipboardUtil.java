package com.littlejie.core.util;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

/**
 * 剪贴板工具类
 * http://developer.android.com/guide/topics/text/copy-paste.html
 * Created by littlejie on 2016/4/15.
 */
public class ClipboardUtil {

    public static final String TAG = "ClipboardUtil";
    private static ClipboardUtil instance;
    private ClipboardManager clipboard;

    private ClipboardUtil(Context context) {
        clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
    }

    public static ClipboardUtil getInstance(Context context) {
        if (instance == null) {
            instance = new ClipboardUtil(context);
        }
        return instance;
    }

    /**
     * 获取剪贴板中第一条String
     *
     * @return
     */
    public String getClipText() {
        String text = null;
        ClipData data = clipboard.getPrimaryClip();
        if (data != null
                && clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
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

    public void clearTextClip() {
        ClipData data = ClipData.newPlainText("", "");
        clipboard.setPrimaryClip(data);
    }

    public void destory() {
        if (instance != null) {
            instance = null;
        }
        if (clipboard != null) {
            clipboard = null;
        }
    }
}