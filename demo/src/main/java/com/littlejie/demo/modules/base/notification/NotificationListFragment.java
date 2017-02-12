package com.littlejie.demo.modules.base.notification;

import android.os.Bundle;

import com.littlejie.demo.annotation.Title;
import com.littlejie.demo.modules.BaseListFragment;

/**
 * Created by littlejie on 2017/1/23.
 */
@Title(title = "通知")
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
        mLstItem.add(SimplestNotificationActivity.class);
        mLstItem.add(SimpleNotificationActivity.class);
        mLstItem.add(NotificationEffectActivity.class);
        mLstItem.add(NotificationStyleActivity.class);
        mLstItem.add(TaskStackBuilderActivity.class);
        mLstItem.add(NotificationListenerServiceActivity.class);
        mLstItem.add(ProgressNotifyActivity.class);
        mLstItem.add(HeadsUpNotificationActivity.class);
        mLstItem.add(CustomNotificationActivity.class);
        notifyDataChanged();
    }

}
