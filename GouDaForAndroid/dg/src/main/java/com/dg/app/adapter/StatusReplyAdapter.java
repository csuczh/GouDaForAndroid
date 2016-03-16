package com.dg.app.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.bean.Reply;
import com.dg.app.util.SpecialTextUtils;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.utils.StringUtils;

import java.util.List;

/**
 * Created by xianxiao on 2015/10/16.
 */
public class StatusReplyAdapter extends BaseAdapter {

    private Context context;
    private List<Reply> comments;

    private final KJBitmap kjb = new KJBitmap();

    public StatusReplyAdapter(Context context, List<Reply> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try{

            ViewHolderList holder;

            if (convertView == null) {
                holder = new ViewHolderList();
                convertView = View.inflate(context, R.layout.list_comment_item, null);
                holder.ll_comments = (LinearLayout) convertView
                        .findViewById(R.id.ll_comments);
                holder.riv_comment_avatar = (ImageView) convertView
                        .findViewById(R.id.riv_comment_avatar);
                holder.tv_comment_subhead = (TextView) convertView
                        .findViewById(R.id.tv_comment_subhead);
                holder.tv_comment_caption = (TextView) convertView
                        .findViewById(R.id.tv_comment_caption);
                holder.tv_comment = (TextView) convertView
                        .findViewById(R.id.tv_comment);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolderList) convertView.getTag();
            }

            final Reply reply = (Reply) getItem(position);

            //头像
            kjb.display(holder.riv_comment_avatar, APIconfig.IMG_BASEURL+reply.getSender_logo(),40,40,R.mipmap.default_head_image);

            //昵称
            holder.tv_comment_subhead.setText(reply.getSender_name());

            //时间
//        holder.tv_comment_caption.setText(DateUtils.getShortTime(reply.getTime()));
            holder.tv_comment_caption.setText(StringUtils.friendlyTime(reply.getTime()));

            SpannableString content = SpecialTextUtils.getWeiboContent(
                    context, holder.tv_comment, reply.getContent());
            holder.tv_comment.setText(content);

            holder.riv_comment_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"进入用户主页",Toast.LENGTH_SHORT);
//                Intent intent = new Intent(context, UserInfoActivity.class);
//                intent.putExtra("userName", user.getName());
//                context.startActivity(intent);
                }
            });

            holder.tv_comment_subhead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"进入用户主页",Toast.LENGTH_SHORT);
//                Intent intent = new Intent(context, UserInfoActivity.class);
//                intent.putExtra("userName", user.getName());
//                context.startActivity(intent);
                }
            });

//        holder.ll_comments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"回复评论",Toast.LENGTH_SHORT);
//
//                //获取当前
//                reply.getSender_id();
//            }
//        });


        }catch (Exception e){
            e.printStackTrace();
        }



        return convertView;
    }

    public static class ViewHolderList {
        public LinearLayout ll_comments;
        public ImageView riv_comment_avatar;
        public TextView tv_comment_subhead;
        public TextView tv_comment_caption;
        public TextView tv_comment;
    }

}
