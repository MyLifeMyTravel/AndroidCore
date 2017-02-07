package com.littlejie.demo.modules.base.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.OnClick;

@Description(description = "TaskStackBuilder 简单测试")
public class TaskStackBuilderActivity extends BaseActivity {

    private NotificationManager mNotificationManager;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_task_stack_builder;
    }

    @Override
    protected void initData() {
        // NotificationManager system service.
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.btn_regular)
    void sendRegularNotification() {
        Intent resultIntent = new Intent(this, NormalResultActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack
        stackBuilder.addParentStack(NormalResultActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Regular Activity");
        builder.setContentText("点我进入应用会按正常流程退出");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);
        mNotificationManager.notify(1, builder.build());
    }

    @OnClick(R.id.btn_special)
    void sendSpecialNotification() {
        // Instantiate a Builder object.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Special Activity");
        builder.setContentText("进入特殊页面,按返回键直接回到桌面");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        // Creates an Intent for the Activity
        Intent notifyIntent =
                new Intent(this, SpecialResultActivity.class);
        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Creates the PendingIntent
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(this, 0, notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // Puts the PendingIntent into the notification builder
        builder.setContentIntent(notifyPendingIntent);
        // Notifications are issued by sending them to the
        // Builds an anonymous Notification object from the builder, and
        // passes it to the NotificationManager
        mNotificationManager.notify(2, builder.build());
    }

    @Override
    protected void process() {

    }
}
