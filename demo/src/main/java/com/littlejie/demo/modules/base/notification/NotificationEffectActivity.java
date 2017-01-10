package com.littlejie.demo.modules.base.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.littlejie.demo.R;

/**
 * 通知提示效果
 */
public class NotificationEffectActivity extends Activity implements View.OnClickListener {

    private NotificationManager mManager;
    private Bitmap mLargeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_effect);

        findViewById(R.id.btn_notify_only_text).setOnClickListener(this);
        findViewById(R.id.btn_notify_with_ring).setOnClickListener(this);
        findViewById(R.id.btn_notify_with_vibrate).setOnClickListener(this);
        findViewById(R.id.btn_notify_with_lights).setOnClickListener(this);
        findViewById(R.id.btn_notify_with_mix).setOnClickListener(this);
        findViewById(R.id.btn_notify_with_insistent).setOnClickListener(this);
        findViewById(R.id.btn_notify_with_alert_once).setOnClickListener(this);
        findViewById(R.id.btn_clear_notify).setOnClickListener(this);

        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mLargeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLargeIcon != null) {
            if (!mLargeIcon.isRecycled()) {
                mLargeIcon.recycle();
            }
            mLargeIcon = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_notify_only_text:
                showNotifyOnlyText();
                break;
            case R.id.btn_notify_with_ring:
                showNotifyWithRing();
                break;
            case R.id.btn_notify_with_vibrate:
                showNotifyWithVibrate();
                break;
            case R.id.btn_notify_with_lights:
                showNotifyWithLights();
                break;
            case R.id.btn_notify_with_mix:
                showNotifyWithMixed();
                break;
            case R.id.btn_notify_with_insistent:
                showInsistentNotify();
                break;
            case R.id.btn_notify_with_alert_once:
                showAlertOnceNotify();
                break;
            case R.id.btn_clear_notify:
                clearNotify();
                break;
        }
    }

    /**
     * 最普通的通知效果
     */
    private void showNotifyOnlyText() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(mLargeIcon)
                .setContentTitle("我是只有文字效果的通知")
                .setContentText("我没有铃声、震动、呼吸灯,但我就是一个通知");
        mManager.notify(1, builder.build());
    }

    /**
     * 展示有自定义铃声效果的通知
     * 补充:使用系统自带的铃声效果:Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
     */
    private void showNotifyWithRing() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是伴有铃声效果的通知")
                .setContentText("美妙么?安静听~")
                //调用系统默认响铃,设置此属性后setSound()会无效
                //.setDefaults(Notification.DEFAULT_SOUND)
                //调用系统多媒体裤内的铃声
                //.setSound(Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,"2"));
                //调用自己提供的铃声
                .setSound(Uri.parse("android.resource://com.littlejie.notification/" + R.raw.sound));
        //另一种设置铃声的方法
        //Notification notify = builder.build();
        //调用系统默认铃声
        //notify.defaults = Notification.DEFAULT_SOUND;
        //调用自己提供的铃声
        //notify.sound = Uri.parse("android.resource://com.littlejie.notification/"+R.raw.sound);
        //调用系统自带的铃声
        //notify.sound = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,"2");
        //mManager.notify(2,notify);
        mManager.notify(2, builder.build());
    }

    /**
     * 展示有震动效果的通知,需要在AndroidManifest.xml中申请震动权限
     * <uses-permission android:name="android.permission.VIBRATE" />
     * 补充:测试震动的时候,手机的模式一定要调成铃声+震动模式,否则你是感受不到震动的
     */
    private void showNotifyWithVibrate() {
        //震动也有两种设置方法,与设置铃声一样,在此不再赘述
        long[] vibrate = new long[]{0, 500, 1000, 1500};
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是伴有震动效果的通知")
                .setContentText("颤抖吧,凡人~")
                //使用系统默认的震动参数,会与自定义的冲突
                //.setDefaults(Notification.DEFAULT_VIBRATE)
                //自定义震动效果
                .setVibrate(vibrate);
        //另一种设置震动的方法
        //Notification notify = builder.build();
        //调用系统默认震动
        //notify.defaults = Notification.DEFAULT_VIBRATE;
        //调用自己设置的震动
        //notify.vibrate = vibrate;
        //mManager.notify(3,notify);
        mManager.notify(3, builder.build());
    }

    /**
     * 显示带有呼吸灯效果的通知,但是不知道为什么,自己这里测试没成功
     */
    private void showNotifyWithLights() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是带有呼吸灯效果的通知")
                .setContentText("一闪一闪亮晶晶~")
                //ledARGB 表示灯光颜色、 ledOnMS 亮持续时间、ledOffMS 暗的时间
                .setLights(0xFF0000, 3000, 3000);
        Notification notify = builder.build();
        //只有在设置了标志符Flags为Notification.FLAG_SHOW_LIGHTS的时候，才支持呼吸灯提醒。
        notify.flags = Notification.FLAG_SHOW_LIGHTS;
        //设置lights参数的另一种方式
        //notify.ledARGB = 0xFF0000;
        //notify.ledOnMS = 500;
        //notify.ledOffMS = 5000;
        //使用handler延迟发送通知,因为连接usb时,呼吸灯一直会亮着
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mManager.notify(4, builder.build());
            }
        }, 10000);
    }

    /**
     * 显示带有默认铃声、震动、呼吸灯效果的通知
     * 如需实现自定义效果,请参考前面三个例子
     */
    private void showNotifyWithMixed() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是有铃声+震动+呼吸灯效果的通知")
                .setContentText("我是最棒的~")
                //等价于setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                .setDefaults(Notification.DEFAULT_ALL);
        mManager.notify(5, builder.build());
    }

    /**
     * 通知无限循环,直到用户取消或者打开通知栏(其实触摸就可以了),效果与FLAG_ONLY_ALERT_ONCE相反
     * 注:这里没有给Notification设置PendingIntent,也就是说该通知无法响应,所以只能手动取消
     */
    private void showInsistentNotify() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是一个死循环,除非你取消或者响应")
                .setContentText("啦啦啦~")
                .setDefaults(Notification.DEFAULT_ALL);
        Notification notify = builder.build();
        notify.flags |= Notification.FLAG_INSISTENT;
        mManager.notify(6, notify);
    }

    /**
     * 通知只执行一次,与默认的效果一样
     */
    private void showAlertOnceNotify() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("仔细看,我就执行一遍")
                .setContentText("好了,已经一遍了~")
                .setDefaults(Notification.DEFAULT_ALL);
        Notification notify = builder.build();
        notify.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
        mManager.notify(7, notify);
    }

    /**
     * 清除所有通知
     */
    private void clearNotify() {
        mManager.cancelAll();
    }

}
