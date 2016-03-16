package com.dg.app;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.baidu.mapapi.SDKInitializer;
import com.dg.app.api.APIconfig;
import com.dg.app.bean.DGUser;
import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.applib.utils.HXPreferenceUtils;
import com.easemob.chat.EMChat;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.DbOpenHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import com.dg.app.api.ApiHttpClient;
import com.dg.app.base.BaseApplication;
import com.dg.app.bean.Constants;
import com.dg.app.bean.User;
import com.dg.app.cache.DataCleanManager;

import static com.dg.app.AppConfig.KEY_FRITST_START;
import static com.dg.app.AppConfig.KEY_LOAD_IMAGE;
import static com.dg.app.AppConfig.KEY_NIGHT_MODE_SWITCH;
import static com.dg.app.AppConfig.KEY_TWEET_DRAFT;
import static  com.dg.app.AppConfig.KEY_FRITST_CLICK;


import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapConfig;
import org.kymjs.kjframe.utils.KJLoger;

import java.util.Properties;
import java.util.UUID;
import com.dg.app.util.CyptoUtils;
import com.dg.app.util.StringUtils;
import com.dg.app.util.MethodsCompat;
import com.dg.app.util.TLog;
/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * 
 * @author 火蚁 (http://my.oschina.net/LittleDY)
 * @version 1.0
 * @created 2014-04-22
 */
public class AppContext extends BaseApplication {

    public static final int PAGE_SIZE = 20;// 默认分页大小

    private static AppContext instance;

    private int loginUid;

    private int has_dog;

    private boolean login;
    public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        instance = this;
        init();
        initLogin();


//        EMChat.getInstance().init(getApplicationContext());

        hxSDKHelper.onInit(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(AppException
                .getAppExceptionHandler(this));
//        UIHelper.sendBroadcastForNotice(this);
    }

    private void init() {
        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        ApiHttpClient.setHttpClient(client);
        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));

        // Log控制器
        KJLoger.openDebutLog(true);
        TLog.DEBUG = BuildConfig.DEBUG;

        // Bitmap缓存地址
        BitmapConfig.CACHEPATH = "dg/imagecache";
    }

    private void initLogin() {
        DGUser dgUser = getLoginUser();
        if (null != dgUser && dgUser.getUserid() > 0) {
            login = true;
            loginUid = dgUser.getUserid();
            has_dog = dgUser.getHave_dog();
        } else {
            this.cleanLoginInfo();
        }
    }

    /**
     * 获得当前app运行的AppContext
     * 
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }


    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     * 
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取App唯一标识
     * 
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     * 
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 保存登录信息
     * 
     * @param dgUser 用户信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final DGUser dgUser) {
        this.loginUid = dgUser.getUserid();
        this.has_dog = dgUser.getHave_dog();
        this.login = true;
        HXPreferenceUtils.getInstance().setCurrentUserAvatar(APIconfig.IMG_BASEURL+dgUser.getUserlogo());
        HXPreferenceUtils.getInstance().setCurrentUserNick(dgUser.getNickname());

        setProperties(new Properties() {
            {
                setProperty("dgUser.userid", String.valueOf(dgUser.getUserid()));
                setProperty("dgUser.cityid", String.valueOf(dgUser.getCityid()));
                setProperty("dgUser.nickname", dgUser.getNickname());
                setProperty("dgUser.sex", String.valueOf(dgUser.getSex()));
                setProperty("dgUser.userlogo", dgUser.getUserlogo());
                setProperty("dgUser.age", String.valueOf(dgUser.getAge()));
                setProperty("dgUser.career", dgUser.getCareer());
                setProperty("dgUser.dog_sex", String.valueOf(dgUser.getDog_sex()));
                setProperty("dgUser.easemob_id", String.valueOf(dgUser.getEasemob_id()));
                setProperty("dgUser.fans_num", String.valueOf(dgUser.getFans_num()));
                setProperty("dgUser.follow_num", String.valueOf(dgUser.getFollow_num()));
                setProperty("dgUser.have_dog", String.valueOf(dgUser.getHave_dog()));
            }
        });
//        setProperty("lng", "113");
//        setProperty("lat", "28.21");

    }

    /**
     * 更新用户信息
     * 
     * @param dgUser
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final DGUser dgUser) {
        setProperties(new Properties() {
            {
                setProperty("dgUser.userid", String.valueOf(dgUser.getUserid()));
                setProperty("dgUser.cityid", String.valueOf(dgUser.getCityid()));
                setProperty("dgUser.nickname", dgUser.getNickname());
                setProperty("dgUser.sex", String.valueOf(dgUser.getSex()));
                setProperty("dgUser.userlogo", dgUser.getUserlogo());
                setProperty("dgUser.age", String.valueOf(dgUser.getAge()));
                setProperty("dgUser.career", dgUser.getCareer());
                setProperty("dgUser.dog_sex", String.valueOf(dgUser.getDog_sex()));
                setProperty("dgUser.easemob_id", dgUser.getEasemob_id());
                setProperty("dgUser.fans_num", String.valueOf(dgUser.getFans_num()));
                setProperty("dgUser.follow_num", String.valueOf(dgUser.getFollow_num()));
                setProperty("dgUser.have_dog", String.valueOf(dgUser.getHave_dog()));
            }
        });
    }

    /**
     * 获得登录用户的信息
     * 
     * @return
     */
    public DGUser getLoginUser() {
        DGUser dgUser = new DGUser();
        dgUser.setUserid(StringUtils.toInt(getProperty("dgUser.userid"), 0));
        dgUser.setCityid(StringUtils.toInt(getProperty("dgUser.cityid"), 0));
        dgUser.setNickname(getProperty("dgUser.nickname"));
        dgUser.setSex(StringUtils.toInt(getProperty("dgUser.sex"), 0));
        dgUser.setUserlogo(getProperty("dgUser.userlogo"));
        dgUser.setAge(StringUtils.toInt(getProperty("dgUser.age"), 0));
        dgUser.setCareer(getProperty("dgUser.career"));
        dgUser.setDog_sex(StringUtils.toInt(getProperty("dgUser.dog_sex"), 0));
        dgUser.setEasemob_id(getProperty("dgUser.easemob_id"));
        dgUser.setFans_num(StringUtils.toInt(getProperty("dgUser.fans_num"), 0));
        dgUser.setFollow_num(StringUtils.toInt(getProperty("dgUser.follow_num"), 0));
        dgUser.setHave_dog(StringUtils.toInt(getProperty("dgUser.have_dog"), 0));
        return dgUser;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.has_dog = 0;
        this.login = false;
        removeProperty("dgUser.userid", "dgUser.cityid", "dgUser.nickname", "dgUser.sex",
                "dgUser.userlogo", "dgUser.age", "dgUser.career", "dgUser.dog_sex"
                , "dgUser.easemob_id", "dgUser.fans_num", "dgUser.follow_num", "dgUser.have_dog");
    }

    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    public int getHas_dog(){
        return has_dog;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        cleanLoginInfo();
        ApiHttpClient.cleanCookie();
        this.cleanCookie();
        this.login = false;
        this.loginUid = 0;
        this.has_dog = 0;
        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
        sendBroadcast(intent);
    }


    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        DataCleanManager.cleanDatabases(this);
        // 清除数据缓存
        DataCleanManager.cleanInternalCache(this);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanManager.cleanCustomCache(MethodsCompat
                    .getExternalCacheDir(this));
        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
        new KJBitmap().cleanCache();
    }

    public static void setLoadImage(boolean flag) {
        set(KEY_LOAD_IMAGE, flag);
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     * 
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    public static String getTweetDraft() {
        return getPreferences().getString(
                KEY_TWEET_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setTweetDraft(String draft) {
        set(KEY_TWEET_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static String getNoteDraft() {
        return getPreferences().getString(
                AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setNoteDraft(String draft) {
        set(AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static boolean isFristStart() {
        return getPreferences().getBoolean(KEY_FRITST_START, true);
    }

    public static void setFristStart(boolean frist) {
        set(KEY_FRITST_START, frist);
    }
    //当第一次点击时弹出
    public static boolean isFirstClick(){
        return getPreferences().getBoolean(KEY_FRITST_CLICK,true);
    }
    public static void setFirstClick(boolean first)
    {
        set(KEY_FRITST_CLICK,first);
    }
    //夜间模式
    public static boolean getNightModeSwitch() {
        return getPreferences().getBoolean(KEY_NIGHT_MODE_SWITCH, false);
    }

    // 设置夜间模式
    public static void setNightModeSwitch(boolean on) {
        set(KEY_NIGHT_MODE_SWITCH, on);
    }

    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getHXId();
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return hxSDKHelper.getPassword();
    }

    /**
     * 设置用户名
     *
     * @param
     */
    public void setUserName(String username) {
        hxSDKHelper.setHXId(username);
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        hxSDKHelper.setPassword(pwd);
    }

    /**
     * 退出登录,清空数据
     */
    public void logout(final boolean isGCM,final EMCallBack emCallBack) {
        // 先调用sdk logout，在清理app中自己的数据
        hxSDKHelper.logout(isGCM, emCallBack);
    }
}
