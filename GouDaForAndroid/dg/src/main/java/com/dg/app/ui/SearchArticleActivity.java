package com.dg.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.fragment.DogKnowSearchFragement;
import com.dg.app.fragment.DogKnowTopicFragement;
import com.umeng.message.PushAgent;

import java.util.Timer;
import java.util.TimerTask;

public class SearchArticleActivity extends FragmentActivity {

    private FrameLayout subFragement;
    private TextView topTitle;
    private ImageView back;
    private int category_id;
    FragmentManager fm;
    private EditText query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_article);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

        initView();
    }
    public  void initView()
    {
        topTitle=(TextView)this.findViewById(R.id.subarticle_top);
        fm=getSupportFragmentManager();



        query=(EditText)this.findViewById(R.id.query);
        query.setFocusable(true);
        query.setFocusableInTouchMode(true);
        query.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) query.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(query, 0);
                           }

                       },
                800);
        query.setOnTouchListener(new View.OnTouchListener() {

            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean iskeyboardShow=false;
                if (event.getX() >= (query.getRight() - query
                        .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds()
                        .width())-20){

                    if(query.getText().toString().replaceAll(" ","")!="") {
                        DogKnowSearchFragement dogKnowSearchFragement = new DogKnowSearchFragement();
                        Bundle bundle = new Bundle();
                        bundle.putString("BUNDLE_KEY_SEARCH_ID", query.getText().toString());
                        dogKnowSearchFragement.setArguments(bundle);
                        FragmentTransaction transaction2 = fm.beginTransaction();
                        transaction2.replace(R.id.subarticle_containers, dogKnowSearchFragement);
                        transaction2.commit();

                    }
                   iskeyboardShow=true;
                }
                return iskeyboardShow;
            }
        });

    }
    public void back(View view)
    {
        finish();
    }

}

