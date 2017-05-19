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

    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
        changeAppLanguage(getAppLocale());
    }

    public static String getDefaultLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static boolean isSameWithSetting() {
        Locale current = Locale.getDefault();
        return current.equals(getAppLocale(sContext));
    }

    /**
     * 判断是否与设定的语言相同.
     *
     * @param context
     * @return
     */
    public static boolean isSameWithSetting(Context context) {
        Locale current = context.getResources().getConfiguration().locale;
        return current.equals(getAppLocale(context));
    }

    public static String getAppLanguage() {
        String name = sContext.getPackageName() + "_" + LANGUAGE;
        SharedPreferences preferences =
                sContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getString(LANGUAGE,
                Locale.getDefault().getLanguage());
    }

    public static String getAppLanguage(Context context) {
        String name = context.getPackageName() + "_" + LANGUAGE;
        SharedPreferences preferences =
                context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getString(LANGUAGE,
                Locale.getDefault().getLanguage());
    }

    public static String getAppCountry() {
        String name = sContext.getPackageName() + "_" + LANGUAGE;
        SharedPreferences preferences =
                sContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getString(COUNTRY,
                Locale.getDefault().getCountry());
    }

    public static String getAppCountry(Context context) {
        String name = context.getPackageName() + "_" + LANGUAGE;
        SharedPreferences preferences =
                context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getString(COUNTRY,
                Locale.getDefault().getCountry());
    }

    public static Locale getAppLocale() {
        return new Locale(getAppLanguage(), getAppCountry());
    }

    public static Locale getAppLocale(Context context) {
        return new Locale(getAppLanguage(context), getAppCountry(context));
    }

    public static void changeAppLanguage(String language,
                                         String country) {
        changeAppLanguage(sContext, new Locale(language, country));
    }

    public static void changeAppLanguage(Context context, String language,
                                         String country) {
        changeAppLanguage(context, new Locale(language, country));
    }

    public static void changeAppLanguage(Locale locale) {
        changeAppLanguage(sContext, locale, true);
    }

    public static void changeAppLanguage(Context context, Locale locale) {
        changeAppLanguage(context, locale, true);
    }

    public static void changeAppLanguage(Locale locale, boolean persistence) {
        changeAppLanguage(sContext, locale, persistence);
    }

    /**
     * 更改应用语言
     *
     * @param context
     * @param locale      语言地区
     * @param persistence 是否持久化
     */
    public static void changeAppLanguage(Context context, Locale locale,
                                         boolean persistence) {
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
            saveLanguageSetting(context, locale);
        }
    }

    /**
     * @param context
     * @param locale
     */
    private static void saveLanguageSetting(Context context, Locale locale) {
        String name = context.getPackageName() + "_" + LANGUAGE;
        SharedPreferences preferences =
                context.getSharedPreferences(name, Context.MODE_PRIVATE);
        preferences.edit().putString(LANGUAGE, locale.getLanguage()).apply();
        preferences.edit().putString(COUNTRY, locale.getCountry()).apply();
    }
}
