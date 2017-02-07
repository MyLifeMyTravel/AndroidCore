package com.littlejie.demo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.littlejie.core.util.FileUtil;
import com.littlejie.demo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 多媒体文件操作工具类
 * Created by littlejie on 2017/2/4.
 */

public class MediaUtil {

    public static final String TAG = MediaUtil.class.getSimpleName();
    //CONTENT_URI 具体使用见 MediaObserverActivity
    private static final Uri CONTENT_URI = MediaStore.Files.getContentUri("external");
    private static final String SPLIT = ";";
    private static final String[] DOCUMENT = new String[]{".doc", ".docx", "xls", "xlsx", "ppt", "pptx"};
    private static String DOWNLOADS = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
            + Environment.DIRECTORY_DOWNLOADS;
    private static final File DOWNLOAD_FOLDER = new File(DOWNLOADS);

    private static DisplayMetrics sMetrics;

    public enum FilterType {
        SUFFIX, MIME_TYPE
    }

    /**
     * 获取Thumbnail缩略图缓存文件
     *
     * @param context
     * @param path     图片、视频路径
     * @param cacheDir 缩略图缓存路径
     * @param kind     MINI_KIND 和 MICRO_KIND.
     * @param options  当 kind = MINI_KIND 时，options 才有效
     * @return
     */
    public static File getThumbnail(Context context, String path, String cacheDir, int kind, BitmapFactory.Options options) {
        File cache = new File(cacheDir);
        if (!cache.exists()) {
            cache.mkdirs();
        }
        //Base64编码，缓存文件名
        String cacheName = Base64.encodeToString((path + kind).getBytes(), Base64.NO_CLOSE);
        File cacheFile = new File(cacheDir + "/" + cacheName);
        if (cacheFile.exists()) {
            Log.d(TAG, "getThumbnail: this thumbnail has cached.");
            return cacheFile;
        }
        try {
            Log.d(TAG, "getThumbnail: get thumbnail");
            Bitmap bitmap = getThumbnailFromSystem(context, path, kind, options);
            if (bitmap != null) {
                FileOutputStream fos = new FileOutputStream(cacheFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                bitmap.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cacheFile;
    }

    /**
     * 通过调用系统接口获取缩略图，可以对比参考 getThumbnailFromFile() ，但是不建议使用
     *
     * @param context
     * @param path    图片、视频路径
     * @param kind    MINI_KIND 和 MICRO_KIND.
     * @param options 当 kind = MINI_KIND 时，options 才有效
     * @return
     */
    //TODO 默认图片
    public static Bitmap getThumbnailFromSystem(Context context, String path, int kind, BitmapFactory.Options options) {
        String mimeType = FileUtil.getFileMimeType(path);
        boolean isImage = mimeType.startsWith("image");
        Cursor cursor = null;
        String[] projection = new String[]{MediaStore.MediaColumns._ID};
        String selection = MediaStore.MediaColumns.DATA + " = ?";
        String[] selectionArg = new String[]{path};
        Uri uri = isImage
                ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                : MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();
        cursor = resolver.query(uri, projection, selection, selectionArg, null);

        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            return isImage
                    ? MediaStore.Images.Thumbnails.getThumbnail(resolver, id, kind, options)
                    : MediaStore.Video.Thumbnails.getThumbnail(resolver, id, kind, options);
        }
        return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
    }

    /**
     * 根据文件路径获取缩略图，由于图片缩略图是自己操作，所以性能上没有 getThumbnailFromSystem() 好
     *
     * @param context
     * @param path    图片、视频路径
     * @param kind    MINI_KIND 和 MICRO_KIND.
     * @param options 当 kind = MINI_KIND 时，options 才有效
     * @return
     */
    private Bitmap getThumbnailFromFile(Context context, String path, int kind, BitmapFactory.Options options) {
        /**
         * android系统中为我们提供了ThumbnailUtils工具类来获取缩略图的处理。
         * ThumbnailUtils.createVideoThumbnail(filePath, kind)
         *          创建视频缩略图，filePath:文件路径，kind：MINI_KIND or MICRO_KIND(缩略图大小的区别)
         * ThumbnailUtils.extractThumbnail(bitmap, width, height)
         *          将bitmap裁剪为指定的大小
         * ThumbnailUtils.extractThumbnail(bitmap, width, height, options)
         *          将bitmap裁剪为指定的大小，可以有参数BitmapFactory.Options参数
         *
         */
        if (sMetrics == null) {
            sMetrics = context.getResources().getDisplayMetrics();
        }
        Bitmap bitmap = null;
        String mimeType = FileUtil.getFileMimeType(path);
        if (mimeType.startsWith("image")) {//若果是图片，即拍照
            //直接通过路径利用BitmapFactory来形成bitmap
            bitmap = BitmapFactory.decodeFile(path);
        } else if (mimeType.startsWith("video")) {//如果是视频，即拍摄视频
            //利用ThumbnailUtils
            bitmap = ThumbnailUtils.createVideoThumbnail(path, kind);
        }

        //获取图片后，我们队图片进行压缩，获取指定大小
        if (bitmap != null) {
            //裁剪大小
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, (int) (100 * sMetrics.density), (int) (100 * sMetrics.density));
        } else {//如果为空，采用我们的默认图片
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }
        return bitmap;
    }

    public static Set<String> getImageFolder(Context context) {
        return getMediaFolder(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true);
    }

    public static Set<String> getAudioFolder(Context context) {
        return getMediaFolder(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, false);
    }

    public static Set<String> getVideoFolder(Context context) {
        return getMediaFolder(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, false);
    }


    public static List<String> getDownloads(Context context) {
        return Arrays.asList(DOWNLOAD_FOLDER.list());
    }

    public static Set<String> getDocument(Context context) {
        Cursor cursor = filterFile(context, FilterType.SUFFIX, new String[]{MediaStore.MediaColumns.DATA}, DOCUMENT);
        if (cursor == null) {
            return null;
        }
        Set<String> fileSet = new HashSet<>();
        while (cursor.moveToNext()) {
            String path = cursor.getString(0);
            fileSet.add(path);
        }
        //关闭Cursor
        cursor.close();
        return fileSet;
    }

    /**
     * @param context
     * @param uri
     * @param onlyReturnParent
     * @return
     */
    public static Set<String> getMediaFolder(Context context, Uri uri, boolean onlyReturnParent) {
        Set<String> fileSet = new HashSet<>();
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            if (onlyReturnParent) {
                int lastIndex = path.lastIndexOf("/");
                if (lastIndex == -1) {
                    Log.d(TAG, "getMediaFolder: " + path);
                    continue;
                }
                path = path.substring(0, lastIndex);
            }
            fileSet.add(path);
        }
        //关闭Cursor
        cursor.close();
        return fileSet;
    }

    /**
     * 文件过滤，如果 arg 为 mime_type，则按照文件类型过滤；否则按照文件后缀过滤
     *
     * @param context
     * @param type
     * @param filter
     * @return 返回Cursor对象，使用完毕请调用cursor.close()
     */
    public static Cursor filterFile(Context context, FilterType type, String[] projection, String filter) {
        return filterFile(context, type, projection, spiltFilter(filter));
    }

    public static Cursor filterFile(Context context, FilterType type, String[] projection, String[] filters) {
        //TODO 优化生成 filter 的方法
        if (FilterType.MIME_TYPE == type) {//根据文件的 MimeType 字段进行过滤
            filters = getFiltersByMimeType(filters);
        } else {//根据文件的 后缀 字段进行过滤
            filters = getFiltersBySuffix(filters);
        }
        //获取 selection 语句
        String selection = getSelection(type, filters);
        //进行数据库查询
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, projection,
                selection, filters, MediaStore.MediaColumns.DATE_MODIFIED + " desc");
        return cursor;
    }

    private static String[] spiltFilter(String filter) {
        if (TextUtils.isEmpty(filter)) {
            return null;
        }
        return filter.split(SPLIT);
    }

    /**
     * 获取过滤条件
     * 示例：select * from files where mime_type like 'image/%' or mime_type like 'video/mp4'
     *
     * @return
     */
    private static String[] getFiltersByMimeType(String[] filters) {
        if (filters == null) {
            return null;
        }
        //形如 image/* 这类 mime_type 需要进行特殊处理
        for (int i = 0; i < filters.length; i++) {
            if (filters[i].endsWith("*")) {
                filters[i] = filters[i].replace("*", "%");
            }
        }
        return filters;
    }

    /**
     * 进行后缀过滤需要用到通配符，故此处做特殊处理
     * 示例：select * from file where _data like '%.txt' or _data like '%.jpg'
     *
     * @return
     */
    private static String[] getFiltersBySuffix(String[] filters) {
        if (filters == null) {
            return null;
        }
        for (int i = 0; i < filters.length; i++) {
            boolean startWithDot = filters[i].trim().startsWith(".");
            filters[i] = "%" + (startWithDot ? "" : ".") + filters[i].trim();
        }
        return filters;
    }

    /**
     * 生成 selection 语句
     *
     * @param type
     * @param filters
     * @return
     */
    private static String getSelection(FilterType type, String[] filters) {
        if (filters == null || filters.length == 0) {
            return null;
        }
        String arg = FilterType.SUFFIX == type
                ? MediaStore.MediaColumns.DATA
                : MediaStore.MediaColumns.MIME_TYPE;
        StringBuilder selection = new StringBuilder();
        for (int i = 0; i < filters.length; i++) {
            selection.append(arg).append(" like ?");
            if (i < filters.length - 1) {
                selection.append(" or ");
            }
        }
        return selection.toString();
    }
}
