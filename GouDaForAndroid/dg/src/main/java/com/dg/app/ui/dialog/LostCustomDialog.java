package com.dg.app.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.dg.app.R;
import com.dg.app.widget.Wheel.OnWheelChangedListener;
import com.dg.app.widget.Wheel.WheelView;
import com.dg.app.widget.Wheel.adapters.ArrayWheelAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by czh on 2015/9/15.
 */
public class LostCustomDialog extends  Dialog implements
        OnWheelChangedListener,View.OnClickListener{
    private WheelView DayWheelView;

    private Button cancelAge;
    private Button enterEnter;

    public  OnWhellSetDayData mCallBack;


    protected String[] days;


    protected String mCurrentLostTime;

   public interface OnWhellSetDayData
    {
        void setWhellData(String w1);

    }
   Context context;
    public LostCustomDialog(Context context, OnWhellSetDayData mCallBack) {
        super(context);
        this.context=context;
        this.mCallBack=mCallBack;
        setCustomDialog();
    }




    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.customdialog_lost,null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(mView);
        setUpViews();
        setUpListener();
        setUpData();

    }

    private void setUpViews() {
        DayWheelView= (WheelView) findViewById(R.id.lost_time);

        cancelAge=(Button)this.findViewById(R.id.lost_cancel);
        enterEnter=(Button)this.findViewById(R.id.lost_enter);

        enterEnter.setOnClickListener(this);
        cancelAge.setOnClickListener(this);


    }

    private void setUpListener() {

        DayWheelView.addChangingListener(this);





    }


    private void setUpData() {

        initDaysDatas();
        try {
            DayWheelView.setViewAdapter(new ArrayWheelAdapter<String>(context, days));
            DayWheelView.setVisibleItems(7);



        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == DayWheelView) {
            mCurrentLostTime = days[newValue];

        }



    }
    @Override
    public void setContentView(int layoutResID) {


    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {

    }

    @Override
    public void setContentView(View view) {

    }
    protected void initDaysDatas()
    {


        try {
            days=new String[120];
            for(int i=0;i<120;i++)
            {
                days[i]=i+1+"天";
            }
            mCurrentLostTime=days[0];

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.lost_cancel)
        {
            dismiss();
        }
        if(v.getId()==R.id.lost_enter)
        {
            mCallBack.setWhellData(mCurrentLostTime);
            dismiss();

        }
    }
}
