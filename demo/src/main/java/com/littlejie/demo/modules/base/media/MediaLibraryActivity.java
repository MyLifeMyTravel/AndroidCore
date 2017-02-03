package com.littlejie.demo.modules.base.media;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.entity.FileInfo;
import com.littlejie.demo.modules.adapter.SimpleFileInfoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 获取多媒体文件(图片、音频、视频)所在的目录
 * Created by littlejie on 2017/2/3.
 */

@Description(description = "获取多媒体文件(图片、音频、视频)所在的目录")
public class MediaLibraryActivity extends BaseActivity {

    private static final String[] PROJECTION = new String[]{MediaStore.Files.FileColumns.DATA};

    @BindView(R.id.lv)
    ListView mLv;
    private SimpleFileInfoAdapter mAdapter;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_media_folder;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mAdapter = new SimpleFileInfoAdapter(this);
        mLv.setAdapter(mAdapter);
    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.btn_get_image_folder)
    void getImageFolder() {
        mAdapter.setData(getMediaFolder(MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
    }

    @OnClick(R.id.btn_get_audio_folder)
    void getAudioFolder() {
        mAdapter.setData(getMediaFolder(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI));
    }

    @OnClick(R.id.btn_get_video_folder)
    void getVideoFolder() {
        mAdapter.setData(getMediaFolder(MediaStore.Video.Media.EXTERNAL_CONTENT_URI));
    }

    @Override
    protected void process() {

    }

    private List<FileInfo> getMediaFolder(Uri uri) {
        long start = System.currentTimeMillis();
        List<FileInfo> lstFolder = null;
        for (int i = 0; i < 10; i++) {
            lstFolder = new ArrayList<>();
            Set<String> folderSet = new HashSet<>();
            Cursor cursor = getContentResolver().query(uri, PROJECTION, null, null, null);
            if (cursor == null) {
                return null;
            }
            while (cursor.moveToNext()) {
                //这样写可以减少10ms左右的时间
                String path = cursor.getString(0);
                folderSet.add(new File(path).getParent());
            }
            //关闭Cursor
            cursor.close();

            Iterator<String> iterator = folderSet.iterator();
            for (; iterator.hasNext(); ) {
                String path = iterator.next();
                FileInfo info = new FileInfo();
                File file = new File(path);
                info.setPath(path);
                info.setName(file.getName());
                info.setModify(file.lastModified());
                lstFolder.add(info);
            }
        }
        long spend = System.currentTimeMillis() - start;
        Log.d(TAG, "getMediaFolder: query 10000 times by set,use " + spend + " mills.");
        return lstFolder;
    }
}
