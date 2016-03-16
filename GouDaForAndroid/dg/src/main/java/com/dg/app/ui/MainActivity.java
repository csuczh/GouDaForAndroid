package com.dg.app.ui;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;

import com.dg.app.AppConfig;
import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.DGUser;
import com.dg.app.bean.Em_User_List;
import com.dg.app.bean.Em_Users;
import com.dg.app.interf.BaseViewInterface;
import com.dg.app.interf.OnTabReselectListener;
import com.dg.app.service.MomentPushIntentService;
import com.dg.app.util.XmlUtils;
import com.dg.app.widget.BadgeView;
import com.dg.app.widget.MyFragmentTabHost;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.DbOpenHelper;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.User;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.common.message.UmengMessageDeviceConfig;
import com.umeng.message.ALIAS_TYPE;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.local.UmengLocalNotification;
import com.umeng.message.local.UmengNotificationBuilder;
import com.umeng.message.tag.TagManager;

import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity
implements  BaseViewInterface,View.OnClickListener,
        View.OnTouchListener,TabHost.OnTabChangeListener,EMEventListener{

    private PushAgent mPushAgent;
    protected static final String TAG = MainActivity.class.getSimpleName();

    //定义双击退出的功能
    private DoubleClickExitHelper mDoubleClickExit;

    //主页底部的导航栏
    @InjectView(android.R.id.tabhost)
    public MyFragmentTabHost mTabHost;

    //显示消息数目的badgeview
    private BadgeView mBvNotice;

    MainActivity mainActivity;

    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;

    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;

    private MyConnectionListener connectionListener=null;

   /*
   * 被用来存储上一屏幕的标题，被用在restoreActionBar
   * */
    private CharSequence mTitile;

    ActionBar actionBar;

    Activity activity;

    private static Map<String, User> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            AppContext.getInstance().Logout();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        initUmeng(this);


        try {
            setContentView(R.layout.activity_main);
            ButterKnife.inject(this);
            actionBar=getSupportActionBar();
            actionBar.hide();
            initView();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.action_bar_title);
        AppManager.getAppManager().addActivity(this);
        mainActivity=this;




        boolean contactSynced = HXSDKHelper.getInstance().isContactsSyncedWithServer();
//        Toast.makeText(mainActivity, "contactSynced:" + contactSynced, Toast.LENGTH_LONG).show();


        try{
        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }

        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);



            init();
            //异步获取当前用户的昵称和头像
//            ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().asyncGetCurrentUserInfo();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }
   static Handler emcat =new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 1:
                    String emlist="";
                    contactList = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
                    Set set=contactList.keySet();
                    for(Iterator iter=set.iterator();iter.hasNext();)
                    {
                        String key=(String)iter.next();
                        emlist+=key+",";
                    }
                       if(emlist=="")
                           break;
                        emlist = emlist.substring(0, emlist.length() - 1);

                        final List<User> users = new ArrayList<>(contactList.size());

                    DGApi.getUsersByEmchat("android", emlist, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            InputStream inputStream = new ByteArrayInputStream(responseBody);
                            System.out.println("inputStream");
                            Em_User_List em_user_list = XmlUtils.toBean(Em_User_List.class, inputStream);
                            List<Em_Users> emUsersList = em_user_list.getList();
                            System.out.println(em_user_list+"emuserlist");
                            for (Em_Users emuser : emUsersList) {
                                User user = new User();
                                user.setUsername(emuser.getEasemob_id());
                                user.setNick(emuser.getNickname());
                                user.setLng(emuser.getPos_lng());
                                user.setSex(emuser.getUser_sex());
                                user.setAvatar(APIconfig.IMG_BASEURL+emuser.getUser_logo());
                                user.setLat(emuser.getPos_lat());
                                user.setUser_id(emuser.getUser_id());
                                contactList.remove(user.getUsername());
                                contactList.put(user.getUsername(),user);
                                users.add(user);

                            }
                            ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(contactList);
                            userDao.saveContactList(users);
                            ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().notifyContactInfosSyncListener(true);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });

                    break;
            }

        }
    };

 static  void asyncFetchContactsFromServer() {
        HXSDKHelper.getInstance().asyncFetchContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> usernames) {
                try {
                    Context context = HXSDKHelper.getInstance().getAppContext();
                    System.out.println("----------------" + usernames.toString());
                    EMLog.d("roster", "contacts size: " + usernames.size());
                    final  Map<String, User> userlist = new HashMap<String, User>();
                    for (String username : usernames) {
                        User user = new User();
                        user.setUsername(username);
                        setUserHearder(username, user);
                        userlist.put(username, user);
                    }
                    // 添加user"申请与通知"
//                    User newFriends = new User();
//                    newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
//                    String strChat = "申请";
//                    newFriends.setNick(strChat);
//                    userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
                    // 存入内存
                          ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
                    // 存入db
                    UserDao dao = new UserDao(context);
                    List<User> users = new ArrayList<User>(userlist.values());
                    dao.saveContactList(users);

                    HXSDKHelper.getInstance().notifyContactsSyncListener(true);

                    if (HXSDKHelper.getInstance().isGroupsSyncedWithServer()) {
                        HXSDKHelper.getInstance().notifyForRecevingEvents();
                    }

//                    ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().asyncFetchContactInfosFromServer(usernames, new EMValueCallBack<List<User>>() {
//
//                        @Override
//                        public void onSuccess(List<User> uList) {
//                            ((DemoHXSDKHelper) HXSDKHelper.getInstance()).updateContactList(uList);
//                            ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().notifyContactInfosSyncListener(true);
//                        }
//
//                        @Override
//                        public void onError(int error, String errorMsg) {
//                        }
//                    });

                    contactList = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
                    System.out.println("contaclist2222:" + contactList);
                    Message message=new Message();
                    message.what=1;
                    emcat.sendMessage(message);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onError(int error, String errorMsg) {
                HXSDKHelper.getInstance().notifyContactsSyncListener(false);
            }

        });
    }

    private static void setUserHearder(String username, User user) {
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                    .toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }


    public void init()
    {
        connectionListener=new MyConnectionListener();
        EMChatManager.getInstance().addConnectionListener(connectionListener);
    }

    /**
     * 连接监听器
     * **/
    public class MyConnectionListener implements EMConnectionListener{
        @Override
        public void onConnected() {
            if(((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList()==null)
            {
                asyncFetchContactsFromServer();
            }

        }

        @Override
        public void onDisconnected(final int error) {
            final String st1 = getResources().getString(R.string.can_not_connect_chat_server_connection);
            final String st2 = getResources().getString(R.string.the_current_network);

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        showAccountRemovedDialog();
                    } else if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        showConflictDialog();
                    }

                }

            });


        }
    }


    private void deleteUserState() {
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("gouda", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.remove("phonenum");
        editor.remove("password");
        editor.commit();//提交修改
    }

    /************************************************************************/
    private void initUmeng(Context context) {

        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setPushCheck(true);    //默认不检查集成配置文件
//		mPushAgent.setLocalNotificationIntervalLimit(false);  //默认本地通知间隔最少是10分钟

        //应用程序启动统计
        //参考集成文档的1.5.1.2
        //http://dev.umeng.com/push/android/integration#1_5_1
        mPushAgent.onAppStart();

        //开启推送并设置注册的回调处理
//        mPushAgent.enable(mRegisterCallback);

        //推送服务的开启
        mPushAgent.enable();

        String device_token= UmengRegistrar.getRegistrationId(context);

//        AppContext.showToast("device_token:\n"+device_token);

        Log.i("device_token",device_token);

        //开启服务，检测自定义消息
        mPushAgent.setPushIntentServiceClass(MomentPushIntentService.class);

        //推送服务的关闭
//        mPushAgent.disable();

        try {
            int user_id = AppContext.getInstance().getLoginUid();
//            AppContext.showToast("user_id:\n" + user_id);
            addAlias(user_id + "", "MAIN");

            DGUser dgUser = AppContext.getInstance().getLoginUser();
            if(dgUser!=null){
//                AppContext.showToast(dgUser.toString());
                addTag("CITY" + dgUser.getCityid());
                addTag("US"+dgUser.getSex());
                addTag("DS"+dgUser.getDog_sex());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        //添加本地定时通知示例
//        addLocalNotification();
    }

    public Handler handler = new Handler();
    //此处是注册的回调处理
    //参考集成文档的1.7.10
    //http://dev.umeng.com/push/android/integration#1_7_10
    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            // TODO Auto-generated method stub
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    updateStatus();
                }
            });
        }
    };

    private void updateStatus() {
        String pkgName = getApplicationContext().getPackageName();
        String info = String.format("enabled:%s  \n isRegistered:%s  \nDeviceToken:%s " +
                        "\nSdkVersion:%s \nAppVersionCode:%s \nAppVersionName:%s",
                mPushAgent.isEnabled(), mPushAgent.isRegistered(),
                mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
                UmengMessageDeviceConfig.getAppVersionCode(this), UmengMessageDeviceConfig.getAppVersionName(this));

//        AppContext.showToast("DeviceToken:\n"+mPushAgent.getRegistrationId());
        Log.i("info", "应用包名：" + pkgName + "\n" + info);
        Log.i("DeviceToken", mPushAgent.getRegistrationId());

        Log.i(TAG, "应用包名：" + pkgName + "\n" + info);
        Log.i(TAG, "updateStatus:" + String.format("enabled:%s  \n isRegistered:%s \n",
                mPushAgent.isEnabled(), mPushAgent.isRegistered()));

    }

    //添加本地定时通知处理示例
    private void addLocalNotification() {
        //初始化通知
        UmengLocalNotification localNotification = new UmengLocalNotification();
        //设置通知开始时间
        //1.开始时间为当前时间往后1小时
//        long time = System.currentTimeMillis() + 60 * 60 * 1000;
        long time = System.currentTimeMillis() + 10*1000;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d=new Date(time);
        String t=format.format(d);
        localNotification.setDateTime(t);
        //2.开始时间为特殊节日，只需要设置开始的年，如果设置了特殊节日，则1设置的时间无效
        //开始为当前年，节日为春节
        /*
        Calendar c = Calendar.getInstance();
        localNotification.setYear(c.get(Calendar.YEAR));
        localNotification.setHour(12);
        localNotification.setMinute(12);
        localNotification.setSecond(12);
        localNotification.setSpecialDay(UmengLocalNotification.CHINESE_NEW_YEAR);
        */
        //设置重复次数，默认是1
        localNotification.setRepeatingNum(100);
        //设置重复间隔，默认是1
        localNotification.setRepeatingInterval(10);
        //设置重复单位，默认是天
        localNotification.setRepeatingUnit(UmengLocalNotification.REPEATING_UNIT_SECOND);

        //初始化通知样式
        UmengNotificationBuilder builder = new UmengNotificationBuilder();
        //设置小图标
        builder.setSmallIconDrawable("ic_launcher");
        //设置大图标
        builder.setLargeIconDrawable("ic_launcher");
        //设置自动清除
        builder.setFlags(Notification.FLAG_AUTO_CANCEL);

        localNotification.setNotificationBuilder(builder);

        mPushAgent.addLocalNotification(localNotification);
    }

    // sample code to add alias for the device / user
    private void addAlias(String alias,String aliasType) {
        if (TextUtils.isEmpty(alias))
        {
            return;
        }
        if (TextUtils.isEmpty(aliasType))
        {
            return;
        }
        if (!mPushAgent.isRegistered())
        {
            return;
        }
        new AddAliasTask(alias,aliasType).execute();
    }

    class AddAliasTask extends AsyncTask<Void, Void, Boolean> {

        String alias;
        String aliasType;

        public AddAliasTask(String aliasString,String aliasTypeString) {
            // TODO Auto-generated constructor stub
            this.alias = aliasString;
            this.aliasType = aliasTypeString;
        }

        protected Boolean doInBackground(Void... params) {
            try {
                return mPushAgent.addAlias(alias, aliasType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (Boolean.TRUE.equals(result))
                Log.i(TAG, "alias was set successfully.");
        }

    }


    // sample code to add tags for the device / user
    private void addTag(String tag) {
        if (TextUtils.isEmpty(tag))
        {
//            toast("请先输入Tag");
            return;
        }
        if (!mPushAgent.isRegistered())
        {
//            toast("抱歉，还未注册");
            return;
        }

        new AddTagTask(tag).execute();
    }

    class AddTagTask extends AsyncTask<Void, Void, String>{

        String tagString;
        String[] tags;
        public AddTagTask(String tag) {
            // TODO Auto-generated constructor stub
            tagString = tag;
            tags = tagString.split(",");
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                TagManager.Result result = mPushAgent.getTagManager().add(tags);
                Log.d(TAG, result.toString());
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Fail";
        }

        @Override
        protected void onPostExecute(String result) {
//            updateInfo("Add Tag:\n" + result);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            int user_id = AppContext.getInstance().getLoginUid();
//            AppContext.showToast("user_id:\n" + user_id);
            addAlias(user_id + "", "MAIN");

            DGUser dgUser = AppContext.getInstance().getLoginUser();
            if(dgUser!=null){
//                AppContext.showToast(dgUser.toString());
                addTag("CITY" + dgUser.getCityid());
                addTag("US"+dgUser.getSex());
                addTag("DS"+dgUser.getDog_sex());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /************************************************************************/

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }


    }
    private InviteMessgeDao inviteMessgeDao;
    private static UserDao userDao;
    @Override
    protected void onResume() {
        super.onResume();

        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();

            EMChatManager.getInstance().activityResumed();
        }



        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(this,
                new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage ,EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged});
    }

 //初始化视图
    @Override
    public void initView() {

      mDoubleClickExit=new DoubleClickExitHelper(this);


        //获得当前的标题
        mTitile=getTitle();

        //启动底部的tabhost，并加载
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        if(Build.VERSION.SDK_INT>10)
        {
            mTabHost.getTabWidget().setShowDividers(0);
        }
        initTabs();
        //设置Tabhost的显示样式
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);

 }
    @Override
    public void initData() {

    }

    private void initTabs()
    {
        MainTab[] tabs=MainTab.values();
        final int size=tabs.length;
        for(int i=0;i<size;i++)
        {
            MainTab mainTab=tabs[i];
            TabHost.TabSpec tab=mTabHost.newTabSpec(getString(mainTab.getResName()));
            View indicator= LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_indicator, null);

            TextView title=(TextView)indicator.findViewById(R.id.tab_title);
            Drawable drawable=this.getResources().getDrawable(mainTab.getResIcon());
            title.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);

            tab.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });

            mTabHost.addTab(tab, mainTab.getClz(),null);
            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }



    @Override
    public void onTabChanged(String tabId) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        boolean consumed = false;

        // use getTabHost().getCurrentTabView to decide if the current tab is
        // touched again
        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            if (v.equals(mTabHost.getCurrentTabView())) {
                // use getTabHost().getCurrentView() to get a handle to the view
                // which is displayed in the tab - and to get this views context
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment != null
                        && currentFragment instanceof OnTabReselectListener) {
                    OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
//                    listener.onTabReselect();
                    consumed = true;
                }
            }
        }
        return consumed;
    }
    private Fragment getCurrentFragment()
    {
        return getSupportFragmentManager().findFragmentByTag(mTabHost.getCurrentTabTag());
    }
//
//    //监听返回键，是否退出程序
//
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            //是否退出应用
            if(AppContext.get(AppConfig.KEY_DOUBLE_CLICK_EXIT, true))
            {
                return mDoubleClickExit.onKeyDown(keyCode,event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        // 提示有新消息
//        HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(null);


    }

    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        // 未读数加1
        User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList().get(Constant.NEW_FRIENDS_USERNAME);
        if (user.getUnreadMsgCount() == 0)
            user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHXSDKHelper.getInstance().logout(true, null);
        String st5 = "账号移除";
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage("显示帐号已经被移除");
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        AppContext.getInstance().cleanLoginInfo();
                        deleteUserState();
                        Intent intent = new Intent(mainActivity, LoginActivity.class);
                        mainActivity.startActivity(intent);
                        mainActivity.finish();

                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e("remove", "---------color userRemovedBuilder error" + e.getMessage());
            }
        }
    }

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHXSDKHelper.getInstance().logout(true, null);

        String st = "账号冲突";
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage("显示帐号在其他设备登陆");
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                        conflictBuilder = null;
                        AppContext.getInstance().cleanLoginInfo();
                        deleteUserState();
//                        AppManager.getAppManager().finishActivity(MainActivity.class);
                        Intent intent = new Intent(mainActivity, LoginActivity.class);
                        mainActivity.startActivity(intent);
                        mainActivity.finish();
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e("conflict", "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }
    /**
     * 监听事件
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: // 普通消息
            {
                EMMessage message = (EMMessage) event.getData();
                System.out.print("main");
                // 提示新消息
                HXSDKHelper.getInstance().getNotifier().onNewMsg(message);

                refreshUI();
                break;
            }

            case EventOfflineMessage: {
                refreshUI();
                break;
            }
            case EventConversationListChanged: {
                refreshUI();
                break;
            }

            default:
                break;
        }
    }

    private void refreshUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                updateUnreadLabel();

            }
        });
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

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
//        Toast.makeText(mainActivity,count+"",Toast.LENGTH_LONG).show();
        if(mTabHost.getCurrentTab()==0) {
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment != null
                    && currentFragment instanceof OnTabReselectListener) {
                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
                listener.onTabReselect(count);
            }
        }

    }
    @Override
    protected void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

}
