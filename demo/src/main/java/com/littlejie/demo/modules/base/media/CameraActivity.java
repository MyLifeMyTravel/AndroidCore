package com.littlejie.demo.modules.base.media;

import android.graphics.Rect;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.DisplayUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.base.media.view.CameraTextureView;

import butterknife.BindView;
import butterknife.OnClick;

@Description(description = "使用 Camera 实现预览")
public class CameraActivity extends BaseActivity {

    @BindView(R.id.content_frame)
    FrameLayout mFrameLayout;
    @BindView(R.id.btn_take_photo)
    Button mBtnTakePhoto;

    private CameraTextureView.CameraInterface mCameraInterface;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_camera;
    }

    @Override
    protected void initData() {
        Rect focusArea = new Rect(0, DisplayUtil.getScreenHeight(this) / 4
                , DisplayUtil.getScreenWidth(this), DisplayUtil.getScreenHeight(this) * 3 / 4);
        CameraTextureView cameraTextureView = new CameraTextureView.Builder(this)
                .setFocusArea(focusArea)
                .setOnImageDataListener(new CameraTextureView.OnImageDataListener() {
                    @Override
                    public void onImageData(final byte[] data, final int rotateDegree) {
                        mBtnTakePhoto.setText("预览");
                    }
                })
                .create();
        mCameraInterface = cameraTextureView.getCameraInterface();
    }

    @Override
    protected void initView() {
        mFrameLayout.addView((View) mCameraInterface);
    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.btn_take_photo)
    void takePhoto() {
        if (!mCameraInterface.isPreviewing()) {
            mCameraInterface.takePreview();
            mBtnTakePhoto.setText("拍照");
        } else {
            mCameraInterface.takePicture();
        }
    }

    @Override
    protected void process() {

    }
}
