package com.littlejie.demo.modules.base.media;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.entity.FileInfo;
import com.littlejie.demo.modules.adapter.SimpleFileInfoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描指定类型文件，这里简单扫描 MimeType = text/plain 或者 image/jpeg 的文件
 * Created by littlejie on 2016/12/28.
 */
@Description(description = "过滤指定类型的文件")
public class FilterFileActivity extends BaseActivity {

    //CONTENT_URI 具体使用见 MediaObserverActivity
    private static final Uri CONTENT_URI = MediaStore.Files.getContentUri("external");

    @BindView(R.id.edt_filter)
    EditText mEdtFilter;
    @BindView(R.id.lv)
    ListView mLv;
    private SimpleFileInfoAdapter mAdapter;

    private boolean isNewFile;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_scan;
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

    @OnClick(R.id.btn_filter_by_suffix)
    void filterFileBySuffix() {
        startFilter(MediaStore.Files.FileColumns.DATA);
    }

    @OnClick(R.id.btn_filter_by_mimetype)
    void filterFileByMimeType() {
        startFilter(MediaStore.Files.FileColumns.MIME_TYPE);
    }

    @OnClick(R.id.btn_new_file)
    void filterNewFile() {
        isNewFile = true;
        startFilter("");
    }

    @OnClick(R.id.btn_not_new_file)
    void filterOldFile() {
        isNewFile = false;
        startFilter("");
    }

    @Override
    protected void process() {

    }

    public void startFilter(String arg) {
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
        StringBuilder selection = new StringBuilder();
        for (int i = 0; i < filters.length; i++) {
            selection.append(arg).append(" like ?");
            if (i < filters.length - 1) {
                selection.append(" or ");
            }
        }
        return selection.toString();
    }
}
