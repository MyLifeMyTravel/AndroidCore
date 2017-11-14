package com.littlejie.demo.modules.base.media;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by littlejie on 2017/11/14.
 */
@Description(description = "调用系统相机拍照")
public class TakePhotoActivity extends BaseActivity {

    public static final int CODE_TAKE_PHOTO = 1;

    @BindView(R.id.iv_photo)
    ImageView ivPhoto;

    private Uri photoUri;
    private RxPermissions rxPermissions;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_take_photo;
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

    @OnClick(R.id.btn_take_photo)
    void takePhoto() {
        // Must be done during an initialization phase like onCreate
        rxPermissions
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            return;
                        }
                        photoUri = getMediaFileUri();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, CODE_TAKE_PHOTO);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showDefaultToast("获取权限失败");
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
            case CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //直接加载所拍原图
                    Glide.with(this).load(photoUri.getPath()).into(ivPhoto);
                }
                break;
        }
    }

    private Uri getMediaFileUri() {
        File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        //创建Media File
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", mediaFile);
        }
        return Uri.fromFile(mediaFile);
    }
}
