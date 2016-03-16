package com.dg.app.service;

import android.content.Context;
import android.content.Intent;

import com.dg.app.AppContext;
import com.umeng.common.message.Log;
import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

/**
 * Created by xianxiao on 2015/11/27.
 */
public class MomentPushIntentService extends UmengBaseIntentService {
    private static final String TAG = MomentPushIntentService.class.getName();

    @Override
    protected void onMessage(Context context, Intent intent) {

        // 需要调用父类的函数，否则无法统计到消息送达
        super.onMessage(context, intent);

        try {
            //可以通过MESSAGE_BODY取得消息体
            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            UTrack.getInstance(context).trackMsgClick(msg);
            Log.d(TAG, "message=" + message);    //消息体
            Log.d(TAG, "custom="+msg.custom);    //自定义消息的内容
            Log.d(TAG, "title="+msg.title);    //通知标题
            Log.d(TAG, "text="+msg.text);    //通知内容

            AppContext.showToast("消息体=" + message);

            AppContext.showToast("自定义消息的内容="+msg.custom);

            AppContext.showToast("通知标题=" + msg.title);

            AppContext.showToast("通知内容=" + msg.text);

            // code  to handle message here
            // ...
            // 完全自定义消息的处理方式，点击或者忽略
            boolean isClickOrDismissed = true;
            if(isClickOrDismissed) {
                //完全自定义消息的点击统计
                UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
            } else {
                //完全自定义消息的忽略统计
                UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }



    }
}
