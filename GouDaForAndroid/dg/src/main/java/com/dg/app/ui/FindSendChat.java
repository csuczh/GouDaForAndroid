package com.dg.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.bean.Owners;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.umeng.message.PushAgent;

public class FindSendChat extends Activity {

    boolean isfriend;
    String easemob_id;
    // 输入框
    private EditText et_write_status;

    public static int MAX_LEN = 20;
    private TextView tweet_text_record;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_send_chat);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

        isfriend=getIntent().getBooleanExtra("isfriend",false);
        easemob_id=getIntent().getStringExtra("easemob_id");
        // 输入框
        et_write_status = (EditText) findViewById(R.id.nearby_send_chat);
        tweet_text_record = (TextView) findViewById(R.id.tweet_text_record);

        et_write_status.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() >= MAX_LEN) {
                    tweet_text_record.setText("已达到最大长度");
                } else {
                    tweet_text_record.setText((MAX_LEN - s.length())
                            + " X");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > MAX_LEN) {
                    et_write_status.setText(s.subSequence(0, MAX_LEN));
                    CharSequence text = et_write_status.getText();
                    if (text instanceof Spannable) {
                        Selection.setSelection((Spannable) text, MAX_LEN);
                    }
                }
            }
        });
    }
    public void sendName(View view)
    {
        if(!isfriend)
        {
            try {
                EMContactManager.getInstance().addContact(easemob_id,et_write_status.getText().toString());
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }else{
            String name= AppContext.getInstance().getProperty("dgUser.easemob_id");
            EMConversation conversation= EMChatManager.getInstance().getConversation(name);
            EMMessage message=EMMessage.createSendMessage(EMMessage.Type.TXT);
            TextMessageBody txtBody=new TextMessageBody(et_write_status.getText().toString());
            message.addBody(txtBody);
            message.setReceipt(easemob_id);
            conversation.addMessage(message);
            EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });

        }

        Toast.makeText(this, "发送消息成功啦！", Toast.LENGTH_LONG).show();
        finish();
        overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
    }
    public void back(View v)
    {
        finish();
    }


}
