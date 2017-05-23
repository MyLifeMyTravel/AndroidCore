package com.littlejie.password.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.littlejie.password.Constants;
import com.littlejie.password.OnFinishListener;
import com.littlejie.password.PasswordManager;
import com.littlejie.password.R;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */

public class SetPwdView extends LinearLayout {

    private static final int[] KEYBOARD_VALUE = Constants.KEYBOARD_VALUE;
    private static final int KEYBOARD_SIZE = KEYBOARD_VALUE.length;
    private TextView tvTitle;
    private LinearLayout groupPassword;
    private TextView tvTip;
    private TextView tvDesc;
    private GridView gvKeyboard;
    private GridAdapter adapter;

    private int pwdLength;
    private int pwdColor;
    //密码，用于缓存
    private StringBuilder password;
    private OnFinishListener onFinishListener;

    public SetPwdView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        pwdLength = PasswordManager.getInstance().getPasswordLength();

        View view = View.inflate(context, R.layout.layout_set_pwd, this);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        groupPassword = (LinearLayout) view.findViewById(R.id.group_password);
        tvTip = (TextView) view.findViewById(R.id.tv_tip);
        tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        gvKeyboard = (GridView) view.findViewById(R.id.gv_keyboard);

        final TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.SetPwdView);
        pwdColor = t.getColor(R.styleable.SetPwdView_password_color,
                getResources().getColor(R.color.main_color));
        t.recycle();
        initPassword();
        initKeyboard();
    }

    private int position = 0;

    private void initPassword() {
        password = new StringBuilder();
        groupPassword.removeAllViews();
        int padding = getResources().getDimensionPixelSize(R.dimen.padding);
        for (int i = 0; i < pwdLength; i++) {
            final TextView textView = new TextView(getContext());
            //设置外边距
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = padding;
            params.rightMargin = padding;
            textView.setLayoutParams(params);
            //设置背景
            textView.setBackgroundResource(R.drawable.bg_password_edit);
            //设置字体大小
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            //设置最多显示字数
            textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            //设置TextView最小字符宽度
            textView.setMinEms(1);
            //居中显示
            textView.setGravity(Gravity.CENTER);
            //输入类型为数字密码
            //textView.setInputType(EditorInfo.TYPE_CLASS_NUMBER
            //        | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
            textView.setTextColor(pwdColor);
            groupPassword.addView(textView);
        }
    }

    private void initKeyboard() {
        adapter = new GridAdapter();
        gvKeyboard.setAdapter(adapter);
        gvKeyboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                if (p == KEYBOARD_SIZE - 1) {//点击删除按钮
                    if (position == 0) {
                        return;
                    }
                    ((TextView) groupPassword.getChildAt(--position)).setText("");
                } else if (p != KEYBOARD_SIZE - 3) {
                    if (position == pwdLength) {//当密码已经输入完成时，直接退出处理
                        return;
                    }
                    if (position <= pwdLength - 1) {//当密码尚未输入完成时，将对应按键的数字放入密码框
                        ((TextView) groupPassword.getChildAt(position++))
                                .setText(String.valueOf(KEYBOARD_VALUE[p]));
                    }
                    //如果密码设置完成，则进行回调
                    if (onFinishListener != null && position == pwdLength) {
                        onFinishListener.onFinish(getPassword());
                    }
                }
            }
        });
    }

    private String getPassword() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pwdLength; i++) {
            sb.append(((TextView) groupPassword.getChildAt(i)).getText());
        }
        return sb.toString();
    }

    public void resetPassword() {
        for (int i = 0; i < pwdLength; i++) {
            ((TextView) groupPassword.getChildAt(i)).setText("");
        }
        position = 0;
        password = new StringBuilder();
    }

    public void setTitle(@StringRes int title) {
        tvTitle.setText(title);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTip(@StringRes int tip) {
        tvTip.setVisibility(VISIBLE);
        tvTip.setText(tip);
    }

    public void setTip(String tip) {
        tvTip.setVisibility(VISIBLE);
        tvTip.setText(tip);
    }

    public void setDesc(@StringRes int desc) {
        tvDesc.setVisibility(VISIBLE);
        tvDesc.setText(desc);
    }

    private class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return KEYBOARD_SIZE;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            //1代表数字按钮，2代表删除按钮
            if (position == KEYBOARD_SIZE - 1) {
                return 2;
            }
            return 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            Log.d("TAG", "position = " + position + "; type = " + type);
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                ViewHolder holder = new ViewHolder();
                if (type == 2) {
                    convertView = inflater.inflate(R.layout.item_keyboard_delete, parent, false);
                } else {
                    convertView = inflater.inflate(R.layout.item_keyborad, parent, false);
                    holder.tvDigit = (TextView) convertView.findViewById(R.id.tv_digit);
                }
                convertView.setTag(holder);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (KEYBOARD_VALUE[position] == -1) {
                //将左下角的按钮置为
                holder.tvDigit.setEnabled(false);
            } else if (KEYBOARD_VALUE[position] != -2) {
                convertView.setBackgroundColor(Color.WHITE);
                holder.tvDigit.setEnabled(true);
                holder.tvDigit.setText(String.valueOf(KEYBOARD_VALUE[position]));
            }
            return convertView;
        }

    }

    private static class ViewHolder {
        TextView tvDigit;
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }
}
