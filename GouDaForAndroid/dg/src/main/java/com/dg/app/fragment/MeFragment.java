package com.dg.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.DGUser;
import com.dg.app.bean.DGUserDetailResponse;
import com.dg.app.service.UmengShareService;
import com.dg.app.ui.CollectionActivity;
import com.dg.app.ui.DGUserInfoActivity;
import com.dg.app.ui.InvitationActivity;
import com.dg.app.ui.MengRankActivity;
import com.dg.app.ui.SettingActivity;
import com.dg.app.util.DGImageUtils;
import com.dg.app.util.Parser;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.controller.UMSocialService;

import org.apache.http.Header;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.RoundImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class MeFragment extends android.support.v4.app.Fragment implements View.OnClickListener
{

    public static final String ME_TAG = "ME_PAGE";
    public static final int[] ME_PAGES = {0,1,2,3,4,5};

    private DGUser user;

    private KJBitmap kjBitmap;

    //我的消息
    private LinearLayout mine_info_all_layout;
    private LinearLayout mine_info_layout;

    private RoundImageView roundImageView;

    private TextView mine_info_username_tv;
    private TextView mine_info_nickname_tv;
    private TextView mine_info_age_tv;
    private TextView mine_info_gender_tv;
    private TextView mine_info_job_tv;

    //荣耀榜
    private LinearLayout ll_setting_rongyaobang;
    private LinearLayout ll_setting_invitation;
    private LinearLayout ll_setting_collection;
    private LinearLayout ll_setting_sharegouda;

    private LinearLayout ll_setting_set;


//    private UMSocialService mController = null;
    private boolean is_share_prepared = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_tweet, container, false);
        View root = View.inflate(getActivity(),
                R.layout.fragment_me, null);
        initData();
        initWidget(root);

        initShare(getActivity());

        return root;
    }

//    初始化分享组件
    private void initShare(Activity activity) {
//        mController = UmengShareService.initUmengShare();
//        if(mController!=null){
//            UmengShareService.configPlatforms(activity);
//            UmengShareService.setShareContent(activity);
//            mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA,
//                    SHARE_MEDIA.EMAIL,
//                    SHARE_MEDIA.SMS);
//            is_share_prepared = true;
//        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    //初始化数据
    protected void initData() {
        kjBitmap = new KJBitmap();

        user = AppContext.getInstance().getLoginUser();
        if(user!=null&&user.getUserid()!=0){
            //加载个人信息
            DGApi.userDetail(user.getUserid(), user.getUserid() ,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    DGUserDetailResponse dgUserDetailResponse = XmlUtils.toBean(DGUserDetailResponse.class, responseBody);

                    if (dgUserDetailResponse.getCode() == 0) {
                        DGUserDetailResponse.UserDetail userDetail = dgUserDetailResponse.getUserDetail();

                        String logo = DGImageUtils.toSmallImageURL(userDetail.getLogo());
                        kjBitmap.display(roundImageView, APIconfig.IMG_BASEURL + logo,roundImageView.getWidth(),roundImageView.getHeight(),R.mipmap.default_head_image);

                        mine_info_username_tv.setText(userDetail.getNickname());

                        mine_info_age_tv.setText(userDetail.getAge()+"岁");

                        if(userDetail.getSex()==0){
                            mine_info_gender_tv.setText("男");
                        }else if(userDetail.getSex()==1){
                            mine_info_gender_tv.setText("女");
                        }

                        mine_info_job_tv.setText(userDetail.getCareer());

                        mine_info_nickname_tv.setText(userDetail.getGradeTag());

                    }else{
                        Toast.makeText(getActivity(),dgUserDetailResponse.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }


    }

    //    初始化视图组件
    protected void initWidget(View parentView) {

        mine_info_all_layout = (LinearLayout) parentView.findViewById(R.id.mine_info_all_layout);

        mine_info_layout = (LinearLayout) parentView.findViewById(R.id.mine_info_layout);

        roundImageView = (RoundImageView) parentView.findViewById(R.id.mine_info_img_iv);

        mine_info_username_tv = (TextView) parentView.findViewById(R.id.mine_info_username_tv);
        mine_info_nickname_tv = (TextView) parentView.findViewById(R.id.mine_info_nickname_tv);
        mine_info_age_tv = (TextView) parentView.findViewById(R.id.mine_info_age_tv);
        mine_info_gender_tv = (TextView) parentView.findViewById(R.id.mine_info_gender_tv);
        mine_info_job_tv = (TextView) parentView.findViewById(R.id.mine_info_job_tv);

        ll_setting_rongyaobang = (LinearLayout) parentView.findViewById(R.id.ll_setting_rongyaobang);
        ll_setting_invitation = (LinearLayout) parentView.findViewById(R.id.ll_setting_invitation);
        ll_setting_collection = (LinearLayout) parentView.findViewById(R.id.ll_setting_collection);
        ll_setting_sharegouda = (LinearLayout) parentView.findViewById(R.id.ll_setting_sharegouda);

        ll_setting_set = (LinearLayout) parentView.findViewById(R.id.ll_setting_set);

        mine_info_all_layout.setOnClickListener(this);
        ll_setting_rongyaobang.setOnClickListener(this);
        ll_setting_invitation.setOnClickListener(this);
        ll_setting_collection.setOnClickListener(this);
        ll_setting_sharegouda.setOnClickListener(this);
        ll_setting_set.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        try{

            Intent intent;
        switch(v.getId()){
            case R.id.mine_info_all_layout:{
                intent = new Intent(getActivity(), DGUserInfoActivity.class);
                startActivity(intent);
                return ;
                }
            case R.id.ll_setting_rongyaobang:{
                intent = new Intent(getActivity(), MengRankActivity.class);
                startActivity(intent);
                return;
                }
            case R.id.ll_setting_invitation:{
                Toast.makeText(getActivity(),"邀请界面",Toast.LENGTH_LONG).show();
                intent = new Intent(getActivity(), InvitationActivity.class);
                startActivity(intent);
                return ;
                }
            case R.id.ll_setting_collection:{
                Toast.makeText(getActivity(),"收藏界面",Toast.LENGTH_LONG).show();
                intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);
                return ;
                }
            case R.id.ll_setting_sharegouda:{
                Toast.makeText(getActivity(),"分享界面",Toast.LENGTH_LONG).show();
//                if(mController!=null&&is_share_prepared){
//                    mController.openShare(getActivity(), false);
//                }
                return;
            }
            case R.id.ll_setting_set:{
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                return;
            }
        }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }


}
