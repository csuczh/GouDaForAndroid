package com.dg.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.DGRegistUser;
import com.dg.app.bean.DGUser;
import com.dg.app.bean.RegistResponse;
import com.dg.app.ui.DGUserInfoActivity;
import com.dg.app.ui.DogIntroActivity;
import com.dg.app.util.XmlUtils;
import com.dg.app.widget.LocationCustomDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class LocationFragment extends android.support.v4.app.Fragment implements View.OnClickListener
{
    private Context context;

    private LinearLayout layout_location_title;
    private ImageView iv_location_titlebar_back;

    private TextView et_location_select;

    private Button button_welcome_entergouda;

    private DGRegistUser registUser;

    private int area_code;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_tweet, container, false);
        View root = View.inflate(getActivity(),
                R.layout.fragment_login_location, null);

        initData();

        initView(root);
        return root;
    }

    private void initData() {
        registUser = (DGRegistUser) getActivity().getIntent().getSerializableExtra("registUser");
    }

    private void initView(View root) {
        layout_location_title = (LinearLayout) root.findViewById(R.id.layout_location_title);
        iv_location_titlebar_back = (ImageView) layout_location_title.findViewById(R.id.iv_location_titlebar_back);
        iv_location_titlebar_back.setOnClickListener(this);

        et_location_select = (TextView) root.findViewById(R.id.et_location_select);
        et_location_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //1、打开滚动轮选择控件
                //2、根据选择的值设置EditText值
                final LocationCustomDialog locationCustomDialog=new LocationCustomDialog(getActivity(), new LocationCustomDialog.OnWheelSetArea() {
                    @Override
                    public void onSetArea(String p, String c, String d, String areaCode) {

                        et_location_select.setText(p+"-"+c+"-"+d);

                        area_code = Integer.parseInt(areaCode);

                        Toast.makeText(context,areaCode,Toast.LENGTH_SHORT).show();
                    }
                });
                //设置对话框的位置
                WindowManager m=getActivity().getWindowManager();
                Window dialogWindow=locationCustomDialog.getWindow();
                WindowManager.LayoutParams lp=dialogWindow.getAttributes();
                Display d=m.getDefaultDisplay();
                lp.height=(int)(d.getHeight()*0.6);
                lp.width=(int)(d.getWidth());
                dialogWindow.setAttributes(lp);
                dialogWindow.setGravity(Gravity.BOTTOM);

                locationCustomDialog.show();
            }
        });

        button_welcome_entergouda = (Button) root.findViewById(R.id.button_welcome_entergouda);
        button_welcome_entergouda.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        try{
        switch(v.getId()){
            case R.id.iv_location_titlebar_back:{
                Toast.makeText(getActivity(),"返回登录界面",Toast.LENGTH_LONG).show();
                getActivity().finish();
                return ;
                }
            case R.id.button_welcome_entergouda:{
                Toast.makeText(getActivity(),"注册",Toast.LENGTH_LONG).show();
                regist();
                return;
                }
        }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void regist() {
        Toast.makeText(getActivity(),registUser.toString(),Toast.LENGTH_LONG).show();
        if(!TextUtils.isEmpty(area_code+"")){
            /**
             *  注册
             * @param phone
             * @param password
             * @param area_code
             * @param nickname
             * @param sex
             * @param age
             * @param logo
             * @param handler
             */
            DGApi.regist(registUser.getPhone(), registUser.getPassword(), area_code,
                    registUser.getNickname(), registUser.getSex(),
                    registUser.getAge(), registUser.getLogo(), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            RegistResponse registResponse = XmlUtils.toBean(RegistResponse.class, responseBody);
                            Log.i("====>RegistResponse", registResponse.toString());
                            if (registResponse.getCode() == 0) {
                                registUser.setUser_id(registResponse.getUserid());
                                registUser.setEasemob_id(registResponse.getEasemob_id());
                                login(registUser);
                            } else {
                                if (registResponse!=null) {
                                    Toast.makeText(getActivity(), registResponse.getMsg(), Toast.LENGTH_LONG).show();
                                }
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getActivity(),"网络异常", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }

    //登录
    private void login(final DGRegistUser registUser){
        if(registUser!=null){
            DGApi.login(registUser.getPhone(), registUser.getPassword(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    DGUser user = XmlUtils.toBean(DGUser.class,responseBody);
                    if(user.getCode()==0){
                        AppContext.getInstance().saveUserInfo(user);

                        Intent intent = new Intent(context,DogIntroActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    AppContext.showToast("网络异常");
                }
            });

        }
    }

}
