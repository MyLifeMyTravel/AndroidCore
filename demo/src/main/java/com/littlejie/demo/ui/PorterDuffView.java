package com.littlejie.demo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.littlejie.demo.R;

/**
 * Created by littlejie on 2017/2/28.
 */

public class PorterDuffView extends View {

    private int mDefaultSize = 150;

    private Paint mBitmapPaint;
    private Bitmap mBitmap1, mBitmap2;

    public PorterDuffView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBitmapPaint = new Paint();
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        mBitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_btn_video_dark);
        mBitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_fab_repair);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap1, 0, 0, mBitmapPaint);
        canvas.drawBitmap(mBitmap2, 0, 0, mBitmapPaint);
    }
}
