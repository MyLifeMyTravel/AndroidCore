package com.littlejie.core.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: lishengjie@bongmi.com
 */

public class WrapperRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int BASE_ITEM_TYPE_HEADER = 100000;
  private static final int BASE_ITEM_TYPE_FOOTER = 200000;

  private SparseArrayCompat<View> headerViewList = new SparseArrayCompat<>();
  private SparseArrayCompat<View> footViewList = new SparseArrayCompat<>();

  private RecyclerView.Adapter innerAdapter;

  public WrapperRecyclerAdapter() {
  }

  public WrapperRecyclerAdapter(RecyclerView.Adapter adapter) {
    innerAdapter = adapter;
  }

  public void setInnerAdapter(RecyclerView.Adapter innerAdapter) {
    this.innerAdapter = innerAdapter;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
    if (headerViewList.get(viewType) != null) {
      return new ViewHolder(headerViewList.get(viewType));

    } else if (footViewList.get(viewType) != null) {
      return new ViewHolder(footViewList.get(viewType));
    }
    return innerAdapter.onCreateViewHolder(parent, viewType);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (isHeaderViewPos(position)) {
      return;
    }
    if (isFooterViewPos(position)) {
      return;
    }
    innerAdapter.onBindViewHolder(holder, position - getHeadersCount());
  }

  @Override
  public int getItemCount() {
    return getHeadersCount() + getFootersCount() + getRealItemCount();
  }

  @Override
  public int getItemViewType(int position) {
    if (isHeaderViewPos(position)) {
      return headerViewList.keyAt(position);
    } else if (isFooterViewPos(position)) {
      return footViewList.keyAt(position -
          getHeadersCount() - getRealItemCount());
    }
    return innerAdapter.getItemViewType(position - getHeadersCount());
  }

  private int getRealItemCount() {
    return innerAdapter.getItemCount();
  }

  public boolean isHeaderViewPos(int position) {
    return position < getHeadersCount();
  }

  public boolean isFooterViewPos(int position) {
    return position >= getHeadersCount() + getRealItemCount();
  }


  public void addHeaderView(View view) {
    headerViewList.put(headerViewList.size() + BASE_ITEM_TYPE_HEADER, view);
  }

  public void removeHeaderView(View view) {
    if (headerViewList.indexOfValue(view) != -1) {
      headerViewList.removeAt(headerViewList.indexOfValue(view));
    }
  }

  public void addFootView(View view) {
    footViewList.put(footViewList.size() + BASE_ITEM_TYPE_FOOTER, view);
  }

  public void removeFooterView(View view) {
    if (footViewList.indexOfValue(view) != -1) {
      footViewList.removeAt(footViewList.indexOfValue(view));
    }
  }

  public int getHeadersCount() {
    return headerViewList.size();
  }

  public int getFootersCount() {
    return footViewList.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    public ViewHolder(View itemView) {
      super(itemView);
    }
  }
}
