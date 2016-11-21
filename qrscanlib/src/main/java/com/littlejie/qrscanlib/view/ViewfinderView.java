/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.littlejie.qrscanlib.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.littlejie.qrscanlib.R;
import com.littlejie.qrscanlib.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 */
public final class ViewfinderView extends View {

    private static final long ANIMATION_DELAY = 24L;
    private static final int OPAQUE = 0xFF;
    private static final int RECT_CORNER_WIDTH = 10;
    private static final int RECT_CORNER_LENGTH = 20;

    private final Paint mPaint;
    private final int mResultPointColor;
    private Rect frame;
    private Collection<ResultPoint> mPossibleResultPoints;
    private Collection<ResultPoint> mLastPossibleResultPoints;
    private int mCornerLength;
    private int mSlideTop = -1;
    private boolean isPause;

    private long mScanInterval = ANIMATION_DELAY;
    private String mScanTip = null;
    //扫描框内边角颜色
    private int mCornerColor;
    //扫描条颜色
    private int mScanBarColor;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every time in onDraw().
        mPaint = new Paint();
        Resources resources = getResources();
        mResultPointColor = resources.getColor(R.color.possible_result_points);
        mPossibleResultPoints = new HashSet<ResultPoint>(5);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        mCornerLength = (int) dm.density * RECT_CORNER_LENGTH;

        mScanTip = getResources().getString(R.string.scan_tip);
        mCornerColor = getResources().getColor(R.color.corner);
        mScanBarColor = getResources().getColor(R.color.corner);
    }

    @Override
    public void onDraw(Canvas canvas) {
        frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (mSlideTop == -1) {
            mSlideTop = frame.top;
        }

        // Draw the exterior (i.e. outside the framing rect) darkened
        drawShadowRect(canvas, frame, width, height);

        // Draw a two pixel solid black border inside the framing rect
//        drawInnerRect(canvas, frame);
        drawRectCorner(canvas, frame.left, frame.top, frame.right, frame.bottom);
        drawScanningBar(canvas, frame.left, mSlideTop, frame.right);

        drawText(canvas, frame.left, frame.bottom, frame.right);

        Collection<ResultPoint> currentPossible = mPossibleResultPoints;
        Collection<ResultPoint> currentLast = mLastPossibleResultPoints;
        if (currentPossible.isEmpty()) {
            mLastPossibleResultPoints = null;
        } else {
            mPossibleResultPoints = new HashSet<ResultPoint>(5);
            mLastPossibleResultPoints = currentPossible;
            mPaint.setAlpha(OPAQUE);
            mPaint.setColor(mResultPointColor);
            for (ResultPoint point : currentPossible) {
                canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, mPaint);
            }
        }
        if (currentLast != null) {
            mPaint.setAlpha(OPAQUE / 2);
            mPaint.setColor(mResultPointColor);
            for (ResultPoint point : currentLast) {
                canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, mPaint);
            }
        }

        mSlideTop += 16;
        if (mSlideTop >= frame.bottom) {
            mSlideTop = frame.top;
        }
        // Request another update at the animation interval, but only repaint the laser line,
        // not the entire viewfinder mask.
        if (!isPause) {
            postInvalidateDelayed(mScanInterval, frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    public void drawViewfinder() {
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        mPossibleResultPoints.add(point);
    }

    private void drawShadowRect(Canvas canvas, Rect frame, int width, int height) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.shadow));

        //上
        canvas.drawRect(0, 0, width, frame.top, mPaint);
        //左
        canvas.drawRect(0, frame.top, frame.left, frame.bottom, mPaint);
        //右
        canvas.drawRect(frame.right, frame.top, width, frame.bottom, mPaint);
        //下
        canvas.drawRect(0, frame.bottom, width, height, mPaint);
    }

    private void drawRectCorner(Canvas canvas, int left, int top, int right, int bottom) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mCornerColor);

        //左上
        canvas.drawRect(left, top, left + RECT_CORNER_WIDTH, top + mCornerLength, mPaint);
        canvas.drawRect(left, top, left + mCornerLength, top + RECT_CORNER_WIDTH, mPaint);

        //右上
        canvas.drawRect(right - mCornerLength, top, right, top + RECT_CORNER_WIDTH, mPaint);
        canvas.drawRect(right - RECT_CORNER_WIDTH, top, right, top + mCornerLength, mPaint);

        //左下
        canvas.drawRect(left, bottom - mCornerLength, left + RECT_CORNER_WIDTH, bottom, mPaint);
        canvas.drawRect(left, bottom - RECT_CORNER_WIDTH, left + mCornerLength, bottom, mPaint);

        //右下
        canvas.drawRect(right - mCornerLength, bottom - RECT_CORNER_WIDTH, right, bottom, mPaint);
        canvas.drawRect(right - RECT_CORNER_WIDTH, bottom - mCornerLength, right, bottom, mPaint);
    }

    private void drawScanningBar(final Canvas canvas, float left, float top, float right) {
        mPaint.reset();
        mPaint.setColor(mScanBarColor);
        canvas.drawRect(left + mCornerLength, top, right - mCornerLength, top + RECT_CORNER_WIDTH, mPaint);
    }

    private void drawText(Canvas canvas, float left, float bottom, int right) {
        mPaint.reset();
        mPaint.setColor(Color.WHITE);
        float textSize = (right - left) / mScanTip.length();
        mPaint.setTextSize(textSize);

        canvas.drawText(mScanTip, left, bottom + getFontHeight() + mCornerLength, mPaint);
    }

    private float getFontHeight() {
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        return metrics.descent - metrics.ascent;
    }

    public void isPause(boolean pause) {
        if (!isPause) {
            postInvalidate(frame.left, frame.top, frame.right, frame.bottom);
        }
        isPause = pause;
    }

    private float getFontWidth(String text) {
        return mPaint.measureText(text);
    }

    public void setScanTip(String tip) {
        mScanTip = tip;
    }

    public void setScanInterval(long interval) {
        mScanInterval = interval;
    }
}