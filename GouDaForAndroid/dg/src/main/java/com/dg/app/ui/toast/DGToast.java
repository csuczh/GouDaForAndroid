package com.dg.app.ui.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.R;

/**
 * Created by xianxiao on 2015/10/26.
 */
public class DGToast extends Toast{

    public DGToast(Context context) {
        super(context);
    }

    public static Toast makeText(Context context,int resouceID,CharSequence text,int duration){

        Toast toast = new Toast(context);

        //获取LayoutInflater对象
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //由layout文件创建一个View对象
        View layout = inflater.inflate(R.layout.toast_custom_notice, null);

        ImageView imageView = (ImageView) layout.findViewById(R.id.iv_toast_img);
        TextView textView = (TextView) layout.findViewById(R.id.tv_toast_content);

        imageView.setImageResource(resouceID);
        textView.setText(text);

        toast.setView(layout);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        return toast;
    }

}
