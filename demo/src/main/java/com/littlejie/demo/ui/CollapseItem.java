package com.littlejie.demo.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.littlejie.demo.R;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */

public class CollapseItem extends LinearLayout implements View.OnClickListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    private ViewGroup titleView;
    private ViewGroup contentView;
    private ViewGroup tipView;
    private TextView tvTitle;
    private ImageView ivIcon;
    private TextView tvContent;
    private TextView tvTip;

    private CharSequence title;
    private CharSequence content;
    private CharSequence tip;

    private boolean collapse;
    private int collapseIcon;
    private int contentHeight;

    public CollapseItem(Context context) {
        super(context);
        init(context, null);
    }

    public CollapseItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_collapse, this);
        titleView = (ViewGroup) view.findViewById(R.id.title);
        contentView = (ViewGroup) view.findViewById(R.id.content);
        tipView = (ViewGroup) view.findViewById(R.id.tip);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        tvTip = (TextView) view.findViewById(R.id.tv_tip);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);

        if (attrs != null) {
            TypedArray t = context.obtainStyledAttributes(attrs,
                    R.styleable.CollapseItem);

            title = t.getString(R.styleable.CollapseItem_title);
            setTitle(title);

            content = t.getString(R.styleable.CollapseItem_content);
            setContent(content);

            tip = t.getString(R.styleable.CollapseItem_tip);
            if (TextUtils.isEmpty(tip)) {
                tipView.setVisibility(GONE);
            } else {
                setTip(tip);
            }

            collapse = t.getBoolean(R.styleable.CollapseItem_collapse, false);
            collapseIcon = t.getResourceId(R.styleable.CollapseItem_icon_collapse,
                    R.mipmap.icon_right_arrow);
            t.recycle();
        }

        ivIcon.setBackgroundResource(collapseIcon);
        titleView.setOnClickListener(this);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public CharSequence getTitle() {
        return title;
    }

    private void setTitle(CharSequence title) {
        this.title = title;
        tvTitle.setText(title);
    }

    public CharSequence getContent() {
        return content;
    }

    private void setContent(CharSequence content) {
        this.content = content;
        tvContent.setText(content);
    }

    private void setContentVisibility(boolean visibility) {
        contentView.setVisibility(visibility ? VISIBLE : GONE);
    }

    private void setTip(CharSequence tip) {
        this.tip = tip;
        tvTip.setText(tip);
    }

    public void rotateCollapseIcon(boolean collapse) {
        rotateCollapseIcon(collapse, 500);
    }

    public void rotateCollapseIcon(boolean collapse, int time) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(ivIcon, "rotation",
                collapse ? 0 : 90, collapse ? 90 : 0);
        rotation.setDuration(time);
        rotation.start();
    }

    private void collapseContent(final boolean collapse) {
        collapseContent(collapse, 1000);
    }

    private void collapseContent(final boolean collapse, int time) {
        int start = collapse ? 0 : contentHeight;
        int end = collapse ? contentHeight : 0;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setDuration(time);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = contentView.getLayoutParams();
                params.height = height;
                contentView.setLayoutParams(params);
                if (height == 0 && !collapse) {
                    setContentVisibility(false);
                }
            }
        });
        valueAnimator.start();
        if (collapse) {
            setContentVisibility(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title:
                collapse = !collapse;
                rotateCollapseIcon(collapse);
                collapseContent(collapse);
                break;
            default:
                break;
        }
    }

    @Override
    public void onGlobalLayout() {
        contentHeight = contentView.getHeight();
        rotateCollapseIcon(collapse, 0);
        collapseContent(collapse, 0);
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
