package cj.library.http;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
/**
 *volley网络请求管理
 * Created by cj on 2015/11/24.
 */
public class RequestManager {

	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;

	public static int default_image;
	public static int failed_image;

	private RequestManager() {
		// no instances
	}

	/**
	 * 初始化RequestQueue和ImageLoader
	 * @param context
	 */
	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context, new OkHttpStack(new OkHttpClient()));

		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
				.getMemoryClass();
		// Use one/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;
		mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
	}

	/**
	 * 获取RequestQueue单例
	 * @return
	 */
	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}

	/**
	 * add时必须添加tag
	 * @param request
	 * 			stringRequest/BeanRequest/ImageRequest...
	 * @param tag
	 */
	public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
		//设置请求超时时间为5秒，重新连接次数为 one 次
		request.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
	}

	/**
	 * 取消网络请求
	 * @param tag 标签
	 */
	public static void cancelAllRequest(Object tag) {
		mRequestQueue.cancelAll(tag);
	}

	/**
	 * 获取ImageLoader单例
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}

	/**
	 * 设置ImageLoader加载时显示的图片和加载失败显示的图片
	 * @param loading   加载中的图片
	 * @param failed	加载失败的图片
	 */
	public static void setImageListenerDrawable(int loading,int failed){
		default_image = loading;
		failed_image = failed;
	}

	/**
	 * 调用前先调用setImageListenerDrawable();
	 * 不设置图片宽高，按原图大小进行加载
	 * @param view	ImageView
	 * @param url
	 */
	public static void displayImage(ImageView view,String url){
		ImageLoader.ImageListener listener = getImageLoader().getImageListener(view,default_image,failed_image);
		getImageLoader().get(url,listener);
	}

	/**
	 * 调用前先调用setImageListenerDrawable();
	 * 设置图片宽高，当超过预设值按预设值进行加载，针对大图效果明显
	 * @param view		ImageView
	 * @param url
	 * @param width		最大宽度
	 * @param height	最大高度
	 */
	public static void displayImage(ImageView view,String url,int width,int height){
		ImageLoader.ImageListener listener = getImageLoader().getImageListener(view,default_image,failed_image);
		getImageLoader().get(url,listener,width,height, ImageView.ScaleType.FIT_XY);
	}

}
