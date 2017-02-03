package com.littlejie.demo.modules.base.fragment.visible;

import android.os.Bundle;

import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.BaseListActivity;

@Description(description = "Fragment 可见性")
public class FragmentVisibleListActivity extends BaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visible_list);
    }

}
