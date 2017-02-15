package com.littlejie.demo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.littlejie.core.base.Core;
import com.littlejie.demo.R;
import com.littlejie.demo.ui.adapter.SingleChoiceAdapter;

/**
 * Created by littlejie on 2017/2/15.
 */

public class DialogUtil {

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
