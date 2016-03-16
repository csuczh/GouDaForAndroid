package com.dg.app.ui.dialog;

import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.api.ApiHttpClient;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.DialogEntity;
import com.dg.app.bean.Foster;
import com.dg.app.bean.FosterList;
import com.dg.app.bean.Province;
import com.dg.app.bean.User;
import com.dg.app.bean.Walk;
import com.dg.app.bean.WalkList;
import com.dg.app.bean.XQ;
import com.dg.app.bean.XQList;
import com.dg.app.ui.SharePopupWindow;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.util.ComputeDistance;
import com.dg.app.util.XmlUtils;
import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.RoundImageView;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import bolts.Bolts;

/**
 * Created by czh on 2015/9/20.
 */

public class InviteDetailDialog extends Dialog {
    //设置页面展示的消息
    DialogEntity dialogEntity;
    //狗的显示
    private RoundImageView dgPicture;
    private ImageView dgSex;
    private TextView  dgNickname;
    //人的显示
    private RoundImageView peoplepicture;
    private ImageView peopleSex;
    private TextView peopleNickname;
    //消息内容
    private TextView content;
    //消息时效时间段
    private TextView start_end_time;
    //按钮
    private Button inviteButton;
    //距离和发消息时间
    private TextView distance;
    private TextView posttime;
    //关闭view
    TextView closeDialog;
    //当前task
    private int task;
    //请求网络
    private int invite_id;
    private double lng;
    private double lat;
    //设置全局上下文
    private Context context;
    private Activity activity;
    int user_id;

    boolean is_allow_chat=true;
    boolean is_allow_share=false;
    DGNoticeToast dgNoticeToast;

    private SharePopupWindow sharePopupWindow;
    public InviteDetailDialog(Activity context) {
        super(context);
        setCustomDialog();
        this.context=context;
        activity=context;
        user_id=Integer.parseInt(AppContext.getInstance().getProperty("dgUser.userid"));
        dgNoticeToast = new DGNoticeToast(context);

    }
    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_custom,null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(mView);
        setUpViews();
    }
    //获取相应的组件
    public  void setUpViews()
    {
        dgPicture=(RoundImageView)this.findViewById(R.id.dog_image);
        dgSex=(ImageView)this.findViewById(R.id.dialog_dg_sex);
        dgNickname=(TextView)this.findViewById(R.id.dialog_dg_nickname);
        peoplepicture=(RoundImageView)this.findViewById(R.id.people_image);
        peopleSex=(ImageView)this.findViewById(R.id.dialog_people_sex);
        peopleNickname=(TextView)this.findViewById(R.id.dialog_people_nickname);
        content=(TextView)this.findViewById(R.id.dialog_content);
        start_end_time=(TextView)this.findViewById(R.id.dialog_time);
        distance=(TextView)this.findViewById(R.id.dialog_distance);
        posttime=(TextView)this.findViewById(R.id.dialog_post_time);
        inviteButton=(Button)this.findViewById(R.id.dialog_button_accept);
        inviteButton.setEnabled(false);
        inviteButton.setVisibility(View.INVISIBLE);
        closeDialog=(TextView)this.findViewById(R.id.dialog_close);
    }

    @Override
    public void show() {
        super.show();
//        initView();
    }

    public void initView()
    {

         if(task==0) {
             DGApi.getWD("android", user_id, invite_id, new AsyncHttpResponseHandler() {
                 @Override
                 public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                     try {
                         WalkList list = parseWalkList(new ByteArrayInputStream(responseBody));
                         setWalk(list.getList());
//                         System.out.println("list.getCode()----->"+list.getCode()+list.getList().getWalk_id());
//                         if(list.getCode()==400) {
//
//
//                         }
//                         else {
//                             activity.runOnUiThread(new Runnable() {
//                                 @Override
//                                 public void run() {
//                                     Toast.makeText(context,"信息被屏蔽了！",Toast.LENGTH_LONG).show();
//                                     dismiss();
//                                 }
//                             });
//                         }
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }

                 @Override
                 public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                 }
             });
         }else  if(task==1)
         {
                DGApi.getXQ("android", user_id, invite_id, new AsyncHttpResponseHandler() {
                  @Override
                  public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                      XQList list = null;
                      try {
                          list = parseXQList(new ByteArrayInputStream(responseBody));

                          setDate(list.getList());

                      } catch (Exception e) {
                          e.printStackTrace();
                      }

                  }

                  @Override
                  public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                  }
              });
         }
        else {
             DGApi.getJY("android", user_id, invite_id, new AsyncHttpResponseHandler() {
                 @Override
                 public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                     try {
                         FosterList fosterList = parseFosterList(new ByteArrayInputStream(responseBody));

                         setFoster(fosterList.getList());

                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }

                 @Override
                 public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                 }
             });
         }


    }
    public void setWalk(final Walk walk)
    {
        inviteButton.setEnabled(true);
        inviteButton.setVisibility(View.VISIBLE);
        is_allow_chat=true;
        is_allow_share=false;

        String dogURl= ApiHttpClient.IMAGE_URL+"/"+walk.getDog_log();
        String peopleURl=ApiHttpClient.IMAGE_URL+"/"+walk.getUser_logo();
        Picasso.with(context).load(dogURl).placeholder(R.mipmap.default_avatar).into(dgPicture);

        if(walk.getDog_sex()==0)
           dgSex.setBackgroundResource(R.mipmap.userinfo_icon_male);
        else
           dgSex.setBackgroundResource(R.mipmap.userinfo_icon_female);
        dgNickname.setText(walk.getDog_nickname());
        Picasso.with(context).load(peopleURl).into(peoplepicture);

        if(walk.getUser_sex()==0)
            peopleSex.setBackgroundResource(R.mipmap.userinfo_icon_male);
        else
            peopleSex.setBackgroundResource(R.mipmap.userinfo_icon_female);
        peopleNickname.setText(walk.getPublisher_name());
        content.setHeight(100);
        content.setText(walk.getExplain());
        start_end_time.setVisibility(View.GONE);

        Double lng=Double.parseDouble(AppContext.getInstance().getProperty("lng"));
        Double lat=Double.parseDouble(AppContext.getInstance().getProperty("lat"));
        Double distance1= ComputeDistance.GetDistance(lat, lng, walk.getLat(), walk.getLng());
        distance.setText(distance1.toString()+"km");
        posttime.setText("发布于"+walk.getTime());
        inviteButton.setText("接受邀请");
    inviteButton.setBackgroundResource(R.drawable.red_button);
        if(walk.getIs_accepted()==1) {
            inviteButton.setText("已经发送了邀请");
            inviteButton.setVisibility(View.VISIBLE);
            is_allow_chat=false;
            inviteButton.setBackgroundResource(R.drawable.darkgray_button);
        }
        if(walk.getIs_agreed()==1)
        {
            inviteButton.setText("已经同意了邀请");
            inviteButton.setVisibility(View.VISIBLE);
            is_allow_chat=false;
            inviteButton.setBackgroundResource(R.drawable.darkgray_button);
        }

        if(walk.getPublisher_id()==user_id)
        {
            inviteButton.setText("分享");
            inviteButton.setVisibility(View.VISIBLE);
            inviteButton.setBackgroundResource(R.drawable.blue_button);
            is_allow_share=true;
        }
        Map<String , com.easemob.chatuidemo.domain.User> friends=((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
        final boolean isfriend=friends.containsKey(walk.getEasemob_id());
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_allow_share == true) {
//                    Toast.makeText(activity, "点击分享", Toast.LENGTH_LONG).show();
                    dgNoticeToast.showSuccess("点击分享");
                    popupwindow();
                    return;
                }
                if (is_allow_chat == false) {
//                    Toast.makeText(activity, "您" + inviteButton.getText().toString(), Toast.LENGTH_LONG).show();
                    dgNoticeToast.showSuccess("您" + inviteButton.getText().toString());
                    return;
                }

                if (!isfriend) {

                    try {
                        EMContactManager.getInstance().addContact(walk.getEasemob_id(), "接受了你的遛狗邀请");

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    String name = AppContext.getInstance().getProperty("dgUser.easemob_id");
                    EMConversation conversation = EMChatManager.getInstance().getConversation(name);
                    EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                    TextMessageBody txtBody = new TextMessageBody("接受了你的遛狗邀请");
                    message.addBody(txtBody);
                    message.setReceipt(walk.getEasemob_id());
                    conversation.addMessage(message);
                    EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(int i, String s) {

                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });

                }

                sendAccept("walk", user_id, invite_id);

                 }
             });



    }
    //设置寄养的数据
    public void setFoster(final Foster foster)
    {

        inviteButton.setEnabled(true);
        inviteButton.setVisibility(View.VISIBLE);
        is_allow_chat=true;
        is_allow_share=false;

        String dogURl= ApiHttpClient.IMAGE_URL+"/"+foster.getDog_log();
        String peopleURl=ApiHttpClient.IMAGE_URL+"/"+foster.getUser_logo();
        if (foster != null && foster.getDog_log() != null&&foster.getDog_log()!="") {
            Picasso.with(context).load(dogURl).into(dgPicture);
        } else {
            Picasso.with(context).load(R.mipmap.default_avatar).into(dgPicture);
        }

        if(foster.getDog_sex()==0)
            dgSex.setBackgroundResource(R.mipmap.userinfo_icon_male);
        else
            dgSex.setBackgroundResource(R.mipmap.userinfo_icon_female);
        dgNickname.setText(foster.getDog_nickname());

        if (foster != null && foster.getUser_logo() != null&&foster.getUser_logo()!="") {
            Picasso.with(context).load(peopleURl).into(peoplepicture);
        } else {
            Picasso.with(context).load(R.mipmap.default_avatar).into(peoplepicture);
        }
        if(foster.getUser_sex()==0)
            peopleSex.setBackgroundResource(R.mipmap.userinfo_icon_male);
        else
            peopleSex.setBackgroundResource(R.mipmap.userinfo_icon_female);
        peopleNickname.setText(foster.getPublisher_name());
        content.setText(foster.getExplain());
        start_end_time.setVisibility(View.VISIBLE);
        String starts[]=foster.getDeadline().split(" ");
        String ends[]=foster.getOfftime().split(" ");
        start_end_time.setText(starts[0] + "-" + ends[0]);
        Double lng=Double.parseDouble(AppContext.getInstance().getProperty("lng"));
        Double lat=Double.parseDouble(AppContext.getInstance().getProperty("lat"));
        Double distance1= ComputeDistance.GetDistance(lat, lng, foster.getLat(), foster.getLng());
        distance.setText(distance1.toString()+"km");

        posttime.setText("发布于" + foster.getTime());
        inviteButton.setText("接受邀请");
        inviteButton.setBackgroundResource(R.drawable.red_button);

        if(foster.getIs_accepted()==1) {
            inviteButton.setText("已经发送了邀请");
            inviteButton.setVisibility(View.VISIBLE);
            is_allow_chat=false;
            inviteButton.setBackgroundResource(R.drawable.darkgray_button);

        }
        if(foster.getIs_agreed()==1)
        {
            inviteButton.setText("已经同意了邀请");
            inviteButton.setVisibility(View.VISIBLE);
            is_allow_chat=false;
            inviteButton.setBackgroundResource(R.drawable.darkgray_button);
        }
        if(foster.getPublisher_id()==user_id)
        {
            inviteButton.setText("分享");
            inviteButton.setVisibility(View.VISIBLE);
            inviteButton.setBackgroundResource(R.drawable.blue_button);
            is_allow_share=true;
        }
        Map<String , com.easemob.chatuidemo.domain.User> friends=((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
        final boolean isfriend=friends.containsKey(foster.getEasemob_id());
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_allow_share==true)
                {
//                    Toast.makeText(activity,"点击分享",Toast.LENGTH_LONG).show();
                    dgNoticeToast.showSuccess("点击分享");
                    popupwindow();
                    return;
                }
                if(is_allow_chat==false)
                {
//                    Toast.makeText(activity,"您"+inviteButton.getText().toString(),Toast.LENGTH_LONG).show();
                    dgNoticeToast.showSuccess("您" + inviteButton.getText().toString());
                    return;
                }

                if(!isfriend)
                {
                    try {
                        EMContactManager.getInstance().addContact(foster.getEasemob_id(), "接受了你的遛狗邀请");
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }else{
                    String name= AppContext.getInstance().getProperty("dgUser.easemob_id");
                    EMConversation conversation=EMChatManager.getInstance().getConversation(name);
                    EMMessage message=EMMessage.createSendMessage(EMMessage.Type.TXT);
                    TextMessageBody txtBody=new TextMessageBody("接受了你的寄养邀请");
                    message.addBody(txtBody);
                    message.setReceipt(foster.getEasemob_id());
                    conversation.addMessage(message);
                    EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(int i, String s) {

                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });

                }
                sendAccept("foster", user_id, invite_id);



            }
        });


    }
    //设置相亲的数据
    public void setDate(final XQ xq)
    {

        inviteButton.setEnabled(true);
        inviteButton.setVisibility(View.VISIBLE);
        is_allow_chat=true;
        is_allow_share=false;

        String dogURl= ApiHttpClient.IMAGE_URL+xq.getDog_log().substring(1);
        String peopleURl=ApiHttpClient.IMAGE_URL+xq.getUser_logo().substring(1);
        if (xq != null && xq.getDog_log() != null&&xq.getDog_log()!="") {
            Picasso.with(context).load(dogURl).into(dgPicture);
        } else {
            Picasso.with(context).load(R.mipmap.default_avatar).into(dgPicture);
        }
        if(xq.getDog_sex()==0)
            dgSex.setBackgroundResource(R.mipmap.userinfo_icon_male);
        else
            dgSex.setBackgroundResource(R.mipmap.userinfo_icon_female);
        dgNickname.setText(xq.getDog_nickname());
        if (xq != null && xq.getUser_logo() != null&&xq.getUser_logo()!="") {
            Picasso.with(context).load(peopleURl).into(peoplepicture);
        } else {
            Picasso.with(context).load(R.mipmap.default_avatar).into(peoplepicture);
        }
        if(xq.getUser_sex()==0)
            peopleSex.setBackgroundResource(R.mipmap.userinfo_icon_male);
        else
            peopleSex.setBackgroundResource(R.mipmap.userinfo_icon_female);
        peopleNickname.setText(xq.getPublisher_name());
        content.setText(xq.getExplain());
        start_end_time.setVisibility(View.VISIBLE);
        start_end_time.setText(xq.getDog_variety());
        Double lng=Double.parseDouble(AppContext.getInstance().getProperty("lng"));
        Double lat=Double.parseDouble(AppContext.getInstance().getProperty("lat"));
        Double distance1= ComputeDistance.GetDistance(lat, lng, xq.getLat(), xq.getLng());
        distance.setText(distance1.toString()+"km");
        posttime.setText("发布于"+xq.getTime());
        inviteButton.setText("接受邀请");
        inviteButton.setBackgroundResource(R.drawable.red_button);

        if(xq.getIs_accepted()==1) {
            inviteButton.setText("已经发送了邀请");
            inviteButton.setVisibility(View.VISIBLE);
            is_allow_chat=false;
            inviteButton.setBackgroundResource(R.drawable.darkgray_button);
        }
        if(xq.getIs_agreed()==1)
        {
            inviteButton.setText("已经同意了邀请");
            inviteButton.setVisibility(View.VISIBLE);
            is_allow_chat=false;
            inviteButton.setBackgroundResource(R.drawable.darkgray_button);
        }
        if(xq.getPublisher_id()==user_id)
        {
            inviteButton.setText("分享");
            inviteButton.setVisibility(View.VISIBLE);
            inviteButton.setBackgroundResource(R.drawable.blue_button);
            is_allow_share=true;
        }
        Map<String , com.easemob.chatuidemo.domain.User> friends=((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
        final boolean isfriend=friends.containsKey(xq.getEasemob_id());
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_allow_share == true) {
                    dgNoticeToast.showSuccess("点击分享");
                    popupwindow();
                    return;
                }
                if (is_allow_chat == false) {
//                    Toast.makeText(activity, "您" + inviteButton.getText().toString(), Toast.LENGTH_LONG).show();
                    dgNoticeToast.showSuccess("您" + inviteButton.getText().toString());
                    return;
                }

                if (!isfriend) {
                    try {
                        EMContactManager.getInstance().addContact(xq.getEasemob_id(), "接受了你的遛狗邀请");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    String name = AppContext.getInstance().getProperty("dgUser.easemob_id");
                    EMConversation conversation = EMChatManager.getInstance().getConversation(name);
                    EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                    TextMessageBody txtBody = new TextMessageBody("接受了你的相亲邀请");
                    message.addBody(txtBody);
                    message.setReceipt(xq.getEasemob_id());
                    conversation.addMessage(message);
                    EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(int i, String s) {

                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });

                }

                sendAccept("date", user_id, invite_id);
            }
        });



    }
    //发送邀请
    public void sendAccept(String type,int user_id,int invite_id)
    {
        DGApi.sendAccpet("android", type, user_id, invite_id, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                inviteButton.setClickable(false);
                inviteButton.setText("已经发送了邀请");
                is_allow_chat=false;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                          dgNoticeToast.showSuccess( "已经发送邀请成功");
                    }
                });


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    //设置邀请的id
    public void setInviteid(int invite_id)
    {
          this.invite_id=invite_id;
    }
    //设置当前邀请的种类
    public void setKind(int task)
    {
        this.task=task;
    }

    public void setNegativeButton(View.OnClickListener onClickListener)
    {
        closeDialog.setOnClickListener(onClickListener);
    }

    protected WalkList parseWalkList(InputStream is) throws Exception {
        WalkList list = null;
        try {
            list = XmlUtils.toBean(WalkList.class, is);
        } catch (NullPointerException e) {
            list = new WalkList();
        }
        return list;
    }

    protected XQList parseXQList(InputStream is) throws Exception {
        XQList list = null;
        try {
            list = XmlUtils.toBean(XQList.class, is);
        } catch (NullPointerException e) {
            list = new XQList();
        }
        return list;
    }

    protected FosterList parseFosterList(InputStream is) throws Exception {
        FosterList list = null;
        try {
            list = XmlUtils.toBean(FosterList.class, is);
        } catch (NullPointerException e) {
            list = new FosterList();
        }
        return list;
    }


    //弹出窗口
    public void popupwindow()
    {

        //创建弹出菜单，并添加监听器
        sharePopupWindow = new SharePopupWindow(activity, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.wx_quan:

                        Toast.makeText(activity,"wx_quan",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.wx_friend:
                        Toast.makeText(activity,"wx_friend",Toast.LENGTH_LONG).show();

                        break;
                    case R.id.qq_quan:
                        Toast.makeText(activity,"qq_quan",Toast.LENGTH_LONG).show();

                        break;
                    case R.id.qq_friend:
                        Toast.makeText(activity,"qq_friend",Toast.LENGTH_LONG).show();

                        break;
                    case R.id.xl_weibo:
                        Toast.makeText(activity,"xl_weibo",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.share_copy:
                        Toast.makeText(activity,"share_copy",Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        });
        //显示窗口
        //设置layout在PopupWindow中显示的位置
        sharePopupWindow.showAtLocation(this.findViewById(R.id.dialog_invite), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
