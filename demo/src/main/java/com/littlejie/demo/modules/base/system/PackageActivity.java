package com.littlejie.demo.modules.base.system;

import android.widget.ListView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.adapter.PackageAdapter;

import butterknife.BindView;

@Description(description = "已安装 Package 信息展示")
public class PackageActivity extends BaseActivity {

    @BindView(R.id.lv)
    ListView mLv;
    private PackageAdapter mAdapter;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_package;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mAdapter = new PackageAdapter(this);
        mLv.setAdapter(mAdapter);
    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {

    }
}
