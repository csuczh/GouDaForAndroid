package com.dg.app.widget;


import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
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
public class LocationCustomDialog extends  Dialog implements
        OnWheelChangedListener,View.OnClickListener{
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Button cancelButton;
    private Button enterButton;



    protected String[] mProvinceDatas;
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

//    protected Map<String, String> mAreaCodeDatasMap = new HashMap<String, String>();

    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName ="";
    protected String mCurrentZipCode ="";

   Context context;

    OnWheelSetArea areas;

    public  interface  OnWheelSetArea{
        public void onSetArea(String p, String c, String d, String areaCode);
    }
    public LocationCustomDialog(Context context, OnWheelSetArea areas) {
        super(context);
        this.context=context;
        this.areas=areas;
        setCustomDialog();
    }


    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.customdialog_location,null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(mView);
        setUpViews();
        setUpListener();
        setUpData();

    }

    private void setUpViews() {
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);

        cancelButton=(Button)this.findViewById(R.id.location_cancel);
        enterButton=(Button)this.findViewById(R.id.location_enter);

        try {
            cancelButton.setOnClickListener(this);
            enterButton.setOnClickListener(this);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    private void setUpListener() {
        // ���change�¼�
        mViewProvince.addChangingListener(this);
        // ���change�¼�
        mViewCity.addChangingListener(this);
        // ���change�¼�
        mViewDistrict.addChangingListener(this);

    }


    private void setUpData() {

        initProvinceDatas();
        try {
            mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
            mViewProvince.setVisibleItems(7);
            mViewCity.setVisibleItems(7);
            mViewDistrict.setVisibleItems(7);
            updateCities();
            updateAreas();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
        mCurrentDistrictName=areas[0];
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
        mViewDistrict.setCurrentItem(0);
    }

    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        mCurrentCityName=cities[0];
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    protected void initProvinceDatas()
    {

        List<ProvinceModel> provinceList = null;

            AssetManager asset = context.getResources().getAssets();

        try {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            provinceList = handler.getDataList();
            mCurrentProviceName="";
            if (provinceList!= null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            mProvinceDatas = new String[provinceList.size()];
            for (int i=0; i< provinceList.size(); i++) {
                mProvinceDatas[i] = provinceList.get(i).getName();

                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j=0; j< cityList.size(); j++) {

                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k=0; k<districtList.size(); k++) {

                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());

                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }

                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }

                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void onClick(View v) {

            if(v.getId()==R.id.location_cancel)
            {
                dismiss();
            }
            if(v.getId()==R.id.location_enter)
            {
                areas.onSetArea(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName,mCurrentZipCode);

                dismiss();
            }


    }
}
