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

        Log.d(TAG, "isSystemApp = " + PackageUtil.isSystemApp(this, "com.google.android.apps.maps"));
        Log.d(TAG, "isSystemApp = " + PackageUtil.isSystemApp(this, "com.nero.htcsenselink"));
    }
}
