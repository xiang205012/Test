package com.test.mytest.testWebView;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/1/3.
 */
public class WebHost {

    private Context mContext;

    public WebHost(Context context){
        this.mContext = context;
    }

    /**
     * callJs是js页面中的一个方法
     * 点击此方法时做我们客户端想做的事情
     */
    @JavascriptInterface
    public void callJs(){
        Toast.makeText(mContext,"call from js",Toast.LENGTH_LONG).show();
    }



}
