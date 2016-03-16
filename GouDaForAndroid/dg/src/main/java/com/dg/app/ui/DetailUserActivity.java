package com.dg.app.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.bean.Room;
import com.dg.app.widget.SwitchView;
import com.umeng.message.PushAgent;

import org.kymjs.kjframe.KJBitmap;

import java.util.ArrayList;
import java.util.List;

public class DetailUserActivity extends AppCompatActivity {

    private ViewPager photos;//用户照片集
    private TextView name;//用户名字
    private ImageView gender;//用户性别
    private TextView age;
    private TextView work;
    private TextView cutevalues;//萌值
    private TextView fanscount;//粉丝数
    //标签
    private Button tag1;
    private Button tag2;
    private Button tag3;
    private Button tag4;
    private Button tag5;
    private Button tag6;
    //说说
    private TextView year1;
    private TextView month1;
    private ImageView sayUrl1;
    private TextView content1;

    private TextView year2;
    private TextView month2;
    private ImageView sayUrl2;
    private TextView content2;

    private TextView year3;
    private TextView month3;
    private ImageView sayUrl3;
    private TextView content3;

    private SwitchView switchView;

    List<View> pageviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_detail_user);
        initView();
        initData();
    }
    public void initView()
    {
        ActionBar actionBar =this.getSupportActionBar();
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        LayoutInflater inflater = (LayoutInflater) this.getLayoutInflater();
        View titleView = inflater.inflate(R.layout.actionbar_user_room, null);
        actionBar.setCustomView(titleView, lp);
        actionBar.setDisplayShowHomeEnabled(false);//去掉导航
        actionBar.setDisplayShowTitleEnabled(false);//去掉标题
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);


       photos=(ViewPager)findViewById(R.id.room_viewpager);
      name=(TextView)findViewById(R.id.room_name);
        gender=(ImageView)findViewById(R.id.room_sex);
        age=(TextView)findViewById(R.id.room_age);
        work=(TextView)findViewById(R.id.room_work);
      cutevalues=(TextView)findViewById(R.id.room_cutevalue);
      fanscount=(TextView)findViewById(R.id.room_fanscount);
        tag1=(Button)findViewById(R.id.room_tag_1);
       tag2=(Button)findViewById(R.id.room_tag_2);
         tag3=(Button)findViewById(R.id.room_tag_3);
         tag4=(Button)findViewById(R.id.room_tag_4);
         tag5=(Button)findViewById(R.id.room_tag_5);
        tag6=(Button)findViewById(R.id.room_tag_6);
        //说说
        year1=(TextView)findViewById(R.id.room_year_1);
        month1=(TextView)findViewById(R.id.room_month_1);
        sayUrl1=(ImageView)findViewById(R.id.room_picture_1);
        content1=(TextView)findViewById(R.id.room_content_1);

        year2=(TextView)findViewById(R.id.room_year_2);
        month2=(TextView)findViewById(R.id.room_month_2);
        sayUrl2=(ImageView)findViewById(R.id.room_picture_2);
        content2=(TextView)findViewById(R.id.room_content_2);


        year3=(TextView)findViewById(R.id.room_year_3);
        month3=(TextView)findViewById(R.id.room_month_3);
        sayUrl3=(ImageView)findViewById(R.id.room_picture_3);
        content3=(TextView)findViewById(R.id.room_content_3);

        switchView=(SwitchView)findViewById(R.id.room_switch_view);
    }
    public void initData()
    {
        Room room=new Room();

        switchView.getLeftView().setText("个人主页");
        switchView.getRightView().setText("狗狗文档");
        KJBitmap kjBitmap=new KJBitmap();
        ImageView imageView1=new ImageView(this);
        imageView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        kjBitmap.display(imageView1, "http://a.hiphotos.baidu.com/zhidao/pic/item/fcfaaf51f3deb48f5c145614f01f3a292cf578ff.jpg");

        ImageView imageView2=new ImageView(this);
        imageView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        kjBitmap.display(imageView2, "http://img.taopic.com/uploads/allimg/121026/240425-12102621551331.jpg");

        ImageView imageView3=new ImageView(this);
        imageView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        kjBitmap.display(imageView3, "http://img.taopic.com/uploads/allimg/121030/240425-12103019162713.jpg");

        pageviews=new ArrayList<>();
        pageviews.add(imageView1);
        pageviews.add(imageView2);
        pageviews.add(imageView3);
        initViewPager();


        name.setText("奖项");
        gender.setBackgroundResource(R.mipmap.userinfo_icon_male);
        age.setText("15");
        work.setText("学生党");
        cutevalues.setText("111111");
        fanscount.setText("222222");
        //标签
      tag1.setText("很乖");
       tag2.setText("好人");
       tag3.setText("爱好和平");
      tag4.setText("hahaha");
       tag5.setText("创新");
       tag6.setText("乐观");
    year1.setText("15");
    month1.setText("九月");
       sayUrl1.setBackgroundResource(R.mipmap.dog);
      content1.setText("kdjfkdfjdkfjjfdk\ndfd");

        year2.setText("15");
        month2.setText("九月");
        sayUrl2.setBackgroundResource(R.mipmap.dog);
        content2.setText("kdjfkdfjdkfjjfdk\ndfd");


        year3.setText("15");
        month3.setText("九月");
        sayUrl3.setBackgroundResource(R.mipmap.dog);
        content3.setText("kdjfkdfjdkfjjfdk\ndfd");




    }
    public  void initViewPager()
    {
        //数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter(){

            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageviews.size();
            }

            @Override
            //断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0==arg1;
            }
            //是从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(pageviews.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1){
                ((ViewPager)arg0).addView(pageviews.get(arg1));
                return pageviews.get(arg1);
            }


        };

        //绑定适配器
        photos.setAdapter(mPagerAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
