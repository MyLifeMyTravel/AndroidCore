package com.littlejie.demo.modules.base.media;

import android.annotation.TargetApi;
import android.content.Context;
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
import com.littlejie.demo.utils.SDCardUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SD卡 相关测试，前提你得有张 SD卡
 * Created by littlejie on 2017/1/20.
 */
@Description(description = "测试Android SD卡相关，如：SD路径、SD是否挂载、SD卡读写授权、SD读写")
public class SDCardActivity extends BaseActivity {

    public static final int REQUEST_DOCUMENT_TREE = 1;

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
        File removableStorageDir = SDCardUtil.getRemovableStorageDir();
        if (removableStorageDir != null) {
            mTvContent.setText(removableStorageDir.getAbsolutePath());
        } else {
            ToastUtil.showDefaultToast("没有发现 SD卡 路径");
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.btn_open_document_tree)
    void openDocumentTree() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_DOCUMENT_TREE);
    }

    @OnClick(R.id.btn_create_document)
    void createDocumentFile() {
        if (!check()) {
            openDocumentTree();
            return;
        }
        String path = genRandomPath() + genRandomName();
        DocumentFile file = SDCardUtil.getDocumentFile(this, path, false);
        try {
            SDCardUtil.write2SDCard(this, file, new StringBufferInputStream("abc"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_copy_file)
    void copyFile() {
        if (!check()) {
            openDocumentTree();
            return;
        }
        String dest = SDCardUtil.getRemovableStorageDir().getAbsolutePath() + "/copy/beep.ogg";
        try {
            SDCardUtil.write2SDCard(this, dest, false, getResources().openRawResource(R.raw.beep));
            ToastUtil.showDefaultToast("文件拷贝成功");
        } catch (IOException e) {
            ToastUtil.showDefaultToast("文件拷贝失败");
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_delete_file)
    void deleteFile() {
        if (!check()) {
            openDocumentTree();
            return;
        }
//        String dest = SDCardUtil.getRemovableStorageDir().getAbsolutePath() + "/copy";
        String dest = SDCardUtil.getRemovableStorageDir().getAbsolutePath() + "/copy/beep.ogg";
//        String dest = SDCardUtil.getRemovableStorageDir().getAbsolutePath() + "/b.apk";
        try {
            //Document.delete()接口支持遍历删除
            boolean delete = SDCardUtil.delete(this, dest, true);
            ToastUtil.showDefaultToast("删除文件" + (delete ? "成功" : "失败"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String genRandomPath() {
        String root = SDCardUtil.getRemovableStorageDir().getAbsolutePath();
        String[] paths = {"/测试1", "/测试2", "/测试3/Test"};
        Random random = new Random();
        String path = root + paths[random.nextInt(3)];
        Log.d(TAG, "Random path = " + path);
        return path;
    }

    private String genRandomName() {
        String[] names = {"/文件1.txt", "/测试2", "/test.txt"};
        Random random = new Random();
        String name = names[random.nextInt(3)];
        Log.d(TAG, "Random name = " + name);
        return name;
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
    }

    @OnClick(R.id.btn_test_sdcard_access)
    void testSDCardAccess() {
        if (!check()) {
            return;
        }
        DocumentFile sd = DocumentFile.fromTreeUri(this, SDCardUtil.getSDCardAccessPermission(this).getUri());
        Log.d(TAG, "onClick: sd card path = " + sd.getUri().toString()
                + ";exists = " + sd.exists() + ";canWrite = " + sd.canWrite()
                + ";canRead = " + sd.canRead());
    }

    private boolean check() {
        if (!SDCardUtil.hasSDCard()) {
            ToastUtil.showDefaultToast("您尚未安装 SD卡");
            return false;
        }
        UriPermission sdcardAccessPermission = SDCardUtil.getSDCardAccessPermission(this);
        if (sdcardAccessPermission == null) {
            ToastUtil.showDefaultToast("尚未授予 SD卡 访问权限");
            return false;
        }
        return true;
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

        //对uri申请永久授权
        takePersistPermission(this, data.getData());
    }

    private void takePersistPermission(Context context, Uri uri) {
        context.getContentResolver().takePersistableUriPermission(uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }

}