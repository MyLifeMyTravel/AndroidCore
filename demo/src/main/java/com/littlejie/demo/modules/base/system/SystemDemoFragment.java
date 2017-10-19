package com.littlejie.demo.modules.base.system;

import android.os.Bundle;

import com.littlejie.demo.annotation.Title;
import com.littlejie.demo.modules.BaseListFragment;
import com.littlejie.demo.modules.base.system.thread.HandlerActivity;
import com.littlejie.demo.modules.base.system.touch.DispatchTouchEventActivity;
import com.littlejie.demo.modules.base.system.touch.ScrollActivity;
import com.littlejie.demo.modules.base.system.touch.ViewMoveActivity;

/**
 * Created by littlejie on 2017/1/24.
 */
@Title(title = "系统")
public class SystemDemoFragment extends BaseListFragment {

    public static SystemDemoFragment newInstance() {

        Bundle args = new Bundle();

        SystemDemoFragment fragment = new SystemDemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        super.process(savedInstanceState);
        mLstItem.add(ClipboardActivity.class);
        mLstItem.add(ShareIntentActivity.class);
        mLstItem.add(DispatchTouchEventActivity.class);
        mLstItem.add(ScrollActivity.class);
        mLstItem.add(ViewMoveActivity.class);
        mLstItem.add(PackageActivity.class);
        mLstItem.add(HandlerActivity.class);
        mLstItem.add(ChangeLanguageActivity.class);
        mLstItem.add(WindowManagerActivity.class);
        notifyDataChanged();
    }
}
