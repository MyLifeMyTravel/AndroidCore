package com.littlejie.demo.modules.base.fragment;

import android.os.Bundle;

import com.littlejie.demo.entity.ItemInfo;
import com.littlejie.demo.modules.BaseListFragment;
import com.littlejie.demo.modules.base.fragment.create.FragmentCreateListActivity;
import com.littlejie.demo.modules.base.fragment.life.FragmentLifeListActivity;
import com.littlejie.demo.modules.base.fragment.menu.FragmentMenuActivity;
import com.littlejie.demo.modules.base.fragment.visible.FragmentVisibleListActivity;

/**
 * Fragment Demo 展示
 * Created by littlejie on 2017/1/23.
 */
public class DemoFragment extends BaseListFragment {

    public static DemoFragment newInstance() {

        Bundle args = new Bundle();

        DemoFragment fragment = new DemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        mLstItem.add(new ItemInfo("Fragment 创建", FragmentCreateListActivity.class));
        mLstItem.add(new ItemInfo("Fragment 生命周期", FragmentLifeListActivity.class));
        mLstItem.add(new ItemInfo("Fragment 创建菜单", FragmentMenuActivity.class));
        mLstItem.add(new ItemInfo("Fragment 可见性", FragmentVisibleListActivity.class));
        notifyDataChanged();
    }
}
