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

public class MengIntroActivity extends Activity implements View.OnClickListener{

    private LinearLayout layout_meng_intro_title;
    private ImageView iv_meng_intro_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meng_intro);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);

        layout_meng_intro_title = (LinearLayout) findViewById(R.id.layout_meng_intro_title);

        iv_meng_intro_back = (ImageView) layout_meng_intro_title.findViewById(R.id.iv_meng_intro_back);
        iv_meng_intro_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_meng_intro_back:
                this.finish();
                break;
        }
    }


}
