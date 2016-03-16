package com.dg.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.bean.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagsAdapter extends BaseAdapter {

	private static int MAX_SIZE = 6;

	private Context context;
	private List<Tag> tags;

	private Map<String,String>  select_tags_map;

	private List<String> select_tags_list;

	private String tag_str;

	public TagsAdapter(Context context, List<Tag> tags,String select_tags) {
		this.context = context;
		this.tags = tags;

		select_tags_map = new HashMap<>();

		select_tags_list = new ArrayList<>();

		String[] select_tags_array =  select_tags.split(",");
		for(String tag_id:select_tags_array){
			select_tags_map.put(tag_id,tag_id);
			select_tags_list.add(tag_id);
		}

//		for (Map.Entry<String, String> entry : select_tags_map.entrySet()) {
//			AppContext.showToast("key= " + entry.getKey() + " and value= " + entry.getValue());
//			System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//		}

		AppContext.showToast("select_tags_list:\n"+select_tags_list.toString());

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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.list_tag_item, null);
			holder.ll_tag_item = (LinearLayout) convertView.findViewById(R.id.ll_tag_item);
			holder.tv_tag_content = (TextView) convertView.findViewById(R.id.tv_tag_content);
			holder.iv_tag_choose = (ImageView) convertView.findViewById(R.id.iv_tag_choose);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Tag tag = getItem(position);

		holder.tv_tag_content.setText(tag.getName());

//		if(select_tags_map.get(tag.getTag_id())!=null){
//			holder.iv_tag_choose.setVisibility(View.VISIBLE);
//		}
//		boolean is_selected = false;
		for(String tag_id:select_tags_list){
			if(tag_id.equals(tag.getTag_id())){
				holder.iv_tag_choose.setVisibility(View.VISIBLE);
				break;
			}
		}


		holder.ll_tag_item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

//					if(select_tags_map.get(tag.getTag_id())!=null){	//已选中
//						select_tags_map.remove(tag.getTag_id());
//						holder.iv_tag_choose.setVisibility(View.GONE);
//					}else{
//						if(select_tags_map.size()<=MAX_SIZE){		//未选中（指定个数内）
//							select_tags_map.put(tag.getTag_id(),tag.getTag_id());
//							tag_str = tag.getName();
//							holder.iv_tag_choose.setVisibility(View.VISIBLE);
//						}else{										//未选中（超过指定个数）
//
//						}
//					}

			}
		});

		return convertView;
	}

	public final class ViewHolder {
		public LinearLayout ll_tag_item;

		public TextView tv_tag_content;

		public ImageView iv_tag_choose;
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
