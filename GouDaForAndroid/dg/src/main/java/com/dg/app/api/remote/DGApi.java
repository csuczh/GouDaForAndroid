package com.dg.app.api.remote;

import android.text.TextUtils;

import com.dg.app.bean.DGRegistDog;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.dg.app.AppContext;
import com.dg.app.AppException;
import com.dg.app.api.ApiHttpClient;
//import com.dg.app.bean.EventApplyData;
//import com.dg.app.bean.OwnersList;
//import com.dg.app.bean.Report;
//import com.dg.app.bean.Tweet;
//import com.dg.app.team.bean.Team;
import com.dg.app.util.StringUtils;
import com.dg.app.util.TLog;

import org.kymjs.kjframe.utils.KJLoger;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;

public class DGApi {

    private static final String DEVICE = "android";

    /**
     * 获取狗搭发现列表
     * 类别 （1，2，3）
     * @param page
     * 第几页
     * @param handler
     */
    public static void getGouDaFindList(String device,int user_id, int city_id,int page, AsyncHttpResponseHandler handler) {
        try {

            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("user_id",user_id);
            params.put("city_id", city_id);
            params.put("page", 0);
            ApiHttpClient.post("User/find", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //邀请消息列表，根据环新的id获取用户信息
    public static  void getUsersByEmchat(String device,String emchat_list,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("emchat_list", emchat_list);
            ApiHttpClient.post("user/emchatToUser", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //获取城市列表
    public static  void getCtyList(String device,String province,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("province", province);
            ApiHttpClient.post("user/cityList", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static  void sendLD(String device,int user_id,String content,String deadline,int city_id,String pos,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("user_id", user_id);
            params.put("content",content);
            params.put("deadline",deadline);
            params.put("city_id",city_id);
            params.put("pos",pos);
            ApiHttpClient.post("invite/walkAdd", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static  void sendXQ(String device,int user_id,String content,String dog_variety,String deadline,int city_id,String pos,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("user_id", user_id);
            params.put("content",content);
            params.put("dog_variety",dog_variety);
            params.put("deadline",deadline);
            params.put("city_id",city_id);
            params.put("pos",pos);
            ApiHttpClient.post("invite/dateAdd", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //发送寄养的消息
    public static  void sendJY(String device,int user_id,String content,String deadline,String off_time,int city_id,String pos,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("user_id", user_id);
            params.put("content",content);
            params.put("deadline",deadline);
            params.put("off_time",off_time);
            params.put("city_id",city_id);
            params.put("pos",pos);
            ApiHttpClient.post("invite/fosterAdd", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //发送遛狗的消息
    public static  void getWD(String device,int user_id,int walk_id,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("user_id",user_id);
            params.put("walk_id", walk_id);
            ApiHttpClient.post("invite/walkDetail", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //获得相亲的消息
    public static  void getXQ(String device,int user_id,int date_id,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("user_id",user_id);
            params.put("date_id", date_id);
            ApiHttpClient.post("invite/dateDetail", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //获得寄养的消息
    public static  void getJY(String device,int user_id,int foster_id,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("user_id",user_id);
            params.put("foster_id", foster_id);
            ApiHttpClient.post("invite/fosterDetail", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //发送邀请的内容
    public static void sendAccpet(String device,String type,int user_id,int invite_id,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("user_id", user_id);
            params.put("type",type);
            params.put("invite_id",invite_id);

            ApiHttpClient.post("invite/acceptInvite", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //获得推荐的文章
    public static  void getRecommend(String device,int user_id,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
//            params.put("userid",user_id);
            ApiHttpClient.post("question/hot", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //获得文章分类
    public static  void getKinds(String device,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            ApiHttpClient.post("question/category", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //获得文章列表
    public static  void getTopics(String device,int userid,int category,int page,int num,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("category",category);
            params.put("page",page);
            params.put("num",num);
            ApiHttpClient.post("question/more", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //获得搜索文章列表
    public static  void searchTopics(String device,String search,int page,int num,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("search",search);
            params.put("page",page);
            params.put("num",num);
            ApiHttpClient.post("question/search", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //点赞
    public static  void likeArticle(String device,int userid,int questionid,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("userid",userid);
            params.put("question_id",questionid);
            ApiHttpClient.post("question/like", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //获得搜索文章列表
    public static  void collectArticle(String device,int userid,int questionid,AsyncHttpResponseHandler handler)
    {

        try {
            RequestParams params = new RequestParams();
            params.put("device", "android");
            params.put("userid",userid);
            params.put("question_id",questionid);
            ApiHttpClient.post("question/favorite", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * 关，狗圈API
     */

    /**
     *同城
     * @param userid
     * @param page
     * @param num
     * @param handler
     */
    public static  void sameCityMoments(int userid,int cityid,int page,int num,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("userid",userid);
//            params.put("cityid",0);
            params.put("page",page);
            params.put("num",num);
            ApiHttpClient.post("group/citywide", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     *热门
     * @param userid
     * @param page
     * @param num
     * @param handler
     */
    public static  void hotMoments(int userid,int page,int num,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("userid",userid);
            params.put("page",page);
            params.put("num",num);
            ApiHttpClient.post("group/hot", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 关注
     * @param userid
     * @param page
     * @param num
     * @param handler
     */
    public static  void followedMoments(int userid,int page,int num,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("userid",userid);
            params.put("page",page);
            params.put("num",num);
            ApiHttpClient.post("group/followed", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 喜欢状态
     * @param userid
     * @param moment_id
     * @param handler
     */
    public static  void like(int userid,int moment_id,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("userid",userid);
            params.put("moment_id", moment_id);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("group/like", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 评论消息
     * @param userid
     * @param start_time
     * @param handler
     */
    public static  void replyMessages(int userid,String start_time,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("userid",userid);
            params.put("start_time", "2015-10-14 22:38:33");
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("group/replyMessage", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 喜欢消息
     * @param userid
     * @param start_time
     * @param handler
     */
    public static  void likeMessages(int userid,String start_time,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("userid",userid);
            params.put("start_time", "2015-10-14 22:38:33");
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("group/likeMessage", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 获取动态详情
     * @param moment_id
     * @param handler
     */
    public static  void detailMoment(int userid,int moment_id,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("userid",userid);
            params.put("moment_id",moment_id);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("group/detail", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * 发布状态
     * @param userid
     * @param content
     * @param images
     * @param handler
     */
    public static  void publishMoment(int userid,String content,String images,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("userid",userid);
            params.put("content", content);
            params.put("images", images);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("group/add", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 发布状态
     * @param moment_id
     * @param userid
     * @param content
     * @param replied
     * @param handler
     */
    public static  void publishReply(int moment_id,int moment_pub_id,int userid,String content,int replied,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("moment_id", moment_id);
            params.put("moment_pub_id", moment_pub_id);
            params.put("userid",userid);
            params.put("content", content);
            if(replied!=-1){
                params.put("replied", replied);
            }
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("group/reply", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * 遛狗请求信息
     * @param user_id
     * @param page
     * @param handler
     */
    public static  void liuDogInvitation(int user_id,int page,int page_num,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id",user_id);
            params.put("page", page);
            params.put("page_num", page_num);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("invite/myWalk", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 相亲请求信息
     * @param user_id
     * @param page
     * @param handler
     */
    public static  void dateDogInvitation(int user_id,int page,int page_num,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id",user_id);
            params.put("page", page);
            params.put("page_num", page_num);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("invite/myDate", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 寄养请求信息
     * @param user_id
     * @param page
     * @param handler
     */
    public static  void fosterDogInvitation(int user_id,int page,int page_num,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id",user_id);
            params.put("page", page);
            params.put("page_num", page_num);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("invite/myFoster", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 获取某用户收藏的文章
     * @param user_id
     * @param page
     * @param num
     * @param handler
     */
    public static  void collection(int user_id,int page,int num,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("userid",user_id);
            params.put("page", page);
            params.put("num", num);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("question/myFavorite", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 用户登陆
     * @param phone
     * @param password
     * @param handler
     */
    public static void login(String phone,String password,AsyncHttpResponseHandler handler)
    {

        try {

            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("phone",phone);
            params.put("password", password);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/login", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 个人主页页面
     * @param user_id
     * @param handler
     */
    public static  void userDetail(int user_id,int viewer_id,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id",user_id);
            params.put("viewer_id",viewer_id);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/userDetail", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 狗狗档案页面
     * @param user_id
     * @param handler
     */
    public static  void dogDetail(int user_id,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id",user_id);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/dogDetail", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 某用户自己动态时间线
     * @param userid
     * @param page
     * @param num
     * @param handler
     */
    public static  void myMoment(int userid,int page,int num,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("userid",userid);
            params.put("page",page);
            params.put("num",num);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("group/my", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 用户资料修改页面中用户个人信息修改
     * @param userid
     * @param logo
     * @param nickname
     * @param career
     * @param sex
     * @param age
     * @param tag
     * @param user_images
     * @param handler
     */
    public static  void modifyUserInfo(int userid,String logo,String nickname,String career,int sex,int age,String tag,String user_images,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id",userid);
            params.put("logo",logo);
            params.put("nickname",nickname);
            params.put("career",career);
            params.put("sex",sex);
            params.put("age",age);
            params.put("tag",tag);
            params.put("user_images",user_images);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/modifyUserInfo", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * 狗狗资料修改页面中用户狗狗信息修改
     * @param userid
     * @param dog_logo
     * @param dog_nickname
     * @param dog_variety
     * @param dog_sex
     * @param dog_age
     * @param dog_tag
     * @param dog_images
     * @param handler
     */
    public static  void modifyDogInfo(int userid,String dog_logo,String dog_nickname,String dog_variety,int dog_sex,Double dog_age,String dog_tag,String dog_images,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id",userid);
            params.put("dog_logo",dog_logo);
            params.put("dog_nickname",dog_nickname);
            params.put("dog_variety",dog_variety);
            params.put("dog_sex",dog_sex);
            params.put("dog_age",dog_age);
            if(!"".equals(dog_tag)){
                params.put("dog_tag",dog_tag);
            }
            if(!"".equals(dog_images)){
                params.put("dog_images",dog_images);
            }
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/modifyDogInfo", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 根据类别获取系统内置标签
     * @param type
     * @param handler
     */
    public static  void tags(int type,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("type",type);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/tag", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 获取验证码
     * @param phone
     * @param handler
     */
    public static  void getCode(String phone,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("phone",phone);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/sendCode", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * 获取验证码
     * @param phone
     * @param handler
     */
    public static void getForgetPasswordCode (String phone,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("phone",phone);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/sendForgetPasswordCode", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

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
    public static  void regist(String phone,String password,int area_code,String nickname,int sex,int age,String logo,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("phone",phone);
            params.put("password",password);
            params.put("area_code",area_code);
            params.put("nickname",nickname);
            params.put("sex",sex);
            params.put("age",age);
            if(!TextUtils.isEmpty(logo)){
                params.put("logo",logo);
            }
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/register", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * 在修改区县页面提交
     * @param user_id
     * @param area_code
     * @param handler
     */
    public static  void modifyArea(int user_id,int area_code,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id",user_id);
            params.put("area_code",area_code);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/modifyArea", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     *
     * @param dog
     * @param handler
     */
    public static  void setDogInfo(DGRegistDog dog,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id", dog.getUser_id());
            if(!TextUtils.isEmpty(dog.getDog_logo())){
                params.put("dog_logo", dog.getDog_logo());
            }
            params.put("dog_nickname",dog.getDog_nickname());
            if(!TextUtils.isEmpty(dog.getDog_variety())){
                params.put("dog_variety",dog.getDog_variety());
            }
            params.put("dog_sex",dog.getDog_sex());
            params.put("dog_age",dog.getDog_age());
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/modifyDogInfo", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 意见与反馈页面提交
     * @param user_id
     * @param content
     * @param city_id
     * @param contact
     * @param handler
     */
    public static  void giveAdvice(int user_id,String content,int city_id,String contact,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id", user_id);
            params.put("content", content);
            params.put("city_id",city_id);
            params.put("contact",contact);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/addAdvice", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 在修改密码页面提交
     * @param user_id
     * @param old_pw
     * @param new_pw
     * @param handler
     */
    public static  void modifyPassword(int user_id,String old_pw,String new_pw,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id", user_id);
            params.put("old_pw", old_pw);
            params.put("new_pw",new_pw);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/modifyPassword", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 在忘记密码页面通过验证码验证之后提交
     * @param phone
     * @param code
     * @param new_pw
     * @param handler
     */
    public static  void forgetPassword(String phone,String code,String new_pw,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("phone", phone);
            params.put("code", code);
            params.put("new_pw",new_pw);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/forgetPassword", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * 三种邀请删除
     * @param user_id
     * @param invite_id
     * @param type
     * @param handler
     */
    public static  void deleteInvitation(int user_id,int invite_id,String type,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id", user_id);
            params.put("invite_id", invite_id);
            params.put("type",type);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("invite/delMyInvite", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 验证向用户手机发送的手机验证码是否正确
     * @param phone
     * @param code
     * @param handler
     */
    public static  void validateCode(String phone,String code,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("phone", phone);
            params.put("code", code);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/validateCode", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 根据类别获取系统内置或者用户自定义的狗狗和人的标签
     * @param user_id
     * @param type
     * @param handler
     */
    public static void getTags(int user_id,int type,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
//            params.put("user_id", user_id);
            params.put("type", type);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/tag", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 关注用户
     * @param userid
     * @param followid
     * @param handler
     */
    public static void followUser(int userid,int followid,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("userid", userid);
            params.put("followid", followid);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/follow", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * 获取积分排行榜
     * @param handler
     */
    public static void getRanking(AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/getRanking", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 获取用户今日任务完成情况
     * @param userid
     * @param handler
     */
    public static void getTodayMission(int userid,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("userid", userid);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/todayMission", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 获得查询用户的所有粉丝信息
     * @param user_id
     * @param page
     * @param page_num
     * @param handler
     */
    public static void getFans(int user_id,int page,int page_num,AsyncHttpResponseHandler handler)
    {
        try {
            RequestParams params = new RequestParams();
            params.put("device", DEVICE);
            params.put("user_id", user_id);
            params.put("page", page);
            params.put("page_num", page_num);
            params.put("time", System.currentTimeMillis() / 1000);
            ApiHttpClient.post("user/userFans", params, handler);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
