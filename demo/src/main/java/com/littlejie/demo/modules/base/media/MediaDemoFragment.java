package com.littlejie.demo.modules.base.media;

import android.os.Bundle;

import com.littlejie.demo.entity.ItemInfo;
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
        mLstItem.add(new ItemInfo("文件过滤", FilterFileActivity.class));
        mLstItem.add(new ItemInfo("SD卡 测试", SDCardActivity.class));
        mLstItem.add(new ItemInfo("监听文件的创建、删除、修改", MediaObserverActivity.class));
        mLstItem.add(new ItemInfo("简单文件管理器", SimpleFileManagerActivity.class));
        mLstItem.add(new ItemInfo("获取多媒体文件(图片、音频、视频)所在的目录", MediaLibraryActivity.class));
        notifyDataChanged();
    }
}
