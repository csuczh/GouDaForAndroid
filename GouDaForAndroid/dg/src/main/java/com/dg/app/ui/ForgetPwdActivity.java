package com.dg.app.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.fragment.ForgetPwdFragment;
import com.dg.app.fragment.LoginHomeFragment;
import com.umeng.message.PushAgent;

public class ForgetPwdActivity extends FragmentActivity{

    private FragmentManager fm;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

//        设置屏幕竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ForgetPwdFragment forgetPwdFragment = new ForgetPwdFragment();
        forgetPwdFragment.setContext(this);

        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_loginactivity, forgetPwdFragment);
        transaction.commit();

    }

}
