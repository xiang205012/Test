package com.gordon.rrod.Sample.http;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.gordon.rrod.Sample.api.RestApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gordon on 2016/5/30.
 */
public class HttpControl {

    private RestApi restApi;
    private OkHttpClient client;
    Retrofit mRetrofit;
    private static final int TIMEOUT = 15;//连接超时时间，秒

    private static HttpControl mInstance;

    public static HttpControl getmInstance(){
        if(mInstance == null){
            synchronized (HttpControl.class){
                if (mInstance == null)
                    mInstance = new HttpControl();
            }
        }
        return mInstance;
    }

    public HttpControl(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .addNetworkInterceptor(new RequestInterceptor())
                .cookieJar(new CookieJar() {
                    private final Map<String,List<Cookie>> cookieStore = getNetMap();

                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
                        ArrayList<String> out = regular(httpUrl.url().toString(),"^(http://).*[?]");
                        boolean isSaveCookie = false;
                        for(Cookie cookie : cookies){
                            if(cookie.name().equals("ASP.NET_SessionId")){
                                isSaveCookie = true;
                            }
                        }
                        if(isSaveCookie && out != null &&
                                out.size() > 0 && cookies != null){
                            cookieStore.put(out.get(0),cookies);
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        ArrayList<String> out = regular(httpUrl.url().toString(),"^(http://).*[?]");
                        if(out != null && out.size() > 0){
                            List<Cookie> outList = cookieStore.get(out.get(0));
                            return outList == null ? new ArrayList<Cookie>() : outList;
                        }else {
                            return new ArrayList<Cookie>();
                        }
                    }
                })
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(RestApi.SERVICE_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        restApi = mRetrofit.create(RestApi.class);

    }

    public RestApi getRestApi(){
        return restApi;
    }

    public class RequestInterceptor implements Interceptor{
        //OKHttp 可以处理重定向中出现空格的情况
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            if(response.code() == 302){// 重定向
                String loaction = response.header("Loaction");
                loaction = regularReplaceAll(loaction, " ", "%20");//空格替换成百分号
                Response response1 = response.newBuilder()
                        .removeHeader("Loaction")
                        .addHeader("Loaction",loaction)
                        .build();
                return response1;

            }
            return response;
        }
    }

    public String regularReplaceAll(String input,String regex,String replace){
        String out = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        out = matcher.replaceAll(replace);
        return out;
    }

    public Map getNetMap(){
        return new ConcurrentHashMap();
    }

    public ArrayList<String> regular(String text,String expression){
        ArrayList<String> out = new ArrayList<>();
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()){
            listAdd(out,matcher.group());
        }
        return out;
    }

    private void listAdd(List out, Object group) {
        if(out == null || group == null || out.contains(group)){
            throw new IllegalArgumentException("not parameter");
        }
        out.add(group);
    }

}
