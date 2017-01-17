package com.littlejie.demo.modules.base.system;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.littlejie.core.util.ClipboardUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.modules.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by littlejie on 2017/1/6.
 */

public class ClipboardActivity extends Activity implements View.OnClickListener,
        ClipboardUtil.OnPrimaryClipChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MIME_CONTACT = "vnd.android.cursor.dir/person";

    private TextView mTvCopied;

    private ClipboardUtil mClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboard);

        //ClipboardUtil在Application的onCreate中调用init初始化
        mClipboard = ClipboardUtil.getInstance();
        mClipboard.addOnPrimaryClipChangedListener(this);

        mTvCopied = (TextView) findViewById(R.id.tv_copied);

        findViewById(R.id.btn_copy_text).setOnClickListener(this);
        findViewById(R.id.btn_copy_rich_text).setOnClickListener(this);
        findViewById(R.id.btn_copy_intent).setOnClickListener(this);
        findViewById(R.id.btn_copy_uri).setOnClickListener(this);
        findViewById(R.id.btn_copy_multiple).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClipboard.removeOnPrimaryClipChangedListener(this);
    }

    @Override
    public void onPrimaryClipChanged(ClipboardManager clipboardManager) {
        Log.d(TAG, clipboardManager.getPrimaryClip().toString());
        mTvCopied.setText(clipboardManager.getPrimaryClip().toString());
        //此处以拷贝 Intent 为例进行处理
        ClipData data = clipboardManager.getPrimaryClip();
        String mimeType = mClipboard.getPrimaryClipMimeType();
        Log.d(TAG, "mimeType = " + mimeType);
        //一般来说，收到系统 onPrimaryClipChanged() 回调时，剪贴板一定不为空
        //但为了保险起见，在这边还是做了空指针判断
        if (data == null) {
            return;
        }
        //前四种为剪贴板默认的MimeType，但是当拷贝数据为Uri时，会出现其它MimeType，需要特殊处理
        if (ClipDescription.MIMETYPE_TEXT_INTENT.equals(mimeType)) {
            //一个 ClipData 可以有多个 ClipData.Item，这里假设只有一个
            startActivity(data.getItemAt(0).getIntent());
        } else if (ClipDescription.MIMETYPE_TEXT_HTML.equals(mimeType)) {

        } else if (ClipDescription.MIMETYPE_TEXT_PLAIN.equals(mimeType)) {

        } else if (ClipDescription.MIMETYPE_TEXT_URILIST.equals(mimeType)) {
            //当uri=content://media/external时，copyUri会进入此if-else语句
        } else if (MIME_CONTACT.equals(mimeType)) {
            Log.d(TAG, mClipboard.coercePrimaryClipToText().toString());
            //当uri=content://contacts/people时，copyUri会进入此if-else语句
            StringBuilder sb = new StringBuilder(mTvCopied.getText() + "\n\n");
            int index = 1;
            Uri uri = data.getItemAt(0).getUri();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            while (cursor.moveToNext()) {
                //打印所有联系人姓名
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                sb.append("联系人 " + index++ + " : " + name + "\n");
            }
            mTvCopied.setText(sb.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_copy_text:
                //普通的文本拷贝
                mClipboard.copyText("文本拷贝", "我是文本~");
                break;
            case R.id.btn_copy_rich_text:
                //平时在浏览器网页上执行的copy就是这种，一般浏览器会使用 ClipData.newHtmlText(label, text, htmlText)往剪贴板里塞东西
                //所以，将这段内容拷贝到诸如 Google+ 、 Gmail 等能处理富文本内容的应用能看到保留格式的内容
                //补充：测试了 QQ浏览器 、 UC浏览器，发现他们拷贝的内容只是单纯的文本，即使用 ClipData.newPlainText(label, text) 往剪贴板里塞东西
                mClipboard.copyHtmlText("HTML拷贝", "我是HTML",
                        "<strong style=\"margin: 0px; padding: 0px; border: 0px; color: rgb(64, 64, 64); font-family: STHeiti, 'Microsoft YaHei', Helvetica, Arial, sans-serif; font-size: 17px; font-style: normal; font-variant: normal; letter-spacing: normal; line-height: 25.920001983642578px; orphans: auto; text-align: justify; text-indent: 34.560001373291016px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(235, 23, 23);\">央视</strong>");
                break;
            case R.id.btn_copy_intent:
                mClipboard.copyIntent("Intent拷贝", getOpenBrowserIntent());
                break;
            case R.id.btn_copy_uri:
                mClipboard.copyUri(getContentResolver(), "Uri拷贝", Uri.parse("content://contacts/people"));
                //mClipboard.copyUri(getContentResolver(), "Uri拷贝", Uri.parse("content://media/external"));
                break;
            case R.id.btn_copy_multiple:
                copyMultiple();
                break;
        }
    }

    /**
     * 打开浏览器
     *
     * @return
     */
    private Intent getOpenBrowserIntent() {
        Uri uri = Uri.parse("http://www.baidu.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        return intent;
    }

    /**
     * 拷贝多组数据到剪贴板
     */
    private void copyMultiple() {
        //ClipData 目前仅能设置单个 MimeType
        List<ClipData.Item> items = new ArrayList<>();
        //故 ClipData.Item 的类型必须和 MimeType 设置的相符
        //比如都是文字，都是URI或都是Intent，而不是混合各种形式。
        ClipData.Item item1 = new ClipData.Item("text1");
        ClipData.Item item2 = new ClipData.Item("text2");
        ClipData.Item item3 = new ClipData.Item("text3");
        ClipData.Item item4 = new ClipData.Item("text4");
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        mClipboard.copyMultiple("Multiple Copy", ClipDescription.MIMETYPE_TEXT_PLAIN, items);
    }
}
