package cn.bongmi.uidemo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.littlejie.core.base.BaseActivity;

import butterknife.BindView;
import cn.bongmi.uidemo.fragment.BaseBindFragment;
import cn.bongmi.uidemo.fragment.BindActionFragment;
import cn.bongmi.uidemo.fragment.CheckBleFragment;
import cn.bongmi.uidemo.fragment.SearchDeviceFragment;

public class BindDeviceActivity extends BaseActivity
        implements OnStepFinishListener {

    private static final String KEY = "step";
    private BindStep bindStep = BindStep.CHECK_BLUETOOTH;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    BaseBindFragment checkBle;
    BaseBindFragment bindAction;
    BaseBindFragment searchBle;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_bind_device;
    }

    @Override
    protected void initData() {
        checkBle = CheckBleFragment.newInstance();
        bindAction = BindActionFragment.newInstance();
        searchBle = SearchDeviceFragment.newInstance();
        if (getIntent() != null) {
            bindStep = (BindStep) getIntent().getSerializableExtra(KEY);
        }
        if (bindStep == null) {
            bindStep = BindStep.CHECK_BLUETOOTH;
        }
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void initViewListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checkBle.setOnStepFinishListener(this);
        bindAction.setOnStepFinishListener(this);
        searchBle.setOnStepFinishListener(this);
    }

    @Override
    protected void process() {
        Fragment fragment;
        if (bindStep == BindStep.CHECK_BLUETOOTH) {
            fragment = checkBle;
        } else if (bindStep == BindStep.BIND_ACTION) {
            fragment = bindAction;
        } else {
            fragment = searchBle;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
//        finish();
    }

    @Override
    public void onStepFinish(BindStep step, boolean finish) {
        switch (step) {
            case BIND_ACTION:
                next(BindStep.SEARCH_DEVICE);
                break;
            case CHECK_BLUETOOTH:
                next(BindStep.BIND_ACTION);
                break;
            case SEARCH_DEVICE:

                break;
            default:
                break;
        }
    }

    private void next(BindStep step) {
        Log.d(TAG, step.toString());
        Intent intent = new Intent(this, BindDeviceActivity.class);
        intent.putExtra(KEY, step);
        startActivity(intent);
        finish();
    }
}
