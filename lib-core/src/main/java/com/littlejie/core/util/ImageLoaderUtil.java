package com.littlejie.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

/**
 * Universal Image Loader 工具类，请在 Application 的 onCreate() 方法中进行初始化
 * Created by littlejie on 2017/2/10.
 */

public class ImageLoaderUtil {

    private static DisplayImageOptions.Builder mDIODiskCache;
    private static DisplayImageOptions.Builder mDIOMemCache;
    private static DisplayImageOptions.Builder mDIODiskCacheRec;
    private static DisplayImageOptions.Builder mDIOMemCacheRec;

    /**
     * 初始化 Universal Image Load
     *
     * @param context  建议使用 ApplicationContext ，否则会造成内存泄漏
     * @param cacheDir 缓存目录
     */
    public static void init(Context context, String cacheDir) {
        mDIODiskCache = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY);

        mDIOMemCache = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .imageScaleType(ImageScaleType.EXACTLY);

        mDIODiskCacheRec = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY);

        mDIOMemCacheRec = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .imageScaleType(ImageScaleType.EXACTLY);

        initUIL(context, cacheDir);
    }

    private static void initUIL(Context context, String cacheDir) {
        if (TextUtils.isEmpty(cacheDir)) {
            return;
        }
        File cacheFile = new File(cacheDir);
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(5 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheFile)) // default
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
    }

    public static void loadDiskCachedImage(String url, int resId) {
        ImageLoader.getInstance().loadImage(url, mDIODiskCacheRec.showImageOnLoading(resId).showImageOnFail(resId).build(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
            }
        });
    }

    public static void loadDiskCachedImage(String url, int resId, int cornerRadius) {

        ImageLoader.getInstance().loadImage(url, mDIODiskCacheRec.showImageOnLoading(resId).showImageOnFail(resId).displayer(new RoundedBitmapDisplayer(cornerRadius)).build(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
            }
        });
    }

    public static void setMemCachedImage(String url, ImageView iv, int resId, int cornerRadius) {
        try {
            ImageLoader.getInstance().displayImage(url, iv,
                    mDIOMemCacheRec.showImageOnLoading(resId).showImageOnFail(resId)
                            .displayer(new RoundedBitmapDisplayer(cornerRadius)).build());
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void setMemCachedImage(String url, ImageView iv, int resId) {
        try {
            ImageLoader.getInstance().displayImage(url, iv,
                    mDIOMemCache.showImageOnLoading(resId).showImageOnFail(resId).build());
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void setDiskCachedImage(String url, ImageView iv) {
        try {
            ImageLoader.getInstance().displayImage(url, iv, mDIODiskCache.build());
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void setDiskCachedImage(String url, ImageView iv, int resId) {
        try {
            if (iv.getDrawable() == null) {
                ImageLoader.getInstance().displayImage(url, iv,
                        mDIODiskCache.showImageOnLoading(resId).showImageOnFail(resId).build());
            } else {
                ImageLoader.getInstance().displayImage(url, iv,
                        mDIODiskCache.showImageOnFail(resId).build());
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void setDiskCachedImage(String url, ImageView iv, int resId, int cornerRadius) {
        try {
            if (iv.getDrawable() == null) {
                ImageLoader.getInstance().displayImage(url, iv,
                        mDIODiskCacheRec.showImageOnLoading(resId).showImageOnFail(resId)
                                .displayer(new RoundedBitmapDisplayer(cornerRadius)).build());
            } else {
                ImageLoader.getInstance().displayImage(url, iv,
                        mDIODiskCacheRec.showImageOnFail(resId)
                                .displayer(new RoundedBitmapDisplayer(cornerRadius)).build());
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void setDiskCachedImage(String url, ImageView iv, DisplayImageOptions options) {
        try {
            ImageLoader.getInstance().displayImage(url, iv, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void setDiskCachedImage(String url, ImageView iv, int resId, final OnImageLoadListener listener) {
        try {
            ImageLoader.getInstance().displayImage(url, iv,
                    iv.getDrawable() == null ?
                            mDIODiskCache.showImageOnLoading(resId).showImageOnFail(resId).build() :
                            mDIODiskCacheRec.showImageOnFail(resId).build(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                            if (listener != null) {
                                listener.onLoadingFailed(s, view, failReason);
                            }
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            if (listener != null) {
                                listener.onLoadingComplete(s, view, bitmap);
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void setDiskCachedImage(String url, ImageView iv, int resId, int cornerRadius, final OnImageLoadListener listener) {
        try {
            ImageLoader.getInstance().displayImage(url, iv,
                    iv.getDrawable() == null ?
                            mDIODiskCacheRec.showImageOnLoading(resId).showImageOnFail(resId)
                                    .displayer(new RoundedBitmapDisplayer(cornerRadius)).build() :
                            mDIODiskCacheRec.showImageOnFail(resId)
                                    .displayer(new RoundedBitmapDisplayer(cornerRadius)).build(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                            if (listener != null) {
                                listener.onLoadingFailed(s, view, failReason);
                            }
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            if (listener != null) {
                                listener.onLoadingComplete(s, view, bitmap);
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public interface OnImageLoadListener {
        void onLoadingComplete(String s, View v, Bitmap bitmap);

        void onLoadingFailed(String s, View view, FailReason failReason);
    }

    public static void setMemCachedImage(String url, ImageView iv) {
        try {
            ImageLoader.getInstance().displayImage(url, iv, mDIOMemCache.build());
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }
}
