package com.littlejie.demo.modules.base.fragment.create;

import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.BaseListActivity;

@Description(description = "创建 Fragment 的两种方式")
public class FragmentCreateListActivity extends BaseListActivity {

    @Override
    protected void process() {
        super.process();
        mLstItem.add(StaticCreateActivity.class);
        mLstItem.add(DynamicCreateActivity.class);
        notifyDataChanged();
    }
}
