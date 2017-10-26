package com.littlejie.demo.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.littlejie.core.util.DisplayUtil;

/**
 * Created by littlejie on 2017/10/24.
 */

public class MultiplyTextView extends LinearLayout {

    private OnItemClickListener mOnItemClickListener;

    public MultiplyTextView(Context context) {
        this(context, null);
    }

    public MultiplyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        initView(context);
    }

    private void initView(Context context) {
        for (int i = 0; i < 4; i++) {
            TextView textView = new TextView(context);
            ViewGroup.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = DisplayUtil.dp2px(context, 30);
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(Color.BLUE);
            textView.setText(String.valueOf(i));
            textView.setOnClickListener(mOnClickListener);
            textView.setTag(i);
            addView(textView);
        }
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).setBackgroundColor(Color.BLUE);
            }
            v.setBackgroundColor(Color.GREEN);
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
            }
        }
    };

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View item, int position);
    }
}
