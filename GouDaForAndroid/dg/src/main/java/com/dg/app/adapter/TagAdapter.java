package com.dg.app.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.bean.Tag;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class TagAdapter extends BaseAdapter {

	private static int MAX_SIZE = 6;

	private Context context;
	private List<Tag> tags;

	private Map<String,String>  select_tags_map;

	private List<String> select_tags_list;

	private String tag_str;

	private LayoutInflater mInflater = null;

	private DGNoticeToast dgNoticeToast;

	public TagAdapter(Context context, List<Tag> tags, String select_tags) {
		this.context = context;
		this.tags = tags;

		//根据context上下文加载布局，
		this.mInflater = LayoutInflater.from(context);

		dgNoticeToast = new DGNoticeToast(context);

		select_tags_map = new HashMap<>();

		if(!StringUtils.isEmpty(select_tags)){
			String[] select_tags_array =  select_tags.split(",");
			for(String tag_id:select_tags_array){
				select_tags_map.put(tag_id,tag_id);
			}
		}

	}
	
	@Override
	public int getCount() {
		return tags.size();
	}

	@Override
	public Tag getItem(int position) {
		return tags.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = mInflater.inflate(R.layout.list_tag_item, null);
		LinearLayout ll_tag_item = (LinearLayout) view.findViewById(R.id.ll_tag_item);
		TextView tv_tag_content = (TextView) view.findViewById(R.id.tv_tag_content);
		final ImageView iv_tag_choose = (ImageView) view.findViewById(R.id.iv_tag_choose);

		final Tag tag = getItem(position);

		tv_tag_content.setText(tag.getName());

		if(select_tags_map.get(tag.getTag_id())!=null){
			iv_tag_choose.setVisibility(View.VISIBLE);
		}

		ll_tag_item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

					if(select_tags_map.get(tag.getTag_id())!=null){	//已选中
						select_tags_map.remove(tag.getTag_id());
						iv_tag_choose.setVisibility(View.GONE);
					}else{
						if(select_tags_map.size()<MAX_SIZE){		//未选中（指定个数内）
							select_tags_map.put(tag.getTag_id(),tag.getTag_id());
							tag_str = tag.getName();
							iv_tag_choose.setVisibility(View.VISIBLE);
						}else{										//未选中（超过指定个数）
							dgNoticeToast.showFailure("最多只能选择6个标签");
						}
					}

			}
		});

		return view;
	}

	public String getTags(){
		String tags = "";
		int i=0;
		for (Map.Entry<String, String> entry : select_tags_map.entrySet()) {
			if(i==0){
				tags = entry.getValue();
			}else{
				tags = tags+","+entry.getValue();
			}
			i++;
			System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
		}
		return tags;
	}

	public String getTag_str(){
		if(select_tags_map!=null&&select_tags_map.size()>0){
			tag_str = tag_str+"...";
		}
		return tag_str;
	}

}
