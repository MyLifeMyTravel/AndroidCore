package com.littlejie.tablayout;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.littlejie.core.util.ToastUtil;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */

public class CustomTab {

    private Context context;
    private TabLayout tabLayout;

    private TextView tvTab;
    private ImageView ivTab;
    private TabLayout.Tab tab;

    public CustomTab(Context context, TabLayout tabLayout) {
        this.context = context;
        this.tabLayout = tabLayout;
        init();
    }

    private void init() {
        View view = View.inflate(context, R.layout.custom_tab, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvTab = (TextView) view.findViewById(R.id.tv_tab);
        ivTab = (ImageView) view.findViewById(R.id.iv_tab);

        tab = tabLayout.newTab();
        tab.setCustomView(view);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab t) {
                if (tab == t) {
                    tvTab.setSelected(true);
                    ivTab.setClickable(true);
                    ivTab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tab.isSelected()) {
                                ToastUtil.showDefaultToast("点击图标");
                            }
                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab t) {
                if (tab == t) {
                    tvTab.setSelected(false);
                }
                ivTab.setClickable(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab t) {

            }
        });

    }

    public void setTab(String text) {
        tvTab.setText(text);
    }

    public void setIcon(int icon) {
        ivTab.setVisibility(View.VISIBLE);
        ivTab.setImageResource(icon);
    }

    public void addToTabLayout() {
        tabLayout.addTab(tab);
    }

}
