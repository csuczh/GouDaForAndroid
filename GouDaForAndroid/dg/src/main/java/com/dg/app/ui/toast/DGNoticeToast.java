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
public class DGNoticeToast{

    private Context context;

    public DGNoticeToast(Context context) {
        this.context = context;
    }

    /**
     * 通用Toast显示
     * @param message
     * @param duration
     * @param icon
     */
    public void show(String message, int duration, int icon) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_notice, null);

        ((TextView) view.findViewById(R.id.id_notice_dialoglabel)).setText(message);

        if (icon != 0) {
            ((ImageView) view.findViewById(R.id.id_notice_dialog_icon))
                    .setImageResource(icon);
        }

        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.show();
    }

    /**
     * 成功
     * @param message
     */
    public void showSuccess(String message) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_notice, null);

        ((TextView) view.findViewById(R.id.id_notice_dialoglabel)).setText(message);

            ((ImageView) view.findViewById(R.id.id_notice_dialog_icon))
                    .setImageResource(R.mipmap.ic_choose);

        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 成功
     * @param message
     */
    public void showSuccess(int message) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_notice, null);

        ((TextView) view.findViewById(R.id.id_notice_dialoglabel)).setText(message);

        ((ImageView) view.findViewById(R.id.id_notice_dialog_icon))
                .setImageResource(R.mipmap.ic_choose);

        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 失败
     * @param message
     */
    public void showFailure(String message) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_notice, null);

        ((TextView) view.findViewById(R.id.id_notice_dialoglabel)).setText(message);

        ((ImageView) view.findViewById(R.id.id_notice_dialog_icon))
                .setImageResource(R.mipmap.ic_close);

        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 失败
     * @param message
     */
    public void showFailure(int message) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_notice, null);

        ((TextView) view.findViewById(R.id.id_notice_dialoglabel)).setText(message);

        ((ImageView) view.findViewById(R.id.id_notice_dialog_icon))
                .setImageResource(R.mipmap.ic_close);

        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }


}
