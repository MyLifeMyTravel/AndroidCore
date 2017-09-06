package com.littlejie.core.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * FragmentPager适配器，解决搭配ViewPager使用时视图状态保存的问题。
 */

public class TabFragmentAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;
    private List<Fragment> pageList;

    public TabFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    public void setPageList(List<Fragment> pageList) {
        this.pageList = pageList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return pageList == null ? null : pageList.get(position);
    }

    @Override
    public int getCount() {
        return pageList == null ? 0 : pageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        Fragment fragment = pageList.get(position);
        fragmentManager.beginTransaction().hide(fragment).commit();
    }
}
