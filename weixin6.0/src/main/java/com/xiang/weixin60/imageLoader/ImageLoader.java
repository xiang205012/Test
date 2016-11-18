package com.xiang.weixin60.imageLoader;


import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 图片加载类
 * Created by Administrator on 2016/5/4.
 */
public class ImageLoader {

    private static ImageLoader mImageLoader;
    /**图片缓存的核心对象*/
    private LruCache<String,Bitmap> mLruCache;
    /**线程池*/
    private ExecutorService mThreadPool;
    /**默认线程池中的线程数量*/
    private static final int DEAFULT_THREAD_COUNT = 1;
    /**任务队列的调度方式,默认是拉到那个地方，那个地方的图片先显示*/
    private Type mType = Type.LIFO;
    /**调度方式，显示方式，*/
    public enum Type{
        LIFO,// (后进先出)拉到那个地方，那个地方的图片先显示
        FIFO; // (先进先出)按顺序显示，前面的没显示完后面的不会显示
    }
    /**任务队列，供线程池取出并执行任务*/
    private LinkedList<Runnable> mTaskQueue;
    /**后台轮询线程*/
    private Thread mPoolThread;
    /**后台轮询线程的Handler*/
    private Handler mPoolThreadHandler;
    /**主线程的Handler*/
    private Handler mUIHandler;
    /**解决并发的问题，主要是addTask()中使用了poolThreadHandler避免init中还没有初始化就使用了*/
    private Semaphore semaphorePoolThreadHandler = new Semaphore(0);

    /**避免在任务队列中取任务时发生意外*/
    private Semaphore semaphorePoolThread;

    /**私有化构造方法，不让外界直接new,同时让用户指定线程池的线程数和显示的方式*/
    private ImageLoader(int threadCount ,Type type){
        init(threadCount,type);
    }
    /**初始化操作*/
    private void init(int threadCount, Type type) {
        mPoolThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        // 去线程池取出并执行任务
                        mThreadPool.execute(getTask());
                        try {
                            semaphorePoolThread.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                semaphorePoolThreadHandler.release();// 通知addTask()方法中可以执行sendMessage了
                Looper.loop();
            }
        };
        mPoolThread.start();
        // 获取应用最大的内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 定义缓存内存的大小
        int cacheMemory = maxMemory / 8;
        // 初始化缓存类
        mLruCache = new LruCache<String,Bitmap>(cacheMemory){
            /**每个Bitmap所占的内存*/
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // value.getRowBytes():每行所占据的字节数，乘以它的高度就是所占的内存大小
                return value.getRowBytes() * value.getHeight();
            }
        };

        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<Runnable>();
        mType = type;
        semaphorePoolThread = new Semaphore(threadCount);
    }

    /**从任务队列取出任务*/
    private Runnable getTask() {
        if(mType == Type.FIFO){
            return mTaskQueue.removeFirst();
        }else if(mType == Type.LIFO){
            return mTaskQueue.removeLast();
        }
        return null;
    }

    public static ImageLoader newInstance(int threadCount ,Type type){
        if(mImageLoader == null){
            synchronized (ImageLoader.class){
                if(mImageLoader == null){
                    mImageLoader = new ImageLoader(threadCount,type);
                }
            }
        }
        return mImageLoader;
    }

    /**根据path为imageview设置图片*/
    public void loadImage(final String path, final ImageView imageView){
        // 避免对此调用产生混乱
        imageView.setTag(path);
        if(mUIHandler == null){
            mUIHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    // 获取图片，为imageView回调设置图片
                    ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageView = holder.imageView;
                    String path = holder.path;
                    // 将path与getTag存储路径进行比较
                    if(imageView.getTag().toString().equals(path)){
                        imageView.setImageBitmap(bm);
                    }
                }
            };
        }
        // 根据path在缓存中获取bitmap
        Bitmap bm = getBitmapFromLruCache(path);
        if(bm != null){
            refreshBitmap(path, imageView, bm);
        }else{
            // 缓存中没有就添加到队列中
            addTask(new Runnable() {
                @Override
                public void run() {
                    // 加载图片，图片压缩
                    // 1.获取图片需要显示的大小(也就是ImageView设置的大小)
                    ImageSize imageSize = getImageViewSize(imageView);
                    // 2.压缩图片
                    Bitmap bitmap = decodeSampedBitmapFromPath(path, imageSize);
                    // 3.把图片加入到缓存
                    addBitmapToLruCache(path, bitmap);
                    refreshBitmap(path, imageView, bitmap);
                    semaphorePoolThread.release();
                }
            });
        }

    }

    private void refreshBitmap(String path, ImageView imageView, Bitmap bm) {
        Message msg = Message.obtain();
        ImgBeanHolder holder = new ImgBeanHolder();
        holder.bitmap = bm;
        holder.path = path;
        holder.imageView = imageView;
        msg.obj = holder;
        mUIHandler.sendMessage(msg);
    }

    /**将图片加入缓存*/
    private void addBitmapToLruCache(String path, Bitmap bm) {
        if(getBitmapFromLruCache(path) == null){
            if(bm != null){
                mLruCache.put(path,bm);
            }
        }
    }

    /**根君图片需要显示的宽和高对图片进行压缩*/
    private Bitmap decodeSampedBitmapFromPath(String path, ImageSize imageSize) {
        // 获得图片的宽和高，
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//并不把图片加载到内存中
        BitmapFactory.decodeFile(path, options);//获取到图片的真实宽高从而压缩
        // options.inSampleSize图片压缩的关键变量
        options.inSampleSize = caculateInSampleSize(options,imageSize);
        // 使用获得到的InSampleSize再次解析图片
        options.inJustDecodeBounds = false;// 加载到内存中
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        return bitmap;
    }

    /**根据需求的宽和高以及图片的实际宽和高计算inSampleSize*/
    private int caculateInSampleSize(BitmapFactory.Options options, ImageSize imageSize) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;// 默认为1
        // 比较
        if(width > imageSize.width || height > imageSize.height){
            // 宽度压缩比例 Math.round ：四舍五入取整 如：Math.round(-8.6) = -9;
            int widthRadio = Math.round(width * 1.0f / imageSize.width);
            // 高度压缩比例
            int heightRadio = Math.round(height * 1.0f / imageSize.height);
            // 得到最终的压缩比例，这里为减少内存以最大的比例压缩，当然为考虑图片的失真度也可以使用Math.min()
            inSampleSize = Math.max(widthRadio,heightRadio);
        }

        return inSampleSize;
    }

    /**根据imageView获取适当的压缩的宽和高*/
    //@SuppressWarnings("NewApi")
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        int width = imageView.getWidth();// 获取imageView的实际宽度
        if(width <= 0){
            width = lp.width;// 获取imageView在layout中声明的宽度
        }
        if(width <= 0){
//            width = imageView.getMaxWidth();// 检查最大值 api 16以上
            // 由于imageView.getMaxWidth();是API 16以上，为兼容低版本使用反射获取
            width = getImageViewFieldValue(imageView,"mMaxWidth");
        }
        if(width <= 0){
            // 最不幸的情况就是屏幕的宽度
            DisplayMetrics displayMetrics = imageView.getContext()
                    .getResources().getDisplayMetrics();
            width = displayMetrics.widthPixels;
        }
        int height = imageView.getHeight();// 获取imageView的实际高度
        if(height <= 0){
            height = lp.height;// 获取imageView在layout中声明的高度
        }
        if(height <= 0){
//            height = imageView.getMaxHeight();// 检查最大值 api 16以上
            // 由于imageView.getMaxHeight();是API 16以上，为兼容低版本使用反射获取
            height = getImageViewFieldValue(imageView,"mMaxHeight");
        }
        if(height <= 0){
            // 最不幸的情况就是屏幕的高度
            DisplayMetrics displayMetrics = imageView.getContext()
                    .getResources().getDisplayMetrics();
            height = displayMetrics.heightPixels;
        }
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    /**
     * 通过反射获取某个对象的属性值
     * @param object 对象
     * @param fieldName  属性
     * @return
     */
    private int getImageViewFieldValue(Object object,String fieldName){
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if(fieldValue > 0 && fieldValue < Integer.MAX_VALUE){
                value = fieldValue;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return value;
    }

    /**添加任务*/
    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
        if(mPoolThreadHandler == null){
            try {
                // 如果mPoolThreadHandler初始化没有完成让sendMessage等着
                semaphorePoolThreadHandler.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 发送后台消息
        mPoolThreadHandler.sendEmptyMessage(0x111);
    }

    /**根据path在缓存中获取bitmap*/
    private Bitmap getBitmapFromLruCache(String key) {
        return mLruCache.get(key);
    }

    /**缓存，避免造成错乱*/
    private class ImgBeanHolder{
        Bitmap bitmap;
        String path;
        ImageView imageView;
    }

    private class ImageSize{
        int width;
        int height;
    }

}
