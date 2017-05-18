package com.littlejie.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * 语言工具类
 * Created by littlejie on 2017/5/17.
 */

public class LanguageUtil {

    public static final String LANGUAGE = "Language";
    public static final String COUNTRY = "country";

    public static String getDefaultLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 判断是否与设定的语言相同.
     *
     * @param context
     * @return
     */
    public static boolean isSetValue(Context context) {
        Locale current = context.getResources().getConfiguration().locale;
        return current.equals(getAppLocale(context));
    }

    public static String getAppLanguage(Context context) {
        String name = context.getPackageName() + "_" + LANGUAGE;
        SharedPreferences preferences =
                context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getString(LANGUAGE,
                Locale.getDefault().getLanguage());
    }

    public static String getAppCountry(Context context) {
        String name = context.getPackageName() + "_" + LANGUAGE;
        SharedPreferences preferences =
                context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getString(COUNTRY,
                Locale.getDefault().getCountry());
    }

    public static Locale getAppLocale(Context context) {
        return new Locale(getAppLanguage(context), getAppCountry(context));
    }

    public static void changeAppLanguage(Context context, String language, String country) {
        changeAppLanguage(context, new Locale(language, country));
    }

    public static void changeAppLanguage(Context context, Locale locale) {
        changeAppLanguage(context, locale, true);
    }

    /**
     * 更改应用语言，当App重启后，更改消失.
     *
     * @param context
     * @param locale
     * @param persistence 是否持久化
     */
    public static void changeAppLanguage(Context context, Locale locale, boolean persistence) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        resources.updateConfiguration(configuration, metrics);
        if (persistence) {
            String name = context.getPackageName() + "_" + LANGUAGE;
            SharedPreferences preferences =
                    context.getSharedPreferences(name, Context.MODE_PRIVATE);
            preferences.edit().putString(LANGUAGE, locale.getLanguage()).apply();
            preferences.edit().putString(COUNTRY, locale.getCountry()).apply();
        }
    }
}
