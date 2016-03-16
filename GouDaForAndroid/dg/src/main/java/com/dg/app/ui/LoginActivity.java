package com.dg.app.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.fragment.LoginHomeFragment;
import com.umeng.message.PushAgent;

public class LoginActivity extends FragmentActivity{

    private FragmentManager fm;
    private FragmentTransaction transaction;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PushAgent.getInstance(this).onAppStart();
        activity=this;

//        设置屏幕竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LoginHomeFragment loginHomeFragment = new LoginHomeFragment();
        loginHomeFragment.setContext(this,activity);

        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_loginactivity, loginHomeFragment);
        transaction.commit();
    }

}
