package com.littlejie.demo.modules.base.ui;

import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.ui.WeightView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */
@Description(description = "体重View")
public class WeightViewActivity extends BaseActivity {

    @BindView(R.id.weight_view)
    WeightView weightView;
    @BindView(R.id.tv_weight)
    TextView tvWeight;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_weight_view;
    }

    @Override
    protected void initData() {
        weightView.setData(50, true);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {
        weightView.setOnValueChangeListener(new WeightView.OnValueChangeListener() {
            @Override
            public void onValueChanged(float value) {
                weight = value;
                tvWeight.setText(String.valueOf(value));
            }
        });
    }

    private int unit = 1;
    private float weight = 50;

    private float scale = 2.2f;

    @OnClick(R.id.btn_change_unit)
    void changeUnit() {
        if (unit == 1) {
            unit = 2;
            weight *= scale;
            weightView.setRange(0, 220);
            weightView.setData(weight, true);
        } else {
            unit = 1;
            weight /= scale;
            weightView.setRange(0, 100);
            weightView.setData(weight, true);
        }
    }

    @Override
    protected void process() {

    }
}
