package com.littlejie.demo.modules.base.fragment.create;

import com.littlejie.demo.entity.ItemInfo;
import com.littlejie.demo.modules.BaseListActivity;

public class FragmentCreateListActivity extends BaseListActivity {

    @Override
    protected void process() {
        super.process();
        mLstItem.add(new ItemInfo("静态添加 Fragment", StaticCreateActivity.class));
        mLstItem.add(new ItemInfo("动态添加 Fragment", DynamicCreateActivity.class));
        notifyDataChanged();
    }
}
