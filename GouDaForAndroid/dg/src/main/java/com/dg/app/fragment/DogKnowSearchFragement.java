package com.dg.app.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.dg.app.adapter.TopicAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseListFragment;
import com.dg.app.base.BaseSearchListFragment;
import com.dg.app.base.BaseTopicListFragment;
import com.dg.app.bean.Topic;
import com.dg.app.bean.TopicList;
import com.dg.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2015/10/27.
 * 主要用于展示某个分类下面的文章
 */
public class DogKnowSearchFragement extends BaseSearchListFragment<Topic> {
    protected static final String TAG = FindNearbyFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "seachslist_";

    @Override
    protected TopicAdapter getListAdapter() {
        return new TopicAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected TopicList parseList(InputStream is) throws Exception {
        TopicList list = null;
        try {
        list = XmlUtils.toBean(TopicList.class, is);
    } catch (NullPointerException e) {
        list = new TopicList();
    }
        return list;
    }

    @Override
    protected TopicList readList(Serializable seri) {
        return ((TopicList) seri);
    }

    @Override
    protected void sendRequestData() {

        DGApi.searchTopics(device,keyword, mCurrentPage, 10, mHandler);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    @Override
    protected void executeOnLoadDataSuccess(List<Topic> data) {
        try {
            super.executeOnLoadDataSuccess(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected long getAutoRefreshTime() {

        return 2 * 60 * 60;

    }
}