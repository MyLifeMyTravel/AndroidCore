package com.littlejie.demo.modules.base.component;

import android.os.Bundle;

import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.annotation.Title;
import com.littlejie.demo.modules.BaseListFragment;
import com.littlejie.demo.modules.base.component.activity.ActivityListActivity;
import com.littlejie.demo.modules.base.component.broadcast.BroadcastListActivity;
import com.littlejie.demo.modules.base.component.provider.ProviderListActivity;
import com.littlejie.demo.modules.base.component.service.ServiceListActivity;

/**
 * Created by littlejie on 2017/2/3.
 */
@Title(title = "四大组件")
@Description(description = "Android 四大组件")
public class ComponentDemoFragment extends BaseListFragment {

    public static ComponentDemoFragment newInstance() {

        Bundle args = new Bundle();

        ComponentDemoFragment fragment = new ComponentDemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        super.process(savedInstanceState);
        mLstItem.add(ActivityListActivity.class);
        mLstItem.add(ServiceListActivity.class);
        mLstItem.add(ProviderListActivity.class);
        mLstItem.add(BroadcastListActivity.class);
        notifyDataChanged();
    }
}
