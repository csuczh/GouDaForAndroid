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
import android.preference.PreferenceActivity;
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
import com.dg.app.bean.DGDogDetailResponse;
import com.dg.app.bean.DGUser;
import com.dg.app.bean.ModifyDogResponse;
import com.dg.app.bean.Tag;
import com.dg.app.bean.UploadImageResponse;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.util.ImageUtils;
import com.dg.app.util.Parser;
import com.dg.app.util.StringUtils;
import com.dg.app.util.XmlUtils;
import com.dg.app.widget.DogAgeWheelDialog;
import com.dg.app.widget.DogSexWheelDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.message.PushAgent;
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

public class DogDetailInfoEditActivity extends Activity implements View.OnClickListener{

    protected static final String TAG = DogDetailInfoEditActivity.class.getSimpleName();

    private static final String ACTION_NAME = "MODIFY_DOGINFO";

    public static final String DOG_TYPE = "DOG_TYPE";

    private PushAgent mPushAgent;

    private DGUser dgUser_origin;

    private DGDogDetailResponse.DogDetail dogDetail;
    private KJBitmap kjb;

    public static final int ACTION_GET_DGNAME = 1100;
    public static final int ACTION_GET_DGTYPE = 2200;
    public static final int ACTION_GET_DGTAGS = 3300;

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

    private ModifyDogInfoReceiver modifyDogInfoReceiver;

    //用户信息
    private int user_id;
    private String dog_logo;
    private String dog_nickname;
    private String dog_variety;
    private int dog_sex;
    private double dog_age;
    private String dog_tag;
    private String dog_images;

    private int[] flags = {0,0,0};

    private String[] m_dog_images = new String[3];

    private Map<Integer,Uri> m_images_map = new HashMap<>();

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
        setContentView(R.layout.activity_dog_detailinfo_edit);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

        initNoticeWidget(this);

        initDogInfo();

        initHttp();
        initTitleBar();
        initView();

        initData();

        mPushAgent = PushAgent.getInstance(this);

        //实例化广播接收器
        modifyDogInfoReceiver = new ModifyDogInfoReceiver();
        //注册广播
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        registerReceiver(modifyDogInfoReceiver, myIntentFilter);

    }

    //初始化狗狗信息
    private void initDogInfo() {
        dog_logo = "";
        dog_nickname =  "";
        dog_variety = "";
        dog_sex = 3;
        dog_age = 0;
        dog_tag = "";
        dog_images = "";
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

        dogDetail = (DGDogDetailResponse.DogDetail) getIntent().getSerializableExtra("dogDetail");
        user_id = AppContext.getInstance().getLoginUid();

        kjb=new KJBitmap();

        if(dogDetail!=null){
            //狗狗头像
            String logo = dogDetail.getDog_logo();
            if(logo!=null&&!"".equals(logo)){
                m_dog_images[0]=logo;
                dog_logo = logo;//原头像
                kjb.display(iv_img_add1, APIconfig.IMG_BASEURL + logo, iv_img_add1.getWidth(), iv_img_add1.getHeight(), R.mipmap.default_image);
            }

            //狗狗图像
            DGDogDetailResponse.DGImgList dgImgList = dogDetail.getDgImgList();
            if(dgImgList!=null){
                List<String> list = dgImgList.getImgList();
                int i = 0;
                for(String img:list){
                    if(i>=0&&i<2){
                        m_dog_images[i+1]=img;
                    }
                    if(i==0){
                        dog_images = img;
                        kjb.display(iv_img_add2,APIconfig.IMG_BASEURL + img,iv_img_add2.getWidth(),iv_img_add2.getHeight(),R.mipmap.default_image);
                    }else if(i==1){
                        dog_images = dog_images+","+img;
                        kjb.display(iv_img_add3,APIconfig.IMG_BASEURL + img,iv_img_add3.getWidth(),iv_img_add3.getHeight(),R.mipmap.default_image);
                    }
                    i++;
                }
            }

            //昵称
            if(!StringUtils.isEmpty(dogDetail.getDog_nickname())){
                dog_nickname = dogDetail.getDog_nickname();
                tv_nickname.setText(dogDetail.getDog_nickname());
            }

            //性别
            if(!StringUtils.isEmpty(dogDetail.getDog_sex()+"")){
                dog_sex = dogDetail.getDog_sex();
                if(dogDetail.getDog_sex()==0){
                    tv_sex.setText("公");
                }else if(dogDetail.getDog_sex()==1){
                    tv_sex.setText("母");
                }
            }

            //年龄
            if(!StringUtils.isEmpty(dogDetail.getDog_age()+"")){
                dog_age = dogDetail.getDog_age();
                tv_age.setText(dogDetail.getDog_age()+"");
            }

            //品种
            if(!StringUtils.isEmpty(dogDetail.getDog_variety())){
                dog_variety = dogDetail.getDog_variety();
                tv_job.setText(dogDetail.getDog_variety());
            }

            //标签
            List<Tag> tag_list = dogDetail.getTags();
            if(tag_list!=null){
                if(tag_list.size()==1){
                    dog_tag = tag_list.get(0).getTag_id()+"";
                    tv_tags.setText(tag_list.get(0).getName());
                }else if(tag_list.size()>1){
                    tv_tags.setText(tag_list.get(0).getName()+"...");

                    dog_tag = tag_list.get(0).getTag_id()+"";
                    for(int i=1;i<tag_list.size();i++){
                        dog_tag = dog_tag+","+tag_list.get(i).getTag_id();
                    }
                }
            }

        }


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageUtils.REQUEST_CODE_FROM_ALBUM) {
            if(resultCode == RESULT_CANCELED) {
                return;
            }
            Uri imageUri = data.getData();
            curr_uri = imageUri;
            updateImgs(curr_uri);

        } else if (requestCode == ImageUtils.REQUEST_CODE_FROM_CAMERA) {
            if(resultCode == RESULT_CANCELED) {
                ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
            } else {
                Uri imageUriCamera = ImageUtils.imageUriFromCamera;
                curr_uri = imageUriCamera;
                updateImgs(curr_uri);
            }

        }else if(requestCode == DogDetailInfoEditActivity.ACTION_GET_DGNAME && resultCode == RESULT_OK){
            dog_nickname = data.getStringExtra(NickNameEditActivity.NICKNAME);
            tv_nickname.setText(dog_nickname);
        }
        else if(requestCode == DogDetailInfoEditActivity.ACTION_GET_DGTYPE && resultCode == RESULT_OK){
            dog_variety = data.getStringExtra(DogTypeEditActivity.DOGVARITY);
            tv_job.setText(dog_variety);
        }
        else if(requestCode == DogDetailInfoEditActivity.ACTION_GET_DGTAGS && resultCode == RESULT_OK){
            dog_tag = data.getStringExtra(TagListActivity.TAGS);
            String tag_str =  data.getStringExtra(TagListActivity.TAGS_STR);
            tv_tags.setText(tag_str);
        }
    }

    //TODO
//    private void updateImgs(Uri curr_uri) {
//        if(curr_uri!=null){
//            Bitmap bitmap = ImageUtils.loadPicasaImageFromGalley(curr_uri,this);
//            if(sel==1){
//                setImage(sel, iv_img_add1, iv_delete_image1, bitmap);
//            }else if(sel==2){
//                setImage(sel,iv_img_add2,iv_delete_image2,bitmap);
//            }else if(sel==3){
//                setImage(sel, iv_img_add3, iv_delete_image3, bitmap);
//            }
//            if(sel>0&&sel<4){
//                m_images_map.put(sel - 1, curr_uri);
//            }
//        }
//
//    }

    private void updateImgs(Uri curr_uri) {
        if(curr_uri!=null){
            if(sel==1){
                setImage(sel, iv_img_add1, iv_delete_image1, curr_uri);
            }else if(sel==2){
                setImage(sel,iv_img_add2,iv_delete_image2,curr_uri);
            }else if(sel==3){
                setImage(sel, iv_img_add3, iv_delete_image3, curr_uri);
            }
            if(sel>0&&sel<4){
                m_images_map.put(sel - 1, curr_uri);
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
            ImageUtils.showImagePickDialog(this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(modifyDogInfoReceiver);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_detailinfo_edit_back:
                this.finish();
                break;
            case R.id.tv_detailinfo_complete:
//                Toast.makeText(this, "完成狗狗信息编辑", Toast.LENGTH_SHORT).show();
                ModifyDogInfo();
                break;
            case R.id.rl_nickname:
//                Toast.makeText(this, "编辑昵称", Toast.LENGTH_SHORT).show();
                intent = new Intent(DogDetailInfoEditActivity.this,NickNameEditActivity.class);
                startActivityForResult(intent, ACTION_GET_DGNAME);
                break;
            case R.id.rl_sex:
//                Toast.makeText(this, "选择性别", Toast.LENGTH_SHORT).show();
                openSexSelectWeel();
                break;
            case R.id.rl_age:
                openAgeSelectWeel();
                break;
            case R.id.rl_job:
                intent = new Intent(DogDetailInfoEditActivity.this,DogTypeEditActivity.class);
                startActivityForResult(intent, ACTION_GET_DGTYPE);
                break;
            case R.id.rl_tags:
                SelectTags();
                break;

            case R.id.iv_img_add1:
//                Toast.makeText(this, "选择图片一", Toast.LENGTH_SHORT).show();
                selectImg(1);
                break;
            case R.id.iv_delete_image1:
//                Toast.makeText(this, "删除图片一", Toast.LENGTH_SHORT).show();
                deleteImage(1);
                break;

            case R.id.iv_img_add2:
//                Toast.makeText(this, "选择图片二", Toast.LENGTH_SHORT).show();
                selectImg(2);
                break;
            case R.id.iv_delete_image2:
//                Toast.makeText(this, "删除图片二", Toast.LENGTH_SHORT).show();
                deleteImage(2);
                break;

            case R.id.iv_img_add3:
//                Toast.makeText(this, "选择图片三", Toast.LENGTH_SHORT).show();
                selectImg(3);
                break;
            case R.id.iv_delete_image3:
//                Toast.makeText(this, "删除图片三", Toast.LENGTH_SHORT).show();
                deleteImage(3);
                break;

        }
    }

    private void SelectTags() {
        try{
            Intent intent = new Intent(DogDetailInfoEditActivity.this,TagListActivity.class);
            intent.putExtra("tags",dog_tag);
            intent.putExtra("type", DogDetailInfoEditActivity.DOG_TYPE);
            startActivityForResult(intent, ACTION_GET_DGTAGS);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void ModifyDogInfo() {

        int code = checkDogInfo();
        if(code==2){
            dgNoticeToast.showFailure("狗狗昵称不能为空");
        }else if(code==3){
            dgNoticeToast.showFailure("请选择狗狗性别");
        }else if(code==4){
            dgNoticeToast.showFailure("请选择狗狗年龄");
        }else if(code==0){
            if(m_images_map.size() > 0) {
                proDia.show();
                //上传图片
                String imgFilePath = null;
                for(Map.Entry<Integer,Uri> img:m_images_map.entrySet()){
                    imgFilePath = ImageUtils.getImageAbsolutePath19(this, img.getValue());
                    sendImage(imgFilePath,img.getKey());
                }
            }else{

                if(StringUtils.isEmpty(dog_logo)){
                    dgNoticeToast.showFailure("请选择一张狗狗logo");
                }else{
                    proDia.show();
                    DGApi.modifyDogInfo(user_id, dog_logo, dog_nickname, dog_variety, dog_sex, dog_age, dog_tag, dog_images, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            ModifyDogResponse modifyDogResponse = XmlUtils.toBean(ModifyDogResponse.class, responseBody);
                            if (modifyDogResponse.getCode() == 0) {
                                proDia.dismiss();
                                dgNoticeToast.showSuccess("修改狗狗信息成功！");

                                DGUser dgUser = AppContext.getInstance().getLoginUser();
                                dgUser.setDog_sex(dog_sex);
                                dgUser.setHave_dog(1);
                                AppContext.getInstance().updateUserInfo(dgUser);



                                deleteTag("DS" + dgUser_origin.getDog_sex());
                                addTag("DS" + dog_sex);
                                finish();
                            } else {
                                proDia.dismiss();
                                dgNoticeToast.showFailure(modifyDogResponse.getMsg());
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

        }
    }

    //检查编辑页信息是否合法
    private int checkDogInfo() {

        if(StringUtils.isEmpty(dog_nickname)){
            return 2;
        }

        if(dog_sex !=0&&dog_sex !=1){
            return 3;
        }

        if(dog_age==0){
            return 4;
        }

        return 0;
    }

    private void openAgeSelectWeel() {
        //TODO
        //1、打开滚动轮选择控件
        //2、根据选择的值设置EditText值
        try {
            DogAgeWheelDialog ageCustomDialog = new DogAgeWheelDialog(this, new DogAgeWheelDialog.OnWhellSetData() {
                @Override
                public void setWhellData(Integer min) {
                    tv_age.setText(min+"");
                    dog_age = min;
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
            DogSexWheelDialog sexCustomDialog = new DogSexWheelDialog(this, new DogSexWheelDialog.OnWhellSetData() {
                @Override
                public void setWhellData(String sex) {
                    tv_sex.setText(sex);
                    if(sex=="公"){
                        dog_sex = 0;
                    }else if(sex=="母"){
                        dog_sex = 1;
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
                    public void onPreStar() {
                        super.onPreStar();
                    }

                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
//                        Toast.makeText(DogDetailInfoEditActivity.this, t, Toast.LENGTH_SHORT).show();
                        Log.i("uploadImage", t);
                        UploadImageResponse uploadImageResponse = Parser.xmlToBean(UploadImageResponse.class, t);
                        int code = uploadImageResponse.getCode();
                        String msg = uploadImageResponse.getMsg();
                        if(code==0){
                            String url = uploadImageResponse.getUrl();
                            if(index>=0&&index<3){
                                m_dog_images[index] = url;
                            }
                            addImageCount();
                            //图片全部上传成功
                            if(getImageCount()==m_images_map.size()){
                                Intent intent = new Intent(ACTION_NAME);
                                intent.putExtra("dog_nickname",dog_nickname);
                                intent.putExtra("dog_variety",dog_variety);
                                intent.putExtra("dog_sex",dog_sex);
                                intent.putExtra("dog_age",dog_age);
                                intent.putExtra("dog_tag",dog_tag);
                                if(m_dog_images[0]!=null&&!"".equals(m_dog_images[0])){
                                    dog_logo = m_dog_images[0];
                                }
                                if(m_dog_images[1]!=null&&!"".equals(m_dog_images[1])){
                                    dog_images = m_dog_images[1];
                                }
                                if(m_dog_images[2]!=null&&!"".equals(m_dog_images[2])){
                                    dog_images = dog_images+","+m_dog_images[2];
                                }
                                intent.putExtra("dog_logo",dog_logo);
                                intent.putExtra("dog_images", dog_images);

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
    class ModifyDogInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                String action = intent.getAction();
                if(action.equals(ACTION_NAME)){
                    String dog_logo = (String) intent.getExtras().get("dog_logo");
                    String dog_nickname = (String) intent.getExtras().get("dog_nickname");
                    String dog_variety = (String) intent.getExtras().get("dog_variety");
                    final Integer dog_sex = (Integer) intent.getExtras().get("dog_sex");
                    Double dog_age = (Double) intent.getExtras().get("dog_age");
                    String dog_tag = (String) intent.getExtras().get("dog_tag");
                    String dog_images = (String) intent.getExtras().get("dog_images");

                    AppContext.showToast(dog_logo+"\n"+dog_nickname+"\n"+dog_variety+"\n"+dog_sex+"\n"+dog_age+"\n"+dog_tag+"\n"+dog_images);

                    AppContext.showToast("ModifyDogInfoReceiver");

                    DGApi.modifyDogInfo(user_id, dog_logo, dog_nickname, dog_variety, dog_sex, dog_age, dog_tag, dog_images, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            ModifyDogResponse modifyDogResponse = XmlUtils.toBean(ModifyDogResponse.class, responseBody);
                            if (modifyDogResponse.getCode() == 0) {
                                proDia.dismiss();
                                dgNoticeToast.showSuccess("修改狗狗信息成功！");

                                DGUser dgUser = AppContext.getInstance().getLoginUser();
                                dgUser.setDog_sex(dog_sex);
                                dgUser.setHave_dog(1);
                                AppContext.getInstance().updateUserInfo(dgUser);

                                finish();
                            } else {
                                proDia.dismiss();
                                dgNoticeToast.showFailure(modifyDogResponse.getMsg());
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
