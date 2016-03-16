package com.dg.app.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.dg.app.adapter.RankingAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseListFragment;
import com.dg.app.bean.Ranking;
import com.dg.app.bean.RankingList;
import com.dg.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

public class MengRankingFragment extends  BaseListFragment<Ranking>
{
    protected static final String TAG = MengRankingFragment.class.getSimpleName();

    private static final String CACHE_KEY_PREFIX = "meng_rank_list_";

    @Override
    protected RankingAdapter getListAdapter() {
        return new RankingAdapter(getActivity());
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected RankingList parseList(InputStream is) throws Exception {
        RankingList list = null;
        try {
            list = XmlUtils.toBean(RankingList.class, is);
        } catch (NullPointerException e) {
            list = new RankingList();
        }
        return list;
    }

    @Override
    protected RankingList readList(Serializable seri) {
        return ((RankingList) seri);
    }

    @Override
    protected void sendRequestData() {
        DGApi.getRanking(mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    @Override
    protected void executeOnLoadDataSuccess(List<Ranking> data) {
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
