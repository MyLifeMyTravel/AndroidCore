package com.littlejie.demo.modules.base;

import android.os.Bundle;

import com.littlejie.demo.BaseTabLayoutFragment;
import com.littlejie.demo.modules.base.component.ComponentDemoFragment;
import com.littlejie.demo.modules.base.fragment.DemoFragment;
import com.littlejie.demo.modules.base.media.MediaDemoFragment;
import com.littlejie.demo.modules.base.notification.NotificationListFragment;
import com.littlejie.demo.modules.base.system.SystemDemoFragment;
import com.littlejie.demo.modules.base.ui.UIDemoFragment;

/**
 * Created by littlejie on 2017/1/7.
 */

public class AndroidBaseFragment extends BaseTabLayoutFragment {

    public static AndroidBaseFragment newInstance() {
        Bundle args = new Bundle();

        AndroidBaseFragment fragment = new AndroidBaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        mFragmentList.add(UIDemoFragment.newInstance());
        mFragmentList.add(DemoFragment.newInstance());
        mFragmentList.add(ComponentDemoFragment.newInstance());
        mFragmentList.add(NotificationListFragment.newInstance());
        mFragmentList.add(MediaDemoFragment.newInstance());
        mFragmentList.add(SystemDemoFragment.newInstance());
        notifyDataChanged();
    }

}
