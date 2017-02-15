package com.littlejie.filemanager.manager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.littlejie.core.manager.TintManager;
import com.littlejie.filemanager.R;

/**
 * 用于管理 App 中会被着色的 Drawable
 * Created by littlejie on 2017/2/15.
 */

public class TintDrawableManager {

    private static Drawable sUnknownDrawable;
    private static Drawable sFolderDrawable, sTxtDrawable;

    public static void init(Context context) {
        Resources resources = context.getResources();
        int defaultColor = resources.getColor(R.color.colorPrimary);
        sUnknownDrawable = TintManager.tintDrawable(context, R.mipmap.ic_unknown_black_24dp, defaultColor);
        sFolderDrawable = TintManager.tintDrawable(context, R.mipmap.ic_folder_black_24dp, defaultColor);
        sTxtDrawable = TintManager.tintDrawable(context, R.mipmap.ic_text_black_24dp, defaultColor);
    }

    public static Drawable getUnknownDrawable() {
        return sUnknownDrawable;
    }

    public static Drawable getFolderDrawable() {
        return sFolderDrawable;
    }

    public static Drawable getTxtDrawable() {
        return sTxtDrawable;
    }
}
