package com.dg.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.R;
import com.dg.app.ui.DogInfoActivity;
import com.dg.app.ui.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class DogIntroFragment extends android.support.v4.app.Fragment implements View.OnClickListener
{

    private Context context;

    private LinearLayout layout_dogintro_title;
//    private ImageView iv_dogintro_titlebar_back;
    private TextView tv_dogintro_titlebar_jump;
    private Button button_havenodog;
    private Button button_havedog;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_tweet, container, false);
        View root = View.inflate(getActivity(),
                R.layout.fragment_login_dogintro, null);

        initView(root);
        return root;
    }

    private void initView(View root) {
        layout_dogintro_title = (LinearLayout) root.findViewById(R.id.layout_dogintro_title);
//        iv_dogintro_titlebar_back = (ImageView) layout_dogintro_title.findViewById(R.id.iv_dogintro_titlebar_back);
//        iv_dogintro_titlebar_back.setOnClickListener(this);

        tv_dogintro_titlebar_jump = (TextView) layout_dogintro_title.findViewById(R.id.tv_dogintro_titlebar_jump);
        tv_dogintro_titlebar_jump.setOnClickListener(this);

        button_havenodog = (Button) root.findViewById(R.id.button_havenodog);
        button_havenodog.setOnClickListener(this);

        button_havedog = (Button) root.findViewById(R.id.button_havedog);
        button_havedog.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        try{

        switch(v.getId()){
            case R.id.tv_dogintro_titlebar_jump:{
                Toast.makeText(getActivity(),"跳过设置界面",Toast.LENGTH_LONG).show();
                //1、直接进入狗搭主界面
                enterMainAty();
                return;
                }
            case R.id.button_havenodog:{
                //1、直接进入狗搭主界面
                enterMainAty();
                return ;
                }
            case R.id.button_havedog:{
                Toast.makeText(getActivity(),"狗狗信息页面",Toast.LENGTH_LONG).show();
                enterDGaty();
                return ;
                }
        }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void enterMainAty() {
        //1、直接进入狗搭主界面
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public void enterDGaty(){
        //1、直接进入狗狗信息修改主界面
        Intent intent = new Intent(getActivity(), DogInfoActivity.class);
        startActivity(intent);
    }

}
