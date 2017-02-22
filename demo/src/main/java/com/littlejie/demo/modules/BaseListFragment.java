package com.littlejie.demo.modules;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.littlejie.core.base.BaseFragment;
import com.littlejie.core.manager.ActivityManager;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.AnnotationUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/1/23.
 */

public class BaseListFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView mLv;
    private ArrayAdapter<String> mArrayAdapter;
    protected List<Class<?>> mLstItem;

    @Override
    protected int getPageLayoutID() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        mLv.setAdapter(mArrayAdapter);
    }

    @Override
    protected void initViewListener() {
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mLstItem == null || mLstItem.size() == 0) {
                    return;
                }
                ActivityManager.startActivity(getContext(), mLstItem.get(position));
            }
        });
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        mLstItem = new ArrayList<>();
    }

    protected void notifyDataChanged() {
        mArrayAdapter.addAll(AnnotationUtil.getDescriptions(mLstItem));
        mArrayAdapter.notifyDataSetChanged();
    }

}
