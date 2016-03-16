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
import com.dg.app.bean.Kind;
import com.dg.app.bean.Owners;
import com.dg.app.ui.DetailDKActivity;
import com.dg.app.ui.SubArticleActivity;
import com.easemob.EMValueCallBack;
import com.easemob.applib.controller.HXSDKHelper;

import org.kymjs.kjframe.KJBitmap;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lenovo on 2015/10/26.
 */
public class KindAdapter extends ListBaseAdapter<Kind> {
    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        final  ViewHolder vh;
            if (convertView == null || convertView.getTag() == null) {
                convertView = getLayoutInflater(parent.getContext()).inflate(
                        R.layout.list_cell_kind, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

           final   Kind kind = mDatas.get(position);
            switch (position) {
                case 0:
                vh.kind_picture.setImageResource(R.mipmap.amusement);
                    break;
                case 1:
                    vh.kind_picture.setImageResource(R.mipmap.consign);
                    break;
                case 2:
                    vh.kind_picture.setImageResource(R.mipmap.dress);
                    break;
                case 3:
                    vh.kind_picture.setImageResource(R.mipmap.food);
                    break;
                case 4:
                    vh.kind_picture.setImageResource(R.mipmap.married);
                    break;
                case 5:
                    vh.kind_picture.setImageResource(R.mipmap.medicine);
                    break;
            }
//            KJBitmap kjb = new KJBitmap();
//            String obsolutURl= ApiHttpClient.IMAGE_URL+owners.getUser_logo().substring(1);
//
//            kjb.display(vh.pictureView, obsolutURl);
        final Context mcontext=parent.getContext();
           vh.kind_name.setText(kind.getName());
           vh.kind_short.setText(kind.getAbbr());
           vh.kind_forward_image.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   try {
                       Intent intent = new Intent(mcontext, SubArticleActivity.class);
                       Bundle bundle = new Bundle();
                       bundle.putInt("category_id", kind.getCategory_id());
                       intent.putExtras(bundle);
                       mcontext.startActivity(intent);
                   }catch (Exception ex)
                   {
                       ex.printStackTrace();
                   }
               }
           });
        return convertView;
    }





    public static class ViewHolder {
        @InjectView(R.id.kind_image)
        ImageView kind_picture;
        @InjectView(R.id.kind_name)
        TextView kind_name;
        @InjectView(R.id.kind_short)
        TextView kind_short;
        @InjectView(R.id.kind_forward_image)
        ImageView kind_forward_image;

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
