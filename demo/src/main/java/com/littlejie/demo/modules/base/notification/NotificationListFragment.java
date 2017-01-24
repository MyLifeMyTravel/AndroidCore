package com.littlejie.demo.modules.base.notification;

import android.os.Bundle;

import com.littlejie.demo.entity.ItemInfo;
import com.littlejie.demo.modules.BaseListFragment;

/**
 * Created by littlejie on 2017/1/23.
 */

public class NotificationListFragment extends BaseListFragment {

    public static NotificationListFragment newInstance() {

        Bundle args = new Bundle();

        NotificationListFragment fragment = new NotificationListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        super.process(savedInstanceState);
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
