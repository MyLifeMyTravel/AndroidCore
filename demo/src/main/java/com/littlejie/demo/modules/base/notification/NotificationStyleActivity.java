package com.littlejie.demo.modules.base.notification;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.littlejie.demo.R;

/**
 * Notification 样式 Demo
 */
public class NotificationStyleActivity extends Activity implements View.OnClickListener {

    private NotificationManager mNotifyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_style);

        findViewById(R.id.btn_big_text_style).setOnClickListener(this);
        findViewById(R.id.btn_inbox_style).setOnClickListener(this);
        findViewById(R.id.btn_big_picture_style).setOnClickListener(this);
        findViewById(R.id.btn_media_style).setOnClickListener(this);
        findViewById(R.id.btn_messaging_style).setOnClickListener(this);

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_big_text_style:
                sendBigTextStyleNotification();
                break;
            case R.id.btn_inbox_style:
                sendInboxStyleNotification();
                break;
            case R.id.btn_big_picture_style:

                break;
            case R.id.btn_media_style:

                break;
            case R.id.btn_messaging_style:

                break;
        }
    }

    /**
     * 发送一个BigTextStyle的通知
     */
    private void sendBigTextStyleNotification() {
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        //相当于 setContentTitle()
        bigTextStyle.setBigContentTitle("系统支持BigTextStyle时显示的标题");
        //bigText() 方法相当于 setContentText()
        bigTextStyle.bigText("系统支持BigTextStyle\n系统支持BigTextStyle\n系统支持BigTextStyle\n系统支持BigTextStyle\n系统支持BigTextStyle\n系统支持BigTextStyle\n系统支持BigTextStyle\n系统支持BigTextStyle\n系统支持BigTextStyle\n");
        //该方法没什么用，可以不设置
        bigTextStyle.setSummaryText("BigTextStyle SummaryText");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("BigTextStyle示例")
                .setContentText("BigTextStyle示例演示")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(bigTextStyle);
        mNotifyManager.notify(1, builder.build());
    }

    /**
     * 发送一个InboxStyle类型的通知
     */
    private void sendInboxStyleNotification() {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("系统支持InboxStyle时显示的标题")
                //InboxStyle最多支持添加5行数据，超过5行不显示
                .addLine("Line 1")
                .addLine("Line 2")
                .addLine("Line 6")
                .setSummaryText("+3 more");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("InboxStyle示例")
                .setContentText("InboxStyle演示示例")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(inboxStyle);
        mNotifyManager.notify(2, builder.build());
    }

    private void sendBigPictureStyleNotification() {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();

    }

    private void sendMediaStyleNotification() {

    }

    private void sendMessagingStyleNotification() {

    }
}
