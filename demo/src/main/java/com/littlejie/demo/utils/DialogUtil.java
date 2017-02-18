package com.littlejie.demo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.littlejie.core.base.Core;
import com.littlejie.demo.R;
import com.littlejie.demo.ui.adapter.SingleChoiceAdapter;

/**
 * Created by littlejie on 2017/2/15.
 */

public class DialogUtil {

    public static void showDialogWithOKCancel(Context context,
                                              int title, int message,
                                              View.OnClickListener positiveListener,
                                              View.OnClickListener negativeListener) {
        showDialog(context, context.getString(title), context.getString(message),
                true, null, positiveListener, true, null, negativeListener);
    }

    public static void showDialogWithOKCancel(Context context,
                                              CharSequence title, CharSequence message,
                                              View.OnClickListener positiveListener,
                                              View.OnClickListener negativeListener) {
        showDialog(context, title, message, true, null, positiveListener, true, null, negativeListener);
    }

    public static void showDialogWithOK(Context context, int resId,
                                        View.OnClickListener positiveListener) {
        showDialog(context, null, context.getString(resId), true, null, positiveListener, false, null, null);
    }

    public static void showDialogWithOK(Context context, int title, int message,
                                        View.OnClickListener positiveListener) {
        showDialog(context, context.getString(title), context.getString(message), true, null, positiveListener, false, null, null);
    }

    public static void showDialogWithOK(Context context, CharSequence title, CharSequence message,
                                        View.OnClickListener positiveListener) {
        showDialog(context, title, message, true, null, positiveListener, false, null, null);
    }

    public static void showDialog(Context context, int title, int message,
                                  boolean showPositive, int positive, final View.OnClickListener positiveListener,
                                  boolean showNegative, int negative, final View.OnClickListener negativeListener) {
        showDialog(context, context.getString(title), context.getString(message),
                showPositive, context.getString(positive), positiveListener,
                showNegative, context.getString(negative), negativeListener);
    }

    public static void showDialog(Context context, CharSequence title, CharSequence message,
                                  boolean showPositive, CharSequence positive, final View.OnClickListener positiveListener,
                                  boolean showNegative, CharSequence negative, final View.OnClickListener negativeListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_message);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }

        if (TextUtils.isEmpty(message)) {
            tvMsg.setVisibility(View.GONE);
        } else {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(message);
        }

        btnOk.setVisibility(showPositive ? View.VISIBLE : View.GONE);
        btnOk.setText(TextUtils.isEmpty(positive) ? context.getString(R.string.btn_ok) : positive);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (positiveListener != null) {
                    positiveListener.onClick(v);
                }
            }
        });

        btnCancel.setVisibility(showNegative ? View.VISIBLE : View.GONE);
        btnCancel.setText(TextUtils.isEmpty(positive) ? context.getString(R.string.btn_cancel) : negative);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (negativeListener != null) {
                    negativeListener.onClick(v);
                }
            }
        });
    }

    public static void showCustomSingleChoice(Context context, CharSequence title, final CharSequence[] items, int checkedItem, final DialogInterface.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_single_choice, null);
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        ListView lv = (ListView) view.findViewById(R.id.lv);
        tvTitle.setText(title);
        final SingleChoiceAdapter adapter = new SingleChoiceAdapter(context, items, checkedItem);
        lv.setAdapter(adapter);
//        lv.setAdapter(new ArrayAdapter<CharSequence>(context, android.R.layout.simple_list_item_1, items));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                adapter.setCheckedItem(position);
                //延迟0.5s发送消息，让用户看到点击效果
                Core.runOnUIThreadDelayed(500, new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onClick(dialog, position);
                        }
                    }
                });
            }
        });
    }
}
