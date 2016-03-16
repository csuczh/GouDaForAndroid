package com.dg.app.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.CommonResponse;
import com.dg.app.ui.LoginActivity;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.util.TimeCountUtil;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class ForgetPwdFragment extends android.support.v4.app.Fragment implements View.OnClickListener
{

    private Context context;

    private LinearLayout layout_forgetpwd_title;
    private ImageView iv_forgetpwd_titlebar_back;

    private EditText et_forgetpwd_username;

    private EditText et_forgetpwd_pwd;

    private EditText et_forgetpwd_copypwd;

    private EditText et_forgetpwd_checkcode;

    private TextView tv_forgetpwd_getcheckcode;

    private TextView tv_forgetpwd_errorinfo;

    private Button button_forgetpwd_complete;

    private String phonenum;

    private String password;

    private String code;

    //提示组件
    private DGNoticeToast dgNoticeToast;
    private ProgressDialog proDia;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_tweet, container, false);
        View root = View.inflate(getActivity(),
                R.layout.fragment_login_forgetpwd, null);

        initNoticeWidget(getActivity());

        initView(root);
        return root;
    }

    /**
     * 初始化提示组件
     * @param context
     */
    private void initNoticeWidget(Context context) {
        proDia = new ProgressDialog(context);
        dgNoticeToast = new DGNoticeToast(context);
    }

    private void initView(View root) {
        layout_forgetpwd_title = (LinearLayout) root.findViewById(R.id.layout_forgetpwd_title);
        iv_forgetpwd_titlebar_back = (ImageView) layout_forgetpwd_title.findViewById(R.id.iv_forgetpwd_titlebar_back);
        iv_forgetpwd_titlebar_back.setOnClickListener(this);

        et_forgetpwd_username = (EditText) root.findViewById(R.id.et_forgetpwd_username);
        et_forgetpwd_pwd = (EditText) root.findViewById(R.id.et_forgetpwd_pwd);
        et_forgetpwd_copypwd = (EditText) root.findViewById(R.id.et_forgetpwd_copypwd);
        et_forgetpwd_checkcode = (EditText) root.findViewById(R.id.et_forgetpwd_checkcode);

        //设置监听器
        et_forgetpwd_username.setOnFocusChangeListener(focusChangeListener);
        et_forgetpwd_pwd.setOnFocusChangeListener(focusChangeListener);
        et_forgetpwd_copypwd.setOnFocusChangeListener(focusChangeListener);

        tv_forgetpwd_getcheckcode = (TextView) root.findViewById(R.id.tv_forgetpwd_getcheckcode);
        tv_forgetpwd_getcheckcode.setOnClickListener(this);

        tv_forgetpwd_errorinfo = (TextView) root.findViewById(R.id.tv_forgetpwd_errorinfo);
        tv_forgetpwd_errorinfo.setVisibility(View.GONE);


        button_forgetpwd_complete = (Button) root.findViewById(R.id.button_forgetpwd_complete);
        button_forgetpwd_complete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        try{
        switch(v.getId()){
            case R.id.button_forgetpwd_complete:{
                validateCode();
                return;
                }
            case R.id.tv_forgetpwd_getcheckcode:{
                //TODO
                //1、获取验证码
                phonenum = et_forgetpwd_username.getText().toString();
                if(!"".equals(phonenum)){
                    //1、开启计时线程、修改文字
                    TimeCountUtil timeCountUtil=new TimeCountUtil(getActivity(),60000,1000,tv_forgetpwd_getcheckcode);
                    timeCountUtil.start();
                    proDia.show();
                    //2、请求验证码
                    DGApi.getForgetPasswordCode(phonenum, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            proDia.dismiss();
                            try {
                                CommonResponse commonResponse = XmlUtils.toBean(CommonResponse.class,responseBody);
                                if(commonResponse.getCode()!=0){
                                    dgNoticeToast.showFailure(commonResponse.getMsg());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            proDia.dismiss();
                            dgNoticeToast.showFailure("网络异常");
                        }
                    });
                }
                return ;
                }

            case R.id.iv_forgetpwd_titlebar_back:
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                break;
        }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void validateCode(){
        phonenum = et_forgetpwd_username.getText().toString();
        password = et_forgetpwd_pwd.getText().toString();
        code = et_forgetpwd_checkcode.getText().toString();

        if(!TextUtils.isEmpty(phonenum)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(code)){
            updatePwd(phonenum, code, password);
        }
    }


    //TODO
    private void updatePwd(String phonenum, String code,String password) {

        proDia.show();

        DGApi.forgetPassword(phonenum, code, password, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                proDia.dismiss();
                CommonResponse commonResponse = XmlUtils.toBean(CommonResponse.class,responseBody);
                if(commonResponse.getCode()==0){
                    dgNoticeToast.showSuccess("密码修改成功");
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }else{
                    dgNoticeToast.showFailure(commonResponse.getMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                proDia.dismiss();
                dgNoticeToast.showFailure("网络错误");
            }
        });

    }


    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()){
                case R.id.et_forgetpwd_username:
                    String phone = et_forgetpwd_username.getText().toString();
                    if(TextUtils.isEmpty(phone)){
                        Toast.makeText(context,"账号不能为空！",Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.et_forgetpwd_pwd:
                    String pwd = et_forgetpwd_pwd.getText().toString();
                    if(TextUtils.isEmpty(pwd)){
                        Toast.makeText(context,"密码不能为空！",Toast.LENGTH_SHORT).show();
                    }else if(pwd.length()<6||pwd.length()>18){
                        Toast.makeText(context,"密码必须为6-18位！",Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.et_forgetpwd_copypwd:
                    String pwd1 = et_forgetpwd_pwd.getText().toString();
                    String pwd2 = et_forgetpwd_copypwd.getText().toString();
                    if(!pwd1.equals(pwd2)){
                        Toast.makeText(context,"两次输入密码必须相同",Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };


}
