package com.dg.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.dg.app.bean.Fan;
import com.dg.app.bean.Moment;
import com.dg.app.ui.DGUserInfoActivity;
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
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.utils.StringUtils;
import org.kymjs.kjframe.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FansAdapter extends ListBaseAdapter<Fan> {

    private Activity context;

    private KJBitmap kjb;

    //提示组件
    private DGNoticeToast dgNoticeToast;
    private ProgressDialog proDia;

    public FansAdapter(Activity context) {
        this.context = context;
        kjb = new KJBitmap();
        initNoticeWidget(context);
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
                        R.layout.list_fan_item, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final Fan fan = mDatas.get(position);

            final int user_id = fan.getUser_id();

            /**
             * 点击单项状态进入状态详情界面
             */
            vh.ll_fan_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DGUserInfoActivity.class);
                    intent.putExtra("user_id",user_id);
                    context.startActivity(intent);
                }
            });

            /**
             *1、设置头像
             */
            String userLogo = DGImageUtils.toSmallImageURL(fan.getUser_logo());
            kjb.display(vh.riv_head, APIconfig.IMG_BASEURL + userLogo, vh.riv_head.getWidth(), vh.riv_head.getHeight(), R.mipmap.default_head_image);

            vh.riv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DGUserInfoActivity.class);
                    intent.putExtra("user_id", user_id);
                    context.startActivity(intent);
                }
            });

            if(fan.getUser_sex()==0){
                vh.iv_fan_sex.setImageResource(R.mipmap.ic_round_man);
            }else if(fan.getUser_sex()==1){
                vh.iv_fan_sex.setImageResource(R.mipmap.ic_round_woman);
            }
            /**
             * 2、设置顶部昵称
             */
            vh.tv_nickname.setText(fan.getNickname());

            /**
             * 3、设置用户信息
             */
            String sex="";
            if(fan.getUser_sex()==0){
                sex = "男";
            }else if(fan.getUser_sex()==1){
                sex = "女";
            }
            String user_info = sex +" / "+fan.getUser_age()+"岁"+" / "+fan.getCareer();
            vh.tv_user_info.setText(user_info);

            /**
             * 4、设置位置
             */
            vh.tv_location.setText(fan.getCity_name());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.ll_fan_item)
        LinearLayout ll_fan_item;

        @InjectView(R.id.riv_head)
        RoundImageView riv_head;

        @InjectView(R.id.iv_fan_sex)
        ImageView iv_fan_sex;

        @InjectView(R.id.tv_nickname)
        TextView tv_nickname;

        @InjectView(R.id.tv_user_info)
        TextView tv_user_info;

        @InjectView(R.id.tv_location)
        TextView tv_location;

        public ViewHolder(View view) {
            try {
                ButterKnife.inject(this, view);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    //弹出菜单点击事件
    private View.OnClickListener itemsOnclick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

}
