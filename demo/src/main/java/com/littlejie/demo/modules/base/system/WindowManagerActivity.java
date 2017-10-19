package com.littlejie.demo.modules.base.system;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */

@Description(description = "WindowManager")
public class WindowManagerActivity extends BaseActivity {

    @BindView(R.id.btn_add_view)
    Button btnAddView;
    private ImageView imageView;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_window_manager;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        imageView = new ImageView(this);
        imageView.setBackgroundResource(R.mipmap.ic_launcher);
    }

    @Override
    protected void initViewListener() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getWindowManager().removeView(v);
                showPopupWindow();
            }
        });
    }

    private void showPopupWindow() {
        TextView textView = new TextView(WindowManagerActivity.this);
        textView.setText("PopupWindow");
        new PopupWindow(textView, 200, 200)
                .showAtLocation(WindowManagerActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    @OnClick(R.id.btn_add_view)
    void addViewToWindow() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        if ((params.softInputMode
//                & WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION) == 0) {
//            WindowManager.LayoutParams nl = new WindowManager.LayoutParams();
//            nl.copyFrom(params);
//            nl.softInputMode |=
//                    WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION;
//            params = nl;
//        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.gravity = Gravity.CENTER;
        params.format = PixelFormat.TRANSLUCENT;
//        windowManager.addView(imageView, params);
        showPopupWindow();
    }

    @OnClick(R.id.btn_show_dialog)
    void showDialog() {
        new AlertDialog.Builder(this)
                .setMessage("AlertDialog")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showPopupWindow();
                    }
                })
                .show();
        showPopupWindow();
    }

    @Override
    protected void process() {

    }
}
