package com.littlejie.filemanager.modules.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.littlejie.core.base.BaseFragment;
import com.littlejie.core.util.FileUtil;
import com.littlejie.filemanager.R;
import com.littlejie.filemanager.constant.ExtraConstant;
import com.littlejie.filemanager.constant.FilterConstant;
import com.littlejie.filemanager.constant.PathConstant;
import com.littlejie.filemanager.impl.IFileAction;
import com.littlejie.filemanager.ui.adapter.FileAdapter;
import com.littlejie.filemanager.util.CustomComparator;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

/**
 * Created by littlejie on 2017/2/12.
 */

public class FileListFragment extends BaseFragment implements IFileAction {

    @BindView(R.id.lv_file)
    ListView mLvFile;
    private FileAdapter mAdapter;

    private String mPath;
    private File[] mFiles;

    public static FileListFragment newInstance(String path) {

        Bundle args = new Bundle();

        FileListFragment fragment = new FileListFragment();
        args.putString(ExtraConstant.EXTRA_PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    public static FileListFragment newInstance(File[] files) {

        Bundle args = new Bundle();

        FileListFragment fragment = new FileListFragment();
        args.putSerializable(ExtraConstant.EXTRA_FILES, files);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getPageLayoutID() {
        return R.layout.fragment_file_list;
    }

    @Override
    protected void initData() {
        mPath = getArguments().getString(ExtraConstant.EXTRA_PATH, PathConstant.ROOT);
        mFiles = (File[]) getArguments().getSerializable(ExtraConstant.EXTRA_FILES);
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
            //因为每个Fragment内部都维护着一个Fragment栈
            getParentFragment().getChildFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, FileListFragment.newInstance(path))
                    .addToBackStack(path)
                    .commit();
        } else {
            FileUtil.openFile(getContext(), path);
        }
    }

    @OnItemLongClick(R.id.lv_file)
    boolean onItemLongClick(int position) {
        mAdapter.showCheckBox(true, position);
        return true;
    }

//    private static final String[] ACTION_FILE = {"复制", "移动", "重命名", "删除"};
//
//    @OnItemLongClick(R.id.lv_file)
//    boolean onItemLongClick(int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
//                .setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, ACTION_FILE),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ToastUtil.showDefaultToast(ACTION_FILE[which]);
//                            }
//                        });
//        builder.show();
//        return true;
//    }

    @Override
    protected void process(Bundle savedInstanceState) {
        if (mFiles == null) {//当mFiles为空，即选择传递path进来时执行该步骤
            mFiles = list(mPath, FilterConstant.HIDDEN_FILE_FILTER);
        }
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
    public boolean mkdirs(String path, String dir) {
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
