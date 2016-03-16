package com.dg.app.service;

import android.app.Activity;
import android.graphics.BitmapFactory;

import com.dg.app.R;
import com.dg.app.api.APIconfig;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.media.MailShareContent;
//import com.umeng.socialize.media.QQShareContent;
//import com.umeng.socialize.media.QZoneShareContent;
//import com.umeng.socialize.media.SinaShareContent;
//import com.umeng.socialize.media.SmsShareContent;
//import com.umeng.socialize.media.TencentWbShareContent;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.media.UMVideo;
//import com.umeng.socialize.media.UMusic;
//import com.umeng.socialize.sso.QZoneSsoHandler;
//import com.umeng.socialize.sso.SinaSsoHandler;
//import com.umeng.socialize.sso.TencentWBSsoHandler;
//import com.umeng.socialize.sso.UMQQSsoHandler;
//import com.umeng.socialize.weixin.controller.UMWXHandler;
//import com.umeng.socialize.weixin.media.CircleShareContent;
//import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by xianxiao on 2015/12/5.
 */
public class UmengShareService {

//    private static UMSocialService mController = null;
//    private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;
//
//    public static UMSocialService initUmengShare(){
//        if(mController==null){
//            mController = UMServiceFactory.getUMSocialService(APIconfig.DESCRIPTOR);
//        }
//        return mController;
//    }
//
//    /**
//     * 配置分享平台参数</br>
//     */
//    public static void configPlatforms(Activity activity) {
//        // 添加新浪SSO授权
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//
//        // 添加QQ、QZone平台
//        addQQQZonePlatform(activity);
//
//        // 添加微信、微信朋友圈平台
//        addWXPlatform(activity);
//    }
//
//    /**
//     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
//     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
//     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
//     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
//     * @return
//     */
//    private static void addQQQZonePlatform(Activity activity){
//        // 添加QQ支持, 并且设置QQ分享内容的target url
//        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity,
//                APIconfig.QQQZone_appId, APIconfig.QQQZone_appKey);
//        qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
//        qqSsoHandler.addToSocialSDK();
//
//        // 添加QZone平台
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, APIconfig.QQQZone_appId, APIconfig.QQQZone_appKey);
//        qZoneSsoHandler.addToSocialSDK();
//    }
//
//    /**
//     * @功能描述 : 添加微信平台分享
//     * @return
//     */
//    private static void addWXPlatform(Activity activity) {
//
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(activity, APIconfig.WX_appId, APIconfig.WX_appSecret);
//        wxHandler.addToSocialSDK();
//
//        // 支持微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(activity, APIconfig.WX_appId, APIconfig.WX_appSecret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//    }
//
//    /**
//     * 根据不同的平台设置不同的分享内容</br>
//     */
//    public static void setShareContent(Activity activity) {
//
//        // 配置SSO
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
//
//
//        //图片分享
//        UMImage urlImage = new UMImage(activity,
//                "http://www.umeng.com/images/pic/social/integrated_3.png");
//
//        UMImage qzoneImage = new UMImage(activity,
//                "http://www.umeng.com/images/pic/social/integrated_3.png");
//        qzoneImage
//                .setTargetUrl("http://www.umeng.com/images/pic/social/integrated_3.png");
//
//        // 视频分享
//        UMVideo video = new UMVideo(
//                "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
//        video.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
//        video.setTitle("友盟社会化组件视频");
//        video.setThumb(urlImage);
//
//        //音乐分享
//        UMusic uMusic = new UMusic(
//                "http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
//        uMusic.setAuthor("umeng");
//        uMusic.setTitle("天籁之音");
//        uMusic.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");
//
//        // UMEmoji emoji = new UMEmoji(getActivity(),
//        // "http://www.pc6.com/uploadimages/2010214917283624.gif");
//        // UMEmoji emoji = new UMEmoji(getActivity(),
//        // "/storage/sdcard0/emoji.gif");
//
//        WeiXinShareContent weixinContent = new WeiXinShareContent();
//        weixinContent
//                .setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-微信。http://www.umeng.com/social");
//        weixinContent.setTitle("友盟社会化分享组件-微信");
//        weixinContent.setTargetUrl("http://www.umeng.com/social");
//        weixinContent.setShareMedia(urlImage);
//        mController.setShareMedia(weixinContent);
//
//        // 设置朋友圈分享的内容
//        CircleShareContent circleMedia = new CircleShareContent();
//        circleMedia
//                .setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-朋友圈。http://www.umeng.com/social");
//        circleMedia.setTitle("友盟社会化分享组件-朋友圈");
//        circleMedia.setShareMedia(urlImage);
//        circleMedia.setShareMedia(uMusic);
//        circleMedia.setShareMedia(video);
//        circleMedia.setTargetUrl("http://www.umeng.com/social");
//        mController.setShareMedia(circleMedia);
//
//
//        // 设置QQ空间分享内容
//        QZoneShareContent qzone = new QZoneShareContent();
//        qzone.setShareContent("share test");
//        qzone.setTargetUrl("http://www.umeng.com");
//        qzone.setTitle("QZone title");
//        qzone.setShareMedia(urlImage);
//        qzone.setShareMedia(uMusic);
//        mController.setShareMedia(qzone);
//
//        video.setThumb(new UMImage(activity, BitmapFactory.decodeResource(
//                activity.getResources(), R.mipmap.ic_dg)));
//
//        QQShareContent qqShareContent = new QQShareContent();
//        qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
//        qqShareContent.setTitle("hello, title");
//        qqShareContent.setShareMedia(urlImage);
//        qqShareContent.setTargetUrl("http://www.umeng.com/social");
//        mController.setShareMedia(qqShareContent);
//
//        // 视频分享
//        UMVideo umVideo = new UMVideo(
//                "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
//        umVideo.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
//        umVideo.setTitle("友盟社会化组件视频");
//
//        TencentWbShareContent tencent = new TencentWbShareContent();
//        tencent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-腾讯微博。http://www.umeng.com/social");
//        // 设置tencent分享内容
//        mController.setShareMedia(tencent);
//
//        UMImage localImage = new UMImage(activity, R.drawable.ic_launcher);
//        // 设置邮件分享内容， 如果需要分享图片则只支持本地图片
//        MailShareContent mail = new MailShareContent(localImage);
//        mail.setTitle("share form umeng social sdk");
//        mail.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-email。http://www.umeng.com/social");
//        // 设置tencent分享内容
//        mController.setShareMedia(mail);
//
//        // 设置短信分享内容
//        SmsShareContent sms = new SmsShareContent();
//        sms.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-短信。http://www.umeng.com/social");
//        // sms.setShareImage(urlImage);
//        mController.setShareMedia(sms);
//
//        SinaShareContent sinaContent = new SinaShareContent();
//        sinaContent
//                .setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-新浪微博。http://www.umeng.com/social");
//        sinaContent.setShareImage( new UMImage( activity, R.drawable.actionbar_back_indicator));
//        mController.setShareMedia(sinaContent);
//
//    }

}
