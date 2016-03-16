package com.dg.app.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.dg.app.AppContext;
import com.dg.app.adapter.MessagesAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseListFragment;
import com.dg.app.bean.DGUser;
import com.dg.app.bean.Message;
import com.dg.app.bean.MessagesList;
import com.dg.app.ui.LoginActivity;
import com.dg.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;


public class LikeMessageFragment extends BaseListFragment<Message>
{

    private DGUser user;

    protected static final String TAG = LikeMessageFragment.class.getSimpleName();

    private static final String CACHE_KEY_PREFIX = "like_messages_list_";

    @Override
    protected MessagesAdapter getListAdapter() {
        return new MessagesAdapter(getActivity(),MessagesAdapter.LIKE_MESSAGE);
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected MessagesList parseList(InputStream is) throws Exception {
        MessagesList list = null;
        try {
            list = XmlUtils.toBean(MessagesList.class, is);
        } catch (NullPointerException e) {
            list = new MessagesList();
        }
        return list;
    }

    @Override
    protected MessagesList readList(Serializable seri) {
        return ((MessagesList) seri);
    }

    @Override
    protected void sendRequestData() {
        user = AppContext.getInstance().getLoginUser();

        if(user!=null&&user.getUserid()!=0){
            DGApi.likeMessages(user.getUserid(), "", mHandler);
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    @Override
    protected void executeOnLoadDataSuccess(List<Message> data) {
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

    public void refresh(){
        sendRequestData();
    }
}
