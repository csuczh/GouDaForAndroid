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

public class UserJobEditActivity extends Activity implements OnClickListener{

	public static int MAX_LEN = 8;

	public static String USERJOB = "USERJOB";

	//顶部标题栏
	private LinearLayout titlebar_job_edit;
	private ImageView iv_job_edit_back;
	private TextView tv_job_complete;

	// 输入框
	private EditText et_job;

	private TextView tv_word_count;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_edit);
		PushAgent.getInstance(this).onAppStart();
		AppManager.getAppManager().addActivity(this);

		initView();

		AppManager.getAppManager().addActivity(this);
	}

	private void initView() {
		// 标题栏
		titlebar_job_edit = (LinearLayout)findViewById(R.id.titlebar_job_edit);
		iv_job_edit_back = (ImageView)titlebar_job_edit.findViewById(R.id.iv_job_edit_back);
		iv_job_edit_back.setOnClickListener(this);

		tv_job_complete = (TextView)titlebar_job_edit.findViewById(R.id.tv_job_complete);
		tv_job_complete.setOnClickListener(this);

		// 输入框
		et_job = (EditText) findViewById(R.id.et_job);
		et_job.addTextChangedListener(new TextWatcher() {

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
					et_job.setText(s.subSequence(0, MAX_LEN));
					CharSequence text = et_job.getText();
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
		case R.id.iv_job_edit_back:
			finish();
			break;
		case R.id.tv_job_complete:
			Toast.makeText(UserJobEditActivity.this,et_job.getText().toString(),Toast.LENGTH_SHORT).show();
			UpdateJob();
			break;
		}
	}

	private void UpdateJob() {
		try{

			if(et_job.getText()!=null && !"".equals(et_job.getText())){
				Intent intent=new Intent();
				intent.putExtra(USERJOB, et_job.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
			}else{
				et_job.setError("职业不能为空");
			}

		}catch (Exception e){
			e.printStackTrace();
		}

	}


}
