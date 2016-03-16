package com.dg.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.CommonResponse;
import com.dg.app.ui.AboutDGActivity;
import com.dg.app.ui.ClearChatRecordActivity;
import com.dg.app.ui.ContactUsActivity;
import com.dg.app.ui.GiveAdviceActivity;
import com.dg.app.ui.LoginActivity;
import com.dg.app.ui.MainActivity;
import com.dg.app.ui.NoticeSettingActivity;
import com.dg.app.ui.UpdatepwdActivity;
import com.dg.app.util.XmlUtils;
import com.dg.app.widget.DGAlertDialog;
import com.dg.app.widget.LocationCustomDialog;
import com.easemob.EMCallBack;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.bitlet.weupnp.Main;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class SettingFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    //顶部actionbar
    private LinearLayout layout_setting_title;
    private ImageView iv_setting_titlebar_back;

    private RelativeLayout update_pwd;
    private RelativeLayout notice_setting;
    private RelativeLayout clear_cache;
    private RelativeLayout clear_chat_record;
    private RelativeLayout set_location;
    private RelativeLayout give_good;
    private RelativeLayout make_gouda_better;
    private RelativeLayout contact_us;
    private RelativeLayout about_gouda;
    private RelativeLayout exit_gouda;

    private int area_code;

    private int user_id;

    private TextView tv_location;


    private boolean is_loginout;

    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity=getActivity();
        // Inflate the layout for this fragment
        View root = View.inflate(getActivity(),
                R.layout.fragment_me_setting, null);

        is_loginout = false;

        user_id = AppContext.getInstance().getLoginUid();
        if(user_id==0){
            Intent intent = new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
        }

        initTitleBar(root);

        return root;
    }

    private void initTitleBar(View root) {
        //标题栏
        layout_setting_title = (LinearLayout) root.findViewById(R.id.layout_setting_title);
        iv_setting_titlebar_back = (ImageView) layout_setting_title.findViewById(R.id.iv_setting_titlebar_back);
        iv_setting_titlebar_back.setOnClickListener(this);

        update_pwd = (RelativeLayout) root.findViewById(R.id.update_pwd);
        notice_setting = (RelativeLayout) root.findViewById(R.id.notice_setting);
        clear_cache = (RelativeLayout) root.findViewById(R.id.clear_cache);
        clear_chat_record = (RelativeLayout) root.findViewById(R.id.clear_chat_record);
        set_location = (RelativeLayout) root.findViewById(R.id.set_location);
        give_good = (RelativeLayout) root.findViewById(R.id.give_good);
        make_gouda_better = (RelativeLayout) root.findViewById(R.id.make_gouda_better);
        contact_us = (RelativeLayout) root.findViewById(R.id.contact_us);
        make_gouda_better = (RelativeLayout) root.findViewById(R.id.make_gouda_better);
        about_gouda = (RelativeLayout) root.findViewById(R.id.about_gouda);
        exit_gouda = (RelativeLayout) root.findViewById(R.id.exit_gouda);

        update_pwd.setOnClickListener(this);
        notice_setting.setOnClickListener(this);
        clear_cache.setOnClickListener(this);
        clear_chat_record.setOnClickListener(this);
        give_good.setOnClickListener(this);
        make_gouda_better.setOnClickListener(this);
        contact_us.setOnClickListener(this);
        make_gouda_better.setOnClickListener(this);
        about_gouda.setOnClickListener(this);
        exit_gouda.setOnClickListener(this);
        set_location.setOnClickListener(this);

        tv_location = (TextView) root.findViewById(R.id.tv_location);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_setting_titlebar_back:{
                getActivity().finish();
                break;
            }
            case R.id.update_pwd:{
                    Toast.makeText(getActivity(), "修改密码", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),UpdatepwdActivity.class);
                    startActivity(intent);
                break;
            }

            case R.id.notice_setting:{
                Toast.makeText(getActivity(), "提醒设置", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),NoticeSettingActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.clear_cache:{
                Dialog dialog;
                DGAlertDialog.Builder  dgDialogBuilder = new DGAlertDialog.Builder(getActivity());
                dgDialogBuilder.setMessage("确认删除缓存吗?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppContext.getInstance().clearAppCache();
                        Toast.makeText(getActivity(), "清楚缓存", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog = dgDialogBuilder.create();
                dialog.show();
                break;
            }

            case R.id.clear_chat_record:{
                Toast.makeText(getActivity(), "清除聊天记录界面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),ClearChatRecordActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.set_location:{
                Toast.makeText(getActivity(), "设置定位", Toast.LENGTH_SHORT).show();
                final LocationCustomDialog locationCustomDialog=new LocationCustomDialog(getActivity(), new LocationCustomDialog.OnWheelSetArea() {
                    @Override
                    public void onSetArea(final String p, final String c, final String d, final String areaCode) {

//                            province.setText(p);
//                            provinceString=p;
//                            city.setText(c);
//                            cityString=c;
//                            dist.setText(d);
//                            distString=d;
//                        tv_location.setText(p+"-"+c+"-"+d);
                        area_code = Integer.parseInt(areaCode);

                        DGApi.modifyArea(user_id, area_code, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                CommonResponse commonResponse = XmlUtils.toBean(CommonResponse.class,responseBody);
                                if(commonResponse.getCode()==0){
                                    area_code = Integer.parseInt(areaCode);
                                    tv_location.setText(p+"-"+c+"-"+d);
                                }else{
                                    Toast.makeText(getActivity(),commonResponse.getMsg(),Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
                            }
                        });


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

                break;
            }
            case R.id.give_good:{
                Toast.makeText(getActivity(), "给我们一个五星好评", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(),WriteStatusActivity.class);
//                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                    startActivity(intent);
//                }
                break;
            }
            case R.id.make_gouda_better:{
                Toast.makeText(getActivity(), "意见反馈", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),GiveAdviceActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.contact_us:{
                Toast.makeText(getActivity(), "联系我们", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),ContactUsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.about_gouda:{
                Toast.makeText(getActivity(), "关于狗搭", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), AboutDGActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.exit_gouda:{
               Dialog dialog;
                DGAlertDialog.Builder  dgDialogBuilder = new DGAlertDialog.Builder(getActivity());
                dgDialogBuilder.setMessage("确定退出登录吗?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final  DialogInterface dialogInterface=dialog;
                        DemoHXSDKHelper.getInstance().logout(true, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                              activity.runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      AppContext.getInstance().cleanLoginInfo();

                                      deleteUserState();
                                      dialogInterface.dismiss();
                                      AppManager.getAppManager().finishActivity(MainActivity.class);
                                      Intent intent = new Intent(getActivity(), LoginActivity.class);
                                      startActivity(intent);
                                  }
                              });
                            }

                            @Override
                            public void onError(int i, String s) {

                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });


                    }
                });
                dialog = dgDialogBuilder.create();
                dialog.show();
                break;
            }

        }
    }

    private void deleteUserState() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("gouda", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.remove("phonenum");
        editor.remove("password");
        editor.commit();//提交修改
    }

}
