package com.dg.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.umeng.message.PushAgent;

public class DogName extends Activity {
    EditText editText;
    // 输入框
    private EditText et_write_status;
    public static int MAX_LEN = 10;
    private TextView tweet_text_record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_name);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

        // 输入框
            et_write_status = (EditText) findViewById(R.id.dog_name);
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
                                              int after) {
                }

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
        Intent intent=this.getIntent();
        intent.putExtra("name",et_write_status.getText().toString()+"");
        this.setResult(RESULT_OK,intent);
        finish();
        overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
    }
    public void back(View v)
    {
        finish();
    }


}
