package com.dg.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.ListBaseAdapter;
import com.dg.app.bean.CommonResponse;
import com.dg.app.bean.Moment;
import com.dg.app.ui.ChatDetailActivity;
import com.dg.app.ui.DGUserInfoActivity;
import com.dg.app.ui.FindSendChat;
import com.dg.app.ui.ImagesBrowserActivity;
import com.dg.app.ui.SelectPicPopupWindow;
import com.dg.app.ui.TweetDetailActivity;
import com.dg.app.ui.TweetPopupWindow;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.ui.toast.DGToast;
import com.dg.app.ui.widget.WrapHeightGridView;
import com.dg.app.util.DGImageUtils;
import com.dg.app.util.SpecialTextUtils;
import com.dg.app.util.XmlUtils;
import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.domain.User;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.utils.StringUtils;
import org.kymjs.kjframe.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MomentsAdapter extends ListBaseAdapter<Moment> implements View.OnClickListener {

    private Activity context;

    private KJBitmap kjb;

    private TweetPopupWindow tweetPopupWindow;

    //提示组件
    private DGNoticeToast dgNoticeToast;
    private ProgressDialog proDia;

    //自定义弹出菜单
    private SelectPicPopupWindow selectPicPopupWindow;

    //获取当前用户的好友信息
    private   Map<String , User> friends;
    private boolean isfriends;//判断是否是好友
    public MomentsAdapter(Activity context) {
        this.context = context;
        kjb = new KJBitmap();
        initNoticeWidget(context);

        friends=((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
//        final boolean isFriend=friends.containsKey(owners.getEasemob_id());

    }

    /**
     * 初始化提示组件
     * @param context
     */
    private void initNoticeWidget(Context context) {
        proDia = new ProgressDialog(context);
        dgNoticeToast = new DGNoticeToast(context);
    }

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        try {
            if (convertView == null || convertView.getTag() == null) {
                convertView = getLayoutInflater(parent.getContext()).inflate(
                        R.layout.list_tweets_item_new, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final Moment moment = mDatas.get(position);


            final int user_id = moment.getPublisher_id();

            /**
             * 点击单项状态进入状态详情界面
             */
            vh.ll_card_tweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TweetDetailActivity.class);
                    intent.putExtra("status", moment);
                    context.startActivity(intent);
                }
            });

            /**
             *1、设置头像
             */
//            kjb.display(vh.iv_headimg, APIconfig.IMG_BASEURL + moment.getUserlogo());
            String userLogo = DGImageUtils.toSmallImageURL(moment.getUserlogo());
            kjb.display(vh.iv_headimg, APIconfig.IMG_BASEURL + userLogo, vh.iv_headimg.getWidth(),vh.iv_headimg.getHeight(), R.mipmap.default_head_image);
            vh.iv_headimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DGUserInfoActivity.class);
                    intent.putExtra("user_id",user_id);
                    context.startActivity(intent);
                }
            });
            /**
             * 2、设置顶部信息
             */
            vh.tv_subhead.setText(moment.getPublisher_name());
            vh.tv_subhead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DGUserInfoActivity.class);
                    intent.putExtra("user_id",user_id);
                    context.startActivity(intent);
                }
            });
            //距离
            vh.tv_caption.setText("服务器未给");
            //设置分享按钮点击事件监听器
            if(user_id!=AppContext.getInstance().getLoginUid()){
                vh.iv_share.setVisibility(View.VISIBLE);
                vh.iv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPopMenuWindow(user_id,moment.getEasemob_id());
                    }
                });
            }else{
                vh.iv_share.setVisibility(View.GONE);
            }
            /**
             * 3、设置状态正文
             */
            vh.tv_content.setText(SpecialTextUtils.getWeiboContent(context, vh.tv_content, moment.getContent()));

            /**
             * 4、设置图片
             */
            try {
                setImages(moment, (FrameLayout) vh.moment_image, vh.gv_images, vh.iv_image);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /**
             * 5、设置底部信息栏
             */

            vh.tv_tweet_time.setText(StringUtils.friendlyTime(moment.getTime()));

            vh.tv_tweet_like_num.setText(moment.getLikes_num()+"");

            if(moment.getLiked()==1){
                vh.iv_tweet_like.setImageResource(R.mipmap.ic_like);
                vh.iv_tweet_like.setClickable(false);
            }else {
                final int current_user_id = AppContext.getInstance().getLoginUid();
                if(current_user_id!=0){

                    final ImageView iv_like = vh.iv_tweet_like;
                    final TextView tv_like_num = vh.tv_tweet_like_num;

                    vh.iv_tweet_like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DGApi.like(current_user_id, moment.getMoment_id(), new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    CommonResponse commonResponse = XmlUtils.toBean(CommonResponse.class,responseBody);
                                    if(commonResponse.getCode()==0){
                                        iv_like.setImageResource(R.mipmap.ic_like);
                                        int old_num = Integer.parseInt(tv_like_num.getText().toString());
                                        tv_like_num.setText((old_num+1)+"");
                                        //点赞成功
                                        dgNoticeToast.showSuccess("点赞成功");
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
                    });
                }
            }


            vh.iv_tweet_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TweetDetailActivity.class);
                    intent.putExtra("status", moment);
                    context.startActivity(intent);
                }
            });

            vh.tv_tweet_comment_num.setText(moment.getReply_num() + "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share:
                break;
        }

    }


    static class ViewHolder {

        @InjectView(R.id.ll_card_tweet)
        LinearLayout ll_card_tweet;

        @InjectView(R.id.msg_item_img_head)
        RoundImageView iv_headimg;

        @InjectView(R.id.tv_subhead)
        TextView tv_subhead;

        @InjectView(R.id.iv_share)
        ImageView iv_share;

        @InjectView(R.id.tv_caption)
        TextView tv_caption;

        @InjectView(R.id.tv_content)
        TextView tv_content;

        @InjectView(R.id.moment_image)
        FrameLayout moment_image;

        @InjectView(R.id.gv_images)
        WrapHeightGridView gv_images;


        @InjectView(R.id.iv_image)
        ImageView iv_image;

        @InjectView(R.id.tv_tweet_time)
        TextView tv_tweet_time;


        @InjectView(R.id.iv_tweet_like)
        ImageView iv_tweet_like;

        @InjectView(R.id.tv_tweet_like_num)
        TextView tv_tweet_like_num;

        @InjectView(R.id.iv_tweet_comment)
        ImageView iv_tweet_comment;

        @InjectView(R.id.tv_tweet_comment_num)
        TextView tv_tweet_comment_num;

        public ViewHolder(View view) {
            try {
                ButterKnife.inject(this, view);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }


    //设置在自适应网格布局中显示图片
    private void setImages(final Moment status, FrameLayout imgContainer,
                           GridView gv_images, ImageView iv_image) {

        try {
            List<String> pic_urls = new ArrayList<>();//获取状态图片列表

            String imgs = status.getImages();

            if(imgs!=null&&!"".equals(imgs)){
                String[] imgs_array = imgs.split(",");
                for(String img:imgs_array){
                    pic_urls.add(img);
                }
            }

            if (pic_urls != null && pic_urls.size() == 1) {
                imgContainer.setVisibility(View.VISIBLE);
                gv_images.setVisibility(View.GONE);
                iv_image.setVisibility(View.VISIBLE);

                String small_pic = DGImageUtils.toSmallImageURL(pic_urls.get(0));
                kjb.display(iv_image, APIconfig.IMG_BASEURL + small_pic, iv_image.getWidth(),iv_image.getHeight(), R.mipmap.default_image);
                iv_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ImagesBrowserActivity.class);
                        intent.putExtra("status", status);
                        context.startActivity(intent);
                    }
                });
            } else if (pic_urls != null && pic_urls.size() > 1) {
                imgContainer.setVisibility(View.VISIBLE);
                gv_images.setVisibility(View.VISIBLE);
                iv_image.setVisibility(View.GONE);

                StatusGridImgsAdapter imagesAdapter = new StatusGridImgsAdapter(context, pic_urls);
                gv_images.setAdapter(imagesAdapter);
                gv_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent intent = new Intent(context, ImagesBrowserActivity.class);
                        intent.putExtra("status", status);
                        intent.putExtra("position", position);
                        context.startActivity(intent);
                    }
                });
            } else {
                imgContainer.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPopMenuWindow(final int publisherid, final String easemob_id) {
        isfriends=friends.containsKey(easemob_id);


        //创建弹出菜单，并添加监听器
        tweetPopupWindow = new TweetPopupWindow(context, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.btn_follow_ta:
                        followUser(publisherid);
                        break;
                    case R.id.btn_begin_chat:
                        Toast.makeText(context, "发起聊天", Toast.LENGTH_LONG).show();
                        if(isfriends)
                        {
                            Intent intent = new Intent(context, ChatDetailActivity.class);
                            intent.putExtra("userId", easemob_id);
                            intent.putExtra("user_to",publisherid);
                            context.startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(context, FindSendChat.class);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isfriend", isfriends);
                            bundle.putString("easemob_id",easemob_id);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                        break;
                    case R.id.btn_invite_liugou:
                        Toast.makeText(context, "约TA一起遛狗", Toast.LENGTH_LONG).show();

                        if (!isfriends) {

                            try {
                                EMContactManager.getInstance().addContact(easemob_id, "想和你一起遛狗邀请");

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            String name = AppContext.getInstance().getProperty("dgUser.easemob_id");
                            EMConversation conversation = EMChatManager.getInstance().getConversation(name);
                            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                            TextMessageBody txtBody = new TextMessageBody("想和你一起遛狗邀请");
                            message.addBody(txtBody);
                            message.setReceipt(easemob_id);
                            conversation.addMessage(message);
                            EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context,"发送遛狗邀请成功",Toast.LENGTH_LONG).show();
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
                        break;
                    case R.id.btn_invite_xiangqin:
                        Toast.makeText(context, "想和TA的狗狗相亲", Toast.LENGTH_LONG).show();
                        if (!isfriends) {
                            try {
                                EMContactManager.getInstance().addContact(easemob_id, "想和你的狗相亲！");

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            String name = AppContext.getInstance().getProperty("dgUser.easemob_id");
                            EMConversation conversation = EMChatManager.getInstance().getConversation(name);
                            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                            TextMessageBody txtBody = new TextMessageBody("想和你的狗相亲！");
                            message.addBody(txtBody);
                            message.setReceipt(easemob_id);
                            conversation.addMessage(message);
                            EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context,"发送相亲邀请成功",Toast.LENGTH_LONG).show();
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
                        break;
                    case R.id.btn_share2other:
                        Toast.makeText(context, "分享到其他平台", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.btn_jubao:
                        Toast.makeText(context, "举报", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        });
        //显示窗口
        //设置layout在PopupWindow中显示的位置
        tweetPopupWindow.showAtLocation(context.findViewById(R.id.ll_gouquan_moments), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }


    /**
     * 关注该用户
     * @param publisherid
     */
    private void followUser(int publisherid) {
        try{
            int user_id = AppContext.getInstance().getLoginUid();
            if(publisherid!=0&&user_id!=0){
                DGApi.followUser(user_id, publisherid, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        CommonResponse commonResponse = XmlUtils.toBean(CommonResponse.class,responseBody);
                        if(commonResponse.getCode()==0){
//                            Toast.makeText(context, "关注TA", Toast.LENGTH_LONG).show();
                            DGToast.makeText(context, R.mipmap.whitebone, "主人你成功\n" +
                                    "关注了对方", Toast.LENGTH_SHORT).show();
                        }else{
                           AppContext.showToast(commonResponse.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        AppContext.showToast("网络异常");
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //弹出菜单点击事件
    private View.OnClickListener itemsOnclick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

}
