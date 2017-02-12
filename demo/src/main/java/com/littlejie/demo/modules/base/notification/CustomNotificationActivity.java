package com.littlejie.demo.modules.base.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.DemoApplication;
import com.littlejie.demo.utils.Constant;

import butterknife.OnClick;

/**
 * @link https://developer.android.com/guide/topics/ui/notifiers/notifications.html#CustomNotification
 */
@Description(description = "自定义 Notification 效果")
public class CustomNotificationActivity extends BaseActivity {

    public static final int ACCEPT_CODE = 0;
    public static final int DENY_CODE = 1;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_custom_notification;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {

    }

    // TODO: 2017/2/12 小米手机需要特殊处理
    @OnClick(R.id.btn_send_custom_notification)
    void sendCustomNotification() {
        //通过 PendingIntent 传值，可以在接受的 Activity 中通过 getIntents() 获取
        Intent accept = new Intent(this, PendingIntentActivity.class);
        accept.putExtra(Constant.EXTRA_NAME, "accept");

        Intent deny = new Intent(this, PendingIntentActivity.class);
        deny.putExtra(Constant.EXTRA_NAME, "deny");

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);
        //将控件的点击事件与对应的PendingIntent绑定
        remoteViews.setOnClickPendingIntent(R.id.btn_accept,
                PendingIntent.getActivity(this, ACCEPT_CODE, accept, PendingIntent.FLAG_UPDATE_CURRENT));
        remoteViews.setOnClickPendingIntent(R.id.btn_deny,
                PendingIntent.getActivity(this, DENY_CODE, deny, PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(Constant.VIBRATE)
                .setContentTitle("自定义通知样式、效果")
                .setContentText("我是自定义通知样式、效果的内容")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContent(remoteViews);
        Notification notify = builder.build();
        //设置点击后自动消失
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
//        if (Build.VERSION.SDK_INT >= 16) {
//            builder.setCustomBigContentView(remoteViews);
//        }
//        builder.setCustomContentView(remoteViews);
        DemoApplication.getNotificationManager().notify(10, notify);
    }

    @Override
    protected void process() {

    }

}
