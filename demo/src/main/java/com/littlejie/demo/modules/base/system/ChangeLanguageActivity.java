package com.littlejie.demo.modules.base.system;

import android.content.Intent;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.LanguageUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.annotation.Title;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */
@Title(title = "应用内切换语言")
@Description(description = "应用内切换语言")
public class ChangeLanguageActivity extends BaseActivity {

    @BindView(R.id.tv_language)
    TextView tvLanguage;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_change_language;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tvLanguage.setText(getString(R.string.current_language, LanguageUtil.getAppLanguage(this)));
    }

    @Override
    protected void initViewListener() {
    }

    @OnClick(R.id.btn_chinese)
    void switch2Chinese() {
        LanguageUtil.changeAppLanguage(this, "zh", "CN");
        restartApplication();
    }

    @OnClick(R.id.btn_english)
    void switch2English() {
        LanguageUtil.changeAppLanguage(this, "en", "");
        restartApplication();
    }

    private void restartApplication() {
        //切换语言信息，需要重启 Activity 才能实现
        Intent intent = new Intent(this, ChangeLanguageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void process() {

    }
}
