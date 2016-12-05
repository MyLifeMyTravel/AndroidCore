package com.littlejie.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.littlejie.core.util.PackageUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "isSystemApp = " + PackageUtil.isSystemApp(this, "com.android.email"));
        Log.d(TAG, "isPreloaded = " + PackageUtil.isAppPreLoaded(this, "com.android.email"));
        Log.d(TAG, "isSystemApp = " + PackageUtil.isSystemApp(this, "com.littlejie.sqlite"));
        Log.d(TAG, "isPreloaded = " + PackageUtil.isAppPreLoaded(this, "com.littlejie.sqlite"));
    }
}
