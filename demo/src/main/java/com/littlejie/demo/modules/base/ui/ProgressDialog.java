package com.littlejie.demo.modules.base.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.littlejie.demo.R;


public class ProgressDialog extends AlertDialog {

    private ProgressBar progressBar;
    private TextView tvMessage;

    private CharSequence message;

    private final Handler handler = new Handler();

    public ProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    private void init() {
        if (TextUtils.isEmpty(message)){
            message = "正在处理，请稍后";
        }
        setMessage(message);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getContext().getResources().getColor(R.color.primary_color),
                PorterDuff.Mode.MULTIPLY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.progress_dialog, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        tvMessage = (TextView) view.findViewById(R.id.message);
        setView(view);
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setMessage(CharSequence message) {
        if (progressBar != null) {
            tvMessage.setText(message);
        } else {
            this.message = message;
        }
    }

    public void dismissDelay() {
        handler.postDelayed(runnable, 300);
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isShowing()) {
                dismiss();
            }
        }
    };
}

