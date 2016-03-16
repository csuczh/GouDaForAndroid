package com.dg.app.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.fragment.DogKnowTopicFragement;
import com.umeng.message.PushAgent;

public class SubArticleActivity extends FragmentActivity {

    private FrameLayout subFragement;
    private TextView topTitle;
    private ImageView back;
    private int category_id;
    FragmentManager fm;
    private String category_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_sub_article);
        category_id=getIntent().getIntExtra("category_id",0);
        category_name=getIntent().getStringExtra("category_name");
        initView();
        AppManager.getAppManager().addActivity(this);
    }
    public  void initView()
    {
        topTitle=(TextView)this.findViewById(R.id.subarticle_top);
        topTitle.setText(category_name+"");
        fm=getSupportFragmentManager();
        DogKnowTopicFragement dogKnowTopicFragement = new DogKnowTopicFragement();
        Bundle bundle = new Bundle();
        bundle.putInt("BUNDLE_KEY_TOPIC_ID", category_id);
        dogKnowTopicFragement.setArguments(bundle);
        FragmentTransaction transaction2 = fm.beginTransaction();
        transaction2.replace(R.id.subarticle_containers, dogKnowTopicFragement);
        transaction2.commit();

    }
    public void back(View view)
    {
        finish();
    }

}
