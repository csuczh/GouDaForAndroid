package com.dg.app.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.dg.app.AppContext;
import com.dg.app.adapter.FosterDogInvitationsAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseListFragment;
import com.dg.app.base.InvitationsBaseListFragment;
import com.dg.app.bean.FosterDogInvitation;
import com.dg.app.bean.FosterDogInvitationsList;
import com.dg.app.ui.LoginActivity;
import com.dg.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

public class FosterDogInvitationFragment extends InvitationsBaseListFragment<FosterDogInvitation>
{
    protected static final String TAG = FosterDogInvitationFragment.class.getSimpleName();

    private static final String CACHE_KEY_PREFIX = "foster_dog_invitations_list_";

    @Override
    protected FosterDogInvitationsAdapter getListAdapter() {
        return new FosterDogInvitationsAdapter(getActivity());
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected FosterDogInvitationsList parseList(InputStream is) throws Exception {
        FosterDogInvitationsList list = null;
        try {
            list = XmlUtils.toBean(FosterDogInvitationsList.class, is);
        } catch (NullPointerException e) {
            list = new FosterDogInvitationsList();
        }
        return list;
    }

    @Override
    protected FosterDogInvitationsList readList(Serializable seri) {
        return ((FosterDogInvitationsList) seri);
    }

    @Override
    protected void sendRequestData() {
        int user_id = AppContext.getInstance().getLoginUid();
        if(user_id!=0){
            DGApi.fosterDogInvitation(user_id, mCurrentPage,10, mHandler);
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
    protected void executeOnLoadDataSuccess(List<FosterDogInvitation> data) {
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
