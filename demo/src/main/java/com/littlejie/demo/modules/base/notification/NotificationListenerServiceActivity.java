package com.littlejie.demo.modules.base.notification;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Button;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.BindView;
import butterknife.OnClick;

@Description(description = "NotificationListenerService 测试")
public class NotificationListenerServiceActivity extends BaseActivity {

    //此为 Settings 中的常量,不过是属于隐藏字段
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    @BindView(R.id.btn_setting)
    Button mBtnSetNotifyAccess;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_get_notification_bar;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.btn_setting)
    void setNotifyAccess() {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        startActivity(intent);
    }

    @OnClick(R.id.btn_start)
    void startNotificationListenerService() {
        startService(new Intent(NotificationListenerServiceActivity.this, MyNotificationListenerService.class));
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBtnSetNotifyAccess.setText(String.format(getString(R.string.set_notification_access), isEnabled() ? "已开启" : "未开启"));
    }

    /**
     * 判断 Notification access 是否开启
     *
     * @return
     */
    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
