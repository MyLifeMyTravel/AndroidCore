package com.littlejie.demo.modules.base.ui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.utils.DialogUtil;

import butterknife.OnClick;

@Description(description = "对话框")
public class DialogActivity extends BaseActivity {

    public static final String[] CHOICE_ITEMS = {"西藏", "新疆", "云南", "四川", "内蒙古", "哈尔滨"};
    private int mCheckedItem = 0;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_dialog;
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

    @OnClick(R.id.btn_show_single_choice_dialog)
    void showSingleChoiceDialog() {
        new AlertDialog.Builder(this)
                .setTitle("单选列表对话框")
                .setSingleChoiceItems(CHOICE_ITEMS, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showDefaultToast(CHOICE_ITEMS[which]);
                    }
                }).show();
    }

    @OnClick(R.id.btn_show_custom_single_choice_dialog)
    void showCustomSingleChoiceDialog() {
        //当使用自定义Dialog时，要特别注意ListView中存在RadioButton和CheckBox的情况，默认这两者会拦截ListView的点击事件
        DialogUtil.showCustomSingleChoice(this, "自定义对话框", CHOICE_ITEMS, mCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mCheckedItem = which;
                ToastUtil.showDefaultToast(CHOICE_ITEMS[which]);
            }
        });
//        new AlertDialog.Builder(this)
//                .setTitle("自定义对话框")
//                .setView(R.layout.layout_custom_notification)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
    }

    @OnClick(R.id.btn_show_custom_dialog)
    void showCustomDialog() {
        DialogUtil.showDialog(this, "自定义对话框", "这是自定义对话框", true, null, null, true, null, null);
    }

    @Override
    protected void process() {

    }
}
