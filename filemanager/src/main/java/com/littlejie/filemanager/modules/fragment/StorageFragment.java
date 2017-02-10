package com.littlejie.filemanager.modules.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.littlejie.core.base.BaseFragment;
import com.littlejie.core.util.MediaUtil;
import com.littlejie.filemanager.R;
import com.littlejie.filemanager.impl.IFileAction;
import com.littlejie.filemanager.modules.adapter.FileAdapter;
import com.littlejie.filemanager.util.Constant;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.OnItemClick;

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
    private File[] mFiles;

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

    @OnItemClick(R.id.lv_file)
    void onItemClick(int position) {
        String path = mFiles[position].getAbsolutePath();
        //如果是文件夹，则创建一个 StorageFragment 用于显示文件夹内容
        //如果是文件，则发送意图，选择合适的 app 打开
        if (mFiles[position].isDirectory()) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, StorageFragment.newInstance(path))
                    .addToBackStack(path)
                    .commit();
        } else {
            MediaUtil.openFile(getContext(), path);
        }
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        mFiles = list(mPath, Constant.HIDDEN_FILE_FILTER);
        mAdapter.setData(mFiles);
    }

    @Override
    public File[] list(String path, FileFilter filter) {
        File file = new File(path);
        File[] files = file.listFiles(filter);
        if (files == null) {
            return null;
        }
        Arrays.sort(files, new CustomComparator());
        return files;
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

    private class CustomComparator implements Comparator<File> {

        @Override
        public int compare(File file1, File file2) {
            /**
             * 1.先比较文件夹 （文件夹在文件的顺序之上）
             * 2.以A-Z的字典排序
             * 3.比较文件夹和文件
             * 4.比较文件和文件夹
             */
            if (file1.isDirectory() && file2.isDirectory()) {
                return file1.getName().compareToIgnoreCase(file2.getName());
            } else {
                if (file1.isDirectory() && !file2.isDirectory()) {
                    return -1;
                } else if (!file1.isDirectory() && file2.isDirectory()) {
                    return 1;
                } else {
                    return file1.getName().compareToIgnoreCase(file2.getName());
                }
            }
        }
    }

}
