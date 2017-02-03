package com.littlejie.demo.modules.base.media;

import android.os.Bundle;

import com.littlejie.demo.modules.BaseListFragment;

/**
 * Created by littlejie on 2017/2/3.
 */

public class MediaDemoFragment extends BaseListFragment {

    public static MediaDemoFragment newInstance() {

        Bundle args = new Bundle();

        MediaDemoFragment fragment = new MediaDemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        super.process(savedInstanceState);
        mLstItem.add(FilterFileActivity.class);
        mLstItem.add(SDCardActivity.class);
        mLstItem.add(MediaObserverActivity.class);
        mLstItem.add(SimpleFileManagerActivity.class);
        mLstItem.add(MediaLibraryActivity.class);
        notifyDataChanged();
    }
}
