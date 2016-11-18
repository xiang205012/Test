package com.xiang.weixin60.surfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.xiang.weixin60.R;

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
public class LuckyPan extends SurfaceView implements SurfaceHolder.Callback ,Runnable{

    /** 操作SurfaceView的holder */
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    /** 绘制时所在的子线程 */
    private Thread mThread;
    /** 线程开关 */
    private boolean isRunning;

    /**盘块上的文字*/
    private String[] titles = new String[]{"单反相机","IPAD","恭喜发财","IPHONE","服装一套","恭喜发财"};
    /**盘块上的图片*/
    private int[] imgs = new int[]{R.drawable.danfan,R.drawable.ipad,R.drawable.f040,R.drawable.iphone,R.drawable.meizi,R.drawable.f040};
    /**各个盘块图片对应的Bitmap*/
    private Bitmap[] bitmaps;
    /**盘块背景颜色*/
    private int[] mColor = new int[]{0xFFFFC300,0XFFF17E01,0xFFFFC300,0XFFF17E01,0xFFFFC300,0XFFF17E01};
    /**主背景图片*/
    private Bitmap mainBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.surfaceview_bg);

    /**盘块的数量*/
    private int mItemCount = 6;
    /**整个盘块的范围*/
    private RectF mRange = new RectF();
    /**整个盘块的半径*/
    private int mRadius;
    /**绘制盘块的画笔*/
    private Paint mArcPaint;
    /**绘制文字的画笔*/
    private Paint mTextPaint;
    /**绘制文字的大小*/
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,20,getResources().getDisplayMetrics());
    /**盘块滚动的速度*/
    private double mSpeed = 5;
    /**盘块滚动的起始角度*/
    private volatile int mStartAngle = 0;
    /**是否点击了停止按钮*/
    private boolean isShouldEnd;
    /**转盘的中心位置*/
    private int mCenter;
    /**当在布局中设置了Padding直接以paddingLeft为准*/
    private int mPadding;

    public LuckyPan(Context context) {
        this(context,null);
    }

    public LuckyPan(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyPan(Context context, AttributeSet attrs, int defStyleAttr) {
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 强制将转盘放在一个正方形内
        int width = Math.min(getMeasuredWidth(),getMeasuredHeight());
        mPadding = getPaddingLeft();
        // 直径
        mRadius = width - mPadding * 2;
        // 中心点
        mCenter = width / 2;

        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 初始化盘块的画笔
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setDither(true);
        // 初始化文字的画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(mTextSize);
        // 初始化盘块的范围
        mRange = new RectF(mPadding,mPadding,mPadding + mRadius,mPadding + mRadius);
        // 初始化图片
        bitmaps = new Bitmap[mItemCount];
        for(int i = 0 ;i < mItemCount;i++){
            bitmaps[i] = BitmapFactory.decodeResource(getResources(),imgs[i]);
        }

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
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            // 控制线程绘制的时间在50毫秒以上
            if(end - start < 50){
                try {
                    Thread.sleep(50 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** 绘制图形 */
    private void draw() {
        try {
            // 获取 Canvas 并锁定它
            mCanvas = mHolder.lockCanvas();
            if(mCanvas != null){
                // TODO draw something

                // 绘制背景
                drawBg();

                // 绘制盘块
                float tmpAngle = mStartAngle;
                float sweepAngle = 360 / mItemCount;
                for(int i = 0;i < mItemCount;i++){
                    mArcPaint.setColor(mColor[i]);
                    // 绘制盘块
                    mCanvas.drawArc(mRange,tmpAngle,sweepAngle,true,mArcPaint);
                    // 绘制文本
                    drawText(tmpAngle, sweepAngle, titles[i]);

                    // 绘制图片
                    drawIcon(tmpAngle,bitmaps[i]);

                    tmpAngle += sweepAngle;
                }

                mStartAngle += mSpeed;
                if(isShouldEnd){// 如果点击了停止按钮，让转盘缓缓的回退停止
                    mSpeed -= 1;
                }
                if(mSpeed <= 0){
                    mSpeed = 0;
                    isShouldEnd = false;
                }

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

    /**启动转盘*/
    public void luckyStart(){
        mSpeed = 10;
        isShouldEnd = false;
    }
    /**停止转盘*/
    public void luckyEnd(){
        isShouldEnd = true;
    }
    /**转盘是否正在转动*/
    public boolean isStart(){
        return mSpeed != 0;
    }
    /**转盘当前状态*/
    public boolean isShouldEnd(){
        return isShouldEnd;
    }

    /**绘制图片*/
    private void drawIcon(float tmpAngle, Bitmap bitmap) {
        int imgWidth = mRadius / 8;
        float angle = (float) ((tmpAngle + 360 / mItemCount / 2) * Math.PI / 180);
        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));
        // 确定图片位置
        Rect rect = new Rect(x - imgWidth / 2,y - imgWidth / 2,x + imgWidth / 2,y + imgWidth / 2);
        mCanvas.drawBitmap(bitmap,null,rect,null);

    }

    /**绘制文本*/
    private void drawText(float tmpAngle, float sweepAngle, String title) {
        Path path = new Path();
        path.addArc(mRange,tmpAngle,sweepAngle);
        // 利用水平平移量让文字居中
        int textWidth = (int) mTextPaint.measureText(title);
        // 水平偏移量 = 盘块的弧长/2 - 文字的长度/2
        int hOffset = (int) (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);
        int vOffset = mRadius / 2 / 6;//垂直偏移量
        mCanvas.drawTextOnPath(title,path,hOffset,vOffset,mTextPaint);
    }

    /**绘制背景*/
    private void drawBg() {
        mCanvas.drawColor(0xFFFFFFFF);
        mCanvas.drawBitmap(mainBitmap,null,new Rect(mPadding/2,mPadding/2,
                getMeasuredWidth()-mPadding/2,getMeasuredHeight()- mPadding/2),null);

    }
}
