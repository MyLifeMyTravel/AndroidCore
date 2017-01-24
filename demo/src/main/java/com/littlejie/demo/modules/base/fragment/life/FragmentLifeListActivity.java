package com.littlejie.demo.modules.base.fragment.life;

import com.littlejie.demo.modules.BaseListActivity;
import com.littlejie.demo.entity.ItemInfo;

public class FragmentLifeListActivity extends BaseListActivity {

    @Override
    protected void process() {
        mLstItem.add(new ItemInfo("Fragment 生命周期", SimpleLifeActivity.class));
        mLstItem.add(new ItemInfo("与 ViewPager 使用时的生命周期", LifeWithViewPagerActivity.class));
        mLstItem.add(new ItemInfo("Fragment 添加进回退栈时的生命周期", LifeWithBackStackActivity.class));
        mLstItem.add(new ItemInfo("Fragment hide()、show()时的生命周期", LifeWithHideShowActivity.class));
        notifyDataChanged();
    }
}
