package com.dg.app.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dg.app.R;
import com.dg.app.adapter.OwnersAdapter;
import com.dg.app.adapter.QuestionAdapter;
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.base.BaseListFragment;
import com.dg.app.base.DogKnowBaseListFragment;
import com.dg.app.bean.Owners;
import com.dg.app.bean.OwnersList;
import com.dg.app.bean.Question;
import com.dg.app.bean.QuestionList;
import com.dg.app.ui.DetailDKActivity;
import com.dg.app.util.XmlUtils;
import com.squareup.picasso.Picasso;

import org.kymjs.kjframe.KJBitmap;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/10/21.
 */
public class DetailDogKnowFragment extends DogKnowBaseListFragment
        implements ViewPager.OnPageChangeListener{
    protected static final String TAG = FindNearbyFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "questionslist_";
    //头部滚动的图片
    private ViewPager viewPager;
    //点点的图片
    private ImageView[] tips;
    //imageview的
    private ImageView[] mImageViews;
    private int[] topIDs;

    ViewGroup group;

    ArrayList<Question> questions=null;
    Activity activity;


    @Override
    protected QuestionAdapter getListAdapter() {
        return new QuestionAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected QuestionList parseList(InputStream is) throws Exception {
        QuestionList list = null;
        try {
            list = XmlUtils.toBean(QuestionList.class, is);
        } catch (NullPointerException e) {
            list = new QuestionList();
        }
        return list;
    }

    @Override
    protected QuestionList readList(Serializable seri) {
        return ((QuestionList) seri);
    }
//该方法是请求网络的，当注册登录完成后，通过Appcontext获得相应的urse_id
    @Override
    protected void sendRequestData() {
        DGApi.getRecommend(device, 1, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Question question =(Question) parent.getAdapter().getItem(position);
        Intent intent = new Intent(activity, DetailDKActivity.class);
        Bundle bundle = new Bundle();
            bundle.putInt("question_id", question.getQuestion_id());
            bundle.putString("title", question.getTitle());
            bundle.putInt("likes_num", question.getLikes_num());
            bundle.putString("publisher_logo", question.getPublisher_logo());
            bundle.putString("publisher_name", question.getPublisher_name());
            intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void executeOnLoadDataSuccess(List<Question> data) {
        try {
            super.executeOnLoadDataSuccess(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //根据获得数据填充UI
    public  void fillUI(ArrayList<Question> topQuestions)
    {
        activity=this.getActivity();
        questions=topQuestions;
        group.removeAllViews();

        tips = new ImageView[topQuestions.size()];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(this.getActivity());
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(15,15);
            if(i!=(tips.length-1)) {
                lp.setMargins(0,0,20,0);
            }
            imageView.setLayoutParams(lp);
            tips[i] = imageView;
            if (i == 0) {
//                tips[i].setBackgroundResource(R.mipmap.page_indicator_focused);
              tips[i].setBackgroundResource(R.drawable.indicator_focused);

            } else {
//                tips[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
                tips[i].setBackgroundResource(R.drawable.indicator_unfocused);

            }

            group.addView(imageView);
        }

        //将图片装载到数组中
        mImageViews = new ImageView[topQuestions.size()];
        topIDs=new int[topQuestions.size()];
        for (int i = 0; i < mImageViews.length; i++) {
            ImageView imageView = new ImageView(this.getActivity());
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageViews[i] = imageView;
            topIDs[i]=topQuestions.get(i).getQuestion_id();
            String urlhead=APIconfig.IMG_BASEURL.substring(0, APIconfig.IMG_BASEURL.length()-1);
            if ( topQuestions.get(i).getTitle_image() != null&&topQuestions.get(i).getTitle_image()!="") {
                Picasso.with(activity).load(urlhead + topQuestions.get(i).getTitle_image()).into(mImageViews[i]);
            } else {
                Picasso.with(activity).load(R.mipmap.default_avatar).into(mImageViews[i]);
            }

        }



        //设置Adapter
        viewPager.setAdapter(new MyAdapter());
        //设置监听，主要是设置点点的背景
        viewPager.setOnPageChangeListener(this);
        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        viewPager.setCurrentItem((mImageViews.length) );
    }
    //初始化头部视图
    public  View initHeaderView()
    {
         View header= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dog_know,null);
         group = (ViewGroup) header.findViewById(R.id.viewGroup);
         viewPager = (ViewPager) header.findViewById(R.id.dogknow_viewpager);
         return  header;
    }
    /**
     *
     * @author xiaanming
     *
     */
    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
//      ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, final int position) {
            try {
                        mImageViews[position % mImageViews.length].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = position % mImageViews.length;
                        Intent intent = new Intent(activity, DetailDKActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("question_id", questions.get(p).getQuestion_id());
                        bundle.putString("title", questions.get(p).getTitle());
                        bundle.putInt("likes_num", questions.get(p).getLikes_num());
                        bundle.putString("publisher_logo", questions.get(p).getPublisher_logo());
                        bundle.putString("publisher_name", questions.get(p).getPublisher_name());
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
//                        Toast.makeText(getActivity(), "点击id：" + topIDs[p] + position % mImageViews.length, Toast.LENGTH_LONG).show();
                    }
                });
                         int k=position%mImageViews.length;

                          ImageView imageView=mImageViews[k];
                         ((ViewPager) container).removeView(imageView);
                          ((ViewPager) container).addView(imageView,0);

            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return mImageViews[position % mImageViews.length];
        }
    }

    @Override
    protected long getAutoRefreshTime() {

        return 2 * 60 * 60;

    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }


    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0 % mImageViews.length);
    }

    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.mipmap.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}