package com.littlejie.demo.modules.base.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.OnClick;

@Description(description = "带进度条的Notification")
public class ProgressNotifyActivity extends BaseActivity {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_progress_notification;
    }

    @Override
    protected void initData() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {
    }

    @OnClick(R.id.btn_start)
    void startDownload() {
        mBuilder.setContentTitle("文件下载")
                .setContentText("下载中...")
                .setSmallIcon(R.mipmap.icon_fab_repair);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int percent = 0;
                for (; percent < 1000; percent += 10) {
                    mBuilder.setProgress(1000, percent, false);
                    mNotificationManager.notify(1, mBuilder.build());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mBuilder.setContentText("下载完成");
                mNotificationManager.notify(1, mBuilder.build());
            }
        }).start();
    }

    @OnClick(R.id.btn_pause)
    void pause() {

    }

    @OnClick(R.id.btn_cancel)
    void cancel() {

    }

    @Override
    protected void process() {

    }

}
