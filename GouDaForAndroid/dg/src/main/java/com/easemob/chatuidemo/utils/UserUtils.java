package com.easemob.chatuidemo.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.ui.DGUserInfoActivity;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chatuidemo.DemoHXSDKHelper;

import com.easemob.chatuidemo.domain.User;
import com.squareup.picasso.Picasso;

public class UserUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static User getUserInfo(String username){
        User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList().get(username);
        if(user == null){
            user = new User(username);
        }

//        if(user != null){
//            //demo没有这些数据，临时填充
//        	if(TextUtils.isEmpty(user.getNick()))
//        		user.setNick(username);
//        }
        return user;
    }
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){

			final User user = getUserInfo(username);
			if (user != null && user.getAvatar() != null&&user.getAvatar()!="") {
				Picasso.with(context).load(user.getAvatar()).placeholder(R.mipmap.default_avatar).into(imageView);
			} else {
				Picasso.with(context).load(R.mipmap.default_avatar).into(imageView);
			}
		final Context context_user=context;
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context_user, DGUserInfoActivity.class);
				intent.putExtra("user_id", user.getUser_id());
				context_user.startActivity(intent);
			}
		});
    }
    
    /**
     * 设置当前用户头像
     */
	public static void setCurrentUserAvatar(Context context, ImageView imageView) {

			   User user = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
		        System.out.println("current user------>"+user.getAvatar());
			   if (user != null && user.getAvatar() != null&&user.getAvatar()!="") {
				   Picasso.with(context).load(user.getAvatar()).placeholder(R.mipmap.default_avatar).into(imageView);
			   } else {
				   Picasso.with(context).load(R.mipmap.default_avatar).into(imageView);
			   }
		final String user_id=AppContext.getInstance().getProperty("dgUser.userid");
		final Context context_user=context;
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context_user, DGUserInfoActivity.class);
				intent.putExtra("user_id", user_id);
				context_user.startActivity(intent);
			}
		});

	}
    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
    	User user = getUserInfo(username);
    	if(user != null){
    		textView.setText(user.getNick());
    	}else{
    		textView.setText(username);
    	}
    }
    
    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView){
    	User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
    	if(textView != null){
    		textView.setText(user.getNick());
    	}
    }
    
    /**
     * 保存或更新某个用户
     * @param
     */
	public static void saveUserInfo(User newUser) {
		if (newUser == null || newUser.getUsername() == null) {
			return;
		}
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newUser);
	}
    
}
