package com.dg.app.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.adapter.ViewPageFragmentAdapter;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.Filter;
import com.dg.app.bean.FosterList;
import com.dg.app.bean.WalkList;
import com.dg.app.bean.XQList;
import com.dg.app.fragment.FindNearbyFragment;
import com.dg.app.ui.ChatActivity;
import com.dg.app.ui.DogDetailInfoEditActivity;
import com.dg.app.ui.SelectActivity;
import com.dg.app.ui.SendJYInvite;
import com.dg.app.ui.SendLDInvite;
import com.dg.app.ui.SendXQInvite;
import com.dg.app.ui.dialog.InviteDetailDialog;
import com.dg.app.ui.empty.EmptyLayout;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.util.XmlUtils;
import com.dg.app.widget.DGAlertDialog;
import com.dg.app.widget.PagerSlidingTabStrip;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;

/**
 * 带有导航条的基类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年11月6日 下午4:59:50
 * 
 */
public abstract class BaseMapViewFragment extends BaseFragment

{
    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected EmptyLayout mErrorLayout;
    //动画中心定位图标
//    protected  ImageView markerCenter;
    //底部中心发送消息的图标
    private ImageView sendMessageIcon;
    TextView selectText;
    //地图webview
    BridgeWebView webView;
    //详情Dialog
    private InviteDetailDialog inviteDetailDialog;
    //记录当前处于的状态，遛狗，相亲，寄养等
    private int task;//0遛狗，1相亲，2寄养

    //当前activi
    Activity mainActivity;

    //fragment管理器
    FragmentManager fm;
    FragmentTransaction transaction;
    //头部的左右按钮
    Button left;
    Button right;
    //狗搭发现模块
    FindNearbyFragment findNearbyFragment;
    //未读的消息条数
    TextView unreadCount;

    FrameLayout findview;

    /**
     * 筛选的条件
     * */
    boolean onoff=false;
    int peopleSex=2;
    int dogSex=2;

    String province="湖南省";
    String city="长沙市";
    String dist="岳麓区";

    String ageMax="0";
    String ageMin="100";

    String lgt="112.979353";
    String lat="28.213478";
    //定位相关的类
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    // LocationData locData = null;
    static BDLocation lastLocation = null;
    ProgressDialog progressDialog;


    View pagerline;

    String dog="你没有狗，无法发送邀请！";

    private boolean is_located=false;
    private DGNoticeToast dgNoticeToast;

    private String dog_sex;
    private String dog_logo;
    private ImageView my_location;
    private View rootView;
    private boolean is_set=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if(rootView==null)
        {
            rootView=inflater.inflate(R.layout.base_viewpage_fragment, null);
        }
        ViewGroup parent=(ViewGroup)rootView.getParent();
        if(parent!=null)
            parent.removeView(rootView);
        return  rootView;
//     return inflater.inflate(R.layout.base_viewpage_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(is_set) {

            mTabStrip.setCurrentPosition(task);
            return;
        }
        is_set=true;
        mTabStrip = (PagerSlidingTabStrip) view
                .findViewById(R.id.pager_tabstrip);
        unreadCount=(TextView)view.findViewById(R.id.unread_msg_number);
        int count=getUnreadMsgCountTotal();
        if(count>0)
            unreadCount.setText(count+"");
        else unreadCount.setVisibility(View.INVISIBLE);
        fm=getFragmentManager();

//            markerCenter = (ImageView) view.findViewById(R.id.markerCenter);
            webView = (BridgeWebView) view.findViewById(R.id.test);
            sendMessageIcon = (ImageView) view.findViewById(R.id.WriteMessage);
            findview = (FrameLayout) view.findViewById(R.id.findview);
            pagerline=(View)view.findViewById(R.id.view_pager_line);
            findview.setVisibility(View.GONE);

            left = (Button) view.findViewById(R.id.button_scene);
            right = (Button) view.findViewById(R.id.button_find);
            left.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            right.setBackgroundResource(R.drawable.bg_actionbar_right_normal);
            my_location=(ImageView)view.findViewById(R.id.my_location);
            my_location.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String latlng=lgt+" "+lat;
                   webView.callHandler("functionlocation", latlng + " " + dist, new CallBackFunction() {
                       @Override
                       public void onCallBack(String data) {
                           // Toast.makeText(getActivity(), "向网页传递过滤信息", Toast.LENGTH_LONG).show();
                       }
                   });
               }
           });
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    left.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
                    right.setBackgroundResource(R.drawable.bg_actionbar_right_normal);
                    right.setTextColor(getResources().getColor(R.color.white));
                    left.setTextColor(getResources().getColor(R.color.actionbar_normal));
                    findview.setVisibility(View.GONE);
                    mTabStrip.setVisibility(View.VISIBLE);
//                    markerCenter.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.VISIBLE);
                    sendMessageIcon.setVisibility(View.VISIBLE);
                    pagerline.setVisibility(View.VISIBLE);
                }
            });
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        left.setBackgroundResource(R.drawable.bg_actionbar_left_normal);
                        right.setBackgroundResource(R.drawable.bg_actionbar_right_clicked);
                        left.setTextColor(getResources().getColor(R.color.white));
                        right.setTextColor(getResources().getColor(R.color.actionbar_normal));
                        findview.setVisibility(View.VISIBLE);
                        mTabStrip.setVisibility(View.GONE);
//                        markerCenter.setVisibility(View.GONE);
                        webView.setVisibility(View.GONE);
                        sendMessageIcon.setVisibility(View.GONE);
                        pagerline.setVisibility(View.GONE);

                        FindNearbyFragment findNearbyFragment = new FindNearbyFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("BUNDLE_KEY_CATALOG", 0);
                        bundle.putString("BUNDLE_KEY_DEDVICE", "android");
                        bundle.putInt("BUNDLE_KEY_CITY_ID", 8);
                        findNearbyFragment.setArguments(bundle);
                        FragmentTransaction transaction2 = fm.beginTransaction();
                        transaction2.replace(R.id.findview, findNearbyFragment);
                        transaction2.commit();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });

            selectText = (TextView) view.findViewById(R.id.select_text);
            selectText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    //设置Intent的class属性，跳转到SecondActivity
                    intent.setClass(getActivity(), SelectActivity.class);
                    //启动Activity
                    startActivityForResult(intent, 0);
                }
            });

            ImageView messageImage = (ImageView) view.findViewById(R.id.chat_messages);
            messageImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent intent = new Intent();
                        //设置Intent的class属性，跳转到SecondActivity
                        intent.setClass(getActivity(), ChatActivity.class);
                        //启动Activity
                        startActivity(intent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });



        mTabStrip.setOnClickTabListener(new PagerSlidingTabStrip.OnClickTabListener(){
            @Override
            public void onClickTab(View tab, int index) {
                if(AppContext.isFirstClick())
                {
                        dgNoticeToast.show("您可以移动地图来发现更多图标",Toast.LENGTH_LONG,R.mipmap.ic_dg);
                       AppContext.setFirstClick(false);
                }
                task=index;
                Filter filter=new Filter();
                //筛选开关，人的性别，狗的性别
                filter.setOnoff(onoff);
                filter.setPeopleSex(peopleSex);
                filter.setDogSex(dogSex);
                //省，市，区
                filter.setProvince(province);
                filter.setCity(city);
                filter.setDist(dist);
                //年龄最大，年龄最小
                filter.setAgeMax(ageMax);
                filter.setAgeMin(ageMin);
                //地理位置的经纬度
                filter.setLat("2");
                filter.setLgt("2");
                //设置当前的类型
                filter.setType(task);

                webView.callHandler("functionSenFilter", new Gson().toJson(filter), new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        //Toast.makeText(getActivity(), "向网页传递过滤信息", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        inviteDetailDialog =new InviteDetailDialog(mainActivity);
        //出事话dialog
        //设置对话框的位置
        WindowManager m=mainActivity.getWindowManager();
        Window dialogWindow= inviteDetailDialog.getWindow();
        WindowManager.LayoutParams lp=dialogWindow.getAttributes();
        Display d=m.getDefaultDisplay();
        lp.height=(int)(d.getHeight()*0.8);
        lp.width=(int)(d.getWidth());
        lp.dimAmount=0.0f;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);



        sendMessageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String hava_dog = AppContext.getInstance().getProperty("dgUser.have_dog");
                int int_have_dog = Integer.parseInt(hava_dog);
                if (int_have_dog == 0) {
//                    Toast.makeText(mainActivity,dog,Toast.LENGTH_LONG).show();

                    Dialog dialog;
                    DGAlertDialog.Builder dgDialogBuilder = new DGAlertDialog.Builder(mainActivity);
                    dgDialogBuilder.setMessage("您需要添加狗狗才能发送邀请").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("添加狗狗", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mainActivity, DogDetailInfoEditActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                    dialog = dgDialogBuilder.create();
                    dialog.show();
                    return;
                }
                if (task == 0) {
                    intent = new Intent();
                    intent.setClass(mainActivity, SendLDInvite.class);
                    startActivityForResult(intent, 1);
                }
                if (task == 1) {
                    intent = new Intent();
                    intent.setClass(mainActivity, SendXQInvite.class);
                    startActivityForResult(intent, 1);
                }
                if (task == 2) {
                    intent = new Intent();
                    //设置Intent的class属性，跳转到SecondActivity
                    intent.setClass(mainActivity, SendJYInvite.class);
                    //启动Activity
                    startActivityForResult(intent, 1);
                }


            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

       // webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        //webView.enablecrossdomain41();
        try {

            if (Build.VERSION.SDK_INT >= 16) {


                Class<?> clazz = webView.getSettings().getClass();
                Method method = clazz.getMethod(

                        "setAllowUniversalAccessFromFileURLs", boolean.class);

                if (method != null) {

                    method.invoke(webView.getSettings(), true);

                }

            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        } catch (NoSuchMethodException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e) {

            e.printStackTrace();

        } catch (InvocationTargetException e) {

            e.printStackTrace();

        }
        String dbPath =mainActivity.getApplication().getDir("database", Context.MODE_PRIVATE).getPath();
        webView.getSettings().setDatabasePath(dbPath);
        // 应用可以有缓存
        webView.getSettings().setAppCacheEnabled(true);
        String appCaceDir =mainActivity.getApplication().getDir("cache", Context.MODE_PRIVATE).getPath();
        webView.setWebChromeClient(new WebChromeClient());

        webView.getSettings().setAppCachePath(appCaceDir);

        webView.loadUrl("file:///android_asset/map.html");



        //必须和js同名函数，注册具体执行函数，类似java实现类。
        webView.registerHandler("submitFromWeb", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                String strChinese = "";
                try {
                    strChinese = URLDecoder.decode(data, "utf-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                String str = strChinese;
                // 例如你可以对原始数据进行处理
                //Toast.makeText(mainActivity, str, Toast.LENGTH_SHORT).show();
                markerAnimation();
                function.onCallBack(str + ",Java经过处理后截取了一部分：" + str.substring(0, 5));
            }

        });

        //必须和js同名函数，注册具体执行函数，类似java实现类。
        webView.registerHandler("showDialog", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

//              Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
                String temp[]=data.split(",");
                String idstr=temp[1];
                int id= Integer.parseInt(idstr);
//                Toast.makeText(getActivity(),id+"id",Toast.LENGTH_LONG).show();
                inviteDetailDialog.setInviteid(id);
                inviteDetailDialog.setKind(task);
                ShowDetailLbs(id, inviteDetailDialog);
//                inviteDetailDialog.show();
                inviteDetailDialog.setNegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inviteDetailDialog.dismiss();
                    }
                });

            }

        });
        mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);

        mTabsAdapter = new ViewPageFragmentAdapter(mTabStrip, mainActivity);

        setScreenPageLimit();
        onSetupTabAdapter(mTabsAdapter);


        if(lastLocation==null)
        {
            webView.setVisibility(View.INVISIBLE);
//            markerCenter.setVisibility(View.INVISIBLE);
            selectText.setClickable(false);
            selectText.setVisibility(View.INVISIBLE);
            if(isOpen(mainActivity)) {
                showMapWithLocationClient();
            }
            else {
                dgNoticeToast.showFailure("GPS未打开");
                String lgt_app=AppContext.getInstance().getProperty("lng");
                String lat_app= AppContext.getInstance().getProperty("lat");

                boolean is_null=false;
                if(lgt_app==null||lgt_app.trim().startsWith("4.9E")||lgt_app=="")
                {

                    lgt_app=lgt;
                    is_null=true;
                }
                if(lat_app==null||lat_app==""||lat_app.trim().startsWith("4.9E"))
                {
                    lat_app=lat;
                    is_null=true;
                }
                showMap(Double.parseDouble(lat_app), Double.parseDouble(lgt_app), dist);

            }
        }
        else if(!is_located){

            selectText.setClickable(true);
            selectText.setVisibility(View.VISIBLE);
            lgt=Double.toString(lastLocation.getLongitude());
            lat=Double.toString(lastLocation.getLatitude());
            province=lastLocation.getProvince();
            city=lastLocation.getCity();
            dist=lastLocation.getDistrict();
            showMap(lastLocation.getLatitude(),lastLocation.getLongitude(),dist);
        }

        dog_sex=AppContext.getInstance().getProperty("dgUser.dog_sex");
//        dog_logo=AppContext.getInstance().getPassword();


    }

    public void initView(View view)
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity=this.getActivity();
        dgNoticeToast=new DGNoticeToast(mainActivity);
        is_located=false;
        is_set=false;
        task=0;//初始化遛狗

    }

    protected void setScreenPageLimit() {
    }

     @Override
     public void onSaveInstanceState(Bundle outState) {
     super.onSaveInstanceState(outState);
     }

    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);


    //地图移动时，图标的动画效果
    public void markerAnimation()
    {
        TranslateAnimation mShowAction = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f);
        mShowAction.setDuration(200);
//        markerCenter.startAnimation(mShowAction);

        mShowAction = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(200);
//        markerCenter.startAnimation(mShowAction);
    }

   public  void setUreadCount(int count)
   {
       if (count > 0) {
           unreadCount.setText(String.valueOf(count));
           unreadCount.setVisibility(View.VISIBLE);

       } else {
           unreadCount.setVisibility(View.INVISIBLE);

       }

   }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for(EMConversation conversation:EMChatManager.getInstance().getAllConversations().values()){
            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal-chatroomUnreadMsgCount;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

          switch (resultCode)
          {
              case Activity.RESULT_OK:
                  Bundle bundle=data.getExtras();
                  onoff=bundle.getBoolean("onoff");
                  peopleSex= bundle.getInt("psex");
                  dogSex=bundle.getInt("dsex");

                  province=bundle.getString("province");
                  city=bundle.getString("city");
                  dist=bundle.getString("distribute");

                  ageMax=bundle.getString("agemax");
                  ageMin=bundle.getString("agemin");


                      Filter filter=new Filter();
                      //筛选开关，人的性别，狗的性别
                      filter.setOnoff(onoff);
                      filter.setPeopleSex(peopleSex);
                      filter.setDogSex(dogSex);
                      //省，市，区
                      filter.setProvince(province);
                      filter.setCity(city);
                      filter.setDist(dist);
                      //年龄最大，年龄最小
                      filter.setAgeMax(ageMax);
                      filter.setAgeMin(ageMin);
                      //地理位置的经纬度
                      filter.setLat("2");
                      filter.setLgt("2");
                      //设置当前的类型
                      filter.setType(task);

                      webView.callHandler("functionSenFilter", new Gson().toJson(filter), new CallBackFunction() {
                          @Override
                          public void onCallBack(String data) {
                             // Toast.makeText(getActivity(), "向网页传递过滤信息", Toast.LENGTH_LONG).show();
                          }
                      });
                  break;
              case 11:
                  Bundle foster=data.getExtras();
                  String lng=foster.getString("lng");
                  String lat=foster.getString("lat");
                  String invite_id_foster=foster.getString("invite_id");
                  String str_foster=lng+"/"+lat+"/"+invite_id_foster+"/"+dog_sex;
//                  Toast.makeText(mainActivity,str_foster,Toast.LENGTH_LONG).show();
                  webView.callHandler("functionSenInvite", str_foster, new CallBackFunction() {
                      @Override
                      public void onCallBack(String data) {
                          // Toast.makeText(getActivity(), "向网页传递过滤信息", Toast.LENGTH_LONG).show();
                      }
                  });
                  break;
              case 22:
                  Bundle walk=data.getExtras();
                  String lng1=walk.getString("lng");
                  String lat1=walk.getString("lat");
                  String invite_id_walk=walk.getString("invite_id");
                  String str_walk=lng1+"/"+lat1+"/"+invite_id_walk+"/"+dog_sex;
//                  Toast.makeText(mainActivity,str_walk,Toast.LENGTH_LONG).show();
                  webView.callHandler("functionSenInvite", str_walk, new CallBackFunction() {
                      @Override
                      public void onCallBack(String data) {
                          // Toast.makeText(getActivity(), "向网页传递过滤信息", Toast.LENGTH_LONG).show();
                      }
                  });
                  break;
              case 33:
                  Bundle date=data.getExtras();
                  String lng2=date.getString("lng");
                  String lat2=date.getString("lat");
                  String invite_id_date=date.getString("invite_id");
                  String str_date=lng2+"/"+lat2+"/"+invite_id_date+"/"+dog_sex;
//                  Toast.makeText(mainActivity,str_date,Toast.LENGTH_LONG).show();
                  webView.callHandler("functionSenInvite", str_date, new CallBackFunction() {
                      @Override
                      public void onCallBack(String data) {
                          // Toast.makeText(getActivity(), "向网页传递过滤信息", Toast.LENGTH_LONG).show();
                      }
                  });
                  break;


          }

    }
    //如果存在经纬度，则展示地图
    private void showMap(double latitude, double longtitude,String dist) {
        selectText.setClickable(true);
        selectText.setVisibility(View.VISIBLE);
        String latlng=Double.toString(longtitude)+" "+Double.toString(latitude);
        webView.callHandler("functionlocation", latlng+" "+dist, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                //Toast.makeText(getActivity(), "向网页传递过滤信息", Toast.LENGTH_LONG).show();
            }
        });
        webView.setVisibility(View.VISIBLE);
//        markerCenter.setVisibility(View.VISIBLE);

    }
    //根据地理位置展示地图
    private void showMapWithLocationClient() {
        String str1 = getResources().getString(R.string.Making_sure_your_location);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(str1);

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface arg0) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.d("map", "cancel retrieve location");
                onDestroy();
            }
        });

        progressDialog.show();

        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        // option.setCoorType("bd09ll"); //设置坐标类型
        // Johnson change to use gcj02 coordination. chinese national standard
        // so need to conver to bd09 everytime when draw on baidu map
        option.setCoorType("gcj02");
        option.setScanSpan(60000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
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
            if(is_located)
                return;
            Log.d("map", "On location change received:" + location);
            Log.d("map", "addr:" + location.getAddrStr());
            selectText.setEnabled(true);
            selectText.setClickable(true);
            selectText.setVisibility(View.VISIBLE);
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
            lgt=Double.toString(lastLocation.getLongitude());
            lat=Double.toString(lastLocation.getLatitude());
            System.out.println("lgt-------->"+lgt+">>>>>>>"+"lat------->"+lat);
            province=lastLocation.getProvince();
            city=lastLocation.getCity();
            dist=lastLocation.getDistrict();
            AppContext.getInstance().setProperty("lng",Double.toString(lastLocation.getLongitude()));
            AppContext.getInstance().setProperty("lat", Double.toString(lastLocation.getLatitude()));
            String latlng=Double.toString(lastLocation.getLongitude())+" "+Double.toString(lastLocation.getLatitude());
            webView.callHandler("functionlocation", latlng+" "+dist, new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                   // Toast.makeText(getActivity(), "向网页传递过滤信息", Toast.LENGTH_LONG).show();
                }
            });
            webView.setVisibility(View.VISIBLE);
//            markerCenter.setVisibility(View.VISIBLE);
            is_located=true;
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    @Override
    public void onResume() {
        if (mLocClient != null) {
            mLocClient.start();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (mLocClient != null)
            mLocClient.stop();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (mLocClient != null) {
            mLocClient.stop();
        }
        super.onPause();
//        lastLocation = null;
    }

   //判断对话框
    public void ShowDetailLbs(int invite_id,final InviteDetailDialog inviteDetailDialog)
    {
      int  user_id=Integer.parseInt(AppContext.getInstance().getProperty("dgUser.userid"));
        if(task==0) {
            DGApi.getWD("android", user_id, invite_id, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        WalkList list = parseWalkList(new ByteArrayInputStream(responseBody));
                        if(list.getCode()==0) {
                            inviteDetailDialog.setWalk(list.getList());
                            inviteDetailDialog.show();
                        }else{
                            Toast.makeText(mainActivity,list.getMsg(),Toast.LENGTH_LONG).show();
                        }
//                         System.out.println("list.getCode()----->"+list.getCode()+list.getList().getWalk_id());
//                         if(list.getCode()==400) {
//
//
//                         }
//                         else {
//                             activity.runOnUiThread(new Runnable() {
//                                 @Override
//                                 public void run() {
//                                     Toast.makeText(context,"信息被屏蔽了！",Toast.LENGTH_LONG).show();
//                                     dismiss();
//                                 }
//                             });
//                         }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }else  if(task==1)
        {
            DGApi.getXQ("android", user_id, invite_id, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    XQList list = null;
                    try {
                        list = parseXQList(new ByteArrayInputStream(responseBody));
                     if(list.getCode()==0)
                     {
                         inviteDetailDialog.setDate(list.getList());
                         inviteDetailDialog.show();
                    }else{
                        Toast.makeText(mainActivity, list.getMsg(), Toast.LENGTH_LONG).show();
                    }

                }

                catch(
                Exception e
                )

                {
                    e.printStackTrace();
                }

            }

            @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
        else {
            DGApi.getJY("android", user_id, invite_id, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        FosterList fosterList = parseFosterList(new ByteArrayInputStream(responseBody));
                       if(fosterList.getCode()==0)
                       {
                        inviteDetailDialog.setFoster(fosterList.getList());
                           inviteDetailDialog.show();
                    }else{
                        Toast.makeText(mainActivity, fosterList.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }

                catch(
                Exception e
                )

                {
                    e.printStackTrace();
                }
            }

            @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }


    }
    //解析遛狗的数据
    protected WalkList parseWalkList(InputStream is) throws Exception {
        WalkList list = null;
        try {
            list = XmlUtils.toBean(WalkList.class, is);
        } catch (NullPointerException e) {
            list = new WalkList();
        }
        return list;
    }
//解析相亲的数据
    protected XQList parseXQList(InputStream is) throws Exception {
        XQList list = null;
        try {
            list = XmlUtils.toBean(XQList.class, is);
        } catch (NullPointerException e) {
            list = new XQList();
        }
        return list;
    }
//解析寄养的数据
    protected FosterList parseFosterList(InputStream is) throws Exception {
        FosterList list = null;
        try {
            list = XmlUtils.toBean(FosterList.class, is);
        } catch (NullPointerException e) {
            list = new FosterList();
        }
        return list;
    }
//判断gps是否打开
    public static final boolean isOpen(final Context context)
    {
        LocationManager locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gps;
    }

}
