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
import com.dg.app.bean.Moment;
import com.dg.app.bean.Ranking;
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

public class RankingAdapter extends ListBaseAdapter<Ranking>{

    private Activity context;

    private KJBitmap kjb;

    private static final String[] MENG_NAME = {"铲屎大帝","铲屎大将","铲屎圣手","铲屎新手"};

    //提示组件'
    private DGNoticeToast dgNoticeToast;
    private ProgressDialog proDia;

    public RankingAdapter(Activity context) {
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
                        R.layout.list_rank_item, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            Ranking ranking = mDatas.get(position);

            final int user_id = ranking.getUser_id();

            /**
             *1、设置头像
             */
            final String userLogo = DGImageUtils.toSmallImageURL(ranking.getLogo());
            kjb.display(vh.iv_headimg, APIconfig.IMG_BASEURL + userLogo, vh.iv_headimg.getWidth(), vh.iv_headimg.getHeight(), R.mipmap.default_head_image);

            vh.iv_headimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DGUserInfoActivity.class);
                    intent.putExtra("user_id", user_id);
                    context.startActivity(intent);
                }
            });

            /**
             * 2、设置排名
             */
            vh.tv_rank_index.setText((position+1)+"");

            /**
             * 3、设置性别
             */
            if(ranking.getSex()==0){
                vh.iv_rank_sex.setImageResource(R.mipmap.ic_round_man);
            }else if(ranking.getSex()==1){
                vh.iv_rank_sex.setImageResource(R.mipmap.ic_round_woman);
            }

            /**
             * 4、设置用户昵称
             */
            vh.tv_caption.setText(ranking.getNickname());

            /**
             * 5、设置萌耀币
             */
            vh.tv_meng_coins_count.setText(ranking.getGrade()+"");

            /**
             * 6、设置图片
             */
            if(position>=0&&position<3){
                vh.tv_meng_name.setText(MENG_NAME[0]);
                vh.tv_meng_name.setBackgroundResource(R.drawable.rank_icon);
            }else if(position>=3&&position<6){
                vh.tv_meng_name.setText(MENG_NAME[1]);
                vh.tv_meng_name.setBackgroundResource(R.drawable.rank_icon_2);
            }else if(position>=6&&position<9){
                vh.tv_meng_name.setText(MENG_NAME[2]);
                vh.tv_meng_name.setBackgroundResource(R.drawable.rank_icon_3);
            }else if(position>=9){
                vh.tv_meng_name.setText(MENG_NAME[3]);
                vh.tv_meng_name.setBackgroundResource(R.drawable.rank_icon_4);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {

        @InjectView(R.id.msg_item_img_head)
        RoundImageView iv_headimg;

        @InjectView(R.id.tv_rank_index)
        TextView tv_rank_index;

        @InjectView(R.id.iv_rank_sex)
        ImageView iv_rank_sex;

        @InjectView(R.id.msg_item_text_uname)
        TextView tv_caption;

        @InjectView(R.id.tv_meng_coins_count)
        TextView tv_meng_coins_count;

        @InjectView(R.id.tv_meng_name)
        TextView tv_meng_name;

        public ViewHolder(View view) {
            try {
                ButterKnife.inject(this, view);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
