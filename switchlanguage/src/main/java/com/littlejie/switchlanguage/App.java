package com.littlejie.switchlanguage;

import android.app.Application;

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
        LanguageUtil.changeAppLanguage(this, LanguageUtil.getAppLocale(this));
    }

    public static String getStringInMemory() {
        return STRING_IN_MEMORY;
    }
}
