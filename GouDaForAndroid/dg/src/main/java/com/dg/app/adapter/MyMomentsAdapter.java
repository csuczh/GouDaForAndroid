package com.dg.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.bean.Moment;
import com.dg.app.bean.Reply;
import com.dg.app.ui.DGUserInfoActivity;
import com.dg.app.ui.TweetDetailActivity;
import com.dg.app.util.DGImageUtils;
import com.dg.app.util.SpecialTextUtils;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.utils.StringUtils;
import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Created by xianxiao on 2015/10/16.
 */
public class MyMomentsAdapter extends KJAdapter<Moment> implements View.OnClickListener {

    private Activity context;
    private final KJBitmap kjb = new KJBitmap();

    private String[] months = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};

    public MyMomentsAdapter(AbsListView view, Collection<Moment> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }

    public MyMomentsAdapter(Activity context, AbsListView view, Collection<Moment> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(AdapterHolder adapterHolder, final Moment moment, boolean b) {

        LinearLayout ll_card_new_moment = adapterHolder.getView(R.id.ll_card_new_moment);
        TextView room_day = adapterHolder.getView(R.id.room_day);
        TextView room_month = adapterHolder.getView(R.id.room_month);
        ImageView room_picture = adapterHolder.getView(R.id.room_picture);
        TextView room_content = adapterHolder.getView(R.id.room_content);


        Date date = com.dg.app.util.StringUtils.toDate(moment.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int month = calendar.get(Calendar.MONTH);

        Log.i("PersonalPageMoment", day + "");

        Log.i("PersonalPageMoment", month + "");
        //天
        room_day.setText(day + "");
        //月
        room_month.setText(months[month]);

        //图片
        String imgs = moment.getImages();
        if (imgs == null || imgs.trim() == "") {
            room_picture.setVisibility(View.GONE);
        } else {
            String first_img = imgs.split(",")[0];
            kjb.display(room_picture, APIconfig.IMG_BASEURL + first_img,room_picture.getWidth(),room_picture.getHeight(),R.mipmap.default_image);
            room_picture.setVisibility(View.VISIBLE);
        }

        //状态正文
        room_content.setText(SpecialTextUtils.getWeiboContent(context, room_content, moment.getContent()));


        ll_card_new_moment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TweetDetailActivity.class);
                intent.putExtra("status", moment);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
