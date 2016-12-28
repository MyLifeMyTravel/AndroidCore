package com.littlejie.core.base;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by littlejie on 2016/4/6.
 */
public abstract class BaseActivity extends Activity {

    public final String TAG = this.getClass().getSimpleName();

    protected int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getPageLayoutID());
        initData();
        initView();
        initViewListener();
        process();
    }

    /**
     * 设置页面布局ID
     *
     * @return
     */
    protected abstract int getPageLayoutID();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化页面控件
     */
    protected abstract void initView();

    /**
     * 初始化控件监听器
     */
    protected abstract void initViewListener();

    protected abstract void process();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void requestPermission(final String permission, String reason, final int requestCode) {
        this.requestCode = requestCode;
        if (checkPermission(permission)) {
            Log.i(TAG, permission + " has NOT been granted. Requesting permissions.");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //弹窗显示请求原因，并重新请求
                Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
            } else {
                requestPermission(new String[]{permission}, requestCode);
            }
        } else {
            Log.i(TAG, permission + " have already been granted.");
            processWithPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == this.requestCode) {
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, permissions[0] + "permission has now been granted.");
            } else {
                Log.i(TAG, "permission was NOT granted.");
                this.finish();
            }
        }
    }

    private boolean checkPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, int requestCode) {
        requestPermission(new String[]{permission}, requestCode);
    }

    private void requestPermission(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    /**
     * 拥有权限时处理
     */
    protected void processWithPermission() {

    }
}
