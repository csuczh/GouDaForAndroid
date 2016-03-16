package com.dg.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dg.app.R;
import com.dg.app.api.ApiHttpClient;
import com.dg.app.base.ListBaseAdapter;
import com.dg.app.bean.Owners;
import com.dg.app.bean.Question;
import com.dg.app.ui.DetailDKActivity;
import com.easemob.EMValueCallBack;
import com.easemob.applib.controller.HXSDKHelper;

import org.kymjs.kjframe.KJBitmap;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lenovo on 2015/10/26.
 */
public class QuestionAdapter extends ListBaseAdapter<Question> {
    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null || convertView.getTag() == null) {
                convertView = getLayoutInflater(parent.getContext()).inflate(
                        R.layout.list_cell_dogknow_recommend, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

             final Question question = mDatas.get(position);
             KJBitmap kjb = new KJBitmap();
             String obsolutURl= ApiHttpClient.IMAGE_URL+question.getPublisher_logo();
             kjb.display(vh.hot_pictrue, obsolutURl);
             vh.hot_comment_count.setText(question.getLikes_num() + "赞");
             vh.hot_title.setText(question.getTitle());
             vh.question_content.setText(question.getContent().toString().trim());
             final  Context mContext=parent.getContext();
//             vh.recommend_forward.setOnClickListener(new View.OnClickListener() {
//                 @Override
//                 public void onClick(View v) {
//                         Intent intent = new Intent(mContext, DetailDKActivity.class);
//                         Bundle bundle = new Bundle();
//                         bundle.putInt("question_id", question.getQuestion_id());
//                         bundle.putString("title", question.getTitle());
//                         bundle.putInt("likes_num", question.getLikes_num());
//                         bundle.putString("publisher_logo", question.getPublisher_logo());
//                         bundle.putString("publisher_name", question.getPublisher_name());
//                         intent.putExtras(bundle);
//                         mContext.startActivity(intent);
//                 }
//             });
        return convertView;
    }





    public static class ViewHolder {
        @InjectView(R.id.hot_title)
        TextView hot_title;
        @InjectView(R.id.hot_pictrue)
        ImageView hot_pictrue;
        @InjectView(R.id.hot_comment_count)
        TextView hot_comment_count;
        @InjectView(R.id.question_content)
        TextView question_content;
        @InjectView(R.id.recommend_forward)
        ImageView recommend_forward;
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
