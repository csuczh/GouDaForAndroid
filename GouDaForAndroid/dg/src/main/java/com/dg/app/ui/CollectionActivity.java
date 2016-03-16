package com.dg.app.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.fragment.CollectionFragment;
import com.umeng.message.PushAgent;

public class CollectionActivity extends FragmentActivity {

    private FragmentManager fm;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        try {
            setContentView(R.layout.activity_setting);
            CollectionFragment collectionFragment = new CollectionFragment();
            fm = getSupportFragmentManager();
            transaction = fm.beginTransaction();
            transaction.replace(R.id.fl_setting_content, collectionFragment);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        销毁碎片控制器
    }

}
