package com.littlejie.core.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.littlejie.core.R;

/**
 * 通用设置 Item
 * Created by littlejie on 2017/2/15.
 */

public class ConfigItem extends LinearLayout {

    private TextView mTvTitle;
    private CheckBox mCheckBox;

    public OnCheckedListener mOnCheckedListener;

    public ConfigItem(Context context) {
        super(context);
        init(context, null);
    }

    public ConfigItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_config, this);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
        //初始化属性
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ConfigItem);
            boolean checked = array.getBoolean(R.styleable.ConfigItem_checked, false);
            mCheckBox.setChecked(checked);
            mTvTitle.setText(array.getString(R.styleable.ConfigItem_title));
            array.recycle();
        }
        initListener();
    }

    private void initListener() {
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnCheckedListener != null) {
                    mOnCheckedListener.onChecked(isChecked);
                }
            }
        });
    }

    public void setChecked(boolean checked) {
        mCheckBox.setChecked(checked);
    }

    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        mOnCheckedListener = onCheckedListener;
    }

    public interface OnCheckedListener {
        void onChecked(boolean isChecked);
    }
}
