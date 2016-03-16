package com.dg.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.umeng.message.PushAgent;

public class NickNameEditActivity extends Activity implements OnClickListener{

	public static int MAX_LEN = 8;

	public static String NICKNAME = "NICKNAME";

	//顶部标题栏
	private LinearLayout titlebar_nickname_edit;
	private ImageView iv_nickname_edit_back;
	private TextView tv_nickname_complete;

	// 输入框
	private EditText et_nickname;

	private TextView tv_word_count;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nickname_edit);
		PushAgent.getInstance(this).onAppStart();
		AppManager.getAppManager().addActivity(this);
		initView();

		AppManager.getAppManager().addActivity(this);
	}

	private void initView() {
		// 标题栏
		titlebar_nickname_edit = (LinearLayout)findViewById(R.id.titlebar_nickname_edit);
		iv_nickname_edit_back = (ImageView)titlebar_nickname_edit.findViewById(R.id.iv_nickname_edit_back);
		iv_nickname_edit_back.setOnClickListener(this);

		tv_nickname_complete = (TextView)titlebar_nickname_edit.findViewById(R.id.tv_nickname_complete);
		tv_nickname_complete.setOnClickListener(this);

		// 输入框
		et_nickname = (EditText) findViewById(R.id.et_nickname);

		et_nickname.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.length() >= MAX_LEN) {
					tv_word_count.setText("已达到最大长度");
				} else {
					tv_word_count.setText((MAX_LEN - s.length())
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
					et_nickname.setText(s.subSequence(0, MAX_LEN));
					CharSequence text = et_nickname.getText();
					if (text instanceof Spannable) {
						Selection.setSelection((Spannable) text, MAX_LEN);
					}
				}
			}
		});


		tv_word_count = (TextView)findViewById(R.id.tv_word_count);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_nickname_edit_back:
			finish();
			break;
		case R.id.tv_nickname_complete:
			Toast.makeText(NickNameEditActivity.this,et_nickname.getText().toString(),Toast.LENGTH_SHORT).show();
			UpdateNickName();
			break;
		}
	}

	private void UpdateNickName() {
		if(et_nickname.getText()!=null && !"".equals(et_nickname.getText())){
			Intent intent=new Intent();
			intent.putExtra(NICKNAME, et_nickname.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
		}else{
			et_nickname.setError("昵称不能为空");
		}
	}


}
