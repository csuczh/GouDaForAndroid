package com.dg.app.widget;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.dg.app.R;
import com.dg.app.bean.CityModel;
import com.dg.app.bean.DistrictModel;
import com.dg.app.bean.ProvinceModel;
import com.dg.app.service.XmlParserHandler;
import com.dg.app.widget.Wheel.OnWheelChangedListener;
import com.dg.app.widget.Wheel.WheelView;
import com.dg.app.widget.Wheel.adapters.ArrayWheelAdapter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by czh on 2015/9/15.
 */
public class AgeCustomDialog extends  Dialog implements
        OnWheelChangedListener,View.OnClickListener{
    private WheelView mViewMinAges;
    private WheelView mViewMaxAges;

    private Button cancelAge;
    private Button enterEnter;

    public  OnWhellSetData mCallBack;


    protected String[] MinAges;
    protected Map<String, String[]> MinToMaxMap = new HashMap<String, String[]>();


    protected String mCurrentMinAge;
    protected String mCurrentMaxAge;

   public interface OnWhellSetData
    {
        void setWhellData(String w1,String w2);

    }
   Context context;
    public AgeCustomDialog(Context context,OnWhellSetData mCallBack) {
        super(context);
        this.context=context;
        this.mCallBack=mCallBack;
        setCustomDialog();

    }




    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.customdialog_age,null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(mView);
        setUpViews();
        setUpListener();
        setUpData();

    }

    private void setUpViews() {
        mViewMinAges = (WheelView) findViewById(R.id.id_min_age);
        mViewMaxAges = (WheelView) findViewById(R.id.id_max_age);

        cancelAge=(Button)this.findViewById(R.id.age_cancel);
        enterEnter=(Button)this.findViewById(R.id.age_enter);

        enterEnter.setOnClickListener(this);
        cancelAge.setOnClickListener(this);
    }

    private void setUpListener() {

        mViewMinAges.addChangingListener(this);

        mViewMaxAges.addChangingListener(this);
    }


    private void setUpData() {

        initAgesDatas();
        try {
            mViewMinAges.setViewAdapter(new ArrayWheelAdapter<String>(context, MinAges));
            mViewMinAges.setVisibleItems(7);
            mViewMaxAges.setVisibleItems(7);

            updateMaxAge();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void updateMaxAge() {
        int pCurrent = mViewMinAges.getCurrentItem();
        mCurrentMinAge = MinAges[pCurrent];

        String[] maxAges = MinToMaxMap.get(mCurrentMinAge);
        mCurrentMaxAge=maxAges[0];
        if (maxAges == null) {
            maxAges = new String[] {"0岁" };
        }
        mViewMaxAges.setViewAdapter(new ArrayWheelAdapter<String>(context, maxAges));
        mViewMaxAges.setCurrentItem(0);

    }
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewMinAges) {
            updateMaxAge();

        } else if (wheel == mViewMaxAges) {
            mCurrentMaxAge = MinToMaxMap.get(mCurrentMinAge)[newValue];

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
            MinAges=new String[120];
            Integer [] minages=new Integer[120];
            for(int i=0;i<120;i++)
            {
                MinAges[i]=i+1+"岁";
                minages[i]=i+1;
            }
            for(int i=0;i<120;i++)
            {
                String temp[]=new String[120-minages[i]+1];

                for(int j=0;j<temp.length;j++)
                {
                    temp[j]=minages[i]+j+1+"岁";
                }
                MinToMaxMap.put(MinAges[i],temp);

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

            mCallBack.setWhellData(mCurrentMinAge,mCurrentMaxAge);
            dismiss();

        }
    }
}
