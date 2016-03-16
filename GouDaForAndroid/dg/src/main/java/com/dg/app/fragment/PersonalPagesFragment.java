package com.dg.app.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.adapter.MyMomentAdapter;
import com.dg.app.adapter.MyMomentsAdapter;
import com.dg.app.adapter.QuestionAdapter;
import com.dg.app.adapter.StatusReplysAdapter;
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.ListBaseAdapter;
import com.dg.app.base.PageBaseListFragment;
import com.dg.app.bean.DGUserDetailResponse;
import com.dg.app.bean.Moment;
import com.dg.app.bean.MomentsList;
import com.dg.app.bean.Question;
import com.dg.app.bean.QuestionList;
import com.dg.app.bean.Reply;
import com.dg.app.bean.Tag;
import com.dg.app.ui.DGUserInfoActivity;
import com.dg.app.ui.DetailDKActivity;
import com.dg.app.ui.MyDetailInfoEditActivity;
import com.dg.app.ui.TweetDetailActivity;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.ui.widget.listview.PullToRefreshBase;
import com.dg.app.ui.widget.listview.PullToRefreshList;
import com.dg.app.util.DisplayUtils;
import com.dg.app.util.SpecialTextUtils;
import com.dg.app.util.StringUtils;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.kymjs.kjframe.KJBitmap;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class PersonalPagesFragment extends PageBaseListFragment{

    private static final String CACHE_KEY_PREFIX = "personal_page_";

    private KJBitmap kjBitmap;

    private DGUserInfoActivity dGActivity;

    public void setdGActivity(DGUserInfoActivity dGActivity) {
        this.dGActivity = dGActivity;
    }

    //顶部actionbar
    private LinearLayout ll_viewpager;
    private ViewPager photos;//用户照片集
    private TextView name;//用户名字
    private ImageView gender;//用户性别
    private TextView age;
    private TextView work;
    private TextView cutevalues;//萌值
    private TextView fanscount;//粉丝数

    private List<View> pageviews;

    private TextView label_1;
    private TextView label_2;
    private TextView label_3;
    private TextView label_4;
    private TextView label_5;
    private TextView label_6;

    private List<TextView> labels;

    private DGUserDetailResponse.UserDetail userDetail;

    private String[] months = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};

    //提示组件
    private DGNoticeToast dgNoticeToast;
    private ProgressDialog proDia;

    /**
     * 初始化提示组件
     * @param context
     */
    public void initNoticeWidget(Context context) {
        proDia = new ProgressDialog(context);
        dgNoticeToast = new DGNoticeToast(context);
    }

    @Override
    protected MyMomentAdapter getListAdapter() {
        return new MyMomentAdapter(getActivity());
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX;
    }

    @Override
    protected MomentsList parseList(InputStream is) throws Exception {
        MomentsList list = null;
        try {
            list = XmlUtils.toBean(MomentsList.class, is);
        } catch (NullPointerException e) {
            list = new MomentsList();
        }
        return list;
    }

    @Override
    protected MomentsList readList(Serializable seri) {
        return ((MomentsList) seri);
    }
    //该方法是请求网络的，当注册登录完成后，通过Appcontext获得相应的urse_id
    @Override
    protected void sendRequestData() {
        page_size = 3;
        //加载最新动态
        DGApi.myMoment(userid, mCurrentPage, page_size, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

    }

    @Override
    protected void executeOnLoadDataSuccess(List<Moment> data) {
        try {
            super.executeOnLoadDataSuccess(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //根据获得数据填充UI
    public void setData()
    {
        kjBitmap = new KJBitmap();

        if(dGActivity!=null){
            userid = dGActivity.getUser_id();
        }
        if(userid==0){
            getActivity().finish();
            return;
        }
        proDia.setMessage("正在加载...");
        proDia.show();
        pageviews = new ArrayList<>();
        //加载个人信息
        DGApi.userDetail(userid, AppContext.getInstance().getLoginUid(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (isAdded()) {
                    proDia.dismiss();
                    DGUserDetailResponse dgUserDetailResponse = XmlUtils.toBean(DGUserDetailResponse.class, responseBody);

                    if (dgUserDetailResponse.getCode() == 0) {
                        //用户头像
                        String logo = dgUserDetailResponse.getUserDetail().getLogo();
                        if (logo != null && !"".equals(logo)) {
                            ImageView imageView = new ImageView(getActivity());
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            kjBitmap.display(imageView, APIconfig.IMG_BASEURL + logo, imageView.getWidth(), imageView.getHeight(), R.mipmap.default_image);
                            pageviews.add(imageView);
                        }
                        //用户图片列表
                        DGUserDetailResponse.ImgList imgList = dgUserDetailResponse.getUserDetail().getImgList();
                        if (imgList != null) {
                            List<String> imgUrlList = imgList.getImgList();
                            if (imgList != null && imgUrlList.size() > 0) {
                                for (String img : imgUrlList) {
                                    ImageView imageView = new ImageView(getActivity());
                                    imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    kjBitmap.display(imageView, APIconfig.IMG_BASEURL + img, imageView.getWidth(), imageView.getHeight(), R.mipmap.default_image);
                                    pageviews.add(imageView);
                                }
                            }
                        }

                        if (pageviews.size() == 0) {
                            ImageView imageView1 = new ImageView(getActivity());
                            imageView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            imageView1.setImageResource(R.mipmap.pagefailed_bg);
                            pageviews.add(imageView1);
                        }

                        initViewPager();

                        updateData(dgUserDetailResponse);
                    } else {
                        dgNoticeToast.showFailure(dgUserDetailResponse.getMsg());
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                proDia.dismiss();
                dgNoticeToast.showFailure("网络异常");
            }
        });
    }

    //初始化适配器
    public  void initViewPager()
    {
        //数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter(){
            @Override
            //获取当前窗体界面数
            public int getCount() {
                return pageviews.size();
            }
            @Override
            //断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
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

    //更新数据
    private void updateData(DGUserDetailResponse mResult) {
        userDetail = mResult.getUserDetail();
        if(userDetail!=null){
            //姓名
            name.setText(userDetail.getNickname());

            //性别
            if(userDetail.getSex()==0){
                gender.setImageResource(R.mipmap.ic_round_man);
            }else if(userDetail.getSex()==1){
                gender.setImageResource(R.mipmap.ic_round_woman);
            }

            //年龄
            age.setText(userDetail.getAge()+"岁");

            //职业
            work.setText(userDetail.getCareer());

            //TODO
            //萌值
            cutevalues.setText(userDetail.getGrade()+"");

            //TODO
            //粉丝数
            fanscount.setText(userDetail.getFans_num()+"");

            //标签
            List<Tag> tags = userDetail.getTags();
            if(tags!=null){
                if(tags.size()==0){
                    labels.get(0).setText("无");
                    labels.get(0).setVisibility(View.VISIBLE);
                }else {
                    for (int i = 0; i < 6; i++) {
                        if (i < tags.size()) {
                            labels.get(i).setText(tags.get(i).getName());
                            labels.get(i).setVisibility(View.VISIBLE);
                        } else {
                            labels.get(i).setVisibility(View.GONE);
                        }
                    }
                }
            }else{
                labels.get(0).setText("无");
                labels.get(0).setVisibility(View.VISIBLE);
                for(int i=1;i<6;i++){
                    labels.get(i).setVisibility(View.GONE);
                }
            }


        }else{
            Toast.makeText(getActivity(), "请完善您的资料", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MyDetailInfoEditActivity.class);
            startActivity(intent);
        }
    }

    //初始化头部视图
    public  View initHeaderView()
    {
        View root = null;
        try{
            root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dg_user_info_new,null);

            ll_viewpager = (LinearLayout) root.findViewById(R.id.ll_viewpager);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ll_viewpager.getLayoutParams();
            int screen_width = DisplayUtils.getScreenWidthPixels(getActivity());
            linearParams.width = screen_width;
            linearParams.height = screen_width;
            ll_viewpager.setLayoutParams(linearParams);

            photos=(ViewPager)root.findViewById(R.id.room_viewpager);
            name=(TextView)root.findViewById(R.id.room_name);
            gender=(ImageView)root.findViewById(R.id.room_sex);
            age=(TextView)root.findViewById(R.id.room_age);
            work=(TextView)root.findViewById(R.id.room_work);
            cutevalues=(TextView)root.findViewById(R.id.room_cutevalue);
            fanscount=(TextView)root.findViewById(R.id.room_fanscount);

            label_1 = (TextView)root.findViewById(R.id.room_tag_1);
            label_2 = (TextView)root.findViewById(R.id.room_tag_2);
            label_3 = (TextView)root.findViewById(R.id.room_tag_3);
            label_4 = (TextView)root.findViewById(R.id.room_tag_4);
            label_5 = (TextView)root.findViewById(R.id.room_tag_5);
            label_6 = (TextView)root.findViewById(R.id.room_tag_6);

            labels = new ArrayList<>();
            labels.add(label_1);
            labels.add(label_2);
            labels.add(label_3);
            labels.add(label_4);
            labels.add(label_5);
            labels.add(label_6);

        }catch (Exception e){
            e.printStackTrace();
        }

        return  root;
    }

    @Override
    protected long getAutoRefreshTime() {

        return 60 * 60 * 60;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public DGUserDetailResponse.UserDetail getUserDetail() {

        if(userDetail!=null){
            return userDetail;
        }
        return null;
    }

}
