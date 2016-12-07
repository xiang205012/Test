package com.gordon.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by ${gordon} on 2016/11/23 0023.
 */

public class ImageLoader {

    private int threadCount;
    public static final int TYPE_LIFO = 0;
    public static final int TYPE_FIFO = 1;
    public int loadType = TYPE_LIFO;
    private ExecutorService threadPool;
    private Handler taskHandler;
    private Handler uiHandler;
    private LinkedList<Runnable> tasks = new LinkedList<>();
    private LruCache<String,Bitmap> mCache;

    private static ImageLoader mInstance;

    private Semaphore taskHandlerSemaphore;
    private Semaphore threadPoolSemaphore;

    public ImageLoader(int threadCount, int loadType) {
        this.threadCount = threadCount;
        this.loadType = loadType;
        taskHandlerSemaphore = new Semaphore(0);
        threadPoolSemaphore = new Semaphore(threadCount);
        threadPool = Executors.newFixedThreadPool(threadCount);
        HandlerThread taskThread = new HandlerThread(ImageLoader.class.getName());
        taskThread.start();
        taskHandler = new Handler(taskThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                threadPool.execute(getRunnable());
                try {
                    threadPoolSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        taskHandlerSemaphore.release();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        mCache = new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

    }

    private Runnable getRunnable() {
        if(loadType == TYPE_LIFO){
            return tasks.removeLast();
        }else if(loadType == TYPE_FIFO){
            return tasks.removeFirst();
        }
        return null;
    }

    public static ImageLoader getmInstance(int threadCount,int loadType){
        if(mInstance == null){
            synchronized (ImageLoader.class){
                if (mInstance == null){
                    mInstance = new ImageLoader(threadCount,loadType);
                }
            }
        }
        return mInstance;
    }

    public void loadImage(final String path, final ImageView imageView){
        imageView.setTag(path);
        if(uiHandler == null){
            uiHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    ViewHolder holder = (ViewHolder) msg.obj;
//                    Log.d("tag"," imageView.getTag : " + imageView.getTag().toString()
//                                    + " \nholder.imageView.getTag : " + holder.imageView.getTag().toString()
//                                    + " \npath : " + path);
                    if(holder.imageView.getTag().toString().equals(holder.path)) {
                        holder.imageView.setImageBitmap(holder.bitmap);
                    }
                }
            };
        }

        final Bitmap bitmap = getBitmapFromCache(path);

        if(bitmap != null){
            Message message = uiHandler.obtainMessage();
            ViewHolder holder = new ViewHolder();
            holder.imageView = imageView;
            holder.bitmap = bitmap;
            holder.path = path;
            message.obj = holder;
            uiHandler.sendMessage(message);
        }else {
            addTaskToQueue(new Runnable(){
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {

                    ImageSize size = getImageViewSize(imageView);
                    Bitmap bit = yasoutupian(path,size);
                    addToCache(path,bit);
                    ViewHolder holder = new ViewHolder();
                    holder.imageView = imageView;
                    holder.bitmap = bit;
                    holder.path = path;
                    Message msg = uiHandler.obtainMessage();
                    msg.obj = holder;
                    uiHandler.sendMessage(msg);
                    threadPoolSemaphore.release();
                }
            });
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize size = new ImageSize();
        DisplayMetrics displayMetrics = imageView.getResources().getDisplayMetrics();
        int width = imageView.getWidth();
        int height = imageView.getHeight();
        if(width <= 0){
            width = imageView.getMeasuredWidth();
        }
        if(width <= 0){
            width = imageView.getMaxWidth();
        }
        if(width <= 0){
            width = displayMetrics.widthPixels;
        }
        if(height <= 0){
            height = imageView.getMeasuredHeight();
        }
        if(height <= 0){
            height = imageView.getMaxHeight();
        }
        if(height <= 0){
            height = displayMetrics.heightPixels;
        }
        size.width = width;
        size.height = height;

        return size;
    }

    private Bitmap yasoutupian(String path,ImageSize size) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        int sampleSize = 1;
        if(options.outWidth > size.width || options.outHeight > size.height){
            int rW = Math.round(options.outWidth * 1.0f / size.width);
            int rH = Math.round(options.outHeight * 1.0f / size.height);
            sampleSize = Math.max(rW,rH);
        }
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        return bitmap;
    }

    private void addToCache(String path,Bitmap bit) {
        if(mCache.get(path) == null){
            mCache.put(path,bit);
        }
    }

    private void addTaskToQueue(Runnable runnable) {
        tasks.add(runnable);

        try {
            if(taskHandler == null) {
                taskHandlerSemaphore.acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        taskHandler.sendEmptyMessage(0x988);
    }

    private Bitmap getBitmapFromCache(String path) {
        return mCache.get(path);
    }

    class ViewHolder{
        String path;
        ImageView imageView;
        Bitmap bitmap;
    }

    class ImageSize{
        int width;
        int height;
    }
}
