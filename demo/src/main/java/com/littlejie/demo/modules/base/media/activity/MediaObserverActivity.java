package com.littlejie.demo.modules.base.media.activity;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.littlejie.demo.R;
import com.littlejie.demo.entity.FileInfo;
import com.littlejie.demo.modules.adapter.SimpleFileInfoAdapter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 多媒体文件观察
 */
public class MediaObserverActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MediaObserverActivity.class.getSimpleName();
    private static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String FILE_TMP = ROOT + "/tmp1.txt";
    //如果一个目录下有 .nomedia 目录，那么这个目录下的文件将被MediaScanner忽略(不包括该目录)
    //当然把 .nomedia 目录放在系统存储的根目录下是不行的
    private static final String DIR_IGNORED = ROOT + "/Ignored By MediaScanner";
    private static final String DIR_NO_MEDIA = DIR_IGNORED + "/.nomedia";
    private static final String FILE_IGNORED1 = DIR_IGNORED + "/ignore1.txt";
    private static final String FILE_IGNORED2 = DIR_IGNORED + "/ignore2.txt";
    //获取外部存储上图片的Uri，相当于 content://media/external/images/media
    //MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    //获取外部存储上音频的Uri，相当于 content://media/external/audio/media
    //MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    //获取外部存储上视频的Uri，相当于 content://media/external/video/media
    //MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    //获取外部存储上的文件Uri，一般用于上述几种类型处理不了的情况，相当于 content://media/external/files/
    private static final Uri CONTENT_URI = MediaStore.Files.getContentUri("external");
    private Handler mHandler = new Handler();
    private MediaStoreObserver mMediaStoreObserver = new MediaStoreObserver(mHandler);

    private ListView mLv;
    private SimpleFileInfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_observer);

        mLv = (ListView) findViewById(R.id.lv);
        mAdapter = new SimpleFileInfoAdapter(this);
        mLv.setAdapter(mAdapter);

        findViewById(R.id.btn_scan_root).setOnClickListener(this);
        findViewById(R.id.btn_scan_root_without_db).setOnClickListener(this);
        findViewById(R.id.btn_create_file).setOnClickListener(this);
        findViewById(R.id.btn_delete_file).setOnClickListener(this);
        findViewById(R.id.btn_delete_file_without_broad).setOnClickListener(this);
        findViewById(R.id.btn_ignore_scan).setOnClickListener(this);
        getContentResolver().registerContentObserver(CONTENT_URI, false, mMediaStoreObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mMediaStoreObserver);
    }

    class MyCo implements Comparator<FileInfo> {

        @Override
        public int compare(FileInfo o1, FileInfo o2) {
            if (o1.getModify() >= o2.getModify()) {
                return -1;
            }
            return 1;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan_root:
                sendScanFileBroadcast(ROOT);
                break;
            case R.id.btn_scan_root_without_db:
//                Collection<File> files = FileUtils.listFilesAndDirs(Environment.getExternalStorageDirectory(),
//                TrueFileFilter.TRUE, TrueFileFilter.INSTANCE);
                File[] files = Environment.getExternalStorageDirectory().listFiles();
                List<FileInfo> lstFile = new ArrayList<>();
                for (File file : files) {
                    FileInfo info = new FileInfo();
                    info.setId(0);
                    info.setModify(file.lastModified() / 1000);
                    info.setName(file.getName());
                    info.setPath(file.getAbsolutePath());
                    info.setParent(0);
                    lstFile.add(info);
                }
                Collections.sort(lstFile, new MyCo());
                mAdapter.setData(lstFile);
                break;
            case R.id.btn_create_file:
                try {
                    //记得添加文件读写权限，此处使用 Apache commons-io 库来实现文件读写
                    FileUtils.write(new File(FILE_TMP), "test", Charset.forName("UTF-8"));
                    Toast.makeText(this, "创建文件成功", Toast.LENGTH_SHORT).show();
                    sendScanFileBroadcast(FILE_TMP);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "创建文件失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_delete_file:
                try {
                    FileUtils.forceDelete(new File(FILE_TMP));
                    Toast.makeText(this, "删除文件成功", Toast.LENGTH_SHORT).show();
                    sendScanFileBroadcast(FILE_TMP);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "删除文件失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_delete_file_without_broad:
                try {
                    FileUtils.forceDelete(new File(FILE_TMP));
                    Toast.makeText(this, "删除文件成功", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "删除文件失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_ignore_scan:
                try {
                    FileUtils.forceMkdir(new File(DIR_IGNORED));
                    FileUtils.forceMkdir(new File(DIR_NO_MEDIA));
                    FileUtils.write(new File(FILE_IGNORED1), "ignore", Charset.forName("UTF-8"));
                    FileUtils.write(new File(FILE_IGNORED2), "ignore", Charset.forName("UTF-8"));
                    Toast.makeText(this, "创建文件夹成功，但是系统不会扫描该目录", Toast.LENGTH_SHORT).show();
                    sendScanFileBroadcast(DIR_IGNORED);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "创建文件夹失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 发送扫描文件的广播，file 路径为根目录时无效
     *
     * @param file
     */
    private void sendScanFileBroadcast(String file) {
        //Uri 必须为 file://+文件路径
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath()));
        sendBroadcast(intent);
    }

    /**
     * 多媒体文件内容观察者，当注册的uri发生改变时进行回调
     * 需要传递 Handler，用于 UI 更新，建议传递主线程的 Handler
     */
    private class MediaStoreObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MediaStoreObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(TAG, "MediaStore.Files has changed.");
            //查询外部存储数据库信息，关于数据库字段可以查看 MediaStore 子类相关的 Columns
            //或者通过 adb 导出数据库查看，数据库位于 com.android.providers.media/databases 目录下
            //按修改时间降序排列
            Cursor cursor = getContentResolver().query(CONTENT_URI,
                    new String[]{MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.DATA,
                            MediaStore.MediaColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.PARENT},
                    "parent = ?",
                    new String[]{"0"},
                    MediaStore.MediaColumns.DATE_MODIFIED + " desc");
            if (cursor == null) {
                return;
            }
            List<FileInfo> lstFile = new ArrayList<>();
            while (cursor.moveToNext()) {
                FileInfo file = new FileInfo();
                //此处 cursor.getString(index) 写法不正规，但却不失简便
                file.setName(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)));
                file.setPath(cursor.getString(1));
                file.setModify(cursor.getLong(2));
                file.setParent(cursor.getInt(3));
                lstFile.add(file);
            }
            mAdapter.setData(lstFile);
        }
    }

}
