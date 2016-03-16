package com.dg.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.R;
import com.dg.app.bean.Tweet;
import com.dg.app.bean.TweetsList;
import com.dg.app.ui.MyDetailInfoEditActivity;
import com.dg.app.ui.widget.EmptyLayout;
import com.dg.app.ui.widget.listview.PullToRefreshBase;
import com.dg.app.ui.widget.listview.PullToRefreshList;
import com.dg.app.util.Parser;
import com.dg.app.widget.SwitchView;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class MyDetailInfoFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    private Context context;
    private KJBitmap kjBitmap=new KJBitmap();

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


    List<View> pageviews;


    private int whichPage = 1;

//    //绑定空布局视图
//    private EmptyLayout mEmptyLayout;
//
//    //绑定下拉刷新视图
//    private PullToRefreshList mRefreshLayout;
//
//    private ListView mListView;

    //顶部actionbar
    private LinearLayout titlebar_my_detailinfo;
    private ImageView iv_detailinfo_titlebar_back;
    private Button button_my_page;
    private Button button_dog_page;
    private TextView tv_detailinfo_edit;

    //TODO
    //适配器


    private KJHttp kjh;
    private final Set<Tweet> tweets = new TreeSet<Tweet>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = View.inflate(getActivity(),
                R.layout.fragment_detail_user, null);

//        mRefreshLayout = (PullToRefreshList)root.findViewById(R.id.prl_rank);
//        mEmptyLayout = (EmptyLayout)root.findViewById(R.id.rank_empty_layout);

        initTitlebBar(root);

        initView(root);

        initData();
//        initWidget(root);

        return root;
    }

    private void initData() {
        ImageView imageView1=new ImageView(context);
        imageView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        kjBitmap.display(imageView1, "http://a.hiphotos.baidu.com/zhidao/pic/item/fcfaaf51f3deb48f5c145614f01f3a292cf578ff.jpg");

        ImageView imageView2=new ImageView(context);
        imageView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        kjBitmap.display(imageView2, "http://img.taopic.com/uploads/allimg/121026/240425-12102621551331.jpg");

        ImageView imageView3=new ImageView(context);
        imageView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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

    //初始化视图
    private void initView(View root) {
        photos=(ViewPager)root.findViewById(R.id.room_viewpager);
        name=(TextView)root.findViewById(R.id.room_name);
        gender=(ImageView)root.findViewById(R.id.room_sex);
        age=(TextView)root.findViewById(R.id.room_age);
        work=(TextView)root.findViewById(R.id.room_work);
        cutevalues=(TextView)root.findViewById(R.id.room_cutevalue);
        fanscount=(TextView)root.findViewById(R.id.room_fanscount);
        tag1=(Button)root.findViewById(R.id.room_tag_1);
        tag2=(Button)root.findViewById(R.id.room_tag_2);
        tag3=(Button)root.findViewById(R.id.room_tag_3);
        tag4=(Button)root.findViewById(R.id.room_tag_4);
        tag5=(Button)root.findViewById(R.id.room_tag_5);
        tag6=(Button)root.findViewById(R.id.room_tag_6);
        //说说
        year1=(TextView)root.findViewById(R.id.room_year_1);
        month1=(TextView)root.findViewById(R.id.room_month_1);
        sayUrl1=(ImageView)root.findViewById(R.id.room_picture_1);
        content1=(TextView)root.findViewById(R.id.room_content_1);

        year2=(TextView)root.findViewById(R.id.room_year_2);
        month2=(TextView)root.findViewById(R.id.room_month_2);
        sayUrl2=(ImageView)root.findViewById(R.id.room_picture_2);
        content2=(TextView)root.findViewById(R.id.room_content_2);


        year3=(TextView)root.findViewById(R.id.room_year_3);
        month3=(TextView)root.findViewById(R.id.room_month_3);
        sayUrl3=(ImageView)root.findViewById(R.id.room_picture_3);
        content3=(TextView)root.findViewById(R.id.room_content_3);

    }

    //初始化标题栏
    private void initTitlebBar(View root) {
        titlebar_my_detailinfo = (LinearLayout) root.findViewById(R.id.titlebar_my_detailinfo);
        iv_detailinfo_titlebar_back = (ImageView) titlebar_my_detailinfo.findViewById(R.id.iv_detailinfo_titlebar_back);
        button_my_page = (Button) titlebar_my_detailinfo.findViewById(R.id.button_my_page);
        button_dog_page = (Button) titlebar_my_detailinfo.findViewById(R.id.button_dog_page);
        tv_detailinfo_edit = (TextView) titlebar_my_detailinfo.findViewById(R.id.tv_detailinfo_edit);

        iv_detailinfo_titlebar_back.setOnClickListener(this);
        button_my_page.setOnClickListener(this);
        button_dog_page.setOnClickListener(this);
        tv_detailinfo_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_detailinfo_titlebar_back:{
                Toast.makeText(getActivity(), "退出我的详细信息页", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                break;
            }

            case R.id.button_my_page:{
                //我的主页
                showMyPage();
                break;
            }

            case R.id.button_dog_page:{
                //狗狗主页
                showDogPage();
                break;
            }

            case R.id.tv_detailinfo_edit:{
                //编辑
                if(whichPage==1){
                    Toast.makeText(getActivity(), "进入我的信息编辑页", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MyDetailInfoEditActivity.class);
                    context.startActivity(intent);
                }else if(whichPage==2){
                    Toast.makeText(getActivity(), "进入狗狗信息编辑页", Toast.LENGTH_SHORT).show();
                }

                break;
            }


        }
    }



    private void showMyPage() {
        whichPage=1;
        //更新顶部actionbar
        updateActionBar(1);
        //更新Listview的数据源

    }

    private void showDogPage() {
        whichPage=2;
        //更新顶部actionbar
        updateActionBar(2);
        //更新Listview的数据源

    }

    public void updateActionBar(int index){
        button_my_page.setBackgroundResource(R.drawable.bg_actionbar_corner);
        button_dog_page.setBackgroundResource(R.drawable.bg_actionbar_corner);
        button_my_page.setTextColor(getResources().getColor(R.color.white));
        button_dog_page.setTextColor(getResources().getColor(R.color.white));

        if(index==1){
            button_my_page.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            button_my_page.setTextColor(getResources().getColor(R.color.actionbar_normal));
        }
        if(index==2){
            button_dog_page.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            button_dog_page.setTextColor(getResources().getColor(R.color.actionbar_normal));
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
