package com.dg.app.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.bean.Entity;
import com.dg.app.bean.ListEntity;
import com.dg.app.bean.Moment;
import com.dg.app.bean.Result;
import com.dg.app.bean.ResultBean;
import com.dg.app.cache.CacheManager;
import com.dg.app.ui.empty.EmptyLayout;
import com.dg.app.util.StringUtils;
import com.dg.app.util.TDevice;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

@SuppressLint("DgApi")
public abstract class PageBaseListFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener,
        OnScrollListener {
    public static final String BUNDLE_KEY_DEVICE="BUNDLE_KEY_DEVICE";
    public static final String BUNDLE_KEY_USERID="BUNDLE_KEY_USERID";
    @InjectView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.listview)
    protected ListView mListView;

    protected ListBaseAdapter<Moment> mAdapter;

    @InjectView(R.id.error_layout)
    protected EmptyLayout mErrorLayout;

    protected int mStoreEmptyState = -1;

    protected int mCurrentPage = 0;

    protected  String device;
    protected  int userid;

    protected int page_size = 10;


    private AsyncTask<String, Void, ListEntity<Moment>> mCacheTask;
    private ParserTask mParserTask;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info_pull_refresh_listview;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view=null;
        try {
            view = inflater.inflate(getLayoutId(), container, false);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ButterKnife.inject(this, view);
            initView(view);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle args = getArguments();
            if (args != null) {
                device = args.getString(BUNDLE_KEY_DEVICE, "android");
                userid = args.getInt(BUNDLE_KEY_USERID, 0);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void initView(View view) {
        initNoticeWidget(getActivity());
        mListView.addHeaderView(initHeaderView());
        setData();

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentPage = 0;
                mState = STATE_REFRESH;
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestData(true);
            }
        });

        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        } else {
            mAdapter = getListAdapter();
            mListView.setAdapter(mAdapter);

            if (requestDataIfViewCreated()) {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                mState = STATE_NONE;
//                requestData(false);
                 requestData(true);

            } else {
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }

        }
        if (mStoreEmptyState != -1) {
            mErrorLayout.setErrorType(mStoreEmptyState);
        }

    }

    @Override
    public void onDestroyView() {
        mStoreEmptyState = mErrorLayout.getErrorState();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        cancelReadCacheTask();
        cancelParserTask();
        super.onDestroy();
    }

    protected abstract ListBaseAdapter<Moment> getListAdapter();

    // 下拉刷新数据
    @Override
    public void onRefresh() {
            if (mState == STATE_REFRESH) {
                return;
            }
            // 设置顶部正在刷新
            mListView.setSelection(0);
            setSwipeRefreshLoadingState();
            mCurrentPage = 0;
            mState = STATE_REFRESH;
            requestData(true);
    }

    protected boolean requestDataIfViewCreated() {
        return true;
    }

    protected String getCacheKeyPrefix() {
        return null;
    }

    protected ListEntity<Moment> parseList(InputStream is) throws Exception {
        return null;
    }

    protected ListEntity<Moment> readList(Serializable seri) {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {}

    private String getCacheKey() {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(mCurrentPage).toString();
    }

    // 是否需要自动刷新
    protected boolean needAutoRefresh() {
        return true;
    }

    /***
     * 获取列表数据
     * 
     * 
     * @author 火蚁 2015-2-9 下午3:16:12
     * 
     * @return void
     * @param refresh
     */
    protected void requestData(boolean refresh) {
        try {
            String key = getCacheKey();
            if (isReadCacheData(refresh)) {
                readCacheData(key);
//                AppContext.showToast("读缓存");
            } else {
                // 取新的数据
//                AppContext.showToast("取新的数据");
                sendRequestData();
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /***
     * 判断是否需要读取缓存的数据
     * 
     * @author 火蚁 2015-2-10 下午2:41:02
     * 
     * @return boolean
     * @param refresh
     * @return
     */
    protected boolean isReadCacheData(boolean refresh) {

        String key = getCacheKey();

        if (!TDevice.hasInternet()) {
            return true;
        }
        // 第一页若不是主动刷新，缓存存在，优先取缓存的
        if (CacheManager.isExistDataCache(getActivity(), key) && !refresh
                && mCurrentPage == 0) {
            return true;
        }
        // 其他页数的，缓存存在以及还没有失效，优先取缓存的
        if (CacheManager.isExistDataCache(getActivity(), key)
                && !CacheManager.isCacheDataFailure(getActivity(), key)
                && mCurrentPage != 0) {
            return true;
        }

        return false;
    }

    // 是否到时间去刷新数据了
    private boolean onTimeRefresh() {
        String lastRefreshTime = AppContext.getLastRefreshTime(getCacheKey());
        String currTime = StringUtils.getCurTimeStr();
        long diff = StringUtils.calDateDifferent(lastRefreshTime, currTime);
        return needAutoRefresh() && diff > getAutoRefreshTime();
    }

    /***
     * 自动刷新的时间
     * 
     * 默认：自动刷新的时间为半天时间
     * 
     * @author 火蚁 2015-2-9 下午5:55:11
     * 
     * @return long
     * @return
     */
    protected long getAutoRefreshTime() {
        return 12 * 60 * 60;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (onTimeRefresh()) {
                onRefresh();
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    protected void sendRequestData() {}

    private void readCacheData(String cacheKey) {
        try {
            cancelReadCacheTask();
            mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    private class CacheTask extends AsyncTask<String, Void, ListEntity<Moment>> {
        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected ListEntity<Moment> doInBackground(String... params) {
            Serializable seri = CacheManager.readObject(mContext.get(),
                    params[0]);
            if (seri == null) {
                return null;
            } else {
                return readList(seri);
            }
        }

        @Override
        protected void onPostExecute(ListEntity<Moment> list) {
            super.onPostExecute(list);
            if (list != null) {
                executeOnLoadDataSuccess(list.getList());
            } else {
                executeOnLoadDataError(null);
            }
            executeOnLoadFinish();
        }
    }

    private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        private SaveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<Context>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }

    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                byte[] responseBytes) {
                if (mCurrentPage == 0 && needAutoRefresh()) {
                    AppContext.putToLastRefreshTime(getCacheKey(),
                            StringUtils.getCurTimeStr());
                }
                if (isAdded()) {
                    if (mState == STATE_REFRESH) {
                        onRefreshNetworkSuccess();
                    }
                    executeParserTask(responseBytes);
                }

        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                Throwable arg3) {
            if (isAdded()) {
                readCacheData(getCacheKey());
            }
        }
    };

    protected void executeOnLoadDataSuccess(List<Moment> data) {
        try {
            if (data == null) {
                data = new ArrayList<Moment>();
            }

            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            if (mCurrentPage == 0) {
                mAdapter.clear();
            }

            for (int i = 0; i < data.size(); i++) {
                if (compareTo(mAdapter.getData(), data.get(i))) {
                    data.remove(i);
                    i--;
                }
            }
            int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
            if ((mAdapter.getCount() + data.size()) == 0) {
                adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
            } else if (data.size() == 0
                    || (data.size() < getPageSize() && mCurrentPage == 0)) {
                adapterState = ListBaseAdapter.STATE_NO_MORE;
//                AppContext.showToast("STATE_NO_MORE");
                mAdapter.notifyDataSetChanged();
            } else {
//                AppContext.showToast("STATE_LOAD_MORE");
                adapterState = ListBaseAdapter.STATE_LOAD_MORE;
            }

//            AppContext.showToast("adapterState ="+adapterState);
            mAdapter.setState(adapterState);
            mAdapter.addData(data);
            // 判断等于是因为最后有一项是listview的状态
            if (mAdapter.getCount() == 1) {

                if (needShowEmptyNoData()) {
                    mErrorLayout.setErrorType(EmptyLayout.NODATA);
                } else {
                    mAdapter.setState(ListBaseAdapter.STATE_EMPTY_ITEM);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * 是否需要隐藏listview，显示无数据状态
     * 
     * @author 火蚁 2015-1-27 下午6:18:59
     * 
     */
    protected boolean needShowEmptyNoData() {
        return true;
    }

    protected boolean compareTo(List<? extends Entity> data, Entity enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (enity.getId() == data.get(i).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected int getPageSize() {
        return page_size;
    }

    protected void onRefreshNetworkSuccess() {}

    protected void executeOnLoadDataError(String error) {
        if (mCurrentPage == 0
                && !CacheManager.isExistDataCache(getActivity(), getCacheKey())) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mAdapter.setState(ListBaseAdapter.STATE_NETWORK_ERROR);
            mAdapter.notifyDataSetChanged();
        }
    }

    // 完成刷新
    protected void executeOnLoadFinish() {
        setSwipeRefreshLoadedState();
        mState = STATE_NONE;
    }

    /** 设置顶部正在加载的状态 */
    private void setSwipeRefreshLoadingState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /** 设置顶部加载完毕的状态 */
    private void setSwipeRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    private void executeParserTask(byte[] data) {
        cancelParserTask();
        mParserTask = new ParserTask(data);
        mParserTask.execute();
    }

    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }

    class ParserTask extends AsyncTask<Void, Void, String> {

        private final byte[] reponseData;
        private boolean parserError;
        private List<Moment> list;

        public ParserTask(byte[] data) {
            this.reponseData = data;
        }

        //保存数据
        public void saveCacheData(ListEntity<Moment> data)
        {
            new SaveCacheTask(getActivity(), data, getCacheKey()).execute();
        }
        @Override
        protected String doInBackground(Void... params) {
            try {
                ListEntity<Moment> data = parseList(new ByteArrayInputStream(
                        reponseData));

                saveCacheData(data);

                list = data.getList();

            } catch (Exception e) {
                e.printStackTrace();
                parserError = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (parserError) {
                readCacheData(getCacheKey());
            } else {
                executeOnLoadDataSuccess(list);
                executeOnLoadFinish();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }
        // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
        if (mState == STATE_LOADMORE || mState == STATE_REFRESH) {
            return;
        }
        // 判断是否滚动到底部
        boolean scrollEnd = false;
        try {
            if (view.getPositionForView(mAdapter.getFooterView()) == view
                    .getLastVisiblePosition())
                scrollEnd = true;
        } catch (Exception e) {
            scrollEnd = false;
        }

//        AppContext.showToast("mCurrentPage="+mCurrentPage);

        if (mState == STATE_NONE && scrollEnd) {
            if (mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE
                    || mAdapter.getState() == ListBaseAdapter.STATE_NETWORK_ERROR) {
                mCurrentPage++;
                mState = STATE_LOADMORE;
//                AppContext.showToast("requestData:false");
                requestData(false);
                mAdapter.setFooterViewLoading();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
//         数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
//         if (mState == STATE_NOMORE || mState == STATE_LOADMORE
//         || mState == STATE_REFRESH) {
//         return;
//         }
//         if (mAdapter != null
//         && mAdapter.getDataSize() > 0
//         && mListView.getLastVisiblePosition() == (mListView.getCount() - 1))
//         {
//         if (mState == STATE_NONE
//         && mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
//         mState = STATE_LOADMORE;
//         mCurrentPage++;
//         requestData(true);
//         }
//         }
    }

    //定义填充头部的ui和初始化头部的视图
    protected  abstract  void initNoticeWidget(Context context);
    protected  abstract  View initHeaderView();
    protected  abstract  void setData();
}
