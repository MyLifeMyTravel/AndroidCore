package com.littlejie.core.manager;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by littlejie on 2016/12/8.
 */

public class TintManager {

    /**
     * 给 drawable 着色
     *
     * @param drawable 需要着色的 drawable 对象
     * @param colors   ColorStateList 对象，代表需要着色的颜色
     * @return
     */
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        //这里需要对 drawable 对象执行 mutate() 操作
        //该操作能防止一个屏幕里多次使用同一个图片，对其中一个图片操作时影响其他图片
        //当然，你也可以在getResource().getDrawable()的时候就执行
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    /**
     * 给 drawable 着色
     *
     * @param drawable 需要着色的 drawable 对象
     * @param color   ColorStateList 对象，代表需要着色的颜色
     * @return
     */
    public static Drawable tintDrawable(Drawable drawable, int color) {
        return tintDrawable(drawable, ColorStateList.valueOf(color));
    }
}
