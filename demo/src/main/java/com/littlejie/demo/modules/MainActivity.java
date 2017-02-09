package com.littlejie.demo.modules;

import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.DeviceUtil;
import com.littlejie.core.util.JsonUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.entity.TestInfo;
import com.littlejie.demo.modules.base.AndroidBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//todo 测试各种情况下的ip
//1. wifi
//2. 手机网络
public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar_bar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void initViewListener() {
//        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                return switchItem(item.getItemId());
//            }
//        });
    }

    private boolean switchItem(int id) {
        switch (id) {
            case R.id.menu_base:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_frame, AndroidBaseFragment.newInstance())
                        .commit();
                return true;
            case R.id.menu_advanced:
                TestInfo info = new TestInfo();
                info.setName("厉圣杰");
                info.setPath("abc");
                Log.d(TAG, "switchItem: " + JsonUtil.toJsonString(info));
                List<String> list = new ArrayList<>();
                list.add(JsonUtil.toJsonString(info));
                list.add("abc");
                Log.d(TAG, "switchItem: " + JsonUtil.toJsonString(list));
                return true;
            case R.id.menu_give_up:

                return true;
            case R.id.menu_setting:

                return true;
            case R.id.menu_feedback:

                return true;
            case R.id.menu_about:

                return true;
            default:
                return false;
        }
    }

    @Override
    protected void process() {
        switchItem(R.id.menu_base);
        Log.d(TAG, "process: ip = "+ DeviceUtil.getMobileIP());
        Log.d(TAG, "process: wifi ip = "+ DeviceUtil.getWifiIP(this));
    }

}
