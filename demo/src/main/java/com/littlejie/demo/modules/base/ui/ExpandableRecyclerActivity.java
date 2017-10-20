package com.littlejie.demo.modules.base.ui;

import android.animation.ObjectAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.ui.adapter.ExpandableRecyclerAdapter;
import com.littlejie.core.ui.adapter.ExpandableRecyclerAdapter.Item;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by littlejie on 2017/10/19.
 */
@Description(description = "ExpandableRecycler")
public class ExpandableRecyclerActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    private List<ExpandableRecyclerAdapter.Item<ParentInfo, ChildInfo>> mItemList;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_expandable_recycler;
    }

    @Override
    protected void initData() {
        mItemList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 500; i++) {
            ParentInfo parentInfo = new ParentInfo();
            parentInfo.setParent("parent " + i);
            int maxJ = random.nextInt(5);
            List<ChildInfo> childInfoList = new ArrayList<>();
            for (int j = 0; j < maxJ; j++) {
                ChildInfo childInfo = new ChildInfo();
                childInfo.setChild("parent " + i + ";child " + j);
                childInfoList.add(childInfo);
            }
            Item<ParentInfo, ChildInfo> item = new Item<>(parentInfo, childInfoList);
            item.setCollapse(random.nextBoolean());
            mItemList.add(item);
        }
    }

    @Override
    protected void initView() {
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void initViewListener() {
        mAdapter.setOnCollapseAnimatorListener(new ExpandableRecyclerAdapter.OnCollapseAnimatorListener() {
            @Override
            public void onCollapseAnimatorStart(ExpandableRecyclerAdapter.BaseViewHolder holder, boolean isCollapse) {
                ParentViewHolder viewHolder = (ParentViewHolder) holder;
                int start = isCollapse ? -180 : 0;
                int end = isCollapse ? 0 : -180;
                ObjectAnimator animator = ObjectAnimator.ofFloat(viewHolder.mIvArrow, "rotation", start, end);
                animator.setDuration(isCollapse ? mAdapter.getItemAddDuration() : mAdapter.getItemRemoveDuration());
                animator.start();
            }

            @Override
            public void onCollapseAnimatorFinish(ExpandableRecyclerAdapter.BaseViewHolder holder, boolean isCollapse) {
            }
        });
    }

    @Override
    protected void process() {
        mAdapter.setItemList(mItemList);
    }

    private class ParentInfo {
        String parent;

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }
    }

    private class ChildInfo {
        String child;

        public String getChild() {
            return child;
        }

        public void setChild(String child) {
            this.child = child;
        }
    }

    public class ParentViewHolder extends ExpandableRecyclerAdapter.BaseViewHolder {

        @BindView(R.id.tv_parent)
        TextView mTvParent;
        @BindView(R.id.iv_arrow)
        ImageView mIvArrow;

        public ParentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ChildViewHolder extends ExpandableRecyclerAdapter.BaseViewHolder {

        @BindView(R.id.tv_child)
        TextView mTvChild;

        public ChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MyAdapter extends ExpandableRecyclerAdapter<ParentInfo, ChildInfo> {

        @Override
        protected BaseViewHolder inflateParentViewHolder(ViewGroup parent, int viewType) {
            //必须使用parent，否则会导致match_parent无效
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_expannable_parent, parent, false);
            return new ParentViewHolder(view);
        }

        @Override
        protected BaseViewHolder inflateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_expannable_child, parent, false);
            return new ChildViewHolder(view);
        }

        @Override
        protected void bindParentViewHolder(final BaseViewHolder holder, Item item, ParentInfo parent, final int position) {
            Log.d(TAG, "bindParentViewHolder " + position);
            final ParentViewHolder viewHolder = (ParentViewHolder) holder;
            viewHolder.mTvParent.setText(parent.getParent());
            viewHolder.mIvArrow.setRotation(item.isCollapse() ? -180 : 0);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collapse(holder, position);
                }
            });
        }

        @Override
        protected void bindChildViewHolder(BaseViewHolder holder, Item item, ChildInfo child) {
            ChildViewHolder viewHolder = (ChildViewHolder) holder;
            viewHolder.mTvChild.setText(child.getChild());
        }

    }
}
