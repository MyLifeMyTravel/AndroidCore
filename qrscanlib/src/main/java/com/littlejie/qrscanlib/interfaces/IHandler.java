package com.littlejie.qrscanlib.interfaces;


import android.graphics.Bitmap;
import android.os.Handler;

import com.google.zxing.Result;
import com.littlejie.qrscanlib.view.ViewfinderView;

/**
 * Created by Lion on 2016/5/4.
 */
public interface IHandler {

    Handler getDecodeHandler();

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    void handleDecode(Result result, Bitmap barcode);

    ViewfinderView getViewfinderView();

    void drawViewfinder();
}
