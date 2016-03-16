package com.dg.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dg.app.R;

/**
 * Created by xianxiao on 2015/11/16.
 */
public class DialogManager {

    private Dialog mDialog;

    private ImageView mIcon;

    private TextView mLable;

    private Context mContext;

    public DialogManager(Context mContext) {
        super();
        this.mContext = mContext;
    }

    /**
     *显示Dialog
     */
    public void showNoticeDialog(){
        mDialog = new Dialog(mContext, R.style.Theme_NoticeDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_notice, null);
        mDialog.setContentView(view);

        mIcon = (ImageView) mDialog.findViewById(R.id.id_notice_dialog_icon);

        mLable = (TextView) mDialog.findViewById(R.id.id_notice_dialoglabel);

        mDialog.show();
    }

    /**
     * 成功提示
     * @param str
     */
    public void success(String str){
        if(mDialog!=null&&mDialog.isShowing()){
            mIcon.setImageResource(R.mipmap.ic_choose);
            mLable.setText(str);
        }
    }

    /**
     * 成功提示
     * @param str
     */
    public void success(int str){
        if(mDialog!=null&&mDialog.isShowing()){
            mIcon.setImageResource(R.mipmap.ic_choose);
            mLable.setText(str);
        }
    }

    /**
     * 失败提示
     * @param str
     */
    public void failure(String str){
        if(mDialog!=null&&mDialog.isShowing()){
            mIcon.setImageResource(R.mipmap.ic_close);
            mLable.setText(str);
        }
    }

    /**
     * 失败提示
     * @param str
     */
    public void failure(int str){
        if(mDialog!=null&&mDialog.isShowing()){
            mIcon.setImageResource(R.mipmap.ic_close);
            mLable.setText(str);
        }
    }


    /**
     * 隐藏Dialog
     */
    public void dismissDialog(){
        if(mDialog!=null&&mDialog.isShowing()){
            mDialog.dismiss();
            mDialog = null;
        }
    }

}
