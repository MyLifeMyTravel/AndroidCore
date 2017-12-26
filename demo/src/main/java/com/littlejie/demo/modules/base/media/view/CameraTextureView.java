package com.littlejie.demo.modules.base.media.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;

/**
 * Created by littlejie on 2017/12/25.
 */

public class CameraTextureView {

    public interface OnImageDataListener {

        /**
         * 拍照完成回调
         *
         * @param data         照片数据
         * @param rotateDegree 旋转角度
         */
        void onImageData(byte[] data, int rotateDegree);
    }

    public interface CameraInterface {

        /**
         * 是否处于预览状态
         *
         * @return
         */
        boolean isPreviewing();

        /**
         * 拍照
         */
        void takePicture();

        /**
         * 预览
         */
        void takePreview();

        /**
         * 设置拍照数据回调监听
         *
         * @param listener
         */
        void setOnImageDataListener(CameraTextureView.OnImageDataListener listener);

        void setFocusArea(Rect focusArea);
    }

    private Context mContext;
    private OnImageDataListener mOnImageDataListener;
    private CameraInterface mCameraInterface;

    private CameraTextureView(Builder builder) {
        mContext = builder.mContext;
        mOnImageDataListener = builder.mOnImageDataListener;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraInterface = new Camera2TextureView(mContext);
        } else {
            mCameraInterface = new Camera1TextureView(mContext);
        }
        mCameraInterface.setFocusArea(builder.mFocusArea);
        mCameraInterface.setOnImageDataListener(mOnImageDataListener);
    }

    public CameraInterface getCameraInterface() {
        return mCameraInterface;
    }

    public static class Builder {

        private Context mContext;
        private Rect mFocusArea;
        private OnImageDataListener mOnImageDataListener;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setFocusArea(Rect focusArea) {
            mFocusArea = focusArea;
            return this;
        }

        public Builder setOnImageDataListener(OnImageDataListener listener) {
            mOnImageDataListener = listener;
            return this;
        }

        public CameraTextureView create() {
            return new CameraTextureView(this);
        }
    }

}
