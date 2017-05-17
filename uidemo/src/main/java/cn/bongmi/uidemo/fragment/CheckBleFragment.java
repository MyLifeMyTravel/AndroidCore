package cn.bongmi.uidemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bongmi.uidemo.BindStep;
import cn.bongmi.uidemo.R;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */

public class CheckBleFragment extends BaseBindFragment {

    public static CheckBleFragment newInstance() {

        Bundle args = new Bundle();

        CheckBleFragment fragment = new CheckBleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_ble, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_next)
    void next() {
        if (onStepFinishListener != null) {
            onStepFinishListener.onStepFinish(BindStep.CHECK_BLUETOOTH, true);
        }
    }

}