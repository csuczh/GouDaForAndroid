package com.dg.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.SpannableString;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.bean.Reply;
import com.dg.app.ui.DGUserInfoActivity;
import com.dg.app.util.DGImageUtils;
import com.dg.app.util.SpecialTextUtils;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.utils.StringUtils;
import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;

import java.util.Collection;

/**
 * Created by xianxiao on 2015/10/16.
 */
public class StatusReplysAdapter extends KJAdapter<Reply> implements View.OnClickListener {

    private Activity context;
    private final KJBitmap kjb = new KJBitmap();

    public StatusReplysAdapter(AbsListView view, Collection<Reply> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }

    public StatusReplysAdapter(Activity context, AbsListView view, Collection<Reply> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(AdapterHolder adapterHolder, Reply reply, boolean b) {
        LinearLayout ll_comments = adapterHolder.getView(R.id.ll_comments);
        ImageView avatar = adapterHolder.getView(R.id.riv_comment_avatar);
        TextView subhead = adapterHolder.getView(R.id.tv_comment_subhead);
        TextView caption = adapterHolder.getView(R.id.tv_comment_caption);
        TextView comment = adapterHolder.getView(R.id.tv_comment);

        //头像
        String logo = DGImageUtils.toSmallImageURL(reply.getSender_logo());
        kjb.display(avatar, APIconfig.IMG_BASEURL + logo, avatar.getWidth(), avatar.getHeight(), R.mipmap.default_head_image);
        final int user_id = reply.getSender_id();
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DGUserInfoActivity.class);
                intent.putExtra("user_id", user_id);
                context.startActivity(intent);
            }
        });

        //昵称
        subhead.setText(reply.getSender_name());

        //时间
        caption.setText(StringUtils.friendlyTime(reply.getTime()));

        //内容

        String reply_conmment = reply.getContent();
        if(reply.getReplied_name()!=null&&!"".equals(reply.getReplied_name())&&reply.getReplied_name().length()!=0){
            reply_conmment = "回复"+reply.getReplied_name()+":"+reply_conmment;
        }
        SpannableString content = SpecialTextUtils.getWeiboContent(
                context, comment, reply_conmment);
        comment.setText(content);


    }

    @Override
    public void onClick(View v) {

    }
}
