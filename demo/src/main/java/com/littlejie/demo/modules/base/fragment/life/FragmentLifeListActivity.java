package com.littlejie.demo.modules.base.fragment.life;

import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.BaseListActivity;

@Description(description = "Fragment 生命周期")
public class FragmentLifeListActivity extends BaseListActivity {

    @Override
    protected void process() {
        mLstItem.add(SimpleLifeActivity.class);
        mLstItem.add(LifeWithViewPagerActivity.class);
        mLstItem.add(LifeWithBackStackActivity.class);
        mLstItem.add(LifeWithHideShowActivity.class);
        notifyDataChanged();
    }
}
