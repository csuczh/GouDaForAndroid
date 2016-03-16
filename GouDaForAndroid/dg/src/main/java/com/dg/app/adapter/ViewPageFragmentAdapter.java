package com.dg.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.baidu.mapapi.map.MapView;
import com.dg.app.R;
import com.dg.app.widget.PagerSlidingTabStrip;
import java.util.ArrayList;

@SuppressLint("Recycle")
public class ViewPageFragmentAdapter {

    private final Context mContext;
    protected PagerSlidingTabStrip mPagerStrip;
//    protected   MapView mapView;

    private final ArrayList<ViewPageInfo> mTabs = new ArrayList<ViewPageInfo>();

    public ViewPageFragmentAdapter(
                                   PagerSlidingTabStrip pageStrip,Context context) {

        mContext = context;
        mPagerStrip = pageStrip;



    }

    public void addTab(String title, String tag,  Bundle args) {
        ViewPageInfo viewPageInfo = new ViewPageInfo(title, tag, args);
        addFragment(viewPageInfo);
    }

    public void addAllTab(ArrayList<ViewPageInfo> mTabs) {
        for (ViewPageInfo viewPageInfo : mTabs) {
            addFragment(viewPageInfo);
        }
    }

    private void addFragment(ViewPageInfo info) {
        if (info == null) {
            return;
        }

        // 加入tab title
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.base_viewpage_fragment_tab_item, null, false);
        TextView title = (TextView) v.findViewById(R.id.tab_title);
        title.setText(info.title);
        mPagerStrip.addTab(v);

        mTabs.add(info);

    }

    /**
     * 移除第一次
     */
    public void remove() {
        remove(0);
    }

    /**
     * 移除一个tab
     * 
     * @param index
     *            备注：如果index小于0，则从第一个开始删 如果大于tab的数量值则从最后一个开始删除
     */
    public void remove(int index) {
        if (mTabs.isEmpty()) {
            return;
        }
        if (index < 0) {
            index = 0;
        }
        if (index >= mTabs.size()) {
            index = mTabs.size() - 1;
        }
        mTabs.remove(index);
        mPagerStrip.removeTab(index, 1);

    }

    /**
     * 移除所有的tab
     */
    public void removeAll() {
        if (mTabs.isEmpty()) {
            return;
        }
        mPagerStrip.removeAllTab();
        mTabs.clear();

    }


    public int getCount() {
        return mTabs.size();
    }


    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

//    @Override
//    public Fragment getItem(int position) {
//        ViewPageInfo info = mTabs.get(position);
//        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
//    }


    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }
}