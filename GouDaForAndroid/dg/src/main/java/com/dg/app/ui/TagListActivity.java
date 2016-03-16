package com.dg.app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.adapter.TagAdapter;
import com.dg.app.adapter.TagsAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.Tag;
import com.dg.app.bean.Tags;
import com.dg.app.bean.TagsList;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.message.PushAgent;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class TagListActivity extends Activity implements OnClickListener{


	public static String TAGS = "TAGS";

	public static String TAGS_STR = "TAGS_STR";

	//顶部标题栏
	private LinearLayout titlebar_tag_edit;
	private ImageView iv_tag_edit_back;
	private TextView tv_tag_complete;

	private ListView lv_tag;

//	private TagsAdapter tagsAdapter;

	private TagAdapter tagsAdapter;

	private List<Tag> tag_list = new ArrayList<>();

	private String tags;

	private String tag_str;

	private String type;

	private int tag_type;

	//提示组件
	private DGNoticeToast dgNoticeToast;
	private ProgressDialog proDia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_select);
		PushAgent.getInstance(this).onAppStart();
		AppManager.getAppManager().addActivity(this);

		initNoticeWidget(this);

		initView();

		initData();
	}

	/**
	 * 初始化提示组件
	 * @param context
	 */
	private void initNoticeWidget(Context context) {
		proDia = new ProgressDialog(context);
		dgNoticeToast = new DGNoticeToast(context);
	}

	private void initData() {
		try{
			AppContext.showToast("type:\n"+type);
			if(MyDetailInfoEditActivity.USER_TYPE.equals(type)){
				tag_type=0;
			}else if(DogDetailInfoEditActivity.DOG_TYPE.equals(type)){
				tag_type=1;
			}

			if(tag_type == 3){
				AppContext.showToast("tag_type == 3");
				finish();
				return;
			}

			DGApi.getTags(0, tag_type, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					TagsList tagsList = XmlUtils.toBean(TagsList.class, responseBody);
					if (tagsList.getCode() == 0) {
						tag_list.addAll(tagsList.getTags());
						if(tagsAdapter!=null){
							tagsAdapter.notifyDataSetChanged();
						}
					} else {
						tagsList.getMsg();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

				}
			});
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	private void initView() {
		try{

			// 标题栏
			titlebar_tag_edit = (LinearLayout) findViewById(R.id.titlebar_tag_edit);
			iv_tag_edit_back = (ImageView)titlebar_tag_edit.findViewById(R.id.iv_tag_edit_back);
			iv_tag_edit_back.setOnClickListener(this);

			tv_tag_complete = (TextView)titlebar_tag_edit.findViewById(R.id.tv_tag_complete);
			tv_tag_complete.setOnClickListener(this);

			lv_tag = (ListView) findViewById(R.id.lv_tag);

			tags = getIntent().getStringExtra("tags");
			type = getIntent().getStringExtra("type");

			if(type==null||"".equals(type)){
				dgNoticeToast.showFailure("加载标签失败");
				finish();
				return;
			}
		//	tagsAdapter = new TagsAdapter(this,tag_list,tags);
			tagsAdapter = new TagAdapter(this, tag_list,tags);
			lv_tag.setAdapter(tagsAdapter);

		}catch (Exception e){
			e.printStackTrace();
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_tag_edit_back:
			finish();
			break;
		case R.id.tv_tag_complete:
			SelectTags();
			break;
		}
	}

	private void SelectTags() {
		try{
			tags = tagsAdapter.getTags();
			tag_str = tagsAdapter.getTag_str();
			if(tags!=null&&tag_str!=null&&!"".equals(tags)&&!"".equals(tag_str)){
				Intent intent=new Intent();
				intent.putExtra(TAGS,tags);
				intent.putExtra(TAGS_STR,tag_str);
				setResult(RESULT_OK, intent);
			}
			finish();
		}catch (Exception e){
			e.printStackTrace();
		}

	}


}
