package com.dg.app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.swipemenulistview.SwipeMenu;
import com.swipemenulistview.SwipeMenuCreator;
import com.swipemenulistview.SwipeMenuItem;
import com.swipemenulistview.SwipeMenuListView;
import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseFragment;
import com.dg.app.bean.Bean;
import com.dg.app.bean.DemoModel;
import com.dg.app.bean.Em_User_List;
import com.dg.app.bean.Em_Users;
import com.dg.app.fragment.FriendsMessagesFragment;

import com.dg.app.fragment.TestFindListFragment;
import com.dg.app.util.ComputeDistance;
import com.dg.app.util.XmlUtils;
import com.dg.app.widget.RandomColor;


import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.chatuidemo.utils.DateUtils;
import com.easemob.chatuidemo.utils.SmileUtils;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;


import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class ChatActivity extends AppCompatActivity implements  EMEventListener{

    FrameLayout inviteLayout;//邀请的布局文件
    FrameLayout chatLayout;//聊天的布局、
    FragmentManager fm;//fragment管理器
    int state=0;//0表示邀请信息，1表示聊天信息
    private boolean isConflict=false;//账号冲突
    private boolean isCurrentAccountRemoved=false;
    private SwipeMenuListView InvitelistView;//邀请的聊天
    private SwipeMenuListView ChatListView;//聊天的界面
    private List<InviteMessage> msgs;//邀请信息
    private NewFriendsMsgAdapter newsFriendsMsgAdapter;//聊天的适配器
    private List<User> users;//聊天历史记录
    private ChatHistoryAdapter chatHistoryAdapter;//历史记录的适配器
    private android.app.AlertDialog.Builder conflictBuilder;//冲突对话框
    private android.app.AlertDialog.Builder accountRemovedBuilder;//账号移除对话框
    private boolean isConflictDialogShow;//冲突对话框显示
    private boolean isAccountRemovedDialogShow;//账号移除对话框
    private static Activity activity;
    private static Map<String,User> contactList;
    private InviteMessgeDao inviteMessgeDao;
    private static UserDao userDao;
    private MyConnectionListener connectionListener=null;

    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            AppContext.getInstance().Logout();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        setContentView(R.layout.activity_chat);

        fm = getSupportFragmentManager();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        LayoutInflater inflater = (LayoutInflater) this.getLayoutInflater();
        View selectView = inflater.inflate(R.layout.actionbar_chat, null);
        actionBar.setCustomView(selectView, lp);
        actionBar.setDisplayShowHomeEnabled(false);//去掉导航
        actionBar.setDisplayShowTitleEnabled(false);//去掉标题
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);

        ImageView back = (ImageView) selectView.findViewById(R.id.chat_image_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        inviteLayout = (FrameLayout) this.findViewById(R.id.invite_layout);
        chatLayout = (FrameLayout) this.findViewById(R.id.chat_layout);

        final Button inviteButton = (Button) this.findViewById(R.id.button_invite_message);
        final Button chatButton = (Button) this.findViewById(R.id.button_talk_message);
        inviteButton.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
        chatButton.setBackgroundResource(R.drawable.bg_actionbar_right_normal);
        inviteLayout.setVisibility(View.VISIBLE);
        chatLayout.setVisibility(View.GONE);
        state = 0;
        activity = this;

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 0;
                 InvitelistView.scrollBack();
                inviteButton.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
                chatButton.setBackgroundResource(R.drawable.bg_actionbar_right_normal);
                if (inviteLayout.getVisibility() == View.VISIBLE)
                    return;
                chatLayout.setVisibility(View.GONE);
                inviteLayout.setVisibility(View.VISIBLE);
            }
        });
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 1;
                ChatListView.scrollBack();
                inviteButton.setBackgroundResource(R.drawable.bg_actionbar_left_normal);
                chatButton.setBackgroundResource(R.drawable.bg_actionbar_right_clicked);
                if (chatLayout.getVisibility() == View.VISIBLE)
                    return;
                chatLayout.setVisibility(View.VISIBLE);
                inviteLayout.setVisibility(View.GONE);
            }
        });
        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }

        //邀请的Dao和用户的Dao
        inviteMessgeDao = new InviteMessgeDao(activity);
        userDao = new UserDao(activity);
        InvitelistView = (SwipeMenuListView) activity.findViewById(R.id.lv_invite);
        ChatListView = (SwipeMenuListView) activity.findViewById(R.id.lv_chat);

        init();
        EMChat.getInstance().setAppInited();
        newsFriendsMsgAdapter=new NewFriendsMsgAdapter();
        chatHistoryAdapter=new ChatHistoryAdapter();
        try {
            msgs = inviteMessgeDao.getMessagesList();
//            System.out.println("msgs----->" + msgs.get(0).getFrom()+"---"+msgs.get(0).getStatus());
           if(msgs!=null) {
               msgs=removeDuplicat(msgs);
               //设置inviteMessage的内容
               InvitelistView.setAdapter(newsFriendsMsgAdapter);
           }
             //获取好友列表
            contactList = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
            System.out.println("contactList---------->" + contactList);
            if(contactList!=null) {
                users = loadUsersWithRecentChat();
                System.out.println("users----->" + users);
                ChatListView.setAdapter(chatHistoryAdapter);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        initUiAndListener();
        AppManager.getAppManager().addActivity(this);
    }
     //邀请的Adapter
    class NewFriendsMsgAdapter  extends BaseAdapter {

        @Override
        public int getCount() {
            return msgs.size();
        }

        @Override
        public InviteMessage getItem(int position) {
            return msgs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = View.inflate(activity, R.layout.list_cell_chat, null);
                    holder.avator = (ImageView) convertView.findViewById(R.id.img_item_edit);
                    holder.reason = (TextView) convertView.findViewById(R.id.chat_cotent);
                    holder.name = (TextView) convertView.findViewById(R.id.txt_item_edit);
                    holder.status = (TextView) convertView.findViewById(R.id.chat_show);
                    holder.time = (TextView) convertView.findViewById(R.id.chat_date);
                    holder.sex = (ImageView) convertView.findViewById(R.id.chat_gender);
                    holder.chat_distance = (TextView) convertView.findViewById(R.id.chat_distance);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                String str1 = activity.getResources().getString(R.string.Has_agreed_to_your_friend_request);
                String str2 = activity.getResources().getString(R.string.agree);
                String str3 = activity.getResources().getString(R.string.Request_to_add_you_as_a_friend);
                String str4 = activity.getResources().getString(R.string.Apply_to_the_group_of);
                String str5 = activity.getResources().getString(R.string.Has_agreed_to);
                String str6 = activity.getResources().getString(R.string.Has_refused_to);
                final InviteMessage msg = (InviteMessage) getItem(position);
                if (msg != null) {
                    //0为男士，1位女士
                    if (msg.getSex() == 0) {
                        holder.sex.setBackgroundResource(R.mipmap.userinfo_icon_male);
                    } else {
                        holder.sex.setBackgroundResource(R.mipmap.userinfo_icon_female);
                    }
                    holder.reason.setText(msg.getReason());
                    holder.name.setText(msg.getNick());
                    holder.time.setText(DateUtils.getTimestampString(new
                            Date(msg.getTime())));
                    if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEAGREED) {
                        holder.status.setVisibility(View.INVISIBLE);
                        holder.reason.setText(str1);
                        holder.status.setBackgroundResource(R.drawable.btn_invite_agreeed);
                        holder.status.setClickable(false);
                    } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED || msg.getStatus() == InviteMessage.InviteMesageStatus.BEAPPLYED) {
                        holder.status.setVisibility(View.VISIBLE);
                        holder.status.setEnabled(true);
                        holder.status.setBackgroundResource(R.drawable.btn_invite_agree);
                        holder.status.setText(str2);
                        if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED) {
                            if (msg.getReason() == null) {
                                // 如果没写理由
                                holder.reason.setText(str3);
                            }
                        } else { //入群申请
                            if (TextUtils.isEmpty(msg.getReason())) {
                                holder.reason.setText(str4 + msg.getGroupName());
                            }
                        }
                        // 设置点击事件
                        holder.status.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // 同意别人发的好友请求
                                acceptInvitation(holder.status, msg);
                                holder.status.setBackgroundResource(R.drawable.btn_invite_agreeed);
                                holder.status.setText("已同意");
                                holder.status.setClickable(false);
                            }
                        });
                    } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.AGREED) {
                        holder.status.setText(str5);
                        holder.status.setBackgroundResource(R.drawable.btn_invite_agreeed);
                        holder.status.setClickable(false);
                    } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.REFUSED) {
                        holder.status.setText(str6);
                        holder.status.setBackgroundResource(R.drawable.btn_invite_agreeed);
                        holder.status.setClickable(false);
                    }
                    //经纬度的计算
                    Double lng = Double.parseDouble(AppContext.getInstance().getProperty("lng"));
                    Double lat = Double.parseDouble(AppContext.getInstance().getProperty("lat"));
                    Double distance1 = ComputeDistance.GetDistance(lat, lng, msg.getLat(), msg.getLng());
                    holder.chat_distance.setText(distance1.toString() + "km");
                    // 设置用户头像
                    if (msg != null && msg.getAvatar() != null && msg.getAvatar() != "") {
                        Picasso.with(activity).load(msg.getAvatar()).placeholder(R.mipmap.default_avatar).into(holder.avator);
                    } else {
                        Picasso.with(activity).load(R.mipmap.default_avatar).into(holder.avator);
                    }
                    final Context context_user = activity;
                    holder.avator.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context_user, DGUserInfoActivity.class);
                            intent.putExtra("user_id", msg.getUser_id());
                            context_user.startActivity(intent);
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return convertView;
        }

        private void acceptInvitation(final TextView button, final InviteMessage msg) {
            final ProgressDialog pd = new ProgressDialog(activity);
            String str1 = activity.getResources().getString(R.string.Are_agree_with);
            final String str2 = activity.getResources().getString(R.string.Has_agreed_to);
            final String str3 = activity.getResources().getString(R.string.Agree_with_failure);
            pd.setMessage(str1);
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            new Thread(new Runnable() {
                public void run() {
                    // 调用sdk的同意方法
                    try {
                        if(msg.getGroupId() == null) //同意好友请求
                        {
                            EMChatManager.getInstance().acceptInvitation(msg.getFrom());
                            //用户同意加好友后，给双方发送消息（已经是好友了，可以对话了）
                            String easemob_id=AppContext.getInstance().getProperty("dgUser.easemob_id");
                            EMConversation conversation=EMChatManager.getInstance().getConversation(easemob_id);
                            EMMessage message=EMMessage.createSendMessage(EMMessage.Type.TXT);
                            TextMessageBody textBody=new TextMessageBody("已经是好友了，可以对话了！");
                            message.addBody(textBody);
                            message.setReceipt(msg.getFrom());
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
                        ((Activity) activity).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                pd.dismiss();
                                button.setText(str2);
                                button.setBackgroundResource(R.drawable.btn_invite_agreeed);
                                msg.setStatus(InviteMessage.InviteMesageStatus.AGREED);
                                // 更新db
                                ContentValues values = new ContentValues();
                                values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                                inviteMessgeDao.updateMessage(msg.getId(), values);
                                List<InviteMessage> list=inviteMessgeDao.getMessagesList();
                                System.out.println("list---->"+list.get(0).getId()+"^^^^^^^"+list.get(0).getFrom()+"*******"+list.get(0).getStatus());
                                System.out.println("msgs--->adapter"+msgs.get(0).getFrom()+".."+msgs.get(0).getStatus());
                                button.setEnabled(false);

                            }
                        });
                    } catch (final Exception e) {

                        ((Activity) activity).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                pd.dismiss();
                                Toast.makeText(activity, str3 + e.getMessage(), Toast.LENGTH_LONG).show();
                                System.out.println("chat_invite" + e.getMessage());
                            }
                        });

                    }
                }
            }).start();

        }
        private  class ViewHolder {
            ImageView avator;
            TextView name;
            TextView reason;
            //		Button status;
//		LinearLayout groupContainer;
            TextView status;
            TextView time;
            ImageView sex;
            TextView chat_distance;
        }

    }
    //聊天历史记录
    class ChatHistoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public User getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_cell_chat_history, null);
                }
                ViewHolder holder = (ViewHolder) convertView.getTag();
                if (holder == null) {
                    holder = new ViewHolder();
                    holder.name = (TextView) convertView.findViewById(R.id.txt_item_edit);
                    holder.unreadLabel = (TextView) convertView.findViewById(R.id.chat_show);
                    holder.message = (TextView) convertView.findViewById(R.id.chat_cotent);
                    holder.time = (TextView) convertView.findViewById(R.id.chat_date);
                    holder.avatar = (ImageView) convertView.findViewById(R.id.img_item_edit);
                    holder.sex=(ImageView)convertView.findViewById(R.id.chat_gender);
                    holder.chat_distance=(TextView)convertView.findViewById(R.id.chat_distance);
                    convertView.setTag(holder);
                }

                final User user = (User) getItem(position);

                //0为男士，1位女士
                if (user.getSex() == 0) {
                    holder.sex.setBackgroundResource(R.mipmap.userinfo_icon_male);
                } else {
                    holder.sex.setBackgroundResource(R.mipmap.userinfo_icon_female);
                }
                if (user != null && user.getAvatar() != null&&user.getAvatar()!="") {
                    Picasso.with(activity).load(user.getAvatar()).into(holder.avatar);
                } else {
                    Picasso.with(activity).load(R.mipmap.default_avatar).into(holder.avatar);
                }
                final Context context_user=activity;
                holder.avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context_user, DGUserInfoActivity.class);
                        intent.putExtra("user_id",user.getUser_id());
                        context_user.startActivity(intent);
                    }
                });
                Double lng=Double.parseDouble(AppContext.getInstance().getProperty("lng"));
                Double lat=Double.parseDouble(AppContext.getInstance().getProperty("lat"));
                Double distance1= ComputeDistance.GetDistance(lat, lng, user.getLat(), user.getLng());
                holder.chat_distance.setText(distance1.toString() + "km");
                String username = user.getUsername();
                // 获取与此用户/群组的会话
                EMConversation conversation = EMChatManager.getInstance().getConversation(username);
                holder.name.setText(user.getNick() != null ? user.getNick() : username);
                if (conversation.getUnreadMsgCount() > 0) {
                    // 显示与此用户的消息未读数
                    holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
                    holder.unreadLabel.setVisibility(View.VISIBLE);
                } else {
                    holder.unreadLabel.setVisibility(View.INVISIBLE);
                }

                if (conversation.getMsgCount() != 0) {
                    // 把最后一条消息的内容作为item的message内容
                    EMMessage lastMessage = conversation.getLastMessage();
                    holder.message.setText(SmileUtils.getSmiledText(activity, CommonUtils.getMessageDigest(lastMessage, (activity))),
                            TextView.BufferType.SPANNABLE);

                    holder.time.setText(com.easemob.util.DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
                }
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return convertView;
        }

        private  class ViewHolder {
            /** 和谁的聊天记录 */
            TextView name;
            /** 消息未读数 */
            TextView unreadLabel;
            /** 最后一条消息的内容 */
            TextView message;
            /** 最后一条消息的时间 */
            TextView time;
            /** 用户头像 */
            ImageView avatar;
            /** 用户年龄 */
            ImageView sex;
            TextView chat_distance;

        }
    }
    //处理信息
    Handler emcat =new Handler()
    {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 1:
                    String emlist = "";
                    contactList = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
                    Set set = contactList.keySet();
                    for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                        String key = (String) iter.next();
                        emlist += key + ",";
                    }
                    if(emlist=="")
                        break;
                    emlist = emlist.substring(0, emlist.length() - 1);
                    final List<User> findUsers = new ArrayList<>(contactList.size());
                    DGApi.getUsersByEmchat("android", emlist, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            InputStream inputStream = new ByteArrayInputStream(responseBody);
                            Em_User_List em_user_list = XmlUtils.toBean(Em_User_List.class, inputStream);
                            List<Em_Users> emUsersList = em_user_list.getList();
                            for (Em_Users emuser : emUsersList) {
                                User user = new User();
                                user.setUsername(emuser.getEasemob_id());
                                user.setNick(emuser.getNickname());
                                user.setLng(emuser.getPos_lng());
                                user.setSex(emuser.getUser_sex());
                                user.setAvatar(APIconfig.IMG_BASEURL + emuser.getUser_logo());
                                user.setLat(emuser.getPos_lat());
                                user.setUser_id(emuser.getUser_id());
                                contactList.remove(user.getUsername());
                                contactList.put(user.getUsername(), user);
                                findUsers.add(user);

                            }
                            ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(contactList);
                            contactList = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
                            System.out.println("contaclist111:" + contactList);
                            if(users!=null)
                            users.removeAll(users);
                            if(loadUsersWithRecentChat()!=null)
                            users.addAll(loadUsersWithRecentChat());
                            if(ChatListView.getAdapter()==null)
                                ChatListView.setAdapter(chatHistoryAdapter);
                            chatHistoryAdapter.notifyDataSetChanged();
                            userDao.saveContactList(users);
                            ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().notifyContactInfosSyncListener(true);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                    break;
                case 2:
                    String inviteuser=msg.getData().getString("inviteuser");
                    final String reason=msg.getData().getString("reason");
                    DGApi.getUsersByEmchat("android",inviteuser , new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            InputStream inputStream = new ByteArrayInputStream(responseBody);
                            Em_User_List em_user_list = XmlUtils.toBean(Em_User_List.class, inputStream);
                            List<Em_Users> emUsersList = em_user_list.getList();
                            InviteMessgeDao inviteMessgeDao=new InviteMessgeDao(activity);
                            for (Em_Users emuser : emUsersList) {
                                InviteMessage msg = new InviteMessage();
                                msg.setFrom(emuser.getEasemob_id());
                                msg.setTime(System.currentTimeMillis());
                                msg.setReason(reason);
                                msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
                                msg.setNick(emuser.getNickname());
                                msg.setLng(emuser.getPos_lng());
                                msg.setSex(emuser.getUser_sex());
                                msg.setAvatar(APIconfig.IMG_BASEURL + emuser.getUser_logo());
                                msg.setLat(emuser.getPos_lat());
                                msg.setUser_id(emuser.getUser_id());
                                List<InviteMessage> msgs11 = inviteMessgeDao.getMessagesList();
                                for (InviteMessage inviteMessage : msgs11) {
                                    if ( inviteMessage.getFrom().equals(emuser.getEasemob_id())) {
                                               inviteMessgeDao.deleteMessage(emuser.getEasemob_id());
                                    }
                                }
                                inviteMessgeDao.saveMessage(msg);
                            }
                            msgs.removeAll(msgs);
                            msgs.addAll(inviteMessgeDao.getMessagesList());
                            msgs=removeDuplicat(msgs);
                            if(InvitelistView.getAdapter()==null)
                                InvitelistView.setAdapter(newsFriendsMsgAdapter);
                            newsFriendsMsgAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        }
                    });
                    break;
            }
        }
    };


    // 刷新ui
    public void refreshContact() {
        try {
            // 可能会在子线程中调到这方法
           activity.runOnUiThread(new Runnable() {
                public void run() {
                    asyncFetchContactsFromServer();
                    refreshUI();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    public void initUiAndListener() {
        // step 1. create a MenuCreator
        SwipeMenuCreator Historycreator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("删除信息");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set item title
                deleteItem.setTitle("删除好友");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        ChatListView.setMenuCreator(Historycreator);

        // step 1. create a MenuCreator
        SwipeMenuCreator Invitecreator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.mipmap.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        InvitelistView.setMenuCreator(Invitecreator);

        ChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    User emContact = (User) chatHistoryAdapter.getItem(position);
                    if (((EMContact) chatHistoryAdapter.getItem(position)).getUsername().equals(AppContext.getInstance().getUserName()))
                        Toast.makeText(activity, "不能和自己聊天额", Toast.LENGTH_LONG).show();
                    else {
                        // 进入聊天页面,这里吧群的聊天给屏蔽掉了，因为不可能有群聊天
                        Intent intent = new Intent(activity, ChatDetailActivity.class);
//                        if ((EMContact)emContact instanceof EMGroup) {
//                            //it is group chat
//                            intent.putExtra("chatType", ChatDetailActivity.CHATTYPE_GROUP);
//                            intent.putExtra("groupId", ((EMGroup) emContact).getGroupId());
//                        } else {
                            //it is single chat
                            intent.putExtra("userId", emContact.getUsername());
                            intent.putExtra("user_to",emContact.getUser_id());
//                        }
                        startActivity(intent);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // step 2. listener item click event
        ChatListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                User user = users.get(position);
                switch (index) {
                    case 0:
                        // open
                        users.remove(position);
                        chatHistoryAdapter.notifyDataSetChanged();
                        deletMessage(user);
                        break;
                    case 1:
                        // delete
              			users.remove(position);
                        chatHistoryAdapter.notifyDataSetChanged();
                        deleteFriend(user);
                        break;
                }
            }
        });

        InvitelistView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                InviteMessage inviteMessage = msgs.get(position);
                switch (index) {
                    case 0:
                        // open
                        msgs.remove(position);
                        newsFriendsMsgAdapter.notifyDataSetChanged();
                        deleteInviteMessage(inviteMessage);
                        break;
                }
            }
        });


    }
    //删除好友消息
    private void deleteInviteMessage(InviteMessage inviteMessage)
    {
        try {
            final InviteMessage tobeDeleteUser = inviteMessage;
//            if (tobeDeleteUser.getStatus()==InviteMessage.InviteMesageStatus.BEINVITEED)
//            {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            EMChatManager.getInstance().refuseInvitation(tobeDeleteUser.getFrom());
//                        } catch (EaseMobException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
            inviteMessgeDao.deleteMessage(tobeDeleteUser.getFrom());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    //删除消息
  private void  deletMessage(User user)
  {
          EMContact tobeDeleteUser = (EMContact) user;
          boolean isGroup = false;
          if (tobeDeleteUser instanceof EMGroup)
              isGroup = true;
          // 删除此会话
          EMChatManager.getInstance().deleteConversation(tobeDeleteUser.getUsername(), isGroup);
          InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(activity);
          inviteMessgeDao.deleteMessage(tobeDeleteUser.getUsername());

  }
    //删除好友
    private void deleteFriend(final User user)
    {
        String st1 = getResources().getString(R.string.deleting);
        final String st2 = getResources().getString(R.string.Delete_failed);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMContact tobeDeleteUser = (EMContact) user;
                    EMContactManager.getInstance().deleteContact(tobeDeleteUser.getUsername());
                    // 删除db和内存中此用户的数据
//                    UserDao dao = new UserDao(activity);
//                    dao.deleteContact(tobeDeleteUser.getUsername());
//                    ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().remove(tobeDeleteUser.getUsername());
//                    contactList = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
                    InviteMessgeDao invitedao = new InviteMessgeDao(activity);
                    invitedao.deleteMessage(tobeDeleteUser.getUsername());
                    pd.dismiss();
                } catch (final Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

   void asyncFetchContactsFromServer() {
        HXSDKHelper.getInstance().asyncFetchContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> usernames) {
                try {
                    Context context = HXSDKHelper.getInstance().getAppContext();
                    System.out.println("21222222222222222222" + usernames.toString());
                    EMLog.d("roster", "contacts size: " + usernames.size());
                    final Map<String, User> userlist = new HashMap<String, User>();
                    for (String username : usernames) {
                        User user = new User();
                        user.setUsername(username);
                        setUserHearder(username, user);
                        userlist.put(username, user);
                    }
//

                    // 存入内存
                    ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);

                    if (HXSDKHelper.getInstance().isGroupsSyncedWithServer()) {
                        HXSDKHelper.getInstance().notifyForRecevingEvents();
                    }

                    Message message=new Message();
                    message.what=1;
                    emcat.sendMessage(message);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onError(int error, String errorMsg) {
                HXSDKHelper.getInstance().notifyContactsSyncListener(false);
            }

        });
    }

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     *
     * @param username
     * @param user
     */
    private static void setUserHearder(String username, User user) {
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                    .toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }

        if (connectionListener != null) {
            EMChatManager.getInstance().removeConnectionListener(connectionListener);
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        refreshUI();
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (!isConflict && !isCurrentAccountRemoved) {

            EMChatManager.getInstance().activityResumed();
        }
        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(this,
                new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged, EMNotifierEvent.Event.EventNewCMDMessage});

    }

    /**
     * 连接监听listener
     */
    public class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {

            boolean contactSynced = HXSDKHelper.getInstance().isContactsSyncedWithServer();

            // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
            if (contactSynced) {

                new Thread() {
                    @Override
                    public void run() {
                        HXSDKHelper.getInstance().notifyForRecevingEvents();
                    }
                }.start();
            }

            if(!contactSynced&& ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList()==null) {
                    asyncFetchContactsFromServer();
            }
        }

        @Override
        public void onDisconnected(final int error) {
            final String st1 = getResources().getString(R.string.can_not_connect_chat_server_connection);
            final String st2 = getResources().getString(R.string.the_current_network);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        showAccountRemovedDialog();
                    } else if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        showConflictDialog();
                    }

                }

            });
        }
    }

    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(null);
    }

    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
    }

    /**
     * set head
     *
     * @param username
     * @return
     */
    User setUserHead(String username) {
        User user = new User();
        user.setUsername(username);
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                    .toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
        return user;
    }

    private void init() {
        // setContactListener监听联系人的变化等
        EMContactManager.getInstance().setContactListener(new MyContactListener());
        // 注册一个监听连接状态的listener
        connectionListener = new MyConnectionListener();
        EMChatManager.getInstance().addConnectionListener(connectionListener);

    }


    /***
     * 好友变化listener
     *
     */
    public class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> usernameList) {
            Map<String, User> localUsers = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList();
            Map<String, User> toAddUsers = new HashMap<String, User>();
            for (String username : usernameList) {
                User user = setUserHead(username);
                // 添加好友时可能会回调added方法两次
                if (!localUsers.containsKey(username)) {
                    userDao.saveContact(user);
                }
                toAddUsers.put(username, user);
            }
            localUsers.putAll(toAddUsers);
            Message message=new Message();
            message.what=1;
            emcat.sendMessage(message);

        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            // 被删除
            Map<String, User> localUsers = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList();
            for (String username : usernameList) {
                localUsers.remove(username);
                userDao.deleteContact(username);
                inviteMessgeDao.deleteMessage(username);
            }
            contactList=localUsers;

            runOnUiThread(new Runnable() {
                public void run() {
//                     如果正在与此用户的聊天页面
                    String st10 = getResources().getString(R.string.have_you_removed);
                    if (ChatDetailActivity.activityInstance != null
                            && usernameList.contains(ChatDetailActivity.activityInstance.getToChatUsername())) {
                        Toast.makeText(activity, ChatDetailActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatDetailActivity.activityInstance.finish();
                    }
                   refresh();
                }
            });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<InviteMessage>   msgs2 = inviteMessgeDao.getMessagesList();
                    //设置adapter
                    msgs.removeAll(msgs);
                    msgs.addAll(msgs2);
                    newsFriendsMsgAdapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public void onContactInvited(final String username, String reason) {

            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
            System.out.println("resaon------------------->"+reason);
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if ( inviteMessage.getFrom().equals(username)) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
            Message message=new Message();
            Bundle bundle=new Bundle();
            bundle.putString("inviteuser",username);
            bundle.putString("reason",reason);
            message.what=2;
            message.setData(bundle);
            emcat.sendMessage(message);

        }

        @Override
        public void onContactAgreed(String username) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            Toast.makeText(activity,"Toast",Toast.LENGTH_LONG).show();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }

        }

        @Override
        public void onContactRefused(String username) {

            // 参考同意，被邀请实现此功能,demo未实现
            Log.d(username, username + "拒绝了你的好友请求");
        }

    }
    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHXSDKHelper.getInstance().logout(true, null);
        String st5 = "移除";
        if (!ChatActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(ChatActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage("显示帐号已经被移除");
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        AppContext.getInstance().cleanLoginInfo();
                        deleteUserState();
                        AppManager.getAppManager().finishActivity(MainActivity.class);
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);

                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e("remove", "---------color userRemovedBuilder error" + e.getMessage());
            }
        }
    }

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHXSDKHelper.getInstance().logout(true, null);

        String st = "conflict";
        if (!ChatActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(ChatActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage("显示帐号在其他设备登陆");
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        AppContext.getInstance().cleanLoginInfo();
                        deleteUserState();
                       AppManager.getAppManager().finishActivity(MainActivity.class);
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);

                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e("conflict", "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }
    /**
     * 监听事件
     */
    @Override
    public void onEvent(EMNotifierEvent event) {

        switch (event.getEvent()) {
            case EventNewMessage: // 普通消息
            {
                EMMessage message = (EMMessage) event.getData();
                // 提示新消息
                HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                refreshUI();
                break;
            }
            case EventNewCMDMessage:{

                break;
            }

            case EventOfflineMessage: {
                EMMessage message = (EMMessage) event.getData();
                refreshUI();
                break;
            }
            case EventConversationListChanged: {
                EMMessage message = (EMMessage) event.getData();
                refreshUI();
                break;
            }

            default:
                break;
        }
    }

    private void refreshUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                refresh();

            }
        });
    }
    /**
     * 刷新页面
     */
    public void refresh() {
        users.removeAll(users);
        users.addAll(loadUsersWithRecentChat());
        chatHistoryAdapter.notifyDataSetChanged();


    }
    @Override
    protected void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);


        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }


    /**
     * 获取有聊天记录的users和groups
     *
     * @param
     * @return
     */
   synchronized private static List<User> loadUsersWithRecentChat() {
        List<User> resultList=null;
        contactList=((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
        System.out.print("contacList:::::" + contactList);
        try{
         resultList = new ArrayList<User>();
        //获取有聊天记录的users，不包括陌生人
        for (User user : contactList.values()) {
            EMConversation conversation = EMChatManager.getInstance().getConversation(user.getUsername());
            if (conversation.getMsgCount() > 0) {
                resultList.add(user);
            }
//            resultList.add(user);
        }

        // 排序
        sortUserByLastChatTime(resultList);}
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return resultList;
    }

    /**
     * 根据最后一条消息的时间排序
     *
     * @param
     */
    private static  void sortUserByLastChatTime(List<User> contactList) {
        Collections.sort(contactList, new Comparator<EMContact>() {
            @Override
            public int compare(final EMContact user1, final EMContact user2) {
                EMConversation conversation1 = EMChatManager.getInstance().getConversation(user1.getUsername());
                EMConversation conversation2 = EMChatManager.getInstance().getConversation(user2.getUsername());

                EMMessage user2LastMessage = conversation2.getLastMessage();
                EMMessage user1LastMessage = conversation1.getLastMessage();
                 boolean isnull=user2LastMessage!=null&&user1LastMessage!=null;

                if (user2LastMessage.getMsgTime() == user1LastMessage.getMsgTime()) {
                    return 0;
                } else if (user2LastMessage.getMsgTime() > user1LastMessage.getMsgTime()) {
                    return 1;
                } else {
                    return -1;
                }

            }

        });
    }

    private void deleteUserState() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("gouda", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.remove("phonenum");
        editor.remove("password");
        editor.commit();//提交修改
    }
    //List链表去掉重复的数据
    public static List removeDuplicat(List list)
    {
        HashSet h=new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }
}
