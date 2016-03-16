package com.dg.app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.adapter.EmotionGvAdapter;
import com.dg.app.adapter.EmotionPagerAdapter;
import com.dg.app.adapter.WriteStatusGridImgsAdapter;
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.AddResponse;
import com.dg.app.bean.UploadImageResponse;
import com.dg.app.fragment.MomentsFragment;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.ui.widget.WrapHeightGridView;
import com.dg.app.util.DGImageUtils;
import com.dg.app.util.DisplayUtils;
import com.dg.app.util.EmotionUtils;
import com.dg.app.util.ImageUtils;
import com.dg.app.util.Parser;
import com.dg.app.util.SpecialTextUtils;
import com.dg.app.util.TDevice;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.message.PushAgent;

import org.apache.http.Header;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WriteStatusActivity extends Activity implements OnClickListener, OnItemClickListener {

	private static final String ACTION_NAME = "PUBLISH_MOMENT";
	//顶部标题栏
	private LinearLayout layout_writestatus_title;
	private ImageView iv_write_titlebar_back;
	private TextView tv_write_titlebar_send;

	// 输入框
	private EditText et_write_status;

	public static int MAX_LEN = 120;
	private static final String TEXT_ATME = "@请输入用户名 ";
	private static final String TEXT_SOFTWARE = "#请输入话题#";

	private TextView tweet_text_record;
	// 添加的九宫格图片
	private WrapHeightGridView gv_write_status;
	// 底部添加栏
	private ImageView iv_image;
	private ImageView iv_at;
	private ImageView iv_topic;
	private ImageView iv_emoji;
//	private ImageView iv_add;
	// 表情选择面板
	private LinearLayout ll_emotion_dashboard;
	private ViewPager vp_emotion_dashboard;
	// 进度框
	private ProgressDialog progressDialog;

	private WriteStatusGridImgsAdapter statusImgsAdapter;
	private ArrayList<Uri> imgUris = new ArrayList<Uri>();

	private EmotionPagerAdapter emotionPagerGvAdapter;

	private KJHttp kjh;

	private PublishMomentReceiver publishMomentReceiver;

	private Integer user_id;

	private String content;

	private StringBuffer images;

	private int imageCount = 0;

	//提示组件
	private DGNoticeToast dgNoticeToast;
	private ProgressDialog proDia;

	public synchronized void addImageCount(){
		imageCount++;
	}

	public synchronized int getImageCount(){
		return imageCount;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_status);
		PushAgent.getInstance(this).onAppStart();
		AppManager.getAppManager().addActivity(this);

		initNoticeWidget(this);

		initView();
		
		initHttp();

		user_id = AppContext.getInstance().getLoginUid();

		//实例化广播接收器
		publishMomentReceiver = new PublishMomentReceiver();
		//注册广播
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(ACTION_NAME);
		registerReceiver(publishMomentReceiver, myIntentFilter);

		AppManager.getAppManager().addActivity(this);
	}

	/**
	 * 初始化提示组件
	 * @param context
	 */
	private void initNoticeWidget(Context context) {
		proDia = new ProgressDialog(context);
		dgNoticeToast = new DGNoticeToast(context);
	}

	private void initHttp() {
		kjh = new KJHttp();
	}

	private void initView() {
		// 标题栏
		layout_writestatus_title = (LinearLayout)findViewById(R.id.layout_writestatus_title);
		iv_write_titlebar_back = (ImageView)layout_writestatus_title.findViewById(R.id.iv_write_titlebar_back);
		iv_write_titlebar_back.setOnClickListener(this);

		tv_write_titlebar_send = (TextView)layout_writestatus_title.findViewById(R.id.tv_write_titlebar_send);
		tv_write_titlebar_send.setOnClickListener(this);

		// 输入框
		et_write_status = (EditText) findViewById(R.id.et_write_status);

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
		tweet_text_record = (TextView) findViewById(R.id.tweet_text_record);

		// 添加的九宫格图片
		gv_write_status = (WrapHeightGridView) findViewById(R.id.gv_write_status);
		// 底部添加栏
		iv_image = (ImageView) findViewById(R.id.iv_image);
		iv_at = (ImageView) findViewById(R.id.iv_at);
		iv_topic = (ImageView) findViewById(R.id.iv_topic);
		iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
//		iv_add = (ImageView) findViewById(R.id.iv_add);
		// 表情选择面板
		ll_emotion_dashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
		vp_emotion_dashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);
		// 进度框
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("微博发布中...");

		statusImgsAdapter = new WriteStatusGridImgsAdapter(this, imgUris, gv_write_status);
		gv_write_status.setAdapter(statusImgsAdapter);
		gv_write_status.setOnItemClickListener(this);

		iv_image.setOnClickListener(this);
		iv_at.setOnClickListener(this);
		iv_topic.setOnClickListener(this);
		iv_emoji.setOnClickListener(this);
//		iv_add.setOnClickListener(this);

		initEmotion();
	}
	
	/**
	 * 发送微博
	 */
	private void sendStatus() {

		content = et_write_status.getText().toString();

		if(TextUtils.isEmpty(content)&&imgUris.size()==0) {
			dgNoticeToast.showFailure("发布消息不能为空");
			return;
		}

		proDia.show();
		//上传图片
		String imgFilePath = null;
		if(imgUris.size() > 0) {
			for(Uri uri:imgUris){

				imgFilePath = ImageUtils.getImageAbsolutePath19(this, uri);
				sendImage(imgFilePath);
//				sendImage(uri);
			}
		}else{
			Intent intent = new Intent(ACTION_NAME);
			intent.putExtra("user_id",user_id);
			intent.putExtra("content",content);
			intent.putExtra("images","");
			sendBroadcast(intent);
		}
	}

	/**
	 *  初始化表情面板内容
	 */
	private void initEmotion() {
		int screenWidth = DisplayUtils.getScreenWidthPixels(this);
		int spacing = DisplayUtils.dp2px(this, 8);
		
		int itemWidth = (screenWidth - spacing * 8) / 7;
		int gvHeight = itemWidth * 3 + spacing * 4;
		
		List<GridView> gvs = new ArrayList<GridView>();
		List<String> emotionNames = new ArrayList<String>();
		for(String emojiName : EmotionUtils.emojiMap.keySet()) {
			emotionNames.add(emojiName);
			
			if(emotionNames.size() == 20) {
				GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
				gvs.add(gv);
				
				emotionNames = new ArrayList<String>();
			}
		}
		
		if(emotionNames.size() > 0) {
			GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
			gvs.add(gv);
		}
		
		emotionPagerGvAdapter = new EmotionPagerAdapter(gvs);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
		vp_emotion_dashboard.setLayoutParams(params);
		vp_emotion_dashboard.setAdapter(emotionPagerGvAdapter);
	}

	/**
	 * 创建显示表情的GridView
	 */
	private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
		GridView gv = new GridView(this);
		gv.setBackgroundResource(R.color.bg_gray);
		gv.setSelector(R.color.transparent);
		gv.setNumColumns(7);
		gv.setPadding(padding, padding, padding, padding);
		gv.setHorizontalSpacing(padding);
		gv.setVerticalSpacing(padding);
		
		LayoutParams params = new LayoutParams(gvWidth, gvHeight);
		gv.setLayoutParams(params);
		
		EmotionGvAdapter adapter = new EmotionGvAdapter(this, emotionNames, itemWidth);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(this);
		
		return gv;
	}

	/**
	 * 更新图片显示
	 */
	private void updateImgs() {
		if(imgUris.size() > 0) {
			gv_write_status.setVisibility(View.VISIBLE);
			statusImgsAdapter.notifyDataSetChanged();
		} else {
			gv_write_status.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_write_titlebar_back:
			finish();
			break;
		case R.id.tv_write_titlebar_send:
			sendStatus();
			break;
		case R.id.iv_image:
			ImageUtils.showImagePickDialog(this);
			break;
		case R.id.iv_at:
			insertMention();
			break;
		case R.id.iv_topic:
			insertTrendTopic();
			break;
		case R.id.iv_emoji:
			if(ll_emotion_dashboard.getVisibility() == View.VISIBLE) {
				ll_emotion_dashboard.setVisibility(View.GONE);
				iv_emoji.setImageResource(R.drawable.btn_insert_emotion);
				TDevice.showSoftKeyboard(et_write_status);
			} else {
				ll_emotion_dashboard.setVisibility(View.VISIBLE);
				iv_emoji.setImageResource(R.drawable.btn_insert_keyboard);
				TDevice.hideSoftKeyboard(et_write_status);
			}
			et_write_status.requestFocus();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object itemAdapter = parent.getAdapter();
		if(itemAdapter instanceof WriteStatusGridImgsAdapter) {
				if(position == statusImgsAdapter.getCount() - 1) {
					ImageUtils.showImagePickDialog(this);
				}

		} else if(itemAdapter instanceof EmotionGvAdapter) {
			EmotionGvAdapter emotionAdapter = (EmotionGvAdapter) itemAdapter;
			
			if(position == emotionAdapter.getCount() - 1) {
				et_write_status.dispatchKeyEvent(
						new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
			} else {
				String emotionName = emotionAdapter.getItem(position);
				
				int curPosition = et_write_status.getSelectionStart();
				StringBuilder sb = new StringBuilder(et_write_status.getText().toString());
				sb.insert(curPosition, emotionName);
				
				SpannableString weiboContent = SpecialTextUtils.getWeiboContent(
						this, et_write_status, sb.toString());

				if(weiboContent.toString().length()>MAX_LEN){
					et_write_status.setText(weiboContent.subSequence(0,MAX_LEN));
					et_write_status.setSelection(MAX_LEN);
				} else {
					et_write_status.setText(weiboContent);
					et_write_status.setSelection(curPosition + emotionName.length());
				}

			}
			
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case ImageUtils.REQUEST_CODE_FROM_ALBUM:
			if(resultCode == RESULT_CANCELED) {
				return;
			}
			Uri imageUri = data.getData();
			
			imgUris.add(imageUri);
			updateImgs();
			break;
		case ImageUtils.REQUEST_CODE_FROM_CAMERA:
			if(resultCode == RESULT_CANCELED) {
				ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
			} else {
				Uri imageUriCamera = ImageUtils.imageUriFromCamera;
				
				imgUris.add(imageUriCamera);
				updateImgs();
			}
			break;

		default:
			break;
		}
	}


	//上传图片
	private void sendImage(String path) {
		try{

			File img = new File(path);

			if(img.exists()){
				HttpParams params = new HttpParams();
				params.put("photo",img);
				params.put("device", "android");
				params.put("time", System.currentTimeMillis() / 1000 +"");
				kjh.post(APIconfig.API_BASEURL + APIconfig.FILE_UPLOADIAMGE, params, new HttpCallBack() {
					@Override
					public void onPreStar() {
						super.onPreStar();
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						Log.i("uploadImage", t);
						UploadImageResponse uploadImageResponse = Parser.xmlToBean(UploadImageResponse.class, t);
						int code = uploadImageResponse.getCode();
						String msg = uploadImageResponse.getMsg();
						if(code==0){
							String url = uploadImageResponse.getUrl();
							if (images==null){
								images = new StringBuffer();
								images.append(url);
							}else {
								images.append(","+url);
							}
							addImageCount();
							//图片全部上传成功
							if(getImageCount()==imgUris.size()){
								Intent intent = new Intent(ACTION_NAME);
								intent.putExtra("user_id",user_id);
								intent.putExtra("content",content);
								intent.putExtra("images",images.toString());
								sendBroadcast(intent);
							}
						}else{
							proDia.dismiss();
							dgNoticeToast.showFailure("图片上传失败\n" + uploadImageResponse.getMsg());
						}

						Log.i("uploadImage", uploadImageResponse.getUrl());
					}

					@Override
					public void onFailure(int errorNo, String strMsg) {
						super.onFailure(errorNo, strMsg);
						proDia.dismiss();
						dgNoticeToast.showFailure(strMsg);
					}
				});
			} else {
				dgNoticeToast.showFailure("获取图片失败，去重新选择");
			}

		}catch (Exception e){
			e.printStackTrace();
		}

	}

//	private void sendImage(Uri uri) {
//		try{
//
//			File img = DGImageUtils.getCompressedBitmapByQuality(uri,WriteStatusActivity.this);
//
//			if(img!=null&&img.exists()){
//				HttpParams params = new HttpParams();
//				params.put("photo",img);
//				params.put("device", "android");
//				params.put("time", System.currentTimeMillis() / 1000 +"");
//				kjh.post(APIconfig.API_BASEURL + APIconfig.FILE_UPLOADIAMGE, params, new HttpCallBack() {
//					@Override
//					public void onPreStar() {
//						super.onPreStar();
//					}
//
//					@Override
//					public void onSuccess(String t) {
//						super.onSuccess(t);
//						Log.i("uploadImage", t);
//						UploadImageResponse uploadImageResponse = Parser.xmlToBean(UploadImageResponse.class, t);
//						int code = uploadImageResponse.getCode();
//						String msg = uploadImageResponse.getMsg();
//						if(code==0){
//							String url = uploadImageResponse.getUrl();
//							if (images==null){
//								images = new StringBuffer();
//								images.append(url);
//							}else {
//								images.append(","+url);
//							}
//							addImageCount();
//							//图片全部上传成功
//							if(getImageCount()==imgUris.size()){
//								Intent intent = new Intent(ACTION_NAME);
//								intent.putExtra("user_id",user_id);
//								intent.putExtra("content",content);
//								intent.putExtra("images",images.toString());
//								sendBroadcast(intent);
//							}
//						}else{
//							proDia.dismiss();
//							dgNoticeToast.showFailure("图片上传失败\n" + uploadImageResponse.getMsg());
//						}
//					}
//
//					@Override
//					public void onFailure(int errorNo, String strMsg) {
//						super.onFailure(errorNo, strMsg);
//						proDia.dismiss();
//						dgNoticeToast.showFailure(strMsg);
//					}
//				});
//			} else {
//				dgNoticeToast.showFailure("获取图片失败，去重新选择");
//			}
//
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//	}


	// 在光标所在处插入“@用户名”
	private void insertMention() {
		TDevice.showSoftKeyboard(et_write_status);
		// 在光标所在处插入“@用户名”
		int curTextLength = et_write_status.getText().length();
		if (curTextLength >= MAX_LEN)
			return;
		String atme = TEXT_ATME;
		int start, end;
		if ((MAX_LEN - curTextLength) >= atme.length()) {
			start = et_write_status.getSelectionStart() + 1;
			end = start + atme.length() - 2;
		} else {
			int num = MAX_LEN - curTextLength;
			if (num < atme.length()) {
				atme = atme.substring(0, num);
			}
			start = et_write_status.getSelectionStart() + 1;
			end = start + atme.length() - 1;
		}
		if (start > MAX_LEN || end > MAX_LEN) {
			start = MAX_LEN;
			end = MAX_LEN;
		}
		et_write_status.getText().insert(et_write_status.getSelectionStart(), atme);
		et_write_status.setSelection(start, end);// 设置选中文字
	}

	// 在光标所在处插入“#话题名#”
	private void insertTrendTopic() {
		// 在光标所在处插入“#话题名#”
		int curTextLength = et_write_status.getText().length();
		if (curTextLength >= MAX_LEN)
			return;
		String software = TEXT_SOFTWARE;
		int start, end;
		if ((MAX_LEN - curTextLength) >= software.length()) {
			start = et_write_status.getSelectionStart() + 1;
			end = start + software.length() - 2;
		} else {
			int num = MAX_LEN - curTextLength;
			if (num < software.length()) {
				software = software.substring(0, num);
			}
			start = et_write_status.getSelectionStart() + 1;
			end = start + software.length() - 1;
		}
		if (start > MAX_LEN || end > MAX_LEN) {
			start = MAX_LEN;
			end = MAX_LEN;
		}
		et_write_status.getText().insert(et_write_status.getSelectionStart(), software);
		et_write_status.setSelection(start, end);// 设置选中文字
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		//注销广播
		unregisterReceiver(publishMomentReceiver);
	}

	/**
	 * //发布状态广播接受器
	 */
	class PublishMomentReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(ACTION_NAME)){
				Integer user_id = (Integer) intent.getExtras().get("user_id");
				String content = (String) intent.getExtras().get("content");
				String images = (String) intent.getExtras().get("images");
				DGApi.publishMoment(user_id, content, images, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
						AddResponse addResponse = XmlUtils.toBean(AddResponse.class,responseBody);
						if(addResponse.getCode()==0){
							proDia.dismiss();
							dgNoticeToast.showSuccess("状态发布成功！");

							Intent intent = new Intent(MomentsFragment.CITY_UPDATE_NAME);
							sendBroadcast(intent);

							finish();
						}else{
							proDia.dismiss();
							dgNoticeToast.showFailure(addResponse.getMsg()+"\n状态发布失败");
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
						proDia.dismiss();
						dgNoticeToast.showFailure("网络异常");
					}
				});
			}

		}
	}

}
