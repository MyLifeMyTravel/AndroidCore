package com.littlejie.demo.modules.base.media;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.entity.FileInfo;
import com.littlejie.demo.modules.adapter.SimpleFileInfoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * 简单文件管理器
 */
public class SimpleFileManagerActivity extends BaseActivity {

    private static final Uri CONTENT_URI = MediaStore.Files.getContentUri("external");
    private static final String[] PROJECTION = new String[]{BaseColumns._ID, MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.PARENT,
            MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.MIME_TYPE};
    //SELECT * FROM files WHERE parent = 0 or parent = (SELECT _id from files WHERE "_data" like '/storage/emulated/0')
    private static final String ROOT_SELECTION = "parent = ? or parent = (SELECT _id from files WHERE _data like '"
            + Environment.getExternalStorageDirectory().getAbsolutePath() + "')";
    private static final String OTHER_SELECTION = "parent = ?";

    @BindView(R.id.lv)
    ListView mLvFile;
    private SimpleFileInfoAdapter mAdapter;
    private List<FileInfo> mLstFile;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_simple_file_manager;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mAdapter = new SimpleFileInfoAdapter(this);
        mLvFile.setAdapter(mAdapter);
    }

    @Override
    protected void initViewListener() {

    }

    @OnItemClick(R.id.lv)
    void onItemClick(int position) {
        FileInfo info = mLstFile.get(position);
        Intent intent = new Intent(SimpleFileManagerActivity.this, SimpleFileManagerActivity.class);
        intent.putExtra("parent", info.getId());
        startActivity(intent);
    }

    @Override
    protected void process() {
        listFolderItem(getIntent().getLongExtra("parent", 0));
    }

    private void listFolderItem(long parent) {
        Cursor cursor = getContentResolver().query(CONTENT_URI, PROJECTION,
                parent == 0 ? ROOT_SELECTION : OTHER_SELECTION,
                new String[]{String.valueOf(parent)},
                null);
        if (cursor == null) {
            return;
        }
        mLstFile = new ArrayList<>();
        long start = System.currentTimeMillis();
        while (cursor.moveToNext()) {
            FileInfo file = new FileInfo();
            //此处 cursor.getString(index) 写法不正规，但却不失简便
            file.setId(cursor.getLong(0));
            file.setName(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)));
            //这样写可以减少10ms左右的时间
            String path = cursor.getString(2);
            file.setPath(path);
            //如果把new File放到FileInfo中执行，可以减少5ms左右，但是效果不稳定
//            file.setFile(new File(path));
            file.setModify(cursor.getLong(3));
            file.setParent(cursor.getInt(4));
            mLstFile.add(file);
        }
        Log.d("ListFile", "spend time = " + (System.currentTimeMillis() - start));
        mAdapter.setData(mLstFile);
    }
}
