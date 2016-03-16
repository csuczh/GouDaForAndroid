package com.dg.app.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.dg.app.api.APIconfig;
import com.dg.app.bean.DGRegistUser;
import com.dg.app.bean.UploadImageResponse;
import com.dg.app.ui.LoginActivity;
import com.dg.app.ui.PhoneRegistActivity;
import com.dg.app.ui.SelectPicPopupWindow;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.util.DGImageUtils;
import com.dg.app.util.ImageUtils;
import com.dg.app.util.Parser;
import com.dg.app.util.StringUtils;
import com.dg.app.util.UploadAvaterUtils;
import com.dg.app.widget.DGAlertDialog;
import com.dg.app.widget.UserAgeWheelDialog;
import com.dg.app.widget.UserSexWheelDialog;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.widget.RoundImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static android.view.View.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class UserInfoFragment extends android.support.v4.app.Fragment implements OnClickListener
{

    private DGRegistUser registUser;

    private Context context;

    private UploadAvaterUtils uploadAvaterUtils;

    private SelectPicPopupWindow selectPicPopupWindow;

    private LinearLayout layout_regist_userinfo_title;
    private ImageView iv_userinfo_titlebar_back;

    private RoundImageView riv_login_avatar;

    private EditText et_regist_username;
    private TextView et_regist_sex;
    private TextView et_regist_age;

    private Button button_userinfo_nextstep;

    private KJHttp kjh;

    private String username;
    private int age;
    private int sex;

    private boolean is_upload_complete;

    private Dialog dialog;

    private DGNoticeToast dgNoticeToast;
    private ProgressDialog proDia;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_tweet, container, false);
        View root = inflate(getActivity(),
                R.layout.fragment_login_userinfo, null);

        is_upload_complete = false;
        registUser = new DGRegistUser();

        initNoticeWidget(getActivity());

        //初始化网络设置
        initData();

        initView(root);
        initUtils();
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

    private void initUtils() {
        uploadAvaterUtils = new UploadAvaterUtils(getActivity());
    }

    private void initView(View root) {
        layout_regist_userinfo_title = (LinearLayout) root.findViewById(R.id.layout_regist_userinfo_title);
        iv_userinfo_titlebar_back = (ImageView) layout_regist_userinfo_title.findViewById(R.id.iv_userinfo_titlebar_back);
        iv_userinfo_titlebar_back.setOnClickListener(this);

        riv_login_avatar = (RoundImageView) root.findViewById(R.id.riv_login_avatar);
        riv_login_avatar.setOnClickListener(this);

        et_regist_username = (EditText) root.findViewById(R.id.et_regist_username);
        et_regist_sex = (TextView) root.findViewById(R.id.et_regist_sex);
        et_regist_age = (TextView) root.findViewById(R.id.et_regist_age);

        et_regist_username.addTextChangedListener(mTextWatcher);
        et_regist_sex.addTextChangedListener(mTextWatcher);
        et_regist_age.addTextChangedListener(mTextWatcher);

        et_regist_sex.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //1、打开滚动轮选择控件
                //2、根据选择的值设置EditText值
                try {
                    UserSexWheelDialog sexCustomDialog = new UserSexWheelDialog(getActivity(), new UserSexWheelDialog.OnWhellSetData() {
                        @Override
                        public void setWhellData(String sex) {
                            et_regist_sex.setText(sex);
                            if (sex.equals("男")) {
                                registUser.setSex(0);
                            } else if (sex.equals("女")) {
                                registUser.setSex(1);
                            }

                        }
                    });
                    //设置对话框的位置
                    WindowManager m = getActivity().getWindowManager();
                    Window dialogWindow = sexCustomDialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    Display d = m.getDefaultDisplay();
                    lp.height = (int) (d.getHeight() * 0.6);
                    lp.width = (int) (d.getWidth());
                    dialogWindow.setAttributes(lp);
                    dialogWindow.setGravity(Gravity.BOTTOM);
                    sexCustomDialog.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        et_regist_age.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //1、打开滚动轮选择控件
                //2、根据选择的值设置EditText值
                try {
                    UserAgeWheelDialog ageCustomDialog=new UserAgeWheelDialog(getActivity(), new UserAgeWheelDialog.OnWhellSetData() {
                        @Override
                        public void setWhellData(Integer min) {
                            et_regist_age.setText(min+"岁");
                            registUser.setAge(min);
                        }
                    });
                    //设置对话框的位置
                    WindowManager m=getActivity().getWindowManager();
                    Window dialogWindow=ageCustomDialog.getWindow();
                    WindowManager.LayoutParams lp=dialogWindow.getAttributes();
                    Display d=m.getDefaultDisplay();
                    lp.height=(int)(d.getHeight()*0.6);
                    lp.width=(int)(d.getWidth());
                    dialogWindow.setAttributes(lp);
                    dialogWindow.setGravity(Gravity.BOTTOM);
                    ageCustomDialog.show();


                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        button_userinfo_nextstep = (Button) root.findViewById(R.id.button_userinfo_nextstep);
        button_userinfo_nextstep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try{
        switch(v.getId()){
            case R.id.iv_userinfo_titlebar_back:{
                Toast.makeText(getActivity(),"返回登录界面",Toast.LENGTH_LONG).show();
                getActivity().finish();
                return ;
                }
            case R.id.riv_login_avatar:{
                Toast.makeText(getActivity(),"上传头像",Toast.LENGTH_LONG).show();
                //1、打开popmenuwindow选项条
                //2、打开照相机拍照/打开本地图库选择
                //3、编辑图片
                openPopMenuWindow();
                return;
                }
            case R.id.button_userinfo_nextstep:{
                nextStep();
                return ;
                }
        }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void nextStep() {

        if(!is_upload_complete){
            AppContext.showToast("请选择一个头像");
            return ;
        }
        String username = et_regist_username.getText().toString();
        String sex = et_regist_sex.getText().toString();
        String age = et_regist_age.getText().toString();
        if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(sex)&&!TextUtils.isEmpty(age)){
            registUser.setNickname(username);
            Intent intent = new Intent(context, PhoneRegistActivity.class);
            intent.putExtra("registUser",registUser);
            startActivity(intent);
        }

    }


    public void openPopMenuWindow(){
        //创建弹出菜单，并添加监听器
        selectPicPopupWindow = new SelectPicPopupWindow(getActivity(), itemsOnclick);
        //显示窗口
        //设置layout在PopupWindow中显示的位置
        selectPicPopupWindow.showAtLocation(getActivity().findViewById(R.id.fl_loginactivity), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    //弹出菜单点击事件
    private OnClickListener itemsOnclick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            selectPicPopupWindow.dismiss();
            Intent intent;
            switch (v.getId()) {
                case R.id.btn_take_photo:
//                    Toast.makeText(getActivity(),"照相",Toast.LENGTH_LONG).show();
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, UploadAvaterUtils.CAMERA_REQUEST_CODE);
                    return;
                case R.id.btn_pick_photo:
//                    Toast.makeText(getActivity(),"选择图片",Toast.LENGTH_LONG).show();
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, UploadAvaterUtils.GALLERY_REQUEST_CODE);
                    return;
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == UploadAvaterUtils.CAMERA_REQUEST_CODE)
        {
            if(data == null)
            {
                return;
            }
            else
            {
                Bundle extras = data.getExtras();
                if(extras != null)
                {
                    Bitmap bm = extras.getParcelable("data");

                    Uri uri = uploadAvaterUtils.saveBitmap(bm);
                    startImageZoom(uri);
                }
            }
        }
        else if(requestCode == UploadAvaterUtils.GALLERY_REQUEST_CODE)
        {
            if(data == null)
            {
                return;
            }
            Uri uri;
            uri = data.getData();
            Uri fileUri = uploadAvaterUtils.convertUri(uri);
            startImageZoom(fileUri);
        }
        else if(requestCode == UploadAvaterUtils.CROP_REQUEST_CODE)
        {
            if(data == null)
            {
                Toast.makeText(getActivity(),"未获取返回后的图片",Toast.LENGTH_LONG).show();
                return;
            }
            Bundle extras = data.getExtras();
            if(extras == null){
                Toast.makeText(getActivity(),"未获取裁剪后的图片",Toast.LENGTH_LONG).show();
                return;
            }
            Bitmap bm = extras.getParcelable("data");
            Toast.makeText(getActivity(),"获取裁剪后的图片"+bm.getHeight(),Toast.LENGTH_LONG).show();

//            riv_login_avatar.setImageBitmap(bm);

            sendImage(bm);
        }
    }


    //初始化数据
    protected void initData() {
        //配置http连接属性
        HttpConfig config = new HttpConfig();
        config.cacheTime = 0;
        config.useDelayCache = false;
        kjh = new KJHttp(config);
    }

    private void sendImage(Bitmap bm) {
        proDia.show();
        try{
//            final Bitmap bitmap = bm;

            //根据图片质量压缩图片
            final Bitmap bitmap = DGImageUtils.compressBitmapByQuality(bm);

            File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.dg.avater");
            if(!tmpDir.exists()&&tmpDir.isDirectory()){//判断文件目录是否存在
                tmpDir.mkdirs();
            }
            File img = new File(tmpDir.getAbsolutePath() + "avater.png");
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            HttpParams params = new HttpParams();
            params.put("photo",img);
            params.put("device", "android");
            params.put("time", System.currentTimeMillis() / 1000 + "");
            kjh.post(APIconfig.API_BASEURL + APIconfig.FILE_UPLOADIAMGE, params, new HttpCallBack() {
                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    proDia.dismiss();
                    Log.i("uploadImage", t);
                    UploadImageResponse uploadImageResponse = Parser.xmlToBean(UploadImageResponse.class,t);
                    if(uploadImageResponse.getCode()==0){
                        String url = uploadImageResponse.getUrl();
                        registUser.setLogo(url);
                        is_upload_complete = true;
                        riv_login_avatar.setImageBitmap(bitmap);
                    }

                }
                @Override
                public void onFailure(int errorNo, String strMsg) {
                    super.onFailure(errorNo, strMsg);
                    proDia.dismiss();
                    dgNoticeToast.showFailure(strMsg);
                }

            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 开始编辑图片大小
     * @param uri
     */
    public void startImageZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 120);
        intent.putExtra("outputY", 120);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, UploadAvaterUtils.CROP_REQUEST_CODE);
    }

    public DGRegistUser getRegistUser() {
        return registUser==null?new DGRegistUser():registUser;
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
            if(StringUtils.isEmpty(et_regist_username.getText().toString())||
                    StringUtils.isEmpty(et_regist_sex.getText().toString())||
                    StringUtils.isEmpty(et_regist_age.getText().toString())){
                button_userinfo_nextstep.setBackgroundResource(R.color.white);
                button_userinfo_nextstep.setClickable(false);
            }else if(!StringUtils.isEmpty(et_regist_username.getText().toString())&&
                    !StringUtils.isEmpty(et_regist_sex.getText().toString())&&
                    !StringUtils.isEmpty(et_regist_age.getText().toString())){
                button_userinfo_nextstep.setBackgroundResource(R.drawable.bg_yellow2gray_sel);
                button_userinfo_nextstep.setClickable(true);
            }

        }
    };

}
