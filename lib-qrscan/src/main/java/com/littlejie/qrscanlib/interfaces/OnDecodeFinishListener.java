package com.littlejie.qrscanlib.interfaces;

import android.graphics.Bitmap;

import com.google.zxing.Result;

/**
 * Created by Lion on 2016/6/18.
 */
public interface OnDecodeFinishListener {
    void onDecodeFinish(Result result, Bitmap barcode);
}
