package com.dg.app.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.dg.app.AppContext;
import com.dg.app.adapter.FansAdapter;
import com.dg.app.adapter.MomentsAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseListFragment;
import com.dg.app.bean.DGUser;
import com.dg.app.bean.Fan;
import com.dg.app.bean.FansList;
import com.dg.app.bean.Moment;
import com.dg.app.bean.MomentsList;
import com.dg.app.ui.LoginActivity;
import com.dg.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;


public class FansListFragment extends BaseListFragment<Fan>
{

    protected static final String TAG = FansListFragment.class.getSimpleName();

    private static final String CACHE_KEY_PREFIX = "fans_list_";

    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    protected FansAdapter getListAdapter() {
        return new FansAdapter(getActivity());
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected FansList parseList(InputStream is) throws Exception {
        FansList list = null;
        try {
            list = XmlUtils.toBean(FansList.class, is);
        } catch (NullPointerException e) {
            list = new FansList();
        }
        return list;
    }

    @Override
    protected FansList readList(Serializable seri) {
        return ((FansList) seri);
    }

    @Override
    protected void sendRequestData() {
        if(user_id!=0) {
            DGApi.getFans(user_id, 0, 10, mHandler);
        }else{
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    @Override
    protected void executeOnLoadDataSuccess(List<Fan> data) {
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

    @Override
    public void onResume() {
        super.onResume();
        sendRequestData();
    }
}
