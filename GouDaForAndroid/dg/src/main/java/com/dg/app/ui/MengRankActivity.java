package com.dg.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.fragment.MengCoinFragment;
import com.dg.app.fragment.MengRankingFragment;
import com.umeng.message.PushAgent;

public class MengRankActivity extends FragmentActivity implements View.OnClickListener{

    private FragmentManager fm;
    private FragmentTransaction transaction;

    private LinearLayout layout_rongyaobang_title;

    private ImageView iv_rongyaobang_back;

    private Button button_coin;

    private Button button_rank;

    private ImageView iv_sharerank;

    private MengCoinFragment mengCoinFragment;

    private MengRankingFragment mengRankingFragment;

    private int whichPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_mengyao_rank_main);
        try {

            initTitleBar();

            initFragment();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initTitleBar() {
        layout_rongyaobang_title = (LinearLayout) findViewById(R.id.layout_rongyaobang_title);

        iv_rongyaobang_back = (ImageView) layout_rongyaobang_title.findViewById(R.id.iv_rongyaobang_back);

        button_coin = (Button) layout_rongyaobang_title.findViewById(R.id.button_coin);
        button_rank = (Button) layout_rongyaobang_title.findViewById(R.id.button_rank);

        iv_sharerank = (ImageView) layout_rongyaobang_title.findViewById(R.id.iv_sharerank);


        iv_rongyaobang_back.setOnClickListener(this);
        button_coin.setOnClickListener(this);
        button_rank.setOnClickListener(this);
        iv_sharerank.setOnClickListener(this);
    }

    private void initFragment() {
        try {
            if(mengCoinFragment==null){
                mengCoinFragment = new MengCoinFragment();
            }
            fm = getSupportFragmentManager();
            transaction = fm.beginTransaction();
            transaction.replace(R.id.fl_ranks, mengCoinFragment);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button_coin:
                showMengCoin();
                break;

            case R.id.button_rank:
                showMengRank();
                break;

            case R.id.iv_sharerank:{
                showIntro();
                break;
            }

            case R.id.iv_rongyaobang_back:
                finish();
                break;

        }
    }

    private void showIntro() {
        Intent intent = new Intent(MengRankActivity.this,MengIntroActivity.class);
        startActivity(intent);
    }

    //显示萌耀币
    public void showMengCoin() {
        //更新顶部actionbar
        updateActionBar(1);
        whichPage=1;
        if(mengCoinFragment ==null){
            mengCoinFragment = new MengCoinFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fl_ranks, mengCoinFragment)
                .commit();
    }

    //显示萌耀榜
    public void showMengRank() {
        //更新顶部actionbar
        updateActionBar(2);
        whichPage=2;

        if(mengRankingFragment==null){
            mengRankingFragment = new MengRankingFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fl_ranks, mengRankingFragment)
                .commit();

    }


    public void updateActionBar(int index){
        button_coin.setBackgroundResource(R.drawable.bg_actionbar_corner);
        button_rank.setBackgroundResource(R.drawable.bg_actionbar_corner);
        button_coin.setTextColor(getResources().getColor(R.color.white));
        button_rank.setTextColor(getResources().getColor(R.color.white));
        if(index==1){
            button_coin.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            button_coin.setTextColor(getResources().getColor(R.color.actionbar_normal));
        }
        if(index==2){
            button_rank.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            button_rank.setTextColor(getResources().getColor(R.color.actionbar_normal));
        }
    }

}
