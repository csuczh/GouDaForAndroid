package com.dg.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.umeng.message.PushAgent;

public class ContactUsActivity extends Activity implements View.OnClickListener{

    private ImageView iv_setting_contact_back;

    private RelativeLayout rl_send_msg;
    private RelativeLayout rl_offical_web;
    private RelativeLayout rl_offical_weibo;
    private RelativeLayout rl_offical_weixin;
    private RelativeLayout rl_dg_qq;
    private RelativeLayout rl_dg_qq_group;
    private RelativeLayout rl_personal_weixin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);


        iv_setting_contact_back = (ImageView)findViewById(R.id.iv_setting_contact_back);
        iv_setting_contact_back.setOnClickListener(this);

        rl_send_msg = (RelativeLayout) findViewById(R.id.rl_send_msg);
        rl_offical_web = (RelativeLayout) findViewById(R.id.rl_offical_web);
        rl_offical_weibo = (RelativeLayout) findViewById(R.id.rl_offical_weibo);
        rl_offical_weixin = (RelativeLayout) findViewById(R.id.rl_offical_weixin);
        rl_dg_qq = (RelativeLayout) findViewById(R.id.rl_dg_qq);
        rl_dg_qq_group = (RelativeLayout) findViewById(R.id.rl_dg_qq_group);
        rl_personal_weixin = (RelativeLayout) findViewById(R.id.rl_personal_weixin);

        rl_send_msg.setOnClickListener(this);
        rl_offical_web.setOnClickListener(this);
        rl_offical_weibo.setOnClickListener(this);
        rl_offical_weixin.setOnClickListener(this);
        rl_dg_qq.setOnClickListener(this);
        rl_dg_qq_group.setOnClickListener(this);
        rl_personal_weixin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_setting_contact_back:
                this.finish();
                break;
            case R.id.rl_send_msg:
                sendMsg();
                break;
            case R.id.rl_offical_web:
                showOfficalWeb();
                break;
            case R.id.rl_offical_weibo:
                showOfficalWeibo();
                break;
            case R.id.rl_offical_weixin:
                showOfficalWeixin();
                break;
            case R.id.rl_dg_qq:
                showOfficalQQ();
                break;
            case R.id.rl_dg_qq_group:
                showQQGroup();
                break;
            case R.id.rl_personal_weixin:
                showPersonalWeixin();
                break;
        }
    }

    //主创个人微信
    private void showPersonalWeixin() {
        Toast.makeText(ContactUsActivity.this, "主创个人微信", Toast.LENGTH_SHORT).show();
    }

    //狗搭QQ交流群
    private void showQQGroup() {
        Toast.makeText(ContactUsActivity.this, "狗搭QQ交流群", Toast.LENGTH_SHORT).show();
    }

    //狗搭QQ公众号
    private void showOfficalQQ() {
        Toast.makeText(ContactUsActivity.this, "狗搭QQ公众号", Toast.LENGTH_SHORT).show();
    }

    //官方微信公众号
    private void showOfficalWeixin() {
        Toast.makeText(ContactUsActivity.this, "官方微信公众号", Toast.LENGTH_SHORT).show();
    }

    //官方微博平台
    private void showOfficalWeibo() {
        Toast.makeText(ContactUsActivity.this, "官方微博平台", Toast.LENGTH_SHORT).show();
    }

    //狗搭官方网站
    private void showOfficalWeb() {
        Toast.makeText(ContactUsActivity.this, "狗搭官方网站", Toast.LENGTH_SHORT).show();
    }

    //发消息给狗搭小助手
    private void sendMsg() {
        Toast.makeText(ContactUsActivity.this, "发消息给狗搭小助手", Toast.LENGTH_SHORT).show();
    }


}
