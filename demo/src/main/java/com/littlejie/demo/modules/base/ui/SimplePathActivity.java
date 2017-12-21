package com.littlejie.demo.modules.base.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

@Description(description = "绘制简单的闭合路径")
public class SimplePathActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_path);
    }
}
