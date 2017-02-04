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
    private static final String[] APK = new String[]{".apk"};
    private static final String[] COMPRESSED = new String[]{".zip"};
    private static final String[] DOCUMENT = new String[]{".doc", ".docx", "xls", "xlsx", "ppt", "pptx"};

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
        mAdapter.setData(getFileInfos(getMediaFolder(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
    }

    @OnClick(R.id.btn_get_audio_folder)
    void getAudioFolder() {
        mAdapter.setData(getFileInfos(getMediaFolder(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)));
    }

    @OnClick(R.id.btn_get_video_folder)
    void getVideoFolder() {
        mAdapter.setData(getFileInfos(getMediaFolder(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)));
    }

    @OnClick(R.id.btn_get_apk_file)
    void getApk() {
        //对于部分 apk，android 数据库中的 mimeType 可能为 null，故根据后缀进行过滤
        //对于部分不是 apk 而后缀被改为 apk，可在过滤完成后再次处理
        mAdapter.setData(getFileInfos(filterFileBySuffix(APK)));
        //mAdapter.setData(getFileInfos(filterFile(MediaDataBaseHelper.FilterType.MIME_TYPE, FileUtil.getMimeTypeBySuffix(".apk"))));
    }

    @OnClick(R.id.btn_get_compressed_file)
    void getCompressedFiles() {
        mAdapter.setData(getFileInfos(filterFileBySuffix(COMPRESSED)));
    }

    @OnClick(R.id.btn_get_document_file)
    void getDocuments() {
        mAdapter.setData(getFileInfos(filterFileBySuffix(DOCUMENT)));
    }

    @Override
    protected void process() {

    }

    private List<FileInfo> getFileInfos(Set<String> files) {
        List<FileInfo> lstFile = new ArrayList<>();
        Iterator<String> iterator = files.iterator();
        for (; iterator.hasNext(); ) {
            String path = iterator.next();
            FileInfo info = new FileInfo();
            File file = new File(path);
            info.setPath(path);
            info.setName(file.getName());
            info.setModify(file.lastModified() / 1000);
            lstFile.add(info);
        }
        return lstFile;
    }

    private Set<String> filterFileBySuffix(String[] filters) {
        return filterFile(MediaDataBaseHelper.FilterType.SUFFIX, filters);
    }

    private Set<String> filterFile(MediaDataBaseHelper.FilterType type, String[] filters) {
        Cursor cursor = MediaDataBaseHelper.filterFile(this, type, null, filters);
        if (cursor == null) {
            return null;
        }
        Set<String> fileSet = new HashSet<>();
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            fileSet.add(path);
        }
        //关闭Cursor
        cursor.close();
        return fileSet;
    }

    /**
     * 获取媒体文件父目录
     *
     * @param uri
     * @return
     */
    private Set<String> getMediaFolder(Uri uri) {
        Set<String> folderSet = new HashSet<>();
        Cursor cursor = getContentResolver().query(uri, PROJECTION, null, null, null);
        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext()) {
            //这样写可以减少3-4ms左右的时间
            String path = cursor.getString(0);
            Log.d(TAG, "getMediaFolder: path = " + path);
            //由于文件名中不可能出现斜杠/，故可以直接截取文件路径获取父目录
            //通过new File()方式获取父目录，执行1000次：15419ms
            //folderSet.add(new File(path).getParent());
            //通过截取路径获取父目录，执行1000次:14207ms
            folderSet.add(path.substring(0, path.lastIndexOf("/")));
        }
        //关闭Cursor
        cursor.close();
        return folderSet;
    }

}
