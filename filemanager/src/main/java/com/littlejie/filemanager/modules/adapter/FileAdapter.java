package com.littlejie.filemanager.modules.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.littlejie.core.manager.TintManager;
import com.littlejie.core.ui.BaseImageView;
import com.littlejie.core.util.FileUtil;
import com.littlejie.core.util.PackageUtil;
import com.littlejie.core.util.SpaceUtil;
import com.littlejie.core.util.TimeUtil;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.filemanager.R;
import com.littlejie.filemanager.util.Constant;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by littlejie on 2017/1/10.
 */

public class FileAdapter extends BaseAdapter {

    private static final String TAG = FileAdapter.class.getSimpleName();

    private Context mContext;
    private File[] mFiles;
    private Drawable mUnknownDrawable;
    private Drawable mFolderDrawable, mTxtDrawable;

    public FileAdapter(Context context) {
        mContext = context;
        Resources resources = context.getResources();
        int defaultColor = resources.getColor(R.color.colorPrimary);
        mUnknownDrawable = TintManager.tintDrawable(context, R.mipmap.ic_unknown_black_24dp, defaultColor);
        mFolderDrawable = TintManager.tintDrawable(context, R.mipmap.ic_folder_black_24dp, defaultColor);
        mTxtDrawable = TintManager.tintDrawable(context, R.mipmap.ic_text_black_24dp, defaultColor);
    }

    public void setData(File[] files) {
        mFiles = files;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFiles == null ? 0 : mFiles.length;
    }

    @Override
    public Object getItem(int position) {
        return mFiles == null ? null : mFiles[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_file, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        File file = (File) getItem(position);
        showIcon(vh.icon, file);
        showInfo(vh.info, file);
        vh.name.setText(file.getName());
        return convertView;
    }

    /**
     * 显示文件图标
     *
     * @param icon
     * @param file
     */
    private void showIcon(BaseImageView icon, File file) {
        FileUtil.FileType mimeType = FileUtil.FileType.getFileType(file);
        Log.d(TAG, "showIcon: file = " + file.getAbsolutePath() + "; mimeType = " + mimeType);
        switch (mimeType) {
            case DIRECTORY:
                icon.setImage(mFolderDrawable);
                break;
            case APK:
                icon.setImage(PackageUtil.getApkIcon(mContext, file.getAbsolutePath()));
                break;
            case IMAGE:
            case VIDEO:
                icon.setImage("file://" + file.getAbsolutePath());
                //此处代码效率得优化
                //icon.setImageBitmap(MediaUtil.getThumbnail(mContext, path, MediaStore.Images.Thumbnails.MICRO_KIND, null));
                break;
            case TXT:
                icon.setImage(mTxtDrawable);
                break;
            default:
                icon.setImage(mUnknownDrawable);
                break;

        }
    }

    private void showInfo(TextView info, File file) {
        String time = TimeUtil.parse2TimeDetail(file.lastModified());
        if (file.isDirectory()) {
            info.setText(file.listFiles(Constant.HIDDEN_FILE_FILTER).length + "项 | " + time);
        } else {
            info.setText(SpaceUtil.getSpace(file.length()) + " | " + time);
        }
    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        BaseImageView icon;
        @BindView(R.id.cbx_checked)
        CheckBox checkbox;
        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.tv_info)
        TextView info;

        @OnClick(R.id.cbx_checked)
        void onCheckBoxClick() {
            ToastUtil.showDefaultToast("哈哈");
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
