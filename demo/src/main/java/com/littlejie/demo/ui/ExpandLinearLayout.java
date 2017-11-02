package com.littlejie.demo.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.littlejie.demo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by littlejie on 2017/10/26.
 */

public class ExpandLinearLayout extends LinearLayout {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private String mTitle;
    private List<String> mStringList;

    public ExpandLinearLayout(Context context, String title) {
        super(context);
        mTitle = title;
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.layout_expand_linear, this);
        ButterKnife.bind(this);

        mStringList = new ArrayList<>();
        int size = new Random().nextInt(10);
        for (int i = 0; i < size; i++) {
            mStringList.add(String.valueOf(i));
        }

        mRecyclerView.setAdapter(new ChildAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setNestedScrollingEnabled(false);
        mTvTitle.setText(mTitle);
    }

    @OnClick(R.id.tv_title)
    void click() {
        boolean isVisible = mRecyclerView.getVisibility() == VISIBLE;
        mRecyclerView.setVisibility(isVisible ? GONE : VISIBLE);
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_child)
        TextView mTvChild;

        public ChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ChildAdapter extends RecyclerView.Adapter<ChildViewHolder> {

        @Override
        public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_expannable_child, parent, false);
            return new ChildViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChildViewHolder holder, int position) {
            holder.mTvChild.setText(mStringList.get(position));
        }

        @Override
        public int getItemCount() {
            return mStringList.size();
        }
    }
}
