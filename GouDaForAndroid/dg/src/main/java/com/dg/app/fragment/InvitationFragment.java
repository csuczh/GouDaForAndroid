package com.dg.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dg.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class InvitationFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    public static final String INVITATION_TYPE[] = {"walk","date","foster"};

    //顶部actionbar
    private LinearLayout layout_yaoqing_title;
    private ImageView iv_invitation_back;
    private Button button_liugou;
    private Button button_xiangqin;
    private Button button_jiyang;

    private android.support.v4.app.FragmentManager fm;

    private LiuDogInvitationFragment liuDogInvitationFragment;

    private DateDogInvitationFragment dateDogInvitationFragment;

    private FosterDogInvitationFragment fosterDogInvitationFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = View.inflate(getActivity(),
                R.layout.fragment_me_yaoqing, null);

        initTitlebBar(root);
        initView();
        return root;
    }

    private void initView() {
        liuDogInvitationFragment = new LiuDogInvitationFragment();
        dateDogInvitationFragment = new DateDogInvitationFragment();
        fosterDogInvitationFragment = new FosterDogInvitationFragment();


        fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fl_invitations_list, liuDogInvitationFragment)
                .commit();

    }

    //初始化标题栏
    private void initTitlebBar(View root) {
        layout_yaoqing_title = (LinearLayout) root.findViewById(R.id.layout_yaoqing_title);
        iv_invitation_back = (ImageView) layout_yaoqing_title.findViewById(R.id.iv_invitation_back);
        button_liugou = (Button) layout_yaoqing_title.findViewById(R.id.button_liugou);
        button_xiangqin = (Button) layout_yaoqing_title.findViewById(R.id.button_xiangqin);
        button_jiyang = (Button) layout_yaoqing_title.findViewById(R.id.button_jiyang);

        iv_invitation_back.setOnClickListener(this);
        button_liugou.setOnClickListener(this);
        button_xiangqin.setOnClickListener(this);
        button_jiyang.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_invitation_back:{
                getActivity().finish();
                break;
            }

            case R.id.button_liugou:{
                //遛狗
                showLiuGou();
                break;
            }

            case R.id.button_xiangqin:{
                //相亲
                showXiangQin();
                break;
            }

            case R.id.button_jiyang:{
                //寄养
                showJiYang();
                break;
            }

        }
    }

    private void showLiuGou() {
        //更新顶部actionbar
        updateActionBar(1);
        if(liuDogInvitationFragment==null){
            liuDogInvitationFragment = new LiuDogInvitationFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fl_invitations_list, liuDogInvitationFragment)
                .commit();
    }

    private void showXiangQin() {
        //更新顶部actionbar
        updateActionBar(2);
        if(dateDogInvitationFragment==null){
            dateDogInvitationFragment = new DateDogInvitationFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fl_invitations_list, dateDogInvitationFragment)
                .commit();
    }

    private void showJiYang() {
        //更新顶部actionbar
        updateActionBar(3);
        if(fosterDogInvitationFragment==null){
            fosterDogInvitationFragment = new FosterDogInvitationFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fl_invitations_list, fosterDogInvitationFragment)
                .commit();
    }

    public void updateActionBar(int index){
        button_liugou.setBackgroundResource(R.drawable.bg_actionbar_corner);
        button_xiangqin.setBackgroundResource(R.drawable.bg_actionbar_corner);
        button_jiyang.setBackgroundResource(R.drawable.bg_actionbar_corner);
        button_liugou.setTextColor(getResources().getColor(R.color.white));
        button_xiangqin.setTextColor(getResources().getColor(R.color.white));
        button_jiyang.setTextColor(getResources().getColor(R.color.white));
        if(index==1){
            button_liugou.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            button_liugou.setTextColor(getResources().getColor(R.color.actionbar_normal));
        }
        if(index==2){
            button_xiangqin.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            button_xiangqin.setTextColor(getResources().getColor(R.color.actionbar_normal));
        }
        if(index==3){
            button_jiyang.setBackgroundResource(R.drawable.bg_actionbar_left_clicked);
            button_jiyang.setTextColor(getResources().getColor(R.color.actionbar_normal));
        }
    }

}
