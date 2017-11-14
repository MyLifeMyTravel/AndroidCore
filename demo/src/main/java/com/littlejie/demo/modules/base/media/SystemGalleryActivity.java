package com.littlejie.demo.modules.base.media;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by littlejie on 2017/11/14.
 */
@Description(description = "调用系统相册选图片")
public class SystemGalleryActivity extends BaseActivity {

    private static final int CODE_SELECT_IMAGE = 1;

    @BindView(R.id.iv_photo)
    ImageView ivPhoto;

    private RxPermissions rxPermissions;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_system_gallery;
    }

    @Override
    protected void initData() {
        rxPermissions = new RxPermissions(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.btn_select_photo)
    void selectPhoto() {
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            return;
                        }
                        Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(albumIntent, CODE_SELECT_IMAGE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void process() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    selectPhoto(data);
                }
                break;
        }
    }

    private void selectPhoto(Intent data) {
        if (data == null) {
            return;
        }
        Uri selectImageUri = data.getData();
        if (selectImageUri == null) {
            return;
        }
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectImageUri, filePathColumn, null, null, null);
        if (cursor == null) {
            return;
        }
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Glide.with(this).load(picturePath).into(ivPhoto);
    }
}
