package com.littlejie.demo.modules.base.ui;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.ui.AutoFitViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by littlejie on 2017/10/18.
 */
@Description(description = "自适应高度的ViewPager")
public class ResizeViewPagerActivity extends BaseActivity {

    private static final int[] COLORS = {Color.BLUE, Color.CYAN, Color.YELLOW, Color.RED};
    private static final int[] HEIGHTS = {2000, 150, 1000};

    @BindView(R.id.viewPager)
    AutoFitViewPager mViewPager;
    private AutoFitPagerAdapter mAdapter;

    private List<View> mViewList;

    @Override
    protected int getPageLayoutID() {
        return R.layout.layout_resize_viewpager;
    }

    @Override
    protected void initData() {
        mViewList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TextView textView = new TextView(this);
            int height = HEIGHTS[i % HEIGHTS.length];
            textView.setHeight(height);
            textView.setBackgroundColor(COLORS[i % COLORS.length]);
            textView.setText("page " + i + ";height = " + height);
            textView.setTag(i);
            mViewList.add(textView);
        }
    }

    @Override
    protected void initView() {
        mAdapter = new AutoFitPagerAdapter(mViewList);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void initViewListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void process() {
        mViewPager.setCurrentItem(mViewList.size() - 1);
    }

    private class AutoFitPagerAdapter extends PagerAdapter {

        private List<View> mViewList;

        public AutoFitPagerAdapter(List<View> viewList) {
            mViewList = viewList;
        }

        @Override
        public int getCount() {
            return mViewList == null ? 0 : mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }
}
