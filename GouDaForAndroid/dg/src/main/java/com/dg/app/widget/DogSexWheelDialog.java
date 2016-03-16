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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by czh on 2015/9/15.
 */
public class DogSexWheelDialog extends  Dialog implements
        OnWheelChangedListener,View.OnClickListener{
    private WheelView wv_user_sex;

    private Button cancelAge;
    private Button enterEnter;

    public  OnWhellSetData mCallBack;


    //protected Integer[] MinAges;
    protected Map<Integer, Integer[]> MinToMaxMap = new HashMap<Integer, Integer[]>();


    protected String[] mSexs;

    protected String mCurrentSex;

   public interface OnWhellSetData
    {
        void setWhellData(String w1);

    }
   Context context;
    public DogSexWheelDialog(Context context, OnWhellSetData mCallBack) {
        super(context);
        this.context=context;
        this.mCallBack=mCallBack;
        setCustomDialog();
    }




    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.customdialog_user_sex,null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(mView);
        setUpViews();
        setUpListener();
        setUpData();

    }

    //设置顶部按钮布局
    private void setUpViews() {
        wv_user_sex = (WheelView) findViewById(R.id.wv_user_sex);

        cancelAge=(Button)this.findViewById(R.id.age_cancel);
        enterEnter=(Button)this.findViewById(R.id.age_enter);

        enterEnter.setOnClickListener(this);
        cancelAge.setOnClickListener(this);
    }

    private void setUpListener() {

        wv_user_sex.addChangingListener(this);

    }


    private void setUpData() {

        initAgesDatas();
        try {
            wv_user_sex.setViewAdapter(new ArrayWheelAdapter<String>(context, mSexs));
            wv_user_sex.setVisibleItems(7);

            updateMaxAge();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void updateMaxAge() {
        int pCurrent = wv_user_sex.getCurrentItem();
        mCurrentSex = mSexs[pCurrent];
    }
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == wv_user_sex) {
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

            mSexs = new String[2];
            mSexs[0] = "公";
            mSexs[1] = "母";

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

            mCallBack.setWhellData(mCurrentSex);
            dismiss();
        }
    }
}
