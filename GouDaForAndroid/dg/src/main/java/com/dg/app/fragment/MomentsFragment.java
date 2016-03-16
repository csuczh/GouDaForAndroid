package com.dg.app.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.f;
import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.ui.MyMessageActivity;
import com.dg.app.ui.WriteStatusActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class MomentsFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    public static final String CITY_UPDATE_NAME = "HOT_UPDATE_NAME";

    private boolean is_update = false;
    //顶部actionbar
    private LinearLayout titlebar_gouquan;
    private ImageView iv_mymessage;
    private Button button_samecity;
    private Button button_hot;
    private Button button_follow;
    private ImageView iv_write_status;

    private android.support.v4.app.FragmentManager fm;

    private SameCityMomentFragment sameCityMomentFragment;

    private HotMomentFragment hotMomentFragment;

    private FollowedMomentFragment followedMomentFragment;

    private FragmentUpdateReceiver fragmentUpdateReceiver;

//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = View.inflate(getActivity(),
                R.layout.fragment_moments, null);

        initTitlebBar(root);
        initView();

        fragmentUpdateReceiver = new FragmentUpdateReceiver();
        //注册广播
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(CITY_UPDATE_NAME);
        getActivity().registerReceiver(fragmentUpdateReceiver, myIntentFilter);

        return root;
    }

    //初始化标题栏
    private void initTitlebBar(View root) {
        titlebar_gouquan = (LinearLayout) root.findViewById(R.id.titlebar_gouquan);
        iv_mymessage = (ImageView) titlebar_gouquan.findViewById(R.id.iv_mymessage);
        button_samecity = (Button) titlebar_gouquan.findViewById(R.id.button_samecity);
        button_hot = (Button) titlebar_gouquan.findViewById(R.id.button_hot);
        button_follow = (Button) titlebar_gouquan.findViewById(R.id.button_follow);
        iv_write_status = (ImageView) titlebar_gouquan.findViewById(R.id.iv_write_status);

        iv_mymessage.setOnClickListener(this);
        button_samecity.setOnClickListener(this);
        button_hot.setOnClickListener(this);
        button_follow.setOnClickListener(this);
        iv_write_status.setOnClickListener(this);
    }


    //    初始化视图组件
    protected void initView() {

        sameCityMomentFragment = new SameCityMomentFragment();
        hotMomentFragment = new HotMomentFragment();
        followedMomentFragment = new FollowedMomentFragment();

        fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fl_moment_list, sameCityMomentFragment)
                .commit();
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_mymessage:{

                try{
                    //跳转到我的消息页面
                    Intent intent = new Intent(getActivity(),MyMessageActivity.class);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            }

            case R.id.button_samecity:{
                //同城
                showSameCity();
                break;
            }

            case R.id.button_hot:{
                //热门
                showHot();
                break;
            }

            case R.id.button_follow:{
                //关注
                showFollowed();
                break;
            }

            case R.id.iv_write_status:{
                //写状态
                Intent intent = new Intent(getActivity(),WriteStatusActivity.class);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            }

        }
    }

    //同城
    private void showSameCity() {
        //更新顶部actionbar
        updateActionBar(1);
        if(sameCityMomentFragment==null){
            sameCityMomentFragment = new SameCityMomentFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fl_moment_list, sameCityMomentFragment)
                .commit();
    }

    //热门
    private void showHot() {
        //更新顶部actionbar
        updateActionBar(2);
        if(hotMomentFragment==null){
            hotMomentFragment = new HotMomentFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fl_moment_list, hotMomentFragment)
                .commit();
    }

    //关注
    private void showFollowed() {
        //更新顶部actionbar
        updateActionBar(3);
        if(followedMomentFragment==null){
            followedMomentFragment = new FollowedMomentFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fl_moment_list, followedMomentFragment)
                .commit();

    }


    public void updateActionBar(int index){
        button_samecity.setBackgroundResource(R.drawable.bg_actionbar_corner);
        button_hot.setBackgroundResource(R.drawable.bg_actionbar_corner);
        button_follow.setBackgroundResource(R.drawable.bg_actionbar_corner);
        button_samecity.setTextColor(getResources().getColor(R.color.white));
        button_hot.setTextColor(getResources().getColor(R.color.white));
        button_follow.setTextColor(getResources().getColor(R.color.white));
        if(index==1){
            button_samecity.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            button_samecity.setTextColor(getResources().getColor(R.color.actionbar_normal));
        }
        if(index==2){
            button_hot.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            button_hot.setTextColor(getResources().getColor(R.color.actionbar_normal));
        }
        if(index==3){
            button_follow.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            button_follow.setTextColor(getResources().getColor(R.color.actionbar_normal));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //注销广播
        getActivity().unregisterReceiver(fragmentUpdateReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(is_update){
            if(sameCityMomentFragment!=null){
                sameCityMomentFragment.refresh();
            }
        }
    }

    /**
     * //发布状态广播接受器
     */
    class FragmentUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(CITY_UPDATE_NAME)){
                if(sameCityMomentFragment!=null){
                    sameCityMomentFragment.refresh();
                }else{
                    is_update = true;
                }
            }

        }
    }

}
