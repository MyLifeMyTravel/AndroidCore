package com.littlejie.demo.modules.base.media;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.base.media.interfaces.OnImageDataListener;
import com.littlejie.demo.modules.base.media.view.Camera2TextureView;

import butterknife.BindView;
import butterknife.OnClick;

@Description(description = "使用 Camera2 实现预览")
public class Camera2Activity extends BaseActivity {

    @BindView(R.id.surface_view)
    Camera2TextureView mSurfaceView;
    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R.id.btn_take_photo)
    Button mBtnTakePhoto;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_camera2;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {
        mSurfaceView.setOnImageDataListener(new OnImageDataListener() {
            @Override
            public void onImageData(final byte[] data) {
                long currentTimeInMills = System.currentTimeMillis();
                Log.d(TAG, "current timeInMills = " + currentTimeInMills);
                if (data != null && data.length != 0) {
                    mSurfaceView.setVisibility(View.GONE);
                    mIvPhoto.setVisibility(View.VISIBLE);
                    mBtnTakePhoto.setText("预览");
                    //将 data 数据转换成 Bitmap
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    mIvPhoto.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length, options));
                }
                Log.d(TAG, "spend time = " + (System.currentTimeMillis() - currentTimeInMills));
            }
        });
    }

    @OnClick(R.id.btn_take_photo)
    void takePhoto() {
        if (mSurfaceView.isPreviewing()) {
            mSurfaceView.takePicture();
        } else {
            mSurfaceView.setVisibility(View.VISIBLE);
            mIvPhoto.setVisibility(View.GONE);
            mSurfaceView.takePreview();
            mBtnTakePhoto.setText("拍照");
        }
    }

    @Override
    protected void process() {

    }
}
