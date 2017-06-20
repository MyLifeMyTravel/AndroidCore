package com.littlejie.demo.modules.base.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.utils.DialogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 1. Dialog
 */
@Description(description = "对话框")
public class DialogActivity extends BaseActivity {

    public static final String[] CHOICE_ITEMS = {"西藏", "新疆", "云南", "四川", "内蒙古", "哈尔滨"};
    private int count = 0;
    private int mCheckedItem = 0;

    @BindView(R.id.btn_show_cover_dialog_by_popup_window)
    Button btnShowCoverDialogByPopupWindow;

    private Handler handler = new Handler();
    private Runnable printRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "count = " + count++);
            handler.postDelayed(printRunnable, 1000);
        }
    };

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_dialog;
    }

    @Override
    protected void initData() {
        handler.post(printRunnable);
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

    @OnClick(R.id.btn_show_cover_dialog_in_dialog)
    void showCoverDialogInDialog() {
        //Dialog点击之后会消失，所以此法不行
        showCoverDialog();
    }

    @OnClick(R.id.btn_show_cover_dialog_by_dialog_fragment)
    void showCoverDialogInDialogFragment() {
        MyDialogFragment dialogFragment = MyDialogFragment.newInstance();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
        dialogFragment.setCancelable(true);
        dialogFragment.setOnSaveListener(new MyDialogFragment.OnSaveListener() {
            @Override
            public void onSave() {
                showCoverDialog();
            }
        });
    }

    @OnClick(R.id.btn_show_cover_dialog_by_popup_window)
    void showCoverDialogInPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.fragment_dialog, null, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        int margin = getResources().getDimensionPixelOffset(R.dimen.large);
        params.setMargins(margin, margin, margin, margin);
        view.setLayoutParams(params);
        Button btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCoverDialog();
            }
        });
        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        popupWindow.showAsDropDown(btnShowCoverDialogByPopupWindow);
    }

    @OnClick(R.id.btn_show_cover_dialog_by_view)
    void showCoverDialogInView() {
        final ViewGroup decorView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        final ViewGroup rootView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.base_dialog_container, decorView, false);
        final ViewGroup container = (ViewGroup) rootView.findViewById(R.id.dialogplus_content_container);
        final View view = LayoutInflater.from(this).inflate(R.layout.fragment_dialog, null, false);
        //获取焦点，否则监听不到按键事件
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                decorView.removeView(rootView);
                return true;
            }
        });
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                decorView.removeView(rootView);
                return false;
            }
        });
        Button btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCoverDialog();
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        int margin = getResources().getDimensionPixelOffset(R.dimen.large);
        params.setMargins(margin, margin, margin, margin);
        container.setLayoutParams(params);
        container.addView(view);
        decorView.addView(rootView);
    }

    private void showCoverDialog() {
        //PopupWindow 会遮挡 DialogFragment
        Button button = new Button(this);
        button.setText("123");
        PopupWindow popupWindow = new PopupWindow(button, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("我是覆盖的Dialog")
//                .setMessage("我将覆盖之前所有的View")
//                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();
    }

    @Override
    protected void process() {

    }

    static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
