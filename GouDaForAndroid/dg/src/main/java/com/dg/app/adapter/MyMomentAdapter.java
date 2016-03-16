package com.dg.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.base.ListBaseAdapter;
import com.dg.app.bean.Moment;
import com.dg.app.ui.TweetDetailActivity;
import com.dg.app.util.SpecialTextUtils;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by xianxiao on 2015/10/16.
 */
public class MyMomentAdapter extends ListBaseAdapter<Moment> {

    private Activity context;
    private final KJBitmap kjb = new KJBitmap();

    private String[] months = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};


    public MyMomentAdapter(Activity context){
        this.context = context;
    }

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_my_tweets_item, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final Moment moment = mDatas.get(position);

        Date date = com.dg.app.util.StringUtils.toDate(moment.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int month = calendar.get(Calendar.MONTH);

        Log.i("PersonalPageMoment", day + "");

        Log.i("PersonalPageMoment", month + "");
        //天
        vh.room_day.setText(day + "");
        //月
        vh.room_month.setText(months[month]);

        //图片
        String imgs = moment.getImages();
        if (imgs == null || imgs.trim() == "") {
            vh.room_picture.setVisibility(View.GONE);
        } else {
            String first_img = imgs.split(",")[0];
            kjb.display(vh.room_picture, APIconfig.IMG_BASEURL + first_img,vh.room_picture.getWidth(),vh.room_picture.getHeight(),R.mipmap.default_image);
            vh.room_picture.setVisibility(View.VISIBLE);
        }

        //状态正文
        vh.room_content.setText(SpecialTextUtils.getWeiboContent(context, vh.room_content, moment.getContent()));

        vh.ll_card_new_moment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TweetDetailActivity.class);
                intent.putExtra("status", moment);
                context.startActivity(intent);

            }
        });

        return convertView;
    }

    public static class ViewHolder {

        @InjectView(R.id.ll_card_new_moment)
        LinearLayout ll_card_new_moment;

        @InjectView(R.id.room_day)
        TextView room_day;

        @InjectView(R.id.room_month)
        TextView room_month;

        @InjectView(R.id.room_picture)
        ImageView room_picture;

        @InjectView(R.id.room_content)
        TextView room_content;

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
