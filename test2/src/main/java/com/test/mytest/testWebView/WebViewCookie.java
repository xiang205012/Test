package com.test.mytest.testWebView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 同步cookie信息
 * Created by Administrator on 2016/1/3.
 */
public class WebViewCookie extends Thread {


    private static final String TAG = "cookie-->>";

    private Handler mHandler;

    public WebViewCookie(Handler handler){
        this.mHandler = handler;
    }


    @Override
    public void run() {
        HttpClient client = new DefaultHttpClient();

        HttpPost post = new HttpPost("http://192.168.1.101:8080/webs/login.jsp");

        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("name","nates"));
        list.add(new BasicNameValuePair("age","12"));

        try {
            post.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 200){
                AbstractHttpClient absClient = (AbstractHttpClient) client;
                List<Cookie> cookies = absClient.getCookieStore().getCookies();
                for(Cookie cookie : cookies){
                    Log.i(TAG,"name = "+cookie.getName()+" age = "+cookie.getValue());
                    Message msg = mHandler.obtainMessage();
                    msg.obj = cookie;
                    mHandler.sendMessage(msg);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
