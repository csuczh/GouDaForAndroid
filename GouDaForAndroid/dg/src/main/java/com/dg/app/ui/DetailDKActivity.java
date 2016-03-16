package com.dg.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.api.ApiHttpClient;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.QuestionList;
import com.dg.app.bean.ResponseResult;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.message.PushAgent;

import org.apache.http.Header;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.RoundImageView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DetailDKActivity extends Activity {
    private String publisher_logo;
    private String publisher_name;
    private int likes_num;
    private String title;
    private int question_id;

    private RoundImageView publisher_logo_image;
    private TextView title_textview;
    private TextView publisher_name_textview;
    private TextView likes_num_textview;
    private ImageView like;
    private ImageView hide;
    private WebView webView;

    Activity activity;
    private SharePopupWindow sharePopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dk);
        PushAgent.getInstance(this).onAppStart();

        Bundle bundle=getIntent().getExtras();
        question_id=bundle.getInt("question_id");
        title=bundle.getString("title", "");
        likes_num=bundle.getInt("likes_num");
        publisher_logo=bundle.getString("publisher_logo", "");
        publisher_name=bundle.getString("publisher_name");
        activity=this;
        initView();
        initData();
    }
    public void initView()
    {
        publisher_logo_image=(RoundImageView)this.findViewById(R.id.dk_image);
        title_textview=(TextView)this.findViewById(R.id.dk_title);
        publisher_name_textview=(TextView)this.findViewById(R.id.dk_name);
        likes_num_textview=(TextView)this.findViewById(R.id.dk_like);
        like=(ImageView)this.findViewById(R.id.send_like);
        hide=(ImageView)this.findViewById(R.id.send_favorite);
        webView=(WebView)this.findViewById(R.id.dk_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
    }
    public void initData()
    {
        KJBitmap kjb = new KJBitmap();
        String logo= ApiHttpClient.IMAGE_URL+publisher_logo;
        kjb.display(publisher_logo_image, logo);
        title_textview.setText(title);
        publisher_name_textview.setText(publisher_name);
        likes_num_textview.setText(likes_num + "");
        final String obsolutURl= ApiHttpClient.IMAGE_URL+"/Mobile/Question/detail/id/"+question_id;
        System.out.println("obsolutURL-------->" + obsolutURl);
       webView.loadUrl(obsolutURl);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//                view.loadUrl(obsolutURl);
                return true;
            }
        });
        final AsyncHttpResponseHandler likehandler=new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    ResponseResult responseResult=parseList(new ByteArrayInputStream(responseBody));
                    switch (responseResult.getCode())
                    {
                        case 604:
                            Toast.makeText(activity,"已经点赞过！",Toast.LENGTH_LONG).show();
                            break;
                        case  605:
                            Toast.makeText(activity,"点赞失败！",Toast.LENGTH_LONG).show();
                            break;
                        default:Toast.makeText(activity,"点赞成功",Toast.LENGTH_LONG).show();
                            likes_num_textview.setText((likes_num + 1) + "");
                            like.setBackgroundResource(R.mipmap.ic_like);
                            like.setClickable(false);
                            break;

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };

        final AsyncHttpResponseHandler collecthandler=new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    ResponseResult responseResult=parseList(new ByteArrayInputStream(responseBody));
                    switch (responseResult.getCode())
                    {
                        case 600:
                            Toast.makeText(activity,"没有此文章！",Toast.LENGTH_LONG).show();
                            break;
                        case 601:
                            Toast.makeText(activity,"已经收藏过！",Toast.LENGTH_LONG).show();
                            break;
                        case 602:
                            Toast.makeText(activity,"已经达到收藏上限！",Toast.LENGTH_LONG).show();
                            break;
                        case 603:
                            Toast.makeText(activity,"收藏失败！",Toast.LENGTH_LONG).show();
                            break;
                        default: Toast.makeText(activity,"收藏成功！",Toast.LENGTH_LONG).show();
                            hide.setBackgroundResource(R.mipmap.ic_collect);
                            hide.setClickable(false);
                            break;


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };
        String user_id=AppContext.getInstance().getProperty("dgUser.userid");
        final int int_id=Integer.parseInt(user_id);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DGApi.likeArticle("android", int_id, question_id, likehandler);
            }
        });
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DGApi.collectArticle("android", int_id, question_id, collecthandler);
            }
        });
    }

    public void back(View view)
    {
        finish();
    }

    protected ResponseResult parseList(InputStream is) throws Exception {
        ResponseResult list = null;
        try {
            list = XmlUtils.toBean(ResponseResult.class, is);
        } catch (NullPointerException e) {
            list = new ResponseResult();
        }
        return list;
    }

    //弹出窗口
    public void popupwindow(View view)
    {

        //创建弹出菜单，并添加监听器
        sharePopupWindow = new SharePopupWindow(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.wx_quan:

                       Toast.makeText(activity,"wx_quan",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.wx_friend:
                        Toast.makeText(activity,"wx_friend",Toast.LENGTH_LONG).show();

                        break;
                    case R.id.qq_quan:
                        Toast.makeText(activity,"qq_quan",Toast.LENGTH_LONG).show();

                        break;
                    case R.id.qq_friend:
                        Toast.makeText(activity,"qq_friend",Toast.LENGTH_LONG).show();

                        break;
                    case R.id.xl_weibo:
                        Toast.makeText(activity,"xl_weibo",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.share_copy:
                        Toast.makeText(activity,"share_copy",Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        });
        //显示窗口
        //设置layout在PopupWindow中显示的位置
        sharePopupWindow.showAtLocation(activity.findViewById(R.id.detail_dk), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

}
