package com.dg.app.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.dg.app.R;

public class TweetPopupWindow extends PopupWindow {
	private Button btn_follow_ta, btn_begin_chat, btn_invite_liugou,btn_invite_xiangqin,btn_share2other,btn_jubao,btn_cancel;
    private View mMenuView;

    public TweetPopupWindow(Activity context, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_popwindow_tweet, null);

        btn_follow_ta = (Button) mMenuView.findViewById(R.id.btn_follow_ta);
        btn_begin_chat = (Button) mMenuView.findViewById(R.id.btn_begin_chat);
        btn_invite_liugou = (Button) mMenuView.findViewById(R.id.btn_invite_liugou);
        btn_invite_xiangqin = (Button) mMenuView.findViewById(R.id.btn_invite_xiangqin);
        btn_share2other = (Button) mMenuView.findViewById(R.id.btn_share2other);
        btn_jubao = (Button) mMenuView.findViewById(R.id.btn_jubao);

        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        //取消按钮
        btn_cancel.setOnClickListener(new OnClickListener() {
 
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        btn_follow_ta.setOnClickListener(itemsOnClick);
        btn_begin_chat.setOnClickListener(itemsOnClick);
        btn_invite_liugou.setOnClickListener(itemsOnClick);
        btn_invite_xiangqin.setOnClickListener(itemsOnClick);
        btn_share2other.setOnClickListener(itemsOnClick);
        btn_jubao.setOnClickListener(itemsOnClick);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {
             
            public boolean onTouch(View v, MotionEvent event) {
                 
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }               
                return true;
            }
        });
 
    }
 
}
