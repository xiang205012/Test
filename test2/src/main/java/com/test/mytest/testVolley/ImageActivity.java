package com.test.mytest.testVolley;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.test.mytest.R;

import java.io.File;

import cj.library.utils.BitmapUtils;

/**
 * Created by Administrator on 2015/8/12.
 */
public class ImageActivity extends VolleyBaseActivity implements View.OnClickListener {

    private ImageView imageView;
    private NetworkImageView netimageView;
    private RequestQueue mQueue;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volley_activity);

        mQueue = Volley.newRequestQueue(this);

        RequestManager.init(this);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageRequest();

        netimageView = (NetworkImageView) findViewById(R.id.netimageView);
        testImageLoader();

        //testXutils();

        button = (Button) findViewById(R.id.get_json);
        button.setOnClickListener(this);


    }



    private void imageRequest() {

        ImageRequest request = new ImageRequest("http://bbs.lidroid.com/static/image/common/logo.png",
                new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
       // mQueue.add(request);
        executeRequest(request);
//        executeRequest(new ImageRequest("fdfsfsdf", new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap bitmap) {
//
//            }
//        }, 0, 0, Bitmap.Config.RGB_565,errorListener()));
    }

    private void testImageLoader() {
        ImageLoader imageLoader = new ImageLoader(mQueue, new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }

            @Override
            public Bitmap getBitmap(String url) {

                return null;

            }

        });
        /**
         *  ImageListener listener = ImageLoader.getImageListener(imageView,R.drawable.default_image, R.drawable.failed_image);

         imageLoader.get("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg", listener);

         imageLoader.get("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg", listener, 200, 200);
         */
        netimageView.setImageUrl("http://bbs.lidroid.com/static/image/common/logo.png",

                imageLoader);
    }

    private void testXutils() {
        BitmapUtils bitmapUtils = new BitmapUtils();
//        bitmapUtils.display(imageView, "http://218.19.126.97:8080/p1525070683.jpg");
    }

    /**
     * 判断缓冲目录是否存在
     * @param context
     * @param fileName
     * @return
     */
    public static boolean isCacheExist(Context context, String fileName) {
        File dir = context.getCacheDir();
        //File file = new File(dir + File.separator + CACHE_DIR + File.separator + fileName);
//        if (file.exists()) {
//            return true;
//        }
        return false;
    }

    /**
     * 删除该路径下面的所有文件和文件夹
     * @param
     */
    public static void delectCache(String path){
        File file = new File(path);
        if(!file.exists()){
            return;
        }

        if(file.isDirectory()){
            for(File child : file.listFiles()){
                if(child.isDirectory()){
                    delectCache(child.getAbsolutePath());
                }else{
                    child.delete();
                }
            }
        }

        file.delete();
    }

    /**
     * 测试GsonRequest
     * @param v
     */
    @Override
    public void onClick(View v) {
        GsonRequest<Weather> gsonRequest = new GsonRequest<Weather>(
                "http://www.weather.com.cn/data/sk/101010100.html", Weather.class,
                new Response.Listener<Weather>() {
                    @Override
                    public void onResponse(Weather weather) {
                        WeatherInfo weatherInfo = weather.getWeatherinfo();
                        Log.i("TAG", "city is " + weatherInfo.getCity());
                        Log.i("TAG", "temp is " + weatherInfo.getTemp());
                        Log.i("TAG", "time is " + weatherInfo.getTime());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(gsonRequest);
    }
}
