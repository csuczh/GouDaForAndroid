package com.dg.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.api.APIconfig;
import com.umeng.message.PushAgent;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

public class UpdatepwdActivity extends Activity implements View.OnClickListener{

    private LinearLayout actionbar_setting_updatepwd;
    private ImageView iv_setting_updatepwd_back;
    private TextView tv_setting_updatepwd_update;

    private EditText et_currentpwd;
    private EditText et_updatepwd;
    private EditText et_copypwd;

    private KJHttp kjh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepwd);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

        actionbar_setting_updatepwd = (LinearLayout) findViewById(R.id.actionbar_setting_updatepwd);

        iv_setting_updatepwd_back = (ImageView) actionbar_setting_updatepwd.findViewById(R.id.iv_setting_updatepwd_back);
        iv_setting_updatepwd_back.setOnClickListener(this);
        tv_setting_updatepwd_update = (TextView) actionbar_setting_updatepwd.findViewById(R.id.tv_setting_updatepwd_update);
        tv_setting_updatepwd_update.setOnClickListener(this);

        et_currentpwd = (EditText) findViewById(R.id.et_currentpwd);
        et_updatepwd = (EditText) findViewById(R.id.et_updatepwd);
        et_copypwd = (EditText) findViewById(R.id.et_copypwd);

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
            case R.id.iv_setting_updatepwd_back:
                this.finish();
                break;
            case R.id.tv_setting_updatepwd_update:
                doUpdate();
                break;
        }
    }

    /**
     * 输入合法性检测
     */
    private boolean inputCheck() {
        if (StringUtils.isEmpty(et_currentpwd.getText().toString())) {
            ViewInject.toast(getString(R.string.account_not_empty));
            return false;
        }
        if (StringUtils.isEmpty(et_updatepwd.getText().toString())) {
            ViewInject.toast(getString(R.string.account_not_empty));
            return false;
        }
        if (StringUtils.isEmpty(et_copypwd.getText().toString())) {
            ViewInject.toast(getString(R.string.account_not_empty));
            return false;
        }
        return true;
    }

    private void doUpdate() {
        if (!inputCheck()) {
            return;
        }
        HttpConfig config = new HttpConfig();
        config.cacheTime = 0;
        KJHttp kjh = new KJHttp(config);
        HttpParams params = new HttpParams();
        //TODO
        params.put("device", "android");
        params.put("time", System.currentTimeMillis() / 1000 +"");
        params.put("user_id", "10");
        params.put("new_pw", "65432100");
        params.put("old_pw", "123456");

        kjh.post(APIconfig.API_BASEURL+APIconfig.USER_MODIFY_PWD, params,
                new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        if (t != null) {

                            Toast.makeText(UpdatepwdActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
                            finish();
//                            try {
//                                if (1 == data.getResult().getErrorCode()) {
//                                    User user = data.getUser();
//                                    user.setCookie(cookie);
//                                    user.setAccount(mEtUid.getText().toString());
//                                    user.setPwd(mEtPwd.getText().toString());
//                                    UIHelper.saveUser(aty, user);
//                                    finish();
//                                } else {
//                                    mEtPwd.setText(null);
//                                    mEtUid.setText(null);
//                                }
//                                ViewInject.toast(data.getResult()
//                                        .getErrorMessage());
//                                // 太多判断了，写的蛋疼，还不如一个NullPointerException
//                            } catch (NullPointerException e) {
//                                ViewInject.toast("登陆失败");
////                                mEtPwd.setText(null);
////                                mEtUid.setText(null);
//                            }
                        }

                    }

                });
    }

}
