package com.littlejie.demo.modules.advance.ui;

import android.os.Bundle;

import com.littlejie.demo.annotation.Title;
import com.littlejie.demo.modules.BaseListFragment;

@Title(title = "自定义UI Demo")
public class CustomUIListFragment extends BaseListFragment {

    public static CustomUIListFragment newInstance() {

        Bundle args = new Bundle();

        CustomUIListFragment fragment = new CustomUIListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        super.process(savedInstanceState);
        mLstItem.add(CustomUIActivity.class);
        mLstItem.add(CollapseActivity.class);
        mLstItem.add(SearchEffectActivity.class);
        notifyDataChanged();
    }
}
