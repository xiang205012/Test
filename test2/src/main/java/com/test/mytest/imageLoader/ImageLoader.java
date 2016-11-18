package com.test.mytest.imageLoader;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
 * Created by Administrator on 2016/11/14 0014.
 */
public class ImageLoader {

    private static ImageLoader mInstance;
    /**图片缓存*/
    private LruCache<String,Bitmap> mLruCache;
    /**图片加载线程池*/
    private ExecutorService mThreadPool;
    /**默认线程数量*/
    private static final int DEFAULT_THREAD_COUNT = 1;
    /**图片加载方式，LIFO:滑动到哪里就加载此处需要显示的图片，FIFO:从上到下顺序加载*/
    public static final int TYPE_LOAD_LIFO = 0;
    public static final int TYPE_LOAD_FIFO = 1;
    private int mLoadType = TYPE_LOAD_LIFO;
    /**任务队列*/
    private LinkedList<Runnable> mTaskQueue;
    /**后台轮询线程*/
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;
    /**UI线程中的Handler*/
    private Handler mUIHandler;
    /**信号量，由于mPoolThreadHandler是在异步线程中初始化，
     * 避免在多线程时mPoolThreadHandler还没有初始化完成就调用了addTask造成空指针*/
    private Semaphore mPoolThreadHandlerSemaphore = new Semaphore(0);
    /**线程池信号量，以threadCount为基数，
     * 比如：当threadCount为3时，使用信号量阻塞，当第四个线程进来是就要等待，
     * 而不是add一个Task，线程就去取一个造成taskQueue里面可能永远只有一个，取的也可能永远是第一个，
     * 这时图片加载的方式设置就变的没什么用途，
     * 一定要等到线程执行完一个任务后才去取，因为线程操作是耗时的，再这耗时过程中mTaskQueue可能已经添加了100个了，
     * 然后空闲的线程再取就有LIFO,FIFO这样的加载方式了*/
    private Semaphore mPoolThreadSemaphore;

    public static ImageLoader getmInstance(int threadCount,int loadType){
        if(mInstance == null){
            synchronized (ImageLoader.class){
                if(mInstance == null){
                    mInstance = new ImageLoader(threadCount,loadType);
                }
            }
        }
        return mInstance;
    }

    /**
     * @param threadCount 线程数
     * @param loadType 加载方式
     */
    public ImageLoader(int threadCount, final int loadType) {
        mTaskQueue = new LinkedList<>();
        mLoadType = loadType;
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mPoolThreadSemaphore = new Semaphore(threadCount);

        mPoolThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        // 线程池取出一个任务并执行
                        Runnable runnable = null;
                        if(loadType == TYPE_LOAD_FIFO){
                            runnable = mTaskQueue.removeFirst();
                        }else if(loadType == TYPE_LOAD_LIFO){
                            runnable = mTaskQueue.removeLast();
                        }
                        mThreadPool.execute(runnable);
                        try {
                            // 当超过threadCount数量的线程进来时就会阻塞
                            mPoolThreadSemaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Looper.loop();
                // 释放一个信号量
                mPoolThreadHandlerSemaphore.release();
            }
        };
        mPoolThread.start();
        // 设置缓存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        mLruCache = new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 计算每个bitmap占据的内存大小
                return value.getRowBytes() * value.getHeight();
            }
        };


    }

    public void loadImage(final String path, final ImageView imageView){
        // 设置tag，在为imageView设置图片时，要判断此tag，避免因GridView机制造成的图片加载错乱
        imageView.setTag(path);
        if(mUIHandler == null){
            mUIHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    // 获取得到的图片，为imageView回调设置图片
                    ImgHolder holder = (ImgHolder) msg.obj;
                    Bitmap bitmap = holder.bitmap;
                    ImageView imageView = holder.imageView;
                    String path = holder.path;
                    // 将path与getTag存储路径进行比较
                    if(imageView.getTag().toString().equals(path)){
                        imageView.setImageBitmap(bitmap);
                    }
                }
            };
        }

        Bitmap bm = getBitmapFromCache(path);
        if(bm != null){
            Message message = mUIHandler.obtainMessage();
            ImgHolder holder = new ImgHolder();
            holder.bitmap = bm;
            holder.imageView = imageView;
            holder.path = path;
            message.obj = holder;
            mUIHandler.sendMessage(message);
        }else{
            addTaskToQueue(new Runnable(){
                @Override
                public void run() {
                    // 获取图片需要显示的大小
                    ImgSize imgSize = getImgSize(imageView);
                    // 压缩图片
                    Bitmap bitmap = contractionBitmapFromPath(path,imgSize);
                    // 加入缓存
                    addLruCache(path,bitmap);
                    // 通知更新
                    Message message = mUIHandler.obtainMessage();
                    ImgHolder holder = new ImgHolder();
                    holder.bitmap = bitmap;
                    holder.imageView = imageView;
                    holder.path = path;
                    message.obj = holder;
                    mUIHandler.sendMessage(message);

                    // 任务执行完成后释放量，那么下一个线程就会去取下一个任务
                    mPoolThreadSemaphore.release();
                }
            });
        }
    }

    private void addLruCache(String path, Bitmap bitmap) {
        if(getBitmapFromCache(path) == null){
            if (bitmap != null){
                mLruCache.put(path,bitmap);
            }
        }
    }

    private Bitmap contractionBitmapFromPath(String path, ImgSize imgSize) {
        // 获得图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        int simpleSize = getSimpleSize(options,imgSize);
        options.inSampleSize = simpleSize;
        // 使用获得的inSimpleSize再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        return bitmap;
    }
    /**获取压缩比例，越大压缩的越小*/
    private int getSimpleSize(BitmapFactory.Options options, ImgSize imgSize) {
        int width = options.outWidth;
        int height = options.outHeight;
        int simpleSize = 1;
        if(width > imgSize.width || height > imgSize.height){
            int rWidth = Math.round(width * 1.0f / imgSize.width);
            int rHeight = Math.round(height * 1.0f / imgSize.height);
            simpleSize = Math.max(rWidth ,rHeight);
        }
        return simpleSize;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private ImgSize getImgSize(ImageView imageView) {
        DisplayMetrics displayMetrics = imageView.getResources().getDisplayMetrics();
        // 获取imageView的宽高压缩图片
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        int width = imageView.getWidth();
        if(width <= 0){// 如果getWidth小于等于0，就拿imageView在布局中设置的宽
            width = layoutParams.width;
        }
        if(width <= 0){// 如果在布局中的宽还是小于等于0(可能是设置成wrap_content)，就拿imageView最大宽度
//            width = imageView.getMaxWidth();// api 16的方法
            width = getFieldMaxValue(imageView,"mMaxWidth");
        }
        if(width <= 0){// 如果最大宽度还是小于等于0 ，就设置成屏幕宽度
            width = displayMetrics.widthPixels;
        }
        int height = imageView.getHeight();
        if(height <= 0){// 如果getWidth小于等于0，就拿imageView在布局中设置的宽
            height = layoutParams.height;
        }
        if(height <= 0){// 如果在布局中的宽还是小于等于0(可能是设置成wrap_content)，就拿imageView最大宽度
//            height = imageView.getMaxHeight();
            height = getFieldMaxValue(imageView,"mMaxHeight");
        }
        if(height <= 0){// 如果最大宽度还是小于等于0 ，就设置成屏幕宽度
            height = displayMetrics.heightPixels;
        }

        ImgSize imgSize = new ImgSize();
        imgSize.width = width;
        imgSize.height = height;

        return imgSize;
    }
    // 反射获取某个类某个属性值
    private int getFieldMaxValue(Object object,String fieldName){
        int value = 0;
        try {
            Field declaredField = object.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            int fieldValue = declaredField.getInt(object);
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


    private synchronized void addTaskToQueue(Runnable runnable) {
        mTaskQueue.add(runnable);
        // 添加到任务队列，并通知线程池执行runnable
        // 使用信号量控制多线程时mPoolThreadHandler可能为空
        try {
            if (mPoolThreadHandler == null){
                // 如果是空，请求一下，自己wait，等待semaphore.release()通知，然后继续执行后面的代码
                mPoolThreadHandlerSemaphore.acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(0x00123);
    }

    private Bitmap getBitmapFromCache(String path) {
        return mLruCache.get(path);
    }

    // 因使用handler发消息，此时不一定及时的响应，就会因GridView机制而造成错乱
    private class ImgHolder{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    private class ImgSize{
        int width;
        int height;
    }

}
