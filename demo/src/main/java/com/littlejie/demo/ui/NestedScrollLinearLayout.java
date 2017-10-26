package com.littlejie.demo.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.littlejie.demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by littlejie on 2017/10/24.
 */

public class NestedScrollLinearLayout extends LinearLayout implements NestedScrollingParent {

    private static final String TAG = NestedScrollLinearLayout.class.getSimpleName();

    private MultiplyTextView mMultiplyTextView;
    private TextView mSingleTextView;
    private NestedViewPager mNestedViewPager;
    private NestedScrollingParentHelper mNestedScrollingParentHelper;

    private final MultiplyTextView.OnItemClickListener mOnItemClickListener = new MultiplyTextView.OnItemClickListener() {
        @Override
        public void onItemClick(View item, int position) {
            mSingleTextView.setBackgroundColor(Color.GREEN);
            mSingleTextView.setText(String.valueOf(position));
            mNestedViewPager.setCurrentItem(position);
        }
    };

    public NestedScrollLinearLayout(Context context) {
        this(context, null);
    }

    public NestedScrollLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMultiplyTextView = (MultiplyTextView) findViewById(R.id.multiply);
        mSingleTextView = (TextView) findViewById(R.id.single);
        mNestedViewPager = (NestedViewPager) findViewById(R.id.viewPager);
        mMultiplyTextView.setOnItemClickListener(mOnItemClickListener);
        initItemData();
        initView();
    }

    private Map<Integer, List<String>> mItemMap;

    private void initView() {
        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_nested_viewpager, null);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            ItemAdapter itemAdapter = new ItemAdapter(mItemMap.get(i));
            recyclerView.setAdapter(itemAdapter);
            viewList.add(view);
        }
        mNestedViewPager.setAdapter(new NestedPagerAdapter(viewList));
    }

    private void initItemData() {
        mItemMap = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            List<String> itemList = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                itemList.add("page " + i + ";item " + j);
            }
            mItemMap.put(i, itemList);
        }
    }

    /*----------以下为NestedScrollingParent实现---------*/
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        Log.d(TAG, "onNestedPreScroll,dx = " + dx + ";dy = " + dy);
        Log.d(TAG, "getScrollY = " + getScrollY());
        //dy>0,上滑；dy<0,下滑
        if (show(dy) || hide(dy)) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    private boolean show(int dy) {
        if (dy < 0) {
            if (getScrollY() >= 0 && mNestedViewPager.getScrollY() == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean hide(int dy) {
        if (dy > 0) {
            if (getScrollY() <= mMultiplyTextView.getMeasuredHeight()) {
                return true;
            }
        }
        return false;
    }

    //scrollBy内部会调用scrollTo
    //限制滚动范围
    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mMultiplyTextView.getMeasuredHeight()) {
            y = mMultiplyTextView.getMeasuredHeight();
        }

        super.scrollTo(x, y);
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    /*----------Adapter--------*/
    private class NestedPagerAdapter extends PagerAdapter {

        private List<View> mViewList;

        public NestedPagerAdapter(List<View> viewList) {
            mViewList = viewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private List<String> mItemList;

        public ItemAdapter(List<String> itemList) {
            mItemList = itemList;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.tvItem.setText(mItemList.get(position));
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }

    /*---------ViewHolder---------*/
    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item)
        TextView tvItem;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
