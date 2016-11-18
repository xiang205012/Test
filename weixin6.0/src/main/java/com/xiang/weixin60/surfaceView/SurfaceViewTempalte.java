package com.xiang.weixin60.surfaceView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * SurfaceView extends View
 * 其实View是在UI线程中进行绘制的，
 * 而SurfaceView是在一个子线程中对自己进行绘制，优势：避免造成UI线程阻塞
 * SurfaceView中包含一个专门用于绘制的Surface,Surface中包含一个Canvas
 * 如何获得Canvas:
 *    getHolder --> SurfaceHolder
 *    holder --> Canvas + 管理SurfaceView的生命周期
 *    surfaceCreated
 *    surfaceChanged
 *    surfaceDestoryed
 *
 * Created by Administrator on 2016/3/1.
 */
public class SurfaceViewTempalte extends SurfaceView implements SurfaceHolder.Callback ,Runnable{

    /** 操作SurfaceView的holder */
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    /** 绘制时所在的子线程 */
    private Thread mThread;
    /** 线程开关 */
    private boolean isRunning;


    public SurfaceViewTempalte(Context context) {
        this(context,null);
    }

    public SurfaceViewTempalte(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SurfaceViewTempalte(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取holder
        mHolder = getHolder();
        // 管理surfaceView的生命周期
        mHolder.addCallback(this);

        // 设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);

        // 设置常量
        setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 当surfaceView创建时，初始化 线程
        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
        // new Thread(new Runnable(){ run(真正的绘制工作在此方法中执行) });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        // 不断的绘制
        while(isRunning){
            draw();
        }
    }

    /** 绘制图形 */
    private void draw() {
        try {
            // 获取 Canvas 并锁定它
            mCanvas = mHolder.lockCanvas();
            if(mCanvas != null){
                // TODO draw something

            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            if(mCanvas != null){
                // 释放 Canvas
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }
}
