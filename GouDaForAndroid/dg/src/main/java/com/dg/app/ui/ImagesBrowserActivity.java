package com.dg.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.bean.Moment;
import com.dg.app.ui.widget.HackyViewPager;
import com.umeng.message.PushAgent;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImagesBrowserActivity extends Activity implements OnClickListener {

	private ViewPager vp_image_brower;
	private TextView tv_image_index;
	private Button btn_save;
	private Button btn_original_image;

	private Moment status;

	private int position;

	private List<String> imgUrls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		AppManager.getAppManager().addActivity(this);

		setContentView(R.layout.activity_images_brower);

		initData();
		initView();
		setData();
	}

	private void initData() {

		status = (Moment) getIntent().getSerializableExtra("status");
		position = getIntent().getIntExtra("position", 0);

		imgUrls =new ArrayList<>();//获取状态图片列表
		//TODO  API有待需改
		String imgs = status.getImages();

		if(imgs!=null && imgs.contains(",")){
			String[] imgs_array = imgs.split(",");
			for(String img:imgs_array){
				imgUrls.add(img);
			}
		}else if(imgs.trim()!=""){
			imgUrls.add(imgs);
		}
//		Toast.makeText(ImageBrowserActivity.this,"imgUrls:\n"+imgUrls.toString(),Toast.LENGTH_SHORT).show();
	}

	private void initView() {
		vp_image_brower = (HackyViewPager) findViewById(R.id.vp_image_brower);

		vp_image_brower.setAdapter(new ImagesPagerAdapter(ImagesBrowserActivity.this,imgUrls));

		tv_image_index = (TextView) findViewById(R.id.tv_image_index);

		btn_save = (Button) findViewById(R.id.btn_save);
		btn_original_image = (Button) findViewById(R.id.btn_original_image);


		btn_original_image.setVisibility(View.GONE);
		btn_save.setVisibility(View.GONE);


		btn_save.setOnClickListener(this);
		btn_original_image.setOnClickListener(this);
	}
	
	private void setData() {

//		adapter = new PhotoViewAdapter(this,imgUrls);

//		vp_image_brower.setAdapter(adapter);

//		initViewPager();

		
		final int size = imgUrls.size();
		int initPosition = Integer.MAX_VALUE / 2 / size * size + position;
		
		if(size > 1) {
			tv_image_index.setVisibility(View.VISIBLE);
			tv_image_index.setText((position+1) + "/" + size);
		} else {
			tv_image_index.setVisibility(View.GONE);
		}
		
		vp_image_brower.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int index = arg0 % size;
				tv_image_index.setText((index + 1) + "/" + size);

				//TODO  显示原图
//				String pic = adapter.getPic(arg0);
//				btn_original_image.setVisibility(pic.isShowOriImag() ?
//						View.GONE : View.VISIBLE);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
				
		vp_image_brower.setCurrentItem(position);
	}

	@Override
	public void onClick(View v) {
//		String picUrl = adapter.getPic(vp_image_brower.getCurrentItem());
		
		switch (v.getId()) {
		case R.id.btn_save:
//			Bitmap bitmap = adapter.getBitmap(vp_image_brower.getCurrentItem());

			//TODO  显示原图
//			boolean showOriImag = picUrl.isShowOriImag();
//			String fileName = "img-" + (showOriImag?"ori-" : "mid-") + picUrl.getImageId();
			
//			String title = fileName.substring(0, fileName.lastIndexOf("."));

//			String title = picUrl.substring(0, picUrl.lastIndexOf("."));
//
//			String insertImage = MediaStore.Images.Media.insertImage(
//					getContentResolver(), bitmap, title, "BoredgImage");
//			if(insertImage == null) {
//				Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show();
//			} else {
//				Toast.makeText(this, "图片保存成功", Toast.LENGTH_SHORT).show();
//			}
			
//			try {
//				ImageUtils.saveFile(this, bitmap, fileName);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			
			break;
		case R.id.btn_original_image:
			//TODO  显示原图
//			picUrl.setShowOriImag(true);
//			adapter.notifyDataSetChanged();
			
			btn_original_image.setVisibility(View.GONE);
			break;
		}
	}


	static class ImagesPagerAdapter extends PagerAdapter {

		private List<String> imgUrls;

		private Activity context;

		private KJBitmap kjb;

		public ImagesPagerAdapter(Activity context,List<String> imgUrls){
			this.context = context;
			this.imgUrls = imgUrls;
			kjb = new KJBitmap();
		}

		@Override
		public int getCount() {
			return imgUrls.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			int index = position % imgUrls.size();
			LayoutInflater mInflater = LayoutInflater.from(context);

			View view = mInflater.inflate(R.layout.activity_image, null);
			final PhotoView photoView = (PhotoView) view.findViewById(R.id.images);
			final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
			progressBar.setVisibility(View.VISIBLE);
			photoView.setVisibility(View.GONE);
//			String url = picUrl.isShowOriImag() ? picUrl.getOriginal_pic() : picUrl.getBmiddle_pic();

//			String small_img = DGImageUtils.toSmallImageURL(imgUrls.get(index));
			String img = imgUrls.get(index);
			kjb.display(photoView, APIconfig.IMG_BASEURL + img, new BitmapCallBack() {

				@Override
				public void onPreLoad() {
					super.onPreLoad();
					progressBar.setVisibility(View.VISIBLE);
					photoView.setVisibility(View.GONE);
				}

				@Override
				public void onFailure(Exception e) {
					super.onFailure(e);
					progressBar.setVisibility(View.GONE);
					photoView.setImageResource(R.mipmap.pagefailed_bg);
					photoView.setVisibility(View.VISIBLE);
				}

				@Override
				public void onFinish() {
					super.onFinish();
					progressBar.setVisibility(View.GONE);
					photoView.setVisibility(View.VISIBLE);
				}
			});

			photoView.setOnFinishListener(new PhotoViewAttacher.OnPhotoTapListener() {
				@Override
				public void onPhotoTap(View view, float x, float y) {
					context.finish();
				}
			});
			// Now just add PhotoView to ViewPager and return it
			container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		public String getPic(int position) {
			return imgUrls.get(position % imgUrls.size());
		}
	}

}
