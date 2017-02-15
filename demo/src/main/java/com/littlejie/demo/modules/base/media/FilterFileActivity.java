package com.littlejie.demo.modules.base.media;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.entity.FileInfo;
import com.littlejie.demo.ui.adapter.SimpleFileInfoAdapter;
import com.littlejie.core.util.MediaUtil;

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

    private static final String[] PROJECTION = new String[]{MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.PARENT};
    @BindView(R.id.edt_filter)
    EditText mEdtFilter;
    @BindView(R.id.lv)
    ListView mLv;
    private SimpleFileInfoAdapter mAdapter;

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
        startFilter(MediaUtil.FilterType.SUFFIX);
    }

    @OnClick(R.id.btn_filter_by_mimetype)
    void filterFileByMimeType() {
        startFilter(MediaUtil.FilterType.MIME_TYPE);
    }

    @Override
    protected void process() {

    }

    /**
     * 文件过滤
     *
     * @param filterType 文件过滤的类型，暂时只支持根据文件后缀和文件的mimeType进行过滤
     */
    public void startFilter(MediaUtil.FilterType filterType) {
        Cursor cursor = MediaUtil.filterFile(this, filterType, PROJECTION, mEdtFilter.getText().toString());
        if (cursor == null) {
            return;
        }
        List<FileInfo> lstFile = new ArrayList<>();
        long start = System.currentTimeMillis();
        while (cursor.moveToNext()) {
            FileInfo file = new FileInfo();
            file.setName(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)));
            //此处 cursor.getString(index) 写法不正规，但却不失简便
            String path = cursor.getString(1);
            file.setPath(path);
            file.setModify(cursor.getLong(2));
            file.setParent(cursor.getInt(3));
            //如果此时调用setFile()，视存储中文件多少，相应的增加耗时
            //file.setFile(new File(path));
            lstFile.add(file);
        }
        cursor.close();
        Log.d("ScanFile", "spend time = " + (System.currentTimeMillis() - start));
        mAdapter.setData(lstFile);
    }

}
