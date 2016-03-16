package com.dg.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.umeng.message.PushAgent;

public class AboutDGActivity extends Activity implements View.OnClickListener{

    private ImageView iv_setting_about_back;

    private TextView tv_dg_protocol;

    private RelativeLayout rl_offical_activity;
    private RelativeLayout rl_help_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dg);
        PushAgent.getInstance(this).onAppStart();

        iv_setting_about_back = (ImageView)findViewById(R.id.iv_setting_about_back);
        iv_setting_about_back.setOnClickListener(this);

        tv_dg_protocol = (TextView)findViewById(R.id.tv_dg_protocol);
        tv_dg_protocol.setOnClickListener(this);

        rl_offical_activity = (RelativeLayout) findViewById(R.id.rl_offical_activity);
        rl_help_center = (RelativeLayout) findViewById(R.id.rl_help_center);

        rl_offical_activity.setOnClickListener(this);
        rl_help_center.setOnClickListener(this);
        AppManager.getAppManager().addActivity(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_setting_about_back:
                this.finish();
                break;
            case R.id.rl_offical_activity:
                showOfficalActivity();
                break;
            case R.id.rl_help_center:
                showHelpCenter();
                break;
            case R.id.tv_dg_protocol:
                showProtocol();
                break;
        }
    }

    //官方活动
    private void showOfficalActivity() {
        Toast.makeText(AboutDGActivity.this, "官方活动", Toast.LENGTH_SHORT).show();
    }

    //帮助中心
    private void showHelpCenter() {
        Toast.makeText(AboutDGActivity.this, "帮助中心", Toast.LENGTH_SHORT).show();
    }

    //查看协议
    private void showProtocol() {
        Toast.makeText(AboutDGActivity.this, "查看协议", Toast.LENGTH_SHORT).show();
    }



}
