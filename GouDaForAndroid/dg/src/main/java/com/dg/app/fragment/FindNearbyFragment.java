package com.dg.app.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.adapter.OwnersAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseListFragment;
import com.dg.app.base.ListBaseAdapter;
import com.dg.app.bean.Owners;
import com.dg.app.bean.OwnersList;
import com.dg.app.interf.OnTabReselectListener;
import com.dg.app.ui.empty.EmptyLayout;
import com.dg.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.security.acl.Owner;
import java.util.List;


public class FindNearbyFragment extends  BaseListFragment<Owners>
{
    protected static final String TAG = FindNearbyFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "ownerslist_";

    @Override
    protected OwnersAdapter getListAdapter() {
        return new OwnersAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected OwnersList parseList(InputStream is) throws Exception {
        OwnersList list = null;
        try {
            list = XmlUtils.toBean(OwnersList.class, is);
            List<Owners> owners=list.getList();

        } catch (NullPointerException e) {
            list = new OwnersList();
        }
        return list;
    }

    @Override
    protected OwnersList readList(Serializable seri) {
        return ((OwnersList) seri);
    }

    @Override
    protected void sendRequestData() {
          String city_id=  AppContext.getInstance().getProperty("dgUser.cityid");
          int int_city_Id=Integer.parseInt(city_id);
          String user_id=  AppContext.getInstance().getProperty("dgUser.userid");
        int int_user_id=Integer.parseInt(city_id);
          DGApi.getGouDaFindList(device, int_user_id,int_city_Id, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    @Override
    protected void executeOnLoadDataSuccess(List<Owners> data) {
      try {
          super.executeOnLoadDataSuccess(data);
      }catch (Exception ex)
      {
          ex.printStackTrace();
      }
    }

    @Override
    protected long getAutoRefreshTime() {

            return 2 * 60 * 60;

}
}
