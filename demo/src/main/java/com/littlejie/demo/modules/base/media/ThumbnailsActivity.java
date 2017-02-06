package com.littlejie.demo.modules.base.media;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.FileUtil;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.utils.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

@Description(description = "Android 图片、视频 缩略图")
public class ThumbnailsActivity extends BaseActivity {

    @BindView(R.id.iv_thumbnail)
    ImageView mIvThumbnail;
    @BindView(R.id.lv)
    ListView mLv;

    private DisplayMetrics mMetrics;
    private List<String> mLstMediaPath;
    private String mCurrentPath;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_thumbnails;
    }

    @Override
    protected void initData() {
        mMetrics = getResources().getDisplayMetrics();
        mLstMediaPath = new ArrayList<>();
        mLstMediaPath.addAll(getMediaPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        mLstMediaPath.addAll(getMediaPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI));
    }

    @Override
    protected void initView() {
        mLv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mLstMediaPath));
    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.btn_save_thumbnail_2_cache)
    void saveThumbnail2Cache() {
        if (TextUtils.isEmpty(mCurrentPath)) {
            ToastUtil.showDefaultToast("请先选择图片或视频");
        }
        ensureCacheFolder();
        Bitmap bitmap = getBitmapFromFile(mCurrentPath);
        try {
            FileOutputStream fos = new FileOutputStream(new File(Constant.CACHE_FOLDER + "/" + Base64.encodeToString(mCurrentPath.getBytes(), Base64.NO_CLOSE)));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureCacheFolder() {
        File cacheFolder = new File(Constant.CACHE_FOLDER);
        if (!cacheFolder.exists()) {
            cacheFolder.mkdirs();
        }
    }


    @OnItemClick(R.id.lv)
    void onItemClick(int position) {
        mCurrentPath = mLstMediaPath.get(position);
        mIvThumbnail.setImageBitmap(getBitmapFromSystem(mCurrentPath));
    }

    @Override
    protected void process() {
//        测试两者生成缩略图时间，注意观察内存使用情况，极可能会发生ANR
//        long start = System.currentTimeMillis();
//        for (String path : mLstMediaPath) {
//            getBitmapFromSystem(path);
//        }
//        Log.d(TAG, "process: getBitmapFromSystem spend = " + (System.currentTimeMillis() - start));
//        start = System.currentTimeMillis();
//        for (String path : mLstMediaPath) {
//            getBitmapFromFile(path);
//        }
//        Log.d(TAG, "process: getBitmapFromFile spend = " + (System.currentTimeMillis() - start));
    }


    private List<String> getMediaPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);
        if (cursor == null) {
            return new ArrayList<>();
        }
        List<String> lstPath = new ArrayList<>();
        while (cursor.moveToNext()) {
            lstPath.add(cursor.getString(0));
        }
        return lstPath;
    }

    private Bitmap getBitmapFromSystem(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        options.inSampleSize = 1;
        return getBitmapFromSystem(getContentResolver(), path, MediaStore.Images.Thumbnails.MICRO_KIND, options);
    }

    private Bitmap getBitmapFromSystem(ContentResolver resolver, String path, int kind, BitmapFactory.Options options) {
        String mimeType = FileUtil.getFileMimeType(path);
        boolean isImage = mimeType.startsWith("image");
        Cursor cursor = null;
        String[] projection = new String[]{MediaStore.MediaColumns._ID};
        String selection = MediaStore.MediaColumns.DATA + " = ?";
        String[] selectionArg = new String[]{path};
        Uri uri = isImage
                ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                : MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        cursor = getContentResolver().query(uri, projection, selection, selectionArg, null);

        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            return isImage
                    ? MediaStore.Images.Thumbnails.getThumbnail(resolver, id, kind, options)
                    : MediaStore.Video.Thumbnails.getThumbnail(resolver, id, kind, options);
        }
        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    //根据文件路径获取缩略图
    private Bitmap getBitmapFromFile(String path) {
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
        Bitmap bitmap = null;
        String mimeType = FileUtil.getFileMimeType(path);
        if (mimeType.startsWith("image")) {//若果是图片，即拍照
            //直接通过路径利用BitmapFactory来形成bitmap
            bitmap = BitmapFactory.decodeFile(path);
        } else if (mimeType.startsWith("video")) {//如果是视频，即拍摄视频
            //利用ThumbnailUtils
            bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
        }

        //获取图片后，我们队图片进行压缩，获取指定大小
        if (bitmap != null) {
            //裁剪大小
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, (int) (100 * mMetrics.density), (int) (100 * mMetrics.density));
        } else {//如果为空，采用我们的默认图片
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
        return bitmap;
    }
}
