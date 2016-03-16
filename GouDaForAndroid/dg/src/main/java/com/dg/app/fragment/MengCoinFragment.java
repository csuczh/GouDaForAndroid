package com.dg.app.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.MengGrade;
import com.dg.app.bean.MengGradeResponse;
import com.dg.app.ui.LoginActivity;
import com.dg.app.ui.empty.EmptyLayout;
import com.dg.app.util.StringUtils;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.message.proguard.G;

import org.apache.http.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class MengCoinFragment extends android.support.v4.app.Fragment {

    private ParserTask mParserTask;

    private TextView tv_mengyao_coin;
    private TextView tv_mengyao_name;

    private ScrollView mScrollView;

    private EmptyLayout mErrorLayout;

    protected int mStoreEmptyState = -1;

    private TextView tv_mission1;
    private TextView tv_mnt;
    private TextView tv_mnt_isdone;

    private TextView tv_mission2;
    private TextView tv_mni;
    private TextView tv_mni_isdone;

    private TextView tv_mission3;
    private TextView tv_ml;
    private TextView tv_ml_isdone;

    private TextView tv_mission4;
    private TextView tv_mld;
    private TextView tv_mld_isdone;

    private TextView tv_mission5;
    private TextView tv_mr;
    private TextView tv_mr_isdone;

    private TextView tv_mission6;
    private TextView tv_mrd;
    private TextView tv_mrd_isdone;

    private TextView tv_mission7;
    private TextView tv_ms;
    private TextView tv_ms_isdone;

    private TextView tv_mission8;
    private TextView tv_mf;
    private TextView tv_mf_isdone;

    private TextView tv_mission9;
    private TextView tv_zl;
    private TextView tv_zl_isdone;

    private TextView tv_mission10;
    private TextView tv_zs;
    private TextView tv_zs_isdone;

    private TextView tv_mission11;
    private TextView tv_isd;
    private TextView tv_isd_isdone;

    private TextView tv_mission12;
    private TextView tv_ir;
    private TextView tv_ir_isdone;

    private TextView tv_mission13;
    private TextView tv_ia;
    private TextView tv_ia_isdone;

    private TextView tv_mission14;
    private TextView tv_ish;
    private TextView tv_ish_isdone;

    private TextView tv_mission15;
    private TextView tv_ol;
    private TextView tv_ol_isdone;

    private TextView tv_mission16;
    private TextView tv_oat;
    private TextView tv_oat_isdone;

    private TextView tv_mission17;
    private TextView tv_ov;
    private TextView tv_ov_isdone;

    private TextView tv_mission18;
    private TextView tv_os;
    private TextView tv_os_isdone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = View.inflate(getActivity(),
                R.layout.fragment_me_mengyao_coin, null);

        iniView(root);

        iniMissionsView(root);

        requestData();

        return root;
    }


    private void iniView(View root) {
        tv_mengyao_coin = (TextView) root.findViewById(R.id.tv_mengyao_coin);

        tv_mengyao_name = (TextView) root.findViewById(R.id.tv_mengyao_name);

        mScrollView = (ScrollView) root.findViewById(R.id.sv_missions);
        mScrollView.setVisibility(View.GONE);
        mErrorLayout = (EmptyLayout) root.findViewById(R.id.error_layout);
        mErrorLayout.setErrorType(com.dg.app.ui.empty.EmptyLayout.NETWORK_LOADING);

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mScrollView.setVisibility(View.GONE);
                mErrorLayout.setErrorType(com.dg.app.ui.empty.EmptyLayout.NETWORK_LOADING);
                requestData();
            }
        });

        if (mStoreEmptyState != -1) {
            mErrorLayout.setErrorType(mStoreEmptyState);
        }

    }

    private void iniMissionsView(View root) {
          tv_mission1 = (TextView) root.findViewById(R.id.tv_mission1);
          tv_mnt  = (TextView) root.findViewById(R.id.tv_mnt);
          tv_mnt_isdone = (TextView) root.findViewById(R.id.tv_mnt_isdone);

          tv_mission2 = (TextView) root.findViewById(R.id.tv_mission2);
          tv_mni = (TextView) root.findViewById(R.id.tv_mni);
          tv_mni_isdone = (TextView) root.findViewById(R.id.tv_mni_isdone);

          tv_mission3 = (TextView) root.findViewById(R.id.tv_mission3);
          tv_ml = (TextView) root.findViewById(R.id.tv_ml);
          tv_ml_isdone = (TextView) root.findViewById(R.id.tv_ml_isdone);

          tv_mission4 = (TextView) root.findViewById(R.id.tv_mission4);
          tv_mld = (TextView) root.findViewById(R.id.tv_mld);
          tv_mld_isdone = (TextView) root.findViewById(R.id.tv_mld_isdone);

          tv_mission5 = (TextView) root.findViewById(R.id.tv_mission5);
          tv_mr = (TextView) root.findViewById(R.id.tv_mr);
          tv_mr_isdone = (TextView) root.findViewById(R.id.tv_mr_isdone);

          tv_mission6 = (TextView) root.findViewById(R.id.tv_mission6);
          tv_mrd = (TextView) root.findViewById(R.id.tv_mrd);
          tv_mrd_isdone = (TextView) root.findViewById(R.id.tv_mrd_isdone);

          tv_mission7 = (TextView) root.findViewById(R.id.tv_mission7);
          tv_ms = (TextView) root.findViewById(R.id.tv_ms);
          tv_ms_isdone = (TextView) root.findViewById(R.id.tv_ms_isdone);

          tv_mission8 = (TextView) root.findViewById(R.id.tv_mission8);
          tv_mf = (TextView) root.findViewById(R.id.tv_mf);
          tv_mf_isdone = (TextView) root.findViewById(R.id.tv_mf_isdone);

          tv_mission9 = (TextView) root.findViewById(R.id.tv_mission9);
          tv_zl = (TextView) root.findViewById(R.id.tv_zl);
          tv_zl_isdone = (TextView) root.findViewById(R.id.tv_zl_isdone);

          tv_mission10 = (TextView) root.findViewById(R.id.tv_mission10);
          tv_zs = (TextView) root.findViewById(R.id.tv_zs);
          tv_zs_isdone = (TextView) root.findViewById(R.id.tv_zs_isdone);

          tv_mission11 = (TextView) root.findViewById(R.id.tv_mission11);
          tv_isd = (TextView) root.findViewById(R.id.tv_isd);
          tv_isd_isdone = (TextView) root.findViewById(R.id.tv_isd_isdone);

          tv_mission12 = (TextView) root.findViewById(R.id.tv_mission12);
          tv_ir = (TextView) root.findViewById(R.id.tv_ir);
          tv_ir_isdone = (TextView) root.findViewById(R.id.tv_ir_isdone);

          tv_mission13 = (TextView) root.findViewById(R.id.tv_mission13);
          tv_ia = (TextView) root.findViewById(R.id.tv_ia);
          tv_ia_isdone = (TextView) root.findViewById(R.id.tv_ia_isdone);

          tv_mission14 = (TextView) root.findViewById(R.id.tv_mission14);
          tv_ish = (TextView) root.findViewById(R.id.tv_ish);
          tv_ish_isdone = (TextView) root.findViewById(R.id.tv_ish_isdone);

          tv_mission15 = (TextView) root.findViewById(R.id.tv_mission15);
          tv_ol = (TextView) root.findViewById(R.id.tv_ol);
          tv_ol_isdone = (TextView) root.findViewById(R.id.tv_ol_isdone);

          tv_mission16 = (TextView) root.findViewById(R.id.tv_mission16);
          tv_oat = (TextView) root.findViewById(R.id.tv_oat);
          tv_oat_isdone = (TextView) root.findViewById(R.id.tv_oat_isdone);

          tv_mission17 = (TextView) root.findViewById(R.id.tv_mission17);
          tv_ov = (TextView) root.findViewById(R.id.tv_ov);
          tv_ov_isdone = (TextView) root.findViewById(R.id.tv_ov_isdone);

          tv_mission18 = (TextView) root.findViewById(R.id.tv_mission18);
          tv_os = (TextView) root.findViewById(R.id.tv_os);
          tv_os_isdone = (TextView) root.findViewById(R.id.tv_os_isdone);
    }

    /***
     * 获取列表数据
     *
     *
     * @author 火蚁 2015-2-9 下午3:16:12
     *
     * @return void
     * @param refresh
     */
    protected void requestData() {
        try {
            // 取新的数据
            int user_id = AppContext.getInstance().getLoginUid();
            if(user_id!=0) {
                DGApi.getTodayMission(user_id,mHandler);
            }else{
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBytes) {
            AppContext.showToast(responseBytes.toString());

            if (isAdded()) {
                executeParserTask(responseBytes);
            }

        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            mErrorLayout.setErrorType(com.dg.app.ui.empty.EmptyLayout.NETWORK_ERROR);
        }
    };

    private void executeParserTask(byte[] data) {
        cancelParserTask();
        mParserTask = new ParserTask(data);
        mParserTask.execute();
    }

    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }

    public void setData(MengGrade data) {
        setMissionItemData(tv_mission1,tv_mnt,tv_mnt_isdone,data.getMnt());

        setMissionItemData(tv_mission2,tv_mni,tv_mni_isdone,data.getMni());

        setMissionItemData(tv_mission3,tv_ml,tv_ml_isdone,data.getMl());

        setMissionItemData(tv_mission4,tv_mld,tv_mld_isdone,data.getMld());

        setMissionItemData(tv_mission5,tv_mr,tv_mr_isdone,data.getMr());

        setMissionItemData(tv_mission6,tv_mrd,tv_mrd_isdone,data.getMrd());

        setMissionItemData(tv_mission7,tv_ms,tv_ms_isdone,data.getMs());

        setMissionItemData(tv_mission8,tv_mf,tv_mf_isdone,data.getMf());

        setMissionItemData(tv_mission9,tv_zl,tv_zl_isdone,data.getZl());

        setMissionItemData(tv_mission10,tv_zs,tv_zs_isdone,data.getZs());

        setMissionItemData(tv_mission11,tv_isd,tv_isd_isdone,data.getIsd());

        setMissionItemData(tv_mission12,tv_ir,tv_ir_isdone,data.getIr());

        setMissionItemData(tv_mission13,tv_ia,tv_ia_isdone,data.getIa());

        setMissionItemData(tv_mission14,tv_ish,tv_ish_isdone,data.getIsh());

        setMissionItemData(tv_mission15,tv_ol,tv_ol_isdone,data.getOl());

        setMissionItemData(tv_mission16,tv_oat,tv_oat_isdone,data.getOat());

        setMissionItemData(tv_mission17,tv_ov,tv_ov_isdone,data.getOv());

        setMissionItemData(tv_mission18,tv_os,tv_os_isdone,data.getOs());

    }

    private void setMissionItemData(TextView tv_mission, TextView tv_grade, TextView tv_mission_isdone, String grade) {
        if(!StringUtils.isEmpty(grade)){
            int score = Integer.parseInt(grade);
            if(score==0){
                tv_mission.setTextColor(getResources().getColor(R.color.black));
                tv_mission_isdone.setText("未完成");
                tv_mission_isdone.setTextColor(getResources().getColor(R.color.black));
                tv_grade.setText("+"+grade);
            }else{
                tv_mission.setTextColor(getResources().getColor(R.color.main_gray));
                tv_mission_isdone.setText("已完成");
                tv_mission_isdone.setTextColor(getResources().getColor(R.color.main_gray));
                tv_grade.setText("+"+grade);
            }
        }
    }

    class ParserTask extends AsyncTask<Void, Void, String> {

        private final byte[] reponseData;
        private boolean parserError;
        private MengGrade mengGrade;

        public ParserTask(byte[] data) {
            this.reponseData = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                MengGradeResponse data = XmlUtils.toBean(MengGradeResponse.class,reponseData);
                if (data.getCode() != 0) {
                    executeOnLoadDataError();
                }else{
                    mengGrade = data.getMengGrade();
                }
            } catch (Exception e) {
                e.printStackTrace();

                parserError = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (parserError) {
                System.out.println("parseError:" + parserError);
                executeOnLoadDataError();
            } else {

                executeOnLoadDataSuccess(mengGrade);
            }
        }
    }

    protected void executeOnLoadDataSuccess(MengGrade mengGrade) {
        try {
            mScrollView.setVisibility(View.VISIBLE);
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);

            setData(mengGrade);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void executeOnLoadDataError() {
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onDestroyView() {
        mStoreEmptyState = mErrorLayout.getErrorState();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        cancelParserTask();
        super.onDestroy();
    }

}
