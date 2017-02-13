package com.littlejie.demo.modules.base.notification;

import android.support.v4.app.NotificationCompat;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.DemoApplication;
import com.littlejie.demo.utils.Constant;

import butterknife.OnClick;

/**
 * 要显示 heads-up 样式的 Notification 要满足以下条件，这里只展示满足 0 和 2 的
 * 0. API Level >= 21
 * 1. The user's activity is in fullscreen mode (the app uses fullScreenIntent), or
 * 2. The notification has high priority and uses ringtones or vibrations
 * <p>
 * 补充：高优先级+震动出现Heads-up Notification的几率比高优先级+响铃大
 */
@Description(description = "Heads-up Notification")
public class HeadsUpNotificationActivity extends BaseActivity {

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_heads_up_notification;
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

    @OnClick(R.id.btn_heads_up_notify_with_vibrate)
    void sendHeadsUpNotificationWithVibrate() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Heads up notification")
                .setContentText("高优先级+震动")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(Constant.VIBRATE);
        DemoApplication.getNotificationManager().notify(1, builder.build());
    }

    @OnClick(R.id.btn_heads_up_notify_with_ringtones)
    void sendHeadsUpNotificationWithRingtones() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Heads up notification")
                .setContentText("高优先级+响铃")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(Constant.NOTIFICATION_SOUND);
        DemoApplication.getNotificationManager().notify(2, builder.build());
    }

    @Override
    protected void process() {

    }
}
