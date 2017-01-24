package com.littlejie.demo.modules.base.media.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.DeviceUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/1/20.
 */
@Description(description = "测试Android SD卡相关，如：SD路径、SD是否挂载、SD卡读写授权、SD读写")
public class SDCardActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_DOCUMENT_TREE = 1;
    @BindView(R.id.btn_write2sd_without_permission)
    Button mBtnWrite2SDCardWithoutPermission;
    @BindView(R.id.btn_open_document_tree)
    Button mBtnOpenDocumentTree;
    @BindView(R.id.btn_get_storage_volumes)
    Button mBtnGetStorageVolumes;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_sd_card;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {
        mBtnWrite2SDCardWithoutPermission.setOnClickListener(this);
        mBtnOpenDocumentTree.setOnClickListener(this);
        mBtnGetStorageVolumes.setOnClickListener(this);
    }

    @Override
    protected void process() {

    }

    @Override
    public void onClick(View v) {
        Uri treeUri = Uri.parse("content://com.android.externalstorage.documents/tree/0816-1A18%3A");
        switch (v.getId()) {
            case R.id.btn_get_sd_card_path:
                getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                break;
            case R.id.btn_write2sd_without_permission:
                write2SDCard(treeUri);
                break;
            case R.id.btn_open_document_tree:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(intent, REQUEST_DOCUMENT_TREE);
                break;
            case R.id.btn_get_storage_volumes:
                String[] paths = DeviceUtil.getStoragePath();
                for (String path : paths) {
                    Log.d(TAG, "onClick: path = " + path);
                }
                List<String> lstPath = DeviceUtil.getExtSDCardPath();
                for (String path : lstPath) {
                    File sd = new File(path);
                    Log.d(TAG, "onClick: sd card path = " + path + ";exists = " + sd.exists() + ";canWrite = " + sd.canWrite()
                            + ";canRead = " + sd.canRead()
                            + ";canExecute = " + sd.canExecute());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        Uri treeUri = data.getData();
        write2SDCard(treeUri);
    }

    private void write2SDCard(Uri treeUri) {
        DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri);

        // List all existing files inside picked directory
        for (DocumentFile file : pickedDir.listFiles()) {
            Log.d(TAG, "Found file " + file.getName() + " with size " + file.length());
        }

        try {
            // Create a new file and write into it
            DocumentFile newFile = pickedDir.createFile("text/plain", "My Novel");
            OutputStream out = getContentResolver().openOutputStream(newFile.getUri());
            out.write("A long time ago...".getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}