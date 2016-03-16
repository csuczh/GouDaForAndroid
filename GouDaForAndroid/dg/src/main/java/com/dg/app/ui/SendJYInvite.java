package com.dg.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.City;
import com.dg.app.bean.Foster_Response;
import com.dg.app.bean.Province;
import com.dg.app.ui.dialog.DatePickerDialog;
import com.dg.app.util.AddDayAndCurrent;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.message.PushAgent;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import butterknife.OnClick;

public class SendJYInvite extends AppCompatActivity {

    //开始时间和结束时间
    TextView startTime;
    TextView endTime;
    //图标
    ImageView startImage;
    ImageView endImage;

    //遛狗信息的地理位置信息
    TextView geoAdress;
    //百度地图选择位置的入口
    ImageView baiduMap;

    //位置信息
    String address;
    //省
    String province;
    //市
    String city;
    //经度
    String lng;
    //维度
    String lat;
    int cityid=-1;
    //年月日
    int year;
    int month;
    int day;

    Activity activity;

    private EditText et_write_status;
    public static int MAX_LEN = 50;
    private TextView tweet_text_record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jy_send_invite);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

        ActionBar actionBar =this.getSupportActionBar();
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        LayoutInflater inflater = (LayoutInflater) this.getLayoutInflater();
        View titleView = inflater.inflate(R.layout.actionbar_title_invite, null);
        actionBar.setCustomView(titleView, lp);
        actionBar.setDisplayShowHomeEnabled(false);//去掉导航
        actionBar.setDisplayShowTitleEnabled(false);//去掉标题
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);

        ImageView imageView=(ImageView)this.findViewById(R.id.send_image_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView sendInvite=(TextView)this.findViewById(R.id.send_invite);
        sendInvite.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              String message = et_write_status.getText().toString();
                                              String start = startTime.getText().toString();
                                              String end = endTime.getText().toString();
                                              String place = geoAdress.getText().toString();
                                              if (message.replaceAll(" ", "") == "") {
                                                  Toast.makeText(activity, "请输入邀请内容！", Toast.LENGTH_LONG).show();
                                                  return;
                                              }
                                              if (start.replaceAll(" ","") == "") {
                                                  Toast.makeText(activity, "开始时间必须写！", Toast.LENGTH_LONG).show();
                                                  return;
                                              }
                                              if (place == "") {
                                                  Toast.makeText(activity, "遛狗地点必须写！", Toast.LENGTH_LONG).show();
                                                  return;
                                              }
                                              if (end.replaceAll(" ","") == "") {
                                                  Toast.makeText(activity, "结束时间必须写！", Toast.LENGTH_LONG).show();
                                                  return;
                                              }
                                                  String user_id=  AppContext.getInstance().getProperty("dgUser.userid");
                                                  int int_user_Id=Integer.parseInt(user_id);
                                                  String content = message;
                                                  String deadline = end;
                                                  String off_tiem = deadline;
                                                  int city_id = cityid;
                                                  String pos = lng + "," + lat;
                                                  DGApi.sendJY("android", int_user_Id, content, deadline, off_tiem, city_id, pos, new AsyncHttpResponseHandler() {
                                                      @Override
                                                      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                          Toast.makeText(activity, "发送成功", Toast.LENGTH_LONG).show();
                                                          Foster_Response foster_response=parseFoster(new ByteArrayInputStream(responseBody));
                                                          Intent   mIntent=new Intent();
                                                          mIntent.putExtra("lng",lng+"");
                                                          mIntent.putExtra("lat",lat+"");
                                                          mIntent.putExtra("invite_id",String.valueOf(foster_response.getFoster_id()));
                                                          SendJYInvite.this.setResult(11,mIntent);
                                                          finish();
                                                      }
                                                      @Override
                                                      public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                          Toast.makeText(activity, new String(responseBody), Toast.LENGTH_LONG).show();
                                                      }
                                                  });
                                          }
                                      }
        );

        startTime=(TextView)this.findViewById(R.id.ji_start_text);
        endTime=(TextView)this.findViewById(R.id.ji_end_text);

        startImage=(ImageView)this.findViewById(R.id.ji_start_image);
        endImage=(ImageView)this.findViewById(R.id.ji_end_image);
        // 输入框
        et_write_status = (EditText) findViewById(R.id.send_jy_edittext);
        tweet_text_record = (TextView) findViewById(R.id.tweet_text_record);
        et_write_status.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() >= MAX_LEN) {
                    tweet_text_record.setText("邀请超过字数啦");
                } else {
                    tweet_text_record.setText((MAX_LEN - s.length())
                            + " X");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > MAX_LEN) {
                    et_write_status.setText(s.subSequence(0, MAX_LEN));
                    CharSequence text = et_write_status.getText();
                    if (text instanceof Spannable) {
                        Selection.setSelection((Spannable) text, MAX_LEN);
                    }
                }
            }
        });


        baiduMap=(ImageView)this.findViewById(R.id.ld_address_imageview);
        geoAdress=(TextView)this.findViewById(R.id.ld_address);

        baiduMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(activity, BaiduMapActivity.class), 4);
            }
        });

        activity=this;


        startImage.setOnClickListener(new View.OnClickListener() {
            Calendar c=Calendar.getInstance();
            @Override
            public void onClick(View v) {

                final DatePickerDialog datePickerDialog=new DatePickerDialog(SendJYInvite.this, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth) {
                     startTime.setText(String.format("%d-%d-%d",startYear,startMonthOfYear+1,startDayOfMonth));
                        year=startYear;
                        month=startMonthOfYear+1;
                        day=startDayOfMonth;
                    }
                },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE),true);
                //设置对话框的位置
                WindowManager m=getWindowManager();
                Window dialogWindow=datePickerDialog.getWindow();
                WindowManager.LayoutParams lp=dialogWindow.getAttributes();
                Display d=m.getDefaultDisplay();
                lp.height=(int)(d.getHeight()*0.6);
                lp.width=(int)(d.getWidth());
                dialogWindow.setAttributes(lp);
                dialogWindow.setGravity(Gravity.BOTTOM);
                datePickerDialog.show();
            }
        });



        endImage.setOnClickListener(new View.OnClickListener() {
            Calendar c=Calendar.getInstance();
            @Override
            public void onClick(View v) {

                if(startTime.getText().toString()=="")
                {
                    Toast.makeText(activity,"请先选择开始时间",Toast.LENGTH_LONG).show();
                    return;
                }
                final DatePickerDialog datePickerDialog=new DatePickerDialog(SendJYInvite.this, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth) {
                        endTime.setText(String.format("%d-%d-%d",startYear,startMonthOfYear+1,startDayOfMonth));

                    }
                },year,month,day,true);
                //设置对话框的位置
                WindowManager m=getWindowManager();
                Window dialogWindow=datePickerDialog.getWindow();
                WindowManager.LayoutParams lp=dialogWindow.getAttributes();
                Display d=m.getDefaultDisplay();
                lp.height=(int)(d.getHeight()*0.6);
                lp.width=(int)(d.getWidth());
                dialogWindow.setAttributes(lp);
                dialogWindow.setGravity(Gravity.BOTTOM);
                datePickerDialog.show();



            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            lng = data.getStringExtra("longitude");
            lat = data.getStringExtra("latitude");
            address = data.getStringExtra("address");

            String temp[] = address.split(" ");
            province = temp[0];
            city = temp[1];
            geoAdress.setText(address.replaceAll(" ", ""));
            String temp_province = province.substring(0, province.length() - 1);
            Toast.makeText(activity, "temp_province" + temp_province, Toast.LENGTH_LONG).show();
            DGApi.getCtyList("android", temp_province, mHandler);

            Toast.makeText(activity, lng + " " + lat + " " + address, Toast.LENGTH_LONG).show();
        }

    }
    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBytes) {
            String str=new String(responseBytes);
            try {
                Province province= parseList(new ByteArrayInputStream(responseBytes));
                List<City> cityList=province.getList();
                String temp_city= city.substring(0, city.length() - 1);
                cityid=-1;
                for(int i=0;i<cityList.size();i++)
                {
                    if(cityList.get(i).getName().equals(temp_city))
                    {
                        cityid=cityList.get(i).getCityid();
                        Toast.makeText(activity,cityid+"city",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                if(cityid==-1)
                {
                    Toast.makeText(activity,"没有当前城市的id",Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            Toast.makeText(activity,"请求网络失败，检查网络是否正常！",Toast.LENGTH_LONG).show();
        }
    };
    protected Province parseList(InputStream is) throws Exception {
        Province list = null;
        try {
            list = XmlUtils.toBean(Province.class, is);
        } catch (NullPointerException e) {
            list = new Province();
        }
        return list;
    }
    //当接受邀请时把返回的值进行解析
    protected Foster_Response parseFoster(InputStream is)
    {
        Foster_Response foster_response;
        try {
            foster_response = XmlUtils.toBean(Foster_Response.class, is);
        }catch (NullPointerException e)
        {
            foster_response=new Foster_Response();
        }
        return foster_response;
    }
   
}
