package com.littlejie.demo.modules.base.media;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

@Description(description = "使用 Camera2 实现预览")
public class Camera2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
    }
}
