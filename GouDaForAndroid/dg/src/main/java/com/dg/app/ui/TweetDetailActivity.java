package com.dg.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.adapter.EmotionGvAdapter;
import com.dg.app.adapter.EmotionPagerAdapter;
import com.dg.app.adapter.StatusGridImgsAdapter;
import com.dg.app.adapter.StatusReplysAdapter;
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.Moment;
import com.dg.app.bean.Reply;
import com.dg.app.bean.ReplyListResponse;
import com.dg.app.bean.ReplyResponse;
import com.dg.app.bean.Replys;
import com.dg.app.ui.toast.DGToast;
import com.dg.app.ui.widget.EmptyLayout;
import com.dg.app.ui.widget.WrapHeightGridView;
import com.dg.app.ui.widget.listview.PullToRefreshBase;
import com.dg.app.ui.widget.listview.PullToRefreshList;
import com.dg.app.util.DGImageUtils;
import com.dg.app.util.DisplayUtils;
import com.dg.app.util.EmotionUtils;
import com.dg.app.util.Parser;
import com.dg.app.util.SpecialTextUtils;
import com.dg.app.util.TDevice;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.message.PushAgent;

import org.apache.http.Header;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by xianxiao on 2015/10/16.
 */
public class TweetDetailActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{

    // 跳转到写评论页面code
    private static final int REQUEST_CODE_WRITE_COMMENT = 2333;

    private final KJBitmap kjb = new KJBitmap();

    private KJHttp kjh;

    private int moment_id;
    private int userid;
    private int moment_pub_id;
    private String content;
    private int replied;

    //标题栏
    private LinearLayout layout_tweetdetail_title;
    private ImageView iv_tweetdetail_titlebar_back;
    private ImageView iv_tweetdetail_titlebar_share;

    //状态信息
    private View status_detail_info;
    private ImageView iv_avatar;
    private TextView tv_subhead;
    private TextView tv_caption;
    private ImageView iv_share;
    private TextView tv_content;

    private FrameLayout include_status_image;
    private WrapHeightGridView gv_images;
    private ImageView iv_image;

    private TextView tv_tweet_time;

    private ImageView iv_tweet_like;
    private TextView tv_tweet_like_num;

    private ImageView iv_tweet_comment;
    private TextView tv_tweet_comment_num;

    // listView - 下拉刷新控件
    private PullToRefreshList mRefreshLayout;

    private ListView mListView;
    private EmptyLayout mEmptyLayout;

    //底部发布回复栏
    private LinearLayout ll_tweetdetail_replys;
    private ImageView iv_emoji_btn;
    private EditText et_reply_content;
    private TextView tv_send_btn;

    // 表情选择面板
    private LinearLayout ll_emotion_dashboard;
    private ViewPager vp_emotion_dashboard;
    private EmotionPagerAdapter emotionPagerGvAdapter;

    // 详情页的微博信息
    private Moment item;
    // 是否需要滚动至评论部分
    private boolean scroll2Comment;
    // 评论当前已加载至的页数
    private long curPage = 1;
    private final Set<Reply> replys = new TreeSet<Reply>();
    //状态列表适配器
//    private StatusReplyAdapter adapter;
    private StatusReplysAdapter adapter;

    //底部弹出菜单
    private TweetPopupWindow tweetPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

//        // 获取intent传入的信息
        item = (Moment) getIntent().getSerializableExtra("status");
        //初始化
        moment_id = item.getMoment_id();
        moment_pub_id = item.getPublisher_id();
        userid = AppContext.getInstance().getLoginUid();
        replied = -1;
        content="";

        //初始化网络操作(放在初始化ListView前)
        initHttp();

        // 初始化View
        initView();

    }


    //    初始化视图
    private void initView() {
        // title - 标题栏
        initTitle();
        // listView headerView - 微博信息
        initDetailHead();

        // listView - 下拉刷新控件
        initListView();

        // 设置数据信息
        setData();
        // bottom_control - 底部评论栏
        initControlBar();
        initEmotion();
    }


    //初始化标题布局
    private void initTitle() {
        //标题栏
        layout_tweetdetail_title = (LinearLayout)findViewById(R.id.layout_tweetdetail_title);//标题栏布局
        iv_tweetdetail_titlebar_back = (ImageView)layout_tweetdetail_title.findViewById(R.id.iv_tweetdetail_titlebar_back);//返回按钮
        iv_tweetdetail_titlebar_back.setOnClickListener(this);
        iv_tweetdetail_titlebar_share = (ImageView)layout_tweetdetail_title.findViewById(R.id.iv_tweetdetail_titlebar_share);//分享按钮
        iv_tweetdetail_titlebar_share.setOnClickListener(this);
    }

    //初始化状态布局
    private void initDetailHead() {
        status_detail_info = View.inflate(this, R.layout.list_tweets_item_new, null);//状态布局

        status_detail_info.setBackgroundResource(R.color.white);
        iv_avatar = (ImageView) status_detail_info.findViewById(R.id.msg_item_img_head);//头像
        iv_avatar.setOnClickListener(this);
        tv_subhead = (TextView) status_detail_info.findViewById(R.id.tv_subhead);//昵称
        tv_subhead.setOnClickListener(this);
        tv_caption = (TextView) status_detail_info.findViewById(R.id.tv_caption);//额外信息
        iv_share = (ImageView) status_detail_info.findViewById(R.id.iv_share);//向下箭头
        iv_share.setVisibility(View.GONE);
        tv_content = (TextView) status_detail_info.findViewById(R.id.tv_content);//状态正文

        include_status_image = (FrameLayout) status_detail_info.findViewById(R.id.moment_image);//多图帧布局容器
        gv_images = (WrapHeightGridView) status_detail_info.findViewById(R.id.gv_images);//自适应网格布局
        iv_image = (ImageView) status_detail_info.findViewById(R.id.iv_image);//单图

        tv_tweet_time = (TextView) status_detail_info.findViewById(R.id.tv_tweet_time);
        iv_tweet_like = (ImageView) status_detail_info.findViewById(R.id.iv_tweet_like);
        iv_tweet_like.setOnClickListener(this);
        tv_tweet_like_num = (TextView) status_detail_info.findViewById(R.id.tv_tweet_like_num);

        iv_tweet_comment = (ImageView) status_detail_info.findViewById(R.id.iv_tweet_comment);
        tv_tweet_comment_num = (TextView) status_detail_info.findViewById(R.id.tv_tweet_comment_num);

    }


    //初始化状态显示栏
    private void initListView() {
        mEmptyLayout = (EmptyLayout) findViewById(R.id.empty_layout);
//        mEmptyLayout.setVisibility(View.GONE);
        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                refresh();
            }
        });

        // listView - 下拉刷新控件
        mRefreshLayout = (PullToRefreshList) findViewById(R.id.plv_tweet_detail);
        mRefreshLayout.setPullLoadEnabled(true);
        mRefreshLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                refresh(0);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                refresh();
            }
        });
        //获取数据显示的listview
        mListView = mRefreshLayout.getRefreshView();
        mListView.setSelector(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        adapter = new StatusReplysAdapter(TweetDetailActivity.this, mListView, replys,
                R.layout.list_comment_item);
        mListView.setAdapter(adapter);

        mListView.addHeaderView(status_detail_info);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Reply reply = (Reply) parent.getItemAtPosition(position);
                    replied = reply.getSender_id();
                    et_reply_content.setHint("回复" + reply.getSender_name() + ":");
                }
            }
        });

        fillUI();

    }

    //初始化底部评论栏
    private void initControlBar() {
        ll_tweetdetail_replys = (LinearLayout) findViewById(R.id.ll_tweetdetail_replys);

        et_reply_content = (EditText) findViewById(R.id.et_reply_content);
        tv_send_btn = (TextView) findViewById(R.id.tv_send_btn);
        tv_send_btn.setOnClickListener(this);

        iv_emoji_btn = (ImageView) findViewById(R.id.iv_emoji_btn);
        iv_emoji_btn.setOnClickListener(this);
        // 表情选择面板
        ll_emotion_dashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
        vp_emotion_dashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);
    }

    /**
     *  初始化表情面板内容
     */
    private void initEmotion() {
        int screenWidth = DisplayUtils.getScreenWidthPixels(this);
        int spacing = DisplayUtils.dp2px(this, 8);

        int itemWidth = (screenWidth - spacing * 8) / 7;
        int gvHeight = itemWidth * 3 + spacing * 4;

        List<GridView> gvs = new ArrayList<GridView>();
        List<String> emotionNames = new ArrayList<String>();
        for(String emojiName : EmotionUtils.emojiMap.keySet()) {
            emotionNames.add(emojiName);

            if(emotionNames.size() == 20) {
                GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
                gvs.add(gv);

                emotionNames = new ArrayList<String>();
            }
        }

        if(emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
            gvs.add(gv);
        }

        emotionPagerGvAdapter = new EmotionPagerAdapter(gvs);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
        vp_emotion_dashboard.setLayoutParams(params);
        vp_emotion_dashboard.setAdapter(emotionPagerGvAdapter);
    }

    /**
     * 创建显示表情的GridView
     */
    private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
        GridView gv = new GridView(this);
        gv.setBackgroundResource(R.color.bg_gray);
        gv.setSelector(R.color.transparent);
        gv.setNumColumns(7);
        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);

        EmotionGvAdapter adapter = new EmotionGvAdapter(this, emotionNames, itemWidth);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);

        return gv;
    }

    //设置数据
    private void setData() {
        if(item!=null && item instanceof Moment){

            // 由于头像地址默认加了一段参数需要去掉
            String headUrl = item.getUserlogo();

            String logo = DGImageUtils.toSmallImageURL(headUrl);
            kjb.display(iv_avatar, APIconfig.IMG_BASEURL + logo, iv_avatar.getWidth(),
                    iv_avatar.getHeight(), R.mipmap.default_head_image);//头像

            final int userId = item.getPublisher_id();

            iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TweetDetailActivity.this, DGUserInfoActivity.class);
                    intent.putExtra("user_id",userId);
                    startActivity(intent);
                }
            });

            tv_subhead.setText(item.getPublisher_name());//昵称
            tv_subhead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TweetDetailActivity.this, DGUserInfoActivity.class);
                    intent.putExtra("user_id",userId);
                    startActivity(intent);
                }
            });

            tv_caption.setText("服务器未给");//距离

            //设置分享按钮点击事件监听器
            iv_share.setVisibility(View.GONE);

            //设置状态正文
            if (TextUtils.isEmpty(item.getContent())) {
                tv_content.setVisibility(View.GONE);
            } else {
                tv_content.setVisibility(View.VISIBLE);
                tv_content.setText(SpecialTextUtils.getWeiboContent(this, tv_content, item.getContent()));
            }

            //设置图片
            setImages(item, include_status_image, gv_images, iv_image);

            /**
             * 设置底部信息栏
             */
            //设置喜欢按钮点击事件监听器
            iv_tweet_like.setOnClickListener(this);
            //喜欢数
            tv_tweet_like_num.setText(item.getLikes_num()+"");

            //设置喜欢按钮点击事件监听器
            iv_tweet_comment.setOnClickListener(this);
            //评论数
            tv_tweet_comment_num.setText(item.getReply_num()+"");
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.iv_emoji_btn:
                if(ll_emotion_dashboard.getVisibility() == View.VISIBLE) {
                    ll_emotion_dashboard.setVisibility(View.GONE);
                    iv_emoji_btn.setImageResource(R.drawable.btn_insert_emotion);
                    TDevice.showSoftKeyboard(et_reply_content);
                } else {
                    ll_emotion_dashboard.setVisibility(View.VISIBLE);
                    iv_emoji_btn.setImageResource(R.drawable.btn_insert_keyboard);
                    TDevice.hideSoftKeyboard(et_reply_content);
                }
                et_reply_content.requestFocus();
                break;
            case R.id.tv_send_btn:
                sendReply();
                break;
            case R.id.iv_tweetdetail_titlebar_back:
                finish();
                break;
            case R.id.iv_tweetdetail_titlebar_share:
                openPopMenuWindow();
                break;

        }
    }

    private void sendReply() {
        content = et_reply_content.getText().toString();
        if(TextUtils.isEmpty(content)) {
            Toast.makeText(TweetDetailActivity.this,"发布消息不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!TextUtils.isEmpty(userid+"")&&!TextUtils.isEmpty(moment_id+"")){
            DGApi.publishReply(moment_id,moment_pub_id, userid, content, replied, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    ReplyResponse replyResponse = XmlUtils.toBean(ReplyResponse.class,responseBody);
                    if(replyResponse.getCode()==0){
                        et_reply_content.setText("");
                        if(ll_emotion_dashboard.getVisibility() == View.VISIBLE) {
                            ll_emotion_dashboard.setVisibility(View.GONE);
                        }
                        TDevice.hideSoftKeyboard(et_reply_content);
                        refresh(0);
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
    }

    private void openPopMenuWindow() {
        //创建弹出菜单，并添加监听器
        tweetPopupWindow = new TweetPopupWindow(TweetDetailActivity.this,itemsOnclick);
        //显示窗口
        //设置layout在PopupWindow中显示的位置
        tweetPopupWindow.showAtLocation(this.findViewById(R.id.rl_tweet_detail), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    //弹出菜单点击事件
    private View.OnClickListener itemsOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tweetPopupWindow.dismiss();
            switch (v.getId()) {

                case R.id.btn_follow_ta:
                    Toast.makeText(TweetDetailActivity.this,"关注TA",Toast.LENGTH_LONG).show();
                    DGToast.makeText(TweetDetailActivity.this, R.mipmap.whitebone, "主人你成功\n" +
                            "关注了对方", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_begin_chat:
                    Toast.makeText(TweetDetailActivity.this,"发起聊天",Toast.LENGTH_LONG).show();

                    break;
                case R.id.btn_invite_liugou:
                    Toast.makeText(TweetDetailActivity.this,"约TA一起遛狗",Toast.LENGTH_LONG).show();

                    break;
                case R.id.btn_invite_xiangqin:
                    Toast.makeText(TweetDetailActivity.this,"想和TA的狗狗相亲",Toast.LENGTH_LONG).show();

                    break;
                case R.id.btn_share2other:
                    Toast.makeText(TweetDetailActivity.this,"分享到其他平台",Toast.LENGTH_LONG).show();
                    break;
                case R.id.btn_jubao:
                    Toast.makeText(TweetDetailActivity.this,"举报",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAdapter = parent.getAdapter();
        if(itemAdapter instanceof EmotionGvAdapter) {
            EmotionGvAdapter emotionAdapter = (EmotionGvAdapter) itemAdapter;

            if(position == emotionAdapter.getCount() - 1) {
                et_reply_content.dispatchKeyEvent(
                        new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            } else {
                String emotionName = emotionAdapter.getItem(position);

                int curPosition = et_reply_content.getSelectionStart();
                StringBuilder sb = new StringBuilder(et_reply_content.getText().toString());
                sb.insert(curPosition, emotionName);

                SpannableString weiboContent = SpecialTextUtils.getWeiboContent(
                        this, et_reply_content, sb.toString());
                et_reply_content.setText(weiboContent);

                et_reply_content.setSelection(curPosition + emotionName.length());
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 如果Back键返回,取消发评论等情况,则直接return,不做后续处理
        if(resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_WRITE_COMMENT:
                // 如果是评论发送成功的返回结果,则重新加载最新评论,同时要求滚动至评论部分
                boolean sendCommentSuccess = data.getBooleanExtra("sendCommentSuccess", false);
                if(sendCommentSuccess) {
                    scroll2Comment = true;
                    refresh(0);
                }
                break;

            default:
                break;
        }
    }

    //设置多图网格显示
    private void setImages(final Moment status, FrameLayout vgContainer, GridView gvImgs, final ImageView ivImg) {
        if (status == null) {
            return;
        }

        List<String> picUrls =new ArrayList<>();//获取状态图片列表
        //TODO  API有待需改
        String imgs = status.getImages();

        if(imgs!=null&&!"".equals(imgs)){
            String[] imgs_array = imgs.split(",");
            for(String img:imgs_array){
                picUrls.add(img);
            }
        }


        if (picUrls != null && picUrls.size() == 1) {

            vgContainer.setVisibility(View.VISIBLE);
            gvImgs.setVisibility(View.GONE);
            ivImg.setVisibility(View.VISIBLE);

            String first_pic = DGImageUtils.toSmallImageURL(picUrls.get(0));
            kjb.display(ivImg, APIconfig.IMG_BASEURL+first_pic,ivImg.getWidth(),ivImg.getHeight(),R.mipmap.default_image);

            ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TweetDetailActivity.this, ImagesBrowserActivity.class);
                    intent.putExtra("status", status);
                    startActivity(intent);
                }
            });
        } else if (picUrls != null && picUrls.size() > 1) {

            vgContainer.setVisibility(View.VISIBLE);
            gvImgs.setVisibility(View.VISIBLE);
            ivImg.setVisibility(View.GONE);

            StatusGridImgsAdapter imagesAdapter = new StatusGridImgsAdapter(TweetDetailActivity.this, picUrls);
            gvImgs.setAdapter(imagesAdapter);
            gvImgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent intent = new Intent(TweetDetailActivity.this, ImagesBrowserActivity.class);
                    intent.putExtra("status", status);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        } else {
//            Toast.makeText(TweetDetailActivity.this, "没有图片", Toast.LENGTH_SHORT).show();
            vgContainer.setVisibility(View.GONE);
        }
    }

    //初始化数据
    protected void initHttp() {
        //配置http连接属性
        HttpConfig config = new HttpConfig();
        config.cacheTime = 5;
        config.useDelayCache = true;
        kjh = new KJHttp(config);
    }

    /**
     * 首次进入时填充数据
     */
    private void fillUI() {
        refresh(0);
    }

    private void refresh() {
        double page = replys.size() / 10;
        page += 1.9; // 因为服务器返回的可能会少于20条，所以采用小数进一法加载下一页
        refresh((int) page);
    }

    /**
     * 根据微博ID返回某条微博的评论列表
     *
     * @param page
     *            页数
     */
    private void refresh(final int page) {

        try{
            // 如果是加载第一页(第一次进入,下拉刷新)时,先清空已有数据
            if (page == 0) {
                replys.clear();
            }

            HttpParams params = new HttpParams();
            params.put("moment_id",item.getMoment_id());
            params.put("page",page);
            params.put("num","10");
            params.put("device", "android");
            params.put("time", System.currentTimeMillis() / 1000 +"");


//            kjh.post(APIconfig.API_BASEURL+APIconfig.GROUP_REPLYLIST, params, new HttpCallBack() {
//                @Override
//                public void onSuccess(String t) {
//                    super.onSuccess(t);
//                    Log.i("====>t", t);
//                    ReplyListResponse replyListResponse = Parser.xmlToBean(ReplyListResponse.class, t);
//                    Log.i("====>replyListResponse", replyListResponse.toString());
//                    if(replyListResponse.getCode()==0){
//                        Replys temp_replys = replyListResponse.getReplies();
//                        Log.i("====>temp_replys", temp_replys.toString());
//                        if(temp_replys!=null){
//                            List<Reply> datas = temp_replys.getReplys();
//                            Log.i("====>datas", datas.toString());
//                            if(datas!=null){
//                                // 将获取到的数据添加至列表中,重复数据不添加
//                                for (Reply data : datas) {
//                                    if (!replys.contains(data)) {
//                                        replys.add(data);
//                                        Log.i("data", data.toString());
//                                    }
//                                }
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                    }
//                    mEmptyLayout.dismiss();
//                }
//
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                    mRefreshLayout.onPullDownRefreshComplete();
//                    mRefreshLayout.onPullUpRefreshComplete();
//                }
//
//                @Override
//                public void onFailure(int errorNo, String strMsg) {
//                    super.onFailure(errorNo, strMsg);
//
//                    AppContext.showToast("网络出错:" + strMsg);
//
//                    if (adapter != null && adapter.getCount() > 0) {
//                        return;
//                    } else {
//                        mEmptyLayout.setErrorType(EmptyLayout.NODATA);
//                    }
//                }
//            });


            kjh.post(APIconfig.API_BASEURL + APIconfig.GROUP_REPLYLIST, params, new HttpCallBack() {
                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);

                    ReplyListResponse replyListResponse = Parser.xmlToBean(ReplyListResponse.class, t);
                    if (replyListResponse.getCode() == 0) {
                        Replys temp_replys = replyListResponse.getReplies();
                            List<Reply> datas = temp_replys.getReplys();
                            if (datas != null) {
                                // 将获取到的数据添加至列表中,重复数据不添加
                                for (Reply data : datas) {
                                    if (!replys.contains(data)) {
                                        replys.add(data);
                                        Log.i("data", data.toString());
                                    }
                                }
                                adapter.refresh(replys);
                                mEmptyLayout.dismiss();
                            }else{
                                adapter.refresh(replys);
                                mEmptyLayout.dismiss();
//                                mEmptyLayout.setErrorType(EmptyLayout.NODATA);
                            }
                    }else{
                        AppContext.showToast(replyListResponse.getMsg());
//                        mEmptyLayout.setErrorType(EmptyLayout.NODATA);
                    }

                }
                @Override
                public void onFinish() {
                    super.onFinish();
                    mRefreshLayout.onPullDownRefreshComplete();
                    mRefreshLayout.onPullUpRefreshComplete();
                }

                @Override
                public void onFailure(int errorNo, String strMsg) {
                    super.onFailure(errorNo, strMsg);

                    AppContext.showToast("网络出错:" + strMsg);

                    if (adapter != null && adapter.getCount() > 0) {
                        return;
                    } else {
                        mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                    }
                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }



    }



}



