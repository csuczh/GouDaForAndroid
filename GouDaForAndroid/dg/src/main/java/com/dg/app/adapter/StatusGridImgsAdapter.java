package com.dg.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.bean.PicUrls;
import com.dg.app.util.DGImageUtils;

import org.kymjs.kjframe.KJBitmap;

/**
 * 状态网格图片布局适配器
 */
public class StatusGridImgsAdapter extends BaseAdapter {

	private Context context;
	private List<String> datas;	//图片链表
	private final KJBitmap kjb = new KJBitmap(); //使用kjbitmap

	public StatusGridImgsAdapter(Context context, List<String> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public String getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.grid_item_image, null);
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		GridView gv = (GridView) parent;
		int horizontalSpacing = gv.getHorizontalSpacing();
		int numColumns = gv.getNumColumns();
		int itemWidth = (gv.getWidth() - (numColumns-1) * horizontalSpacing
				- gv.getPaddingLeft() - gv.getPaddingRight()) / numColumns;

		LayoutParams params = new LayoutParams(itemWidth, itemWidth);
		holder.iv_image.setLayoutParams(params);

		String url = getItem(position);

		url = DGImageUtils.toSmallImageURL(url);
//		kjb.display(holder.iv_image, APIconfig.IMG_BASEURL+url);
//		kjb.display(holder.iv_image, APIconfig.IMG_BASEURL + url, itemWidth,itemWidth, R.mipmap.default_image);
		kjb.display(holder.iv_image, APIconfig.IMG_BASEURL + url, itemWidth,itemWidth, R.mipmap.default_image);
		return convertView;
	}

	public static class ViewHolder {
		public ImageView iv_image;
	}

}
