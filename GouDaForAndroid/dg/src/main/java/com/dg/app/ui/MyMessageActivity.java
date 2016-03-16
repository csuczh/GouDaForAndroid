package com.dg.app.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.fragment.LikeMessageFragment;
import com.dg.app.fragment.ReplyMessageFragment;
import com.umeng.message.PushAgent;

public class MyMessageActivity extends FragmentActivity implements OnClickListener{

	//标题栏
	private LinearLayout titlebar_mymessages;
	private ImageView iv_message_titlebar_back;
	private TextView tv_message_titlebar_cancle;
	private Button button_comment;
	private Button button_like;

	private android.support.v4.app.FragmentManager fm;

	private ReplyMessageFragment replyMessageFragment;
	private LikeMessageFragment likeMessageFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mymessages);
		PushAgent.getInstance(this).onAppStart();
		initTitleBar();
		initView();

		AppManager.getAppManager().addActivity(this);
	}

	private void initView() {
		replyMessageFragment = new ReplyMessageFragment();
		likeMessageFragment = new LikeMessageFragment();


		fm = getSupportFragmentManager();
		fm.beginTransaction()
				.replace(R.id.fl_messages_list, replyMessageFragment)
				.commit();

	}

	private void initTitleBar() {
		//标题栏
		titlebar_mymessages = (LinearLayout) findViewById(R.id.titlebar_mymessages);
		iv_message_titlebar_back = (ImageView) titlebar_mymessages.findViewById(R.id.iv_message_titlebar_back);
		tv_message_titlebar_cancle = (TextView) titlebar_mymessages.findViewById(R.id.tv_message_titlebar_cancle);

		button_comment = (Button) titlebar_mymessages.findViewById(R.id.button_comment);
		button_like = (Button) titlebar_mymessages.findViewById(R.id.button_like);

		iv_message_titlebar_back.setOnClickListener(this);
		tv_message_titlebar_cancle.setOnClickListener(this);
		button_comment.setOnClickListener(this);
		button_like.setOnClickListener(this);


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_message_titlebar_back:
			finish();
			break;
		case R.id.tv_message_titlebar_cancle:
			Toast.makeText(this, "清空消息", Toast.LENGTH_SHORT).show();

			break;
		case R.id.button_comment:
			showComment();
			break;
		case R.id.button_like:
			showLike();
			break;
		}
	}

	private void showLike() {
		//更新顶部actionbar
		updateActionBar(2);
		if(likeMessageFragment == null){
			likeMessageFragment = new LikeMessageFragment();
		}
		fm.beginTransaction()
				.replace(R.id.fl_messages_list, likeMessageFragment)
				.commit();
	}

	private void showComment() {
		//更新顶部actionbar
		updateActionBar(1);
		if(replyMessageFragment == null){
			replyMessageFragment = new ReplyMessageFragment();
		}
		fm.beginTransaction()
				.replace(R.id.fl_messages_list, replyMessageFragment)
				.commit();
	}

	public void updateActionBar(int index) {
		button_comment.setBackgroundResource(R.drawable.bg_actionbar_corner);
		button_like.setBackgroundResource(R.drawable.bg_actionbar_corner);
		button_comment.setTextColor(getResources().getColor(R.color.white));
		button_like.setTextColor(getResources().getColor(R.color.white));
		if (index == 1) {
			button_comment.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
			button_comment.setTextColor(getResources().getColor(R.color.actionbar_normal));
		}
		if (index == 2) {
			button_like.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
			button_like.setTextColor(getResources().getColor(R.color.actionbar_normal));
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		if(replyMessageFragment!=null){
			replyMessageFragment.refresh();
		}
		if(likeMessageFragment!=null){
			likeMessageFragment.refresh();
		}

	}
}


