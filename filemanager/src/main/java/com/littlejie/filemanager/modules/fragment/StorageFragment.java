package com.littlejie.filemanager.modules.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.littlejie.core.base.BaseFragment;
import com.littlejie.core.util.FileUtil;
import com.littlejie.filemanager.Impl.IFileAction;
import com.littlejie.filemanager.R;
import com.littlejie.filemanager.entity.FileInfo;
import com.littlejie.filemanager.modules.adapter.FileAdapter;
import com.littlejie.filemanager.util.Constant;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/1/10.
 */

public class StorageFragment extends BaseFragment implements IFileAction {

    @BindView(R.id.tv_path)
    TextView mTvPath;
    @BindView(R.id.lv_file)
    ListView mLvFile;
    private FileAdapter mAdapter;

    private String mPath;
    private List<FileInfo> mFiles;

    public static StorageFragment newInstance(String path) {
        Bundle args = new Bundle();

        StorageFragment fragment = new StorageFragment();
        args.putString(Constant.EXTRA_PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getPageLayoutID() {
        return R.layout.fragment_storage;
    }

    @Override
    protected void initData() {
        mPath = getArguments().getString(Constant.EXTRA_PATH, Constant.ROOT);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mAdapter = new FileAdapter(getActivity());
        mLvFile.setAdapter(mAdapter);
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process(Bundle savedInstanceState) {
        mAdapter.setData(list(mPath, null));
    }

    @Override
    public List<FileInfo> list(String path, FileFilter filter) {
        File file = new File(path);
        File[] files = file.listFiles(filter);
        if (files == null) {
            return null;
        }
        List<FileInfo> lstFile = new ArrayList<>();
        for (File tmp : files) {
            FileInfo info = new FileInfo();
            info.setName(tmp.getName());
            info.setFile(tmp);
            info.setLastModify(tmp.lastModified());
            info.setMimeType(FileUtil.getFileMimeType(tmp.getAbsolutePath()));
            info.setPath(tmp.getAbsolutePath());
            info.setSize(file.length());
            lstFile.add(info);
        }
        return lstFile;
    }

    @Override
    public boolean createFolder(String path, String folder) {
        return false;
    }

    @Override
    public boolean move(String src, String dest) {
        return false;
    }

    @Override
    public boolean copy(String src, String dest) {
        return false;
    }

    @Override
    public boolean rename(String src, String dest) {
        return false;
    }

    @Override
    public boolean delete(String path) {
        return false;
    }
}
