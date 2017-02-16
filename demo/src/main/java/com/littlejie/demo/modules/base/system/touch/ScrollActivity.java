package com.littlejie.demo.modules.base.system.touch;

import android.os.Bundle;

import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.BaseListActivity;

@Description(description = "测试 Scroll")
public class ScrollActivity extends BaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
    }
}
