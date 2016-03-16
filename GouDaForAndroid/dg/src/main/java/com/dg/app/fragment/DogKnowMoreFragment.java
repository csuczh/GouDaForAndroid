package com.dg.app.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.dg.app.R;
import com.dg.app.adapter.KindAdapter;
import com.dg.app.adapter.OwnersAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseFragment;
import com.dg.app.base.BaseListFragment;
import com.dg.app.base.DogKnowMoreBaseListFragment;
import com.dg.app.bean.Kind;
import com.dg.app.bean.KindsList;
import com.dg.app.bean.Owners;
import com.dg.app.bean.OwnersList;
import com.dg.app.bean.Question;
import com.dg.app.ui.SubArticleActivity;
import com.dg.app.util.XmlUtils;

import org.kymjs.kjframe.KJBitmap;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lenovo on 2015/10/21.
 */
public class DogKnowMoreFragment extends DogKnowMoreBaseListFragment<Kind> {
    protected static final String TAG = FindNearbyFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "kindslist_";

    @Override
    protected KindAdapter getListAdapter() {
        return new KindAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected KindsList parseList(InputStream is) throws Exception {
        KindsList list = null;
        try {
            list = XmlUtils.toBean(KindsList.class, is);

        } catch (NullPointerException e) {
            list = new KindsList();
        }
        return list;
    }

    @Override
    protected KindsList readList(Serializable seri) {
        return ((KindsList) seri);
    }

    @Override
    protected void sendRequestData() {
        DGApi.getKinds(device, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        try {
            Kind kind =(Kind) parent.getAdapter().getItem(position);
            Intent intent = new Intent(parent.getContext(), SubArticleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("category_id", kind.getCategory_id());
            bundle.putString("category_name",kind.getName());
            intent.putExtras(bundle);
            parent.getContext().startActivity(intent);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    protected void executeOnLoadDataSuccess(List<Kind> data) {
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