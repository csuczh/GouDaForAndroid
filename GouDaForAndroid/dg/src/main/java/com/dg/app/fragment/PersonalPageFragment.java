package com.dg.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.DGUser;
import com.dg.app.bean.DGUserDetailResponse;
import com.dg.app.bean.Moment;
import com.dg.app.bean.MomentsList;
import com.dg.app.bean.Tag;
import com.dg.app.ui.DGUserInfoActivity;
import com.dg.app.ui.FansListActivity;
import com.dg.app.ui.LoginActivity;
import com.dg.app.ui.MyDetailInfoEditActivity;
import com.dg.app.ui.TweetDetailActivity;
import com.dg.app.ui.toast.DGNoticeToast;
import com.dg.app.util.DisplayUtils;
import com.dg.app.util.SpecialTextUtils;
import com.dg.app.util.StringUtils;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.kymjs.kjframe.KJBitmap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class PersonalPageFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    private static final String CACHE_KEY_PREFIX = "personal_page_";

    private KJBitmap kjBitmap = new KJBitmap();;

    private int user_id;

    private DGUserInfoActivity dGActivity;

    public void setdGActivity(DGUserInfoActivity dGActivity) {
        this.dGActivity = dGActivity;
    }
//    private ParserTask mParserTask;
//    private AsyncTask<String, Void, DGUserDetailResponse> mCacheTask;

    //顶部actionbar
    private LinearLayout ll_viewpager;
    private ViewPager photos;//用户照片集
    private TextView name;//用户名字
    private ImageView gender;//用户性别
    private TextView age;
    private TextView work;
    private TextView cutevalues;//萌值

    private LinearLayout ll_fans;

    private TextView fanscount;//粉丝数

    private List<View> pageviews;

    private TextView label_1;
    private TextView label_2;
    private TextView label_3;
    private TextView label_4;
    private TextView label_5;
    private TextView label_6;

    private List<TextView> labels;

    private LinearLayout ll_card_new_moment;

    private TextView room_day;
    private TextView room_month;
    private ImageView room_picture;
    private TextView room_content;

    private DGUserDetailResponse.UserDetail userDetail;

    private String[] months = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};

    //提示组件
    private DGNoticeToast dgNoticeToast;
    private ProgressDialog proDia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = View.inflate(getActivity(),
                R.layout.fragment_dg_user_info, null);

        initNoticeWidget(getActivity());

        initView(root);

        initData();

        return root;
    }

    /**
     * 初始化提示组件
     * @param context
     */
    private void initNoticeWidget(Context context) {
        proDia = new ProgressDialog(context);
        dgNoticeToast = new DGNoticeToast(context);
    }

    //    初始化视图组件
    protected void initView(View root) {
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

        ll_fans = (LinearLayout) root.findViewById(R.id.ll_fans);

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

        ll_card_new_moment = (LinearLayout) root.findViewById(R.id.ll_card_new_moment);

        room_day = (TextView)root.findViewById(R.id.room_day);
        room_month= (TextView)root.findViewById(R.id.room_month);
        room_picture= (ImageView)root.findViewById(R.id.room_picture);
        room_content= (TextView)root.findViewById(R.id.room_content);
    }


    private void initData() {

        if(dGActivity!=null){
            user_id = dGActivity.getUser_id();
        }
        if(user_id==0){
            getActivity().finish();
            return;
        }
        proDia.setMessage("正在加载...");
        proDia.show();
        pageviews = new ArrayList<>();
            //加载个人信息
            DGApi.userDetail(user_id, AppContext.getInstance().getLoginUid(),new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (isAdded()) {
                        proDia.dismiss();
                        DGUserDetailResponse dgUserDetailResponse = XmlUtils.toBean(DGUserDetailResponse.class, responseBody);

                        if (dgUserDetailResponse.getCode() == 0) {
                            //用户头像
                            String logo = dgUserDetailResponse.getUserDetail().getLogo();
                            if(logo!=null&&!"".equals(logo)){
                                ImageView imageView = new ImageView(getActivity());
                                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                kjBitmap.display(imageView, APIconfig.IMG_BASEURL + logo,imageView.getWidth(),imageView.getHeight(),R.mipmap.default_image);
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
                                        kjBitmap.display(imageView, APIconfig.IMG_BASEURL + img,imageView.getWidth(),imageView.getHeight(),R.mipmap.default_image);
                                        pageviews.add(imageView);
                                    }
                                }
                            }

                            if(pageviews.size()==0){
                                ImageView imageView1 = new ImageView(getActivity());
                                imageView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                imageView1.setImageResource(R.mipmap.pagefailed_bg);
                                pageviews.add(imageView1);
                            }


                            initViewPager();

                            updateData(dgUserDetailResponse);
                            loadNewMoment();
                        }else{
                            dgNoticeToast.showFailure(dgUserDetailResponse.getMsg());
                            getActivity().finish();
//                            Toast.makeText(getActivity(),dgUserDetailResponse.getMsg(),Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    proDia.dismiss();
                    dgNoticeToast.showFailure("网络异常");
                    getActivity().finish();
//                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void loadNewMoment() {
            //加载最新动态
            DGApi.myMoment(user_id, 0, 1, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    MomentsList momentsList = XmlUtils.toBean(MomentsList.class, responseBody);
                    if (momentsList.getCode() == 0) {
                        List<Moment> list = momentsList.getList();
                        if (list != null && list.size() > 0) {
                            final Moment moment = list.get(0);
                            if (moment != null) {
                                Date date = StringUtils.toDate(moment.getTime());
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);

                                int day = calendar.get(Calendar.DAY_OF_MONTH);

                                int month = calendar.get(Calendar.MONTH);

                                Log.i("PersonalPageMoment", day + "");

                                Log.i("PersonalPageMoment", month + "");
                                //天
                                room_day.setText(day + "");
                                //月
                                room_month.setText(months[month]);

                                //图片
                                String imgs = moment.getImages();
                                if (imgs == null || imgs.trim() == "") {
                                    room_picture.setVisibility(View.GONE);
                                } else {
                                    String first_img = imgs.split(",")[0];
                                    kjBitmap.display(room_picture, APIconfig.IMG_BASEURL + first_img,room_picture.getWidth(),room_picture.getHeight(),R.mipmap.default_image);
                                    room_picture.setVisibility(View.VISIBLE);
                                }

                                //状态正文
                                room_content.setText(SpecialTextUtils.getWeiboContent(getActivity(), room_content, moment.getContent()));


                                ll_card_new_moment.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), TweetDetailActivity.class);
                                        intent.putExtra("status", moment);
                                        getActivity().startActivity(intent);

                                    }
                                });

                            }

                        }
                    } else {
                        AppContext.showToast(momentsList.getMsg());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                }
            });

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

            ll_fans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FansListActivity.class);
                    intent.putExtra("user_id",user_id);
                    startActivity(intent);
                }
            });

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

    public DGUserDetailResponse.UserDetail getUserDetail() {
        return userDetail;
    }


    public void refresh(){

    }

    //    //解析数据成功
//    private void executeDataSuccess(DGUserDetailResponse mResult) {
//        if(mResult!=null && mResult.getCode()==0){
//            DGUserDetailResponse.UserDetail userDetail = mResult.getUserDetail();
//
//            //姓名
//            name.setText(userDetail.getNickname());
//
//            //性别
//            if(userDetail.getSex()==0){
//                gender.setImageResource(R.mipmap.ic_round_man);
//            }else if(userDetail.getSex()==1){
//                gender.setImageResource(R.mipmap.ic_round_woman);
//            }
//
//            //年龄
//            age.setText(userDetail.getAge()+"岁");
//
//            //职业
//            work.setText(userDetail.getCareer());
//
//            //TODO
//            //萌值
//            cutevalues.setText("123456");
//
//            //TODO
//            //粉丝数
//            fanscount.setText("654321");
//
//            //标签
//            List<String> tags = userDetail.getTags().getTags();
//            if(tags.size()==0){
//                labels.get(0).setText("无");
//            }else{
//                for(int i=0;i<6;i++){
//                    if(i<=tags.size()){
//                        labels.get(i).setText(tags.get(i));
//                    }else {
//                        labels.get(i).setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            //最新动态
//            DGApi.myMoment(10, 0, 1, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    MomentsList momentsList = XmlUtils.toBean(MomentsList.class, responseBody);
//                    if (momentsList.getCode() == 0) {
//                        Moment moment = momentsList.getList().get(0);
//                        Date date = StringUtils.toDate(moment.getTime());
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.setTime(date);
//
//                        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//                        int month = calendar.get(Calendar.MONTH);
//
//                        //天
//                        room_day.setText(day);
//
//                        //月
//                        room_month.setText(months[month]);
//
//                        //图片
//                        String imgs = moment.getImages();
//                        if (imgs == null || imgs.trim() == "") {
//                            room_picture.setVisibility(View.GONE);
//                        } else {
//                            String first_img = imgs.split(",")[0];
//                            kjBitmap.display(room_picture, APIconfig.IMG_BASEURL + first_img);
//                        }
//
//                        //状态正文
//                        room_content.setText(SpecialTextUtils.getWeiboContent(getActivity(), room_content, moment.getContent()));
//                    }
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                }
//            });
//
//        }
//
////        ImageView imageView1=new ImageView(getActivity());
////        imageView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
////        kjBitmap.display(imageView1, "http://a.hiphotos.baidu.com/zhidao/pic/item/fcfaaf51f3deb48f5c145614f01f3a292cf578ff.jpg");
//
//    }


//    //执行解析返回数据的任务
//    private void executeParserTask(byte[] data) {
//        cancelParserTask();
//        mParserTask = new ParserTask(data);
//        mParserTask.execute();
//    }
//
//    private void cancelReadCacheTask() {
//        if (mCacheTask != null) {
//            mCacheTask.cancel(true);
//            mCacheTask = null;
//        }
//    }
//
//
//    protected DGUserDetailResponse readList(Serializable seri) {
//        return null;
//    }
//
//    private class CacheTask extends AsyncTask<String, Void, DGUserDetailResponse> {
//        private final WeakReference<Context> mContext;
//
//        private CacheTask(Context context) {
//            mContext = new WeakReference<Context>(context);
//        }
//
//        @Override
//        protected DGUserDetailResponse doInBackground(String... params) {
//            Serializable seri = CacheManager.readObject(mContext.get(),
//                    params[0]);
//            if (seri == null) {
//                return null;
//            } else {
//                return readList(seri);
//            }
//        }
//
//        @Override
//        protected void onPostExecute(DGUserDetailResponse dgUserDetailResponse) {
//            super.onPostExecute(dgUserDetailResponse);
//            if (dgUserDetailResponse != null) {
//                executeDataSuccess(dgUserDetailResponse);
//            } else {
//                //TODO
////                executeOnLoadDataError(null);
//            }
//        }
//    }
//
//    private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
//        private final WeakReference<Context> mContext;
//        private final Serializable seri;
//        private final String key;
//
//        private SaveCacheTask(Context context, Serializable seri, String key) {
//            mContext = new WeakReference<Context>(context);
//            this.seri = seri;
//            this.key = key;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            CacheManager.saveObject(mContext.get(), seri, key);
//            return null;
//        }
//    }
//
//    protected String getCacheKeyPrefix() {
//        return CACHE_KEY_PREFIX;
//    }
//
//    private String getCacheKey() {
//        return new StringBuilder(getCacheKeyPrefix()).toString();
//    }
//
//    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
//
//        @Override
//        public void onSuccess(int statusCode, Header[] headers,
//                              byte[] responseBytes) {
//            if (isAdded()) {
//                executeParserTask(responseBytes);
//            }
//        }
//
//        @Override
//        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//                              Throwable arg3) {
//            if (isAdded()) {
//                readCacheData(getCacheKey());
//            }
//        }
//    };
//
//    private void readCacheData(String cacheKey) {
//        cancelReadCacheTask();
//        mCacheTask = new CacheTask(getActivity());
//        mCacheTask.execute(cacheKey);
//    }
//
//    class ParserTask extends AsyncTask<Void, Void, String> {
//
//        private final byte[] reponseData;
//        private boolean parserError;
//        private DGUserDetailResponse mResult;
//
//        public ParserTask(byte[] data) {
//            this.reponseData = data;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            try {
//                mResult = XmlUtils.toBean(DGUserDetailResponse.class,
//                        reponseData);
//
//                new SaveCacheTask(getActivity(), mResult, getCacheKey()).execute();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                parserError = true;
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (parserError) {
//                readCacheData(getCacheKey());
//            } else {
//                executeDataSuccess(mResult);
//            }
//        }
//    }
//
//
//    private void cancelParserTask() {
//        if (mParserTask != null) {
//            mParserTask.cancel(true);
//            mParserTask = null;
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        cancelReadCacheTask();
//        cancelParserTask();
//        super.onDestroy();
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ll_card_new_moment:{

                try{
                    Toast.makeText(getActivity(), "跳转到详细状态页面", Toast.LENGTH_SHORT).show();
                    //跳转到我的消息页面
//                    Intent intent = new Intent(getActivity(),MyMessageActivity.class);
//                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                        startActivity(intent);
//                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            }


        }
    }


}
