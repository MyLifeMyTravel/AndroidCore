package com.littlejie.password;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Vibrator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.password.storage.PasswordStorage;

public class InputPwdActivity extends BaseActivity {

    private static final int[] KEYBOARD_VALUE = Constants.KEYBOARD_VALUE;
    private static final int KEYBOARD_SIZE = KEYBOARD_VALUE.length;

    private Vibrator vibrator;

    private LinearLayout groupPassword;
    private TextView tvTip;
    private RecyclerView recyclerView;
    private DigitKeyboardAdapter adapter;
    private Button btnDel;

    private int retryTimes;
    private int pwdRetryTimes;
    private int pwdLength;
    private int position;
    private StringBuilder password = new StringBuilder();

    private OnDeblockListener onDeblockListener;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_input_pwd;
    }

    @Override
    protected void initData() {
        pwdLength = PasswordManager.getInstance().getPasswordLength();
        pwdRetryTimes = PasswordManager.getInstance().getPasswordRetryTimes();
        onDeblockListener = PasswordManager.getInstance().getOnDeblockResultListener();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void initView() {
        groupPassword = (LinearLayout) findViewById(R.id.group_password);
        tvTip = (TextView) findViewById(R.id.tv_tip);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.digit_margin)));
        adapter = new DigitKeyboardAdapter();
        recyclerView.setAdapter(adapter);

        btnDel = (Button) findViewById(R.id.btn_del);
        initPasswordView();
    }

    private void initPasswordView() {
        groupPassword.removeAllViews();
        int padding = getResources().getDimensionPixelSize(R.dimen.padding);
        for (int i = 0; i < pwdLength; i++) {
            ImageView imageView = new ImageView(this);
            //设置外边距
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = padding;
            params.rightMargin = padding;
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(R.drawable.icon_password_deblocking);
            groupPassword.addView(imageView);
        }
    }

    @Override
    protected void initViewListener() {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int p) {
                inputPassword(p);
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delPassword();
            }
        });
    }

    private void inputPassword(int p) {
        if (position == pwdLength) {//当密码已经输入完成时，直接退出处理
            return;
        }
        if (position <= pwdLength - 1) {//当密码尚未输入完成时，将对应按键的数字放入密码框
            password.append(KEYBOARD_VALUE[p]);
            groupPassword.getChildAt(position++).setSelected(true);
        }
        //如果密码输入完成且与设置的密码相等，则解开应用锁
        if (position == pwdLength) {
            if (PasswordStorage.encryptPassword(this, password.toString())
                    .equals(PasswordStorage.get(InputPwdActivity.this))) {
                onDeblock(true);
                finish();
            } else {
                vibrateCheckPasswordFailed();
                animCheckPasswordFailed();
                retryTimes++;
                int left = pwdRetryTimes - retryTimes;
                if (left == 0) {
                    onDeblock(false);
                    finish();
                }
                clearPassword();
                tvTip.setText(getString(R.string.more_attempts, left));
            }
        }
    }

    private void delPassword() {
        if (position == 0) {
            return;
        }
        //删除最后一位数字
        password.deleteCharAt(password.length() - 1);
        groupPassword.getChildAt(--position).setSelected(false);
    }

    private void onDeblock(boolean success) {
        if (onDeblockListener != null) {
            onDeblockListener.onDeblock(DeblockType.DEBLOCK, success);
        }
    }

    //密码验证失败动画
    private void animCheckPasswordFailed() {
        //Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_error_password);
        //groupPassword.startAnimation(animation);
        ObjectAnimator animator = ObjectAnimator.ofFloat(groupPassword, "translationX", 0, -50, 0, 50, 0);
        animator.setDuration(200);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(2);
        animator.start();
    }

    //密码验证失败震动
    private void vibrateCheckPasswordFailed() {
        vibrator.vibrate(1000);
    }

    private void clearPassword() {
        for (int i = 0; i < pwdLength; i++) {
            groupPassword.getChildAt(i).setSelected(false);
        }
        password = new StringBuilder();
        position = 0;
    }

    @Override
    protected void process() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        //重写输入密码界面的返回按钮，防止用户通过点击返回按钮回到主界面
        moveTaskToBack(true);
    }

    private class DigitKeyboardAdapter
            extends RecyclerView.Adapter<DigitKeyboardAdapter.DigitKeyboardHolder> {

        private OnItemClickListener onItemClickListener;

        @Override
        public DigitKeyboardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(InputPwdActivity.this)
                    .inflate(R.layout.item_keyboard_digit, parent, false);
            return new DigitKeyboardHolder(view);
        }

        @Override
        public void onBindViewHolder(DigitKeyboardHolder holder, int position) {
            int value = Constants.KEYBOARD_VALUE[position];
            holder.itemView.setVisibility(value < 0 ? View.INVISIBLE : View.VISIBLE);
            holder.tvDigit.setText(String.valueOf(value));
        }

        @Override
        public int getItemCount() {
            return KEYBOARD_SIZE;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        class DigitKeyboardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView tvDigit;

            public DigitKeyboardHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                tvDigit = (TextView) itemView.findViewById(R.id.tv_digit);
            }

            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        }
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            outRect.bottom = space;
            if (position % 3 == 0) {
                outRect.left = 0;
                outRect.right = space;
            } else if (position % 3 == 1) {
                outRect.left = space;
                outRect.right = space;
            } else if (position % 3 == 2) {
                outRect.left = space;
                outRect.right = space;
            }
        }

        public SpaceItemDecoration(int space) {
            this.space = space;
        }
    }
}
