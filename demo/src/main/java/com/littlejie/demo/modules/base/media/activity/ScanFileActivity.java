package com.littlejie.demo.modules.base.media.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.littlejie.demo.R;
import com.littlejie.demo.entity.FileInfo;
import com.littlejie.demo.modules.adapter.SimpleFileInfoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 扫描指定类型文件，这里简单扫描 MimeType = text/plain 或者 image/jpeg 的文件
 * Created by littlejie on 2016/12/28.
 */

public class ScanFileActivity extends AppCompatActivity implements View.OnClickListener {

    //CONTENT_URI 具体使用见 MediaObserverActivity
    private static final Uri CONTENT_URI = MediaStore.Files.getContentUri("external");

    private EditText mEdtFilter;
    private Button mBtnFilterSuffix;
    private Button mBtnFilterMimeType;
    private ListView mLv;
    private SimpleFileInfoAdapter mAdapter;

    private boolean isNewFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mEdtFilter = (EditText) findViewById(R.id.edt_filter);
        mBtnFilterSuffix = (Button) findViewById(R.id.btn_filter_by_suffix);
        mBtnFilterMimeType = (Button) findViewById(R.id.btn_filter_by_mimetype);
        mLv = (ListView) findViewById(R.id.lv);
        mAdapter = new SimpleFileInfoAdapter(this);
        mLv.setAdapter(mAdapter);

        mBtnFilterSuffix.setOnClickListener(this);
        mBtnFilterMimeType.setOnClickListener(this);
        findViewById(R.id.btn_new_file).setOnClickListener(this);
        findViewById(R.id.btn_not_new_file).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_filter_by_suffix:
                startFilter(MediaStore.Files.FileColumns.DATA);
                break;
            case R.id.btn_filter_by_mimetype:
                startFilter(MediaStore.Files.FileColumns.MIME_TYPE);
                break;
            case R.id.btn_new_file:
                isNewFile = true;
                startFilter("");
                break;
            case R.id.btn_not_new_file:
                isNewFile = false;
                startFilter("");
                break;
        }
    }

    private void startFilter(String arg) {
        String[] filters = null;
        //TODO 优化生成 filter 的方法
        if (MediaStore.Files.FileColumns.MIME_TYPE.equals(arg)) {//根据文件的 MimeType 字段进行过滤
            filters = getFilters();
        } else {//根据文件的 后缀 字段进行过滤
            filters = getFiltersBySuffix();
        }
        //获取 selection 语句
        String selection = getSelection(arg, filters);
        //进行数据库查询
        Cursor cursor = getContentResolver().query(CONTENT_URI, new String[]{MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.DATA,
                        MediaStore.MediaColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.PARENT},
                selection, filters, null);
        if (cursor == null) {
            return;
        }
        List<FileInfo> lstFile = new ArrayList<>();
        long start = System.currentTimeMillis();
        while (cursor.moveToNext()) {
            FileInfo file = new FileInfo();
            //此处 cursor.getString(index) 写法不正规，但却不失简便
            file.setName(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)));
            String path = cursor.getString(1);
            file.setPath(path);
            file.setModify(cursor.getLong(2));
            file.setParent(cursor.getInt(3));
            if (isNewFile) {
                file.setFile(new File(path));
            }
            lstFile.add(file);
        }
        Log.d("ScanFile", "spend time = " + (System.currentTimeMillis() - start));
        mAdapter.setData(lstFile);
    }

    private String[] getFilters() {
        String filter = mEdtFilter.getText().toString();
        if (TextUtils.isEmpty(filter)) {
            return null;
        }
        return filter.split(",");
    }

    /**
     * 进行后缀过滤需要用到通配符，故此处做特殊处理
     * 示例：select * from data where _data like '%.txt'
     *
     * @return
     */
    private String[] getFiltersBySuffix() {
        String filter = mEdtFilter.getText().toString();
        if (TextUtils.isEmpty(filter)) {
            return null;
        }
        String[] filters = filter.split(",");
        for (int i = 0; i < filters.length; i++) {
            filters[i] = "%." + filters[i].trim();
        }
        return filters;
    }

    /**
     * 生成 selection 语句
     *
     * @param arg
     * @param filters
     * @return
     */
    private String getSelection(String arg, String[] filters) {
        if (filters == null || filters.length == 0) {
            return null;
        }
        StringBuffer selection = new StringBuffer();
        for (int i = 0; i < filters.length; i++) {
            selection.append(arg + " like ?");
            if (i < filters.length - 1) {
                selection.append(" or ");
            }
        }
        return selection.toString();
    }
}
