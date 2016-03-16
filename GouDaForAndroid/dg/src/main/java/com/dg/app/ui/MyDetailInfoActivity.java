package com.dg.app.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.fragment.MyDetailInfoFragment;
import com.umeng.message.PushAgent;

public class MyDetailInfoActivity extends FragmentActivity {

    private FragmentManager fm;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);
        try {
            setContentView(R.layout.activity_setting);
            fm = getSupportFragmentManager();
            transaction = fm.beginTransaction();
            MyDetailInfoFragment myDetailInfoFragment = new MyDetailInfoFragment();
            myDetailInfoFragment.setContext(this);
            transaction.replace(R.id.fl_setting_content, myDetailInfoFragment);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
