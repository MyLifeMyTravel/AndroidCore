package com.littlejie.demo.modules.base.system.touch;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import butterknife.OnClick;

@Description(description = "测试 View 触摸移动")
public class ViewMoveActivity extends BaseActivity {

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_view_move;
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

    @OnClick(R.id.move_view)
    void onMoveView() {
        ToastUtil.showDefaultToast("点击");
    }

    @Override
    protected void process() {

    }

}
