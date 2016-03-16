package com.dg.app.api;

public interface APIconfig {
	
	public static final String PUBLIC_KEY_STR=
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKQ0PEj950V7LTbmjLO7puKIyO"
					+"0+VCTcv7WJW32dtssJU98gUUe+jsWkw8XzUcOTue6Orc2W20MjSoDzCxyvRg+BR8"
					+"TcZmncQJk9KZeFjvWnoKh8OmuiFJPkWRtg0DKkU7Gew7GNvkdiEZqj/+Sq71Hq3v"
					+"GoYEyo1nhOputCcL+QIDAQAB";

	public static final String IMG_BASEURL = "http://www.goumatch.com/";

	public static final String API_BASEURL = "http://www.goumatch.com/Api/";


	//友盟分享配置
	public static final String DESCRIPTOR = "com.umeng.share";

	public static final String QQQZone_appId = "100424468";
	public static final String QQQZone_appKey = "c7394704798a158208a74ab60104f0ba";

	// 注意：在微信授权的时候，必须传递appSecret
	// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
	public static final String WX_appId = "wx967daebe835fbeac";
	public static final String WX_appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";


	
	//用户api
	public static final String USER_REGIST = "user/register";
	public static final String USER_LOGIN = "user/login";
	public static final String USER_FOLLOW = "user/follow";
	public static final String USER_CITYLIST = "user/cityList";
	public static final String USER_REPORT = "user/report";
	public static final String USER_TAG = "user/tag";
	public static final String USER_MODIFY_PWD = "user/modifyPassword";
	public static final String USER_MODIFY_USERINFO = "user/modifyUserInfo";
	public static final String USER_MODIFY_DOGINFO = "user/modifyDogInfo";
	public static final String USER_MODIFY_IMAGE = "user/modifyImage";
	public static final String USER_ADD_IMAGE = "user/addImage";
	public static final String USER_DELETE_IMAGE = "user/delImage";
	public static final String USER_ADD_ADVICE = "user/addAdvice";
	public static final String USER_USER_DETAIL = "user/userDetail";
	public static final String USER_DOG_DETAIL = "user/dogDetail";
	
	//狗圈api
	public static final String GROUP_DETAIL = "group/detail";
	public static final String GROUP_REPLYLIST = "group/replyList";
	public static final String GROUP_ADD  = "group/add";
	public static final String GROUP_DELETE = "group/delete";
	public static final String GROUP_LIKE = "group/like";
	public static final String GROUP_REPLY = "group/reply";
	public static final String GROUP_CITYWIDE = "group/citywide";
	public static final String GROUP_HOT = "group/hot";
	public static final String GROUP_FOLLOWED = "group/followed";
	public static final String GROUP_MY = "group/my";
	public static final String GROUP_REPLY_MESSAGES = "group/replyMessage";
	public static final String GROUP_LIKE_MESSAGES = "group/likeMessage";
	//上传图片api
	public static final String FILE_UPLOADLOGO = "file/uploadLogo";
	public static final String FILE_UPLOADIAMGE = "file/uploadOneImage";
	
}
