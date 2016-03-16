/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dg.app.ui;


import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.umeng.message.PushAgent;

import java.io.IOException;
import java.net.URLDecoder;


public class BaiduMapActivity extends AppCompatActivity {

	private final static String TAG = "map";

	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	Button sendButton = null;

	EditText indexText = null;
	int index = 0;
	// LocationData locData = null;
	static BDLocation lastLocation = null;
	public static BaiduMapActivity instance = null;

	private BridgeWebView mapView;
	private ImageView locationMarker;

	String latitude;
    String longitude;
	String address;

	boolean send=true;

	ProgressDialog progressDialog;
	private LocationMode mCurrentMode;

	private Activity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		try {
			instance = this;
			setContentView(R.layout.activity_baidumap);
			ActionBar actionBar = getSupportActionBar();
			actionBar.hide();
			activity = this;
			mapView = (BridgeWebView) this.findViewById(R.id.location);
			locationMarker = (ImageView) this.findViewById(R.id.location_marker);
			sendButton = (Button) findViewById(R.id.btn_location_send);


			mapView.getSettings().setDomStorageEnabled(true);
			mapView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			mapView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
			mapView.getSettings().setSupportZoom(true);
			mapView.getSettings().setBuiltInZoomControls(true);
			mapView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			mapView.getSettings().setDatabaseEnabled(true);
			mapView.getSettings().setDefaultTextEncodingName("utf-8");

			String dbPath = this.getApplication().getDir("database", Context.MODE_PRIVATE).getPath();
			mapView.getSettings().setDatabasePath(dbPath);
			// 应用可以有缓存
			mapView.getSettings().setAppCacheEnabled(true);
			String appCaceDir = this.getApplication().getDir("cache", Context.MODE_PRIVATE).getPath();
			mapView.setWebChromeClient(new WebChromeClient());
			mapView.getSettings().setAppCachePath(appCaceDir);
			mapView.loadUrl("file:///android_asset/location.html");


			//必须和js同名函数，注册具体执行函数，类似java实现类。
			mapView.registerHandler("submitFromWeb", new BridgeHandler() {

				@Override
				public void handler(String data, CallBackFunction function) {
					String strChinese = "";
					try {
						strChinese = URLDecoder.decode(data, "utf-8");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					String str = strChinese;
					String messages[] = str.split(",");
					address = messages[1];
					String lnglat[]=messages[2].split(" ");
					longitude=lnglat[0];
					latitude=lnglat[1];
					// 例如你可以对原始数据进行处理
					markerAnimation();
					function.onCallBack(str + ",Java经过处理后截取了一部分：" + str.substring(0, 5));
				}

			});

			if(lastLocation==null)
			{
				mapView.setVisibility(View.INVISIBLE);
				locationMarker.setVisibility(View.INVISIBLE);
				showMapWithLocationClient();
			}
			else {
				longitude=Double.toString(lastLocation.getLongitude());
				latitude=Double.toString(lastLocation.getLatitude());
				address=lastLocation.getProvince()+" "+lastLocation.getCity()+" "+lastLocation.getDistrict();

				showMap(lastLocation.getLongitude(),lastLocation.getLatitude());
			}

		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		AppManager.getAppManager().addActivity(this);


	}
	//如果存在经纬度，则展示地图
	private void showMap(double latitude, double longtitude) {
	   sendButton.setVisibility(View.VISIBLE);
		String latlng=Double.toString(longtitude)+" "+Double.toString(latitude);
		mapView.callHandler("functionInJs", latlng, new CallBackFunction() {
			@Override
			public void onCallBack(String data) {
				//Toast.makeText(activity, "向网页传递过滤信息", Toast.LENGTH_LONG).show();
			}
		});
		mapView.setVisibility(View.VISIBLE);
		locationMarker.setVisibility(View.VISIBLE);

	}
	//根据地理位置展示地图
	private void showMapWithLocationClient() {
		String str1 = getResources().getString(R.string.Making_sure_your_location);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(str1);

		progressDialog.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface arg0) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				Log.d("map", "cancel retrieve location");
				finish();
			}
		});

		progressDialog.show();

		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		// option.setCoorType("bd09ll"); //设置坐标类型
		// Johnson change to use gcj02 coordination. chinese national standard
		// so need to conver to bd09 everytime when draw on baidu map
		option.setCoorType("gcj02");
		option.setScanSpan(30000);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
	}

	//地图移动时，图标的动画效果
	public void markerAnimation()
	{
		TranslateAnimation mShowAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f);
		mShowAction.setDuration(200);
		locationMarker.startAnimation(mShowAction);

		mShowAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(200);
		locationMarker.startAnimation(mShowAction);
	}





	@Override
	protected void onPause() {
		if (mLocClient != null) {
			mLocClient.stop();
		}
		super.onPause();
		lastLocation = null;
	}

	@Override
	protected void onResume() {
		if (mLocClient != null) {
			mLocClient.start();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mLocClient != null)
			mLocClient.stop();
		super.onDestroy();
	}

	/**
	 * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			Log.d("map", "On location change received:" + location);
			Log.d("map", "addr:" + location.getAddrStr());
			sendButton.setEnabled(true);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

			if (lastLocation != null) {
				if (lastLocation.getLatitude() == location.getLatitude() && lastLocation.getLongitude() == location.getLongitude()) {
					Log.d("map", "same location, skip refresh");
					return;
				}
			}
			lastLocation = location;
			longitude=Double.toString(lastLocation.getLongitude());
			latitude=Double.toString(lastLocation.getLatitude());
			address=lastLocation.getProvince()+" "+lastLocation.getCity()+" "+lastLocation.getDistrict();
			String latlng=Double.toString(lastLocation.getLongitude())+" "+Double.toString(lastLocation.getLatitude());
			mapView.callHandler("functionInJs", latlng, new CallBackFunction() {
				@Override
				public void onCallBack(String data) {
					//Toast.makeText(activity, "向网页传递过滤信息", Toast.LENGTH_LONG).show();
				}
			});
			mapView.setVisibility(View.VISIBLE);
			locationMarker.setVisibility(View.VISIBLE);
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	public static final boolean isOpen(final  Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network)
		{
			return true;
		}
		return false;
	}



public static final void openGps(Context context)
{
	  Intent GpsIntent=new Intent();
	  GpsIntent.setClassName("com.android.settings","com.android.settings.widget.SettingsAppWidgetProvider");
	 GpsIntent.addCategory("android.intent.category.ALTERNATIVE");
	GpsIntent.setData(Uri.parse("custom:3"));
	try {
		PendingIntent.getBroadcast(context,0,GpsIntent,0).send();
	} catch (PendingIntent.CanceledException e) {
		e.printStackTrace();
	}


}

	public void back(View v) {
		finish();
	}

	public void sendLocation(View view) {
		Intent intent = this.getIntent();
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		intent.putExtra("address", address);
		this.setResult(RESULT_OK, intent);
		finish();
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}

}
