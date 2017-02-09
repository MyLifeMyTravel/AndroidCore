package com.littlejie.demo.modules.base.component.service;

import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.BaseListActivity;

/**
 * Created by littlejie on 2017/2/4.
 */
@Description(description = "Service(服务) Demo")
public class ServiceListActivity extends BaseListActivity {

    @Override
    protected void process() {
        super.process();
        mLstItem.add(BinderServiceActivity.class);
        notifyDataChanged();
    }
}
