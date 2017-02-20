package com.littlejie.demo.modules.advance;

import android.os.Bundle;

import com.littlejie.demo.modules.BaseListFragment;

/**
 * Created by littlejie on 2017/2/20.
 */

public class AndroidAdvanceFragment extends BaseListFragment {

    public static AndroidAdvanceFragment newInstance() {

        Bundle args = new Bundle();

        AndroidAdvanceFragment fragment = new AndroidAdvanceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        super.process(savedInstanceState);
        mLstItem.add(BrowserScreenShotActivity.class);
        notifyDataChanged();
    }
}
