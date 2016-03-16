package com.dg.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.umeng.message.PushAgent;

public class NoticeSettingActivity extends Activity implements View.OnClickListener{

    private LinearLayout layout_noticesetting_title;
    private ImageView iv_setting_titlebar_back;

    private ImageView noticesound_setting;
    private ImageView gouquannotice_setting;
    private ImageView chatnotice_setting;
    private ImageView agreeinvite_setting;

    private boolean if_sound,if_notice,if_chat_notice,if_can_invite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_setting_noticesetting);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

        layout_noticesetting_title = (LinearLayout) findViewById(R.id.layout_noticesetting_title);

        iv_setting_titlebar_back = (ImageView) layout_noticesetting_title.findViewById(R.id.iv_setting_titlebar_back);
        iv_setting_titlebar_back.setOnClickListener(this);

        noticesound_setting = (ImageView) findViewById(R.id.noticesound_setting);
        gouquannotice_setting = (ImageView) findViewById(R.id.gouquannotice_setting);
        chatnotice_setting = (ImageView) findViewById(R.id.chatnotice_setting);
        agreeinvite_setting = (ImageView) findViewById(R.id.agreeinvite_setting);

        noticesound_setting.setOnClickListener(this);
        gouquannotice_setting.setOnClickListener(this);
        chatnotice_setting.setOnClickListener(this);
        agreeinvite_setting.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_setting_updatepwd_back:
                Toast.makeText(NoticeSettingActivity.this,"退出当前页面",Toast.LENGTH_SHORT).show();
                this.finish();
                break;

            case R.id.noticesound_setting:
                Toast.makeText(NoticeSettingActivity.this,"通知声音修改",Toast.LENGTH_SHORT).show();
                if(if_sound){
                    if_sound = false;
                    noticesound_setting.setImageResource(R.mipmap.close);
                    }else{
                    if_sound = true;
                    noticesound_setting.setImageResource(R.mipmap.open);
                }

                break;
            case R.id.gouquannotice_setting:
                Toast.makeText(NoticeSettingActivity.this,"狗圈通知",Toast.LENGTH_SHORT).show();
                if(if_notice){
                    if_notice = false;
                    gouquannotice_setting.setImageResource(R.mipmap.close);
                }else{
                    if_notice = true;
                    gouquannotice_setting.setImageResource(R.mipmap.open);
                }
                break;
            case R.id.chatnotice_setting:
                Toast.makeText(NoticeSettingActivity.this,"聊天提醒",Toast.LENGTH_SHORT).show();
                if(if_chat_notice){
                    if_chat_notice = false;
                    chatnotice_setting.setImageResource(R.mipmap.close);
                }else{
                    if_chat_notice = true;
                    chatnotice_setting.setImageResource(R.mipmap.open);
                }
                break;
            case R.id.agreeinvite_setting:
                Toast.makeText(NoticeSettingActivity.this,"运行通过狗圈发起邀请",Toast.LENGTH_SHORT).show();
                if(if_can_invite){
                    if_can_invite = false;
                    agreeinvite_setting.setImageResource(R.mipmap.close);
                }else{
                    if_can_invite = true;
                    agreeinvite_setting.setImageResource(R.mipmap.open);
                }
                break;
        }
    }


}
