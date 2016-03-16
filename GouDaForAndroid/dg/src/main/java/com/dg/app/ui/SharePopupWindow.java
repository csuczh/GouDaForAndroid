package com.dg.app.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.dg.app.R;

public class SharePopupWindow extends PopupWindow {
	private ImageView wx_quan, wx_friend, qq_quan,qq_friend,xl_weibo,share_copy;
    private Button btn_cancel;
    private View mMenuView;

    public SharePopupWindow(Activity context, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_popwindow_share, null);

        wx_quan = (ImageView) mMenuView.findViewById(R.id.wx_quan);
        wx_friend = (ImageView) mMenuView.findViewById(R.id.wx_friend);
        qq_quan = (ImageView) mMenuView.findViewById(R.id.qq_quan);
        qq_friend = (ImageView) mMenuView.findViewById(R.id.qq_friend);
        xl_weibo = (ImageView) mMenuView.findViewById(R.id.xl_weibo);
        share_copy = (ImageView) mMenuView.findViewById(R.id.share_copy);


        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        //取消按钮
        btn_cancel.setOnClickListener(new OnClickListener() {
 
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        wx_quan.setOnClickListener(itemsOnClick);
        wx_friend.setOnClickListener(itemsOnClick);
        qq_quan.setOnClickListener(itemsOnClick);
        qq_friend.setOnClickListener(itemsOnClick);
        xl_weibo.setOnClickListener(itemsOnClick);
        share_copy.setOnClickListener(itemsOnClick);

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
//        //实例化一个ColorDrawable颜色为半透明
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
