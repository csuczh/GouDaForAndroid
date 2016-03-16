package com.dg.app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.CommonResponse;
import com.dg.app.bean.DGUser;
import com.dg.app.bean.DGUserDetailResponse;
import com.dg.app.bean.Tag;
import com.dg.app.bean.UploadImageResponse;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.util.ImageUtils;
import com.dg.app.util.Parser;
import com.dg.app.util.StringUtils;
import com.dg.app.util.XmlUtils;
import com.dg.app.widget.UserAgeWheelDialog;
import com.dg.app.widget.UserSexWheelDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.message.PushAgent;
import com.umeng.message.proguard.S;
import com.umeng.message.tag.TagManager;

import org.apache.http.Header;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDetailInfoEditActivity extends Activity implements View.OnClickListener {

    protected static final String TAG = MyDetailInfoEditActivity.class.getSimpleName();

    private static final String ACTION_NAME = "MODIFY_USERINFO";

    public static final String USER_TYPE = "USER_TYPE";

    private PushAgent mPushAgent;

    private DGUser dgUser_origin;

    private DGUserDetailResponse.UserDetail userDetail;
    private KJBitmap kjb;

    public static final int ACTION_GET_NAME = 1000;
    public static final int ACTION_GET_JOB = 2000;
    public static final int ACTION_GET_TAGS = 3000;

    private LinearLayout titlebar_my_detailinfo_edit;
    private ImageView iv_detailinfo_edit_back;
    private TextView tv_detailinfo_complete;

    private ImageView iv_img_add1;
    private ImageView iv_delete_image1;

    private ImageView iv_img_add2;
    private ImageView iv_delete_image2;

    private ImageView iv_img_add3;
    private ImageView iv_delete_image3;

    private RelativeLayout rl_nickname;
    private RelativeLayout rl_sex;
    private RelativeLayout rl_age;
    private RelativeLayout rl_job;
    private RelativeLayout rl_tags;

    private TextView tv_nickname;
    private TextView tv_sex;
    private TextView tv_age;
    private TextView tv_job;
    private TextView tv_tags;

    private KJHttp kjh;

    private ModifyUserInfoReceiver modifyUserInfoReceiver;

    //用户信息
    private int user_id;
    private String m_logo;
    private String m_nickname;
    private String m_career;
    private int m_sex;
    private int m_age;
    private String m_tag;
    private String m_images;

    private String[] m_user_images = new String[3];

    private Map<Integer,Uri> m_images_map = new HashMap<>();

    private int[] flags = {0,0,0};

    private int sel;

    private Uri curr_uri;

    private int imageCount = 0;

    //提示组件
    private DGNoticeToast dgNoticeToast;
    private ProgressDialog proDia;

    public synchronized void addImageCount(){
        imageCount++;
    }

    public synchronized int getImageCount(){
        return imageCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detailinfo_edit);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

        initNoticeWidget(this);

        initHttp();
        initTitleBar();
        initView();

        initData();

        mPushAgent = PushAgent.getInstance(this);

        //实例化广播接收器
        modifyUserInfoReceiver = new ModifyUserInfoReceiver();
        //注册广播
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        registerReceiver(modifyUserInfoReceiver, myIntentFilter);
    }

    /**
     * 初始化提示组件
     * @param context
     */
    private void initNoticeWidget(Context context) {
        proDia = new ProgressDialog(context);
        dgNoticeToast = new DGNoticeToast(context);
    }

    private void initData() {
        dgUser_origin = AppContext.getInstance().getLoginUser();

        userDetail = (DGUserDetailResponse.UserDetail) getIntent().getSerializableExtra("userDetail");
        user_id = AppContext.getInstance().getLoginUid();
        if(user_id==0){
            Intent intent = new Intent(MyDetailInfoEditActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        if(userDetail==null){
            finish();
            return;
        }

        kjb=new KJBitmap();

        //头像
        String user_logo = userDetail.getLogo();
        if(user_logo!=null&&!"".equals(user_logo)){
            m_user_images[0]=user_logo;
            m_logo = user_logo;//原头像
            kjb.display(iv_img_add1,APIconfig.IMG_BASEURL + user_logo,iv_img_add1.getWidth(),iv_img_add1.getHeight(),R.mipmap.default_image);
        }

        //图像
        DGUserDetailResponse.ImgList imgList = userDetail.getImgList();
        if(imgList!=null){
            List<String> list = imgList.getImgList();
            int i = 0;
            for(String img:list){
                if(i>=0&&i<2){
                    m_user_images[i+1]=img;
                }
                if(i==0){
                    m_images = img;
                    kjb.display(iv_img_add2,APIconfig.IMG_BASEURL + img,iv_img_add2.getWidth(),iv_img_add2.getHeight(),R.mipmap.default_image);
                }else if(i==1){
                    m_images = m_images+","+img;
                    kjb.display(iv_img_add3,APIconfig.IMG_BASEURL + img,iv_img_add3.getWidth(),iv_img_add3.getHeight(),R.mipmap.default_image);
                }
                i++;
            }
        }


        //昵称
        if(!StringUtils.isEmpty(userDetail.getNickname())){
            m_nickname = userDetail.getNickname();
            tv_nickname.setText(userDetail.getNickname());
        }

        //性别
        if(!StringUtils.isEmpty(userDetail.getSex()+"")){
            m_sex = userDetail.getSex();
            if(userDetail.getSex()==0){
                tv_sex.setText("男");
            }else if(userDetail.getSex()==1){
                tv_sex.setText("女");
            }
        }

        //年龄
        if(!StringUtils.isEmpty(userDetail.getAge()+"")){
            m_age = userDetail.getAge();
            tv_age.setText(userDetail.getAge()+"");
        }

        //工作
        if(!StringUtils.isEmpty(userDetail.getCareer())){
            m_career = userDetail.getCareer();
            tv_job.setText(userDetail.getCareer());
        }

        //标签
        List<Tag> tag_list = userDetail.getTags();
        if(tag_list!=null){
            if(tag_list.size()==1){
                m_tag = tag_list.get(0).getTag_id()+"";
                tv_tags.setText(tag_list.get(0).getName());
            }else if(tag_list.size()>1){
                tv_tags.setText(tag_list.get(0).getName()+"...");

                m_tag = tag_list.get(0).getTag_id()+"";
                for(int i=1;i<tag_list.size();i++){
                    m_tag = m_tag+","+tag_list.get(i).getTag_id();
                }
            }
        }

    }

    private void initView() {
        iv_img_add1 = (ImageView) findViewById(R.id.iv_img_add1);
        iv_delete_image1 = (ImageView) findViewById(R.id.iv_delete_image1);

        iv_img_add2 = (ImageView) findViewById(R.id.iv_img_add2);
        iv_delete_image2 = (ImageView) findViewById(R.id.iv_delete_image2);

        iv_img_add3 = (ImageView) findViewById(R.id.iv_img_add3);
        iv_delete_image3 = (ImageView) findViewById(R.id.iv_delete_image3);

        iv_img_add1.setOnClickListener(this);
        iv_delete_image1.setOnClickListener(this);

        iv_img_add2.setOnClickListener(this);
        iv_delete_image2.setOnClickListener(this);

        iv_img_add3.setOnClickListener(this);
        iv_delete_image3.setOnClickListener(this);


        rl_nickname = (RelativeLayout) findViewById(R.id.rl_nickname);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        rl_age = (RelativeLayout) findViewById(R.id.rl_age);
        rl_job = (RelativeLayout) findViewById(R.id.rl_job);
        rl_tags = (RelativeLayout) findViewById(R.id.rl_tags);

        rl_nickname.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_age.setOnClickListener(this);
        rl_job.setOnClickListener(this);
        rl_tags.setOnClickListener(this);

        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_job = (TextView) findViewById(R.id.tv_job);
        tv_tags = (TextView) findViewById(R.id.tv_tags);
    }

    private void initTitleBar() {
        titlebar_my_detailinfo_edit = (LinearLayout) findViewById(R.id.titlebar_my_detailinfo_edit);

        iv_detailinfo_edit_back = (ImageView) titlebar_my_detailinfo_edit.findViewById(R.id.iv_detailinfo_edit_back);
        iv_detailinfo_edit_back.setOnClickListener(this);
        tv_detailinfo_complete = (TextView) titlebar_my_detailinfo_edit.findViewById(R.id.tv_detailinfo_complete);
        tv_detailinfo_complete.setOnClickListener(this);

    }

    private void initHttp() {
        //配置http连接属性
        HttpConfig config = new HttpConfig();
        config.cacheTime = 0;
        config.useDelayCache = false;
        kjh = new KJHttp(config);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(modifyUserInfoReceiver);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_detailinfo_edit_back:
                this.finish();
                break;
            case R.id.tv_detailinfo_complete:
                ModifyUserInfo();
                break;
            case R.id.rl_nickname:
                intent = new Intent(MyDetailInfoEditActivity.this,NickNameEditActivity.class);
                startActivityForResult(intent, ACTION_GET_NAME);
                break;
            case R.id.rl_sex:
                openSexSelectWeel();
                break;
            case R.id.rl_age:
                openAgeSelectWeel();
                break;
            case R.id.rl_job:
                intent = new Intent(MyDetailInfoEditActivity.this,UserJobEditActivity.class);
                startActivityForResult(intent, ACTION_GET_JOB);
                break;
            case R.id.rl_tags:
                SelectTags();
                break;
            case R.id.iv_img_add1:
                selectImg(1);
                break;
            case R.id.iv_delete_image1:
                deleteImage(1);
                break;
            case R.id.iv_img_add2:
                selectImg(2);
                break;
            case R.id.iv_delete_image2:
                deleteImage(2);
                break;
            case R.id.iv_img_add3:
                selectImg(3);
                break;
            case R.id.iv_delete_image3:
                deleteImage(3);
                break;

        }
    }

    //进入标签选择界面
    private void SelectTags() {
        try{

            Intent intent = new Intent(MyDetailInfoEditActivity.this,TagListActivity.class);
            intent.putExtra("tags",m_tag);
            intent.putExtra("type",MyDetailInfoEditActivity.USER_TYPE);
            startActivityForResult(intent, ACTION_GET_TAGS);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //修改用户信息
    private void ModifyUserInfo() {

        proDia.show();

        if(m_images_map.size() > 0) {
            //上传图片
            String imgFilePath = null;
            for(Map.Entry<Integer,Uri> img:m_images_map.entrySet()){
                imgFilePath = ImageUtils.getImageAbsolutePath19(this, img.getValue());
                sendImage(imgFilePath,img.getKey());
            }
        }else{
            DGApi.modifyUserInfo(user_id, m_logo, m_nickname, m_career, m_sex, m_age, m_tag, m_images, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    CommonResponse commonResponse = XmlUtils.toBean(CommonResponse.class,responseBody);
                    if(commonResponse.getCode()==0){
                        proDia.dismiss();
                        dgNoticeToast.showSuccess("修改个人信息成功！");

                        deleteTag("US" + dgUser_origin.getSex());
                        addTag("US" + m_sex);

                        finish();
                    }else{
                        proDia.dismiss();
                        dgNoticeToast.showFailure(commonResponse.getMsg());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    proDia.dismiss();
                    dgNoticeToast.showFailure("网络异常");
                }
            });

        }

    }

    private void openAgeSelectWeel() {
        //TODO
        //1、打开滚动轮选择控件
        //2、根据选择的值设置EditText值
        try {
            UserAgeWheelDialog ageCustomDialog = new UserAgeWheelDialog(this, new UserAgeWheelDialog.OnWhellSetData() {
                @Override
                public void setWhellData(Integer min) {
                    tv_age.setText(min+"");
                    m_age = min;
                }
            });
            //设置对话框的位置
            WindowManager m = this.getWindowManager();
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

    private void openSexSelectWeel() {
        //1、打开滚动轮选择控件
        //2、根据选择的值设置EditText值
        try {
            UserSexWheelDialog sexCustomDialog = new UserSexWheelDialog(this, new UserSexWheelDialog.OnWhellSetData() {
                @Override
                public void setWhellData(String sex) {
                    tv_sex.setText(sex);
                    if(sex=="男"){
                        m_sex = 0;
                    }else if(sex=="女"){
                        m_sex = 1;
                    }
                }
            });
            //设置对话框的位置
            WindowManager m = this.getWindowManager();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageUtils.REQUEST_CODE_FROM_ALBUM) {
            if(resultCode == RESULT_CANCELED) {
                return;
            }
            Uri imageUri = data.getData();
            updateImgs(imageUri);

        } else if (requestCode == ImageUtils.REQUEST_CODE_FROM_CAMERA) {
            if(resultCode == RESULT_CANCELED) {
                ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
            } else {
                Uri imageUriCamera = ImageUtils.imageUriFromCamera;
                updateImgs(imageUriCamera);
            }

        }else if(requestCode == MyDetailInfoEditActivity.ACTION_GET_NAME && resultCode == RESULT_OK){
            m_nickname = data.getStringExtra(NickNameEditActivity.NICKNAME);

            tv_nickname.setText(m_nickname);
        }
        else if(requestCode == MyDetailInfoEditActivity.ACTION_GET_JOB && resultCode == RESULT_OK){
            m_career = data.getStringExtra(UserJobEditActivity.USERJOB);
            tv_job.setText(m_career);
        }
        else if(requestCode == MyDetailInfoEditActivity.ACTION_GET_TAGS && resultCode == RESULT_OK){
            m_tag = data.getStringExtra(TagListActivity.TAGS);
            String tag_str =  data.getStringExtra(TagListActivity.TAGS_STR);
            tv_tags.setText(tag_str);
        }
    }

    //更新图片
//    private void updateImgs(Uri curr_uri) {
//        if(curr_uri!=null){
//            Bitmap bitmap = ImageUtils.loadPicasaImageFromGalley(curr_uri,this);
//            if(sel==1){
//                setImage(sel,iv_img_add1,iv_delete_image1,bitmap);
//            }else if(sel==2){
//                setImage(sel,iv_img_add2,iv_delete_image2,bitmap);
//            }else if(sel==3){
//                setImage(sel,iv_img_add3,iv_delete_image3,bitmap);
//            }
//            if(sel>0&&sel<4){
//                m_images_map.put(sel-1,curr_uri);
//            }
//        }
//
//    }

    private void updateImgs(Uri curr_uri) {
        if(curr_uri!=null){
            if(sel==1){
                setImage(sel,iv_img_add1,iv_delete_image1,curr_uri);
            }else if(sel==2){
                setImage(sel,iv_img_add2,iv_delete_image2,curr_uri);
            }else if(sel==3){
                setImage(sel,iv_img_add3,iv_delete_image3,curr_uri);
            }
            if(sel>0&&sel<4){
                m_images_map.put(sel-1,curr_uri);
            }
        }

    }

    //设置图片
//    private void setImage(final int index, final ImageView iv_img_add, final ImageView iv_delete_image,Bitmap bitmap) {
//        if(flags[index-1]==0){
//            iv_img_add.setImageBitmap(bitmap);
//            iv_delete_image.setVisibility(View.VISIBLE);
//            iv_delete_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    deleteImage(index);
//                }
//            });
//            iv_img_add.setClickable(false);
//            flags[index-1]=1;
//        }
//    }

    private void setImage(final int index, final ImageView iv_img_add, final ImageView iv_delete_image,Uri bitmap) {
        if(flags[index-1]==0){
            iv_img_add.setImageURI(bitmap);
            iv_delete_image.setVisibility(View.VISIBLE);
            iv_delete_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteImage(index);
                }
            });
            iv_img_add.setClickable(false);
            flags[index-1]=1;
        }
    }

    //删除图片
    private void deleteImage(int index) {
        ImageView iv_img_add = null;
        ImageView iv_delete_image = null;
        if(index<1 || index >3){
            return ;
        }
        if(index==1){
            iv_img_add = iv_img_add1;
            iv_delete_image = iv_delete_image1;
        }else if(index==2){
            iv_img_add = iv_img_add2;
            iv_delete_image = iv_delete_image2;
        }else if(index==3){
            iv_img_add = iv_img_add3;
            iv_delete_image = iv_delete_image3;
        }
        if(flags[index-1]==1){
            iv_img_add.setImageResource(R.drawable.selector_image_add);
            iv_delete_image.setVisibility(View.GONE);
            iv_img_add.setClickable(true);
            flags[index-1] = 0;
        }
    }

    //选择图片
    public void selectImg(int index){
        if(index>0 && index<4){
            sel = index;
            ImageUtils.showImagePickDialog(MyDetailInfoEditActivity.this);
        }
    }

    //上传图片
    private void sendImage(String path, final int index) {
        try{
            File img = new File(path);
            if(img.exists()){
                HttpParams params = new HttpParams();
                params.put("photo",img);
                params.put("device", "android");
                params.put("time", System.currentTimeMillis() / 1000 +"");
                kjh.post(APIconfig.API_BASEURL + APIconfig.FILE_UPLOADIAMGE, params, new HttpCallBack() {

                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
//                        Toast.makeText(MyDetailInfoEditActivity.this, t, Toast.LENGTH_SHORT).show();
                        Log.i("uploadImage", t);
                        UploadImageResponse uploadImageResponse = Parser.xmlToBean(UploadImageResponse.class, t);
                        int code = uploadImageResponse.getCode();
                        String msg = uploadImageResponse.getMsg();
                        if(code==0){
                            String url = uploadImageResponse.getUrl();

                            if(index>=0&&index<3){
                                m_user_images[index] = url;
                            }
                            addImageCount();
                            //图片全部上传成功
                            if(getImageCount()==m_images_map.size()){
//                                Toast.makeText(MyDetailInfoEditActivity.this, "图片全部上传成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ACTION_NAME);
                                intent.putExtra("m_nickname",m_nickname);
                                intent.putExtra("m_career",m_career);
                                intent.putExtra("m_sex",m_sex);
                                intent.putExtra("m_age",m_age);
                                intent.putExtra("m_tag",m_tag);

                                if(m_user_images[0]!=null&&!"".equals(m_user_images[0])){
                                    m_logo = m_user_images[0];
                                }
                                if(m_user_images[1]!=null&&!"".equals(m_user_images[1])){
                                    m_images = m_user_images[1];
                                }
                                if(m_user_images[2]!=null&&!"".equals(m_user_images[2])){
                                    m_images = m_images+","+m_user_images[2];
                                }
                                intent.putExtra("m_logo",m_logo);
                                intent.putExtra("m_user_images",m_images);
                                sendBroadcast(intent);
                            }
                        }else{
                            proDia.dismiss();
                            dgNoticeToast.showFailure(msg + "\n图片上传失败");
                        }

                        Log.i("uploadImage", uploadImageResponse.getUrl());
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        super.onFailure(errorNo, strMsg);
                        proDia.dismiss();
                        dgNoticeToast.showFailure(strMsg);
                    }
                });
            }else {
                proDia.dismiss();
                dgNoticeToast.showFailure("获取图片失败，请重新选择");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * //发布状态广播接受器
     */
    class ModifyUserInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try{

                String action = intent.getAction();
                if(action.equals(ACTION_NAME)){
                    final String m_logo = (String) intent.getExtras().get("m_logo");
                    final String m_nickname = (String) intent.getExtras().get("m_nickname");
                    final String m_career = (String) intent.getExtras().get("m_career");
                    final Integer m_sex = (Integer) intent.getExtras().get("m_sex");
                    final Integer m_age = (Integer) intent.getExtras().get("m_age");
                    String m_tag = (String) intent.getExtras().get("m_tag");
                    String m_user_images = (String) intent.getExtras().get("m_user_images");

                    DGApi.modifyUserInfo(user_id, m_logo, m_nickname, m_career, m_sex, m_age, m_tag, m_user_images, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            CommonResponse commonResponse = XmlUtils.toBean(CommonResponse.class,responseBody);
                            if(commonResponse.getCode()==0){
                                proDia.dismiss();
                                dgNoticeToast.showSuccess("修改个人信息成功！");

                                DGUser dgUser = AppContext.getInstance().getLoginUser();
                                dgUser.setUserlogo(m_logo);
                                dgUser.setNickname(m_nickname);
                                dgUser.setCareer(m_career);
                                dgUser.setSex(m_sex);
                                dgUser.setAge(m_age);
                                AppContext.getInstance().updateUserInfo(dgUser);

                                deleteTag("US" + dgUser_origin.getSex());
                                addTag("US" + m_sex);
                                finish();
                            }else{
                                proDia.dismiss();
                                dgNoticeToast.showFailure(commonResponse.getMsg());
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            proDia.dismiss();
                            dgNoticeToast.showFailure("网络异常");
                        }
                    });
                }

            }catch (Exception e){
                e.printStackTrace();
            }

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
    /**
     * 添加标签异步任务
     */
    class AddTagTask extends AsyncTask<Void, Void, String> {

        String tagString;
        String[] tags;
        public AddTagTask(String tag) {
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
        protected void onPostExecute(String result) { }
    }

    // sample code to add tags for the device / user
    private void deleteTag(String tag) {
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

    /**
     * 添加标签异步任务
     */
    class DeleteTagTask extends AsyncTask<Void, Void, String> {

        String tagString;
        String[] tags;
        public DeleteTagTask(String tag) {
            tagString = tag;
            tags = tagString.split(",");
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                TagManager.Result result = mPushAgent.getTagManager().delete(tags);
                Log.d(TAG, result.toString());
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Fail";
        }

        @Override
        protected void onPostExecute(String result) { }
    }


}
