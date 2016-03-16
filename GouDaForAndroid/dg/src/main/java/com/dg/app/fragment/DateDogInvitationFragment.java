package com.dg.app.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.dg.app.AppContext;
import com.dg.app.adapter.DateDogInvitationsAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseListFragment;
import com.dg.app.base.InvitationsBaseListFragment;
import com.dg.app.bean.DateDogInvitation;
import com.dg.app.bean.DateDogInvitationsList;
import com.dg.app.bean.ListEntity;
import com.dg.app.ui.LoginActivity;
import com.dg.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

public class DateDogInvitationFragment extends InvitationsBaseListFragment<DateDogInvitation>
{
    protected static final String TAG = DateDogInvitationFragment.class.getSimpleName();

    private static final String CACHE_KEY_PREFIX = "date_dog_invitations_list_";

    @Override
    protected DateDogInvitationsAdapter getListAdapter() {
        return new DateDogInvitationsAdapter(getActivity());
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected DateDogInvitationsList parseList(InputStream is) throws Exception {
        DateDogInvitationsList list = null;
        try {
            list = XmlUtils.toBean(DateDogInvitationsList.class, is);
        } catch (NullPointerException e) {
            list = new DateDogInvitationsList();
        }
        return list;
    }

    @Override
    protected DateDogInvitationsList readList(Serializable seri) {
        return ((DateDogInvitationsList)seri);
    }

    @Override
    protected void sendRequestData() {
        int user_id = AppContext.getInstance().getLoginUid();
        if(user_id!=0){
            DGApi.dateDogInvitation(user_id, mCurrentPage,10, mHandler);
        }else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    @Override
    protected void executeOnLoadDataSuccess(List<DateDogInvitation> data) {
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
