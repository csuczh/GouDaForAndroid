package com.dg.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dg.app.R;
import com.dg.app.api.ApiHttpClient;
import com.dg.app.base.ListBaseAdapter;
import com.dg.app.bean.Question;
import com.dg.app.bean.Topic;
import com.dg.app.ui.DetailDKActivity;
import com.squareup.picasso.Picasso;

import org.kymjs.kjframe.KJBitmap;
import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lenovo on 2015/10/27.
 */
public class TopicAdapter extends ListBaseAdapter<Topic> {
    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_subtopic, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final Topic topic = mDatas.get(position);
//        KJBitmap kjb = new KJBitmap();
//        String obsolutURl= ApiHttpClient.IMAGE_URL+topic.getPublisher_logo();
//        kjb.display(vh.hot_pictrue, obsolutURl);
//        String obsolutURl="";
//        if(topic.getPublisher_logo()!=null&&topic.getPublisher_logo()!="")
//            obsolutURl= ApiHttpClient.IMAGE_URL+"/"+topic.getPublisher_logo();
//        Picasso.with(context).load(obsolutURl).into(vh.hot_pictrue);
         vh.hot_pictrue.setBackgroundResource(R.drawable.dogknow_yellowcircle);

        vh.topic_name.setText(topic.getTitle());
        final Context mContext=parent.getContext();
        vh.topic_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(mContext, DetailDKActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("question_id", topic.getQuestion_id());
                    bundle.putString("title", topic.getTitle());
                    bundle.putInt("likes_num", topic.getLikes_num());
                    bundle.putString("publisher_logo", topic.getPublisher_logo());
                    bundle.putString("publisher_name", topic.getPublisher_name());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);

            }
        });
        return convertView;
    }





    public static class ViewHolder {
        @InjectView(R.id.topic_image)
        ImageView hot_pictrue;
        @InjectView(R.id.topic_name)
        TextView topic_name;
        @InjectView(R.id.topic_forward_image)
        ImageView topic_forward;

        public ViewHolder(View view) {
            try{
                ButterKnife.inject(this, view);}
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }
    }
}