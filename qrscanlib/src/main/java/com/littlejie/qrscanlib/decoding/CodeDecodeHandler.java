package com.littlejie.qrscanlib.decoding;

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

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.littlejie.qrscanlib.camera.CameraManager;
import com.littlejie.qrscanlib.interfaces.IHandler;
import com.littlejie.qrscanlib.view.ViewfinderResultPointCallback;

import java.util.Vector;

/**
 * This class handles all the messaging which comprises the state machine for capture.
 */
public final class CodeDecodeHandler extends Handler {

    private static final String TAG = CodeDecodeHandler.class.getSimpleName();

    private final IHandler iHandler;
    private final DecodeThread decodeThread;
    private State state;

    public static final int AUTO_FOCUS = 1;
    public static final int RESTART_PREVIEW = 2;
    public static final int DECODE_SUCCEEDED = 3;
    public static final int DECODE_FAILED = 4;
    public static final int DECODE = 5;
    public static final int QUIT = 6;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CodeDecodeHandler(IHandler iHandler, Vector<BarcodeFormat> decodeFormats,
                             String characterSet) {
        this.iHandler = iHandler;
        decodeThread = new DecodeThread(iHandler, decodeFormats, characterSet,
                new ViewfinderResultPointCallback(iHandler.getViewfinderView()));
        decodeThread.start();
        state = State.SUCCESS;
        // Start ourselves capturing previews and decoding.
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case AUTO_FOCUS:
                //Log.d(TAG, "Got auto-focus message");
                // When one auto focus pass finishes, start another. This is the closest thing to
                // continuous AF. It does seem to hunt a bit, but I'm not sure what else to do.
                if (state == State.PREVIEW) {
                    CameraManager.get().requestAutoFocus(this, AUTO_FOCUS);
                }
                break;
            case RESTART_PREVIEW:
                Log.d(TAG, "Got restart preview message");
                restartPreviewAndDecode();
                break;
            case DECODE_SUCCEEDED:
                Log.d(TAG, "Got decode succeeded message");
                state = State.SUCCESS;
                Bundle bundle = message.getData();

                Bitmap barcode = bundle == null ? null :
                        (Bitmap) bundle.getParcelable(DecodeThread.BARCODE_BITMAP);
                iHandler.handleDecode((Result) message.obj, barcode);
                break;
            case DECODE_FAILED:
                // We're decoding as fast as possible, so when one decode fails, start another.
                state = State.PREVIEW;
                CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), DECODE);
                break;
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.get().stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), QUIT);
        quit.sendToTarget();
        try {
            decodeThread.join();
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(DECODE_SUCCEEDED);
        removeMessages(DECODE_FAILED);
    }

    public void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), DECODE);
            CameraManager.get().requestAutoFocus(this, AUTO_FOCUS);
            iHandler.drawViewfinder();
        }
    }

}
