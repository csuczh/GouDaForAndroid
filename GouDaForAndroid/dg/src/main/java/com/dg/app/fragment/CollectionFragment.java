package com.dg.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dg.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class CollectionFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    //顶部actionbar
    private LinearLayout layout_shoucang_title;
    private ImageView iv_collect_titlebar_back;

    private FragmentManager fm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = View.inflate(getActivity(),
                R.layout.fragment_me_shoucang, null);

        initTitlebBar(root);

        initView();

        return root;
    }

    private void initView() {

        try{

            CollectionsFragment collectionsFragment = new CollectionsFragment();
            fm = getFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.fl_collections_list, collectionsFragment)
                    .commit();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //初始化标题栏
    private void initTitlebBar(View root) {
        layout_shoucang_title = (LinearLayout) root.findViewById(R.id.layout_shoucang_title);
        iv_collect_titlebar_back = (ImageView) layout_shoucang_title.findViewById(R.id.iv_collect_titlebar_back);

        iv_collect_titlebar_back.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_collect_titlebar_back:{
                    Toast.makeText(getActivity(), "跳转到我的消息页面", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                break;
            }

            }

        }
    }
