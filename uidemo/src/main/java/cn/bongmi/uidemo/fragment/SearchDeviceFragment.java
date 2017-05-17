package cn.bongmi.uidemo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bongmi.uidemo.R;
import cn.bongmi.uidemo.SearchDeviceImageView;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */

public class SearchDeviceFragment extends BaseBindFragment {

    @BindView(R.id.iv_search_ble)
    SearchDeviceImageView ivSearch;

    private Handler handler = new Handler();

    public static SearchDeviceFragment newInstance() {

        Bundle args = new Bundle();

        SearchDeviceFragment fragment = new SearchDeviceFragment();
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
        return inflater.inflate(R.layout.fragment_search_ble,
                container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handler.postDelayed(scanRunnable, 100);
    }

    @OnClick(R.id.tv_search)
    void t() {
        ivSearch.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        ivSearch.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private final Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            ivSearch.start();
        }
    };

}
