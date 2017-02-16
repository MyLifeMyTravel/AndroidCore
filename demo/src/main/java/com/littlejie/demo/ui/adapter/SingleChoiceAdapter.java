package com.littlejie.demo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.littlejie.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by littlejie on 2017/2/15.
 */

public class SingleChoiceAdapter extends BaseAdapter {

    private Context mContext;
    private CharSequence[] mChoices;
    private int mCheckedItem;

    public SingleChoiceAdapter(Context context, CharSequence[] choices, int checkedItem) {
        mContext = context;
        mChoices = choices;
        mCheckedItem = checkedItem;
    }

    public void setCheckedItem(int checkedItem) {
        mCheckedItem = checkedItem;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mChoices == null ? 0 : mChoices.length;
    }

    @Override
    public Object getItem(int position) {
        return mChoices[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_custom_single_choice, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        vh.title.setText(mChoices[position]);
        vh.radio.setChecked(position == mCheckedItem);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.radio)
        RadioButton radio;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
