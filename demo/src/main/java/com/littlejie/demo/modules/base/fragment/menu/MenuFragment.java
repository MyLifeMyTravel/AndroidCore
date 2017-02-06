package com.littlejie.demo.modules.base.fragment.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.littlejie.core.base.BaseFragment;
import com.littlejie.demo.R;
import com.littlejie.demo.utils.Constant;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/1/22.
 */

public class MenuFragment extends BaseFragment {

    @BindView(R.id.tv_content)
    TextView mTvContent;
    private int mMenuID;
    private String mTitle;
    private boolean hasOptionsMenu;

    public static MenuFragment newInstance(boolean hasOptionsMenu) {

        Bundle args = new Bundle();

        MenuFragment fragment = new MenuFragment();
        args.putBoolean(Constant.EXTRA_MENU, hasOptionsMenu);
        fragment.setArguments(args);
        return fragment;
    }

    public static MenuFragment newInstance(int menu, String title) {

        Bundle args = new Bundle();

        MenuFragment fragment = new MenuFragment();
        args.putInt(Constant.EXTRA_ID, menu);
        args.putString(Constant.EXTRA_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: " + mTitle + "; isVisibleToUser = " + isVisibleToUser);
    }

    @Override
    protected int getPageLayoutID() {
        return R.layout.fragment_menu;
    }

    @Override
    protected void initData() {
        hasOptionsMenu = getArguments().getBoolean(Constant.EXTRA_MENU, true);
        if (hasOptionsMenu) {
            setHasOptionsMenu(hasOptionsMenu);
            mMenuID = getArguments().getInt(Constant.EXTRA_ID);
            mTitle = getArguments().getString(Constant.EXTRA_TITLE);
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mTvContent.setText(mTitle);
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process(Bundle savedInstanceState) {

    }

    @Override
    protected void createOptionsMenu(Menu menu, MenuInflater inflater) {
        super.createOptionsMenu(menu, inflater);
        inflater.inflate(mMenuID, menu);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        //1.判断是否有 Parent，若无，则表明是直接 attach 在 Activity 下
//        //2.判断 getUserVisibleHint() 是否为 true，若为 true ，则表示对用户可见
//        //综合 1 、 2 ，可以判断出是否需要创建菜单
//        boolean needCreate = getParentFragment() == null || getParentFragment().getUserVisibleHint();
//        Log.d(TAG, "onCreateOptionsMenu: title = " + mTitle + "; needCreate = " + needCreate);
//        if (needCreate) {
//            inflater.inflate(mMenuID, menu);
//        }
//    }
}
