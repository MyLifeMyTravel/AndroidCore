package com.littlejie.demo.modules.base.media.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;

import com.littlejie.demo.modules.base.media.interfaces.OnImageDataListener;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Camera2 Api 实现的相机
 * Created by littlejie on 2017/12/6.
 */
@TargetApi(value = Build.VERSION_CODES.LOLLIPOP)
public class Camera2SurfaceView extends TextureView implements TextureView.SurfaceTextureListener {

    private static final String TAG = Camera2SurfaceView.class.getSimpleName();
    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * Camera state: Showing camera preview.
     */
    private static final int STATE_PREVIEW = 0;

    /**
     * Camera state: Waiting for the focus to be locked.
     */
    private static final int STATE_WAITING_LOCK = 1;

    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;

    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;

    /**
     * Camera state: Picture was taken.
     */
    private static final int STATE_PICTURE_TAKEN = 4;

    //后置摄像头ID
    private static final String CAMERA_ID = String.valueOf(CameraCharacteristics.LENS_FACING_FRONT);

    //摄像头管理，用于检测、打开摄像头
    //通过CameraManager.getCameraCharacteristics(CameraID)获取摄像头相关信息
    private CameraManager mCameraManager;
    //CameraDevice对象，相当于Camera对象
    private CameraDevice mCameraDevice;
    //摄像头参数
    private CameraCharacteristics mCameraCharacteristics;

    private CameraCaptureSession mCameraCaptureSession;
    //拍照或预览是，调用CameraCaptureSession需要一个CaptureRequest对象
    private CaptureRequest mPreviewRequest;
    private CaptureRequest.Builder mPreviewRequestBuilder;

    private final CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {

        @Override
        public void onCaptureStarted(@NonNull final CameraCaptureSession session,
                                     @NonNull final CaptureRequest request,
                                     final long timestamp, final long frameNumber) {
            //拍照时快门声音或拍照时动画
            super.onCaptureStarted(session, request, timestamp, frameNumber);
            Log.d(TAG, "onCaptureStarted");
        }

        @Override
        public void onCaptureProgressed(@NonNull final CameraCaptureSession session,
                                        @NonNull final CaptureRequest request,
                                        @NonNull final CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
            Log.d(TAG, "onCaptureProgressed");
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull final CameraCaptureSession session,
                                       @NonNull final CaptureRequest request,
                                       @NonNull final TotalCaptureResult result) {
            //拍照结束时回调
            //TotalCaptureResult包含拍照过程中所有CaptureRequest
            super.onCaptureCompleted(session, request, result);
            Log.d(TAG, "onCaptureCompleted");
            process(result);
        }

        @Override
        public void onCaptureFailed(@NonNull final CameraCaptureSession session,
                                    @NonNull final CaptureRequest request,
                                    @NonNull final CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            Log.d(TAG, "onCaptureFailed");
        }

        @Override
        public void onCaptureSequenceCompleted(@NonNull final CameraCaptureSession session,
                                               final int sequenceId, final long frameNumber) {
            super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
            Log.d(TAG, "onCaptureSequenceCompleted");
        }

        @Override
        public void onCaptureSequenceAborted(@NonNull final CameraCaptureSession session,
                                             final int sequenceId) {
            super.onCaptureSequenceAborted(session, sequenceId);
            Log.d(TAG, "onCaptureSequenceAborted");
        }

        @Override
        public void onCaptureBufferLost(@NonNull final CameraCaptureSession session,
                                        @NonNull final CaptureRequest request,
                                        @NonNull final Surface target, final long frameNumber) {
            super.onCaptureBufferLost(session, request, target, frameNumber);
            Log.d(TAG, "onCaptureBufferLost");
        }

        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    // 等待锁定的状态，某些设备完成锁定后 CONTROL_AF_STATE 可能为 null
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // 如果焦点已经锁定，不管是否对焦成功，检查 CONTROL_AE_STATE
                        // 某些设备可能为空
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            // 若果自动曝光良好，将状态置为可拍照，并执行拍照
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            // 以上状态都不满足，执行预拍照
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }
    };

    private ImageReader mImageReader;
    //线程名称
    private static final String THREAD_CAMERA2 = "Camera2";
    //后台线程，用于处理Camera相关的数据处理，防止阻塞UI线程
    private Handler mCameraHandler;
    //主线程
    private Handler mMainHandler;
    /**
     * Orientation of the camera sensor
     */
    private int mSensorOrientation;

    private Size mPreviewSize;
    private OnImageDataListener mOnImageDataListener;
    private int mState = STATE_PREVIEW;

    public Camera2SurfaceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        //初始化Camera线程
        HandlerThread thread = new HandlerThread(THREAD_CAMERA2);
        thread.start();
        mCameraHandler = new Handler(thread.getLooper());
        //初始化主线程
        mMainHandler = new Handler(Looper.getMainLooper());

        setKeepScreenOn(true);
        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface,
                                          final int width, final int height) {
        //打开相机
        openCamera(width, height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(final SurfaceTexture surface,
                                            final int width, final int height) {
        //预览方向改变时，执行转换操作
        configureTransform(width, height);
    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = (Activity) getContext();
        if (null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        setTransform(matrix);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(final SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(final SurfaceTexture surface) {

    }

    private void openCamera(int width, int height) {
        //检查相机权限, 如果没有权限则请求权限
        //此处不做过多处理，由外部Activity去做权限请求
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //设置相机参数
        setupCameraParameter(width, height);
        //打开相机
        try {
            mCameraManager.openCamera(CAMERA_ID, mStateCallback, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //相机状态回调
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull final CameraDevice camera) {
            Log.d(TAG, "camera opened");
            mCameraDevice = camera;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull final CameraDevice camera) {
            Log.d(TAG, "camera disconnected");
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull final CameraDevice camera, final int error) {
            Log.d(TAG, "camera error，code = " + error);
            camera.close();
            mCameraDevice = null;
        }
    };

    private void createCameraPreviewSession() {
        Log.d(TAG, "createCameraPreviewSession()");
        try {
            SurfaceTexture surfaceTexture = getSurfaceTexture();
            assert surfaceTexture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface surface = new Surface(surfaceTexture);

            //创建一个用于预览的 CaptureRequest
            //CameraDevice.TEMPLATE_PREVIEW:预览
            mPreviewRequestBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            //创建一个 CameraCaptureSession 会话
            //第一个参数表示用于显示输出数据的Surface
            //mSurfaceHolder.getSurface()用于显示预览
            //mImageReader.getSurface()用于显示图片
            //第二个参数表示CameraCaptureSession的回调状态接口
            //第三个参数表示StateCallback回调执行的线程，默认为主线程
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull final CameraCaptureSession session) {
                            Log.d(TAG, "CameraCaptureSession.StateCallback onConfigured()");
                            // 相机已经关闭，不再预览
                            if (mCameraDevice == null) {
                                return;
                            }

                            // 此次会话已准备就绪，可以开始预览
                            mCameraCaptureSession = session;
                            try {
                                // Auto focus should be continuous for camera preview.
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // Flash is automatically enabled when necessary.
                                // setAutoFlash(mPreviewRequestBuilder);

                                mPreviewRequest = mPreviewRequestBuilder.build();
                                // 开启预览，预览数据通过 mCaptureCallback 回调出来
                                mCameraCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, mCameraHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull final CameraCaptureSession session) {

                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setupCameraParameter(int width, int height) {
        try {
            //获取后置摄像头的参数
            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(CAMERA_ID);

            StreamConfigurationMap map = mCameraCharacteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map == null) {
                return;
            }

            mSensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

            Size displaySize = new Size(width, height);

            Size largest = findBestSize(map.getOutputSizes(ImageFormat.JPEG), displaySize);
            mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                    ImageFormat.JPEG, /*maxImages*/2);
            mImageReader.setOnImageAvailableListener(
                    mOnImageAvailableListener, mCameraHandler);

            mPreviewSize = findBestSize(map.getOutputSizes(SurfaceTexture.class), displaySize);
            Log.d(TAG, "preview size: " + mPreviewSize.toString());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    private Size findBestSize(Size[] sizes, Size resolution) {
        int bestX = 0;
        int bestY = 0;
        int diff = Integer.MAX_VALUE;
        for (Size size : sizes) {
            int newX = size.getWidth();
            int newY = size.getHeight();

            int newDiff = Math.abs(newX - resolution.getWidth())
                    + Math.abs(newY - resolution.getHeight());
            int newDiff2 = Math.abs(newX - resolution.getHeight())
                    + Math.abs(newY - resolution.getWidth());
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
            return new Size(bestX, bestY);
        } else {
            // Ensure that the camera resolution is a multiple of 8,
            // as the screen may not be.
            return new Size((resolution.getWidth() >> 3) << 3, (resolution.getHeight() >> 3) << 3);
        }
    }

    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(final ImageReader reader) {
            Log.d(TAG, "onImageAvailable()");
            Image image = reader.acquireNextImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            final byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            //此处不要调用reader.close()，否则会收不到该回调
            image.close();
            if (mOnImageDataListener != null) {
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mOnImageDataListener.onImageData(bytes);
                    }
                });
            }
        }
    };

    public void setOnImageDataListener(final OnImageDataListener onImageDataListener) {
        mOnImageDataListener = onImageDataListener;
    }

    public void takePreview() {
        unlockFocus();
    }

    public void takePicture() {
        lockFocus();
    }

    /**
     * Lock the focus as the first step for a still image capture.
     */
    private void lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the lock.
            mState = STATE_WAITING_LOCK;
            mCameraCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the precapture sequence for capturing a still image. This method should be called when
     * we get a response in {@link #mCaptureCallback} from {@link #lockFocus()}.
     */
    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mCameraCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Capture a still picture. This method should be called when we get a response in
     * {@link #mCaptureCallback} from both {@link #lockFocus()}.
     */
    private void captureStillPicture() {
        try {
            if (mCameraDevice == null) {
                return;
            }
            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // setAutoFlash(captureBuilder);

            // 获取手机方向
            int rotation = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRotation();
            // 根据设备方向计算设置照片的方向
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            mCameraCaptureSession.stopRepeating();
            mCameraCaptureSession.abortCaptures();
            mCameraCaptureSession.capture(captureBuilder.build(), null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unlock the focus. This method should be called when still image capture sequence is
     * finished.
     */
    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            //setAutoFlash(mPreviewRequestBuilder);
            mCameraCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mCameraHandler);
            // After this, the camera will go back to the normal state of preview.
            mState = STATE_PREVIEW;
            mCameraCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
                    mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    public boolean isPreviewing() {
        return mState == STATE_PREVIEW;
    }

    private boolean mManualFocusEngaged;

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        final int actionMasked = event.getActionMasked();
        if (actionMasked != MotionEvent.ACTION_DOWN) {
            return false;
        }
        if (mManualFocusEngaged) {
            Log.d(TAG, "Manual focus already engaged");
            return true;
        }

        final Rect sensorArraySize = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);

        //TODO: here I just flip x,y, but this needs to correspond with the sensor orientation (via SENSOR_ORIENTATION)
        final int y = (int) ((event.getX() / (float) getWidth()) * (float) sensorArraySize.height());
        final int x = (int) ((event.getY() / (float) getHeight()) * (float) sensorArraySize.width());
        final int halfTouchWidth = 150; //(int)motionEvent.getTouchMajor(); //TODO: this doesn't represent actual touch size in pixel. Values range in [3, 10]...
        final int halfTouchHeight = 150; //(int)motionEvent.getTouchMinor();
        MeteringRectangle focusAreaTouch = new MeteringRectangle(Math.max(x - halfTouchWidth, 0),
                Math.max(y - halfTouchHeight, 0),
                halfTouchWidth * 2,
                halfTouchHeight * 2,
                MeteringRectangle.METERING_WEIGHT_MAX - 1);

        try {
            //first stop the existing repeating request
            mCameraCaptureSession.stopRepeating();

            //cancel any existing AF trigger (repeated touches, etc.)
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_CANCEL);
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_OFF);
            mCameraCaptureSession.capture(mPreviewRequestBuilder.build(), mTouchFocusCallback, mCameraHandler);

            //Now add a new AF trigger with focus region
            if (isMeteringAreaAFSupported()) {
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, new MeteringRectangle[]{focusAreaTouch});
            }
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);

            //then we ask for a single request (not repeating!)
            mCameraCaptureSession.capture(mPreviewRequestBuilder.build(), mTouchFocusCallback, mCameraHandler);
            mManualFocusEngaged = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return super.onTouchEvent(event);
    }

    private final CameraCaptureSession.CaptureCallback mTouchFocusCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Log.d(TAG, "Manual AF success: " + result);
            mManualFocusEngaged = false;
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            unlockFocus();
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            Log.e(TAG, "Manual AF failure: " + failure);
            mManualFocusEngaged = false;
        }
    };

    private boolean isMeteringAreaAFSupported() {
        if (mCameraCharacteristics == null) {
            return false;
        }
        return mCameraCharacteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF) >= 1;
    }
}
