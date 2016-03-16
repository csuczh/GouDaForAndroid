/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dg.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.bean.Message;
import com.dg.app.ui.widget.CollapsibleTextView;
import com.dg.app.util.FileUtil;
import com.dg.app.util.SpecialTextUtils;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.utils.StringUtils;
import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;

import java.util.Collection;

/**
 * 动弹列表适配器
 * 
 * @author kymjs (http://www.kymjs.com)
 * 
 */
public class MessageAdapter extends KJAdapter<Message> {

    private final KJBitmap kjb = new KJBitmap();
    private Context context;

    public MessageAdapter(AbsListView view, Collection<Message> mDatas,
                          int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }

    public MessageAdapter(Context context,AbsListView view, Collection<Message> mDatas,
                          int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context = context;
    }

    //TODO  修改我的消息页面的数据适配
    @Override
    public void convert(AdapterHolder helper, Message item, boolean isScrolling) {
        // 头像
        String headUrl = item.getPublisher_logo();

        if (isScrolling) {
            kjb.displayCacheOrDefult(helper.getView(R.id.msg_item_img_head),
                    APIconfig.IMG_BASEURL+headUrl, R.mipmap.default_head_image);
        } else {
            kjb.display(helper.getView(R.id.msg_item_img_head), APIconfig.IMG_BASEURL+headUrl, 45,
                    45, R.mipmap.default_head_image);
        }

        //发布者昵称
        helper.setText(R.id.tv_head, item.getPublisher_name());

        //正文
        TextView content = helper
                .getView(R.id.tv_comment);

        content.setText(SpecialTextUtils.getWeiboContent(context,content,item.getContent()));

        //时间
        helper.setText(R.id.tv_time,
                StringUtils.friendlyTime(item.getPublish_time()));

        //状态
        ImageView image = helper.getView(R.id.iv_moment_img);
        TextView text = helper.getView(R.id.tv_moment_content);
        if (StringUtils.isEmpty(item.getImage())) {
            image.setVisibility(View.GONE);
            text.setText(SpecialTextUtils.getWeiboContent(context, content, item.getOrigin()));
        } else {
            image.setVisibility(View.VISIBLE);
            if (isScrolling) {
                kjb.displayCacheOrDefult(image, APIconfig.IMG_BASEURL+item.getImage(),
                        R.drawable.pic_bg);
            } else {
                kjb.displayWithLoadBitmap(image, APIconfig.IMG_BASEURL+item.getImage(),
                        R.drawable.pic_bg);
            }
        }
    }


}
