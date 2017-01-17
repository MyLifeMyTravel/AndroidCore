package com.littlejie.demo.modules.base.notification;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.net.HttpManager;
import com.littlejie.core.util.DeviceUtil;
import com.littlejie.core.util.JsonUtil;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.entity.TestInfo;

import butterknife.BindView;

public class ProgressNotifyActivity extends BaseActivity implements View.OnClickListener {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.btn_pause)
    Button mBtnPause;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_progress_notification;
    }

    @Override
    protected void initData() {
        String test = "{\"path\":\"abc\",\"name\":\"厉圣杰\"}";
        TestInfo t = JsonUtil.fromJsonString(test, TestInfo.class);
        Log.d(TAG, t.toString());
        Log.d(TAG, DeviceUtil.getSysInfo());
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {
        mBtnStart.setOnClickListener(this);
        mBtnPause.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    protected void process() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                startDownload();
                break;
            case R.id.btn_pause:
                HttpManager.downloadBySystem("http://img1.niutuku.com/design/1207/2339/ntk-2339-435vnvwllr4xg.jpg",
                        Environment.DIRECTORY_DCIM, "a.jpg", DownloadManager.Request.NETWORK_WIFI, DownloadManager.Request.VISIBILITY_HIDDEN,
                        null, new HttpManager.OnDownloadCompleteListener() {
                            @Override
                            public void onDownloadComplete(long id, boolean success) {
                                ToastUtil.showDefaultToast(success ? "下载完成" : "下载失败");
                            }
                        });
//                HttpManager.downloadFileAsync("http://img1.niutuku.com/design/1207/2339/ntk-2339-435vnvwllr4xg.jpg",
//                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/a.jpg", new HttpManager.OnDownloadCompleteListener() {
//                            @Override
//                            public void onDownloadComplete(boolean success) {
//                                ToastUtil.showDefautToast(success ? "下载完成" : "下载失败");
//                            }
//                        });
                break;
            case R.id.btn_cancel:
                DeviceUtil.getExtSDCardPath();
                break;
        }
    }

    private void startDownload() {
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
}
