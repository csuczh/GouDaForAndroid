package com.dg.app.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.fragment.LocationFragment;
import com.dg.app.fragment.LoginHomeFragment;
import com.umeng.message.PushAgent;

public class LocationActivity extends FragmentActivity{

    private FragmentManager fm;
    private FragmentTransaction transaction;

    private FrameLayout fl_loginactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

//        设置屏幕竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LocationFragment locationFragment = new LocationFragment();
        locationFragment.setContext(this);

        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_loginactivity, locationFragment);
        transaction.commit();

    }

}
