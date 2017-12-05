package com.littlejie.demo.modules.base.media;

import android.os.Bundle;

import com.littlejie.demo.annotation.Title;
import com.littlejie.demo.modules.BaseListFragment;

/**
 * Created by littlejie on 2017/2/3.
 */
@Title(title = "多媒体")
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
        mLstItem.add(ThumbnailsActivity.class);
        mLstItem.add(FileActionActivity.class);
        mLstItem.add(TakePhotoActivity.class);
        mLstItem.add(SystemGalleryActivity.class);
        mLstItem.add(CameraActivity.class);
        mLstItem.add(Camera2Activity.class);
        notifyDataChanged();
    }
}
