package com.dg.app.widget;


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

public class DogAgeWheelDialog extends  Dialog implements
        OnWheelChangedListener,View.OnClickListener{
    private WheelView wv_user_age;

    private Button cancelAge;
    private Button enterEnter;

    public  OnWhellSetData mCallBack;

    protected Integer[] MinAges;

    protected Integer mCurrentAge;

   public interface OnWhellSetData
    {
        void setWhellData(Integer w1);

    }
   Context context;
    public DogAgeWheelDialog(Context context, OnWhellSetData mCallBack) {
        super(context);
        this.context=context;
        this.mCallBack=mCallBack;
        setCustomDialog();

    }




    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.customdialog_user_age,null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(mView);
        setUpViews();
        setUpListener();
        setUpData();

    }

    private void setUpViews() {
        wv_user_age = (WheelView) findViewById(R.id.wv_user_age);

        cancelAge=(Button)this.findViewById(R.id.age_cancel);
        enterEnter=(Button)this.findViewById(R.id.age_enter);

        enterEnter.setOnClickListener(this);
        cancelAge.setOnClickListener(this);
    }

    private void setUpListener() {

        wv_user_age.addChangingListener(this);

    }


    private void setUpData() {

        initAgesDatas();
        try {
            wv_user_age.setViewAdapter(new ArrayWheelAdapter<Integer>(context, MinAges));
            wv_user_age.setVisibleItems(7);

            updateMaxAge();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void updateMaxAge() {
        int pCurrent = wv_user_age.getCurrentItem();
        mCurrentAge = MinAges[pCurrent];
    }
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == wv_user_age) {
            updateMaxAge();

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


    protected void initAgesDatas()
    {

        try {
            MinAges=new Integer[20];
            for(int i=0;i<20;i++)
            {
                MinAges[i]=i+1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.age_cancel)
        {
            dismiss();
        }
        if(v.getId()==R.id.age_enter)
        {

            mCallBack.setWhellData(mCurrentAge);
            dismiss();
        }
    }
}
