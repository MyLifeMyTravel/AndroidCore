package com.littlejie.demo.modules.base.media.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;

import com.littlejie.core.util.DisplayUtil;
import com.littlejie.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by littlejie on 2017/12/4.
 */

public class Camera1TextureView extends TextureView implements TextureView.SurfaceTextureListener, CameraTextureView.CameraInterface {

    private static final String TAG = Camera1TextureView.class.getSimpleName();
    //自动对焦区域
    private static final int AUTO_FOCUS_AREA = 300;

    private boolean isPreviewing = false;
    private Camera mCamera;
    private CameraTextureView.OnImageDataListener mOnImageDataListener;
    private Point mPreviewSize;
    private Rect mFocusArea;

    public Camera1TextureView(final Context context) {
        this(context, null);
    }

    public Camera1TextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置屏幕常亮
        setKeepScreenOn(true);
        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, final int width, final int height) {
        initCamera(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(final SurfaceTexture surface, final int width, final int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(final SurfaceTexture surface) {
        Log.d(TAG, "onSurfaceTextureDestroyed");
        if (checkCameraOpen()) {
            stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(final SurfaceTexture surface) {

    }

    private void initCamera(SurfaceTexture surface) {
        //判断系统是否支持拍照
        if (!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.d(TAG, "system not support camera.");
            return;
        }
        try {
            mCamera = Camera.open();
            //旋转90度，API<14、API>=14&API<24、API>24
            //Activity为横屏情况下，不用设置。
            //竖屏情况下顺时针旋转90才能正常显示
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewTexture(surface);

            int width = getWidth();
            int height = getHeight();
            Camera.Parameters parameters = mCamera.getParameters();
            mPreviewSize =
                    findBestSize(parameters.getSupportedPreviewSizes(), new Point(width, height));
            Point pictureResolution =
                    findBestSize(parameters.getSupportedPictureSizes(), new Point(width, height));

            Log.i(TAG, "preview size: " + mPreviewSize.x + "|" + mPreviewSize.y);
            Log.i(TAG, "picture size: " + pictureResolution.x + "|" + pictureResolution.y);

            //设置预览尺寸
            parameters.setPreviewSize(mPreviewSize.x, mPreviewSize.y);
            //设置图片尺寸
            parameters.setPictureSize(pictureResolution.x, pictureResolution.y);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(parameters);
            startPreview();
            //自动对焦
            doAutoFocus(DisplayUtil.getScreenWidth((Activity) getContext()) / 2, DisplayUtil.getScreenHeight((Activity) getContext()) / 2);
        } catch (Exception e) {
            Log.d(TAG, "Throw exception while open camera.Exception message : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean checkCameraOpen() {
        if (mCamera == null) {
            ToastUtil.showDefaultToast("相机打开失败，请授权或重试");
            return false;
        }
        return true;
    }

    /**
     * 根据 SurfaceView 的大小寻找最合适的预览尺寸
     *
     * @param sizes
     * @param resolution
     * @return
     */
    private Point findBestSize(List<Camera.Size> sizes, Point resolution) {
        int bestX = 0;
        int bestY = 0;
        int diff = Integer.MAX_VALUE;
        for (Camera.Size size : sizes) {
            int newX = size.width;
            int newY = size.height;

            int newDiff = Math.abs(newX - resolution.x)
                    + Math.abs(newY - resolution.y);
            int newDiff2 = Math.abs(newX - resolution.y)
                    + Math.abs(newY - resolution.x);
            newDiff = Math.min(newDiff, newDiff2);
            if (newDiff == 0) {
                bestX = newX;
                bestY = newY;
                break;
            } else if (newDiff < diff) {
                bestX = newX;
                bestY = newY;
                diff = newDiff;
            }
        }

        if (bestX > 0 && bestY > 0) {
            return new Point(bestX, bestY);
        } else {
            // Ensure that the camera resolution is a multiple of 8,
            // as the screen may not be.
            return new Point((resolution.x >> 3) << 3, (resolution.y >> 3) << 3);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        doAutoFocus((int) event.getX(), (int) event.getY());
        return super.onTouchEvent(event);
    }

    private void doAutoFocus(int x, int y) {
        if (!checkCameraOpen()) {
            return;
        }
        mCamera.cancelAutoFocus();

        if (mFocusArea != null && (y < mFocusArea.top || y > mFocusArea.bottom)) {
            y = (mFocusArea.top + mFocusArea.bottom) / 2;
        }

        Rect rect = calculateFocusArea(x, y);
        Log.d(TAG, "focus rect: " + rect.top + "|" + rect.bottom + "|"
                + rect.left + "|" + rect.right + "|");
        Camera.Parameters parameters = mCamera.getParameters();
        if (!Camera.Parameters.FOCUS_MODE_AUTO.equals(parameters.getFocusMode())) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        if (parameters.getMaxNumFocusAreas() > 0) {
            ArrayList<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(rect, 500));
            parameters.setFocusAreas(focusAreas);
        }

        try {
            mCamera.setParameters(parameters);
            startPreview();
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    Log.d(TAG, "onAutoFocus = " + success);
                    if (!camera.getParameters().getFocusMode().equals(
                            Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                        Camera.Parameters parameters = camera.getParameters();
                        parameters.setFocusMode(Camera.Parameters
                                .FOCUS_MODE_CONTINUOUS_PICTURE);
                        if (parameters.getMaxNumFocusAreas() > 0) {
                            parameters.setFocusAreas(null);
                        }
                        camera.setParameters(parameters);
                        startPreview();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / getWidth()) * 2000
                - 1000).intValue(), AUTO_FOCUS_AREA);
        int top = clamp(Float.valueOf((y / getHeight()) * 2000
                - 1000).intValue(), AUTO_FOCUS_AREA);

        return new Rect(left, top, left + AUTO_FOCUS_AREA, top + AUTO_FOCUS_AREA);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
            if (touchCoordinateInCameraReper > 0) {
                result = 1000 - focusAreaSize / 2;
            } else {
                result = -1000 + focusAreaSize / 2;
            }
        } else {
            result = touchCoordinateInCameraReper - focusAreaSize / 2;
        }
        return result;
    }

    private void startPreview() {
        if (!checkCameraOpen()) {
            return;
        }
        mCamera.startPreview();
        isPreviewing = true;
    }

    private void stopPreview() {
        if (!checkCameraOpen()) {
            return;
        }
        mCamera.stopPreview();
        isPreviewing = false;
    }

    @Override
    public boolean isPreviewing() {
        return isPreviewing;
    }

    /**
     * 封装 Camera 对象的 takePicture 方法
     */
    @Override
    public void takePicture() {
        if (!checkCameraOpen()) {
            return;
        }
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, final Camera camera) {
                stopPreview();
                if (mOnImageDataListener != null) {
                    mOnImageDataListener.onImageData(data, 90);
                }
            }
        });
    }

    @Override
    public void takePreview() {
        if (!checkCameraOpen()) {
            return;
        }
        startPreview();
    }

    @Override
    public void setOnImageDataListener(final CameraTextureView.OnImageDataListener onImageDataListener) {
        mOnImageDataListener = onImageDataListener;
    }

    @Override
    public void setFocusArea(final Rect focusArea) {
        mFocusArea = focusArea;
    }

}
