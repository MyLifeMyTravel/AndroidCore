package com.littlejie.core.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * BaseFragment,所有Fragment的基类
 * Created by littlejie on 2016/4/23.
 */
public abstract class BaseFragment extends Fragment {

    protected final String TAG = this.getClass().getSimpleName();

    protected abstract int getPageLayoutID();

    protected abstract void initData();

    protected abstract void initView(View view, Bundle savedInstanceState);

    protected abstract void initViewListener();

    protected abstract void process(Bundle savedInstanceState);

    /**
     * 创建菜单，封装解决 Fragment 与 ViewPager 使用时创建菜单不正确的问题
     *
     * @param menu
     * @param inflater
     */
    protected void createOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //如果当前 Fragment 在 ViewPager 中为可见状态，则让 Activity 重绘菜单
        if (isVisibleToUser && getContext() != null) {
            ((Activity) getContext()).invalidateOptionsMenu();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(getPageLayoutID(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        ButterKnife.bind(this, view);
        initView(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        initViewListener();
        process(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //1.判断是否有 Parent，若无，则表明是直接 attach 在 Activity 下
        //2.判断 getUserVisibleHint() 是否为 true，若为 true ，则表示对用户可见
        //综合 1 、 2 ，可以判断出是否需要创建菜单
        boolean needCreate = getParentFragment() == null || getParentFragment().getUserVisibleHint();
        if (needCreate) {
            createOptionsMenu(menu, inflater);
        }
    }

}
