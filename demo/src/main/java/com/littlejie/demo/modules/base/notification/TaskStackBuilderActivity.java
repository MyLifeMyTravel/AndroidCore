package com.littlejie.demo.modules.base.notification;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;

import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

@Description(description = "TaskStackBuilder 简单测试")
public class TaskStackBuilderActivity extends Activity implements View.OnClickListener {

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_stack_builder);

        findViewById(R.id.btn_regular).setOnClickListener(this);
        findViewById(R.id.btn_special).setOnClickListener(this);

        // NotificationManager system service.
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_regular:
                sendRegularNotification();
                break;
            case R.id.btn_special:
                sendSpecailNotification();
                break;
        }
    }

    private void sendRegularNotification() {
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

    private void sendSpecailNotification() {
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
}
