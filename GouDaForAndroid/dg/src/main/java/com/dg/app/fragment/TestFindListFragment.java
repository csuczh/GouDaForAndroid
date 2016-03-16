package com.dg.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dg.app.R;
import com.dg.app.adapter.OwnersAdapter;
import com.dg.app.base.BaseFragment;
import com.dg.app.base.ListBaseAdapter;
import com.dg.app.bean.Entity;
import com.dg.app.bean.Owners;
import com.dg.app.ui.DetailUserActivity;
import com.dg.app.ui.empty.EmptyLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by czh on 2015/9/23.
 */
public class TestFindListFragment
//        extends BaseFragment
//implements AdapterView.OnItemClickListener
{
//    @InjectView(R.id.swiperefreshlayout)
//    protected SwipeRefreshLayout mSwipeRefreshLayout;
//
//    @InjectView(R.id.listview)
//    protected ListView mListView;
//
//    protected OwnersAdapter mAdapter;
//
//    @InjectView(R.id.error_layout)
//    protected EmptyLayout mErrorLayout;
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_pull_refresh_listview;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(getLayoutId(), container, false);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        ButterKnife.inject(this, view);
//        initView(view);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        ArrayList<Owners> datas=new ArrayList<Owners>();
//        for(int i=0;i<10;i++)
//        {
//            Owners temp=new Owners();
//            temp.setUrl("http://img3.imgtn.bdimg.com/it/u=2117727038,2641018931&fm=21&gp=0.jpg");
//            temp.setSex(0);
//            temp.setNickname("大熊");
//            temp.setAge(18);
//            temp.setActor("学生");
//            temp.setDistance("0.2km");
//            temp.setTime("0.9秒");
//            temp.setState(0);
//            datas.add(temp);
//        }
//        try {
//            mAdapter=new OwnersAdapter();
//            mAdapter.addData(datas);
//        }
//       catch (Exception ex)
//       {
//           ex.printStackTrace();
//       }
//
//
//
//
//    }
//
//    @Override
//    public void initView(View view) {
//          mListView.setAdapter(mAdapter);
//          mListView.setOnItemClickListener(this);
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,
//                            long id) {
//        try{
//        Intent intent = new Intent();
//        intent.setClass(this.getActivity(), DetailUserActivity.class);
//        startActivity(intent);}
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }

}
