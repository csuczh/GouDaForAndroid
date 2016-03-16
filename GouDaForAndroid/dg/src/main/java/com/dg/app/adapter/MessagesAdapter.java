package com.dg.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.ListBaseAdapter;
import com.dg.app.bean.Message;
import com.dg.app.bean.Moment;
import com.dg.app.bean.MomentDetailResponse;
import com.dg.app.ui.DGUserInfoActivity;
import com.dg.app.ui.TweetDetailActivity;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.util.DGImageUtils;
import com.dg.app.util.SpecialTextUtils;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.utils.StringUtils;
import org.kymjs.kjframe.widget.RoundImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MessagesAdapter extends ListBaseAdapter<Message>{

    private Activity context;

    private KJBitmap kjb;

    public static final String REPLY_MESSAGE = "REPLY_MESSAGE";

    public static final String LIKE_MESSAGE = "LIKE_MESSAGE";

    private String which_page;

    //提示组件
    private DGNoticeToast dgNoticeToast;
    private ProgressDialog proDia;

    public MessagesAdapter(Activity context,String which_page) {
        this.context = context;
        this.which_page = which_page;
        kjb = new KJBitmap();
        initNoticeWidget(context);
    }

    private void initNoticeWidget(Activity context) {
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
                        R.layout.list_message_item, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final Message message = mDatas.get(position);

            final int moment_id = message.getMoment_id();
            final int user_id = message.getPublisher_id();
            //点击进入详情页
            vh.ll_card_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enterDetailAty(moment_id);
                }
            });

            /**
             *1、设置头像
             */
//            kjb.display(vh.iv_headimg, APIconfig.IMG_BASEURL + message.getPublisher_logo());
            String userLogo = DGImageUtils.toSmallImageURL(message.getPublisher_logo());
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
            if(message.getPublisher_name()==null||"".equals(message.getPublisher_name())){
                vh.tv_head.setText("无名");
            }else{
                vh.tv_head.setText(message.getPublisher_name());
            }
            vh.tv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DGUserInfoActivity.class);
                    intent.putExtra("user_id",user_id);
                    context.startActivity(intent);
                }
            });

            /**
             * 3、设置评论内容
             */

            if (which_page==REPLY_MESSAGE){
                vh.tv_comment.setText(SpecialTextUtils.getWeiboContent(context, vh.tv_comment, message.getContent()));
                vh.iv_like.setVisibility(View.GONE);

            }else if(which_page ==LIKE_MESSAGE){
                vh.tv_comment.setVisibility(View.GONE);
                vh.iv_like.setVisibility(View.VISIBLE);
            }

            /**
             * 4、设置原状态
             */
            if (StringUtils.isEmpty(message.getImage())) {
                vh.iv_moment_img.setVisibility(View.GONE);
                vh.tv_moment_content.setVisibility(View.VISIBLE);
                vh.tv_moment_content.setText(SpecialTextUtils.getWeiboContent(context, vh.tv_moment_content, message.getOrigin()));
            } else {
                vh.iv_moment_img.setVisibility(View.VISIBLE);
                vh.tv_moment_content.setVisibility(View.GONE);
//                kjb.display(vh.iv_moment_img, APIconfig.IMG_BASEURL + message.getImage());
                String first_image = DGImageUtils.toSmallImageURL(message.getImage());
                kjb.display(vh.iv_moment_img, APIconfig.IMG_BASEURL + first_image, vh.iv_moment_img.getWidth(),vh.iv_moment_img.getHeight(), R.mipmap.default_image);
            }



            //点击进入详情页
            vh.ll_orign_moment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enterDetailAty(moment_id);
                }
            });

            /**
             * 5、时间
             */
            vh.tv_time.setText(StringUtils.friendlyTime(message.getPublish_time()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    //进入详细界面
    private void enterDetailAty(final int moment_id) {
        int user_id = AppContext.getInstance().getLoginUid();
        if(user_id!=0){
            proDia.show();
            DGApi.detailMoment(user_id,moment_id, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    MomentDetailResponse momentDetailResponse = XmlUtils.toBean(MomentDetailResponse.class,responseBody);
                    proDia.dismiss();
                    if(momentDetailResponse.getCode()==0){
                        Moment moment = momentDetailResponse.getMoment();
                        Intent intent = new Intent(context, TweetDetailActivity.class);
                        intent.putExtra("status", moment);
                        context.startActivity(intent);
                    }else {
                        dgNoticeToast.showFailure(momentDetailResponse.getMsg());
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

    static class ViewHolder {

        @InjectView(R.id.ll_card_message)
        LinearLayout ll_card_message;

        @InjectView(R.id.msg_item_img_head)
        RoundImageView iv_headimg;

        @InjectView(R.id.tv_head)
        TextView tv_head;

        @InjectView(R.id.tv_comment)
        TextView tv_comment;

        @InjectView(R.id.iv_like)
        ImageView iv_like;

        @InjectView(R.id.tv_time)
        TextView tv_time;

        @InjectView(R.id.iv_moment_img)
        ImageView iv_moment_img;

        @InjectView(R.id.tv_moment_content)
        TextView tv_moment_content;

        @InjectView(R.id.ll_orign_moment)
        LinearLayout ll_orign_moment;

        public ViewHolder(View view) {
            try {
                ButterKnife.inject(this, view);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
