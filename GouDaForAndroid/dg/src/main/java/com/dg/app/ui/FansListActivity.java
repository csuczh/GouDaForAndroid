package com.dg.app.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.fragment.FansListFragment;
import com.dg.app.fragment.UserInfoFragment;
import com.umeng.message.PushAgent;

public class FansListActivity extends FragmentActivity implements View.OnClickListener {

    private FragmentManager fm;
    private FragmentTransaction transaction;

    //标题栏
    private LinearLayout layout_fanslist;
    private ImageView iv_fanslist_back;

    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanslist);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);
        initTitleBar();

        user_id = getIntent().getIntExtra("user_id",0);

        //1、进入详细信息设置界面
        FansListFragment fansListFragment = new FansListFragment();
        fansListFragment.setUser_id(user_id);
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_fanslist, fansListFragment);
        transaction.commit();

    }

    private void initTitleBar() {
        //标题栏
        layout_fanslist = (LinearLayout) findViewById(R.id.layout_fanslist);
        iv_fanslist_back = (ImageView) layout_fanslist.findViewById(R.id.iv_fanslist_back);
        iv_fanslist_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_fanslist_back:
                finish();
                break;
        }
    }

}
