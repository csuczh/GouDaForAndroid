package com.dg.app.viewpagerfragment;

import android.os.Bundle;
import android.view.View;

import com.dg.app.R;
import com.dg.app.adapter.ViewPageFragmentAdapter;
import com.dg.app.base.BaseMapViewFragment;
import com.dg.app.bean.MapList;
import com.dg.app.interf.OnTabReselectListener;


/**
 * Created by czh on
 * 2015/9/7.
 */
public class GouDaMapViewFragment extends BaseMapViewFragment
    implements OnTabReselectListener
       {
    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
       //加载头部viewpager的tab标签
        String[] title = getResources().getStringArray(
                R.array.gouda_viewpage_arrays);
        adapter.addTab(title[0], "遛狗",
                getBundle(MapList.CATALOG_LIUGOU));
        adapter.addTab(title[1], "相亲",
                getBundle(MapList.CATALOG_XIANGQIN));
        adapter.addTab(title[2], "寄养",
                getBundle(MapList.CATALOG_JIYANG));

    }
    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt("goud_type", newType);

        return bundle;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }
  public void refreash(int count)
  {
       setUreadCount(count);
  }


           @Override
           public void onTabReselect(int count) {
               refreash(count);
           }
       }
