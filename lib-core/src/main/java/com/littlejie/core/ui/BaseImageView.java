package com.littlejie.core.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.littlejie.core.R;
import com.littlejie.core.util.ImageLoaderUtil;

/**
 * 自定义 ImageView 类，封装 UniversalImageLoader
 * Created by littlejie on 2016/12/1.
 */

public class BaseImageView extends android.support.v7.widget.AppCompatImageView {

    protected int mRadius = 0;
    private int mImageResId;

    public BaseImageView(Context context, int resId) {
        super(context);
        mImageResId = resId;
        setImage(mImageResId);
    }

    public BaseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setScaleType(ScaleType.FIT_XY);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseImageView);
        mImageResId = a.getResourceId(R.styleable.BaseImageView_defaultImage, 0);

        mRadius = a.getDimensionPixelSize(R.styleable.BaseImageView_radius, 0);

        boolean custom = a.getBoolean(R.styleable.BaseImageView_customImage, false);
        if (custom) {
            mImageResId = a.getResourceId(R.styleable.BaseImageView_imageSrc, 0);
        }
        a.recycle();

        setImage(mImageResId);
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public void setImage(Bitmap bitmap) {
        setImageBitmap(bitmap);
    }

    public void setImage(Drawable drawable) {
        setImageDrawable(drawable);
    }

    public void setImage(int resId) {
        if (resId == 0) {
            setImageResource(0);
        } else {
            String res = "drawable://" + resId;
            if (mRadius != 0) {
                ImageLoaderUtil.setMemCachedImage(res, this, mImageResId, mRadius);
            } else {
                ImageLoaderUtil.setMemCachedImage(res, this, mImageResId);
            }
        }
    }

    public void setImage(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return;
        }
        if (mRadius != 0) {
            ImageLoaderUtil.setDiskCachedImage(uri, this, mImageResId, mRadius);
        } else {
            ImageLoaderUtil.setDiskCachedImage(uri, this, mImageResId);
        }
    }

    public void setImage(String url, ImageLoaderUtil.OnImageLoadListener listener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (mRadius != 0) {
            ImageLoaderUtil.setDiskCachedImage(url, this, mImageResId, mRadius, listener);
        } else {
            ImageLoaderUtil.setDiskCachedImage(url, this, mImageResId, listener);
        }
    }

}
