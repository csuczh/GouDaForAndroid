package com.dg.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.api.ApiHttpClient;
import com.dg.app.base.ListBaseAdapter;
import com.dg.app.bean.Owners;
import com.dg.app.ui.DGUserInfoActivity;
import com.dg.app.ui.DetailDKActivity;
import com.dg.app.ui.FindSendChat;
import com.dg.app.util.ComputeDistance;
import com.dg.app.util.StringUtils;
import com.dg.app.util.TypefaceUtils;
import com.easemob.EMValueCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.squareup.picasso.Picasso;


import org.kymjs.kjframe.KJBitmap;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OwnersAdapter extends ListBaseAdapter<Owners> {
    private Context context;
    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        context=parent.getContext();
      final  ViewHolder vh;
        try {
            if (convertView == null || convertView.getTag() == null) {
                convertView = getLayoutInflater(parent.getContext()).inflate(
                        R.layout.list_cell_findnearby, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final Owners owners = mDatas.get(position);

           // KJBitmap kjb = new KJBitmap();
            String obsolutURl="";
            if(owners.getUser_logo()!=null&&owners.getUser_logo()!="")
              obsolutURl= ApiHttpClient.IMAGE_URL+owners.getUser_logo();
            Picasso.with(context).load(obsolutURl).into(vh.pictureView);

           // kjb.display(vh.pictureView, obsolutURl);

            vh.pictureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DGUserInfoActivity.class);
                    intent.putExtra("user_id", owners.getUser_id());
                    context.startActivity(intent);
                }
            });
            //0为男士，1位女士
            if (owners.getUser_sex() == 0) {
                vh.sexView.setBackgroundResource(R.mipmap.userinfo_icon_male);
                vh.gender.setText("男");
            } else {
                vh.sexView.setBackgroundResource(R.mipmap.userinfo_icon_female);
                vh.gender.setText("女");

            }
            vh.nickname.setText(owners.getNickname());


            vh.age.setText(owners.getUser_age()+"");
            vh.actor.setText(owners.getCareer());

//            vh.distance.setText(owners.getDistance());
            Double lng=Double.parseDouble(AppContext.getInstance().getProperty("lng"));
            Double lat=Double.parseDouble(AppContext.getInstance().getProperty("lat"));

            Double distance1= ComputeDistance.GetDistance(lat, lng, owners.getPos_lat(), owners.getPos_lng());
            vh.distance.setText(distance1.toString()+"km");


//            SimpleDateFormat sdf=new SimpleDateFormat();
//            Date dt= sdf.parse(owners.getLogin_time());
//            Long time=dt.getTime();
//            Long time2=System.currentTimeMillis();
            String friendtime=StringUtils.friendly_time(owners.getLogin_time());
            vh.time.setText(friendtime);
            Map<String , com.easemob.chatuidemo.domain.User> friends=((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
            final boolean isFriend=friends.containsKey(owners.getEasemob_id());
            if(!isFriend)
             vh.state.setText("打招呼");
            else {
                vh.state.setText("聊天");
            }
            vh.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent intent = new Intent(context, FindSendChat.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isfriend", isFriend);
                        bundle.putString("easemob_id",owners.getEasemob_id());
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                }
            });

//            asyncFetchContactsFromServer(vh.state,owners.getNickname());
//            if (owners.getState() == 0) {
//                vh.state.setText("打招呼");
//            } else {
//                vh.state.setText("正在审核");
//            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return convertView;
    }





   public static class ViewHolder {
        @InjectView(R.id.owner_image)
       ImageView pictureView;
        @InjectView(R.id.owner_sex)
        ImageView sexView;
        @InjectView(R.id.owner_nickname)
        TextView nickname;
        @InjectView(R.id.owner_gender)
        TextView gender;
        @InjectView(R.id.owner_people_age)
        TextView age;
        @InjectView(R.id.owner_actor)
        TextView actor;
        @InjectView(R.id.owner_distance)
        TextView distance;
        @InjectView(R.id.owner_time)
        TextView time;
        @InjectView(R.id.owner_state)
        TextView state;
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
