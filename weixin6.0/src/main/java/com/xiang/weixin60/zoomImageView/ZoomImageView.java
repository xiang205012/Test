package com.xiang.weixin60.zoomImageView;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * 多点触控的图片，放大缩小
 * Created by Administrator on 2016/4/30.
 */
public class ZoomImageView extends ImageView implements
        ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener,
        View.OnTouchListener {

    /**
     * 首先监听我们要加载的图片的大小，
     * 实现OnGlobalLayoutListener : 意思是当布局加载完毕后调用onGlobalLayout()方法，
     * 我们在此方法中得到图片的大小，如果大了就缩小，小了就放大，并且居中显示
     * 此接口需要注册也需要反注册：onAttachedToWindow(注册) onDetachedFromWindow(反注册)
     *
     *
     */

    /**使onGlobalLayout只调用一次*/
    private boolean isFirst;

    /**初始化时缩放的值*/
    private float mInitScale;
    /**双击放大达到的值*/
    private float mMidScale;
    /**放大的最大值 , 也就是说图片的缩放范围是：mInitScale ~ mMaxScale*/
    private float mMaxScale;
    /**将图片(加载时)进行缩放和平移的Matrix*/
    private Matrix matrix ;
    /**与用户交互时的缩放类(捕获用户多指触控时缩放的比例)*/
    private ScaleGestureDetector mScaleGestureDetector;

///--------------- 以下是自由移动的相关变量
    // 记录上一次多点触控的数量(也就是上一次有多少个手指在屏幕上)
    // 用于确定触控的中心点(两个手指和三个四个手指触控的中心点肯定是不一样的)
    private int mLastPointerCount;
    // 最后一次触控的中心点坐标
    private float mLastX;
    private float mLastY;
    // getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
    // 如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页
    private int mTouchSlop;
    // 能否发生移动
    private boolean isCanDrag;
    /**检测能否左右移动*/
    private boolean isCheckLeftAndRight;
    /**检测能否上下移动*/
    private boolean isCheckTopAndBottom;

///---------------- 双击放大缩小
    //一般情况下，我们知道View类有个View.OnTouchListener内部接口，
    // 通过重写他的onTouch(View v, MotionEvent event)方法，
    // 我们可以处理一些touch事件，但是这个方法太过简单，
    // 如果需要处理一些复杂的手势，用这个GestureDetector
    private GestureDetector mGestureDetector;
    /**如果已经到缩放的过程中，不允许用户再次双击*/
    private boolean isAutoScale;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        matrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(),this);

        setOnTouchListener(this);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            // 双击放大缩小
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // 避免用户疯狂的点击放大缩小
                if(isAutoScale) return true;

                float x = e.getX();
                float y = e.getY();
                if(getCurrentScale() < mMidScale){//放大
//    直接放大       matrix.postScale(mMidScale / getCurrentScale(),mMidScale / getCurrentScale(),x,y);
                    // 缓慢的放大
                    postDelayed(new AutoScaleRunnable(mMidScale,x,y),16);
                    isAutoScale = true;
                }else{//缩小
//    直接缩小       matrix.postScale(mInitScale / getCurrentScale(),mInitScale / getCurrentScale(),x,y);
                    // 缓慢的缩小
                    postDelayed(new AutoScaleRunnable(mInitScale,x,y),16);
                    isAutoScale = true;
                }
                setImageMatrix(matrix);
                return true;
            }
        });
    }

    /**自动缓慢的放大与缩小*/
    private class AutoScaleRunnable implements Runnable{
        /**缩放的目标值*/
        private float mTargetScale;
        // 缩放的中心点坐标
        private float x;
        private float y;
        // 缩放的梯度，每次放大1.07 或缩小0.93，以达到缓慢缩放的效果
        private final float BIGGER = 1.07f;
        private final float SMALL = 0.93f;

        // 临时的变量
        private float tmpScale;

        public AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;
            if(getCurrentScale() < mTargetScale){
                tmpScale = BIGGER;// 放大
            }
            if(getCurrentScale() > mTargetScale){
                tmpScale = SMALL;// 缩小
            }
        }

        @Override
        public void run() {
            matrix.postScale(tmpScale,tmpScale,x,y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(matrix);

            float currentScale = getCurrentScale();
            if((tmpScale > 1.0f && currentScale < mTargetScale)// 放大
                ||(tmpScale < 1.0f && currentScale > mTargetScale))// 缩小
            {
                postDelayed(this,16);
                // 每隔16毫秒执行一次run()方法，直到不能放大或缩小进入else中

            }else{
                // 不能放大或缩小时就设置成目标缩放值
                float scale = mTargetScale / currentScale;
                matrix.postScale(scale,scale,x,y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(matrix);
                isAutoScale = false;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if(!isFirst){
            // 得到控件的宽高
            int width = getWidth();
            int height = getHeight();

            // 得到图片的宽高
            Drawable drawable = getDrawable();
            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();

            // 默认缩放比例
            float scale = 1.0f;
            /**当图片的宽度大于控件的宽度，并且图片的高度小于控件的高度.按宽度比进行缩放*/
            if(dw > width && dh < height){
                scale = width * 1.0f / dw;
            }
            /**当图片的高度大于控件的高度，并且图片的宽度小于控件的宽度.按高度比进行缩放*/
            if(dw < width && dh > height){
                scale = height * 1.0f / dh;
            }
            /**当图片的宽度或高度都大于或小于控件时，取宽高比小的那个进行缩放*/
            if((dw < width && dh < height) || (dw > width && dw > height)){
                scale = Math.min(width * 1.0f / dw,height *1.0f / dh);
            }

            /** 得到初始化时的缩放比例 */
            mInitScale = scale;
            mMidScale = mInitScale * 2;
            mMaxScale = mInitScale * 4;

            // 得到图片放在控件上的偏移量(将图片移动到控件的中心)
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() /2 - dh / 2;
            // 通过setImageMatrix(matrix)来移动，matrix有一系列的post方法(平移，缩放，旋转，斜切等效果)
            matrix.postTranslate(dx,dy);// 平移
            matrix.postScale(mInitScale,mInitScale,width / 2,height / 2);// 以控件中心进行缩放
            setImageMatrix(matrix);


            isFirst = true;
        }
    }

    /**
     * 获取当前图片的缩放值
     * 因为图片在加载时通过Matrix(3*3的一维数组)来缩放的
     * @return
     */
    public float getCurrentScale(){
        float[] values = new float[9];
        matrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    // {{ 实现ScaleGestureDetector接口的方法

    /** 缩放时调用 */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // 获取触控时的缩放比例
        float scale = detector.getScaleFactor();
        // 当前图片的缩放比例
        float currentScale = getCurrentScale();

        if(getDrawable() == null) return true;

        // 缩放范围控制
        // 如果当前的缩放比例小于最大值，手指触控缩放比例大于1.0f,说明是想放大的
        // 如果当前的缩放比例大于最小值，手指触控的缩放比例小于1.0f,说明是想缩小的
        if((currentScale < mMaxScale && scale > 1.0f)
                || (currentScale > mInitScale && scale < 1.0f)){
            // 控制缩放最小值
            if(currentScale * scale < mInitScale){
                scale = mInitScale / currentScale;
            }
            // 控制缩放最大值
            if(currentScale * scale > mMaxScale){
                scale = mMaxScale / currentScale;
            }
            // 以两指间中心进行缩放
            matrix.postScale(scale,scale,detector.getFocusX(),detector.getFocusY());
            // 缩放时检查边界和位置
            checkBorderAndCenterWhenScale();
            setImageMatrix(matrix);
        }

        return true;
    }

    /**在缩放的时候进行边界控制以及位置控制*/
    private void checkBorderAndCenterWhenScale() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;//图片在缩放时边界可能不在屏幕的边界上的偏移量
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();

        if(rectF.width() >= width) {
            if (rectF.left > 0) {
                // 此时图片是超出屏幕右边界的
                deltaX = -rectF.left;
            }
            if (rectF.right < width) {
                deltaX = width - rectF.right;
            }
        }
        if(rectF.height() >= height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top;
            }
            if (rectF.bottom < height) {
                deltaY = height - rectF.bottom;
            }
        }

        // 如果宽度或者高度小于控件的宽或高，则让图片居中
        if(rectF.width() < width){
            deltaX = width / 2 - rectF.right + rectF.width()/2;
        }
        if(rectF.height() < height){
            deltaY = height/2 - rectF.bottom + rectF.height()/2;
        }

        matrix.postTranslate(deltaX,deltaY);
    }

    /**通过matrix获得缩放时图片的宽高*/
    private RectF getMatrixRectF(){
        Matrix rMatrix = matrix;
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        if(drawable != null){
            rectF.set(0,0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            rMatrix.mapRect(rectF);
        }
        return rectF;
    }

    /** 开始缩放 */
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;// 必须返回true,默认返回false;
    }

    /** 结束缩放 */
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
    // }}

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 如果是双击 直接返回 交给 SimpleOnGestureListener中的onDoubleTap()处理
        if(mGestureDetector.onTouchEvent(event)){
            return true;
        }

        // 将手指触控的事件交给ScaleGestureDetector来处理
        mScaleGestureDetector.onTouchEvent(event);

        // ----- 自由移动
        // 多点触控时的中心点坐标
        float x = 0;
        float y = 0;
        // 得到多点触控的数量
        int pointerCount = event.getPointerCount();
        for(int i = 0 ;i < pointerCount; i++){
            x += event.getX(i);
            y += event.getY(i);
        }
        x /= pointerCount;
        y /= pointerCount;

        if(mLastPointerCount != pointerCount){
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = pointerCount;
        RectF rectF = getMatrixRectF();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // 解决嵌入到viewpager中放大后不能平移的冲突，因为viewpager将事件拦截了
                if(rectF.width() > getWidth() || rectF.height() > getHeight()){
                    if(getParent() instanceof ViewPager) {
                        // 请求父容器不要拦截事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(rectF.width() > getWidth() || rectF.height() > getHeight()){
                    if(getParent() instanceof ViewPager) {
                        // 请求父容器不要拦截事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                float dx = x - mLastX;
                float dy = y - mLastY;
                if(!isCanDrag){
                    isCanDrag = isMoveAction(dx,dy);
                }
                if(isCanDrag){
                    if(getDrawable() != null){
                        // 上下左右都能移动
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        // 如果宽度小于控件宽度，不允许横向移动
                        if(rectF.width() < getWidth()){
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        // 如果高度小于控件高度，不允许纵向移动
                        if(rectF.height() < getHeight()){
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }
                        matrix.postTranslate(dx,dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(matrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;

        }

        return true;
    }

    /**当移动时进行边界检查*/
    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int heigth = getHeight();

        if(rectF.top > 0 && isCheckTopAndBottom){
            deltaY = -rectF.top;
        }
        if(rectF.bottom < heigth && isCheckTopAndBottom){
            deltaY = heigth - rectF.bottom;
        }
        if(rectF.left > 0 && isCheckLeftAndRight){
            deltaX = -rectF.left;
        }
        if(rectF.right < width && isCheckLeftAndRight){
            deltaX = width - rectF.right;
        }
        matrix.postTranslate(deltaX, deltaY);

    }

    /**判断是否发生移动*/
    private boolean isMoveAction(float dx,float dy){
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

}
