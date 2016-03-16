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

import org.kymjs.kjframe.KJHttp;

public class ClearChatRecordActivity extends Activity implements View.OnClickListener{

    private ImageView iv_setting_clearchat_back;

    private LinearLayout ll_clear_chat;
    private LinearLayout ll_clear_invite;

    private KJHttp kjh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clearchat);
        PushAgent.getInstance(this).onAppStart();

        iv_setting_clearchat_back = (ImageView)findViewById(R.id.iv_setting_clearchat_back);
        iv_setting_clearchat_back.setOnClickListener(this);

        ll_clear_chat = (LinearLayout) findViewById(R.id.ll_clear_chat);
        ll_clear_invite = (LinearLayout) findViewById(R.id.ll_clear_invite);

        ll_clear_chat.setOnClickListener(this);
        ll_clear_invite.setOnClickListener(this);

        AppManager.getAppManager().addActivity(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_setting_clearchat_back:
                this.finish();
                break;
            case R.id.ll_clear_chat:
                clearChat();
                break;
            case R.id.ll_clear_invite:
                clearInvite();
                break;
        }
    }

    //清除聊天记录
    private void clearChat() {
        Toast.makeText(ClearChatRecordActivity.this, "清楚聊天记录", Toast.LENGTH_SHORT).show();
    }

    //清除邀请信息记录
    private void clearInvite() {
        Toast.makeText(ClearChatRecordActivity.this, "清楚邀请信息记录", Toast.LENGTH_SHORT).show();
    }


}
