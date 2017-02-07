package com.littlejie.demo.modules.base.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.MainActivity;

import butterknife.OnClick;

/**
 * 这是一个最简单的通知例子,该通知只有必要的三个属性:
 * 小图标、标题、内容
 */
@Description(description = "一个最简单的Notification Demo")
public class SimplestNotificationActivity extends BaseActivity{

    private NotificationManager mNotifyManager;
    private Bitmap mLargeIcon;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_simplest_notification;
    }

    @Override
    protected void initData() {
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mLargeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_fab_repair);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {

    }

    void sendNotification() {
        //获取NotificationManager实例
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                //设置小图标
                .setSmallIcon(R.mipmap.icon_fab_repair)
                //设置通知标题
                .setContentTitle("最简单的Notification")
                //设置通知内容
                .setContentText("只有小图标、标题、内容");
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(1, builder.build());
    }

    /**
     * 发送一个简单的通知,只带有小图标、标题、内容
     */
    @OnClick(R.id.btn_send_simplest_notification)
    void sendSimplestNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("最简单的Notification")
                .setContentText("只有小图标、标题、内容");
        mNotifyManager.notify(1, builder.build());
    }

    /**
     * 发送一个具有大图标的简单通知
     * 当setSmallIcon与setLargeIcon同时存在时,smallIcon显示在通知的右下角,largeIcon显示在左侧
     * 当只设置setSmallIcon时,smallIcon显示在左侧
     */
    @OnClick(R.id.btn_send_simplest_notification_with_large_icon)
    void sendSimplestNotificationWithLargeIcon() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("带大图标的Notification")
                .setContentText("有小图标、大图标、标题、内容")
                .setLargeIcon(mLargeIcon);
        mNotifyManager.notify(2, builder.build());
    }

    /**
     * 发送一个点击跳转到MainActivity的消息
     */
    @OnClick(R.id.btn_send_simplest_notification_with_action)
    void sendSimplestNotificationWithAction() {
        //获取PendingIntent
        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建 Notification.Builder 对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentTitle("我是带Action的Notification")
                .setContentText("点我会打开MainActivity")
                .setContentIntent(mainPendingIntent);
        //发送通知
        mNotifyManager.notify(3, builder.build());
    }

    @Override
    protected void process() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //回收bitmap
        if (mLargeIcon != null) {
            if (!mLargeIcon.isRecycled()) {
                mLargeIcon.recycle();
            }
            mLargeIcon = null;
        }
    }

}
