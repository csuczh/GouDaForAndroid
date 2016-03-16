package com.dg.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.base.ListBaseAdapter;
import com.dg.app.bean.Collection;
import com.dg.app.ui.DGUserInfoActivity;
import com.dg.app.ui.DetailDKActivity;
import com.dg.app.util.DGImageUtils;
import com.dg.app.util.SpecialTextUtils;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.RoundImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CollectionsAdapter extends ListBaseAdapter<Collection>{

    private KJBitmap kjb;

    public CollectionsAdapter(Activity context) {
        this.context = context;
        kjb = new KJBitmap();
    }

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        try {
            if (convertView == null || convertView.getTag() == null) {
                convertView = getLayoutInflater(parent.getContext()).inflate(
                        R.layout.list_collect_item, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final Collection collection = mDatas.get(position);

            /**
             * 点击单项收藏进入收藏详情界面
             */
            vh.ll_card_collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"进入收藏详情界面",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, DetailDKActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("question_id", collection.getQuestion_id());
                    bundle.putString("title", collection.getTitle());
                    bundle.putInt("likes_num", collection.getLikes_num());
                    bundle.putString("publisher_logo", collection.getPublisher_logo());
                    bundle.putString("publisher_name", collection.getPublisher_name());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            /**
             *1、设置头像
             */
            String userLogo = DGImageUtils.toSmallImageURL(collection.getPublisher_logo());
            kjb.display(vh.iv_headimg, APIconfig.IMG_BASEURL + userLogo, vh.iv_headimg.getWidth(), vh.iv_headimg.getHeight(), R.mipmap.default_head_image);

            vh.iv_headimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DGUserInfoActivity.class);
                    intent.putExtra("user_id", collection.getPublisher_id());
                    context.startActivity(intent);
                }
            });

            /**
             * 2、设置顶部信息
             */
            vh.tv_subhead.setText(collection.getPublisher_name());
            //时间
            vh.tv_caption.setText(collection.getPublish_time());

            /**
             * 3、设置收藏正文
             */
            vh.tv_content.setText(SpecialTextUtils.getWeiboContent(context, vh.tv_content, collection.getTitle()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }


    static class ViewHolder {

        @InjectView(R.id.msg_item_img_head)
        RoundImageView iv_headimg;

        @InjectView(R.id.ll_card_collection)
        LinearLayout ll_card_collection;

        @InjectView(R.id.msg_item_text_uname)
        TextView tv_subhead;

        @InjectView(R.id.msg_item_text_time)
        TextView tv_caption;

        @InjectView(R.id.tv_collection_question)
        TextView tv_content;

        public ViewHolder(View view) {
            try {
                ButterKnife.inject(this, view);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
