package com.littlejie.demo.modules.base.media;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.utils.Constant;
import com.littlejie.core.util.MediaUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

@Description(description = "Android 图片、视频 缩略图")
public class ThumbnailsActivity extends BaseActivity {

    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.iv_thumbnail)
    ImageView mIvThumbnail;
    @BindView(R.id.lv)
    ListView mLv;

    private List<String> mLstMediaPath;
    private int mSelectPosition;
    private String mCurrentPath;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_thumbnails;
    }

    @Override
    protected void initData() {
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
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onItemClick(mSelectPosition);
            }
        });
    }

    @OnClick(R.id.btn_save_thumbnail_2_cache)
    void saveThumbnail2Cache() {
        if (TextUtils.isEmpty(mCurrentPath)) {
            ToastUtil.showDefaultToast("请先选择图片或视频");
            return;
        }
        //根据缩略图类型进行缓存
        int thumbnailType = getThumbnailType();
        File thumbnail = MediaUtil.getThumbnail(this, mCurrentPath, Constant.CACHE_FOLDER, thumbnailType, null);
        //测试File转Bitmap
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(thumbnail));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mIvThumbnail.setImageBitmap(bitmap);
    }

    @OnItemClick(R.id.lv)
    void onItemClick(int position) {
        mSelectPosition = position;
        mCurrentPath = mLstMediaPath.get(position);
        mIvThumbnail.setImageBitmap(getThumbnailFromSystem(mCurrentPath, getThumbnailType()));
    }

    private int getThumbnailType() {
        //理论上，图片和视频的缩略图的kind是要分开获取的，但是源码中，两者的常量是一样，所以此处偷懒下
        return mRadioGroup.getCheckedRadioButtonId() == R.id.radio_mini
                ? MediaStore.Images.Thumbnails.MINI_KIND
                : MediaStore.Images.Thumbnails.MICRO_KIND;
    }

    @Override
    protected void process() {
//        测试两者生成缩略图时间，注意观察内存使用情况，极可能会发生ANR
//        long start = System.currentTimeMillis();
//        for (String path : mLstMediaPath) {
//            getThumbnail(path);
//        }
//        Log.d(LANGUAGE, "process: getThumbnail spend = " + (System.currentTimeMillis() - start));
//        start = System.currentTimeMillis();
//        for (String path : mLstMediaPath) {
//            getThumbnailFromFile(path);
//        }
//        Log.d(LANGUAGE, "process: getThumbnailFromFile spend = " + (System.currentTimeMillis() - start));
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

    private Bitmap getThumbnailFromSystem(String path, int kind) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
//        options.inSampleSize = 1;
        return MediaUtil.getThumbnail(this, path, kind, null);
    }

}
