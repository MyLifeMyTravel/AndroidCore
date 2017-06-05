package com.littlejie.switchlanguage;

import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.LanguageUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * zh-rSG默认会适配到zh-rCN
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.tv_string_in_memory)
    TextView tvStringInMemory;
    @BindView(R.id.tv_locale_code)
    TextView tvLocaleCode;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tvLanguage.setText(getString(R.string.current_language, LanguageUtil.getAppLanguage(this)));
        tvStringInMemory.setText(App.getStringInMemory());
        tvLocaleCode.setText(getString(R.string.locale_code));
    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.btn_chinese)
    void switch2Chinese() {
        LanguageUtil.changeAppLanguage(this, Locale.SIMPLIFIED_CHINESE);
        restartApplication();
    }

    @OnClick(R.id.btn_english)
    void switch2English() {
        LanguageUtil.changeAppLanguage(this, Locale.ENGLISH);
        restartApplication();
    }

    @OnClick(R.id.btn_jump)
    void jump() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void restartApplication() {
        //切换语言信息，需要重启 Activity 才能实现
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged");
    }

    @Override
    protected void process() {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "默认语言1 = " + Locale.getDefault().getLanguage() + ";默认语言2 = " + getResources().getConfiguration().locale.getLanguage());
        Log.d(TAG, "默认国家 = " + Locale.getDefault().getCountry());
    }
}
