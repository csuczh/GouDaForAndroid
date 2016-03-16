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
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.bean.DGDogDetailResponse;
import com.dg.app.bean.DGUserDetailResponse;
import com.dg.app.fragment.DogPageFragment;
import com.dg.app.fragment.DogPagesFragment;
import com.dg.app.fragment.PersonalPageFragment;
import com.dg.app.fragment.PersonalPagesFragment;
import com.umeng.message.PushAgent;

public class DGUserInfoActivity extends FragmentActivity implements View.OnClickListener{

    private FragmentManager fm;
    private FragmentTransaction transaction;

    private LinearLayout titlebar_my_detailinfo;

    private ImageView iv_detailinfo_titlebar_back;

    private Button button_my_page;

    private Button button_dog_page;

    private TextView tv_detailinfo_edit;

//    private PersonalPageFragment personalPageFragment;

    private PersonalPagesFragment personalPagesFragment;

//    private DogPageFragment dogPageFragment;

    private DogPagesFragment dogPagesFragment;

    private int whichPage = 1;

    private int user_id = 0;

    private DGUserDetailResponse.UserDetail userDetail;
    private DGDogDetailResponse.DogDetail dogDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_dg_user_info_main);
        AppManager.getAppManager().addActivity(this);

        user_id = getIntent().getIntExtra("user_id",0);

        initTitleBar();

        initFragment();

    }

    private void initTitleBar() {
        titlebar_my_detailinfo = (LinearLayout) findViewById(R.id.titlebar_my_detailinfo);

        iv_detailinfo_titlebar_back = (ImageView) titlebar_my_detailinfo.findViewById(R.id.iv_detailinfo_titlebar_back);

        button_my_page = (Button) titlebar_my_detailinfo.findViewById(R.id.button_my_page);
        button_dog_page = (Button) titlebar_my_detailinfo.findViewById(R.id.button_dog_page);

        tv_detailinfo_edit = (TextView) titlebar_my_detailinfo.findViewById(R.id.tv_detailinfo_edit);

        if(getUser_id() != AppContext.getInstance().getLoginUid()){
            tv_detailinfo_edit.setVisibility(View.GONE);
            tv_detailinfo_edit.setClickable(false);
        }

        iv_detailinfo_titlebar_back.setOnClickListener(this);
        button_my_page.setOnClickListener(this);
        button_dog_page.setOnClickListener(this);
        tv_detailinfo_edit.setOnClickListener(this);
    }

    private void initFragment() {
        try {

//            personalPageFragment = new PersonalPageFragment();
//            personalPageFragment.setdGActivity(this);

            personalPagesFragment = new PersonalPagesFragment();
            personalPagesFragment.setdGActivity(this);


//            dogPageFragment = new DogPageFragment();
//            dogPageFragment.setdGActivity(this);

            dogPagesFragment = new DogPagesFragment();
            dogPagesFragment.setdGActivity(this);

            fm = getSupportFragmentManager();
            transaction = fm.beginTransaction();
//            transaction.replace(R.id.fl_infos, personalPageFragment);
            transaction.replace(R.id.fl_infos, personalPagesFragment);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button_my_page:
                showMyPage();
                break;

            case R.id.button_dog_page:
                showDogPage();
                break;

            case R.id.tv_detailinfo_edit:{
                enterEditPage();
                break;
            }

            case R.id.iv_detailinfo_titlebar_back:
                Toast.makeText(this, "退出", Toast.LENGTH_SHORT).show();
                finish();
                break;

        }
    }

    private void enterEditPage() {
        try{
            //编辑
            if(whichPage==1){
                Toast.makeText(DGUserInfoActivity.this, "进入我的信息编辑页", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DGUserInfoActivity.this, MyDetailInfoEditActivity.class);
//                userDetail = personalPageFragment.getUserDetail();

                userDetail = personalPagesFragment.getUserDetail();

                if(userDetail!=null){
                    intent.putExtra("userDetail",userDetail);
                    startActivity(intent);
                }
                return ;
            }else if(whichPage==2){
                Toast.makeText(DGUserInfoActivity.this, "进入狗狗信息编辑页", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DGUserInfoActivity.this, DogDetailInfoEditActivity.class);
//                dogDetail = dogPageFragment.getDogDetail();
                dogDetail = dogPagesFragment.getDogDetail();

                if(dogDetail!=null){
                    intent.putExtra("dogDetail",dogDetail);
                }
                startActivity(intent);
                return ;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //显示狗狗主页
    public void showDogPage() {
        //更新顶部actionbar
        updateActionBar(2);
        whichPage=2;
//        if(dogPageFragment==null){
//            dogPageFragment = new DogPageFragment();
//            dogPageFragment.setdGActivity(this);
//
//        }
//        fm.beginTransaction()
//                .replace(R.id.fl_infos, dogPageFragment)
//                .commit();

        if(dogPagesFragment==null){
            dogPagesFragment = new DogPagesFragment();
            dogPagesFragment.setdGActivity(this);

        }
        fm.beginTransaction()
                .replace(R.id.fl_infos, dogPagesFragment)
                .commit();

    }

    //显示我的主页
    public void showMyPage() {
        //更新顶部actionbar
        updateActionBar(1);
        whichPage=1;
//        if(personalPageFragment==null){
//            personalPageFragment = new PersonalPageFragment();
//            personalPageFragment.setdGActivity(this);
//        }
//        fm.beginTransaction()
//                .replace(R.id.fl_infos, personalPageFragment)
//                .commit();


        if(personalPagesFragment==null){
            personalPagesFragment = new PersonalPagesFragment();
            personalPagesFragment.setdGActivity(this);
        }
        fm.beginTransaction()
                .replace(R.id.fl_infos, personalPagesFragment)
                .commit();

    }


    public void updateActionBar(int index){
        button_my_page.setBackgroundResource(R.drawable.bg_actionbar_corner);
        button_dog_page.setBackgroundResource(R.drawable.bg_actionbar_corner);
        button_my_page.setTextColor(getResources().getColor(R.color.white));
        button_dog_page.setTextColor(getResources().getColor(R.color.white));
        if(index==1){
            button_my_page.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            button_my_page.setTextColor(getResources().getColor(R.color.actionbar_normal));
        }
        if(index==2){
            button_dog_page.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            button_dog_page.setTextColor(getResources().getColor(R.color.actionbar_normal));
        }
    }

    public int getUser_id(){
        if(user_id==0){
            user_id = AppContext.getInstance().getLoginUid();
        }
        return user_id;
    }


    public boolean isSelf(){
        user_id = getUser_id();
        return user_id==AppContext.getInstance().getLoginUid()?true:false;
    }

}
