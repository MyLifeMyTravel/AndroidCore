package com.littlejie.demo.modules.advance.ui;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.base.Core;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.ui.SearchImageView;

import butterknife.BindView;

@Description(description = "搜索效果动画")
public class SearchEffectActivity extends BaseActivity {

    @BindView(R.id.search)
    SearchImageView search;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_search_effect;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {
        Core.runOnUIThreadDelayed(100, new Runnable() {
            @Override
            public void run() {
                search.start();
            }
        });
    }
}
