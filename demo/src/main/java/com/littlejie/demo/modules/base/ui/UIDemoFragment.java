package com.littlejie.demo.modules.base.ui;

import android.os.Bundle;

import com.littlejie.demo.annotation.Title;
import com.littlejie.demo.modules.BaseListFragment;
import com.littlejie.demo.modules.base.system.touch.ViewMoveActivity;

/**
 * Created by littlejie on 2017/2/15.
 */
@Title(title = "UI")
public class UIDemoFragment extends BaseListFragment {

    public static UIDemoFragment newInstance() {

        Bundle args = new Bundle();

        UIDemoFragment fragment = new UIDemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        super.process(savedInstanceState);
        mLstItem.add(DialogActivity.class);
        mLstItem.add(ViewMoveActivity.class);
        mLstItem.add(CustomTextActivity.class);
        mLstItem.add(PorterDuffActivity.class);
        notifyDataChanged();
    }
}
