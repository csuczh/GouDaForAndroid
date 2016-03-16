package com.dg.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.widget.AgeCustomDialog;
import com.dg.app.widget.LocationCustomDialog;
import com.dg.app.widget.Wheel.WheelView;
import com.umeng.message.PushAgent;

public class SelectActivity extends AppCompatActivity
implements  ImageView.OnClickListener{
     //主人的性别
    protected int peopleSex[]={0,0,1};
    protected TextView peopleTextView[]=new TextView[3];
    int peopleSexIndex=2;
    //狗的性别
    protected  int dogSex[]={0,0,1};
    protected TextView dogTextView[]=new TextView[3];
    int dogSexIndex=2;
//    //狗的体型
//    protected  int dogType[]={0,0,0,1};
//    protected  TextView dogTypeTextView[]=new TextView[4];
    int dogTypeIndex=2;

    SelectActivity selectActivity;

    private TextView ageMin;
    String  ageMinString;
    private TextView ageMax;
    String ageMaxString;

    private TextView province;
    String provinceString;

    private TextView city;
    String cityString;

    private TextView dist;
    String distString;

    boolean onoff;
    ToggleButton toggleButton;

    LinearLayout Onoffsview;//开关打开和关闭时，显示和隐藏
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

        Onoffsview=(LinearLayout)this.findViewById(R.id.hide_onoff);


       toggleButton=(ToggleButton)this.findViewById(R.id.toogleButton);
       toggleButton.setChecked(false);
        toggleButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == false) {
                    onoff=false;
                    Onoffsview.setVisibility(View.INVISIBLE);
                    setPeopleSexSelected(2);
                    setDogSexSelected(2);
                    ageMin.setText("");
                    ageMax.setText("");

                    province.setText("");
                    city.setText("");
                    dist.setText("");
                } else
                {
                    Onoffsview.setVisibility(View.VISIBLE);
                    onoff=true;
                    setPeopleSexSelected(2);
                    setDogSexSelected(2);
//                    setDogTypeSelected(2);

                    ageMin.setText("");
                    ageMax.setText("");

                    province.setText("");
                    city.setText("");
                    dist.setText("");

                }
            }


        });

        ageMin=(TextView)this.findViewById(R.id.age_min);
        ageMax=(TextView)this.findViewById(R.id.age_max);

        province=(TextView)this.findViewById(R.id.location_province);
        city=(TextView)this.findViewById(R.id.location_city);
        dist=(TextView)this.findViewById(R.id.location_distribute);

        peopleTextView[0]=(TextView)this.findViewById(R.id.text_male);
        peopleTextView[1]=(TextView)this.findViewById(R.id.text_female);
        peopleTextView[2]=(TextView)this.findViewById(R.id.text_what);

        peopleTextView[0].setClickable(true);
        peopleTextView[0].setFocusable(true);
        peopleTextView[0].setOnClickListener(this);
        peopleTextView[1].setOnClickListener(this);
        peopleTextView[2].setOnClickListener(this);;

        dogTextView[0]=(TextView)this.findViewById(R.id.text_dg_male);
        dogTextView[1]=(TextView)this.findViewById(R.id.text_dg_female);
        dogTextView[2]=(TextView)this.findViewById(R.id.text_dg_what);
        dogTextView[0].setOnClickListener(this);
        dogTextView[1].setOnClickListener(this);
        dogTextView[2].setOnClickListener(this);

//        dogTypeTextView[0]=(TextView)this.findViewById(R.id.text_dg_big);
//        dogTypeTextView[1]=(TextView)this.findViewById(R.id.text_dg_middle);
//        dogTypeTextView[2]=(TextView)this.findViewById(R.id.text_dg_small);
//        dogTypeTextView[3]=(TextView)this.findViewById(R.id.text_dg_whatever);
//
//        dogTypeTextView[0].setOnClickListener(this);
//        dogTypeTextView[1].setOnClickListener(this);
//        dogTypeTextView[2].setOnClickListener(this);
//        dogTypeTextView[3].setOnClickListener(this);
        selectActivity=this;
//
//

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        LayoutInflater inflater = (LayoutInflater) this.getLayoutInflater();
        View selectView = inflater.inflate(R.layout.action_select_bar_title, null);
        actionBar.setCustomView(selectView, lp);
        actionBar.setDisplayShowHomeEnabled(false);//去掉导航
        actionBar.setDisplayShowTitleEnabled(false);//去掉标题
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);

        ImageView imageView=(ImageView)selectView.findViewById(R.id.select_image_back);
        imageView.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(selectActivity);
            }
        });

        TextView finishSelect=(TextView)selectView.findViewById(R.id.finish_select);
        finishSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.putExtra("onoff",onoff);
                intent.putExtra("psex",peopleSexIndex);
                intent.putExtra("dsex",dogSexIndex);

                intent.putExtra("province",provinceString);
                intent.putExtra("city",cityString);
                intent.putExtra("distribute",distString);

                intent.putExtra("agemax",ageMaxString);
                intent.putExtra("agemin",ageMinString);

                SelectActivity.this.setResult(RESULT_OK, intent);
                SelectActivity.this.finish();
            }
        });

        ImageView ageForward=(ImageView)this.findViewById(R.id.age_forward);
        ImageView locationForward=(ImageView)this.findViewById(R.id.location_forward);

        ageForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AgeCustomDialog ageCustomDialog=new AgeCustomDialog(selectActivity, new AgeCustomDialog.OnWhellSetData() {
                        @Override
                        public void setWhellData(String min, String max) {
                            ageMin.setText(min+"");
                            ageMinString=min+"";
                            ageMax.setText(max+"");
                            ageMaxString=max+"";
                        }
                    });
                    //设置对话框的位置
                    WindowManager m=getWindowManager();
                    Window dialogWindow=ageCustomDialog.getWindow();
                    WindowManager.LayoutParams lp=dialogWindow.getAttributes();
                    Display d=m.getDefaultDisplay();
                    lp.height=(int)(d.getHeight()*0.6);
                    lp.width=(int)(d.getWidth());
                    dialogWindow.setAttributes(lp);
                    dialogWindow.setGravity(Gravity.BOTTOM);
                    ageCustomDialog.show();


                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
        locationForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LocationCustomDialog locationCustomDialog=new LocationCustomDialog(selectActivity, new LocationCustomDialog.OnWheelSetArea() {
                    @Override
                    public void onSetArea(String p, String c, String d,String code) {

                        province.setText(p);
                        provinceString=p;
                        city.setText(c);
                        cityString=c;
                        dist.setText(d);
                        distString=d;
                    }
                });
                //设置对话框的位置
                WindowManager m=getWindowManager();
                Window dialogWindow=locationCustomDialog.getWindow();
                WindowManager.LayoutParams lp=dialogWindow.getAttributes();
                Display d=m.getDefaultDisplay();
                lp.height=(int)(d.getHeight()*0.6);
                lp.width=(int)(d.getWidth());
                dialogWindow.setAttributes(lp);
                dialogWindow.setGravity(Gravity.BOTTOM);

                locationCustomDialog.show();
            }
        });

        initView();
    }
    //
    public void initView()
    {
        SharedPreferences stateShare=getSharedPreferences("select", Activity.MODE_PRIVATE);

        onoff=stateShare.getBoolean("onoff", false);
        peopleSexIndex=stateShare.getInt("peoplesex", 2);
        dogSexIndex=stateShare.getInt("dogsex", 2);
        dogTypeIndex=stateShare.getInt("dogtype", 2);
        ageMinString=stateShare.getString("agemin", "");
        ageMaxString=stateShare.getString("agemax", "");
        provinceString=stateShare.getString("province", "");
        cityString=stateShare.getString("city", "");
        distString=stateShare.getString("dist", "");

        if(onoff==false)
        {
            toggleButton.setChecked(false);
            Onoffsview.setVisibility(View.INVISIBLE);
            return;
        }
        toggleButton.setChecked(true);
        Onoffsview.setVisibility(View.VISIBLE);
        setPeopleSexSelected(peopleSexIndex);
        setDogSexSelected(dogSexIndex);
//        setDogTypeSelected(dogTypeIndex);

        ageMin.setText(ageMinString);
        ageMax.setText(ageMaxString);

        province.setText(provinceString);
        city.setText(cityString);
        dist.setText(distString);

    }
    @Override
    public void onClick(View v) {
      String selected=((TextView)v).getTag().toString();
        selected=selected.trim();

        switch (selected)
        {
            case "人男" :setPeopleSexSelected(0);break;
            case "人女":setPeopleSexSelected(1);break;
            case  "人不限":setPeopleSexSelected(2);break;
            case "狗公":setDogSexSelected(0);break;
            case "狗母":setDogSexSelected(1);break;
            case "狗不限":setDogSexSelected(2);break;
//            case "大":setDogTypeSelected(0);break;
//            case "中":setDogTypeSelected(1);break;
//            case"小":setDogTypeSelected(2);break;
//            case "体型不限":setDogTypeSelected(3);break;

        }

    }



    //设置指定位置的主人性别选择状态
    public void setPeopleSexSelected(int index)
    {
        for(int i=0;i<peopleSex.length;i++)
        {
            if(i!=index)
            {
                peopleSex[i]=0;
                peopleTextView[i].setBackgroundResource(R.drawable.corner_view_gray);
            }
        }
        peopleSex[index]=1;
        peopleSexIndex=index;
        peopleTextView[index].setBackgroundResource(R.drawable.corner_view_red);
    }
    //设置指定位置的狗性别选择状态
    public void setDogSexSelected(int index)
    {

        for(int i=0;i<dogSex.length;i++)
        {
            if(i!=index)
            {
                dogSex[i]=0;
                dogTextView[i].setBackgroundResource(R.drawable.corner_view_gray);
            }
        }
        dogSex[index]=1;
        dogSexIndex=index;
        dogTextView[index].setBackgroundResource(R.drawable.corner_view_red);
    }
//    //设置指定位置的狗的类型选择状态
//    public void setDogTypeSelected(int index)
//    {
//        for(int i=0;i<dogType.length;i++)
//        {
//            if(i!=index)
//            {
//                dogType[i]=0;
//                dogTypeTextView[i].setBackgroundResource(R.drawable.corner_view_gray);
//            }
//        }
//        dogType[index]=1;
//        dogTypeIndex=index;
//        dogTypeTextView[index].setBackgroundResource(R.drawable.corner_view_red);
//    }
    //保存当前界面的数据
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean("onoff",onoff);
        outState.putIntArray("peoplesex",peopleSex);
        outState.putIntArray("dogsex",dogSex);
//        outState.putIntArray("dogtype",dogType);
        outState.putString("agemin", ageMinString);
        outState.putString("agemax", ageMaxString);
        outState.putString("province", provinceString);
        outState.putString("city", cityString);
        outState.putString("dist", distString);

        super.onSaveInstanceState(outState, outPersistentState);

    }
    //恢复当前界面的数据
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        onoff=savedInstanceState.getBoolean("onoff");
        peopleSex= savedInstanceState.getIntArray("peoplesex");
        dogSex=savedInstanceState.getIntArray("dogsex");
//        dogType=savedInstanceState.getIntArray("dogtype");
        ageMinString=savedInstanceState.getString("agemin");
        ageMinString=savedInstanceState.getString("agemax");
        provinceString=savedInstanceState.getString("province");
        cityString=savedInstanceState.getString("city");
        distString=savedInstanceState.getString("dist");
        initView();

    }

    @Override
    protected void onPause() {

        SharedPreferences stateShare=getSharedPreferences("select", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=stateShare.edit();

        editor.putBoolean("onoff", onoff);
        editor.putInt("peoplesex", peopleSexIndex);
        editor.putInt("dogsex", dogSexIndex);
        editor.putInt("dogtype", dogTypeIndex);
        editor.putString("agemin", ageMinString);
        editor.putString("agemax", ageMaxString);
        editor.putString("province", provinceString);
        editor.putString("city", cityString);
        editor.putString("dist", distString);

        editor.commit();
        super.onPause();


    }

    @Override
    protected void onDestroy() {
//        SharedPreferences stateShare=getPreferences( Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor=stateShare.edit();
//
//        editor.putBoolean("onoff",onoff);
//        editor.putInt("peoplesex", peopleSexIndex);
//        editor.putInt("dogsex", dogSexIndex);
//        editor.putInt("dogtype", dogTypeIndex);
//        editor.putString("agemin", ageMinString);
//        editor.putString("agemax", ageMaxString);
//        editor.putString("province", provinceString);
//        editor.putString("city", cityString);
//        editor.putString("dist", distString);
//
//        editor.commit();
//

        super.onDestroy();
    }
}
