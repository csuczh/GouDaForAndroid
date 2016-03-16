package com.dg.app.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.dg.app.AppContext;
import com.dg.app.adapter.CollectionsAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseListFragment;
import com.dg.app.bean.Collection;
import com.dg.app.bean.CollectionsList;
import com.dg.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;


public class CollectionsFragment extends BaseListFragment<Collection>
{
    protected static final String TAG = CollectionsFragment.class.getSimpleName();

    private static final String CACHE_KEY_PREFIX = "dog_knows_collections_list_";

    @Override
    protected CollectionsAdapter getListAdapter() {
        return new CollectionsAdapter(getActivity());
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected CollectionsList parseList(InputStream is) throws Exception {
        CollectionsList list = null;
        try {
            list = XmlUtils.toBean(CollectionsList.class, is);
        } catch (NullPointerException e) {
            list = new CollectionsList();
        }
        return list;
    }

    @Override
    protected CollectionsList readList(Serializable seri) {
        return ((CollectionsList) seri);
    }

    @Override
    protected void sendRequestData() {
        int user_id = AppContext.getInstance().getLoginUid();
        if(user_id!=0) {
            DGApi.collection(user_id, mCurrentPage, 10, mHandler);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    @Override
    protected void executeOnLoadDataSuccess(List<Collection> data) {
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
