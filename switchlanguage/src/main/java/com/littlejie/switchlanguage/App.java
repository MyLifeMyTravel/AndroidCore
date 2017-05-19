package com.littlejie.switchlanguage;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.littlejie.core.util.LanguageUtil;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */

public class App extends Application {

    public static String STRING_IN_MEMORY;

    @Override
    public void onCreate() {
        super.onCreate();
        STRING_IN_MEMORY = getString(R.string.string_in_memory);
        LanguageUtil.init(this);
        registerActivityLifecycleCallbacks(callbacks);
    }

    public static String getStringInMemory() {
        return STRING_IN_MEMORY;
    }

    ActivityLifecycleCallbacks callbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            //强制修改应用语言
            if (!LanguageUtil.isSameWithSetting(activity)) {
                LanguageUtil.changeAppLanguage(activity,
                        LanguageUtil.getAppLocale(activity));
                Log.d("TAG", "language = " + LanguageUtil.getAppLocale(activity).getLanguage()
                        + ";country = " + LanguageUtil.getAppLocale(activity).getCountry());
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };
}
