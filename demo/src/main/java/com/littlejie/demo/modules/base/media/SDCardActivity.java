package com.littlejie.demo.modules.base.media;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.DeviceUtil;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.utils.DialogUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SD卡 相关测试，前提你得有张 SD卡
 * Created by littlejie on 2017/1/20.
 */
@Description(description = "测试Android SD卡相关，如：SD路径、SD是否挂载、SD卡读写授权、SD读写")
public class SDCardActivity extends BaseActivity {

    public static final int REQUEST_DOCUMENT_TREE = 1;

    //treeUri 需根据实际测试进行修改
    //treeUri 形如 Uri.parse("content://com.android.externalstorage.documents/tree/0816-1A18%3A")
    private Uri treeUri;

    @BindView(R.id.tv_content)
    TextView mTvContent;

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
    }

    @OnClick(R.id.btn_get_all_storage_path)
    void getAllStoragePath() {
        mTvContent.setText(Arrays.toString(DeviceUtil.getStoragePath()));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.btn_get_sd_card_path)
    void getSDCardPath() {
        List<String> lstExtSDCardPath = DeviceUtil.getExtSDCardPath();
        if (lstExtSDCardPath == null) {
            ToastUtil.showDefaultToast("没有发现 SD卡 路径");
            return;
        }
        mTvContent.setText(lstExtSDCardPath.toString());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.btn_open_document_tree)
    void openDocumentTree() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_DOCUMENT_TREE);
    }

    @OnClick(R.id.btn_take_persist_permission)
    void takePersistPermission() {
        if (treeUri == null) {
            ToastUtil.showDefaultToast("请先授予SD卡访问权限");
            return;
        }
        getContentResolver().takePersistableUriPermission(treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }

    @OnClick(R.id.btn_get_persist_permissions)
    void getPersistPermission() {
        List<UriPermission> lstPermissions = getContentResolver().getPersistedUriPermissions();

        StringBuilder sb = new StringBuilder();
        for (UriPermission permission : lstPermissions) {
            sb.append(permission.getUri().toString()).append("\n");
            Log.d(TAG, "getPersistPermission: permission = " + permission.getUri().toString());
        }
        mTvContent.setText(sb.toString());
    }

    @OnClick(R.id.btn_release_persist_permission)
    void releasePersistPermission() {
        final List<UriPermission> lstPermissions = getContentResolver().getPersistedUriPermissions();

        List<String> lstUri = new ArrayList<>();
        for (UriPermission permission : lstPermissions) {
            lstUri.add(permission.getUri().toString());
        }
        String[] uris = new String[lstUri.size()];
        DialogUtil.showSingleChoiceDialog(this, "释放永久授权的Uri", lstUri.toArray(uris), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getContentResolver().releasePersistableUriPermission(lstPermissions.get(which).getUri(),
                        Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        });

//        DialogUtil.showSingleChoiceDialog(this, );
    }

    @OnClick(R.id.btn_get_storage_volumes)
    void getStorageVolumesInfo() {
        String[] paths = DeviceUtil.getStoragePath();
        if (paths == null) {
            Log.d(TAG, "getStorageVolumesInfo: paths is null");
            return;
        }
        for (String path : paths) {
            Log.d(TAG, "onClick: path = " + path);
        }
        List<String> lstPath = DeviceUtil.getExtSDCardPath();
        if (lstPath == null) {
            Log.d(TAG, "getStorageVolumesInfo: lstPath is null");
            return;
        }
        for (String path : lstPath) {
            File sd = new File(path);
            Log.d(TAG, "onClick: sd card path = " + path + ";exists = " + sd.exists() + ";canWrite = " + sd.canWrite()
                    + ";canRead = " + sd.canRead()
                    + ";canExecute = " + sd.canExecute());
        }
    }

    @Override
    protected void process() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        treeUri = data.getData();
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
            if (out != null) {
                out.write("A long time ago...".getBytes());
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}