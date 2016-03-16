package com.dg.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.AdviceResponse;
import com.dg.app.ui.toast.DGToast;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.message.PushAgent;

import org.apache.http.Header;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

public class GiveAdviceActivity extends Activity implements View.OnClickListener{

    private LinearLayout titlebar_give_advice;
    private ImageView iv_give_advice_back;
    private TextView tv_advice_complete;

    private EditText et_advice;
    private EditText et_contact;

    private KJHttp kjh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_advice);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);
        titlebar_give_advice = (LinearLayout) findViewById(R.id.titlebar_give_advice);

        iv_give_advice_back = (ImageView) titlebar_give_advice.findViewById(R.id.iv_give_advice_back);
        iv_give_advice_back.setOnClickListener(this);
        tv_advice_complete = (TextView) titlebar_give_advice.findViewById(R.id.tv_advice_complete);
        tv_advice_complete.setOnClickListener(this);

        et_advice = (EditText) findViewById(R.id.et_advice);
        et_contact = (EditText) findViewById(R.id.et_contact);

        initHttp();
    }

    private void initHttp() {
        //配置http连接属性
        HttpConfig config = new HttpConfig();
        config.cacheTime = 0;
        config.useDelayCache = false;
        kjh = new KJHttp(config);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_give_advice_back:
                this.finish();
                break;
            case R.id.tv_advice_complete:
                doAdvice();
                break;
        }
    }

    /**
     * 输入合法性检测
     */
    private boolean inputCheck() {
        if (StringUtils.isEmpty(et_advice.getText().toString())) {
            ViewInject.toast("意见反馈不能为空！");
            return false;
        }
        return true;
    }

    private void doAdvice() {
        if (!inputCheck()) {
            return;
        }
        int user_id = AppContext.getInstance().getLoginUid();
        int city_id = AppContext.getInstance().getLoginUser().getCityid();
        String content = et_advice.getText().toString();
        String contact = et_contact.getText().toString();
        DGApi.giveAdvice(user_id, content, city_id, contact, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                AdviceResponse adviceResponse = XmlUtils.toBean(AdviceResponse.class,responseBody);
                if(adviceResponse.getCode()==0){
                    DGToast.makeText(GiveAdviceActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
