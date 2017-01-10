package com.littlejie.demo.modules.base.notification;

import com.littlejie.demo.entity.ItemInfo;
import com.littlejie.demo.modules.BaseListActivity;

/**
 * Created by littlejie on 2017/1/10.
 */

public class NotificationListActivity extends BaseListActivity {

    @Override
    protected void process() {
        super.process();
        mLstItem.add(new ItemInfo("一个简单的Demo", SimplestNotificationActivity.class));
        mLstItem.add(new ItemInfo("Notification 简单 Demo", SimpleNotificationActivity.class));
        mLstItem.add(new ItemInfo("Notification 提示形式", NotificationEffectActivity.class));
        mLstItem.add(new ItemInfo("Notification 样式", NotificationStyleActivity.class));
        mLstItem.add(new ItemInfo("TaskStackBuilder 简单测试", TaskStackBuilderActivity.class));
        mLstItem.add(new ItemInfo("启动 NotificationListenerService", NotificationListenerServiceActivity.class));
        mLstItem.add(new ItemInfo("带进度条的Notification", ProgressNotifyActivity.class));
        notifyDataChanged();
    }
}
