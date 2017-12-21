package com.littlejie.demo.modules.base.media.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.modules.base.media.interfaces.OnImageDataListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by littlejie on 2017/12/4.
 */

public class Camera1SurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = Camera1SurfaceView.class.getSimpleName();
    //自动对焦区域
    private static final int AUTO_FOCUS_AREA = 300;

    private Camera mCamera;
    private OnImageDataListener mOnImageDataListener;

    public Camera1SurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        SurfaceHolder holder = getHolder();
        //设置屏幕常亮
        holder.setKeepScreenOn(true);
        //设置 Surface 上预期的显示的 PixelFormat
        holder.setFormat(PixelFormat.RGBA_8888);
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        //初始化相机
        initCamera(holder);
    }

    private void initCamera(SurfaceHolder holder) {
        //判断系统是否支持拍照
        if (!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.d(TAG, "system not support camera.");
            return;
        }
        try {
            mCamera = Camera.open();
            //旋转90度，API<14、API>=14&API<24、API>24
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);

            int width = getWidth();
            int height = getHeight();
            Camera.Parameters parameters = mCamera.getParameters();
            Point previewResolution =
                    findBestSize(parameters.getSupportedPreviewSizes(), new Point(width, height));
            Point pictureResolution =
                    findBestSize(parameters.getSupportedPictureSizes(), new Point(width, height));

            Log.i(TAG, "preview size: " + previewResolution.x + "|" + previewResolution.y);
            Log.i(TAG, "picture size: " + pictureResolution.x + "|" + pictureResolution.y);

            //设置预览尺寸
            parameters.setPreviewSize(previewResolution.x, previewResolution.y);
            //设置图片尺寸
            parameters.setPictureSize(pictureResolution.x, pictureResolution.y);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Throw exception while open camera.Exception message : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        if (checkCameraOpen()) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
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
            mCamera.startPreview();
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
                        camera.startPreview();
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

    /**
     * 封装 Camera 对象的 takePicture 方法
     */
    public void takePicture() {
        if (!checkCameraOpen()) {
            return;
        }
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, final Camera camera) {
                if (mOnImageDataListener != null) {
                    mOnImageDataListener.onImageData(data);
                }
            }
        });
    }

    public void setOnImageDataListener(final OnImageDataListener onImageDataListener) {
        mOnImageDataListener = onImageDataListener;
    }
}
