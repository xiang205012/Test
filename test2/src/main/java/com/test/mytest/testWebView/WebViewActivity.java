package com.test.mytest.testWebView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.test.mytest.MainActivity;
import com.test.mytest.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/12/29.
 */
public class WebViewActivity extends Activity {

    private static final String TAG = "WebView--->>";
    @InjectView(R.id.btn_webview_back)
    Button btn_back;
    @InjectView(R.id.btn_webview_refresh)
    Button btn_refresh;
    @InjectView(R.id.tv_webview_title)
    TextView webTitle;
    @InjectView(R.id.web_view)
    WebView webView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String cookie = msg.obj.toString();
            CookieSyncManager.createInstance(WebViewActivity.this);
            // API不支持一下代码，需另想办法，降低API版本
            //CookieManager manager = CookieManager.getInstance();
            //manager.setAcceptCookie(true);
            //manager.setCookie("http:192.168.1.101:8080/webs", cookie);
            //CookieSyncManager.getInstance().sync();
            webView.loadUrl("http://192.168.1.101:8080/webs/login.jsp");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.inject(this);

//        webView.loadUrl("http://www.baidu.com");
        webView.loadUrl("http://shouji.baidu.com/");

        //webView设置与js交互
        webView.getSettings().setJavaScriptEnabled(true);
        //WebHost是自定义的与js交互的相关类
        //特别注意：在混淆代码时不要把js交互类混淆了，否则无法调用
        webView.addJavascriptInterface(new WebHost(this),"js");

        //webView与服务器同步cookie
        new WebViewCookie(handler).start();

        //webView默认是打开系统的浏览器加载网页，
        // 如果想自己的app加载就需要webView.setWevViewClient();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
            //如果webView加载失败，不要直接显示(难看)，而是加载assets目录下的一个美工做的错误页面
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //加载本地错误页面，还有一种是直接写布局代替错误页
                view.loadUrl("file:///android_assets/error.html");
                //textView.setText("网页加载错误");
            }
        });
        //如果想通过自定义html协议来做相应的操作，跟js前端人员沟通如果定义协议（此方式可以避免js远程注入的问题）
        // <a href="http://192.168.1.103:8080/webs/error.html?startActivity"> load page </a>
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //自定义协议
                if(url.endsWith("startActivity")){
                    WebViewActivity.this.startActivity(new Intent(WebViewActivity.this, MainActivity.class));
                    return true;//不要忘了返回true
                }
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
            //如果webView加载失败，不要直接显示(难看)，而是加载assets目录下的一个美工做的错误页面
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //加载本地错误页面，还有一种是直接写布局代替错误页
                view.loadUrl("file:///android_assets/error.html");
                //textView.setText("网页加载错误");
            }
        });
        //如果想设置网页的标题title
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                webTitle.setText(title);
                super.onReceivedTitle(view, title);
            }
        });
        //如果想通过网页下载文件
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Log.i(TAG,"URL : " + url );
                if(url.endsWith(".apk")) {
                    //自定义下载
                    new WebThread(url).start();

                    //调用系统方式下载，比上面快一点
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }
            }
        });


    }

    @OnClick({R.id.btn_webview_refresh,R.id.btn_webview_back})
    public void click(View view){
        switch(view.getId()){
            case R.id.btn_webview_back:
                finish();
                break;
            case R.id.btn_webview_refresh:
                //刷新，重新加载
                webView.reload();
                break;
        }
    }


}
