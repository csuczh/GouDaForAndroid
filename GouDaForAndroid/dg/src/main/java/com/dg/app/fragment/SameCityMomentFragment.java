package com.dg.app.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.dg.app.AppContext;
import com.dg.app.adapter.MomentsAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseListFragment;
import com.dg.app.base.MomentsBaseListFragment;
import com.dg.app.bean.DGUser;
import com.dg.app.bean.Moment;
import com.dg.app.bean.MomentsList;
import com.dg.app.ui.LoginActivity;
import com.dg.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;


public class
        SameCityMomentFragment extends MomentsBaseListFragment<Moment>
{

    private DGUser user;

    protected static final String TAG = SameCityMomentFragment.class.getSimpleName();

    private static final String CACHE_KEY_PREFIX = "same_city_moments_list_";

    private MomentsAdapter momentsAdapter;

    @Override
    protected MomentsAdapter getListAdapter() {

        momentsAdapter = new MomentsAdapter(getActivity());
        return momentsAdapter;
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX;
    }

    @Override
    protected MomentsList parseList(InputStream is) throws Exception {
        MomentsList list = null;
        try {
            list = XmlUtils.toBean(MomentsList.class, is);
        } catch (NullPointerException e) {
            list = new MomentsList();
        }
        return list;
    }

    @Override
    protected MomentsList readList(Serializable seri) {
        return ((MomentsList) seri);
    }

    @Override
    protected void sendRequestData() {
        user = AppContext.getInstance().getLoginUser();
        if(user!=null&&user.getUserid()!=0){
            DGApi.sameCityMoments(user.getUserid() ,1 , mCurrentPage, MomentsBaseListFragment.PAGE_SIZE, mHandler);
        }else{
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    @Override
    protected void executeOnLoadDataSuccess(List<Moment> data) {
      try {
          super.executeOnLoadDataSuccess(data);
      }catch (Exception ex)
      {
          ex.printStackTrace();
      }
    }

    @Override
    protected long getAutoRefreshTime() {
            return 5 * 60;

    }

    public void refresh(){
        this.sendRequestData();
    }

}
