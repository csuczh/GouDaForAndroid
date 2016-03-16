package com.dg.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.ListBaseAdapter;
import com.dg.app.bean.DateDogInvitation;
import com.dg.app.fragment.InvitationFragment;
import com.dg.app.ui.LoginActivity;
import com.dg.app.widget.DGAlertDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.kymjs.kjframe.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DateDogInvitationsAdapter extends ListBaseAdapter<DateDogInvitation>{

    private Activity context;

    public DateDogInvitationsAdapter(Activity context) {
        this.context = context;
    }

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        try {
            if (convertView == null || convertView.getTag() == null) {
                convertView = getLayoutInflater(parent.getContext()).inflate(
                        R.layout.list_inviteinfo_item, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final DateDogInvitation dateGouInvitation = mDatas.get(position);

            /**
             * 点击单项状态进入邀请详情界面
             */
            vh.ll_card_invitation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            /**
             * 1、设置时间
             */
//            vh.tv_time.setText(StringUtils.friendlyTime(dateGouInvitation.getPublish_time()));
            vh.tv_time.setText(dateGouInvitation.getPublish_time());
            /**
             *2、设置请求消息
             */

            vh.tv_require_info.setText(dateGouInvitation.getInvite_explain()+"\n\n我狗狗的品种是："+dateGouInvitation.getDog_variety()+"。");

            /**
             * 3、设置按钮点击事件
             */
            final Button del_button = vh.btn_del;
            final int pos = position;
            vh.btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog;
                    DGAlertDialog.Builder  dgDialogBuilder = new DGAlertDialog.Builder(context);
                    dgDialogBuilder.setMessage("确定删除吗?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            del_button.setClickable(false);
                            //网络请求删除该请求
                            int user_id = AppContext.getInstance().getLoginUid();
                            if(user_id!=0){
                                DGApi.deleteInvitation(user_id, dateGouInvitation.getDate_id(), InvitationFragment.INVITATION_TYPE[1], new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        mDatas.remove(pos);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        AppContext.showToast("网络请求失败，请重试");
                                        del_button.setClickable(true);
                                    }
                                });

                            }
                        }
                    });
                    dialog = dgDialogBuilder.create();
                    dialog.show();
                }
            });



//            vh.btn_del.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    del_button.setClickable(false);
//                    //TODO
//                    //网络请求删除该请求
//                    int user_id = AppContext.getInstance().getLoginUid();
//                    if(user_id!=0){
//                        DGApi.deleteInvitation(user_id, dateGouInvitation.getDate_id(), InvitationFragment.INVITATION_TYPE[1], new AsyncHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                mDatas.remove(pos);
//                                notifyDataSetChanged();
//                                Toast.makeText(context,"删除遛狗请求信息详情界面",Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                            }
//                        });
//
//                    }else{
//                        Intent intent = new Intent(context, LoginActivity.class);
//                        context.startActivity(intent);
//                    }
//
//
//                }
//            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {

        @InjectView(R.id.ll_card_invitation)
        LinearLayout ll_card_invitation;

        @InjectView(R.id.tv_time)
        TextView tv_time;

        @InjectView(R.id.tv_require_info)
        TextView tv_require_info;

        @InjectView(R.id.btn_del)
        Button btn_del;

        public ViewHolder(View view) {
            try {
                ButterKnife.inject(this, view);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
