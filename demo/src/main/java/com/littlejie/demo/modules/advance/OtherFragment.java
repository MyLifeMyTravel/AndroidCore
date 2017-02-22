package com.littlejie.demo.modules.advance;

import android.os.Bundle;

import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.annotation.Title;
import com.littlejie.demo.modules.BaseListFragment;

/**
 * Created by littlejie on 2017/2/21.
 */

@Description(description = "")
@Title(title = "其它")
public class OtherFragment extends BaseListFragment {

    public static OtherFragment newInstance() {

        Bundle args = new Bundle();

        OtherFragment fragment = new OtherFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
