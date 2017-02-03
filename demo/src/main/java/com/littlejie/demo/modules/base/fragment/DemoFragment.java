package com.littlejie.demo.modules.base.fragment;

import android.os.Bundle;

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
        mLstItem.add(FragmentCreateListActivity.class);
        mLstItem.add(FragmentLifeListActivity.class);
        mLstItem.add(FragmentMenuActivity.class);
        mLstItem.add(FragmentVisibleListActivity.class);
        notifyDataChanged();
    }
}
