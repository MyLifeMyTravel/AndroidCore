package com.littlejie.core.util;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * 剪贴板工具类
 * http://developer.android.com/guide/topics/text/copy-paste.html
 * Created by littlejie on 2016/4/15.
 */
public class ClipboardUtil {

    public static final String TAG = ClipboardUtil.class.getSimpleName();

    private static final String MIME_CONTACT = "vnd.android.cursor.dir/person";
    /**
     * 由于系统剪贴板在某些情况下会多次调用，但调用间隔基本不会超过5ms
     * 考虑到用户操作，将阈值设为100ms，过滤掉前几次无效回调
     */
    private static final int THRESHOLD = 100;

    private Context mContext;
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
            // 获取 ClipData 类型，根据源码可知，ClipDescription 的 mimeType 字段暂时有且只有一个
            // String mimeType = mClipboardManager.getPrimaryClipDescription().getMimeType(0);
            for (OnPrimaryClipChangedListener listener : mOnPrimaryClipChangedListeners) {
                listener.onPrimaryClipChanged(mClipboardManager);
            }
            // onPrimaryClipChangedListener(mClipboardManager);
        }
    }

    private void onPrimaryClipChangedListener(ClipboardManager clipboardManager) {
//        Log.d(TAG, clipboardManager.getPrimaryClip().toString());
//        //此处以拷贝 Intent 为例进行处理
//        ClipData data = clipboardManager.getPrimaryClip();
//        String mimeType = getPrimaryClipMimeType();
//        Log.d(TAG, "mimeType = " + mimeType);
//        //一般来说，收到系统 onPrimaryClipChanged() 回调时，剪贴板一定不为空
//        //但为了保险起见，在这边还是做了空指针判断
//        if (data == null) {
//            return;
//        }
//        //前四种为剪贴板默认的MimeType，但是当拷贝数据为Uri时，会出现其它MimeType，需要特殊处理
//        if (ClipDescription.MIMETYPE_TEXT_INTENT.equals(mimeType)) {
//            //一个 ClipData 可以有多个 ClipData.Item，这里假设只有一个
//            //startActivity(data.getItemAt(0).getIntent());
//        } else if (ClipDescription.MIMETYPE_TEXT_HTML.equals(mimeType)) {
//
//        } else if (ClipDescription.MIMETYPE_TEXT_PLAIN.equals(mimeType)) {
//
//        } else if (ClipDescription.MIMETYPE_TEXT_URILIST.equals(mimeType)) {
//            //当uri=content://media/external时，copyUri会进入此if-else语句
//        } else if (MIME_CONTACT.equals(mimeType)) {
//            //当uri=content://contacts/people时，copyUri会进入此if-else语句
//            StringBuilder sb = new StringBuilder();
//            int index = 1;
//            Uri uri = data.getItemAt(0).getUri();
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            while (cursor.moveToNext()) {
//                //打印所有联系人姓名
//                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                sb.append("联系人 " + index++ + " : " + name + "\n");
//            }
//        }
    }

    private ClipboardUtil(Context context) {
        mContext = context;
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
    public static ClipboardUtil init(Context context) {
        if (mInstance == null) {
            mInstance = new ClipboardUtil(context);
        }
        return mInstance;
    }

    /**
     * 获取ClipboardUtil实例，记得初始化
     *
     * @return
     */
    public static ClipboardUtil getInstance() {
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
     * 判断剪贴板内是否有数据
     *
     * @return
     */
    public boolean hasPrimaryClip() {
        return mClipboardManager.hasPrimaryClip();
    }

    /**
     * 获取剪贴板中第一条String
     *
     * @return
     */
    public String getClipText() {
        ClipData data = mClipboardManager.getPrimaryClip();
        if (data != null
                && mClipboardManager.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            return data.getItemAt(0).getText().toString();
        }
        return null;
    }

    /**
     * 获取剪贴板中第一条String
     *
     * @param context
     * @return
     */
    public String getClipText(Context context) {
        return getClipText(context, 0);
    }

    /**
     * 获取剪贴板中指定位置item的string
     *
     * @param context
     * @param index
     * @return
     */
    public String getClipText(Context context, int index) {
        ClipData data = mClipboardManager.getPrimaryClip();
        if (data == null) {
            return null;
        }
        if (data.getItemCount() > index) {
            return data.getItemAt(index).coerceToText(context).toString();
        }
        return null;
    }

    /**
     * 将文本拷贝至剪贴板
     *
     * @param text
     */
    public void copyText(String label, String text) {
        ClipData clip = ClipData.newPlainText(label, text);
        mClipboardManager.setPrimaryClip(clip);
    }

    /**
     * 将HTML等富文本拷贝至剪贴板
     *
     * @param label
     * @param text
     * @param htmlText
     */
    public void copyHtmlText(String label, String text, String htmlText) {
        ClipData clip = ClipData.newHtmlText(label, text, htmlText);
        mClipboardManager.setPrimaryClip(clip);
    }

    /**
     * 将Intent拷贝至剪贴板
     *
     * @param label
     * @param intent
     */
    public void copyIntent(String label, Intent intent) {
        ClipData clip = ClipData.newIntent(label, intent);
        mClipboardManager.setPrimaryClip(clip);
    }

    /**
     * 将Uri拷贝至剪贴板
     * If the URI is a content: URI,
     * this will query the content provider for the MIME type of its data and
     * use that as the MIME type.  Otherwise, it will use the MIME type
     * {@link ClipDescription#MIMETYPE_TEXT_URILIST}.
     * 如 uri = "content://contacts/people"，那么返回的MIME type将变成"vnd.android.cursor.dir/person"
     *
     * @param cr    ContentResolver used to get information about the URI.
     * @param label User-visible label for the clip data.
     * @param uri   The URI in the clip.
     */
    public void copyUri(ContentResolver cr, String label, Uri uri) {
        ClipData clip = ClipData.newUri(cr, label, uri);
        mClipboardManager.setPrimaryClip(clip);
    }

    /**
     * 将多组数据放入剪贴板中，如选中ListView多个Item，并将Item的数据一起放入剪贴板
     *
     * @param label    User-visible label for the clip data.
     * @param mimeType mimeType is one of them:{@link android.content.ClipDescription#MIMETYPE_TEXT_PLAIN},
     *                 {@link android.content.ClipDescription#MIMETYPE_TEXT_HTML},
     *                 {@link android.content.ClipDescription#MIMETYPE_TEXT_URILIST},
     *                 {@link android.content.ClipDescription#MIMETYPE_TEXT_INTENT}.
     * @param items    放入剪贴板中的数据
     */
    public void copyMultiple(String label, String mimeType, List<ClipData.Item> items) {
        if (items == null) {
            throw new NullPointerException("items is null");
        }
        int size = items.size();
        ClipData clip = new ClipData(label, new String[]{mimeType}, items.get(0));
        for (int i = 1; i < size; i++) {
            clip.addItem(items.get(i));
        }
        mClipboardManager.setPrimaryClip(clip);
    }

    /**
     * 获取当前剪贴板内容的MimeType
     *
     * @return 当前剪贴板内容的MimeType
     */
    public String getPrimaryClipMimeType() {
        return mClipboardManager.getPrimaryClipDescription().getMimeType(0);
    }

    /**
     * 获取剪贴板内容的MimeType
     *
     * @param clip 剪贴板内容
     * @return 剪贴板内容的MimeType
     */
    public String getClipMimeType(ClipData clip) {
        return clip.getDescription().getMimeType(0);
    }

    /**
     * 获取剪贴板内容的MimeType
     *
     * @param clipDescription 剪贴板内容描述
     * @return 剪贴板内容的MimeType
     */
    public String getClipMimeType(ClipDescription clipDescription) {
        return clipDescription.getMimeType(0);
    }

    /**
     * 清空剪贴板
     */
    public void clearClip() {
        mClipboardManager.setPrimaryClip(null);
    }

}