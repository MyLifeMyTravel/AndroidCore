package com.littlejie.filemanager.modules.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.littlejie.core.base.BaseFragment;
import com.littlejie.filemanager.R;
import com.littlejie.filemanager.command.AppCommand;
import com.littlejie.filemanager.constant.ExtraConstant;
import com.littlejie.filemanager.impl.OnBackPressedListener;

import java.io.File;

/**
 * Created by littlejie on 2017/1/10.
 */

public class StorageFragment extends BaseFragment implements OnBackPressedListener {

    private static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

    private File[] mFiles;

    public static StorageFragment newInstance() {

        Bundle args = new Bundle();

        StorageFragment fragment = new StorageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static StorageFragment newInstance(File[] files) {
        Bundle args = new Bundle();

        StorageFragment fragment = new StorageFragment();
        args.putSerializable(ExtraConstant.EXTRA_FILES, files);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getPageLayoutID() {
        return R.layout.fragment_storage;
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mFiles = (File[]) getArguments().getSerializable(ExtraConstant.EXTRA_FILES);
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
    }

    @Override
    protected void initViewListener() {
        AppCommand.addOnBackPressedListener(this);
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, mFiles == null
                        ? FileListFragment.newInstance(ROOT)
                        : FileListFragment.newInstance(mFiles))
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppCommand.removeOnBackPressedListener(this);
    }

    @Override
    public boolean onBackPressed() {
        //如果嵌套Fragment的回退栈不为0，则返回按钮按下事件被消费
        int count = getChildFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            return false;
        }
        getChildFragmentManager().popBackStack();
        return true;
    }
}
