package com.littlejie.core.ui.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by littlejie on 2017/10/19.
 */

public abstract class ExpandableRecyclerAdapter<K, V>
        extends RecyclerView.Adapter<ExpandableRecyclerAdapter.BaseViewHolder> {

    private static final String TAG = ExpandableRecyclerAdapter.class.getSimpleName();

    private static final int TYPE_PARENT = 1;
    private static final int TYPE_CHILD = 2;

    private List<Item<K, V>> mItemList;
    private long mItemAddDuration;
    private long mItemRemoveDuration;

    private OnCollapseAnimatorListener mOnCollapseAnimatorListener;
    private Handler mHandler = new Handler();

    public void setItemList(List<Item<K, V>> itemList) {
        mItemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder,viewType = " + getViewTypeString(viewType));
        initItemAnimatorDuration(parent);
        if (viewType == TYPE_PARENT) {
            return inflateParentViewHolder(parent, viewType);
        } else if (viewType == TYPE_CHILD) {
            return inflateChildViewHolder(parent, viewType);
        }
        return null;
    }

    private void initItemAnimatorDuration(ViewGroup parent) {
        if (mItemAddDuration != 0 && mItemRemoveDuration != 0) {
            return;
        }
        if (!(parent instanceof RecyclerView)) {
            return;
        }
        RecyclerView recyclerView = (RecyclerView) parent;
        mItemAddDuration = recyclerView.getItemAnimator().getAddDuration();
        mItemRemoveDuration = recyclerView.getItemAnimator().getRemoveDuration();
        Log.d(TAG, "mItemAddDuration = " + mItemAddDuration + ";mItemRemoveDuration = " + mItemRemoveDuration);
    }

    protected abstract BaseViewHolder inflateParentViewHolder(ViewGroup parent, int viewType);

    protected abstract BaseViewHolder inflateChildViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Log.d(TAG, "onBindViewHolder,position:" + position
                + ";viewType is " + getViewTypeString(viewType));
        int parentPosition = getParentPosition(position);
        if (viewType == TYPE_PARENT) {
            bindParentViewHolder(holder, mItemList.get(parentPosition), getParent(position), parentPosition);
        } else if (viewType == TYPE_CHILD) {
            bindChildViewHolder(holder, mItemList.get(parentPosition), getChild(position));
        }
    }

    private String getViewTypeString(int viewType) {
        return viewType == TYPE_PARENT
                ? "Parent" : (viewType == TYPE_CHILD ? "Child" : "Other");
    }

    /**
     * @param holder
     * @param item     item数据
     * @param parent   父Item 的数据
     * @param position 父Item的位置，排除子Item
     */
    protected abstract void bindParentViewHolder(BaseViewHolder holder, Item item, K parent, int position);

    /**
     * @param holder
     * @param item   item数据
     * @param child  子Item 的数据
     */
    protected abstract void bindChildViewHolder(BaseViewHolder holder, Item item, V child);

    @Override
    public int getItemCount() {
        int parentCount = mItemList.size();
        int childCount = 0;
        for (Item wrapper : mItemList) {
            if (!wrapper.isCollapse()) {
                childCount += wrapper.getChildList().size();
            }
        }
        return parentCount + childCount;
    }

    @Override
    public int getItemViewType(int position) {
        int count = 0;
        for (Item wrapper : mItemList) {
            if (count++ == position) {
                return TYPE_PARENT;
            }
            if (!wrapper.isCollapse()) {
                count += wrapper.getChildList().size();
            }
            if (position < count) {
                return TYPE_CHILD;
            }
        }
        return TYPE_PARENT;
    }

    private int getParentPosition(int position) {
        int start = 0;
        for (int i = 0; i < mItemList.size(); i++) {
            Item item = mItemList.get(i);
            start++;
            if (!item.isCollapse()) {
                start += item.getChildList().size();
            }
            if (position < start) {
                return i;
            }
        }
        return 0;
    }

    private int getChildPositionInParent(int position) {
        int parentPosition = getParentPosition(position);
        for (int i = 0; i < parentPosition; i++) {
            Item item = mItemList.get(i);
            position -= 1;
            if (!item.isCollapse()) {
                position -= item.getChildList().size();
            }
        }
        return position - 1;
    }

    private K getParent(int position) {
        return mItemList.get(getParentPosition(position)).getParent();
    }

    private V getChild(int position) {
        Item<K, V> item = mItemList.get(getParentPosition(position));
        return item.getChildList().get(getChildPositionInParent(position));
    }

    public void collapse(BaseViewHolder holder, int position) {
        Item<K, V> item = mItemList.get(position);
        int childCount = item.getChildList().size();
        int start = getCountByParentPosition(position) + 1;
        Log.d(TAG, "collapse,parent item = " + position
                + ";start = " + start + ";childCount = " + childCount);
        if (item.isCollapse()) {
            notifyItemRangeInserted(start, childCount);
        } else {
            notifyItemRangeRemoved(start, childCount);
        }
        notifyCollapseAnimator(holder, mItemRemoveDuration, item.isCollapse());
        item.setCollapse(!item.isCollapse());
    }

    private void notifyCollapseAnimator(final BaseViewHolder holder, long delay, final boolean isCollapse) {
        if (mOnCollapseAnimatorListener == null) {
            return;
        }
        mOnCollapseAnimatorListener.onCollapseAnimatorStart(holder, isCollapse);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mOnCollapseAnimatorListener.onCollapseAnimatorFinish(holder, !isCollapse);
            }
        }, delay);
    }

    private int getCountByParentPosition(int position) {
        int count = 0;
        for (int i = 0; i < position; i++) {
            count++;
            Item item = mItemList.get(i);
            if (!item.isCollapse()) {
                count += item.getChildList().size();
            }
        }
        return count;
    }

    public void setOnCollapseAnimatorListener(OnCollapseAnimatorListener onCollapseAnimatorListener) {
        mOnCollapseAnimatorListener = onCollapseAnimatorListener;
    }

    public interface OnCollapseAnimatorListener {
        /**
         * 折叠、展开动画开始
         *
         * @param isCollapse false表示展开，true表示折叠
         */
        void onCollapseAnimatorStart(BaseViewHolder holder, boolean isCollapse);

        /**
         * 折叠、展开动画结束
         *
         * @param isCollapse false表示已经折叠，true表示已经展开
         */
        void onCollapseAnimatorFinish(BaseViewHolder holder, boolean isCollapse);
    }

    public long getItemAddDuration() {
        return mItemAddDuration;
    }

    public long getItemRemoveDuration() {
        return mItemRemoveDuration;
    }

    public static class Item<K, V> {
        private boolean collapse;
        private K parent;
        private List<V> childList;

        public Item(K parent, List<V> childList) {
            this.parent = parent;
            this.childList = childList;
        }

        public boolean isCollapse() {
            return collapse;
        }

        public void setCollapse(boolean collapse) {
            this.collapse = collapse;
        }

        public K getParent() {
            return parent;
        }

        public void setParent(K parent) {
            this.parent = parent;
        }

        public List<V> getChildList() {
            return childList;
        }

        public void setChildList(List<V> childList) {
            this.childList = childList;
        }

    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

}
