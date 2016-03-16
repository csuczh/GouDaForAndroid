package com.dg.app.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.fragment.SettingFragment;
import com.umeng.message.PushAgent;

public class SettingActivity extends FragmentActivity {

    private FrameLayout fl_setting_content;

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

            //设置屏幕竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


            SettingFragment settingFragment = new SettingFragment();
            transaction.replace(R.id.fl_setting_content, settingFragment);

            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
