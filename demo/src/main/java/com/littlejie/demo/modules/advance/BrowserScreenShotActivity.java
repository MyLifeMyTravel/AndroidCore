package com.littlejie.demo.modules.advance;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.core.util.ToastUtil;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;

/**
 * 获取浏览器分享网页时的屏幕图片
 * <p>
 * 默认情况下，图片都是以Uri形式保存在Intent的Map中，但不同浏览器的key不一样
 * Firefox不支持屏幕截图、百度只能获取浏览器图标
 * <p>
 * 测试时，请在AndroidManifest.xml中将ShareActivity相关配置注释
 */
@Description(description = "浏览器截屏获取")
public class BrowserScreenShotActivity extends BaseActivity {

    //chrome/内置浏览器/UC/QQ
    //通过getContentResolver().query()，可以得知Chrome有_display_name、_size、_data三个字段来保存截屏图片相关数据
    //但是通过Cursor查询时，_data字段为null，所以只能通过输入流来统一处理
    private static final String[] KEY_BROWSER_SCREENSHOT = {"share_screenshot_as_stream",
            "share_full_screen", "file", "android.intent.extra.STREAM"};

    @BindView(R.id.iv_screen_shot)
    ImageView mIvScreenShot;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_browser_screen_shot;
    }

    @Override
    protected void initData() {
        if (getIntent() == null){
            return;
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {

    }

    @Override
    protected void process() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() == null) {
            return;
        }
        Uri screenShot = null;
        //循环遍历，获取截屏的Uri
        for (String key : KEY_BROWSER_SCREENSHOT) {
            screenShot = getIntent().getExtras().getParcelable(key);
            if (screenShot != null) {
                break;
            }
        }

        if (screenShot == null) {
            ToastUtil.showDefaultToast("获取浏览器截屏失败~");
            return;
        }
        try {
            //通过ContentProvider获取截屏图片的输入流
            InputStream is = getContentResolver().openInputStream(screenShot);
            mIvScreenShot.setImageBitmap(BitmapFactory.decodeStream(is));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
