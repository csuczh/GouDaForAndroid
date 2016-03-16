package com.dg.app.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.dg.app.AppContext;
import com.dg.app.adapter.LiuDogInvitationsAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseListFragment;
import com.dg.app.base.InvitationsBaseListFragment;
import com.dg.app.bean.LiuDogInvitationsList;
import com.dg.app.bean.LiuGouInvitation;
import com.dg.app.ui.LoginActivity;
import com.dg.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

public class LiuDogInvitationFragment extends InvitationsBaseListFragment<LiuGouInvitation>
{
    protected static final String TAG = LiuDogInvitationFragment.class.getSimpleName();

    private static final String CACHE_KEY_PREFIX = "liu_dog_invitations_list_";

    @Override
    protected LiuDogInvitationsAdapter getListAdapter() {
        return new LiuDogInvitationsAdapter(getActivity());
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected LiuDogInvitationsList parseList(InputStream is) throws Exception {
        LiuDogInvitationsList list = null;
        try {
            list = XmlUtils.toBean(LiuDogInvitationsList.class, is);
        } catch (NullPointerException e) {
            list = new LiuDogInvitationsList();
        }
        return list;
    }

    @Override
    protected LiuDogInvitationsList readList(Serializable seri) {
        return ((LiuDogInvitationsList)seri);
    }

    @Override
    protected void sendRequestData() {
        int user_id = AppContext.getInstance().getLoginUid();
        if(user_id!=0){
            DGApi.liuDogInvitation(user_id, mCurrentPage,10, mHandler);
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
    protected void executeOnLoadDataSuccess(List<LiuGouInvitation> data) {
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
