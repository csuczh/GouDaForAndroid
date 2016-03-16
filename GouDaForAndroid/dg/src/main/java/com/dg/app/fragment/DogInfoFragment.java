package com.dg.app.fragment;

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
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.CommonResponse;
import com.dg.app.bean.DGRegistDog;
import com.dg.app.bean.UploadImageResponse;
import com.dg.app.bean.UploadLogoResponse;
import com.dg.app.ui.LoginActivity;
import com.dg.app.ui.MainActivity;
import com.dg.app.ui.SelectPicPopupWindow;
import com.dg.app.ui.SettingActivity;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.util.DGImageUtils;
import com.dg.app.util.Parser;
import com.dg.app.util.StringUtils;
import com.dg.app.util.UploadAvaterUtils;
import com.dg.app.util.XmlUtils;
import com.dg.app.widget.DogAgeWheelDialog;
import com.dg.app.widget.DogSexWheelDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.widget.RoundImageView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class DogInfoFragment extends android.support.v4.app.Fragment implements View.OnClickListener
{
    private Context context;

    private KJHttp kjh;

    private UploadAvaterUtils uploadAvaterUtils;

    private LinearLayout layout_doginfo_title;
    private ImageView iv_doginfo_titlebar_back;
    private TextView tv_doginfo_titlebar_jump;

    private RoundImageView riv_dg_regist_avatar;

    private EditText et_regist_dogname;
    private TextView et_regist_dogage;
    private TextView et_regist_dogsex;
    private EditText et_regist_dogtype;

    private Button button_regist_dgnextstep;

    private int user_id;
    private String dog_logo;
    private String dog_nickname;
    private String dog_variety;
    private int dog_sex;
    private int dog_age;

    //提示组件
    private DGNoticeToast dgNoticeToast;
    private ProgressDialog proDia;

    private SelectPicPopupWindow selectPicPopupWindow;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_tweet, container, false);
        View root = View.inflate(getActivity(),
                R.layout.fragment_login_doginfo, null);

        initNoticeWidget(getActivity());

        initHttp();
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


    //初始化数据
    protected void initHttp() {
        //配置http连接属性
        HttpConfig config = new HttpConfig();
        config.cacheTime = 0;
        config.useDelayCache = false;
        kjh = new KJHttp(config);
    }

    private void initView(View root) {
        layout_doginfo_title = (LinearLayout) root.findViewById(R.id.layout_doginfo_title);
        iv_doginfo_titlebar_back = (ImageView) layout_doginfo_title.findViewById(R.id.iv_doginfo_titlebar_back);
        iv_doginfo_titlebar_back.setOnClickListener(this);

        tv_doginfo_titlebar_jump = (TextView) layout_doginfo_title.findViewById(R.id.tv_doginfo_titlebar_jump);
        tv_doginfo_titlebar_jump.setOnClickListener(this);

        riv_dg_regist_avatar = (RoundImageView) root.findViewById(R.id.riv_dg_regist_avatar);
        riv_dg_regist_avatar.setOnClickListener(this);

        et_regist_dogname = (EditText) root.findViewById(R.id.et_regist_dogname);
        et_regist_dogage = (TextView) root.findViewById(R.id.et_regist_dogage);
        et_regist_dogsex = (TextView) root.findViewById(R.id.et_regist_dogsex);
        et_regist_dogtype = (EditText) root.findViewById(R.id.et_regist_dogtype);

        et_regist_dogage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1、打开滚动轮选择控件
                //2、根据选择的值设置EditText值
                try {
                    DogAgeWheelDialog ageCustomDialog = new DogAgeWheelDialog(getActivity(), new DogAgeWheelDialog.OnWhellSetData() {
                        @Override
                        public void setWhellData(Integer age) {
                            et_regist_dogage.setText(age+"");
                            dog_age = age;
                        }
                    });
                    //设置对话框的位置
                    WindowManager m = getActivity().getWindowManager();
                    Window dialogWindow = ageCustomDialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    Display d = m.getDefaultDisplay();
                    lp.height = (int) (d.getHeight() * 0.6);
                    lp.width = (int) (d.getWidth());
                    dialogWindow.setAttributes(lp);
                    dialogWindow.setGravity(Gravity.BOTTOM);
                    ageCustomDialog.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        et_regist_dogsex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1、打开滚动轮选择控件
                //2、根据选择的值设置EditText值
                try {
                    DogSexWheelDialog sexCustomDialog = new DogSexWheelDialog(getActivity(), new DogSexWheelDialog.OnWhellSetData() {
                        @Override
                        public void setWhellData(String sex) {
                            et_regist_dogsex.setText(sex);
                            if (sex.equals("公")) {
                                dog_sex = 0;
                            } else if (sex.equals("母")) {
                                dog_sex = 1;
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

        button_regist_dgnextstep = (Button) root.findViewById(R.id.button_regist_dgnextstep);
        button_regist_dgnextstep.setOnClickListener(this);
    }

    private void initUtils() {
        uploadAvaterUtils = new UploadAvaterUtils(getActivity());
    }

    @Override
    public void onClick(View v) {
        try{

            Intent intent;
        switch(v.getId()){
            case R.id.iv_doginfo_titlebar_back:{
                getActivity().finish();
                break;
                }
            case R.id.tv_doginfo_titlebar_jump:{
//                Toast.makeText(getActivity(),"跳过设置界面",Toast.LENGTH_LONG).show();
                //TODO
                //1、直接进入狗搭主界面
                intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
                }
            case R.id.riv_dg_regist_avatar:{
                openPopMenuWindow();
                break;
                }
            case R.id.button_regist_dgnextstep:{
                setDogInfo();
                break;
                }
        }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setDogInfo() {
        user_id = AppContext.getInstance().getLoginUid();
        dog_nickname = et_regist_dogname.getText().toString();
        dog_age = Integer.parseInt(et_regist_dogage.getText().toString());
        String sex = et_regist_dogsex.getText().toString();
        if(sex.equals("公")){
            dog_sex = 0;
        }else if(sex.equals("母")){
            dog_sex = 1;
        }
        dog_variety = et_regist_dogtype.getText().toString();

        if(user_id==0){
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        }else if(TextUtils.isEmpty(dog_nickname)||TextUtils.isEmpty(dog_age+"")||TextUtils.isEmpty(dog_sex+"")){
            Toast.makeText(getActivity(),"信息不能为空",Toast.LENGTH_SHORT).show();
        }else{

            if(dog_logo==null&&StringUtils.isEmpty(dog_logo)){
                AppContext.showToast("请选择一个头像");
                return ;
            }

            DGRegistDog dog = new DGRegistDog();
            dog.setUser_id(user_id);
            dog.setDog_nickname(dog_nickname);
            dog.setDog_age(dog_age);
            dog.setDog_sex(dog_sex);
            dog.setDog_variety(dog_variety);
            dog.setDog_logo(dog_logo);

            proDia.show();

            DGApi.setDogInfo(dog, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    proDia.dismiss();
                    CommonResponse commonResponse = XmlUtils.toBean(CommonResponse.class, responseBody);
                    if (commonResponse.getCode() == 0) {
                        dgNoticeToast.showSuccess("设置成功");
                        //1、直接进入狗搭主界面
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        dgNoticeToast.showFailure(commonResponse.getMsg());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    dgNoticeToast.showFailure("网络异常");
                }
            });

        }
    }

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

//            riv_dg_regist_avatar.setImageBitmap(bm);
            sendImage(bm);
        }
    }

//    private void sendImage(Bitmap bm) {
//
//        try{
////            final Bitmap bitmap = bm;
//
//            //根据图片质量压缩图片
//            final Bitmap bitmap = DGImageUtils.compressBitmapByQuality(bm);
//
//            HttpParams params = new HttpParams();
//            File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.dg.avater");
//            if(!tmpDir.exists()&&tmpDir.isDirectory()){//判断文件目录是否存在
//                tmpDir.mkdirs();
//            }
//            File img = new File(tmpDir.getAbsolutePath() + "dogavater.png");
//            FileOutputStream fos = new FileOutputStream(img);
//            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.flush();
//            fos.close();
//
//            proDia.show();
//
//            params.put("photo",img);
//            params.put("device", "android");
//            params.put("time", System.currentTimeMillis() / 1000 + "");
//            kjh.post(APIconfig.API_BASEURL + APIconfig.FILE_UPLOADIAMGE, params, new HttpCallBack() {
//                @Override
//                public void onPreStar() {
//                    super.onPreStar();
//                }
//                @Override
//                public void onSuccess(String t) {
//                    super.onSuccess(t);
//                    proDia.dismiss();
//                    Log.i("uploadImage", t);
//                    UploadImageResponse uploadImageResponse = Parser.xmlToBean(UploadImageResponse.class, t);
//                    if(uploadImageResponse.getCode()==0){
//                        dog_logo = uploadImageResponse.getUrl();
//                        riv_dg_regist_avatar.setImageBitmap(bitmap);
//                    }else{
//                        dgNoticeToast.showFailure(uploadImageResponse.getMsg());
//                    }
//                }
//                @Override
//                public void onFailure(int errorNo, String strMsg) {
//                    super.onFailure(errorNo, strMsg);
//                    proDia.dismiss();
//                    dgNoticeToast.showFailure("网络异常");
//                }
//            });
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }

    //改后api,上传狗logo
    private void sendImage(Bitmap bm) {

        try{
            //根据图片质量压缩图片
            final Bitmap bitmap = DGImageUtils.compressBitmapByQuality(bm);

            HttpParams params = new HttpParams();
            File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.dg.avater");
            if(!tmpDir.exists()&&tmpDir.isDirectory()){//判断文件目录是否存在
                tmpDir.mkdirs();
            }
            File img = new File(tmpDir.getAbsolutePath() + "dogavater.png");
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            proDia.show();

            params.put("photo", img);
            params.put("dog",1);
            params.put("device", "android");
            params.put("time", System.currentTimeMillis() / 1000 + "");
            kjh.post(APIconfig.API_BASEURL + APIconfig.FILE_UPLOADLOGO, params, new HttpCallBack() {
                @Override
                public void onPreStar() {
                    super.onPreStar();
                }
                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    proDia.dismiss();
                    Log.i("uploadImage", t);
                    UploadLogoResponse uploadLogoResponse = Parser.xmlToBean(UploadLogoResponse.class, t);
                    if(uploadLogoResponse.getCode()==0){
                        dog_logo = uploadLogoResponse.getUrl();
                        riv_dg_regist_avatar.setImageBitmap(bitmap);
                    }else{
                        AppContext.showToast(uploadLogoResponse.getMsg());
                        dgNoticeToast.showFailure(uploadLogoResponse.getError());
                    }
                }
                @Override
                public void onFailure(int errorNo, String strMsg) {
                    super.onFailure(errorNo, strMsg);
                    proDia.dismiss();
                    dgNoticeToast.showFailure("网络异常");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
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
    private View.OnClickListener itemsOnclick = new View.OnClickListener() {

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

    /**
     * 开始编辑图片大小
     * @param uri
     */
    public void startImageZoom(Uri uri)
    {
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
}
