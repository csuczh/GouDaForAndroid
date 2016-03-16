package com.dg.app.fragment;

import android.app.Fragment;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.CommonResponse;
import com.dg.app.bean.DGRegistUser;
import com.dg.app.bean.RegistResponse;
import com.dg.app.ui.LocationActivity;
import com.dg.app.util.TimeCountUtil;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class PhoneRegistFragment extends android.support.v4.app.Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener
{

    private Context context;

    private LinearLayout layout_regist_phonenum_title;  //顶部栏
    private ImageView iv_phonenum_titlebar_back;    //回退按钮

    private EditText et_regist_phonenum;   //手机号输入框
    private EditText et_regist_pwd; //密码输入框
    private EditText et_regist_checkcode;   //注册码输入框

    private TextView tv_regist_getcheckcode; //获取注册码

    private CheckBox cb_readgouda;  //已阅读

    private TextView tv_gouda_protocol;//狗搭协议

    private TextView tv_checkcode_error;   //错误信息

    private Button button_nextstep;


    private DGRegistUser registUser;

    private String phonenum;
    private String password;
    private String code;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_tweet, container, false);
        View root = View.inflate(getActivity(),
                R.layout.fragment_login_phonenum, null);

        initData();

        initView(root);
        return root;
    }

    private void initData() {
        registUser = (DGRegistUser) getActivity().getIntent().getSerializableExtra("registUser");
        if(registUser!=null){
            AppContext.showToast(registUser.toString());
        }else {
            AppContext.showToast("registUser为空");
        }

    }

    private void initView(View root) {
        layout_regist_phonenum_title = (LinearLayout) root.findViewById(R.id.layout_regist_phonenum_title);
        iv_phonenum_titlebar_back = (ImageView) layout_regist_phonenum_title.findViewById(R.id.iv_phonenum_titlebar_back);
        iv_phonenum_titlebar_back.setOnClickListener(this);

        et_regist_phonenum = (EditText) root.findViewById(R.id.et_regist_phonenum);
        et_regist_pwd  = (EditText) root.findViewById(R.id.et_regist_pwd);
        et_regist_checkcode = (EditText) root.findViewById(R.id.et_regist_checkcode);


        tv_regist_getcheckcode = (TextView) root.findViewById(R.id.tv_regist_getcheckcode);
        tv_regist_getcheckcode.setOnClickListener(this);

        cb_readgouda = (CheckBox) root.findViewById(R.id.cb_readgouda);
        cb_readgouda.setOnCheckedChangeListener(this);

        tv_gouda_protocol = (TextView) root.findViewById(R.id.tv_gouda_protocol);
        tv_gouda_protocol.setOnClickListener(this);

        tv_checkcode_error = (TextView) root.findViewById(R.id.tv_checkcode_error);
        tv_checkcode_error.setVisibility(View.GONE);

        button_nextstep = (Button) root.findViewById(R.id.button_nextstep);
        button_nextstep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try{
        switch(v.getId()){
            case R.id.iv_phonenum_titlebar_back:{
                Toast.makeText(getActivity(),"返回登录界面",Toast.LENGTH_LONG).show();
                getActivity().finish();
                return ;
                }
            case R.id.tv_regist_getcheckcode:{
                Toast.makeText(getActivity(),"获取验证码",Toast.LENGTH_LONG).show();
                //TODO
                phonenum = et_regist_phonenum.getText().toString();
                if(!"".equals(phonenum)){
                    //1、开启计时线程、修改文字
                    TimeCountUtil timeCountUtil=new TimeCountUtil(getActivity(),60000,1000,tv_regist_getcheckcode);
                    timeCountUtil.start();
                    //2、请求验证码
                    DGApi.getCode(phonenum, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            CommonResponse commonResponse = XmlUtils.toBean(CommonResponse.class,responseBody);
                            if(commonResponse.getCode()!=0){
                                AppContext.showToast(commonResponse.getMsg());
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            AppContext.showToast("网络错误");
                        }
                    });
                }

                return;
                }
            case R.id.tv_gouda_protocol:{
                Toast.makeText(getActivity(),"狗搭协议详细介绍",Toast.LENGTH_LONG).show();
                //TODO
                //1、请求后台获取详细协议内容
                //2、弹出dialing显示
                return ;
                }
            case R.id.button_nextstep:{
                nextStep();
                return ;
                }
        }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void nextStep() {
        if (!cb_readgouda.isChecked()){
            Toast.makeText(getActivity(),"请阅读并同意相关协议",Toast.LENGTH_LONG).show();
        }else{
            phonenum = et_regist_phonenum.getText().toString();
            password = et_regist_pwd.getText().toString();
            code = et_regist_checkcode.getText().toString();

            if(!TextUtils.isEmpty(phonenum)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(code)){

                DGApi.validateCode(phonenum, code, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        CommonResponse commonResponse  = XmlUtils.toBean(CommonResponse.class,responseBody);
                        if(commonResponse.getCode()==0){
                            if(tv_checkcode_error.getVisibility()==View.VISIBLE){
                                tv_checkcode_error.setVisibility(View.GONE);
                            }

                            registUser.setPhone(phonenum);
                            registUser.setPassword(password);

                            Intent intent = new Intent(context, LocationActivity.class);
                            intent.putExtra("registUser",registUser);
                            startActivity(intent);

                        }else{
                            tv_checkcode_error.setVisibility(View.VISIBLE);
                            AppContext.showToast(commonResponse.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        AppContext.showToast("网络出错");
                    }
                });


            }

        }

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            button_nextstep.setClickable(true);
           }else{
            button_nextstep.setClickable(false);
           }
    }
}
