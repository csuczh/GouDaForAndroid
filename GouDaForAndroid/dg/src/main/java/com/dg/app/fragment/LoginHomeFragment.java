package com.dg.app.fragment;

import android.app.Activity;import android.app.Dialog;import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.DGUser;
import com.dg.app.ui.DogIntroActivity;
import com.dg.app.ui.ForgetPwdActivity;
import com.dg.app.ui.MainActivity;
import com.dg.app.ui.UserInfoActivity;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.util.MD5;
import com.dg.app.util.StringUtils;
import com.dg.app.util.TDevice;
import com.dg.app.util.XmlUtils;
import com.dg.app.widget.DGAlertDialog;
import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.jivesoftware.smackx.muc.UserStatusListener;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class LoginHomeFragment extends android.support.v4.app.Fragment implements View.OnClickListener
{

    private Context context;
    private Activity activity;
    private KJBitmap kjb;

    private RoundImageView riv_login_avatar;

    private EditText et_login_phonenumber;
    private EditText et_login_pwd;

    private String mUserName = "";
    private String mPassword = "";

    private TextView tv_forgetpwd;

    private TextView tv_login_btn;
    private TextView tv_regist_btn;

    private String phonenum;
    private String password;
    private String user_logo;

    //提示组件
    private DGNoticeToast dgNoticeToast;
    private ProgressDialog proDia;

    private Dialog dialog;
    private DGAlertDialog.Builder dgDialogBuilder;


    public static final String BROADCAST_ACTION="com.example.corn";

    //同步说
    public static final Object lock1=new Object();

    public void setContext(Context context,Activity activity) {
        this.context = context;
        this.activity=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_tweet, container, false);
        View root = View.inflate(getActivity(),
                R.layout.fragment_login_home, null);
        try {
        initNoticeWidget(getActivity());

        initView(root);

            //获取保存的用户状态
            initData();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return root;
    }

    /**
     * 初始化提示组件
     * @param context
     */
    private void initNoticeWidget(Context context) {
        proDia = new ProgressDialog(context);
        dgNoticeToast = new DGNoticeToast(context);
        dgDialogBuilder = new DGAlertDialog.Builder(getActivity());
    }

    private void initView(View root) {

        riv_login_avatar = (RoundImageView) root.findViewById(R.id.riv_login_avatar);

        et_login_phonenumber = (EditText) root.findViewById(R.id.et_login_phonenumber);

        et_login_phonenumber.addTextChangedListener(mTextWatcher);

        et_login_pwd = (EditText) root.findViewById(R.id.et_login_pwd);

        et_login_pwd.addTextChangedListener(mTextWatcher);

        tv_forgetpwd = (TextView) root.findViewById(R.id.tv_forgetpwd);
        tv_forgetpwd.setOnClickListener(this);

        tv_login_btn = (TextView) root.findViewById(R.id.tv_login_btn);
        tv_login_btn.setOnClickListener(this);

        tv_regist_btn = (TextView) root.findViewById(R.id.tv_regist_btn);
        tv_regist_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try{
        switch(v.getId()){
            case R.id.tv_forgetpwd:{
                Intent intent  = new Intent(context, ForgetPwdActivity.class);
                startActivity(intent);
                return ;
                }
            case R.id.tv_login_btn:{
                try{
                    try {
                        handleLogin();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                return ;
                }
            case R.id.tv_regist_btn:{
                Intent intent  = new Intent(context, UserInfoActivity.class);
                startActivity(intent);
                return;
            }
        }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //初始化数据
    protected void initData() {
        kjb = new KJBitmap();
        SharedPreferences sharedPreferences = context.getSharedPreferences("gouda", Context.MODE_PRIVATE); //私有数据

        phonenum = sharedPreferences.getString("phonenum", "");

        password = sharedPreferences.getString("password", "");

        user_logo = sharedPreferences.getString("logo","");

        if(!"".equals(phonenum)&&!"".equals(password)){
            DGApi.login(phonenum, password, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    DGUser loginResponse = XmlUtils.toBean(DGUser.class, responseBody);
                    if (loginResponse.getCode() == 0) {
                        AppContext.getInstance().saveUserInfo(loginResponse);
                        saveUserInfoByXML(loginResponse.getUserlogo(), phonenum, password);
                        String easemob_pwd= MD5.getMD5(loginResponse.getEasemob_id());
                        Log.i("easemob", loginResponse.getEasemob_id() + ":" + easemob_pwd);
                       loginEasemob(loginResponse.getEasemob_id(), easemob_pwd);

                        Intent intent = new Intent();
                        //TODO  修改登入入口
                        intent.setClass(context, MainActivity.class);
                        startActivity(intent);
                        activity.finish();

                    } else {
                        if(!"".equals(user_logo)){
                            kjb.display(riv_login_avatar, APIconfig.IMG_BASEURL + user_logo);
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    dgNoticeToast.showFailure("网络出错");
                }

            });
        }else{
            if(!"".equals(user_logo)){
                kjb.display(riv_login_avatar, APIconfig.IMG_BASEURL + user_logo);
            }
        }
    }
    //登录到环信服务器
    public void loginEasemob(final String username,final String password)
    {
        EMChatManager.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                proDia.dismiss();
                try {
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    //处理好友和群组
//                    initializeContacts();
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
//                 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                        AppContext.getInstance().getProperty("dgUser.nickname"));

                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
                Intent intent = new Intent();
                //TODO  修改登入入口
                intent.setClass(context, MainActivity.class);
                startActivity(intent);
                Log.d("sucess", "suce");
            }

            @Override
            public void onError(int i, String s)
            {
                System.out.println("sdfdf"+s);
            }

            @Override
            public void onProgress(int i, String s) {
                Log.d("progress", s);
            }
        });

    }

    private void handleLogin() {
        synchronized (lock1) {
            if (prepareForLogin()) {
                return;
            }
            // if the data has ready
            mUserName = et_login_phonenumber.getText().toString();
            mPassword = et_login_pwd.getText().toString();
            login(mUserName, mPassword);
        }
    }

    //登录信息检测
    private boolean prepareForLogin() {
        if (!TDevice.hasInternet()) {
            dgNoticeToast.showFailure(R.string.tip_no_internet);
            return true;
        }
        String login_phone = et_login_phonenumber.getText().toString();
        String login_pwd = et_login_pwd.getText().toString();
        if(!login_phone.startsWith("0")){
            if (login_phone.length() != 11) {
//            et_login_phonenumber.setError("请输入正确的手机号");
                dgDialogBuilder.setMessage("请输入正确的手机号").setNegativeButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog = dgDialogBuilder.create();
                dialog.show();
                et_login_phonenumber.requestFocus();
                return true;
            }
            if (login_pwd.length()<6||login_pwd.length()>18) {
//                et_login_pwd.setError("请输入密码");
                dgDialogBuilder.setMessage("密码只能6-18位哦").setNegativeButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog = dgDialogBuilder.create();
                dialog.show();
                et_login_pwd.requestFocus();
                return true;
            }
        }
        return false;
    }

    /**
     * 保存用户登录状态
     * @param userlogo
     * @param mUserName
     * @param mPassword
     */
    private void saveUserInfoByXML(String userlogo, String mUserName, String mPassword){

            SharedPreferences sharedPreferences = context.getSharedPreferences("gouda", Context.MODE_PRIVATE); //私有数据
            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器

            if(!StringUtils.isEmpty(userlogo)){
                editor.putString("logo", userlogo);

            }
            if(!StringUtils.isEmpty(mUserName)&&!StringUtils.isEmpty(mPassword)){
                editor.putString("phonenum", mUserName);
                editor.putString("password", mPassword);
            }
            editor.commit();//提交修改
    }


    private void login(final String phonenum, final String password){

        proDia.setMessage("正在登录...");
        proDia.show();

           DGApi.login(phonenum, password, new AsyncHttpResponseHandler() {
               @Override
               public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                   proDia.dismiss();
                   DGUser loginResponse = XmlUtils.toBean(DGUser.class, responseBody);
                   if (loginResponse.getCode() == 0) {
                       dgNoticeToast.showSuccess("登录成功");
                       AppContext.getInstance().saveUserInfo(loginResponse);
                       saveUserInfoByXML(loginResponse.getUserlogo(), phonenum, password);
                       String pwd = MD5.getMD5(loginResponse.getEasemob_id());
                       loginEasemob(loginResponse.getEasemob_id(), pwd);
                       Intent intent = new Intent();
                       //TODO  修改登入入口
                       intent.setClass(context, MainActivity.class);
                       startActivity(intent);
                   } else {
                       dgNoticeToast.showFailure("登录失败\n" + loginResponse.getMsg());
                   }
               }

               @Override
               public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                   proDia.dismiss();
                   dgNoticeToast.showFailure("网络出错");
               }

           });



    }

    private void initializeContacts()
    {
        Map<String , User> userlist=new HashMap<String,User>();
        //添加user"申请与通知"
        User newFriends=new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat="Invitation and notification";
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME,newFriends);
        //添加”Robot“
        User robotUser=new User();
        String strRoot="jiqi";
        robotUser.setUsername(Constant.CHAT_ROBOT);
        robotUser.setNick(strRoot);
        robotUser.setHeader("");
        userlist.put(Constant.CHAT_ROBOT, robotUser);

        //存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
        //存入db
        UserDao dao=new UserDao(context);
        List<User> users=new ArrayList<>(userlist.values());
        dao.saveContactList(users);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(StringUtils.isEmpty(et_login_phonenumber.getText().toString())||
                    StringUtils.isEmpty(et_login_pwd.getText().toString())){
                tv_login_btn.setBackgroundResource(R.color.white);
                tv_login_btn.setClickable(false);
            }else if(!StringUtils.isEmpty(et_login_phonenumber.getText().toString())&&
                    !StringUtils.isEmpty(et_login_pwd.getText().toString())){
                tv_login_btn.setBackgroundResource(R.drawable.bg_yellow2gray_sel);
                tv_login_btn.setClickable(true);
            }

        }
    };

}
